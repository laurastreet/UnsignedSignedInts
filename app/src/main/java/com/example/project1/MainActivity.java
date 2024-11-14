package com.example.project1;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText practiceScore, quizScore;
    private static String MA = "MainActivity";
    public static final int ACTIVITY_RESULT = 1;
    public static int buttonSelected; //indicates which RadioButton is selected
    public static int correct_guesses;
    public static int overall_guesses;
    public static String quiz_score;
    public static String practice_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSelected = 0;
        practiceScore = findViewById(R.id.scoreTxt);
        practiceScore.setText("0/0");
        quizScore = findViewById(R.id.quizTxt);
        quizScore.setText("N/A");
        correct_guesses = 0;
        overall_guesses = 0;

    }

    public void practice(View view){
        //   subgroup.setVisibility(View.VISIBLE);
        Intent myIntent = new Intent(this, Practice.class);
        this.startActivityForResult(myIntent, ACTIVITY_RESULT);
    }

    public void quiz(View view){
        //   subgroup.setVisibility(View.INVISIBLE);
        Intent myIntent = new Intent(this, Quiz.class);
        this.startActivityForResult(myIntent, ACTIVITY_RESULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent myIntent){
        super.onActivityResult(requestCode, resultCode, myIntent);
        //  String result = myIntent.getExtras().getString(Quiz.MESSAGE);
        Log.i(MA, "quiz_score: " + MainActivity.quiz_score);
        Log.i(MA, "in onActivityResult");
        Log.i(MA, "practice_score: " + MainActivity.practice_score);
        quizScore.setText(MainActivity.quiz_score);
        practiceScore.setText(MainActivity.practice_score);
    }

}