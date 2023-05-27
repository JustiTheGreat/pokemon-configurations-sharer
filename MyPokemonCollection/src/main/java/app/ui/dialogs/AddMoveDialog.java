package app.ui.dialogs;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.mypokemoncollection.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import app.data_objects.Move;
import app.ui.fragments.AddPokemon;
import app.ui.fragments.ICallbackContext;
import app.ui.layout_adapters.searchable.MoveAdapter;

public class AddMoveDialog extends GeneralisedDialog {
    private final List<Move> allMovesData;
    private final int moveIndex;

    public AddMoveDialog(ICallbackContext context, List<Move> allMovesData, int moveIndex) {
        super(context);
        this.allMovesData = allMovesData;
        this.moveIndex = moveIndex;
    }

    @Override
    protected void create() {
        dialog = new Dialog(((AddPokemon) callbackContext).requireActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.TOP;
        window.setAttributes(layoutParams);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AddPokemon) callbackContext).requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 90 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void setupFunctionality() {
        AutoCompleteTextView movesTV = dialog.findViewById(R.id.d_search_search);
        movesTV.setAdapter(new MoveAdapter(((AddPokemon) callbackContext).getActivity(), allMovesData));
        movesTV.setThreshold(1);
        movesTV.setOnTouchListener((View v, MotionEvent event) -> {
            movesTV.showDropDown();
            return false;
        });

        AtomicReference<Move> move = new AtomicReference<>();
        movesTV.setOnItemClickListener((parent, view, position, id) -> move.set((Move) movesTV.getAdapter().getItem(position)));
        Button button = dialog.findViewById(R.id.d_search_button);
        button.setOnClickListener(v -> {
            if (move.get() != null) callbackContext.callback(this, move.get());
            else callbackContext.timedOut(this);
            dialog.dismiss();
        });
    }

    public int getMoveIndex() {
        return moveIndex;
    }
}
