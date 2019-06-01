package com.heart_rate.app.User;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.heart_rate.app.R;

import java.io.Serializable;

public class SymptomsActivity extends Activity {

    String[] symptoms1=new String[]{"itching","skin_rash","nodal_skin_eruptions","continuous_sneezing","shivering","chills","joint_pain",
            "stomach_pain","acidity","ulcers_on_tongue","muscle_wasting","vomiting", "burning_micturition","spotting_urination","fatigue",
            "weight_gain","anxiety","cold_hands_and_feets","mood_swings","weight_loss","restlessness","lethargy"};

    String[] symptoms2=new String[]{"patches_in_throat","irregular_sugar_level","cough","high_fever","sunken_eyes","breathlessness","sweating",
            "dehydration","indigestion","headache","yellowish_skin","dark_urine","nausea","loss_of_appetite","pain_behind_the_eyes","back_pain","constipation",
            "abdominal_pain","diarrhoea","mild_fever","yellow_urine","yellowing_of_eyes"};

    String[] symptoms3=new String[]{"acute_liver_failure","fluid_overload","swelling_of_stomach","swelled_lymph_nodes","malaise",
            "blurred_and_distorted_vision","phlegm","throat_irritation","redness_of_eyes","sinus_pressure","runny_nose","congestion",
            "chest_pain","weakness_in_limbs","fast_heart_rate","pain_during_bowel_movements","pain_in_anal_region","bloody_stool",
            "irritation_in_anus","neck_pain","dizziness","cramps"};

    String[] symptoms4=new String[]{"bruising","obesity","swollen_legs","swollen_blood_vessels","puffy_face_and_eyes","enlarged_thyroid",
            "brittle_nails","swollen_extremeties","excessive_hunger","extra_marital_contacts","drying_and_tingling_lips",
            "slurred_speech","knee_pain","hip_joint_pain","muscle_weakness","stiff_neck","swelling_joints","movement_stiffness",
            "spinning_movements","loss_of_balance","unsteadiness","weakness_of_one_body_side"};

    String[] symptoms5=new String[]{"loss_of_smell","bladder_discomfort","foul_smell_of_urine","continuous_feel_of_urine","passage_of_gases",
            "internal_itching","toxic_look_(typhos)","depression","irritability","muscle_pain","altered_sensorium","red_spots_over_body",
            "belly_pain","abnormal_menstruation","dischromic_patches","watering_from_eyes","increased_appetite","polyuria",
            "family_history","mucoid_sputum","rusty_sputum"};

    String[] symptoms6=new String[]{"lack_of_concentration","visual_disturbances","receiving_blood_transfusion","receiving_unsterile_injections",
            "coma","stomach_bleeding","distention_of_abdomen","history_of_alcohol_consumption","fluid_overload","blood_in_sputum",
            "prominent_veins_on_calf","palpitations","painful_walking","pus_filled_pimples","blackheads","scurring","skin_peeling",
            "silver_like_dusting","small_dents_in_nails","inflammatory_nails","blister","red_sore_around_nose","yellow_crust_ooze"};

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    Spinner spinner5;
    Spinner spinner6;
    Button button;

    String heart_rate=null;

    public class SpinnersData implements Serializable
    {
        private String spinner1;
        private String spinner2;
        private String spinner3;
        private String spinner4;
        private String spinner5;
        private String spinner6;


        public String getSpinner1()
        {
            return spinner1;
        }
        public void setSpinner1(String spinner1)
        {
            this.spinner1 = spinner1;
        }
        public String getSpinner2()
        {
            return spinner2;
        }
        public void setSpinner2(String spinner2)
        {
            this.spinner2 = spinner2;
        }
        public String getSpinner3()
        {
            return spinner3;
        }
        public void setSpinner3(String spinner3)
        {
            this.spinner3 = spinner3;
        }
        public String getSpinner4()
        {
            return spinner4;
        }
        public void setSpinner4(String spinner4)
        {
            this.spinner4 = spinner4;
        }
        public String getSpinner5()
        {
            return spinner5;
        }
        public void setSpinner5(String spinner5)
        {
            this.spinner5 = spinner5;
        }
        public String getSpinner6()
        {
            return spinner6;
        }
        public void setSpinner6(String spinner6)
        {
            this.spinner5 = spinner5;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        //Get Heart rate of the user

        Bundle bundle;
        bundle= getIntent().getExtras();



        //heart_rate = bundle.getString("Heart Rate");

        //final String heart_rate = bundle.getString("Heart Rate");

        spinner1=findViewById(R.id.spinnerId1);
        ArrayAdapter arrayAdapter1= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,symptoms1);
        spinner1.setAdapter(arrayAdapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos =position;
                String value1=(String )spinner1.getItemAtPosition(pos);
                //Toast.makeText(getApplicationContext(), value1, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2=findViewById(R.id.spinnerId2);
        ArrayAdapter arrayAdapter2= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,symptoms2);
        spinner2.setAdapter(arrayAdapter2);


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos =position;
                String value2=(String )spinner2.getItemAtPosition(pos);
               // Toast.makeText(getApplicationContext(), value2, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3=findViewById(R.id.spinnerId3);
        ArrayAdapter arrayAdapter3= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,symptoms3);
        spinner3.setAdapter(arrayAdapter3);


        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos =position;
                String value3=(String )spinner3.getItemAtPosition(pos);
               // Toast.makeText(getApplicationContext(), value3, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner4=findViewById(R.id.spinnerId4);
        ArrayAdapter arrayAdapter4= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,symptoms4);
        spinner4.setAdapter(arrayAdapter4);


        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos =position;
                String value4=(String )spinner4.getItemAtPosition(pos);
                //Toast.makeText(getApplicationContext(), value4, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner5=findViewById(R.id.spinnerId5);
        ArrayAdapter arrayAdapter5= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,symptoms5);
        spinner5.setAdapter(arrayAdapter5);


        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos =position;
                String value5=(String )spinner5.getItemAtPosition(pos);
               // Toast.makeText(getApplicationContext(), value5, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner6=findViewById(R.id.spinnerId6);
        ArrayAdapter arrayAdapter6= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,symptoms6);
        spinner6.setAdapter(arrayAdapter6);

        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos =position;
                String value6=(String )spinner6.getItemAtPosition(pos);
               // Toast.makeText(getApplicationContext(), value6, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Toast.makeText(getApplicationContext(),"Select Your Symptoms and then click the Button for prediction",Toast.LENGTH_LONG).show();
        
        button=findViewById(R.id.disease_pred);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                Intent intent=new Intent(SymptomsActivity.this,DiseasePredictionActivity.class);

                bundle.putString("Symptom1",spinner1.getSelectedItem().toString());
                bundle.putString("Symptom2",spinner2.getSelectedItem().toString());
                bundle.putString("Symptom3",spinner3.getSelectedItem().toString());
                bundle.putString("Symptom4",spinner4.getSelectedItem().toString());
                bundle.putString("Symptom5",spinner5.getSelectedItem().toString());
                bundle.putString("Symptom6",spinner6.getSelectedItem().toString());
                //bundle.putString("Heart Rate User", heart_rate);


                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
