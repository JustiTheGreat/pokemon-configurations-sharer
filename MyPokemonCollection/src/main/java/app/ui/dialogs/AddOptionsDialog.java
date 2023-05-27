package app.ui.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultLauncher;

import com.journeyapps.barcodescanner.ScanOptions;
import com.mypokemoncollection.R;

import app.ui.fragments.ICallbackContext;
import app.ui.fragments.GeneralisedFragment;

public class AddOptionsDialog extends GeneralisedDialog{

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher;

    public AddOptionsDialog(ICallbackContext callbackContext, ActivityResultLauncher<ScanOptions> barcodeLauncher) {
        super(callbackContext);
        this.barcodeLauncher = barcodeLauncher;
    }

    @Override
    protected void create() {
        dialog = new Dialog(((GeneralisedFragment<?>) callbackContext).requireActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_add_options);

        Window window = dialog.getWindow();
        window.setDimAmount(0);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.x = ((GeneralisedFragment<?>) callbackContext).requireActivity().findViewById(R.id.f_c_add_options_button).getWidth() / 2 + 100;
        layoutParams.y = 30;
        window.setAttributes(layoutParams);
    }

    @Override
    protected void setupFunctionality() {
        dialog.findViewById(R.id.dao_write).setOnClickListener(view1 -> addPokemonManually(dialog));
        dialog.findViewById(R.id.dao_scan).setOnClickListener(view1 -> {
            scanQRCode();
            dialog.dismiss();
        });
    }

    private void addPokemonManually(Dialog dialog) {
        ((GeneralisedFragment<?>) callbackContext).navigateTo(R.id.action_collection_to_add);
        dialog.dismiss();
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
