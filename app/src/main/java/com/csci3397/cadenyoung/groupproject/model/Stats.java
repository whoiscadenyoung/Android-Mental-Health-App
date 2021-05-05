package com.csci3397.cadenyoung.groupproject.model;

import com.csci3397.cadenyoung.groupproject.R;

import java.util.ArrayList;
import java.util.Hashtable;

// Images used are open source, created by https://randallcurtis.itch.io/16-bit-rpg-icons

public class Stats {
    private Hashtable<String, Stat> stats;

    public Stats(UserStats userStats) {
        stats = new Hashtable<String, Stat>();
        progress = userStats.getUserStats();

        this.addStat(R.drawable.mental, 1, "mental", R.string.mental_desc, R.color.stat_mental, progress[0]);
        this.addStat(R.drawable.stress, 2, "stress", R.string.stress_desc, R.color.stat_stress, progress[1]);
        this.addStat(R.drawable.screen, 3, "screen", R.string.screen_desc, R.color.stat_screen, progress[2]);
        this.addStat(R.drawable.eating, 4, "eating", R.string.eating_desc, R.color.stat_eating, progress[3]);
        this.addStat(R.drawable.water, 5, "water", R.string.water_desc, R.color.stat_water, progress[4]);
        this.addStat(R.drawable.fitness, 6, "fitness", R.string.fitness_desc, R.color.stat_fitness, progress[5]);
        this.addStat(R.drawable.sleep, 7, "sleep", R.string.sleep_desc, R.color.stat_sleep, progress[6]);
    }

    public Stats() {
        stats = new Hashtable<String, Stat>();

        this.addStat(R.drawable.mental, 1, "mental", R.string.mental_desc, R.color.stat_mental, 50);
        this.addStat(R.drawable.stress, 2, "stress", R.string.stress_desc, R.color.stat_stress, 50);
        this.addStat(R.drawable.screen, 3, "screen", R.string.screen_desc, R.color.stat_screen, 50);
        this.addStat(R.drawable.eating, 4, "eating", R.string.eating_desc, R.color.stat_eating, 50);
        this.addStat(R.drawable.water, 5, "water", R.string.water_desc, R.color.stat_water, 50);
        this.addStat(R.drawable.fitness, 6, "fitness", R.string.fitness_desc, R.color.stat_fitness, 50);
        this.addStat(R.drawable.sleep, 7, "sleep", R.string.sleep_desc, R.color.stat_sleep, 50);
    }

    private void addStat(int imageId, int id, String name, int descId, int colorId, int progress) {
        if (!stats.containsKey(name)) stats.put(name, new Stat(imageId, id, name, descId, colorId, progress));
    }

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
                    change = 0.1;
                    break;
                case 5:
                    change = 0.25;
                    break;
                default:
                    change = 0;
                    break;
            }
            int changeProgress = (int) (statProgress * change);
            stat.setProgress(changeProgress + statProgress);
        }
    }
    public UserStats updateFromQuiz(Quiz quiz) {
        ArrayList<Question> questions = quiz.getQuestions();
        for (Question question : questions) {
            String questionType = question.getQuestionType();
            if (stats.containsKey(questionType)) {
                updateStat(questionType, question.getAnswer());
            }
        }
        UserStats userStats = new UserStats(stats.get("mental").getProgress(), stats.get("stress").getProgress(),
                stats.get("screen").getProgress(), stats.get("eating").getProgress(), stats.get("water").getProgress(),
                stats.get("fitness").getProgress(), stats.get("sleep").getProgress());
        return userStats;
    }

    public Stat[] getStats() {return stats.values().toArray(new Stat[0]);}
}
