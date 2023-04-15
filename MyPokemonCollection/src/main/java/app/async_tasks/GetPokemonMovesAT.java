package app.async_tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.data_objects.Move;
import app.ui.fragments.ICallbackContext;
import app.web_scrapping.GetPokemonMoves;

public class GetPokemonMovesAT extends AsyncTask<Long, String, List<Move>> {
    private final ICallbackContext callbackContext;

    public GetPokemonMovesAT(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Move> doInBackground(Long... params) {
        return GetPokemonMoves.get(params[0]);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(List<Move> result) {
        if (isCancelled()) return;
        if (result == null) callbackContext.timedOut(this);
        else callbackContext.callback(this, result);
    }
}
