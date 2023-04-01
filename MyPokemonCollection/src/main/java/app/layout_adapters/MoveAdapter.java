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

import app.data_objects.Move;

public class MoveAdapter extends ArrayAdapter<Move> {
    private final List<Move> moves;

    public MoveAdapter(Context context, List<Move> moves) {
        super(context, R.layout.layout_move, moves);
        this.moves = new ArrayList<>(moves);
    }

    @Override
    public Move getItem(int index) {
        return moves.get(index);
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
            List<Move> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(moves);
            } else {
                String pattern = constraint.toString().toLowerCase().trim();
                moves.forEach(abilityRow -> {
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
            return ((Move) resultValue).getName();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    public View getView(int position, View convertView, ViewGroup parent) {
        Move move = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_move, parent, false);
        }

        TextView name = convertView.findViewById(R.id.l_move_name);
        name.setText(move.getName());

        TextView type = convertView.findViewById(R.id.l_move_type);
        type.setText(move.getType().getName().toUpperCase());
        type.setBackgroundResource(move.getType().getColor());

        ImageView category = convertView.findViewById(R.id.l_move_category);
        category.setImageBitmap(move.getCategory().getIcon());

        TextView power = convertView.findViewById(R.id.l_move_power);
        power.setText(String.valueOf(move.getPower()));

        TextView accuracy = convertView.findViewById(R.id.l_move_accuracy);
        accuracy.setText(String.valueOf(move.getAccuracy()));

        TextView PP = convertView.findViewById(R.id.l_move_pp);
        PP.setText(String.valueOf(move.getPp()));

        return convertView;
    }
}


