package com.example.fraser.accelerometerdata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LogIn extends AppCompatActivity  {

    private EditText username ,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=(EditText)findViewById(R.id.editText2);
        password=(EditText)findViewById(R.id.editText);
        username.setText("");
        password.setText("");
    }

    public void logIn(View view)
    {
        if(username.getText().toString().equals("1234") &&password.getText().toString().equals("1234"))
        {
            //authorize login for admin
            username.setText("");
            password.setText("");
            openRecord();
        }
        if(username.getText().toString().equals("101") &&password.getText().toString().equals("101"))
        {
            //just random user
            username.setText("");
            password.setText("");
            openMain();
        }
        else
        {
            username.setText("");
            password.setText("");
        }
    }

    public void openRecord() {
        Intent intent = new Intent(this, RecordGesture.class);
        startActivity(intent);
    }
    public void openMain() {
        Intent intent = new Intent(this, MainActivity1.class);
        startActivity(intent);
    }


}
