package app.ui.dialogs;

import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.ScanOptions;
import com.mypokemoncollection.R;

import app.ui.fragments.GeneralisedFragment;
import app.ui.fragments.ICallbackContext;

public class AddOptionsDialog extends GeneralisedDialog {

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher;

    public AddOptionsDialog(ICallbackContext callbackContext, ActivityResultLauncher<ScanOptions> barcodeLauncher) {
        super(callbackContext);
        this.barcodeLauncher = barcodeLauncher;
    }

    @Override
    protected void create() {
        GeneralisedFragment<?> fragment = ((GeneralisedFragment<?>) callbackContext);
        dialog = new Dialog(fragment.requireActivity());
        dialog.setContentView(R.layout.dialog_bottom);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(fragment.requireActivity(), R.color.analogous_1),
                        ContextCompat.getColor(fragment.requireActivity(), R.color.analogous_2),
                        ContextCompat.getColor(fragment.requireActivity(), R.color.analogous_3),
                        ContextCompat.getColor(fragment.requireActivity(), R.color.analogous_4),
                });
        dialog.getWindow().setBackgroundDrawable(gradientDrawable);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void setupFunctionality() {
        dialog.findViewById(R.id.dbCompleteL).setOnClickListener(view -> {
            ((GeneralisedFragment<?>) callbackContext).navigateTo(R.id.action_collection_to_add);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.dbScanL).setOnClickListener(view -> {
            scanQRCode();
            dialog.dismiss();
        });
    }

    private void scanQRCode() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setOrientationLocked(false);
        options.setPrompt(((GeneralisedFragment<?>) callbackContext).getString(R.string.scan_a_pokemon_qr_code));
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);
    }
}
