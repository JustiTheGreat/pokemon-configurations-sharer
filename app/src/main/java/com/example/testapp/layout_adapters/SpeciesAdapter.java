package com.example.testapp.layout_adapters;

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

import com.example.testapp.PokemonConstants;
import com.example.testapp.R;
import com.example.testapp.data_objects.SpeciesRow;

import java.util.ArrayList;
import java.util.List;

public class SpeciesAdapter extends ArrayAdapter<SpeciesRow> implements PokemonConstants {
    private final Context context;
    private final ArrayList<SpeciesRow> speciesRows;

    public SpeciesAdapter(Context context, ArrayList<SpeciesRow> speciesRows) {
        super(context, R.layout.layout_species, speciesRows);
        this.context = context;
        this.speciesRows = new ArrayList<>(speciesRows);
    }

    @Override
    public SpeciesRow getItem(int index) {
        return speciesRows.get(index);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SpeciesRow> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(speciesRows);
            } else {
                String pattern = constraint.toString().toLowerCase().trim();
                speciesRows.forEach(speciesRow -> {
                    if (speciesRow.getSpecies().toLowerCase().contains(pattern)) {
                        suggestions.add(speciesRow);
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

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((SpeciesRow) resultValue).getSpecies();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    public View getView(int position, View convertView, ViewGroup parent) {
        SpeciesRow speciesRow = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_species, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.l_species_image);
        image.setImageBitmap(speciesRow.getIcon());
        image.setContentDescription(speciesRow.getSpecies());

        TextView species = convertView.findViewById(R.id.l_species_species);
        species.setText(speciesRow.getSpecies());

        ArrayList<TextView> types = new ArrayList<>();
        types.add(convertView.findViewById(R.id.l_species_type1));
        if (speciesRow.getTypes().size() == 2) {
            types.add(convertView.findViewById(R.id.l_species_type2));
        } else {
            convertView.findViewById(R.id.l_species_type2).setVisibility(View.GONE);
        }
        types.forEach(t -> {
            t.setText(speciesRow.getTypes().get(types.indexOf(t)));
            TYPES.forEach(T -> {
                if (T.getName().equalsIgnoreCase(speciesRow.getTypes().get(types.indexOf(t)))) {
                    t.setBackgroundResource(T.getColor());
                }
            });
        });

        return convertView;
    }
}
