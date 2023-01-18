package edu.plu.heathdj.mocha;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    Button[] createButton = new Button[14];
    public static final String createNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        createButton[0] = (Button)findViewById(R.id.button0);
        createButton[1] = (Button)findViewById(R.id.button1);
        createButton[2] = (Button)findViewById(R.id.button2);
        createButton[3] = (Button)findViewById(R.id.button3);
        createButton[4] = (Button)findViewById(R.id.button4);
        createButton[5] = (Button)findViewById(R.id.button5);
        createButton[6] = (Button)findViewById(R.id.button6);
        createButton[7] = (Button)findViewById(R.id.button7);
        createButton[8] = (Button)findViewById(R.id.button8);
        createButton[9] = (Button)findViewById(R.id.button9);
        createButton[10] = (Button)findViewById(R.id.button10);
        createButton[11] = (Button)findViewById(R.id.button11);
        createButton[12] = (Button)findViewById(R.id.button12);
        createButton[13] = (Button)findViewById(R.id.button13);
        for(int i=0;i<14;i++) createButton[i].setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int whatToDo;
        switch (view.getId()) {
            case R.id.button0:
                whatToDo = 0;
                break;
            case R.id.button1:
                whatToDo = 1;
                break;
            case R.id.button2:
                whatToDo = 2;
                break;
            case R.id.button3:
                whatToDo = 3;
                break;
            case R.id.button4:
                whatToDo = 4;
                break;
            case R.id.button5:
                whatToDo = 5;
                break;
            case R.id.button6:
                whatToDo = 6;
                break;
            case R.id.button7:
                whatToDo = 7;
                break;
            case R.id.button8:
                whatToDo = 8;
                break;
            case R.id.button9:
                whatToDo = 9;
                break;
            case R.id.button10:
                whatToDo = 10;
                break;
            case R.id.button11:
                whatToDo = 11;
                break;
            case R.id.button12:
                whatToDo = 12;
                break;
            case R.id.button13:
                whatToDo = 13;
                break;
            default:
                whatToDo = 0;
                break;
        }
        android.content.Intent intent = new android.content.Intent(this,MainActivity.class);
        intent.putExtra(createNumber,whatToDo);
        startActivity(intent);
    }
}