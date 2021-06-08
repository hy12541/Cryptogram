package edu.gatech.seclass.crypto6300;

import android.content.Context;

import java.util.ArrayList;

public class CryptogramApp {

    private ArrayList<User> users;
    private ArrayList<Cryptogram> cryptograms;
    private ArrayList<CryptogramStatus> cryptogramStatusList;

    private ArrayList<String> cryptogramsCreatedByCurrentPlayer, cryptogramsAttemptedByCurrentPlayer;

    private DBOps dbOps;

    public CryptogramApp(Context context){
        users = new ArrayList<User>();
        cryptograms = new ArrayList<Cryptogram>();
        cryptogramStatusList = new ArrayList<CryptogramStatus>();
        dbOps = new DBOps(context);
    }

    // ------------------------------------------ User ------------------------------------------ //
    public void addPlayer(Player player) {
        dbOps.addPlayer(player);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void initializeUsers() {
        dbOps.initializeUsers();
    }

    public void loadUsers(){
        users = dbOps.loadUsers();
    }

    public void updatePlayer(Player player) {
        dbOps.updatePlayer(player);
    }

    // --------------------------------------- Cryptogram --------------------------------------- //
    public ArrayList<Cryptogram> getCryptograms() {
        return cryptograms;
    }

    public void addCryptogram(Cryptogram cryptogram) {
        dbOps.addCryptogram(cryptogram);
    }

    public void loadCryptograms() {
        cryptograms = dbOps.loadCryptograms();
    }

    public void updateCryptogram(Cryptogram cryptogram) {
        dbOps.updateCryptogram(cryptogram);
    }

    // ---------------------------- Cryptogram-Player Relationships ----------------------------- //
    public ArrayList<String> getCryptogramsCreatedByCurrentPlayer() {
        return cryptogramsCreatedByCurrentPlayer;
    }

    public void addCreateRelation(String creatorName, String cryptogramTitle) {
        /** Depreciated */
        dbOps.addCreateRelation(creatorName, cryptogramTitle);
    }

    public void loadCryptogramsCreatedBy(String creatorName) {
        cryptogramsCreatedByCurrentPlayer = dbOps.loadCryptogramsCreatedBy(creatorName);
    }

    public ArrayList<String> getCryptogramsAttemptedByCurrentPlayer() {
        return cryptogramsAttemptedByCurrentPlayer;
    }

    public void addAttemptedRelation(CryptogramStatus cryptogramStatus) {
        dbOps.addAttemptedRelation(cryptogramStatus);
    }

    public void loadCryptogramsAttemptedBy(String attempterName) {
        cryptogramsAttemptedByCurrentPlayer = dbOps.loadCryptogramsAttemptedBy(attempterName);
    }

    public void addCryptogramStatus(CryptogramStatus cryptogramStatus) {
        dbOps.addCryptogramStatus(cryptogramStatus);
    }

    public CryptogramStatus getCryptogramStatus(String playerName) {
        return dbOps.getCryptogramStatus(playerName);
    }

    public Boolean removeCryptogramStatus(CryptogramStatus cryptogramStatus) {
        return dbOps.removeCryptogramStatus(cryptogramStatus);
    }

    public Player getPlayerByName(String playerName){
        return dbOps.getPlayer(playerName);
    }

    public void updateCryptogramStatus(CryptogramStatus cryptogramStatus) {
        dbOps.updateCryptogramStatus(cryptogramStatus);
    }
}
