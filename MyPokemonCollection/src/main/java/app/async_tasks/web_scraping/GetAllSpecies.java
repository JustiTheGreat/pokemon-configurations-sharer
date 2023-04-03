package app.async_tasks.web_scraping;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.async_tasks.TaskHelper;
import app.ui.fragments.ICallbackContext;
import app.data_objects.Pokemon;

public class GetAllSpecies extends AsyncTask<String, String, List<Pokemon>>{
    private final ICallbackContext callbackContext;

    public GetAllSpecies(ICallbackContext callbackContext){
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Pokemon> doInBackground(String... strings) {
        return TaskHelper.getAllPokemonData();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(List<Pokemon> result) {
        if (isCancelled()) return;
        if (result == null) callbackContext.timedOut();
        else callbackContext.callback(this, result);
    }
}
