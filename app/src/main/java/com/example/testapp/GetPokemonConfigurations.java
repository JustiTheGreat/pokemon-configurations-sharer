package com.example.testapp;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class GetPokemonConfigurations extends AsyncTask implements PokemonConstants {
    private Fragment fragment;
    private ArrayList<PokemonConfiguration> pokemonConfigurations = new ArrayList<>();
    private ArrayList<Cell> cells = new ArrayList<>();

    public GetPokemonConfigurations(Fragment fragment) {
        this.fragment = fragment;
    }

    private NATURE getNature(String s){
        for (NATURE N:NATURES) {
            if(s.equals(N.getName()))return N;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void readFromDatabase() {
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
            String line="";

            while ((line = reader.readLine()) != null) {
                String[] configuration = line.split(":");
                pokemonConfigurations.add(new PokemonConfiguration(
                        Integer.parseInt(configuration[0]),
                        configuration[1],
                        configuration[2],
                        configuration[3],
                        new Ability(configuration[4], ""),
                        getNature(configuration[5]),//nature
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
                        null//moves
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            readFromDatabase();

            Document doc = Jsoup.connect("https://pokemondb.net/pokedex/all").get();
            Elements elements = doc.getElementsByClass("ent-name");
            pokemonConfigurations.forEach(pc -> {
                for (Element e : elements) {
                    if (e.text().equals(pc.getSpecies())) {
                        HelperInterface helper = new Helper();
                        Bitmap image = helper.getImageViewFromElement(e);
                        String species = e.text();
                        String name = pc.getName();
                        ArrayList<String> types = helper.getTypesFromElement(e);
                        ArrayList<Integer> stats = helper.calculateStats(e, pc);

                        cells.add(new Cell(image, species, name, types));
                        break;
                    }
                }
            });
            return new PokemonConfigurationAdapter(fragment.getContext(), cells);
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Object object) {
        ((PokemonConfigurations) fragment).setGridViewAdapter((BaseAdapter) object);
    }

    private static class PokemonConfigurationAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Cell> cells;

        public PokemonConfigurationAdapter(Context context, ArrayList<Cell> cells) {
            this.context = context;
            this.cells = cells;
        }

        @Override
        public int getCount() {
            return cells.size();
        }

        @Override
        public Object getItem(int position) {
            return cells.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Cell cell = (Cell) getItem(position);

            if (convertView == null) {
                if (cell.getTypes().size() == 1) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.grid_view_layout_1type, parent, false);
                } else if (cell.getTypes().size() == 2) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.grid_view_layout_2types, parent, false);
                }
            }

            ImageView image = convertView.findViewById(R.id.image);
            TextView species = convertView.findViewById(R.id.species);
            TextView name = convertView.findViewById(R.id.name);
            ArrayList<TextView> types = new ArrayList<>();
            types.add(convertView.findViewById(R.id.type1));
            if (cell.getTypes().size() == 2) {
                types.add(convertView.findViewById(R.id.type2));
            }

            image.setImageBitmap(cell.getImage());
            image.setContentDescription(cell.getSpecies());
            species.setText(cell.getSpecies());
            name.setText(cell.getName());
            types.forEach(t -> {
                t.setText(cell.getTypes().get(types.indexOf(t)));
                TYPES.forEach(T -> {
                    if (T.getName().equalsIgnoreCase(cell.getTypes().get(types.indexOf(t)))) {
                        t.setBackgroundResource(T.getColor());
                    }
                });
            });

            return convertView;
        }
    }
}