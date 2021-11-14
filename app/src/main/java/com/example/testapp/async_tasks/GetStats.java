package com.example.testapp.async_tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.PokemonConstants;
import com.example.testapp.fragments.AddPokemon;

import java.util.ArrayList;

public class GetStats extends AsyncTask implements PokemonConstants {
    private Fragment fragment;
    private ArrayList<Integer> baseStats;

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected Object doInBackground(Object[] objects) {
        fragment = (Fragment) objects[0];
        baseStats = Helper.getBaseStats((String) objects[1]);
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(Object object) {
        ((AddPokemon) fragment).setStatsRows(baseStats);
    }
}
