package app.layout_adapters;

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

import com.example.testapp.R;
import app.Storage;
import app.data_objects.Move;

import java.util.ArrayList;

public class MoveItemAdapterForAddEdit extends ArrayAdapter<Move> {
    public MoveItemAdapterForAddEdit(Context context, ArrayList<Move> moves) {
        super(context, 0, moves);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Move move = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_move_item, parent, false);
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
        PP.setText(String.valueOf(move.getPP()));

        TextView description = convertView.findViewById(R.id.l_moveitem_description);
        description.setText(move.getDescription());

        ImageButton change = convertView.findViewById(R.id.l_moveitem_change);
        change.setOnClickListener(view -> Storage.getAddPokemonFragment().movesDialog(position));

        ImageButton remove = convertView.findViewById(R.id.l_moveitem_remove);
        remove.setOnClickListener(view -> {
            remove(getItem(position));
            Storage.getAddPokemonFragment().recalculateWeights();
            Storage.getAddPokemonFragment().setAddMoveButtonVisibility(true);
            Storage.getAddPokemonFragment().seeIfFinalizingIsPossible();
        });

        return convertView;
    }
}

