package app.ui.layout_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mypokemoncollection.R;

import java.util.List;

import app.data_objects.Move;

public class MoveItemAdapterForDetails extends ArrayAdapter<Move> {
    public MoveItemAdapterForDetails(Context context, List<Move> moves) {
        super(context, 0, moves);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Move move = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_move_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.l_moveitem_name);
        name.setText(move.getName());

        TextView type = convertView.findViewById(R.id.l_moveitem_type);
        type.setText(move.getType().getName().toUpperCase());
        type.setBackgroundResource(move.getType().getColor());

        ImageView category = convertView.findViewById(R.id.l_moveitem_category);
        category.setImageBitmap(move.getCategory().getIcon());

        TextView power = convertView.findViewById(R.id.l_moveitem_power);
        power.setText(String.valueOf(move.getPower()));

        TextView accuracy = convertView.findViewById(R.id.l_moveitem_accuracy);
        accuracy.setText(String.valueOf(move.getAccuracy()));

        TextView PP = convertView.findViewById(R.id.l_moveitem_pp);
        PP.setText(String.valueOf(move.getPp()));

        TextView description = convertView.findViewById(R.id.l_moveitem_description);
        description.setText(move.getDescription());
        
        ImageButton change = convertView.findViewById(R.id.l_moveitem_change);
        change.setVisibility(View.GONE);

        ImageButton remove = convertView.findViewById(R.id.l_moveitem_remove);
        remove.setVisibility(View.GONE);

        return convertView;
    }
}