package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private Button playerLoginButton, adminLoginButton, playerSignupButton;
    private EditText newPlayerName, newPlayerEmail, userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        playerLoginButton = findViewById(R.id.playerLogin);
        adminLoginButton = findViewById(R.id.adminLogin);
        playerSignupButton = findViewById(R.id.signup);
        newPlayerName = findViewById(R.id.newPlayerName);
        newPlayerEmail = findViewById(R.id.newPlayerEmail);
        userName = findViewById(R.id.userName);

        // Initialize USER Table in DB
        CryptogramApp app = new CryptogramApp(getApplicationContext());
        app.initializeUsers();

        // ------------------------------------ Set Buttons ------------------------------------- //
        playerLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ playerLogin(userName.getText().toString()); }
        });

        adminLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                adminLogin(userName.getText().toString());
            }
        });

        playerSignupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { playerSignup(newPlayerName.getText().toString(), newPlayerEmail.getText().toString()); }
        });
    }

    public void login(String name){
        // user name cannot be empty
        if(name.length()==0) {
            userName.setError("please input user name!");
            return;
        }
        // load users from DB, and check if there exists a user with this name
        CryptogramApp app = new CryptogramApp(getApplicationContext());
        app.loadUsers();
        ArrayList<User> users = app.getUsers();
        User currentPlayer = CryptoUtils.searchUser(users, name);
        if (currentPlayer==null) {
            userName.setError("user name doesn't exists!");
        } else {
            Log.i("user details","currentPlayer>>>>>"+currentPlayer.getUserName()+">>>>>>>>>>>>>>>>class>>>>>>>>>>>>>>"+currentPlayer.getClass()+">>>>>>>>>>>>>>>>isAdmin>>>>>"+currentPlayer.getIsAdmin());
            if(currentPlayer.getIsAdmin()){
                Intent i = new Intent(this, CryptogramStatistics.class);
                startActivity(i);
            } else{
                Intent in = new Intent(this, PlayerMenu.class);
                in.putExtra("current_player", currentPlayer);
                startActivity(in);
            }

        }
    }

    public void playerLogin(String name){
        // user name cannot be empty
        if(name.length()==0) {
            userName.setError("please input user name!");
            return;
        }
        // load users from DB, and check if there exists a user with this name
        CryptogramApp app = new CryptogramApp(getApplicationContext());
        app.loadUsers();
        ArrayList<User> users = app.getUsers();
        User currentPlayer = CryptoUtils.searchUser(users, name);
        if (currentPlayer==null) {
            userName.setError("user name doesn't exists!");
        }
        else if (currentPlayer.getIsAdmin()){
            userName.setError("Please enter a player's username!");
        }
        else {
            Intent in = new Intent(this, PlayerMenu.class);
            in.putExtra("current_player", currentPlayer);
            startActivity(in);
        }
    }

    public void adminLogin(String name){
        // TODO: [On Hold - implement if multiple admins are required]
        //  check if the admin exists in USER Table in the DB
        if(name.length()==0) {
            userName.setError("please input user name!");
            return;
        }
        CryptogramApp app = new CryptogramApp(getApplicationContext());
        app.loadUsers();
        ArrayList<User> users = app.getUsers();
        User currentPlayer = CryptoUtils.searchUser(users, name);
        if (currentPlayer==null) {
            userName.setError("wrong administrator user name!");
        }else if(currentPlayer.getIsAdmin()){
            Intent i = new Intent(this, CryptogramStatistics.class);
            startActivity(i);
        } else{
            userName.setError("wrong administrator user name!");
        }
    }

    public void playerSignup(String name, String email){
        if(name.length()==0) {
            newPlayerName.setError("please input user name!");
            return;
        }
        if(email.length()==0) {
            newPlayerEmail.setError("please input email!");
            return;
        }
        if(!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            newPlayerEmail.setError("please input valid email!");
            return;
        }

        CryptogramApp app = new CryptogramApp(getApplicationContext());
        app.loadUsers();
        ArrayList<User> users = app.getUsers();
        User currentPlayer = CryptoUtils.searchUser(users, name);
        if (currentPlayer!=null) {
            newPlayerName.setError("existed user name!");
        } else {
            Player player = new Player(name, email);
            app.addPlayer(player);
            Intent i = new Intent(this, PlayerMenu.class);
            i.putExtra("current_player", player);
            startActivity(i);
        }
    }
}

