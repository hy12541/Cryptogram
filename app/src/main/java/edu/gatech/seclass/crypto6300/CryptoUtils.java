package edu.gatech.seclass.crypto6300;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CryptoUtils {
    public static ArrayList<String> getCryptogramTitles(CryptogramApp app) {
        ArrayList<String> cryptogramTitles = new ArrayList<>();

        ArrayList<Cryptogram> cryptograms = app.getCryptograms();
        for (Cryptogram c : cryptograms) {
            if(c.getIsActive()){
                cryptogramTitles.add(c.getTitle());
            }
        }
        return cryptogramTitles;
    }

    public static String randomSelect(ArrayList<String> games, ArrayList<String> attemptedGames, ArrayList<String> createdGames) {
        Set<String> gamesSet = new HashSet<String>(games);
        Set<String> attemptedGamesSet = new HashSet<String>(attemptedGames);
        Set<String> createdGamesSet = new HashSet<String>(createdGames);

        gamesSet.removeAll(attemptedGamesSet);
        gamesSet.removeAll(createdGamesSet);

        ArrayList<String> filteredGames = new ArrayList(gamesSet);
        int size = filteredGames.size();
        try {
            int i = new Random().nextInt(size);
            return filteredGames.get(i);
        } catch (Exception IllegalArgumentException) {
            return null;
        }
    }

    public static Cryptogram getCryptogram(CryptogramApp app, String cryptogramTitle) {
        Cryptogram selectedCryptogram = null;
        ArrayList<Cryptogram> cryptograms = app.getCryptograms();
        for (Cryptogram c : cryptograms) {
            if (c.getTitle().equals(cryptogramTitle)) {
                selectedCryptogram = c;
            }
        }
        return selectedCryptogram;
    }

    public static User searchUser(ArrayList<User> users, String userName) {
        User currentPlayer = null;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserName().equalsIgnoreCase(userName)){
                currentPlayer = users.get(i);
            }
        }
        return currentPlayer;
    }

    public static HashMap<Character, List<Integer>> analyze(String phrase) {
        /**
         * This function analyzes solution to create a map object, which takes each char from solution
         * as key, and a list of its multiple positions as value.
         * */
        // TODO: filter special characters
        // (re-)initialize map
        HashMap<Character, List<Integer>> map = new HashMap<>();

        for (int i=0; i<phrase.length(); i++) {
            Character c = phrase.charAt(i);
            List<Integer> idx;
            if (!map.containsKey(c)) {
                idx = new ArrayList<>();
            } else {
                idx = map.get(c);
            }
            idx.add(i);
            map.put(c, idx);
        }
        return map;
    }

    public static ArrayList<Character> uniqueCharacters(String origPhrase) {
        String filteredPhrase = origPhrase.replaceAll("[^a-zA-Z]","");
        ArrayList<Character> uniqCharList = new ArrayList<>();
        for (char c : filteredPhrase.toCharArray()) {
            if (!uniqCharList.contains(c)) {
                uniqCharList.add(c);
            }
        }
        return uniqCharList;
    }
}
