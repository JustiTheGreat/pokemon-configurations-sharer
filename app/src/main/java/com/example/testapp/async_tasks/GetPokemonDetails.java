package com.example.testapp.async_tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.fragments.PokemonDetails;
import com.example.testapp.data_objects.Ability;

import java.util.ArrayList;

public class GetPokemonDetails extends AsyncTask {
    private Fragment fragment;
    private Bitmap image;
    private ArrayList<String> types;
    private Ability ability;
    private ArrayList<Integer> baseStats;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected Object doInBackground(Object[] objects) {
        fragment = (Fragment) objects[0];
        image = Helper.getBitmap((String) objects[1]);
        types = Helper.getTypes((String) objects[1]);
        ability = Helper.getAbility((String) objects[2]);
        baseStats = Helper.getBaseStats((String) objects[1]);
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(Object o) {
        ((PokemonDetails) fragment).set(image, types, ability, baseStats);
    }
}
