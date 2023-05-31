package app.ui.adapters.searchable;

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

import com.mypokemoncollection.R;

import java.util.ArrayList;
import java.util.List;

import app.data_objects.Ability;

public class AbilityAdapter extends ArrayAdapter<Ability> {
    private final List<Ability> defaultData;
    private final List<Ability> currentData;

    public AbilityAdapter(Context context, List<Ability> abilities) {
        super(context, R.layout.adapter_ability, abilities);
        this.defaultData = new ArrayList<>(abilities);
        this.currentData = new ArrayList<>(abilities);
    }

    @Override
    public int getCount() {
        return currentData.size();
    }

    @Override
    public Ability getItem(int index) {
        return currentData.get(index);
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
            List<Ability> suggestions = new ArrayList<>();
            if (constraint == null || constraint.toString().trim().length() == 0)
                suggestions.addAll(defaultData);
            else defaultData.forEach(ability -> {
                if (ability.getName().toLowerCase().contains(constraint.toString().toLowerCase().trim()))
                    suggestions.add(ability);
            });
            return new FilterResults() {{
                this.values = suggestions;
                this.count = suggestions.size();
            }};
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            currentData.clear();
            currentData.addAll((List<Ability>) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Ability) resultValue).getName();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    public View getView(int position, View convertView, ViewGroup parent) {
        Ability ability = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_ability, parent, false);
        }

        TextView name = convertView.findViewById(R.id.l_abilities_name);
        name.setText(ability.getName());

        TextView description = convertView.findViewById(R.id.l_abilities_description);
        description.setText(ability.getDescription());

        return convertView;
    }
}

