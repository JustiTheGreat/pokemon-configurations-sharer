package app.layout_adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.testapp.R;
import app.data_objects.GridViewCell;

import java.util.ArrayList;

public class PokemonConfigurationAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<GridViewCell> gridViewCells;

    public PokemonConfigurationAdapter(Context context, ArrayList<GridViewCell> gridViewCells) {
        this.context = context;
        this.gridViewCells = gridViewCells;
    }

    @Override
    public int getCount() {
        return gridViewCells.size();
    }

    @Override
    public Object getItem(int position) {
        return gridViewCells.get(position);
    }

    @Override
    public long getItemId(int position) {
        return gridViewCells.get(position).getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewCell gridViewCell = (GridViewCell) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_collection, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.image);
        image.setImageBitmap(gridViewCell.getImage());
        image.setContentDescription(gridViewCell.getSpecies());
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(image.getContext(), R.color.white),
                        ContextCompat.getColor(image.getContext(), R.color.main_color)
                });
        image.setBackground(gradientDrawable);

        TextView species = convertView.findViewById(R.id.species);
        species.setText(gridViewCell.getSpecies());

        TextView name = convertView.findViewById(R.id.name);
        name.setText(gridViewCell.getName());

        ArrayList<TextView> types = new ArrayList<>();
        types.add(convertView.findViewById(R.id.type1));
        if (gridViewCell.getTypes().size() == 2) {
            types.add(convertView.findViewById(R.id.type2));
        } else {
            convertView.findViewById(R.id.type2).setVisibility(View.GONE);
        }

        types.forEach(t -> {
            t.setText(gridViewCell.getTypes().get(types.indexOf(t)).getName().toUpperCase());
            t.setBackgroundResource(gridViewCell.getTypes().get(types.indexOf(t)).getColor());
        });
        return convertView;
    }
}

