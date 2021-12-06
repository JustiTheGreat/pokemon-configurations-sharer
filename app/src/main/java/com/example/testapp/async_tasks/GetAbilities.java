package com.example.testapp.async_tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.data_objects.Ability;
import com.example.testapp.fragments.AddPokemon;

import java.util.ArrayList;

public class GetAbilities extends AsyncTask {
    private Fragment fragment;
    private ArrayList<Ability> abilities;

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected Object doInBackground(Object[] objects) {
        fragment = (Fragment) objects[0];
        abilities = Helper.getPokemonAbilities((String) objects[1]);
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(Object object) {
        ((AddPokemon) fragment).setAbilitiesRows(abilities);
    }
}