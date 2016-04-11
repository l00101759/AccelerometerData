package com.example.fraser.accelerometerdata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SpeechRecognised extends AppCompatActivity {

    private TextView title, answer;
    ListView listView;
    ArrayAdapter adapter;

    // Array of strings...
    String[] transactions = {"PayPal - 04/02 \t\t\t\t\t\t 150.00","Centra - 05/02 \t\t\t\t\t\t 15.55","Aldi - 10/02 \t\t\t\t\t\t 26.95","Three - 15/02 \t\t\t 20.00"};
    String[] balance = {"01/04/2016 \t\t\t\t\t\t 150.00","10/04/2016 \t\t\t\t\t\t 15.55","12/04/2016 \t\t\t\t\t\t 26.95","24/04/2016 \t\t\t\t\t\t -20.00"};
    String[] support = {"Customer Support", "Phone \t\t\t 1800 123 123" , "Email \t\t\t support@help.ie", "Opening Hours : 8am to 5pm"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognised);

        //Intent intent = getIntent();
        //int message = intent.getIntExtra(SpeechTester.EXTRA_MESSAGE, 0);

        Intent mIntent = getIntent();
        int message = mIntent.getIntExtra("index", 0);


        title = (TextView)findViewById(R.id.txtTitle);
        title.setText("");
        System.out.println("*********************Message received = " + message);

        displayAnswer(message + 1);//adds one for index purposes

        //adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, transactions);
        //listView = (ListView) findViewById(R.id.listView);
        //listView.setAdapter(adapter);

    }

    //will display the corresponding list view
    private void displayAnswer(int ans)
    {
        switch(ans){
            case 1 :    title.setText(ans +". Your Balance ");
                        //answer.setText("€100.0");
                        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, balance);
                        listView = (ListView) findViewById(R.id.listView);
                        listView.setAdapter(adapter);
                        break;
            case 2 :
                        title.setText(ans + ". Recent Transactions ");
                        //answer.setText("Paypal = 150.0 \n Centra 20.50");
                        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, transactions);
                        listView = (ListView) findViewById(R.id.listView);
                        listView.setAdapter(adapter);
                        break;
            case 3 :
                        title.setText(ans +". Customer Support ");
                        //answer.setText("Opening hours = 8am - 5pm \n Call: 1800 123 123");
                        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, support);
                        listView = (ListView) findViewById(R.id.listView);
                        listView.setAdapter(adapter);
                        break;
            default:
                        title.setText("No Records..");
                        answer.setText("...");
                        break;
        }

    }

    private void displayAnswer1(String ans)
    {
        if(ans.equalsIgnoreCase("balance"))
        {
            answer.setText("Balance = 100.0");
        }
        if(ans.equalsIgnoreCase("new balance"))
        {
            answer.setText("Balance = 1500.0");
        }
        if(ans.equalsIgnoreCase("transactions"))
        {
            answer.setText("Paypal = 150.0 \n Centra 20.50");
        }
        if(ans.equalsIgnoreCase("customer support"))
        {
            answer.setText("Opening hours = 8am - 5pm \n Call: 1800 123 123");
        }
        else
        {
            answer.setText("EMPTY");
        }
    }

}
