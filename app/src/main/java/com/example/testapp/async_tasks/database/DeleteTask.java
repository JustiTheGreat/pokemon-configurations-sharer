package com.example.testapp.async_tasks.database;

import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.R;
import com.example.testapp.StringConstants;
import com.example.testapp.data_objects.Pokemon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class DeleteTask extends AsyncTask implements StringConstants {
    private Fragment fragment;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            fragment = (Fragment) objects[0];
            Pokemon pokemon = (Pokemon) objects[1];

            String data = encodeStrings(new String[]{"id_pokemon"}, new String[]{"" + pokemon.getID()});

            URL url = new URL(DELETE_POKEMON_LINK);
            URLConnection conn;
            try {
                conn = url.openConnection();
            } catch (IOException e1) {
                return SERVER_IS_OFFLINE;
            }
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            if ((line = reader.readLine()) != null) sb.append(line);

            return sb.toString();
        } catch (IOException e) {
            return DELETE_PROBLEMS;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if (o.equals(DELETE_SUCCESS)) {
            NavHostFragment
                    .findNavController(fragment)
                    .navigate(R.id.action_details_to_collection);
        }
        Toast.makeText(fragment.getActivity(), (String) o, Toast.LENGTH_SHORT).show();
    }
}
