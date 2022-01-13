package app.async_tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import app.data_objects.Ability;
import app.fragments.AddPokemon;

import java.util.ArrayList;

public class GetAbilities extends AsyncTask {
    private Fragment fragment;
    private ArrayList<Ability> abilities;

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected Object doInBackground(Object[] objects) {
        fragment = (Fragment) objects[0];
        abilities = TaskHelper.getPokemonAbilities((String) objects[1]);
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(Object object) {
        if (isCancelled()) return;
        ((AddPokemon) fragment).setAbilities(abilities);
    }
}