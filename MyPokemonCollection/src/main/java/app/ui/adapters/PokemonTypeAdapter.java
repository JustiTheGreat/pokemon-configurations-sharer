package app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mypokemoncollection.R;

import java.util.List;

import app.data_objects.PokemonType;

public class PokemonTypeAdapter extends BaseAdapter {

    private final Context context;
    private final List<PokemonType> pokemonTypes;

    public PokemonTypeAdapter(Context context, List<PokemonType> pokemonTypes) {
        this.context = context;
        this.pokemonTypes = pokemonTypes;
    }

    @Override
    public int getCount() {
        return pokemonTypes.size();
    }

    @Override
    public PokemonType getItem(int i) {
        return pokemonTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.adapter_pokemon_type, viewGroup, false);

        TextView pokemonType = rootView.findViewById(R.id.aPokemonTypeTV);
        pokemonType.setText(pokemonTypes.get(i).getName().toUpperCase());
        pokemonType.setBackgroundResource(pokemonTypes.get(i).getColor());

        return rootView;
    }
}
