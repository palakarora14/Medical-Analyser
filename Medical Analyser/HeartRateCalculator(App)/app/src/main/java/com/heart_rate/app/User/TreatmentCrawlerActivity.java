package com.heart_rate.app.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.heart_rate.app.R;

import java.text.Normalizer;

public class TreatmentCrawlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_crawler);
        
        Toast.makeText(getApplicationContext(),"Please wait , This may take some time ! ",Toast.LENGTH_LONG).show();

        Bundle bundle=new Bundle();
        bundle=getIntent().getExtras();
        String result = bundle.getString("Disease");


        WebView webView = findViewById(R.id.webv);
        result = Normalizer.normalize(result,Normalizer.Form.NFD);
        result = result.replaceAll(" ","_");
        result = result.replaceAll("[^\\x00-\\x7F]","_");
        webView.loadUrl("http://pythonestpython.pythonanywhere.com/serve/show2/"+result+"/");
        Log.e("TAG", "http://pythonestpython.pythonanywhere.com/serve/show2/"+result+"/");
    }
}
