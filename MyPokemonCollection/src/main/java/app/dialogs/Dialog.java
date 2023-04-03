package app.dialogs;

import android.view.View;

import app.ui.fragments.ICallbackContext;

public abstract class Dialog {
    protected final ICallbackContext context;
    protected final int resourceId;
    protected android.app.Dialog dialog;

    public Dialog(ICallbackContext context, int resourceId) {
        this.context = context;
        this.resourceId = resourceId;
    }

    public void setViewVisible(int resourceId) {
        dialog.findViewById(resourceId).setVisibility(View.VISIBLE);
    }

    public abstract void loadDialog();
}
