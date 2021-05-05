package com.csci3397.cadenyoung.groupproject.model;

import com.csci3397.cadenyoung.groupproject.R;

import java.util.ArrayList;
import java.util.Hashtable;

// Images used are open source, created by https://randallcurtis.itch.io/16-bit-rpg-icons

public class Stats {
    private Hashtable<String, Stat> stats;

    public Stats() {
        stats = new Hashtable<String, Stat>();

        this.addStat(R.drawable.mental, "mental", R.string.mental_desc, R.color.stat_mental);
        this.addStat(R.drawable.stress, "stress", R.string.stress_desc, R.color.stat_stress);
        this.addStat(R.drawable.screen, "screen", R.string.screen_desc, R.color.stat_screen);
        this.addStat(R.drawable.eating, "eating", R.string.eating_desc, R.color.stat_eating);
        this.addStat(R.drawable.water, "water", R.string.water_desc, R.color.stat_water);
        this.addStat(R.drawable.fitness, "fitness", R.string.fitness_desc, R.color.stat_fitness);
        this.addStat(R.drawable.sleep, "sleep", R.string.sleep_desc, R.color.stat_sleep);
    }

    private void addStat(int imageId, String name, int descId, int colorId) {
        if (!stats.containsKey(name)) stats.put(name, new Stat(imageId, name, descId, colorId, 50));
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

    public void updateFromQuiz(Quiz quiz) {
        ArrayList<Question> questions = quiz.getQuestions();
        for (Question question : questions) {
            updateStat(question.getQuestionType(), question.getAnswer());
        }

    }

    public Stat[] getStats() {return stats.values().toArray(new Stat[0]);}
}
