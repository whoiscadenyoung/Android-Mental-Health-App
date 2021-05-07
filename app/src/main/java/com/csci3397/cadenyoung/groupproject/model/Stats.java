package com.csci3397.cadenyoung.groupproject.model;

import com.csci3397.cadenyoung.groupproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Images used are open source, created by https://randallcurtis.itch.io/16-bit-rpg-icons

public class Stats {
    private Map<String, Stat> stats;
    private int avatarType;

    public Stats() {
        stats = new HashMap<String, Stat>();

        this.addStat(R.drawable.mental, 1, "mental", R.string.mental_desc, R.color.stat_mental, 50);
        this.addStat(R.drawable.stress, 2, "stress", R.string.stress_desc, R.color.stat_stress, 50);
        this.addStat(R.drawable.screen, 3, "screen", R.string.screen_desc, R.color.stat_screen, 50);
        this.addStat(R.drawable.eating, 4, "eating", R.string.eating_desc, R.color.stat_eating, 50);
        this.addStat(R.drawable.water, 5, "water", R.string.water_desc, R.color.stat_water, 50);
        this.addStat(R.drawable.fitness, 6, "fitness", R.string.fitness_desc, R.color.stat_fitness, 50);
        this.addStat(R.drawable.sleep, 7, "sleep", R.string.sleep_desc, R.color.stat_sleep, 50);
    }

    public Stats(int progress) {
        stats = new HashMap<String, Stat>();

        this.addStat(R.drawable.mental, 1, "mental", R.string.mental_desc, R.color.stat_mental, progress);
        this.addStat(R.drawable.stress, 2, "stress", R.string.stress_desc, R.color.stat_stress, progress);
        this.addStat(R.drawable.screen, 3, "screen", R.string.screen_desc, R.color.stat_screen, progress);
        this.addStat(R.drawable.eating, 4, "eating", R.string.eating_desc, R.color.stat_eating, progress);
        this.addStat(R.drawable.water, 5, "water", R.string.water_desc, R.color.stat_water, progress);
        this.addStat(R.drawable.fitness, 6, "fitness", R.string.fitness_desc, R.color.stat_fitness, progress);
        this.addStat(R.drawable.sleep, 7, "sleep", R.string.sleep_desc, R.color.stat_sleep, progress);
    }

    private void addStat(int imageId, int id, String name, int descId, int colorId, int progress) {
        if (!stats.containsKey(name)) stats.put(name, new Stat(imageId, id, name, descId, colorId, progress));
    }

    public void replaceStats(Stats newStats) {
        for (Stat newStat : newStats.getStats().values()) {
            if (this.stats.containsKey(newStat.getName())) {
                this.stats.get(newStat.getName()).setProgress(newStat.getProgress());
            }
        }
    }

    public void setAvatarType(int avatarType) {this.avatarType = avatarType;}
    public int getAvatarType() {return this.avatarType;}

    public int averageStats() {
        int totalStats = 0;
        for (Stat stat : stats.values()) {
            totalStats += stat.getProgress();
        }
        return totalStats / stats.size();
    }

    public String returnAvatarPath() {
        String fileName = "";

        int averageStats = averageStats();
        if (averageStats > 85) fileName += "happy_open";
        else if (averageStats > 65) fileName += "happy";
        else if (averageStats > 40) fileName += "meh";
        else if (averageStats > 20) fileName += "teary";
        else fileName += "unalive";

        switch (getAvatarType()) {
            case 1:
                fileName += "_green";
                break;
            case 2:
                fileName += "_blue";
                break;
            default:
                fileName += "_green";
                break;
        }
        return fileName;
    }

    public Map<String, Stat> getStats() {return stats;}

    public void updateStat(String statName, int quizAnswer) {
        if (stats.containsKey(statName)) {
            Stat stat = stats.get(statName);
            assert stat != null;
            int statProgress = stat.getProgress();
            double change;
            switch (quizAnswer) {
                case 1:
                    change = -0.25;
                    break;
                case 2:
                    change = -0.1;
                    break;
                case 3:
                    change = 0.02;
                    break;
                case 4:
                    if (statProgress < 10 ) change = 2.0;
                    else change = 0.1;
                    break;
                case 5:
                    if (statProgress < 10 ) change = 3.0;
                    else change = 0.25;
                    break;
                default:
                    if (statProgress < 10 ) change = 4.0;
                    else change = 0;
                    break;
            }
            int changeProgress = (int) (statProgress * change);
            stat.setProgress(changeProgress + statProgress);
        }
    }

    public void quizUpdate(Quiz quiz) {
        ArrayList<Question> questions = quiz.getQuestions();
        for (Question question : questions) {
            String questionType = question.getQuestionType();
            if (stats.containsKey(questionType)) {
                updateStat(questionType, question.getAnswer());
            }
        }
    }
}
