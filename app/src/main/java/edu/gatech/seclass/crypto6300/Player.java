package edu.gatech.seclass.crypto6300;
import java.util.*;

public class Player extends User{
    private int points;
    private int playTimes;


    public Player(String name, String emailAddress) {
        super(name, emailAddress,false);
        points = 20;
    }

    public Player(String name, String emailAddress, Integer p) {
        super(name, emailAddress,false);
        points = p;
    }

    public Player(String name, String emailAddress, Integer p, Integer pt) {
        super(name, emailAddress,false);
        points = p;
        playTimes = pt;
    }

    public void updatePoints(Integer p) {
        points += p;
    }

    public int getPoints() { return points; }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPlayTimes() { return playTimes; }

    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    public void updatePlayTimes() {
        playTimes += 1;
    }

}
