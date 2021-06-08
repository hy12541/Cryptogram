package edu.gatech.seclass.crypto6300;

        import android.app.Application;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.KeyEvent;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class SolveCryptogram extends AppCompatActivity {
    private TextView titleTV, encodedPhraseTV, currentAttemptTV, attemptsRemainingTV, hintTV;
    private Spinner replaceFromSpinner, replaceToSpinner;
    private Button replaceButton, submitButton, backButton;

    private CryptogramStatus cryptogramStatus;

    private Map<Character, List<Integer>> char2IndexMapEnco;
    private Map<Character, List<Integer>> char2IndexMapSolu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solve_cryptogram);

        titleTV = findViewById(R.id.title);
        encodedPhraseTV = findViewById(R.id.encodedPhraseSC);
        currentAttemptTV = findViewById(R.id.currentAttemptSC);
        attemptsRemainingTV = findViewById(R.id.attemptsRemainingSC);
        hintTV = findViewById(R.id.hintSC);
        replaceFromSpinner = findViewById(R.id.replaceFromSC);
        replaceToSpinner = findViewById(R.id.replaceToSC);
        replaceButton = findViewById(R.id.replaceSC);
        submitButton = findViewById(R.id.submitSC);
        backButton = findViewById(R.id.backSC);

        // assign CryptogramStatus object that transferred from Betpoints
        Intent intent = getIntent();
        cryptogramStatus = (CryptogramStatus) intent.getSerializableExtra("current_cryptogram_status");

        // set values for TextView
        titleTV.setText(cryptogramStatus.getCryptogram().getTitle());
        encodedPhraseTV.setText(cryptogramStatus.getCryptogram().getEncodedPhrase());
        currentAttemptTV.setText(cryptogramStatus.getCurrentAttempt());
        attemptsRemainingTV.setText(String.valueOf(cryptogramStatus.getNumAttemptsRemaining()));

        // ------------------------------------ Set Listener ------------------------------------ //
        replaceFromSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {  // TODO: onTouch
                // populate replaceFrom spinner using unique letters from solution
                ArrayList<Character> uniqChars = CryptoUtils.uniqueCharacters(encodedPhraseTV.getText().toString());
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter(SolveCryptogram.this, android.R.layout.simple_spinner_item, uniqChars);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                replaceFromSpinner.setAdapter(adapter);

                // analyze encoded phrase
                char2IndexMapEnco = CryptoUtils.analyze(encodedPhraseTV.getText().toString());
                return false;
            }
        });

        replaceFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                char selectedItemChar = selectedItem.charAt(0);

                // populate replaceTo spinner using values from resource file
                ArrayAdapter<CharSequence> adapter;
                if (Character.isUpperCase(selectedItemChar)) {
                    adapter = ArrayAdapter.createFromResource(SolveCryptogram.this,
                            R.array.upperCaseLetters, android.R.layout.simple_spinner_item);
                } else {
                    adapter = ArrayAdapter.createFromResource(SolveCryptogram.this,
                            R.array.lowerCaseLetters, android.R.layout.simple_spinner_item);
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                replaceToSpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        currentAttemptTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String replaceFrom = replaceFromSpinner.getSelectedItem().toString();
                String replaceTo = replaceToSpinner.getSelectedItem().toString();
                if(replaceFrom.equals(replaceTo)){
                    currentAttemptTV.requestFocus();
                    currentAttemptTV.setError("letter cannot be replaced by its own!");
                    return;
                }

                if (currentAttemptTV.getText().length() > 0) {
                    currentAttemptTV.requestFocus();
                    currentAttemptTV.setError(null);
                }
            }
        });

        replaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call encode to replace letters and display encoded phrase
                encode();
                currentAttemptTV.setText(cryptogramStatus.getCurrentAttempt());
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // before leave, update the DB
                CryptogramApp app = new CryptogramApp(getApplicationContext());
                app.updateCryptogramStatus(cryptogramStatus);

                Intent i = new Intent(SolveCryptogram.this, PlayerMenu.class);
                i.putExtra("current_player", cryptogramStatus.getPlayer());
                startActivity(i);
            }
        });
    }

    private void encode() {
        if(replaceFromSpinner.getSelectedItem()==null || replaceToSpinner.getSelectedItem()==null){
            currentAttemptTV.setError("please choose a letter");
            return;
        }
        String replaceFrom = replaceFromSpinner.getSelectedItem().toString();
        String replaceTo = replaceToSpinner.getSelectedItem().toString();
        char replaceFromChar = replaceFrom.charAt(0);
        char replaceToChar = replaceTo.charAt(0);

        char[] solutionCharArray = cryptogramStatus.getCurrentAttempt().toCharArray();
        List<Integer> targetIdx = char2IndexMapEnco.get(replaceFromChar);
        for (int idx : targetIdx) {
            solutionCharArray[idx] = replaceToChar;
        }
        String temp = new String(solutionCharArray);
        cryptogramStatus.setCurrentAttempt(temp);
    }

    private void submit() {
        String encodedPhrase = encodedPhraseTV.getText().toString();
        String currentAttempt = currentAttemptTV.getText().toString();

        // check replacement
        for(int i=0; i<encodedPhrase.length(); i++){
            if(Character.isLetter(encodedPhrase.charAt(i))){
                if(encodedPhrase.charAt(i)==currentAttempt.charAt(i)){
                    currentAttemptTV.requestFocus();
                    currentAttemptTV.setError("Not all unique letters have been replaced!");
                    return;
                }
            }
        }

        // check if letter is replaced by unique letter
        char2IndexMapSolu = CryptoUtils.analyze(currentAttempt);
        for (int i=0; i<currentAttempt.length(); i++) {
            List<Integer> idxList = char2IndexMapSolu.get(currentAttempt.charAt(i));
            Character pre = encodedPhrase.charAt(idxList.get(0));
            for (Integer idx :idxList) {
                if (encodedPhrase.charAt(idx) != pre) {
                    currentAttemptTV.requestFocus();
                    currentAttemptTV.setError("different letters cannot be replaced to a same letter");
                    return;
                }
            }
        }

        CryptogramApp app = new CryptogramApp(getApplicationContext());

        // update the number of attempts remaining
        cryptogramStatus.updateNumAttemptsRemaining(-1);

        // Setup - Return to PlayerMenu
        Intent i = new Intent(SolveCryptogram.this, PlayerMenu.class);

        // check if currentAttempt is right or not
        if (cryptogramStatus.getCryptogram().getSolution().equals(cryptogramStatus.getCurrentAttempt())) {
            // display a message
            Toast toast = Toast.makeText(SolveCryptogram.this, "Congratulations!", Toast.LENGTH_SHORT);
            toast.show();

            //
            winUpdate(app);

            // return to PlayerMenu
            i.putExtra("current_player", cryptogramStatus.getPlayer());
            startActivity(i);
        } else {
            if (cryptogramStatus.getNumAttemptsRemaining()==0) {
                // display a message
                Toast toast = Toast.makeText(SolveCryptogram.this, "The game was lost.", Toast.LENGTH_SHORT);
                toast.show();

                //
                loseUpdate(app);

                // return to PlayerMenu
                i.putExtra("current_player", cryptogramStatus.getPlayer());
                startActivity(i);

            } else {
                Integer num = cryptogramStatus.getNumAttemptsRemaining();
                attemptsRemainingTV.setText(String.valueOf(num));
                Toast toast = Toast.makeText(SolveCryptogram.this, "Wrong Answer. Try Again", Toast.LENGTH_SHORT);
                toast.show();
                if (num<=2) {
                    hintTV.setText(cryptogramStatus.getCryptogram().getHint());
                }
            }
        }
    }

    private void winUpdate(CryptogramApp app) {
        //------------------ cryptogramStatus Update ------------------//
        if (cryptogramStatus.getNumAttemptsRemaining()>2) {
            cryptogramStatus.getPlayer().updatePoints(cryptogramStatus.getBet());
        }
        cryptogramStatus.getCryptogram().updateNumCompleted(1);
        cryptogramStatus.getCryptogram().updateNumSolved(1);
        cryptogramStatus.getPlayer().updatePlayTimes();

        //------------------ Database Table Update ------------------//
        app.updateCryptogram(cryptogramStatus.getCryptogram());
        app.updatePlayer(cryptogramStatus.getPlayer());
        app.addAttemptedRelation(cryptogramStatus);
        app.removeCryptogramStatus(cryptogramStatus);
    }

    private void loseUpdate(CryptogramApp app) {
        //------------------ cryptogramStatus Update ------------------//
        cryptogramStatus.getPlayer().updatePoints(-cryptogramStatus.getBet());
        cryptogramStatus.getCryptogram().updateNumCompleted(1);
        cryptogramStatus.getPlayer().updatePlayTimes();

        //------------------ Database Table Update ------------------//
        app.updateCryptogram(cryptogramStatus.getCryptogram());
        app.updatePlayer(cryptogramStatus.getPlayer());
        app.addAttemptedRelation(cryptogramStatus);
        app.removeCryptogramStatus(cryptogramStatus);
    }
}