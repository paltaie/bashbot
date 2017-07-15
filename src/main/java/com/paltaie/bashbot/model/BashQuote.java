package com.paltaie.bashbot.model;

public class BashQuote {
    private String text;
    private Integer score;

    public BashQuote(String text, Integer score) {
        this.text = text;
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "BashQuote{" +
                "text='" + text + '\'' +
                ", score=" + score +
                '}';
    }
}
