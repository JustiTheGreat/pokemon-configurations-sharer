package com.example.testapp;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public interface HelperInterface {
    Bitmap getImageViewFromElement(Element e);

    TextView getTextViewFromString(String s);

    ArrayList<String> getTypesFromElement(Element element);

    ArrayList<Integer> getBaseStatsFromElement(Element element);

    ArrayList<Integer> calculateStats(Element e, PokemonConfiguration pc);
}
