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

public class UpdateTask extends AsyncTask implements StringConstants {
    private Fragment fragment;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            fragment = (Fragment) objects[0];
            Pokemon pokemon = (Pokemon) objects[1];

            StringBuilder ivs = new StringBuilder();
            StringBuilder evs = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                ivs.append(":").append(pokemon.getIVs().get(i));
                evs.append(":").append(pokemon.getEVs().get(i));
            }
            ivs = new StringBuilder(ivs.substring(1));
            evs = new StringBuilder(evs.substring(1));

            String data = encodeStrings(
                    new String[]{"id_pokemon", "name", "species", "gender", "level", "ability", "nature", "move1", "move2", "move3", "move4", "ivs", "evs"},
                    new String[]{
                            "" + pokemon.getID(),
                            pokemon.getName(),
                            pokemon.getSpecies(),
                            pokemon.getGender(),
                            "" + pokemon.getLevel(),
                            pokemon.getAbility().getName(),
                            pokemon.getNature().getName(),
                            pokemon.getMoves().get(0) == null ? "" : pokemon.getMoves().get(0).getName(),
                            pokemon.getMoves().get(1) == null ? "" : pokemon.getMoves().get(1).getName(),
                            pokemon.getMoves().get(2) == null ? "" : pokemon.getMoves().get(2).getName(),
                            pokemon.getMoves().get(3) == null ? "" : pokemon.getMoves().get(3).getName(),
                            ivs.toString(),
                            evs.toString(),
                    }
            );

            URL url = new URL(UPDATE_POKEMON_LINK);
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
            return UPDATE_PROBLEMS;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if (o.equals(UPDATE_SUCCESS)) {
            NavHostFragment
                    .findNavController(fragment)
                    .navigate(R.id.action_add_to_collection);
        }
        Toast.makeText(fragment.getActivity(), (String) o, Toast.LENGTH_SHORT).show();
    }
}
