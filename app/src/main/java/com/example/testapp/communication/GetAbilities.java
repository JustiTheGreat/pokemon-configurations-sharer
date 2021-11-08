package com.example.testapp.communication;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.Helper;
import com.example.testapp.PokemonConstants;
import com.example.testapp.activities.AddPokemon;
import com.example.testapp.data_objects.Ability;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetAbilities extends AsyncTask implements PokemonConstants {
    private Fragment fragment;

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected Object doInBackground(Object[] objects) {
        this.fragment = (Fragment) objects[0];
        return Helper.getAbilitiesFromString((String)objects[1]);
    }

    @Override
    protected void onPostExecute(Object object) {
        ((AddPokemon) fragment).setAbilitiesRows((ArrayList<Ability>) object);
    }
}

