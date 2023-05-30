package app.ui.dialogs;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mypokemoncollection.R;

import app.data_objects.Pokemon;
import app.ui.fragments.GeneralisedFragment;
import app.ui.fragments.ICallbackContext;

public class QRCodeDialog extends GeneralisedDialog {
    private final Pokemon pokemon;

    public QRCodeDialog(ICallbackContext callbackContext, Pokemon pokemon) {
        super(callbackContext);
        this.pokemon = pokemon;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void create() {
        dialog = new Dialog(((GeneralisedFragment<?>) callbackContext).requireActivity());
        dialog.setContentView(R.layout.dialog_qr);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((GeneralisedFragment<?>) callbackContext).requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels / 2;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(pokemon.toStringOfTransmissibleData(), BarcodeFormat.QR_CODE, width, width);
            ((ImageView) dialog.findViewById(R.id.d_qr_image)).setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setupFunctionality() {
    }
}
