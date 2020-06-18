package com.example.android.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
TextView scoreboardA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreboardA = (TextView) findViewById(R.id.text);
    }


    int scoreA = 0;
    int scoreB = 0;

    public void point3A(View view) {
        scoreA = scoreA + 3;
        displayA(scoreA);
    }
    public void point3B(View view) {
        scoreB = scoreB + 3;
        displayB(scoreB);
    }


    public void point2A(View view) {
        scoreA=scoreA+2;
        displayA(scoreA);
    }

    public void point2B(View view){
        scoreB = scoreB + 2;
        displayB(scoreB);
    }


    public void Free(View view){
        if(view.getId()==R.id.ft1){
            scoreB = scoreB + 5;
            displayB(scoreB);
        }
        else {
            scoreA = scoreA + 5;
            displayA(scoreA);
        }
    }

   public void displayA(int scoreA){

       scoreboardA.setText(scoreA+"");
    }

    public void displayB(int scoreB){
        TextView scoreboardB = (TextView) findViewById(R.id.text1);
        scoreboardB.setText(scoreB+"");
    }
    public void reset(View view){
        scoreA=0;scoreB=0;
        displayB(scoreB);
        displayA(scoreA);
    }
}

