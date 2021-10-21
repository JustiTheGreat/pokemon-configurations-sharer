package com.example.testapp;

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
                    null,
                    MALE,
                    new Ability("Overgrow", "description"),
                    null,
                    5,
                    new ArrayList<Integer>(){{add(31);add(31);add(31);add(31);add(31);add(31);}},
                    new ArrayList<Integer>(){{add(6);add(0);add(0);add(252);add(0);add(252);}}
                )
        );
        pokemonConfigurations.add(new PokemonConfiguration(
                    2,
                    "Ivysaur",
                    null,
                    MALE,
                    new Ability("Overgrow", "description"),
                    null,
                    5,
                    new ArrayList<Integer>(){{add(31);add(31);add(31);add(31);add(31);add(31);}},
                    new ArrayList<Integer>(){{add(6);add(0);add(0);add(252);add(0);add(252);}}
                )
        );
        pokemonConfigurations.add(new PokemonConfiguration(
                    3,
                    "Venusaur",
                    null,
                    MALE,
                    new Ability("Overgrow", "description"),
                    null,
                    5,
                    new ArrayList<Integer>(){{add(31);add(31);add(31);add(31);add(31);add(31);}},
                    new ArrayList<Integer>(){{add(6);add(0);add(0);add(252);add(0);add(252);}}
                )
        );

    }

    public void readFromDatabase(){
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            readFromDatabase();

            Document doc = Jsoup.connect("https://pokemondb.net/pokedex/all").get();
            Elements elements = doc.getElementsByClass("ent-name");
            ArrayList<String> added = new ArrayList<>();
            pokemonConfigurations.forEach(pc -> {
                for (Element e : elements) {
                    if (e.text().equals(pc.getSpecies()) && !added.contains(e.text())) {
                        added.add(e.text());
                        HelperInterface helper = new Helper();
                        Bitmap image = helper.getImageViewFromElement(e);
                        String species = e.text();
                        String name = pc.getName();
                        ArrayList<String> types = helper.getTypesFromElement(e);
                        ArrayList<Integer> stats = helper.calculateStats(e,pc);

                        cells.add(new Cell(image,species,name,types));
//                        TextView textView = new TextView(fragment.getContext());
//                        textView.setText(pc.getSpecies());
//                        textView.setId(pokemonConfigurations.indexOf(pc));
//                        //textView.setLayoutParams(new GridView.LayoutParams(240,100));
//                        textViews.add(textView);
                        break;
                    }
                }
            });
            return new PokemonConfigurationAdapter(fragment.getContext(),cells);
            //return new PokemonConfigurationAdapter(textViews);
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

        public PokemonConfigurationAdapter(Context context,ArrayList<Cell> cells) {
            this.context=context;
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

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            return textViews.get(position);
//        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Cell cell = (Cell)getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.grid_view_template, parent, false);
            }

            ImageView image = convertView.findViewById(R.id.image);
            TextView species = convertView.findViewById(R.id.species);
            TextView name = convertView.findViewById(R.id.name);
            ArrayList<TextView> types = new ArrayList<>();
            types.add(convertView.findViewById(R.id.type1));
            types.add(convertView.findViewById(R.id.type2));

            image.setImageBitmap(cell.getImage());
            image.setContentDescription(cell.getSpecies());
            species.setText(cell.getSpecies());
            name.setText(cell.getName());
            types.forEach(t->t.setText(cell.getTypes().get(types.indexOf(t))));

            return convertView;
        }
    }
}