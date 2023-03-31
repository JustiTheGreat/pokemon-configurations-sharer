//package app.layout_adapters;
//
//import android.content.Context;
//import android.os.Build;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Filter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//
//import com.mypokemoncollection.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import app.data_objects.SpeciesRow;
//
//public class SpeciesAdapter extends ArrayAdapter<SpeciesRow> {
//    private final List<SpeciesRow> speciesRows;
//
//    public SpeciesAdapter(Context context, List<SpeciesRow> speciesRows) {
//        super(context, R.layout.layout_species, speciesRows);
//        this.speciesRows = new ArrayList<>(speciesRows);
//    }
//
//    @Override
//    public SpeciesRow getItem(int index) {
//        return speciesRows.get(index);
//    }
//
//    @NonNull
//    @Override
//    public Filter getFilter() {
//        return filter;
//    }
//
//    private final Filter filter = new Filter() {
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            ArrayList<SpeciesRow> suggestions = new ArrayList<>();
//            if (constraint == null || constraint.length() == 0) {
//                suggestions.addAll(speciesRows);
//            } else {
//                String pattern = constraint.toString().toLowerCase().trim();
//                speciesRows.forEach(speciesRow -> {
//                    if (speciesRow.getSpecies().toLowerCase().contains(pattern)) {
//                        suggestions.add(speciesRow);
//                    }
//                });
//            }
//            return new FilterResults() {{
//                this.values = suggestions;
//                this.count = suggestions.size();
//            }};
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            clear();
//            addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public CharSequence convertResultToString(Object resultValue) {
//            return ((SpeciesRow) resultValue).getSpecies();
//        }
//    };
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    public View getView(int position, View convertView, ViewGroup parent) {
//        SpeciesRow speciesRow = getItem(position);
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_species, parent, false);
//        }
//
//        ImageView image = convertView.findViewById(R.id.l_species_image);
//        image.setImageBitmap(speciesRow.getSprite());
//        image.setContentDescription(speciesRow.getSpecies());
//
//        TextView species = convertView.findViewById(R.id.l_species_species);
//        species.setText(speciesRow.getSpecies());
//
//        List<TextView> types = new ArrayList<>();
//        types.add(convertView.findViewById(R.id.l_species_type1));
//        if (speciesRow.getTypes().size() == 2) {
//            types.add(convertView.findViewById(R.id.l_species_type2));
//        } else {
//            convertView.findViewById(R.id.l_species_type2).setVisibility(View.GONE);
//        }
//        types.forEach(t -> {
//            t.setText(speciesRow.getTypes().get(types.indexOf(t)).getName().toUpperCase());
//            t.setBackgroundResource(speciesRow.getTypes().get(types.indexOf(t)).getColor());
//        });
//
//        return convertView;
//    }
//}
