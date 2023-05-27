package app.ui.dialogs;

import android.app.Dialog;

import app.ui.fragments.ICallbackContext;

public abstract class GeneralisedDialog {

    protected final ICallbackContext callbackContext;
    protected Dialog dialog;

    public GeneralisedDialog(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void load(){
        create();
        setupFunctionality();
        dialog.show();
    }

    protected abstract void create();

    protected abstract void setupFunctionality();
}
