package com.example.lovro.myapplication.domain;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class Colors {
    static public List<String> colors=new ArrayList<String>(){{
        add("#9c229c");
        add("#db6064");
        add("#28ce98");
        add("#ac5fae");
        add("#ba3e7c");
        add("#026ba3");
        add("#59db9b");
        add("#3a81ec");
        add("#c9d50f");
        add("#e9d70d");
        add("#7440d1");
        add("#16d05c");
        add("#262ed2");
        add("#d79387");
        add("#1024d3");
        add("#23d740");
        add("#3ac51a");
        add("#17a044");
        add("#140471");
        add("#6bee86");
        add("#78185a");
        add("#7391c3");
        add("#783df7");
        add("#ef1633");
        add("#68433f");
        add("#4b58a5");
        add("#8e3eb4");
        add("#e311e8");
        add("#304514");
    }};


    public static int getColor(int i) {
        if (i>28){
            return Color.parseColor(colors.get(28));
        }
        return Color.parseColor(colors.get(i));
    }
}
