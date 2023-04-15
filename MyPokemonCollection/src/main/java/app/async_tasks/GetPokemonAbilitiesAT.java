package app.async_tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.data_objects.Ability;
import app.ui.fragments.ICallbackContext;
import app.web_scrapping.GetPokemonAbilities;

public class GetPokemonAbilitiesAT extends AsyncTask<Long, String, List<Ability>> {
    private final ICallbackContext callbackContext;

    public GetPokemonAbilitiesAT(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Ability> doInBackground(Long... params) {
        return GetPokemonAbilities.get(params[0]);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(List<Ability> result) {
        if (isCancelled()) return;
        if (result == null) callbackContext.timedOut(this);
        else callbackContext.callback(this, result);
    }
}