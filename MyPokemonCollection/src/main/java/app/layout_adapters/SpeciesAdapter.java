package app.layout_adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;

import java.util.ArrayList;
import java.util.List;

import app.data_objects.Pokemon;
import app.data_objects.SpeciesRow;

public class SpeciesAdapter extends ArrayAdapter<Pokemon> {
    private final List<Pokemon> pokemonList;

    public SpeciesAdapter(Context context, List<Pokemon> pokemonList) {
        super(context, R.layout.layout_species, pokemonList);
        this.pokemonList = new ArrayList<>(pokemonList);
    }

    @Override
    public Pokemon getItem(int index) {
        return pokemonList.get(index);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Pokemon> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(pokemonList);
            } else {
                String pattern = constraint.toString().toLowerCase().trim();
                pokemonList.forEach(pokemon -> {
                    if (pokemon.getSpecies().toLowerCase().contains(pattern)) {
                        suggestions.add(pokemon);
                    }
                });
            }
            return new FilterResults() {{
                this.values = suggestions;
                this.count = suggestions.size();
            }};
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Pokemon) resultValue).getSpecies();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    public View getView(int position, View convertView, ViewGroup parent) {
        Pokemon pokemon = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_species, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.l_species_image);
        image.setImageBitmap(pokemon.getSprite());
        image.setContentDescription(pokemon.getSpecies());

        TextView species = convertView.findViewById(R.id.l_species_species);
        species.setText(pokemon.getSpecies());

        List<TextView> types = new ArrayList<>();
        types.add(convertView.findViewById(R.id.l_species_type1));
        if (pokemon.getTypes().size() == 2) {
            types.add(convertView.findViewById(R.id.l_species_type2));
        } else {
            convertView.findViewById(R.id.l_species_type2).setVisibility(View.GONE);
        }
        types.forEach(t -> {
            t.setText(pokemon.getTypes().get(types.indexOf(t)).getName().toUpperCase());
            t.setBackgroundResource(pokemon.getTypes().get(types.indexOf(t)).getColor());
        });

        return convertView;
    }
}
