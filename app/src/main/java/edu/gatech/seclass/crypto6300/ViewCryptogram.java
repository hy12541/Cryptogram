package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import edu.gatech.seclass.crypto6300.R;

public class ViewCryptogram extends AppCompatActivity{
    private Cryptogram cryptogram;
    private TextView creator, title,solution,hint,encode;
    private Spinner penalize;
    private Button backButton;
    private Button disableCryptogramButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cryptogram_view);

        title=findViewById(R.id.cryViewTitle);
        solution=findViewById(R.id.solCryView);
        creator=findViewById(R.id.creatorCryView);
        hint=findViewById(R.id.hintCryView);
        encode=findViewById(R.id.encodeCryView);
        backButton = findViewById(R.id.backCryView);
        disableCryptogramButton = findViewById(R.id.disable);

        Intent intent = getIntent();
        cryptogram = (Cryptogram) intent.getSerializableExtra("title");
        title.setText(cryptogram.getTitle());
        solution.setText(cryptogram.getSolution());
        creator.setText(cryptogram.getCreatorName());
        hint.setText(cryptogram.getHint());
        encode.setText(cryptogram.getEncodedPhrase());

        penalize=(Spinner) findViewById(R.id.penalize);
        Integer[] items = new Integer[]{0,1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        penalize.setAdapter(adapter);
        if(!cryptogram.getIsActive()){
            penalize.setEnabled(false);
            disableCryptogramButton.setEnabled(false);
        }

        disableCryptogramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(disableCryptogram(cryptogram)){
                   // TODO: put this into submit
                   Intent i = new Intent(ViewCryptogram.this, CryptogramStatistics.class);
                   startActivity(i);
               } else {
                   creator.requestFocus();
                   creator.setError("The player has less points");

               }



            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewCryptogram.this, CryptogramStatistics.class);
                startActivity(i);
            }
        });

    }
    private boolean disableCryptogram(Cryptogram cryptogram){
        //String cryptogram

        String cryptogramCreator = cryptogram.getCreatorName();
        if(penalizeCreator(cryptogramCreator)){
            cryptogram.setIsActive(false);
            CryptogramApp app = new CryptogramApp(getApplicationContext());
            app.updateCryptogram(cryptogram);
            return true;
        }
        return false;

    }

    private boolean penalizeCreator(String creatorName){
        CryptogramApp app = new CryptogramApp(getApplicationContext());
        Integer penaltyPoints =  (Integer)  penalize.getSelectedItem();
        Player creatorObj = app.getPlayerByName(creatorName);
        if(creatorObj.getPoints() >= penaltyPoints){
            creatorObj.updatePoints(-1*penaltyPoints);
            app.updatePlayer(creatorObj);
            return true;
        } else {

            return false;
        }

    }
}
