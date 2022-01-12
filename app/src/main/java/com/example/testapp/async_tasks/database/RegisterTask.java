package com.example.testapp.async_tasks.database;

import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.R;
import com.example.testapp.constants.StringConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class RegisterTask extends AsyncTask implements StringConstants {
    private Fragment fragment;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            fragment = (Fragment) objects[0];
            String username = (String) objects[1];
            String email = (String) objects[2];
            String password = (String) objects[3];

            String data = encodeStrings(
                    new String[]{"username", "email", "password"},
                    new String[]{username, password, email}
            );

            URL url = new URL(REGISTER_LINK);
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
            return REGISTER_PROBLEMS;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if (o.equals(REGISTER_SUCCESS)) {
            NavHostFragment
                    .findNavController(fragment)
                    .navigate(R.id.action_register_to_login);
        }
        Toast.makeText(fragment.getActivity(), (String) o, Toast.LENGTH_SHORT).show();
    }
}
