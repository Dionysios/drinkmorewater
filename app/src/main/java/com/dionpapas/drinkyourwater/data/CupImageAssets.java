package com.dionpapas.drinkyourwater.data;

import com.dionpapas.drinkyourwater.R;

import java.util.ArrayList;
import java.util.List;

public class CupImageAssets {

    private static final List<Integer> cups = new ArrayList<Integer> () {{
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
        add(R.drawable.ic_launcher);
    }};

    public static List<Integer> getCups() {
        return cups;
    }
}
