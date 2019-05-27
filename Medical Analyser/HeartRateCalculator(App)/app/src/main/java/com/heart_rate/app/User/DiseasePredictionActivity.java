package com.heart_rate.app.User;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.heart_rate.app.R;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

public class DiseasePredictionActivity extends AppCompatActivity {

    Button btnPredict;
    Button btnCrawler;

    EditText editText;

    Interpreter tflite;

    InputStream inputStream;

    String prediction=null;
    //String disease;

    float pred_value=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_prediction);

        Bundle bundle;
        bundle= getIntent().getExtras();

       final String Symp1 = bundle.getString("Symptom1");
       final String Symp2 = bundle.getString("Symptom2");
       final String Symp3 = bundle.getString("Symptom3");
       final String Symp4 = bundle.getString("Symptom4");
       final String Symp5 = bundle.getString("Symptom5");
       final String Symp6 = bundle.getString("Symptom6");
       //final String heart_rate = bundle.getString("Heart Rate User");


        final TextView textView1 = findViewById(R.id.textView1);
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView3);
        TextView textView4 = findViewById(R.id.textView4);
        TextView textView5 = findViewById(R.id.textView5);
        TextView textView6 = findViewById(R.id.textView6);
        final TextView textViewID = findViewById(R.id.textViewId);
        Button btnCrawler =findViewById(R.id.buttonCrawler);



        textView1.setText("1. " + Symp1);
        textView2.setText("2. " + Symp2);
        textView3.setText("3. " + Symp3);
        textView4.setText("4. " + Symp4);
        textView5.setText("5. " + Symp5);
        textView6.setText("6. " + Symp6);
        //textView8.setText(heart_rate +" BPM");


        editText = findViewById(R.id.editViewId);

        btnPredict = findViewById(R.id.predictButton);

        //for loading the model
        try{
          tflite=new Interpreter(loadModelFile());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] input = new String[]{Symp1,Symp2,Symp3,Symp4,Symp5,Symp6};
                String prediction=doInference(input);
                textViewID.setText("This may be Your disease : " + "("+ pred_value*100 +")" +"accuracy");
                editText.setText(prediction);


            }
        });

        btnCrawler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String disease=editText.getText().toString();
                Log.e("disease",disease);
                Bundle bundle1=new Bundle();
                Intent intent=new Intent(DiseasePredictionActivity.this,TreatmentCrawlerActivity.class);
                bundle1.putString("Disease",disease);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
    }


    public String doInference(String[] user_symptoms){

        int val=0;
        int array[]=ReadCSV_symptoms(user_symptoms);


        float[][] input=new float[][]{{0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0}};


        for(int j=0;j<6;j++)
        {

            input[0][array[j]]=1;

            Log.e("reverse", String.valueOf(input[0][array[j]]));
        }

         float output[][]={{0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0}};

        tflite.run(input,output);

        int inferredValue=0;

        for(int i=0;i<44;i++) {

            if(output[0][i] >0.4)
            {
              inferredValue =i;
              pred_value=output[0][i];
              Log.e("value",String.valueOf(output[0][i]));
            }
            Log.e("run", String.valueOf(inferredValue));
        }

        //int random = 5 + Random.nextInt(10 - 5 + 1);

        prediction =ReadCSV_diseases(inferredValue);
        Log.e("ans",prediction);

        return  prediction;
    }

    private int[] ReadCSV_symptoms(String[] user_symptoms) {

       int arr[]=new int[10];

        //Reading the Symptoms_dict csv file
        inputStream = getResources().openRawResource(R.raw.symptoms_dict);
        try {
            Scanner scanner=new Scanner(inputStream);
            scanner.next();
            while (scanner.hasNext()){
                String data=scanner.next();
                for(int i=0;i<user_symptoms.length;i++){

                    if(data.contains(user_symptoms[i])){
                        Log.e("data",data);
                        String intValue = data.replaceAll("[^0-9]", ""); // returns 123

                        arr[i]=Integer.parseInt(intValue);
                        Log.e("reverse", String.valueOf(arr[i]));
                        //Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();

                    }
                }
            }
            scanner.close();

        } catch (Exception e){
            Log.e("An Error has occured",e.toString());
        }
        return arr;
    }

    private  String  ReadCSV_diseases(int val){
        String disease=null;
        //Reading the Disease_dict csv file

        inputStream = getResources().openRawResource(R.raw.disease_dict);
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            //Scanner scanner=new Scanner(inputStream);
            //scanner.next();
            String data;
            while ((data=bufferedReader.readLine())!=null){

                //Log.e("dis",data);
                if(data.contains(String.valueOf(val)))
                {
                    String dis[]=data.split(",");
                    //Log.e("dis",disease);
                    disease=dis[1];
                    Log.e("dis",disease);
                   // disease=data.replaceAll("[^a-zA-Z\\s]", "");
                }
            }
            //scanner.close();

        } catch (Exception e){
            Log.e("An Error has occured",e.toString());
        }
        return disease;
    }

    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor fileDescriptor =this.getAssets().openFd("model.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset =fileDescriptor.getStartOffset();
        long declaredLength =fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }

}

