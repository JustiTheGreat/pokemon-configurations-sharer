package com.example.testapp.async_tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.LoggedUser;
import com.example.testapp.R;
import com.example.testapp.StringConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class LoginTask extends AsyncTask implements StringConstants {
    private Fragment fragment;
    private String username;
    private String password;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            fragment = (Fragment) objects[0];
            username = (String) objects[1];
            password = (String) objects[2];

            String data = encodeStrings(
                    new String[]{"username", "password"},
                    new String[]{username, password}
            );

            URL url = new URL(LOGIN_LINK);
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

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();
        } catch (IOException e) {
            return LOGIN_PROBLEMS;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        Toast toast = Toast.makeText(fragment.getActivity(), (String) o, Toast.LENGTH_LONG);
        if (o.equals(LOGIN_SUCCESS)) {
            LoggedUser.setUsername(username);
            LoggedUser.setPassword(password);
            NavHostFragment
                    .findNavController(fragment)
                    .navigate(R.id.action_login_to_pokemonCollection);
        }
        toast.show();
    }
}