package edu.gatech.seclass.crypto6300;

import java.io.Serializable;

public class CryptogramStatus implements Serializable {
    private Player player;
    private Cryptogram cryptogram;
    private Integer bet, numAttemptsRemaining;
    private String currentAttempt;

    public CryptogramStatus(Player currentPlayer, Cryptogram currentCryptogram, Integer currentBet) {
        player = currentPlayer;
        cryptogram = currentCryptogram;
        bet = currentBet;
        numAttemptsRemaining = 5;
        currentAttempt = currentCryptogram.getEncodedPhrase();
    }

    public CryptogramStatus(Player currentPlayer, Cryptogram currentCryptogram, Integer currentBet,
                            Integer AttemptsRemaining, String currentAttemptPhrase) {
        player = currentPlayer;
        cryptogram = currentCryptogram;
        bet = currentBet;
        numAttemptsRemaining = AttemptsRemaining;
        currentAttempt = currentAttemptPhrase;
    }

    public void setPlayer(Player currentPlayer) {
        player = currentPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public void setCryptogram(Cryptogram currentCryptogram) {
        cryptogram = currentCryptogram;
    }

    public Cryptogram getCryptogram() {
        return cryptogram;
    }

    public void setBet(Integer currentBet) {
        bet = currentBet;
    }

    public Integer getBet() {
        return bet;
    }

    public void updateNumAttemptsRemaining(Integer num) {
        numAttemptsRemaining += num;
    }

    public Integer getNumAttemptsRemaining() {
        return numAttemptsRemaining;
    }

    public void setCurrentAttempt(String curAttempt) {
        currentAttempt = curAttempt;
    }

    public String getCurrentAttempt() {
        return currentAttempt;
    }
}
