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

import java.util.ArrayList;

public class GetPokemonConfigurations extends AsyncTask implements PokemonConstants {
    private Fragment fragment;
    private ArrayList<PokemonConfiguration> pokemonConfigurations = new ArrayList<>();
    private ArrayList<Cell> cells = new ArrayList<>();

    public GetPokemonConfigurations(Fragment fragment) {
        this.fragment = fragment;
        pokemonConfigurations.add(new PokemonConfiguration(
                1,
                "Bulbasaur",
                "\"Veni\"",
                MALE,
                new Ability("Overgrow", "description"),
                null,
                5,
                new ArrayList<Integer>() {{
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                }},
                new ArrayList<Integer>() {{
                    add(6);
                    add(0);
                    add(0);
                    add(252);
                    add(0);
                    add(252);
                }}
                )
        );
        pokemonConfigurations.add(new PokemonConfiguration(
                2,
                "Ivysaur",
                "\"Veni\"",
                MALE,
                new Ability("Overgrow", "description"),
                null,
                5,
                new ArrayList<Integer>() {{
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                }},
                new ArrayList<Integer>() {{
                    add(6);
                    add(0);
                    add(0);
                    add(252);
                    add(0);
                    add(252);
                }}
                )
        );
        pokemonConfigurations.add(new PokemonConfiguration(
                3,
                "Venusaur",
                "\"Veni\"",
                MALE,
                new Ability("Overgrow", "description"),
                null,
                5,
                new ArrayList<Integer>() {{
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                }},
                new ArrayList<Integer>() {{
                    add(6);
                    add(0);
                    add(0);
                    add(252);
                    add(0);
                    add(252);
                }}
                )
        );
        pokemonConfigurations.add(new PokemonConfiguration(
                4,
                "Charmander",
                "\"Veni\"",
                MALE,
                new Ability("Overgrow", "description"),
                null,
                5,
                new ArrayList<Integer>() {{
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                    add(31);
                }},
                new ArrayList<Integer>() {{
                    add(6);
                    add(0);
                    add(0);
                    add(252);
                    add(0);
                    add(252);
                }}
                )
        );

    }

    public void readFromDatabase() {
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
                        t.setBackgroundColor(T.getColor());

                    }
                });
            });

            return convertView;
        }
    }
}