package edu.hitsz.dao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ScoreRecord implements Serializable {
    private String username;
    private int score;
    private LocalDateTime recordTime;

    public ScoreRecord(String username, int score, LocalDateTime recordTime) {
        this.username = username;
        this.score = score;
        this.recordTime = recordTime;
    }

    public int getScore(){
        return score;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("%s, %d, %s", username, score, recordTime.format(formatter));
    }
}
