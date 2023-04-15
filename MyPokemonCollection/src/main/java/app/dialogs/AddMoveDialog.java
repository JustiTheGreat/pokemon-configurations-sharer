package app.dialogs;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import app.ui.fragments.ICallbackContext;
import app.data_objects.Move;
import app.layout_adapters.searchable.MoveAdapter;
import app.ui.fragments.AddPokemon;
import lombok.Getter;

public class AddMoveDialog extends Dialog {
    private final List<Move> allMovesData;
    @Getter private final int moveIndex;

    public AddMoveDialog(ICallbackContext context, int resourceId, List<Move> allMovesData, int moveIndex) {
        super(context, resourceId);
        this.allMovesData = allMovesData;
        this.moveIndex = moveIndex;
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void loadDialog() {
        dialog = new android.app.Dialog(((AddPokemon) context).getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(resourceId);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.TOP;
        window.setAttributes(layoutParams);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AddPokemon) context).requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 90 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView movesTV = dialog.findViewById(R.id.d_search_search);
        movesTV.setAdapter(new MoveAdapter(((AddPokemon) context).getActivity(), allMovesData));
        movesTV.setThreshold(1);
        movesTV.setOnTouchListener((View v, MotionEvent event) -> {
            movesTV.showDropDown();
            return false;
        });
        AtomicReference<Move> move = new AtomicReference<>();
        movesTV.setOnItemClickListener((parent, view, position, id) -> move.set((Move) movesTV.getAdapter().getItem(position)));
        Button button = dialog.findViewById(R.id.d_search_button);
        button.setOnClickListener(v -> {
            if (move.get() != null) context.callback(this, move.get());
            else context.timedOut(this);
            dialog.dismiss();
        });
        dialog.show();
    }
}
