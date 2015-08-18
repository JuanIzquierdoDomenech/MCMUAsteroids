package com.mcmu.juanjesus.mcmuasteroids;

import java.util.Vector;

public interface ScoreStorage {

    public void SaveScore(int score, String player, long date);
    public Vector<String> ScoreList(int amount);
}
