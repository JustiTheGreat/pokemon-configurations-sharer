package com.example.testapp.layout_adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.testapp.R;
import com.example.testapp.data_objects.Ability;

import java.util.ArrayList;
import java.util.List;

public class AbilityAdapter extends ArrayAdapter<Ability> {
    private final ArrayList<Ability> abilitiesRows;

    public AbilityAdapter(Context context, ArrayList<Ability> abilitiesRows) {
        super(context, R.layout.layout_ability, abilitiesRows);
        this.abilitiesRows = new ArrayList<>(abilitiesRows);
    }

    @Override
    public Ability getItem(int index) {
        return abilitiesRows.get(index);
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
            ArrayList<Ability> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(abilitiesRows);
            } else {
                String pattern = constraint.toString().toLowerCase().trim();
                abilitiesRows.forEach(abilityRow -> {
                    if (abilityRow.getName().toLowerCase().contains(pattern)) {
                        suggestions.add(abilityRow);
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
            return ((Ability) resultValue).getName();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    public View getView(int position, View convertView, ViewGroup parent) {
        Ability speciesRow = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_ability, parent, false);
        }

        TextView name = convertView.findViewById(R.id.l_abilities_name);
        name.setText(speciesRow.getName());

        TextView description = convertView.findViewById(R.id.l_abilities_description);
        description.setText(speciesRow.getDescription());

        return convertView;
    }
}

