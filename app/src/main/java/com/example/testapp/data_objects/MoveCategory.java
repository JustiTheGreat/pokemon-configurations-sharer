package com.example.testapp.data_objects;

import android.graphics.Bitmap;

public class MoveCategory {
    private final String name;
    private final Bitmap icon;

    public MoveCategory(String name, Bitmap icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Bitmap getIcon() {
        return icon;
    }
}
