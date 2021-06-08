package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CryptogramStatistics extends AppCompatActivity {
    DBOps dbops;
    ListView cryptograms;
    private Button backButton;

    CryptogramAdapter cAdapter;
    ArrayList<Cryptogram> cryptogramL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cryptogram_statistics);

        cryptograms=(ListView)findViewById(R.id.cryptogramList);
        backButton = findViewById(R.id.back);

        dbops=new DBOps(this);
        cryptogramL=new ArrayList<Cryptogram>();
        //loadCryptograms();
        cryptogramL=dbops.loadCryptograms();
        //ArrayList <User> users=dbops.loadUsers();
        //String name=playerList.get(0).getUserName();
        cAdapter= new CryptogramAdapter(this,cryptogramL);

        cryptograms.setAdapter(cAdapter);

        cryptograms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(CryptogramStatistics.this,cryptogramL.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                Intent i=new Intent(view.getContext(),ViewCryptogram.class);
                i.putExtra("title",cryptogramL.get(position));
                startActivity(i);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CryptogramStatistics.this,Login.class);
                startActivity(i);
            }
        });

        cAdapter.notifyDataSetChanged();
    }

    //private void loadCryptograms() {


   // }
}
