package com.example.testapp.communication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.Helper;
import com.example.testapp.PokemonConstants;
import com.example.testapp.R;
import com.example.testapp.activities.PokemonCollectionActivity;
import com.example.testapp.data_objects.GridViewCell;
import com.example.testapp.data_objects.PokemonConfiguration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class GetPokemonConfigurations extends AsyncTask implements PokemonConstants {
    private Fragment fragment;
    private ArrayList<PokemonConfiguration> pokemonConfigurations;

    public GetPokemonConfigurations(Fragment fragment) {
        this.fragment = fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<PokemonConfiguration> readFromDatabase() {
        ArrayList<PokemonConfiguration> pokemonConfigurations = new ArrayList<>();
        try {
            String username = "a";

            String link = "http://192.168.0.11/get_pokemon.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] configuration = line.split(":");
                pokemonConfigurations.add(new PokemonConfiguration(
                        Integer.parseInt(configuration[0]),
                        configuration[1],
                        configuration[2],
                        configuration[3],
                        configuration[4],
                        configuration[5],
                        Integer.parseInt(configuration[6]),
                        new ArrayList<Integer>() {{
                            add(Integer.parseInt(configuration[7]));
                            add(Integer.parseInt(configuration[8]));
                            add(Integer.parseInt(configuration[9]));
                            add(Integer.parseInt(configuration[10]));
                            add(Integer.parseInt(configuration[11]));
                            add(Integer.parseInt(configuration[12]));
                        }},
                        new ArrayList<Integer>() {{
                            add(Integer.parseInt(configuration[13]));
                            add(Integer.parseInt(configuration[14]));
                            add(Integer.parseInt(configuration[15]));
                            add(Integer.parseInt(configuration[16]));
                            add(Integer.parseInt(configuration[17]));
                            add(Integer.parseInt(configuration[18]));
                        }},
                        null
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return pokemonConfigurations;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected Object doInBackground(Object[] objects) {
        ArrayList<GridViewCell> gridViewCells = new ArrayList<>();
        pokemonConfigurations = readFromDatabase();

        Document doc = null;
        try {
            doc = Jsoup.connect("https://pokemondb.net/pokedex/all").get();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Elements elements = doc.getElementsByClass("ent-name");
        pokemonConfigurations.forEach(pc -> {
            for (Element e : elements) {
                if (e.text().equals(pc.getSpecies())) {
                    try {
                        long id = pc.getId();
                        Bitmap image = Helper.getBitmapFromElement(e);
                        String species = pc.getSpecies();
                        String name = pc.getName();
                        ArrayList<String> types = Helper.getTypesFromElement(e);
                        gridViewCells.add(new GridViewCell(id, image, species, name, types));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        System.exit(-1);
                    }
                    break;
                }
            }
        });

        return new PokemonConfigurationAdapter(fragment.getContext(), gridViewCells);
    }

    @Override
    protected void onPostExecute(Object object) {
        ((PokemonCollectionActivity) fragment).setGridViewAdapter((BaseAdapter) object);
        ((PokemonCollectionActivity) fragment).setPokemonConfigurations(pokemonConfigurations);
    }

    private static class PokemonConfigurationAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<GridViewCell> gridViewCells;

        public PokemonConfigurationAdapter(Context context, ArrayList<GridViewCell> gridViewCells) {
            this.context = context;
            this.gridViewCells = gridViewCells;
        }

        @Override
        public int getCount() {
            return gridViewCells.size();
        }

        @Override
        public Object getItem(int position) {
            return gridViewCells.get(position);
        }

        @Override
        public long getItemId(int position) {
            return gridViewCells.get(position).getId();
        }

        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewCell gridViewCell = (GridViewCell) getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.grid_view_layout, parent, false);
            }

            ImageView image = convertView.findViewById(R.id.image);
            TextView species = convertView.findViewById(R.id.species);
            TextView name = convertView.findViewById(R.id.name);
            ArrayList<TextView> types = new ArrayList<>();
            types.add(convertView.findViewById(R.id.type1));
            if (gridViewCell.getTypes().size() == 2) {
                types.add(convertView.findViewById(R.id.type2));
            } else {
                convertView.findViewById(R.id.type2).setVisibility(View.GONE);
            }

            image.setImageBitmap(gridViewCell.getImage());
            image.setContentDescription(gridViewCell.getSpecies());
            species.setText(gridViewCell.getSpecies());
            name.setText(gridViewCell.getName());
            types.forEach(t -> {
                t.setText(gridViewCell.getTypes().get(types.indexOf(t)));
                TYPES.forEach(T -> {
                    if (T.getName().equalsIgnoreCase(gridViewCell.getTypes().get(types.indexOf(t)))) {
                        t.setBackgroundResource(T.getColor());
                    }
                });
            });

            return convertView;
        }
    }
}