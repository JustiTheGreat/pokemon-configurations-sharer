package app.connections.async_tasks;

import android.os.AsyncTask;

import app.ui.fragments.ICallbackContext;
import app.ui.fragments.GeneralisedFragment;

public abstract class GeneralisedTask<T> extends AsyncTask<Void, Void, T> {
    protected final ICallbackContext callbackContext;

    public GeneralisedTask(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
        if (callbackContext instanceof GeneralisedFragment) {
            ((GeneralisedFragment<?>) callbackContext).addTask(this);
        }
    }

    @Override
    protected void onPostExecute(T t) {
        if (isCancelled()) return;
        if (t == null) callbackContext.timedOut(this);
        else callbackContext.callback(this, t);
    }
}
