package com.example.testapp;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    private ArrayList<TextView> textViews = new ArrayList<>();

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
                    new int[]{31, 31, 31, 31, 31, 31},
                    new int[]{6, 0, 0, 252, 0, 252}
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
                    new int[]{31, 31, 31, 31, 31, 31},
                    new int[]{6, 0, 0, 252, 0, 252}
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
                    new int[]{31, 31, 31, 31, 31, 31},
                    new int[]{6, 0, 0, 252, 0, 252}
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

            pokemonConfigurations.forEach(pc -> {
                for (Element e : elements) {
                    if (e.text().equals(pc.getSpecies())) {
                        TextView textView = new TextView(fragment.getContext());
                        textView.setText(pc.getSpecies());
                        textView.setId(pokemonConfigurations.indexOf(pc));
                        //textView.setLayoutParams(new GridView.LayoutParams(240,100));
                        textViews.add(textView);
                        break;
                    }
                }
            });
            return new PokemonConfigurationAdapter(textViews);
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
        private ArrayList<TextView> textViews;

        public PokemonConfigurationAdapter(ArrayList<TextView> textViews) {
            this.textViews = textViews;
        }

        @Override
        public int getCount() {
            return textViews.size();
        }

        @Override
        public Object getItem(int position) {
            return textViews.get(position).getText().toString();
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return textViews.get(position);
        }
    }
}