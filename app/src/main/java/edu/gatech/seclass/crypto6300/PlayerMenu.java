package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayerMenu extends AppCompatActivity {
    private Button createCryptogramButton, solveCryptogramButton, viewPlayersButton, logOutButton;
    private TextView currentPlayerNameTV, currentPlayerPointsTV;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_menu);

        createCryptogramButton = findViewById(R.id.createCryptogram);
        solveCryptogramButton = findViewById(R.id.solveCryptogram);
        viewPlayersButton =  findViewById(R.id.viewPlayers);
        logOutButton = findViewById(R.id.logout);
        currentPlayerNameTV = findViewById(R.id.currentPlayerName);
        currentPlayerPointsTV = findViewById(R.id.currentPlayerPoints);

        // retrieve play object from page redirected from - the Login page
        Intent intent = getIntent();
        player = (Player) intent.getSerializableExtra("current_player");

        // display current player's name and points
        currentPlayerNameTV.setText(player.getUserName());
        currentPlayerPointsTV.setText(String.valueOf(player.getPoints()));

        setListeners();
    }

    private void setListeners() {
        createCryptogramButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(PlayerMenu.this, NewCryptogram.class);
                i.putExtra("current_player", player);
                startActivity(i);
            }
        });
        solveCryptogramButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                solve();
            }
        });
        viewPlayersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(PlayerMenu.this, PlayerStatistics.class);
                startActivity(i);
            }
        });
        logOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(PlayerMenu.this, Login.class);
                startActivity(i);
            }
        });
    }

    private void solve() {
        // check if the current player has an attempting game by loading cryptogramStatus
        // if so, send this cryptogramStatus to the next page
        CryptogramApp app = new CryptogramApp(getApplicationContext());
        CryptogramStatus cryptogramStatus = app.getCryptogramStatus(player.getUserName());

        // if not, create a cryptogramStatus and send it to the next page
        if (cryptogramStatus==null){
            // randomly select a cryptogram
            app.loadCryptograms();
            ArrayList<String> cryptogramTitles = CryptoUtils.getCryptogramTitles(app);

            app.loadCryptogramsCreatedBy(player.getUserName());
            ArrayList<String> createdCryptogramsTitles = app.getCryptogramsCreatedByCurrentPlayer();
            app.loadCryptogramsAttemptedBy(player.getUserName());
            ArrayList<String> attemptedCryptogramsTitles = app.getCryptogramsAttemptedByCurrentPlayer();

            String selectedCryptogramTitle = CryptoUtils.randomSelect(cryptogramTitles, createdCryptogramsTitles, attemptedCryptogramsTitles);
            if (selectedCryptogramTitle==null) {
                Toast toast = Toast.makeText(PlayerMenu.this,
                        "No Cryptogram is available for you! Please login as an another player", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // get cryptogram from app by matching the selectedCryptogramTitle
                Cryptogram selectedCryptogram = CryptoUtils.getCryptogram(app, selectedCryptogramTitle);

                Intent i = new Intent(PlayerMenu.this, BetPoints.class);
                i.putExtra("current_player", player);
                i.putExtra("current_cryptogram", selectedCryptogram);
                startActivity(i);
            }
        } else {
            Intent i = new Intent(PlayerMenu.this, SolveCryptogram.class);
            i.putExtra("current_cryptogram_status", cryptogramStatus);
            startActivity(i);
        }
    }
}