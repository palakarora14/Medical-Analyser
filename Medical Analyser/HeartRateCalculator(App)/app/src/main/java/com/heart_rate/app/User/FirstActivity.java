package com.heart_rate.app.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.heart_rate.app.MainActivity;
import com.heart_rate.app.R;

public class FirstActivity extends Activity {

    Button button1;
    Button button2;
    Button button3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        button1=findViewById(R.id.heart_rate);
        button2=findViewById(R.id.symptoms);
        button3=findViewById(R.id.developers);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(FirstActivity.this,SymptomsActivity.class);
                startActivity(intent2);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(FirstActivity.this,AboutUsActivity.class);
                startActivity(intent3);
            }
        });

    }
}
