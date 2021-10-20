package com.example.testapp;

import android.content.Context;
import android.media.Image;
import android.widget.GridView;
import android.widget.TextView;

public class ToDelete{
    private Context context;
    private PokemonConfiguration pokemonConfiguration;
    private Image image;
    private String[] type;
    private int[] baseStats;

    public ToDelete(Context context) {
        this.context=context;

    }
    public void addView(PokemonConfiguration pokemonConfiguration, Image image,String[] type, int[] baseStats){
        this.pokemonConfiguration=pokemonConfiguration;
        this.image=image;
        this.type=type;
        this.baseStats=baseStats;
    }

    public TextView addSomething(){
        TextView textView = new TextView(context);
        textView.setText(pokemonConfiguration.getSpecies());
        textView.setLayoutParams(new GridView.LayoutParams(240,100));
        return textView;
    }
}
