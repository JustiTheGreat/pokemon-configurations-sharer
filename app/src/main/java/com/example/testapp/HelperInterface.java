package com.example.testapp;

import android.graphics.Bitmap;

import com.example.testapp.data_objects.PokemonConfiguration;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public interface HelperInterface {
    Bitmap getImageViewFromElement(Element e);

    ArrayList<String> getTypesFromElement(Element element);

    ArrayList<Integer> getBaseStatsFromElement(Element element);

    ArrayList<Integer> calculateStats(Element e, PokemonConfiguration pc);
}
