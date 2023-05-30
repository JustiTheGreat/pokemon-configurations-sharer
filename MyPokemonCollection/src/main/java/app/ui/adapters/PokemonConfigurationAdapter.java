package app.ui.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.mypokemoncollection.R;

import java.util.ArrayList;
import java.util.List;

import app.data_objects.Pokemon;

public class PokemonConfigurationAdapter extends BaseAdapter {
    private final Context context;
    private final List<Pokemon> pokemonList;

    public PokemonConfigurationAdapter(Context context, List<Pokemon> pokemonList) {
        this.context = context;
        this.pokemonList = pokemonList;
    }

    @Override
    public int getCount() {
        return pokemonList.size();
    }

    @Override
    public Object getItem(int position) {
        return pokemonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pokemon pokemon = (Pokemon) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_collection, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.image);
        image.setImageBitmap(pokemon.getImage());
        image.setContentDescription(pokemon.getSpecies());
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(image.getContext(), R.color.white),
                        ContextCompat.getColor(image.getContext(), R.color.main_color)
                });
        image.setBackground(gradientDrawable);

        TextView species = convertView.findViewById(R.id.species);
        species.setText(pokemon.getSpecies());

        TextView name = convertView.findViewById(R.id.name);
        name.setText(pokemon.getName());

        List<TextView> types = new ArrayList<>();
        types.add(convertView.findViewById(R.id.type1));
        if (pokemon.getTypes().size() == 2) {
            types.add(convertView.findViewById(R.id.type2));
        } else {
            convertView.findViewById(R.id.type2).setVisibility(View.GONE);
        }

        types.forEach(t -> {
            t.setText(pokemon.getTypes().get(types.indexOf(t)).getName().toUpperCase());
            t.setBackgroundResource(pokemon.getTypes().get(types.indexOf(t)).getColor());
        });
        return convertView;
    }
}

