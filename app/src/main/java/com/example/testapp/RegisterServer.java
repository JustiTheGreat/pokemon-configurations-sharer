package com.example.testapp;

import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class RegisterServer extends AsyncTask implements ToastMessages {
    private Fragment fragment;

    public RegisterServer(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //            try {
//                String username = (String) objects[0];
//                String password = (String) objects[1];
//                String link = "http://http://192.168.0.11/" + "login.php?username=" + username + "& password=" + password;
//
//                HttpClient client = new DefaultHttpClient();
//                HttpGet request = new HttpGet();
//                request.setURI(new URI(link));
//                HttpResponse response = client.execute(request);
//
//                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//                StringBuffer sb = new StringBuffer("");
//                String line;
//                while ((line = in.readLine()) != null) {
//                    sb.append(line);
//                    break;
//                }
//                in.close();
//
//                return sb.toString();
//            } catch (Exception e) {
//                return "Exception: " + e.getMessage();
//            }
//        } else if(flag==0){
        try {
            String username = (String) objects[0];
            String password = (String) objects[1];
            String email = (String) objects[2];

            String link = "http://192.168.0.11/register.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line="";

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
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
        if (o.equals(REGISTER_SUCCESS)) {
            NavHostFragment
                    .findNavController(fragment)
                    .navigate(R.id.action_register_to_login);
        }
        toast.setText((String) o);
        toast.show();
    }
}
