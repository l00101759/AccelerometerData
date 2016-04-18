package com.example.fraser.accelerometerdata;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import android.speech.tts.TextToSpeech;
import android.util.Log;

public class SpeechTester extends AppCompatActivity {

    protected static final int REQUEST_OK = 1;
    private Button speak;
    private TextView word;
    private TextToSpeech t1;

    ListView listView;
    ArrayAdapter adapter;

    String wordSpoken = "";
    //array of possible words that the application will recongise
    String [] menuOptions = {"1. Balance", "2. Transactions" , "3. Customer support", "4. Exit"};
    String [] possibleWords = {"balance", "transactions" , "customer support", "exit"};

    //for sending/receiving message to activity
    //public final static String EXTRA_MESSAGE = "com.example.fraser.messageToActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_tester);
        word = (TextView)findViewById(R.id.txtWord);
        //speak=(Button)findViewById(R.id.button);

        //create the list view for the menu
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, menuOptions);
        listView = (ListView) findViewById(R.id.listViewMenu);
        listView.setAdapter(adapter);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        /*speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = possibleWords[0] +"... "+possibleWords[1] +"... "+possibleWords[2] +"... "+possibleWords[3];
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "");
                //t1.playSilentUtterance(1000, TextToSpeech.QUEUE_FLUSH, null);
            }
        });*/


    }

    public void listen(View view)
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-UK");
        try {
            startActivityForResult(i, REQUEST_OK);
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Boolean flag = false;
        int count = 0;

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_OK  && resultCode==RESULT_OK) {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ((TextView)findViewById(R.id.txtWord)).setText("You said '" +thingsYouSaid.get(0)+"'");
            wordSpoken = thingsYouSaid.get(0);

            wordSpoken = wordSpoken.toLowerCase();
            int index = -1;

            //!!!!!!if the word is not correct then allow user to input motion!!!
            for(int i = 0; i<possibleWords.length; i++)
            {
                if(wordSpoken.equals(possibleWords[i]))
                {
                    flag = true;
                    //get index
                   // index = i;
                    goNext(i);
                }
                if(wordSpoken.equals("exit"))
                {
                    flag = true;
                    goToLogin();
                }
            }
            if(!flag)
            {
                goGestureRecog();
            }
        }
        //so if the word they said isnt reconginsed
        //take them to the gesture recognition activity
    }

    public void goToLogin() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
    public void goGestureRecog() {
        Intent intent = new Intent(this, GestureRecognition.class);
        startActivity(intent);
    }
    public void goNext(int index) {
       // Intent intent = new Intent(this, SpeechRecognised.class);

        //TextView word = (TextView)findViewById(R.id.txtWord);
        //String wordToSend = word.getText().toString();
        //System.out.println("*****Word to send = "  +wordSpoken);

        //intent.putExtra(EXTRA_MESSAGE, index);
        //startActivity(intent);
        Intent intent = new Intent(this, SpeechRecognised.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }
}
