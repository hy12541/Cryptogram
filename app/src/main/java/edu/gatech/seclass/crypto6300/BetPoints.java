package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class BetPoints extends AppCompatActivity {
    private EditText pointsBetET;
    private Button submitButton, backButton;

    private Player player;
    private Cryptogram cryptogram;
    private Integer pointsBet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bet_points);

        submitButton = findViewById(R.id.submitBP);
        backButton = findViewById(R.id.backBP);
        pointsBetET = findViewById(R.id.pointsBetBP);

        // assign player and cryptogram object that transferred from PlayerMenu
        Intent intent = getIntent();
        player = (Player) intent.getSerializableExtra("current_player");
        cryptogram = (Cryptogram) intent.getSerializableExtra("current_cryptogram");

        // ------------------------------------ Set Listener ------------------------------------ //
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPointsBetValid()) {
                    submit();
                } else {
                    Toast toast = Toast.makeText(BetPoints.this,
                            "Please enter valid points to bet", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private Boolean isPointsBetValid(){
        // 1 <= pointsBet <= 10
        // points bet = min(player's current points, pointsBet)
        String pointsBetStr = pointsBetET.getText().toString();
        try{
            pointsBet = Integer.valueOf(pointsBetStr);
            Integer pointsBalance = player.getPoints();
            if (pointsBet>10 || pointsBet<1) {
                pointsBetET.setError("Points bet should be an integer between 1 and 10, inclusive.");
                return false;
            } else if (pointsBet > pointsBalance) {
                pointsBetET.setError("Points balance is " + pointsBalance);
                return false;
            } else {
                // TODO: display a message "points bet is valid"
                return true;
            }
        } catch(Exception e){
            pointsBetET.setError("Points bet should be an integer between 1 and 10, inclusive.");
            return false;
        }
    }

    private void submit() {
        // Initialize a CryptogramStatus object
        CryptogramStatus cryptogramStatus = new CryptogramStatus(player, cryptogram, pointsBet);

        // Add the CryptogramStatus object to DB
        CryptogramApp app = new CryptogramApp(getApplicationContext());
        app.addCryptogramStatus(cryptogramStatus);

        // redirected to SolveCryptogram
        Intent i = new Intent(BetPoints.this, SolveCryptogram.class);
        i.putExtra("current_cryptogram_status", cryptogramStatus);
        startActivity(i);
    }
}
