package com.example.project1;

import android.app.Activity;
import android.graphics.Paint;
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

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Practice extends AppCompatActivity {
    EditText unsignedint, signedint, hex;
    TextView textUnsigned, textSigned, scoreTxt, random;
    //  TextView textHex;
    RadioGroup subgroup;
    Button checkAnswer;
    Spinner ones, tens, hundreds;
    private static String MA = "MainActivity";
    public static final int ACTIVITY_RESULT = 1;
    public int buttonSelected; //indicates which RadioButton is selected
    public static int correct_guesses;
    public static int overall_guesses;
    public static String practice_score;
    public int myRandomNum; //random hex number to show to user
    private String myRandomNumStr;
    private String savedValue;  //restores value in case of screen rotation
    private String savedBits; //restores bits in case of screen rotation
    public final static String PA = "PracticeActivity"; //to use in Log.i

    static final int Umax8 = 255;
    static final int Umax10 = 1023;
    static final int Umax12 = 4095;
    static final int Tmax8 = 127;
    static final int Tmax10 = 511;
    static final int Tmax12 = 2047;
    static final int Tmin8 = -128;
    static final int Tmin10 = -512;
    static final int Tmin12 = -4096;
    private String bits; //indicates whether 8bits, 10bits or 12bits is chosen
    private String onesStr; //stores user-selected Spinner character in unsigned/signed int to hex conversion
    private String tensStr;
    private String hundredsStr;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        subgroup = (RadioGroup)findViewById(R.id.practiceChoice);
        scoreTxt = findViewById(R.id.scoreTxt);
        scoreTxt.setText("0/0");
        scoreTxt.setPaintFlags(scoreTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        random = findViewById(R.id.random);
        random.setVisibility(View.VISIBLE);
        random.setPaintFlags(random.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textUnsigned = (TextView)findViewById(R.id.textUnsigned);
        textUnsigned.setVisibility(View.VISIBLE);
        textSigned = (TextView)findViewById(R.id.textSigned);
        textSigned.setVisibility(View.VISIBLE);
        unsignedint = findViewById(R.id.unsignedint);
        unsignedint.setVisibility(View.VISIBLE);
        signedint = findViewById(R.id.signedint);
        signedint.setVisibility(View.VISIBLE);
        //    textHex = (TextView)findViewById(R.id.textHex);
        //    textHex.setVisibility(View.INVISIBLE);
        //  hex = (EditText)findViewById(R.id.hex);
        //  hex.setVisibility(View.INVISIBLE);
        checkAnswer = (Button)findViewById(R.id.checkAnswer);
        correct_guesses = 0;
        overall_guesses = 0;

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity.practice_score = scoreTxt.getText().toString();
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
        /*    if(savedInstanceState != null){
            savedInstanceState.get(savedValue);
            random.setText(savedValue);
        //    bits;
        }
        else {*/
        //initial question will always be hex to decimal, 8 bit
        Random rand = new Random();
        myRandomNum = rand.nextInt(Umax8 + 1); //creates random integer that fits within 8bits
        myRandomNumStr = "0x" + Integer.toHexString(myRandomNum); //converts to base 16
        random.setText(myRandomNumStr); //displays random hex number in app in base 16
        bits = "8bit";
        buttonSelected = 0;

        //spinner code
        Spinner spinner0 = (Spinner) findViewById(R.id.bits);
        ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(this,
                R.array.bits_array, android.R.layout.simple_spinner_item);
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner0.setAdapter(adapter0);
        spinner0.setOnItemSelectedListener(new BitSpinner());

        //spinner code
        ones = (Spinner) findViewById(R.id.ones);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.hex_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ones.setAdapter(adapter1);
        ones.setOnItemSelectedListener(new SpinnerActivity0());

        //spinner code
        tens = (Spinner) findViewById(R.id.tens);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.hex_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tens.setAdapter(adapter2);
        tens.setOnItemSelectedListener(new SpinnerActivity1());

        //spinner code
        hundreds = (Spinner) findViewById(R.id.hundreds);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.hex_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hundreds.setAdapter(adapter3);
        hundreds.setOnItemSelectedListener(new SpinnerActivity2());

        ones.setVisibility(View.INVISIBLE);
        tens.setVisibility(View.INVISIBLE);
        hundreds.setVisibility(View.INVISIBLE);
        //   }
    }

    public class BitSpinner extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            bits = parent.getItemAtPosition(pos).toString();
            Log.i(PA, "bits: " + bits);
            switch(buttonSelected) {
                case 0:
                    hex2intSetup(view);
                    break;
                case 1:
                    unsigned2hexSetup(view);
                    break;
                case 2:
                    signed2hexSetup(view);
                    break;
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    public class SpinnerActivity0 extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            onesStr = parent.getItemAtPosition(pos).toString();
            Log.i(PA, "ones: " + onesStr);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    public class SpinnerActivity1 extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            tensStr = parent.getItemAtPosition(pos).toString();
            Log.i(PA, "tens: " + tensStr);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    public class SpinnerActivity2 extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            hundredsStr = parent.getItemAtPosition(pos).toString();
            Log.i(PA, "hundreds: " + hundredsStr);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
    //called when user selects 'Hex to Decimal' option in Practice
    //checks to see whether hex input is valid and checks to see if signed and unsigned int answers
    //NOTE - hex input should be valid bc it is generated by the program, not the user (in onCreate())
    //given for hex are correct
    public void hex2intSetup(View v) {
        ones.setVisibility(View.INVISIBLE);
        tens.setVisibility(View.INVISIBLE);
        hundreds.setVisibility(View.INVISIBLE);
        buttonSelected = 0;
        Random rand = new Random();
        int BOUND, OUT_OF_RANGE_BOUND;
        if(bits.equals("8bit")){
            BOUND = Umax8 +1;
            OUT_OF_RANGE_BOUND = (int)(0.10*BOUND + BOUND); //add 10% to generate some illegal values
        }
        else if(bits.equals("10bit")) {
            BOUND = Umax10 +1;
            OUT_OF_RANGE_BOUND = (int)(0.10*BOUND + BOUND); //add 10% to generate some illegal values
        }
        else {

            BOUND = Umax12 +1;
            OUT_OF_RANGE_BOUND = (int)(0.10*BOUND + BOUND); //add 10% to generate some illegal values
        }
        myRandomNum = rand.nextInt(OUT_OF_RANGE_BOUND); //creates random integer that fits within 8bits
        //+10%
        Log.i(MA, "myRandomNum: " + myRandomNum); //shows this value in base 10
        random = findViewById(R.id.random);
        random.setVisibility(View.VISIBLE);
        String str = "0x" + Integer.toHexString(myRandomNum); //converts to base 16
        random.setText(str); //displays random hex number in app in base 16
        textUnsigned.setVisibility(View.VISIBLE);
        textSigned.setVisibility(View.VISIBLE);
        unsignedint.setText("");
        unsignedint.setVisibility(View.VISIBLE);
        signedint.setText("");
        signedint.setVisibility(View.VISIBLE);
        //   textHex.setVisibility(View.INVISIBLE);
        //   hex.setVisibility(View.INVISIBLE);
    }

    public void unsigned2hexSetup(View view){
        buttonSelected = 1;
        Random rand = new Random();
        int BOUND, OUT_OF_RANGE_BOUND;
        ones.setVisibility(View.VISIBLE);
        tens.setVisibility(View.VISIBLE);
        if(bits.equals("8bit")){
            BOUND = Umax8 +1;
            OUT_OF_RANGE_BOUND = (int)(0.10*BOUND + BOUND); //add 10% to generate some illegal values
            hundreds.setVisibility(View.INVISIBLE);
        }
        else if(bits.equals("10bit")) {
            BOUND = Umax10 +1;
            OUT_OF_RANGE_BOUND = (int)(0.10*BOUND + BOUND); //add 10% to generate some illegal values
            hundreds.setVisibility(View.VISIBLE);
        }
        else {
            BOUND = Umax12 +1;
            OUT_OF_RANGE_BOUND = (int)(0.10*BOUND + BOUND); //add 10% to generate some illegal values
            hundreds.setVisibility(View.VISIBLE);
        }
        myRandomNum = rand.nextInt(OUT_OF_RANGE_BOUND); //0-256 +10% exclusive
        Log.i(MA, "myRandomNum: " + myRandomNum); //shows this value in base 10
        random = findViewById(R.id.random);
        String str = Integer.toString(myRandomNum); //displays in base 10
        random.setText(str); //displays random hex number in app in base 10
        random.setVisibility(View.VISIBLE);
        //   textHex.setText("");
        //   textHex.setVisibility(View.VISIBLE);
        //   hex.setVisibility(View.VISIBLE);
        textSigned.setVisibility(View.INVISIBLE);
        signedint.setVisibility(View.INVISIBLE);
        textUnsigned.setVisibility(View.INVISIBLE);
        unsignedint.setVisibility(View.INVISIBLE);
    }

    public void signed2hexSetup(View view){
        ones.setVisibility(View.VISIBLE);
        tens.setVisibility(View.VISIBLE);
        hundreds.setVisibility(View.INVISIBLE);
        buttonSelected = 2;
        Random rand = new Random();
        int BOUND, OUT_OF_RANGE_BOUND, TMAX;
        if(bits.equals("8bit")){
            BOUND = Umax8 +1;
            OUT_OF_RANGE_BOUND = (int)(0.10*BOUND + BOUND); //add 10% to generate some illegal values
            TMAX = Tmax8;
            hundreds.setVisibility(View.INVISIBLE);
        }
        else if(bits.equals("10bit")) {
            BOUND = Umax10 +1;
            OUT_OF_RANGE_BOUND = (int)(0.10*BOUND + BOUND); //add 10% to generate some illegal values
            TMAX = Tmax10;
            hundreds.setVisibility(View.VISIBLE);
        }
        else {
            BOUND = Umax12 +1;
            OUT_OF_RANGE_BOUND = (int)(0.10*BOUND + BOUND); //add 10% to generate some illegal values
            TMAX = Tmax12;
            hundreds.setVisibility(View.VISIBLE);
        }
        myRandomNum = rand.nextInt(OUT_OF_RANGE_BOUND) -TMAX; //-127 to 154 for 8 bit
        Log.i(MA, "myRandomNum: " + myRandomNum); //shows this value in base 10
        random = findViewById(R.id.random);
        String str = Integer.toString(myRandomNum); //displays in base 10
        random.setText(str); //displays random hex number in app in base 10
        random.setVisibility(View.VISIBLE);
        //   textHex.setText("");
        //   textHex.setVisibility(View.VISIBLE);
        //  hex.setVisibility(View.VISIBLE);
        textSigned.setVisibility(View.INVISIBLE);
        signedint.setVisibility(View.INVISIBLE);
        textUnsigned.setVisibility(View.INVISIBLE);
        unsignedint.setVisibility(View.INVISIBLE);
    }

    public void checkAnswer(View view){
        switch (buttonSelected) {
            case 0: //R.id.hex2int
                hex2int(view);
                break;
            case 1:   //R.id.unsigned2hex:
                unsigned2hex(view);
                break;
            case 2:     //R.id.signed2hex:
                signed2hex(view);
                break;
        }
    }

    public void hex2int(View v) {
        try {
            String root = random.getText().toString(); //get value from UI
            String randomNum = root.substring(2);  //get rid of '0x' prefix
            Log.i(PA, "hexString: " + randomNum);
            int unsignedExpected = Integer.parseInt(randomNum, 16);
            int signedExpected;
            int TMAX, UMAX;
            if (bits.equals("8bit")) {
                TMAX = Tmax8;
                UMAX = Umax8;
            } else if (bits.equals("10bit")) {
                TMAX = Tmax10;
                UMAX = Umax10;
            } else {
                TMAX = Tmax12;
                UMAX = Umax12;
            }
            if (unsignedExpected > TMAX)
                signedExpected = unsignedExpected - (UMAX + 1);
            else
                signedExpected = unsignedExpected;
            Log.i(PA, "signedExpected: " + signedExpected);
            Log.i(PA, "unsignedExpected: " + unsignedExpected);
            int signedActual = Integer.parseInt(signedint.getText().toString());
            int unsignedActual = Integer.parseInt(unsignedint.getText().toString());
            Log.i(PA, "signedActual: " + signedExpected);
            Log.i(PA, "unsignedActual: " + unsignedExpected);
            if (signedExpected == signedActual && unsignedExpected == unsignedActual) {
                //output answer correct
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                correct_guesses++;
                overall_guesses++;
                scoreTxt.setText("" + correct_guesses + "/" + overall_guesses);
            } else {
                Toast.makeText(this, "Incorrect. Correct answer unsigned: " + unsignedExpected +
                        ". Correct answer signed: " + signedExpected, Toast.LENGTH_SHORT).show();
                overall_guesses++;
                scoreTxt.setText("" + correct_guesses + "/" + overall_guesses);
            }
        }catch(NumberFormatException e){
            Toast.makeText(this, "Please enter a valid input.", Toast.LENGTH_SHORT).show();
        }
    }

    public void unsigned2hex(View view){
        try {
            String randomNum = random.getText().toString(); //capture value from UI
            int num = Integer.parseInt(randomNum); //convert to int
            String hexExpected = Integer.toHexString(num); //0x prefix not included
            Log.i(PA, "hexExpected: " + hexExpected);
            String hexActual;
            if(bits.equals("8bit")) {
                if (!onesStr.equals("0"))
                    hexActual = tensStr.concat(onesStr);
                else
                    hexActual = onesStr; //leave off leading zero
            }
            else {
                if(!hundredsStr.equals("0"))
                    hexActual = hundredsStr.concat(tensStr+onesStr);
                else{                       //leave off leading zero
                    if(!tensStr.equals("0"))
                        hexActual = tensStr.concat(onesStr);
                    else
                        hexActual = onesStr; //leave off leading zero
                }
            }
            if (hexActual.equals(hexExpected)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                correct_guesses++;
                overall_guesses++;
                scoreTxt.setText("" + correct_guesses + "/" + overall_guesses);
            } else {
                Toast.makeText(this, "Incorrect. Correct answer: "
                        + hexExpected, Toast.LENGTH_SHORT).show();
                overall_guesses++;
                scoreTxt.setText("" + correct_guesses + "/" + overall_guesses);
            }
        }catch(NumberFormatException e){
            Toast.makeText(this, "Please enter a valid input.", Toast.LENGTH_SHORT).show();
        }
    }

    public void signed2hex(View view){
        try {
            String randomNum = random.getText().toString(); //capture value from UI
            int num = Integer.parseInt(randomNum); //convert to int
            String hexExpected;
            String hexActual;
            int UMAX;
            if(bits.equals("8bit")){
                UMAX = Umax8;
                if(!onesStr.equals("0"))
                    hexActual = tensStr.concat(onesStr);
                else
                    hexActual = onesStr; //leave off leading zero
            }
            else if(bits.equals("10bit")) {
                UMAX = Umax10;
                if(!hundredsStr.equals("0"))
                    hexActual = hundredsStr.concat(tensStr+onesStr);
                else{                       //leave off leading zero
                    if(!tensStr.equals("0"))
                        hexActual = tensStr.concat(onesStr);
                    else
                        hexActual = onesStr; //leave off leading zero
                }
            }
            else {
                UMAX = Umax12;
                if(!hundredsStr.equals("0"))
                    hexActual = hundredsStr.concat(tensStr+onesStr);
                else{                       //leave off leading zero
                    if(!tensStr.equals("0"))
                        hexActual = tensStr.concat(onesStr);
                    else
                        hexActual = onesStr; //leave off leading zero
                }
            }
            if(num < 0){
                int unsigned = num + UMAX + 1;
                hexExpected = Integer.toHexString(unsigned); //doesn't include '0x' prefix
            }
            else
                hexExpected = Integer.toHexString(num); //doesn't include '0x' prefix
            Log.i(PA, "hexExpected: " + hexExpected);
            if(hexActual.equals(hexExpected)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                correct_guesses++;
                overall_guesses++;
                scoreTxt.setText("" + correct_guesses + "/" + overall_guesses);
            }
            else {
                Toast.makeText(this, "Incorrect. Correct answer: "
                        + hexExpected, Toast.LENGTH_SHORT).show();
                overall_guesses++;
                scoreTxt.setText("" + correct_guesses + "/" + overall_guesses);
            }
        }catch(NumberFormatException e){
            Toast.makeText(this, "Please enter a valid input.", Toast.LENGTH_SHORT).show();
        }
    }

  /*  public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(savedValue, myRandomNumStr);
        outState.putString(savedBits, bits);
    }*/

    //if user wants another question, reset visibilities to INVISIBLE
    public void illegalInput(View view){
      /*  textUnsigned.setVisibility(View.INVISIBLE);
        textSigned.setVisibility(View.INVISIBLE);
        unsignedint.setVisibility(View.INVISIBLE);
        signedint.setVisibility(View.INVISIBLE);
        //textHex.setVisibility(View.INVISIBLE);
       // hex.setVisibility(View.INVISIBLE);
        randomHex.setVisibility(View.INVISIBLE);*/
        String input = random.getText().toString();
        int TMAX, TMIN, UMAX;
        if(bits.equals("8bit")) {
            TMAX = Tmax8;
            TMIN = Tmin8;
            UMAX = Umax8;
        }
        else if(bits.equals("10bit")){
            TMAX = Tmax10;
            TMIN = Tmin10;
            UMAX = Umax10;
        }
        else{
            TMAX = Tmax12;
            TMIN = Tmin12;
            UMAX = Umax12;
        }
        //  boolean checked = ((RadioButton) view).isChecked();
        boolean illegal = false;
        int unsignedExpected, num = 0;
        //  switch (view.getId()) {
        switch(buttonSelected){
            //  case R.id.hex2int:
            case 0:
                String root = input.substring(2);  //get rid of '0x' prefix
                unsignedExpected = Integer.parseInt(root, 16);
                Log.i(PA, "unsignedExpected: " + unsignedExpected);
                if(unsignedExpected > UMAX)
                    illegal = true;
                break;
            case 1:
                unsignedExpected = Integer.parseInt(input);
                Log.i(PA, "unsignedExpected: " + unsignedExpected);
                if(unsignedExpected > UMAX)
                    illegal = true;
                break;
            case 2:
                num = Integer.parseInt(input); //convert to int
                Log.i(PA, "num: " + num);
                if(num > TMAX || num < TMIN)
                    illegal = true;
                break;
        }
        if(illegal){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            correct_guesses++;
            overall_guesses++;
            scoreTxt.setText("" + correct_guesses + "/" + overall_guesses);
        }
        else{
            Toast.makeText(this, "Incorrect. Input is legal.", Toast.LENGTH_SHORT).show();
            overall_guesses++;
            scoreTxt.setText("" + correct_guesses + "/" + overall_guesses);
        }
    }

    //send score back to MainActivity
    public void quit(View view){
        //pass values back to MainActivity
        //    Intent intent = new Intent();
        MainActivity.practice_score = scoreTxt.getText().toString();
        //    intent.putExtra(MESSAGE, "pillow");
        this.finish();
    }
}
