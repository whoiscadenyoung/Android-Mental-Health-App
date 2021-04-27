package com.csci3397.cadenyoung.groupproject.model;

import com.csci3397.cadenyoung.groupproject.R;

import java.util.Collection;
import java.util.Hashtable;

public class Stats {
    private Hashtable<String, Stat> stats;

    public Stats() {
        stats = new Hashtable<String, Stat>();

        this.addStat(R.drawable.stress, "emotion", R.string.emotion_desc, R.color.stat_emotional);
        this.addStat(R.drawable.stress, "stress", R.string.stress_desc, R.color.stat_stress);
        this.addStat(R.drawable.screen, "screen", R.string.screen_desc, R.color.stat_screen);
        this.addStat(R.drawable.eating, "eating", R.string.eating_desc, R.color.stat_eating);
        this.addStat(R.drawable.water, "water", R.string.water_desc, R.color.stat_water);
        this.addStat(R.drawable.fitness, "fitness", R.string.fitness_desc, R.color.stat_fitness);
    }

    private void addStat(int imageId, String name, int descId, int colorId) {
        if (!stats.containsKey(name)) stats.put(name, new Stat(imageId, name, descId, colorId));
    }

    public void updateStat(String name, int newValue) {
        if (stats.containsKey(name)) stats.get(name).setValue(newValue);
    }

    public Collection<Stat> getStats() {
        return stats.values();
    }
}
