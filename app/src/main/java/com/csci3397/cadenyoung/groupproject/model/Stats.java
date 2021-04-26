package com.csci3397.cadenyoung.groupproject.model;

import com.csci3397.cadenyoung.groupproject.R;

import java.util.Collection;
import java.util.Hashtable;

public class Stats {
    private Hashtable<String, Stat> stats;

    public Stats() {
        stats = new Hashtable<String, Stat>();

        this.addStat(R.drawable.stress, "emotion", R.string.emotion_desc);
        this.addStat(R.drawable.stress, "stress", R.string.stress_desc);
        this.addStat(R.drawable.screen, "screen", R.string.screen_desc);
        this.addStat(R.drawable.eating, "eating", R.string.eating_desc);
        this.addStat(R.drawable.water, "water", R.string.water_desc);
        this.addStat(R.drawable.fitness, "fitness", R.string.fitness_desc);
    }

    private void addStat(int imageId, String name, int desc) {
        if (!stats.containsKey(name)) stats.put(name, new Stat(imageId, name, desc));
    }

    public void updateStat(String name, double newValue) {
        if (stats.containsKey(name)) stats.get(name).setValue(newValue);
    }

    public Collection<Stat> getStats() {
        return stats.values();
    }
}
