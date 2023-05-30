package app.ui.dialogs;

import android.app.Dialog;
import android.widget.TextView;

import com.mypokemoncollection.R;

import app.ui.fragments.GeneralisedFragment;
import app.ui.fragments.ICallbackContext;

public abstract class YesNoQuestionDialog extends GeneralisedDialog {

    private final int questionResourceId;

    protected YesNoQuestionDialog(ICallbackContext callbackContext, int questionResourceId) {
        super(callbackContext);
        this.questionResourceId = questionResourceId;
    }

    @Override
    protected void create() {
        dialog = new Dialog(((GeneralisedFragment<?>) callbackContext).requireActivity());
        dialog.setContentView(R.layout.dialog_yes_or_no_question);
    }

    @Override
    protected void setupFunctionality() {
        ((TextView) dialog.findViewById(R.id.dynQuestionTV)).setText(questionResourceId);
        dialog.findViewById(R.id.dynYesB).setOnClickListener(v -> {
            doTheAction();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.dynNoB).setOnClickListener(v -> dialog.dismiss());
    }

    protected abstract void doTheAction();
}
