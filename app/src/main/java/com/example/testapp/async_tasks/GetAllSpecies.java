package com.example.testapp.async_tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.PokemonConstants;
import com.example.testapp.StringConstants;
import com.example.testapp.async_tasks.TaskHelper;
import com.example.testapp.data_objects.SpeciesRow;
import com.example.testapp.fragments.AddPokemon;

import java.util.ArrayList;

public class GetAllSpecies extends AsyncTask implements PokemonConstants, StringConstants {
    private Fragment fragment;

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected Object doInBackground(Object[] objects) {
        this.fragment = (Fragment) objects[0];
        return TaskHelper.getAllSpecies();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(Object object) {
        if (isCancelled()) return;
        ((AddPokemon) fragment).setAllSpeciesData((ArrayList<SpeciesRow>) object);
    }
}
