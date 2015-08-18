package com.mcmu.juanjesus.mcmuasteroids;

import java.util.Vector;

public class ArrayScoreStorage implements ScoreStorage {

    //region Private Member Variables
    private Vector<String> scores;
    //endregion

    //region Constructors
    public ArrayScoreStorage() {
        scores = new Vector<String>();
        scores.add("123000 Pepe1");
        scores.add("111000 Pepe2");
        scores.add("011000 Pepe3");
    }
    //endregion

    @Override
    public void saveScore(int score, String player, long date) {
        scores.add(0, score + " " + player);
    }

    @Override
    public Vector<String> scoreList(int amount) {
        return scores;
    }
}
