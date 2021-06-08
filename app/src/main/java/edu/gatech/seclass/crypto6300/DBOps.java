package edu.gatech.seclass.crypto6300;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBOps extends SQLiteOpenHelper {

    static final String DB_NAME = "Crypto6300";
    static final int DB_VERSION = 8;

    public DBOps(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable = "CREATE TABLE IF NOT EXISTS USERS " +
                "(USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERNAME TEXT UNIQUE, " +
                "EMAIL TEXT, " +
                "POINTS INTEGER, " +
                "PlAYEDCRY INTEGER, " +
                "IS_ADMIN INTEGER);";
        db.execSQL(createUserTable);

        String createCryptogramTable = "CREATE TABLE IF NOT EXISTS CRYPTOGRAMS " +
                "(CRYPTOGRAM_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TITLE TEXT UNIQUE, " +
                "SOLUTION TEXT, " +
                "ENCODED_PHRASE TEXT, " +
                "HINT TEXT, " +
                "CREATOR_NAME TEXT, " +
                "IS_ACTIVE INTEGER, " +
                "NUM_COMPLETED INTEGER, " +
                "NUM_SOLVED INTEGER);";
        db.execSQL(createCryptogramTable);

        String createCreateRelationTable = "CREATE TABLE IF NOT EXISTS CREATE_RELATION " +
                "(RELATION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CREATOR_NAME TEXT, " +
                "CRYPTOGRAM_NAME TEXT);";
        db.execSQL(createCreateRelationTable);

        String createAttemptRelationTable = "CREATE TABLE IF NOT EXISTS ATTEMPTED_RELATION " +
                "(RELATION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ATTEMPTER_NAME TEXT, " +
                "CRYPTOGRAM_NAME TEXT);";
        db.execSQL(createAttemptRelationTable);

        String createCryptogramStatusTable = "CREATE TABLE IF NOT EXISTS CRYPTOGRAM_STATUS " +
                "(STATUS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PLAYER_NAME TEXT UNIQUE, " +
                "CRYPTOGRAM_NAME TEXT, " +
                "BET INTEGER, " +
                "NUM_ATTEMPTS_REMAINING, INTEGER, " +
                "CURRENT_ATTEMPT, TEXT);";
        db.execSQL(createCryptogramStatusTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE USERS");
        db.execSQL("DROP TABLE CRYPTOGRAMS");
        db.execSQL("DROP TABLE CREATE_RELATION");
        db.execSQL("DROP TABLE ATTEMPTED_RELATION");
        db.execSQL("DROP TABLE CRYPTOGRAM_STATUS");
        onCreate(db);
    }

    // ------------------------------------------ User ------------------------------------------ //
    public void initializeUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        // default player
        ContentValues values = new ContentValues();
        values.put("USERNAME", "Orisa");
        values.put("EMAIL", "Orisa@gatech.edu");
        values.put("IS_ADMIN", 0);
        values.put("POINTS", 20);
        values.put("PLAYEDCRY", 0);
        try {
            db.insertOrThrow("USERS", null, values);
        } catch (SQLException e) {
            // TODO: handle exception here
        }

        // default admin
        values = new ContentValues();
        values.put("USERNAME", "admin");
        values.put("EMAIL", "admin@gatech.edu");
        values.put("IS_ADMIN", 1);
        try {
            db.insertOrThrow("USERS", null, values);
        } catch (SQLException e) {
            // TODO: handle exception here
        }
    }

    public void addPlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("USERNAME", player.getUserName());
        values.put("EMAIL", player.getEmail());
        values.put("POINTS", player.getPoints());
        values.put("IS_ADMIN", 0);
        values.put("PLAYEDCRY", 0);
        try {
            db.insertOrThrow("USERS", null, values);
        } catch (SQLException e) {
            // TODO: handle exception here
        }
    }

    public ArrayList<User> loadUsers() {
        // DB
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM USERS";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<User> users = new ArrayList<User>();

        try {
            while (cursor.moveToNext()) {
                String userName = cursor.getString(cursor.getColumnIndex("USERNAME"));
                String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
                int is_admin = cursor.getInt(cursor.getColumnIndex("IS_ADMIN"));
                Integer points = cursor.getInt(cursor.getColumnIndex("POINTS"));
                Integer playTimes = cursor.getInt(cursor.getColumnIndex("PlAYEDCRY"));
                Log.i("fromdb","username>>>>"+userName+">>>>>is_admin>>>>>"+is_admin);
                if (is_admin==0) {
//                    Player player = new Player(userName, email, points);
                    Player player = new Player(userName, email, points, playTimes);
                    users.add(player);
                } else {
                    Admin admin = new Admin(userName, email, true);
                    users.add(admin);
                }
            }
        } finally {
            cursor.close();
        }
        return users;
    }

    public ArrayList<Player> loadPlayers() {
        // DB
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT USERNAME,EMAIL,PLAYEDCRY,POINTS FROM USERS WHERE IS_ADMIN = ? ORDER BY POINTS DESC";
        Cursor cursor = db.rawQuery(query, new String[]{"0"});

        ArrayList<Player> players = new ArrayList<Player>();

        try {

            while (cursor.moveToNext()) {
                Log.v("out-data","PLAYEDCRY"+" "+cursor.getColumnIndex("PlAYEDCRY")+" "
                        +cursor.getColumnName(0)+" "+cursor.getColumnName(1)+" "+cursor.getColumnName(2)
                        +" "+cursor.getColumnName(3));
                String userName = cursor.getString(cursor.getColumnIndex("USERNAME"));
                String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
                Integer playedtimes = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2)));
                Integer points = cursor.getInt(cursor.getColumnIndex("POINTS"));
                Player player = new Player(userName, email, points, playedtimes);
                players.add(player);
            }
        } finally {
            cursor.close();
        }
        return players;
    }
    public Player getPlayer(String playerName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = null;
        String selection = "USERNAME = ?";
        String[] selectionArgs = {playerName};
        Cursor cursor = db.query("USERS", projection, selection, selectionArgs, null, null, null);

        Player player = null;
        try {
            while (cursor.moveToNext()) {
                String userName = cursor.getString(cursor.getColumnIndex("USERNAME"));
                String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
                Integer points = cursor.getInt(cursor.getColumnIndex("POINTS"));
                player = new Player(userName, email, points);
            }
        } finally {
            cursor.close();
        }
        return player;
    }

    public void updatePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("USERNAME", player.getUserName());
        values.put("EMAIL", player.getEmail());
        values.put("POINTS", player.getPoints());
        values.put("IS_ADMIN", 0);
        values.put("PLAYEDCRY", player.getPlayTimes());
        try {
            db.update("USERS", values, "USERNAME='" + player.getUserName() + "'", null);
        } catch (SQLException e) {
            // TODO: handle exception here
        }
    }

    // --------------------------------------- Cryptogram --------------------------------------- //
    public void addCryptogram(Cryptogram cryptogram) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("TITLE", cryptogram.getTitle());
        values.put("SOLUTION", cryptogram.getSolution());
        values.put("ENCODED_PHRASE", cryptogram.getEncodedPhrase());
        values.put("HINT", cryptogram.getHint());
        values.put("CREATOR_NAME", cryptogram.getCreatorName());
        values.put("IS_ACTIVE", cryptogram.getIsActive() ? 1 : 0);
        values.put("NUM_COMPLETED", cryptogram.getNumCompleted());
        values.put("NUM_SOLVED", cryptogram.getNumSolved());

        db.insertOrThrow("CRYPTOGRAMS", null, values);
    }

    public ArrayList<Cryptogram> loadCryptograms() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM CRYPTOGRAMS ORDER BY CRYPTOGRAM_ID DESC";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Cryptogram> cryptograms = new ArrayList<Cryptogram>();

        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                String solution = cursor.getString(cursor.getColumnIndex("SOLUTION"));
                String encodedPhrase = cursor.getString(cursor.getColumnIndex("ENCODED_PHRASE"));
                String hint = cursor.getString(cursor.getColumnIndex("HINT"));
                String creatorName = cursor.getString(cursor.getColumnIndex("CREATOR_NAME"));
                int isActiveInt = cursor.getInt(cursor.getColumnIndex("IS_ACTIVE"));
                Boolean isActive = (isActiveInt==1);
                int numCompleted = cursor.getInt(cursor.getColumnIndex("NUM_COMPLETED"));
                int numSolved = cursor.getInt(cursor.getColumnIndex("NUM_SOLVED"));

                Cryptogram cryptogram = new Cryptogram(title, solution, encodedPhrase, hint, creatorName, isActive, numCompleted, numSolved);
                cryptograms.add(cryptogram);
            }
        } finally {
            cursor.close();
        }
        return cryptograms;
    }

    public Cryptogram getCryptogram(String cryptogramTitle) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = null;
        String selection = "TITLE = ?";
        String[] selectionArgs = {cryptogramTitle};
        Cursor cursor = db.query("CRYPTOGRAMS", projection, selection, selectionArgs, null, null, null);

        Cryptogram cryptogram = null;
        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                String solution = cursor.getString(cursor.getColumnIndex("SOLUTION"));
                String encodedPhrase = cursor.getString(cursor.getColumnIndex("ENCODED_PHRASE"));
                String hint = cursor.getString(cursor.getColumnIndex("HINT"));
                String creatorName = cursor.getString(cursor.getColumnIndex("CREATOR_NAME"));
                int isActiveInt = cursor.getInt(cursor.getColumnIndex("IS_ACTIVE"));
                Boolean isActive = (isActiveInt==1);
                int numCompleted = cursor.getInt(cursor.getColumnIndex("NUM_COMPLETED"));
                int numSolved = cursor.getInt(cursor.getColumnIndex("NUM_SOLVED"));

                cryptogram = new Cryptogram(title, solution, encodedPhrase, hint, creatorName, isActive, numCompleted, numSolved);
            }
        } finally {
            cursor.close();
        }
        return cryptogram;
    }

    public void updateCryptogram(Cryptogram cryptogram) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("TITLE", cryptogram.getTitle());
        values.put("SOLUTION", cryptogram.getSolution());
        values.put("ENCODED_PHRASE", cryptogram.getEncodedPhrase());
        values.put("HINT", cryptogram.getHint());
        values.put("CREATOR_NAME", cryptogram.getCreatorName());
        values.put("IS_ACTIVE", cryptogram.getIsActive() ? 1 : 0);
        values.put("NUM_COMPLETED", cryptogram.getNumCompleted());
        values.put("NUM_SOLVED", cryptogram.getNumSolved());

        db.update("CRYPTOGRAMS", values, "TITLE='" + cryptogram.getTitle() + "'", null);
    }

    // ---------------------------- Cryptogram-Player Relationships ----------------------------- //
    public void addCreateRelation(String creatorName, String cryptogramTitle) {
        /** Depreciated */
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CREATOR_NAME", creatorName);
        values.put("CRYPTOGRAM_NAME", cryptogramTitle);

        db.insertOrThrow("CREATE_RELATION", null, values);
    }

    public ArrayList<String> loadCryptogramsCreatedBy(String creatorName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"TITLE"};
        String selection = "CREATOR_NAME = ?";
        String[] selectionArgs = { creatorName };

        Cursor cursor = db.query("CRYPTOGRAMS", projection, selection, selectionArgs, null, null, null);

        ArrayList<String> cryptogramTitles = new ArrayList<String>();

        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                cryptogramTitles.add(title);
            }
        } finally {
            cursor.close();
        }
        return cryptogramTitles;
    }

    public void addAttemptedRelation(CryptogramStatus cryptogramStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        String attempterName = cryptogramStatus.getPlayer().getUserName();
        String cryptogramTitle = cryptogramStatus.getCryptogram().getTitle();

        ContentValues values = new ContentValues();
        values.put("ATTEMPTER_NAME", attempterName);
        values.put("CRYPTOGRAM_NAME", cryptogramTitle);

        db.insertOrThrow("ATTEMPTED_RELATION", null, values);
    }

    public ArrayList<String> loadCryptogramsAttemptedBy(String attempterName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"CRYPTOGRAM_NAME"};
        String selection = "ATTEMPTER_NAME = ?";
        String[] selectionArgs = { attempterName };
        Cursor cursor = db.query("ATTEMPTED_RELATION", projection, selection, selectionArgs, null, null, null);

        ArrayList<String> cryptogramTitles = new ArrayList<String>();

        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("CRYPTOGRAM_NAME"));
                cryptogramTitles.add(title);
            }
        } finally {
            cursor.close();
        }
        return cryptogramTitles;
    }

    public void addCryptogramStatus(CryptogramStatus cryptogramStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PLAYER_NAME", cryptogramStatus.getPlayer().getUserName());
        values.put("CRYPTOGRAM_NAME", cryptogramStatus.getCryptogram().getTitle());
        values.put("BET", cryptogramStatus.getBet());
        values.put("NUM_ATTEMPTS_REMAINING", cryptogramStatus.getNumAttemptsRemaining());
        values.put("CURRENT_ATTEMPT", cryptogramStatus.getCurrentAttempt());

        db.insertOrThrow("CRYPTOGRAM_STATUS", null, values);
    }

    public CryptogramStatus getCryptogramStatus(String playerName) {
        /**
         * return null if the cryptogramStatus of interest does not exist
         * */
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = null;
        String selection = "PLAYER_NAME = ?";
        String[] selectionArgs = {playerName};
        Cursor cursor = db.query("CRYPTOGRAM_STATUS", projection, selection, selectionArgs, null, null, null);

        CryptogramStatus cryptogramStatus = null;

        try {
            while (cursor.moveToNext()) {
                String playerName_ = cursor.getString(cursor.getColumnIndex("PLAYER_NAME"));
                String cryptogramTitle = cursor.getString(cursor.getColumnIndex("CRYPTOGRAM_NAME"));
                Integer bet = cursor.getInt(cursor.getColumnIndex("BET"));
                Integer numAttemptsRemaining = cursor.getInt(cursor.getColumnIndex("NUM_ATTEMPTS_REMAINING"));
                String currentAttempt = cursor.getString(cursor.getColumnIndex("CURRENT_ATTEMPT"));

                // get player and cryptogram
                Player player = getPlayer(playerName_);
                Cryptogram cryptogram = getCryptogram(cryptogramTitle);

                cryptogramStatus = new CryptogramStatus(player, cryptogram, bet, numAttemptsRemaining, currentAttempt);
            }
        } finally {
            cursor.close();
        }
        return cryptogramStatus;
    }

    public void updateCryptogramStatus(CryptogramStatus cryptogramStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PLAYER_NAME", cryptogramStatus.getPlayer().getUserName());
        values.put("CRYPTOGRAM_NAME", cryptogramStatus.getCryptogram().getTitle());
        values.put("BET", cryptogramStatus.getBet());
        values.put("NUM_ATTEMPTS_REMAINING", cryptogramStatus.getNumAttemptsRemaining());
        values.put("CURRENT_ATTEMPT", cryptogramStatus.getCurrentAttempt());

        db.update("CRYPTOGRAM_STATUS", values, "PLAYER_NAME='" + cryptogramStatus.getPlayer().getUserName() + "'", null);
    }

    public Boolean removeCryptogramStatus(CryptogramStatus cryptogramStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("CRYPTOGRAM_STATUS",
                "PLAYER_NAME='" + cryptogramStatus.getPlayer().getUserName() + "'",
                null) > 0;
    }
}
