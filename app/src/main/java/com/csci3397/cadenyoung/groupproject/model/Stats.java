package com.csci3397.cadenyoung.groupproject.model;

import android.util.Log;

import com.csci3397.cadenyoung.groupproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Images used are open source, created by https://randallcurtis.itch.io/16-bit-rpg-icons

public class Stats {
    private Map<String, Stat> stats;

    public Stats(UserStats userStats) {
        stats = new HashMap<String, Stat>();

        this.addStat(R.drawable.mental, 1, "mental", R.string.mental_desc, R.color.stat_mental, userStats.getStat1progress());
        this.addStat(R.drawable.stress, 2, "stress", R.string.stress_desc, R.color.stat_stress,  userStats.getStat2progress());
        this.addStat(R.drawable.screen, 3, "screen", R.string.screen_desc, R.color.stat_screen,  userStats.getStat3progress());
        this.addStat(R.drawable.eating, 4, "eating", R.string.eating_desc, R.color.stat_eating, userStats.getStat4progress());
        this.addStat(R.drawable.water, 5, "water", R.string.water_desc, R.color.stat_water, userStats.getStat5progress());
        this.addStat(R.drawable.fitness, 6, "fitness", R.string.fitness_desc, R.color.stat_fitness, userStats.getStat6progress());
        this.addStat(R.drawable.sleep, 7, "sleep", R.string.sleep_desc, R.color.stat_sleep, userStats.getStat7progress());
    }

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

    public Map<String, Stat> getStats() {return stats;}

    public void updateStat(String statName, int quizAnswer) {
        if (stats.containsKey(statName)) {
            Stat stat = stats.get(statName);
            assert stat != null;
            int statProgress = stat.getProgress();
            Log.d("UPDATESTAT PROG + ANSWER:", String.valueOf(statProgress) + " " + String.valueOf(quizAnswer));
            double change;
            switch (quizAnswer) {
                case 1:
                    change = -0.75;
                    break;
                case 2:
                    change = -0.5;
                    break;
                case 3:
                    change = 0.25;
                    break;
                case 4:
                    change = 0.5;
                    break;
                case 5:
                    change = 0.75;
                    break;
                default:
                    change = 0;
                    break;
//                case 1:
//                    change = -0.25;
//                    break;
//                case 2:
//                    change = -0.1;
//                    break;
//                case 3:
//                    change = 0.02;
//                    break;
//                case 4:
//                    change = 0.1;
//                    break;
//                case 5:
//                    change = 0.25;
//                    break;
//                default:
//                    change = 0;
//                    break;
            }
            int changeProgress = (int) (statProgress * change);
            Log.d("CHANGEPROG: ", String.valueOf(changeProgress));
            Log.d("NEW STAT: ", String.valueOf(changeProgress + statProgress));
            stat.setProgress(changeProgress + statProgress);
        }
    }

    public UserStats updateFromQuiz(String userID, Quiz quiz) {
        ArrayList<Question> questions = quiz.getQuestions();
        for (Question question : questions) {
            String questionType = question.getQuestionType();
            if (stats.containsKey(questionType)) {
                updateStat(questionType, question.getAnswer());
            }
        }
        UserStats userStats = new UserStats(userID, stats.get("mental").getProgress(), stats.get("stress").getProgress(),
                stats.get("screen").getProgress(), stats.get("eating").getProgress(), stats.get("water").getProgress(),
                stats.get("fitness").getProgress(), stats.get("sleep").getProgress());
        return userStats;
    }

    public void quizUpdate(Quiz quiz) {
        Log.d("QUIZUPDATE", "Updating Stats from Quiz Now");
        ArrayList<Question> questions = quiz.getQuestions();
        for (Question question : questions) {
            String questionType = question.getQuestionType();
            if (stats.containsKey(questionType)) {
                updateStat(questionType, question.getAnswer());
            }
        }
    }
}
