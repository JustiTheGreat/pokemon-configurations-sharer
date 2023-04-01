package app.async_tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.async_tasks.database.ICallbackContext;
import app.data_objects.Ability;

public class GetAbilities extends AsyncTask<Long, String, List<Ability>> {
    private final ICallbackContext callbackContext;

    public GetAbilities(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Ability> doInBackground(Long... longs) {
        return TaskHelper.getPokemonAbilities(longs[0]);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(List<Ability> result) {
        if (isCancelled()) return;
        if (result == null) callbackContext.timedOut();
        else callbackContext.callback(this, result);
    }
}