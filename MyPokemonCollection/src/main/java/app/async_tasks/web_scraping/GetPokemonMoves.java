package app.async_tasks.web_scraping;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.async_tasks.TaskHelper;
import app.ui.fragments.ICallbackContext;
import app.data_objects.Move;

public class GetPokemonMoves extends AsyncTask<Long, String, List<Move>> {
    private final ICallbackContext callbackContext;

    public GetPokemonMoves(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Move> doInBackground(Long... longs) {
        return TaskHelper.getPokemonMoves(longs[0]);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(List<Move> result) {
        if (isCancelled()) return;
        if (result == null) callbackContext.timedOut();
        else callbackContext.callback(this, result);
    }
}
