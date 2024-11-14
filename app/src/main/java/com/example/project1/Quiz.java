package com.example.project1;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Quiz extends AppCompatActivity {

    private TextView scoreText, textUnsigned, textSigned, textHex, bitsView, question;
    private EditText signedint, unsignedint, hex;
    private Button checkAnswer;
    public final static String MESSAGE =
            "MainActivity";                 //to use in Intent
    public final static String QA = "QuizActivity"; //to use in Log.i
    public int correct_guesses;
    public int overall_guesses;
    public int questionSelected;
    static final int Umax8 = 255;
    static final int Umax10 = 1023;
    static final int Umax12 = 4095;
    static final int Tmax8 = 127;
    static final int Tmax10 = 511;
    static final int Tmax12 = 2047;
    static final int Tmin8 = -128;
    static final int Tmin10 = -512;
    static final int Tmin12 = -4096;
    public int bits; //0, 1, or 2
    public int qType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //  intent = getIntent();

        question = findViewById(R.id.random);
        question.setPaintFlags(question.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //   question.setEnabled(false);
        scoreText = findViewById(R.id.scoreTxt);
        scoreText.setText("0/0");
        scoreText.setPaintFlags(scoreText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //   scoreText.setEnabled(false);
        checkAnswer = findViewById(R.id.checkAnswer);
        checkAnswer.setVisibility(View.VISIBLE);
        textUnsigned = findViewById(R.id.textUnsigned);
        textUnsigned.setVisibility(View.VISIBLE);
        textSigned = findViewById(R.id.textSigned);
        textSigned.setVisibility(View.INVISIBLE);
        unsignedint = findViewById(R.id.unsignedint);
        unsignedint.setVisibility(View.INVISIBLE);
        signedint = findViewById(R.id.signedint);
        signedint.setVisibility(View.INVISIBLE);
        textHex = findViewById(R.id.textHex);
        textHex.setVisibility(View.INVISIBLE);
        hex = findViewById(R.id.hex);
        hex.setVisibility(View.INVISIBLE);
        correct_guesses = 0;
        overall_guesses = 0;


        questionSelected = 0;
        bitsView = findViewById(R.id.bits);
        bitsView.setPaintFlags(bitsView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //   bitsView.setEnabled(false);
        //   bitsView.setText("(8bit)");
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity.quiz_score = scoreText.getText().toString();
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void onStart(){
        super.onStart();
        //generate first question
        Random rand = new Random();
        bits = rand.nextInt(3); //randomly selects index of bits ArrayList
        qType = rand.nextInt(3); //randomly selected index of type ArrayList
        View view = this.getCurrentFocus();
        randomNum(bits, qType, view);
    }

    public void randomNum(int bits, int type, View view){
        Random rand = new Random();
        int UMAX = 0;
        int TMAX = 0;
        switch(bits) {
            case 0:
                UMAX = Umax8 +1;
                TMAX = Tmax8;
                bitsView.setText("8bit");
                break;
            case 1:
                UMAX = Umax10 + 1;
                TMAX = Tmax10;
                bitsView.setText("10bit");
                break;
            case 2 :
                UMAX = Umax12 +1;
                TMAX = Tmax12;
                bitsView.setText("12bit");
                break;
        }

        int randomNum;
        String output = "";
        switch(type){
            case 0:
                randomNum = rand.nextInt(UMAX);
                output = "0x" + Integer.toHexString(randomNum);
                hex2intSetup(view);
                break;
            case 1:
                randomNum = rand.nextInt(UMAX);
                output = ""+ rand.nextInt(UMAX);
                unsigned2hexSetup(view);
                break;
            case 2:
                randomNum = rand.nextInt(UMAX);
                output = "" + (rand.nextInt(UMAX) - TMAX); //-Tmax to Tmax
                signed2hexSetup(view);
                break;
        }
        question.setText(output);
    }

    public void hex2intSetup(View v) {
        textUnsigned.setVisibility(View.VISIBLE);
        textSigned.setVisibility(View.VISIBLE);
        unsignedint.setVisibility(View.VISIBLE);
        signedint.setVisibility(View.VISIBLE);
        unsignedint.setText("");
        signedint.setText("");
        textHex.setVisibility(View.INVISIBLE);
        hex.setVisibility(View.INVISIBLE);
        questionSelected = 0;
    }

    public void unsigned2hexSetup(View view){
        textHex.setVisibility(View.VISIBLE);
        hex.setVisibility(View.VISIBLE);
        hex.setText("");
        textSigned.setVisibility(View.INVISIBLE);
        signedint.setVisibility(View.INVISIBLE);
        textUnsigned.setVisibility(View.INVISIBLE);
        unsignedint.setVisibility(View.INVISIBLE);
        questionSelected = 1;
    }

    public void signed2hexSetup(View view){
        textHex.setVisibility(View.VISIBLE);
        hex.setVisibility(View.VISIBLE);
        hex.setText("");
        textSigned.setVisibility(View.INVISIBLE);
        signedint.setVisibility(View.INVISIBLE);
        textUnsigned.setVisibility(View.INVISIBLE);
        unsignedint.setVisibility(View.INVISIBLE);
        questionSelected = 2;
    }

    //calls appropriate method based on question type (hex, unsigned int, signed int)
    public void checkAnswer(View view){
        switch(questionSelected){
            case 0:
                hex2int(view);
                break;
            case 1:
                unsigned2hex(view);
                break;
            case 2:
                signed2hex(view);
                break;
        }

        if(overall_guesses<10) {
            Random rand = new Random();
            int bits = rand.nextInt(3); //randomly selects index of bits ArrayList
            int type = rand.nextInt(3); //randomly selected index of type ArrayList
            randomNum(bits, type, view);
        }
        else{
            Toast.makeText(this, "Quiz score: " +
                    correct_guesses + "/" + overall_guesses +
                    ". Returning to MainActivity", Toast.LENGTH_SHORT).show();
            MainActivity.quiz_score = scoreText.getText().toString();
            this.finish();
        }
    }

    public void hex2int(View v) {
        String rNum = question.getText().toString(); //gets String representation of question
        String rNumInt = rNum.substring(2); //removes '0x'
        Log.i(QA, "number: " + rNumInt);
        int myRandomNum = Integer.parseInt(rNumInt, 16);
        int unsignedExpected = myRandomNum;
        Log.i(QA, "number2: " + unsignedExpected);
        int signedExpected;

        int TMAX, UMAX;
        if (bitsView.getText().toString().equals("8bit")) {
            TMAX = Tmax8;
            UMAX = Umax8;
        } else if (bitsView.getText().toString().equals("10bit")) {
            TMAX = Tmax10;
            UMAX = Umax10;
        } else {
            TMAX = Tmax12;
            UMAX = Umax12;
        }
        Log.i(QA, "bitsView: " + bitsView.getText().toString());
        if (unsignedExpected > TMAX)
            signedExpected = unsignedExpected - (UMAX + 1);
        else
            signedExpected = unsignedExpected;
        Log.i(QA, "signedExpected: " + signedExpected);
        Log.i(QA, "unsignedExpected: " + unsignedExpected);

        try{
            int signedActual = Integer.parseInt(signedint.getText().toString());
            int unsignedActual = Integer.parseInt(unsignedint.getText().toString());
            Log.i(QA, "signedActual: " + signedExpected);
            Log.i(QA, "unsignedActual: " + unsignedExpected);
            if (signedExpected == signedActual && unsignedExpected == unsignedActual) {
                //output answer correct
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                correct_guesses++;
                overall_guesses++;
                scoreText.setText("" + correct_guesses + "/" + overall_guesses);
            }
            else {
                Toast.makeText(this, "Incorrect. Correct answer unsigned:" + unsignedExpected
                        + ". Correct answer signed: " + signedExpected, Toast.LENGTH_SHORT).show();
                overall_guesses++;
                scoreText.setText("" + correct_guesses + "/" + overall_guesses);
            }
        }catch(NumberFormatException e){
            Toast.makeText(this, "Incorrect. Correct answer unsigned: " + unsignedExpected
                    + ". Correct answer signed: " + signedExpected, Toast.LENGTH_SHORT).show();
            overall_guesses++;
            scoreText.setText("" + correct_guesses + "/" + overall_guesses);
        }
    }

    public void unsigned2hex(View view){
        //   try {
        String rNum = question.getText().toString(); //gets String representation of question
        int value = Integer.parseInt(rNum);
        String hexExpected = "";
        if(bitsView.getText().toString().equals("8bit")||
                bitsView.getText().toString().equals("12bit")) //for 8bits and 12bits
            hexExpected = "0x" + Integer.toHexString(value);
        else {
            if(value<Umax10) //if value < 1023
                hexExpected = "0x" + Integer.toHexString(value);
            else {
                value = value - (Umax10 + 1);
                hexExpected = "0x" + Integer.toHexString(value);
            }
        }
        Log.i(QA, "bitsView: " + bitsView);
        Log.i(QA, "hexExpected: " + hexExpected);
        String hexActual = hex.getText().toString();
        if (hexActual.equals(hexExpected)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            correct_guesses++;
            overall_guesses++;
            scoreText.setText("" + correct_guesses + "/" + overall_guesses);
        }
        else {
            //empty String will also count as incorrect
            Toast.makeText(this, "Incorrect. Correct answer: " +
                    hexExpected, Toast.LENGTH_SHORT).show();
            overall_guesses++;
            scoreText.setText("" + correct_guesses + "/" + overall_guesses);
        }
     /*   }catch(NumberFormatException e){
            Toast.makeText(this, "Please enter a valid input.", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void signed2hex(View view){
        //  try {
        String rNum = question.getText().toString(); //gets String representation of question;
        Log.i(QA, "hexExpected: " + rNum);
        int num = Integer.parseInt(rNum); //gets numeric version
        Log.i(QA, "hexExpected int form: " + num);
        String hexExpected;

        int UMAX;
        if (bitsView.getText().toString().equals("8bit")) {
            UMAX = Umax8;
        } else if (bitsView.getText().toString().equals("10bit")) {
            UMAX = Umax10;
        } else {
            UMAX = Umax12;
        }
        if (num < 0) {
            int unsigned = num + UMAX + 1;
            hexExpected = "0x" + Integer.toHexString(unsigned);
        } else
            hexExpected = "0x" + Integer.toHexString(num);
        Log.i(QA, "hexExpected: " + hexExpected);
        String hexActual = hex.getText().toString();
        if (hexActual.equals(hexExpected)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            correct_guesses++;
            overall_guesses++;
            scoreText.setText("" + correct_guesses + "/" + overall_guesses);
        }
             /*   else if(hexActual.equals("")){
                Toast.makeText(this, "Please enter a valid input.", Toast.LENGTH_SHORT).show();
            }*/
        else {
            //empty String will also count as incorrect
            Toast.makeText(this, "Incorrect. Correct answer: " +
                    hexExpected, Toast.LENGTH_SHORT).show();
            overall_guesses++;
            scoreText.setText("" + correct_guesses + "/" + overall_guesses);
        }
        //  }catch(NumberFormatException e){
        //     Toast.makeText(this, "Please enter a valid input.", Toast.LENGTH_SHORT).show();
        //  }
    }

    //send score back to MainActivity
    public void quit(View view){
        //pass values back to MainActivity
        //    Intent intent = new Intent();
        MainActivity.quiz_score = scoreText.getText().toString();
        //    intent.putExtra(MESSAGE, "pillow");
        this.finish();
    }
}
