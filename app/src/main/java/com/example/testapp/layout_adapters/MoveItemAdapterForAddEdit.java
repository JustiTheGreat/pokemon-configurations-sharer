package com.example.testapp.layout_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testapp.R;
import com.example.testapp.data_objects.Move;

import java.util.ArrayList;

public class MoveItemAdapterForAddEdit extends ArrayAdapter<Move> {
    public MoveItemAdapterForAddEdit(Context context, ArrayList<Move> moves) {
        super(context, 0, moves);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Move move = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_move_item, parent, false);
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
        PP.setText(String.valueOf(move.getPP()));

        TextView description = convertView.findViewById(R.id.l_moveitem_description);
        description.setText(move.getDescription());

        ImageButton change = convertView.findViewById(R.id.l_moveitem_change);
        change.setVisibility(View.GONE);

        ImageButton remove = convertView.findViewById(R.id.l_moveitem_remove);
        remove.setVisibility(View.GONE);

        return convertView;
    }
}

