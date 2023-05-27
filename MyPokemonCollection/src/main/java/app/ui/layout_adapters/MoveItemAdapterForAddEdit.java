package app.ui.layout_adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;

import java.util.List;

import app.data_objects.Move;
import app.ui.dialogs.AddMoveDialog;
import app.ui.dialogs.GeneralisedDialog;
import app.ui.fragments.AddPokemon;

public class MoveItemAdapterForAddEdit extends ArrayAdapter<Move> {

    private final AddPokemon fragment;
    public MoveItemAdapterForAddEdit(Context context, List<Move> moves, AddPokemon fragment) {
        super(context, 0, moves);
        this.fragment = fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Move move = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_move_item, parent, false);
        }
        convertView.setLayoutParams(new ListView.LayoutParams(parent.getMeasuredWidth(), parent.getMeasuredHeight() / getCount()));

        switch(position){
            case 0:
                convertView.findViewById(R.id.l_moveitem_container).setBackgroundResource(R.color.analogous_1);
                break;
            case 1:
                convertView.findViewById(R.id.l_moveitem_container).setBackgroundResource(R.color.analogous_2);
                break;
            case 2:
                convertView.findViewById(R.id.l_moveitem_container).setBackgroundResource(R.color.analogous_3);
                break;
            case 3:
                convertView.findViewById(R.id.l_moveitem_container).setBackgroundResource(R.color.analogous_4);
                break;
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
        change.setOnClickListener(view -> {
            GeneralisedDialog dialog = new AddMoveDialog(fragment, fragment.getAllMovesData(), position);
            dialog.load();
        });

        ImageButton remove = convertView.findViewById(R.id.l_moveitem_remove);
        remove.setOnClickListener(view -> {
            remove(getItem(position));
            fragment.recalculateWeights();
            fragment.setAddMoveButtonVisibility(true);
            fragment.seeIfFinalizingIsPossible();
        });

        return convertView;
    }
}

