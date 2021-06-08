package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewCryptogram extends AppCompatActivity {
    private EditText titleET, solutionET, hintET;
    private Spinner replaceFromSpinner, replaceToSpinner;
    private TextView encodedPhraseTV;
    private Button replaceButton, submitButton, backButton;

    private Player player;

    private Map<Character, List<Integer>> char2IndexMapSolu;
    private Map<Character, List<Integer>> char2IndexMapEnco;
    private String currentAttempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_cryptogram);

        titleET = findViewById(R.id.titleNC);
        solutionET = findViewById(R.id.solutionNC);
        hintET = findViewById(R.id.hintNC);
        replaceFromSpinner = findViewById(R.id.replaceFromNC);
        replaceToSpinner = findViewById(R.id.replaceToNC);
        encodedPhraseTV = findViewById(R.id.encodedPhraseNC);
        replaceButton = findViewById(R.id.replaceNC);
        submitButton = findViewById(R.id.submitNC);
        backButton = findViewById(R.id.backNC);


//        Character[] items = new Character[]{'a', 'b', 'c'};
//        ArrayList<Character> uniqChars = uniqueCharacters(solutionET.getText().toString());
//        Character[] letters = new Character[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
//        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, letters);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        replaceFromSpinner.setAdapter(adapter);
//        replaceToSpinner.setAdapter(adapter);

        // assign player object that transferred from PlayerMenu
        Intent intent = getIntent();
        player = (Player) intent.getSerializableExtra("current_player");

        // ------------------------------------ Set Listener ------------------------------------ //
        titleET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isTitleUnique();
                }
            }
        });

        solutionET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // populate replaceFrom spinner using unique letters from solution
                    ArrayList<Character> uniqChars = CryptoUtils.uniqueCharacters(solutionET.getText().toString());
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(NewCryptogram.this,
                            android.R.layout.simple_spinner_item, uniqChars);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    replaceFromSpinner.setAdapter(adapter);

                    // analyze solution and set currentAttempt as solution phrase
                    char2IndexMapSolu = CryptoUtils.analyze(solutionET.getText().toString());
                    currentAttempt = solutionET.getText().toString();
                }
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
                    adapter = ArrayAdapter.createFromResource(NewCryptogram.this,
                            R.array.upperCaseLetters, android.R.layout.simple_spinner_item);
                } else {
                    adapter = ArrayAdapter.createFromResource(NewCryptogram.this,
                            R.array.lowerCaseLetters, android.R.layout.simple_spinner_item);
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                replaceToSpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        encodedPhraseTV.addTextChangedListener(new TextWatcher() {
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
                    encodedPhraseTV.requestFocus();
                    encodedPhraseTV.setError("letter cannot be replaced by its own!");
                    return;
                }

                if (encodedPhraseTV.getText().length() > 0) {
                    encodedPhraseTV.requestFocus();
                    encodedPhraseTV.setError(null);
                }
            }
        });

        replaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call encode to replace letters and display encoded phrase
                encode();
                encodedPhraseTV.setText(currentAttempt);
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
                finish();
            }
        });

    }

    private Boolean isTitleUnique() {
        CryptogramApp app = new CryptogramApp(getApplicationContext());
        app.loadCryptograms();
        Cryptogram c = CryptoUtils.getCryptogram(app, titleET.getText().toString());
        if (c!=null) {
            titleET.setError("Cryptogram " + titleET.getText().toString() + " already exists. " +
                    "Please choose another name.");
            return false;
        } else {
            return true;
        }
    }

    private void encode() {
        // check if letters are selected from both spinner [seems it will never be triggered]
        if(replaceFromSpinner.getSelectedItem()==null || replaceToSpinner.getSelectedItem()==null){
            encodedPhraseTV.requestFocus();
            encodedPhraseTV.setError("please choose a letter");
            return;
        }

        // replace, update the currentAttempt
        char replaceFromChar = replaceFromSpinner.getSelectedItem().toString().charAt(0);
        char replaceToChar = replaceToSpinner.getSelectedItem().toString().charAt(0);
        char[] solutionCharArray = currentAttempt.toCharArray();
        List<Integer> targetIdx = char2IndexMapSolu.get(replaceFromChar);
        for (int idx : targetIdx) {
            solutionCharArray[idx] = replaceToChar;
        }
        currentAttempt = new String(solutionCharArray);
    }


    private void submit() {
        // check empty input
        if(titleET.getText().toString().isEmpty()){
            titleET.requestFocus();
            titleET.setError("please input a title");
            return;
        }
        if(solutionET.getText().toString().isEmpty()){
            solutionET.requestFocus();
            solutionET.setError("please input a solution");
            return;
        }
        if(hintET.getText().toString().isEmpty()){
            hintET.requestFocus();
            hintET.setError("please input a hint");
            return;
        }
        if(encodedPhraseTV.getText().toString().isEmpty()){
            encodedPhraseTV.requestFocus();
            encodedPhraseTV.setError("please encode your solution");
            return;
        }

        // check unique title
        if (!isTitleUnique()) {
            return;
        }

        String title = titleET.getText().toString();
        String solution = solutionET.getText().toString();
        String encodedPhrase = encodedPhraseTV.getText().toString();
        String hint = hintET.getText().toString();
        String creatorName = player.getUserName();

        // check replacement
        for(int i=0; i<solution.length(); i++){
            if(Character.isLetter(solution.charAt(i))){
                if(solution.charAt(i)==encodedPhrase.charAt(i)){
                    encodedPhraseTV.requestFocus();
                    encodedPhraseTV.setError("Not all unique letters have been replaced!");
                    return;
                }
            }
        }

        // check if letter is replaced by unique letter
        char2IndexMapEnco = CryptoUtils.analyze(encodedPhrase);
        for (int i=0; i<encodedPhrase.length(); i++) {
            List<Integer> idxList = char2IndexMapEnco.get(encodedPhrase.charAt(i));
            Character pre = solution.charAt(idxList.get(0));
            for (Integer idx :idxList) {
                if (solution.charAt(idx) != pre) {
                    encodedPhraseTV.requestFocus();
                    encodedPhraseTV.setError("different letters cannot be replaced to a same letter");
                    return;
                }
            }
        }

        // save cryptogram to DB
        Cryptogram cryptogram = new Cryptogram(title, solution, encodedPhrase, hint, creatorName);
        CryptogramApp app = new CryptogramApp(getApplicationContext());
        app.addCryptogram(cryptogram);

        // update player object and player data in DB
        player.updatePoints(5);
        app.updatePlayer(player);

        // message
        Toast toast = Toast.makeText(NewCryptogram.this, "Cryptogram " + title + " is created!", Toast.LENGTH_SHORT);
        toast.show();

        Intent i = new Intent(NewCryptogram.this, PlayerMenu.class);
        i.putExtra("current_player", player);
        startActivity(i);
    }
}