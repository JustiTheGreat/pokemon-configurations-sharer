package com.example.testapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class LoginServer extends AsyncTask implements ToastMessages {
    private Fragment fragment;

    public LoginServer(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String username = (String) objects[0];
            String password = (String) objects[1];

            String link = "http://192.168.0.11/login.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
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
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        Toast toast = Toast.makeText(fragment.getActivity(), "", Toast.LENGTH_SHORT);
        if (o.equals(LOGIN_SUCCESS)) {
            NavHostFragment
                    .findNavController(fragment)
                    .navigate(R.id.action_login_to_pokemonCollection);
        }
        toast.setText((String) o);
        toast.show();
    }
}