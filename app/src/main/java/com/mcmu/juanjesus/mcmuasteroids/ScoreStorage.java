package com.mcmu.juanjesus.mcmuasteroids;

import java.util.Vector;

public interface ScoreStorage {

    public void saveScore(int score, String player, long date);
    public Vector<String> scoreList(int amount);
}
