package edu.gatech.seclass.crypto6300;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;

public class PlayerStatistics extends AppCompatActivity {
    DBOps dbops;
    ListView players;
    private Button backButton;
    Adapter adapter;
    ArrayList<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_statistics);

        backButton = findViewById(R.id.back);
        players=(ListView)findViewById(R.id.playerList);
        dbops=new DBOps(this);
        playerList=new ArrayList<Player>();
        loadPlayers();

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        //final ArrayList<String> arrayList=new ArrayList<>();
        //arrayList.add("User Name          Games Attempted          Points");
       // ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        //players.setAdapter(arrayAdapter);
    }

    private void loadPlayers(){
        playerList=dbops.loadPlayers();

        adapter= new Adapter(this,playerList);
        players.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
