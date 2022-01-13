package app.async_tasks.database;

import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.R;
import app.Storage;
import app.constants.StringConstants;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class LoginTask extends AsyncTask implements StringConstants {
    private Fragment fragment;
    private String username;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            fragment = (Fragment) objects[0];
            username = (String) objects[1];
            String password = (String) objects[2];

            String data = encodeStrings(
                    new String[]{"username", "password"},
                    new String[]{username, password}
            );

            URL url = new URL(LOGIN_LINK);
            URLConnection conn;
            try {
                conn = url.openConnection();
            } catch (ConnectTimeoutException e1) {
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
            return LOGIN_PROBLEMS;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if (o.equals(LOGIN_SUCCESS)) {
            Storage.setUsername(username);
            NavHostFragment
                    .findNavController(fragment)
                    .navigate(R.id.action_login_to_collection);
        }
        Toast.makeText(fragment.getContext(), (String) o, Toast.LENGTH_SHORT).show();
    }
}