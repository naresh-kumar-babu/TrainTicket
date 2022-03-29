package com.example.trainticket;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class PassengerDetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Spinner p1_berth,p2_berth,p3_berth,p4_berth;
    private Button addBtn,payment,deleteBtn;
    private int passenger_count=1;
    private CardView p1,p2,p3,p4;
    private CardView[] cardsArray;
    private String seat_type, seat_availability, train_name, train_no, train_arrival, train_departure;
    private boolean[] flag = {true,false,false,false};
    private int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);
        mAuth = FirebaseAuth.getInstance();
        p1_berth = findViewById(R.id.p1_berth);
        p2_berth = findViewById(R.id.p2_berth);
        p3_berth = findViewById(R.id.p3_berth);
        p4_berth = findViewById(R.id.p4_berth);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        seat_availability = getIntent().getStringExtra("seat_availability").toString();
        seat_type = getIntent().getStringExtra("seat_type").toString();
        train_arrival = getIntent().getStringExtra("train_arrival").toString();
        train_name = getIntent().getStringExtra("train_name").toString();
        train_departure = getIntent().getStringExtra("train_departure").toString();
        train_no = getIntent().getStringExtra("train_no").toString();
        cardsArray  = new CardView[]{p1, p2, p3, p4};
        addBtn = findViewById(R.id.addBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        payment = findViewById(R.id.proceed_to_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();

        if(passenger_count==1){
            deleteBtn.setVisibility(View.GONE);
        }

        String[] berth = new String[]{"No Preference","Lower Berth","Middle Berth","Upper Berth"};
        ArrayAdapter<String> adapter_berth = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, berth);
        p1_berth.setAdapter(adapter_berth);
        p2_berth.setAdapter(adapter_berth);
        p3_berth.setAdapter(adapter_berth);
        p4_berth.setAdapter(adapter_berth);

        addBtn.setOnClickListener(view -> {
            if(passenger_count>Integer.parseInt(seat_availability)){
                Toast.makeText(PassengerDetailsActivity.this,"No more seats available",Toast.LENGTH_SHORT).show();
            }
            else if(passenger_count < 4 ){
                cardsArray[passenger_count].setVisibility(View.VISIBLE);
                flag[passenger_count] = true;
                passenger_count++;
                if(passenger_count == 4)
                    addBtn.setVisibility(View.GONE);
                if(passenger_count == 2)
                    deleteBtn.setVisibility(View.VISIBLE);
            }
        });
        deleteBtn.setOnClickListener(view -> {
            if(passenger_count>1){
                cardsArray[passenger_count-1].setVisibility(View.GONE);
                flag[passenger_count-1] = false;
                passenger_count--;
                if(passenger_count==1) {
                    deleteBtn.setVisibility(View.GONE);
                    addBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        payment.setOnClickListener(view -> {
            Intent intent = new Intent(PassengerDetailsActivity.this, BookingPaymentActivity.class);
            for(int i=0;i<4;i++){
                if(flag[i]){
                    if(i==0){
                        EditText p1_name = findViewById(R.id.p1_name);
                        EditText p1_age = findViewById(R.id.p1_age);
                        RadioGroup p1_gender = findViewById(R.id.p1_gender);
                        RadioButton p1_gender_c = findViewById(p1_gender.getCheckedRadioButtonId());
                        String _p1_name = p1_name.getText().toString();
                        String _p1_age = p1_age.getText().toString();
                        String _p1_gender = p1_gender_c.getText().toString();
                        String _p1_berth = p1_berth.getSelectedItem().toString();
                        if(TextUtils.isEmpty(_p1_name) || TextUtils.isEmpty(_p1_berth) || TextUtils.isEmpty(_p1_gender) || TextUtils.isEmpty(_p1_age)){
                            Toast.makeText(PassengerDetailsActivity.this,"Please fill all the details", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            cnt++;
                            intent.putExtra("p1_name",_p1_name);
                            intent.putExtra("p1_age",_p1_age);
                            intent.putExtra("p1_gender",_p1_gender);
                            intent.putExtra("p1_berth",_p1_berth);
                            intent.putExtra("passenger_cnt",cnt);
                        }
                    }
                    else if(i==1){
                        EditText p2_name = findViewById(R.id.p2_name);
                        EditText p2_age = findViewById(R.id.p2_age);
                        RadioGroup p2_gender = findViewById(R.id.p2_gender);
                        RadioButton p2_gender_c = findViewById(p2_gender.getCheckedRadioButtonId());
                        String _p2_name = p2_name.getText().toString();
                        String _p2_age = p2_age.getText().toString();
                        String _p2_gender = p2_gender_c.getText().toString();
                        String _p2_berth = p2_berth.getSelectedItem().toString();
                        if(TextUtils.isEmpty(_p2_name) || TextUtils.isEmpty(_p2_berth) || TextUtils.isEmpty(_p2_gender) || TextUtils.isEmpty(_p2_age)){
                            Toast.makeText(PassengerDetailsActivity.this,"Please fill all the details", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            cnt++;
                            intent.putExtra("p2_name",_p2_name);
                            intent.putExtra("p2_age",_p2_age);
                            intent.putExtra("p2_gender",_p2_gender);
                            intent.putExtra("p2_berth",_p2_berth);
                            intent.putExtra("passenger_cnt",cnt);
                        }
                    }
                    else if(i==2){
                        EditText p3_name = findViewById(R.id.p3_name);
                        EditText p3_age = findViewById(R.id.p3_age);
                        RadioGroup p3_gender = findViewById(R.id.p3_gender);
                        RadioButton p3_gender_c = findViewById(p3_gender.getCheckedRadioButtonId());
                        String _p3_name = p3_name.getText().toString();
                        String _p3_age = p3_age.getText().toString();
                        String _p3_gender = p3_gender_c.getText().toString();
                        String _p3_berth = p3_berth.getSelectedItem().toString();
                        if(TextUtils.isEmpty(_p3_name) || TextUtils.isEmpty(_p3_berth) || TextUtils.isEmpty(_p3_gender) || TextUtils.isEmpty(_p3_age)){
                            Toast.makeText(PassengerDetailsActivity.this,"Please fill all the details", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            cnt++;
                            intent.putExtra("p3_name",_p3_name);
                            intent.putExtra("p3_age",_p3_age);
                            intent.putExtra("p3_gender",_p3_gender);
                            intent.putExtra("p3_berth",_p3_berth);
                            intent.putExtra("passenger_cnt",cnt);
                        }
                    }
                    else if(i==3){
                        EditText p4_name = findViewById(R.id.p4_name);
                        EditText p4_age = findViewById(R.id.p4_age);
                        RadioGroup p4_gender = findViewById(R.id.p4_gender);
                        RadioButton p4_gender_c = findViewById(p4_gender.getCheckedRadioButtonId());
                        String _p4_name = p4_name.getText().toString();
                        String _p4_age = p4_age.getText().toString();
                        String _p4_gender = p4_gender_c.getText().toString();
                        String _p4_berth = p4_berth.getSelectedItem().toString();
                        if(TextUtils.isEmpty(_p4_name) || TextUtils.isEmpty(_p4_berth) || TextUtils.isEmpty(_p4_gender) || TextUtils.isEmpty(_p4_age)){
                            Toast.makeText(PassengerDetailsActivity.this,"Please fill all the details", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            cnt++;
                            intent.putExtra("p4_name",_p4_name);
                            intent.putExtra("p4_age",_p4_age);
                            intent.putExtra("p4_gender",_p4_gender);
                            intent.putExtra("p4_berth",_p4_berth);
                            intent.putExtra("passenger_cnt",cnt);
                        }
                    }
                }
            }
            intent.putExtra("seat_availability",seat_availability);
            intent.putExtra("seat_type",seat_type);
            intent.putExtra("train_arrival",train_arrival);
            intent.putExtra("train_name",train_name);
            intent.putExtra("train_departure",train_departure);
            intent.putExtra("train_no",train_no);
            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changePassword:
                Intent wIntent = new Intent(PassengerDetailsActivity.this, WalletActivity.class);
                startActivity(wIntent);
                return true;
            case R.id.logout:
                mAuth.signOut ();
                finish ();
                startActivity ( new Intent ( this, LoginActivity.class ) );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}