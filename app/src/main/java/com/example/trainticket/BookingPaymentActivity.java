package com.example.trainticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BookingPaymentActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView upi_card,netbanking_card,creditcard_card,debitcard_card,wallet_card;
    private String uid,purpose;
    private int passenger_cnt;
    private DatabaseReference usersRef;
    private CardView walletCard;
    private ArrayList<HashMap<String, Object>> passengerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_payment);
        mAuth = FirebaseAuth.getInstance();
        upi_card = findViewById(R.id.upi_card);
        netbanking_card = findViewById(R.id.netbanking_card);
        creditcard_card = findViewById(R.id.creditcard_card);
        debitcard_card  = findViewById(R.id.debitcard_card);
        wallet_card = findViewById(R.id.wallet_card);
        walletCard = findViewById(R.id.walletCard);
        purpose = getIntent().getStringExtra("purpose");
        Toolbar toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();

        if(purpose.equals("Booking")){
            walletCard.setVisibility(View.VISIBLE);
            passenger_cnt = getIntent().getIntExtra("passenger_cnt",1);
            upi_card.setOnClickListener(view -> booking());
            netbanking_card.setOnClickListener(view -> booking());
            creditcard_card.setOnClickListener(view -> booking());
            debitcard_card.setOnClickListener(view -> booking());
            wallet_card.setOnClickListener(view -> booking());
        }
        if(purpose.equals("wallet")){
            walletCard.setVisibility(View.GONE);
            String money = getIntent().getStringExtra("money");
            upi_card.setOnClickListener(view -> walletBalance(money));
            netbanking_card.setOnClickListener(view -> walletBalance(money));
            creditcard_card.setOnClickListener(view -> walletBalance(money));
            debitcard_card.setOnClickListener(view -> walletBalance(money));
        }


    }

    public void walletBalance(String money){
        System.out.println(money);
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        uid = mAuth.getCurrentUser().getUid();
        HashMap<String, Object> wallet = new HashMap<>();
        wallet.put("wallet_balance", money);
        usersRef.child(uid).updateChildren(wallet).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(BookingPaymentActivity.this,"Wallet Balance added",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BookingPaymentActivity.this,WalletActivity.class);
                startActivity(intent);
            } else {
                String message = task.getException().getMessage();
                Toast.makeText(BookingPaymentActivity.this, "Error occurred. " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void booking(){
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        uid = mAuth.getCurrentUser().getUid();
        String uniqueId = usersRef.push().getKey();
        HashMap<String, Object> train_details = new HashMap<>();
        train_details.put("seat_availability", getIntent().getStringExtra("seat_availability"));
        train_details.put("seat_type", getIntent().getStringExtra("seat_type"));
        train_details.put("train_arrival", getIntent().getStringExtra("train_arrival"));
        train_details.put("train_name", getIntent().getStringExtra("train_name"));
        train_details.put("train_departure", getIntent().getStringExtra("train_departure"));
        train_details.put("train_no", getIntent().getStringExtra("train_no"));
        train_details.put("starting_pt",getIntent().getStringExtra("from_place"));
        train_details.put("destination_pt",getIntent().getStringExtra("to_place"));
        train_details.put("journey_date",getIntent().getStringExtra("journey_date"));
        train_details.put("booking_date",new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        train_details.put("passenger_cnt",passenger_cnt);
        if(passenger_cnt>3){
            HashMap<String, Object> passenger4 = new HashMap<>();
            passenger4.put("p4_name",getIntent().getStringExtra("p4_name"));
            passenger4.put("p4_age",getIntent().getStringExtra("p4_age"));
            passenger4.put("p4_gender",getIntent().getStringExtra("p4_gender"));
            passenger4.put("p4_berth",getIntent().getStringExtra("p4_berth"));
            passengerList.add(passenger4);
        }if(passenger_cnt>2){
            HashMap<String, Object> passenger3 = new HashMap<>();
            passenger3.put("p3_name",getIntent().getStringExtra("p3_name"));
            passenger3.put("p3_age",getIntent().getStringExtra("p3_age"));
            passenger3.put("p3_gender",getIntent().getStringExtra("p3_gender"));
            passenger3.put("p3_berth",getIntent().getStringExtra("p3_berth"));
            passengerList.add(passenger3);
        }if(passenger_cnt>1){
            HashMap<String, Object> passenger2 = new HashMap<>();
            passenger2.put("p2_name",getIntent().getStringExtra("p2_name"));
            passenger2.put("p2_age",getIntent().getStringExtra("p2_age"));
            passenger2.put("p2_gender",getIntent().getStringExtra("p2_gender"));
            passenger2.put("p2_berth",getIntent().getStringExtra("p2_berth"));
            passengerList.add(passenger2);
        }if(passenger_cnt>0){
            HashMap<String, Object> passenger1 = new HashMap<>();
            passenger1.put("p1_name",getIntent().getStringExtra("p1_name"));
            passenger1.put("p1_age",getIntent().getStringExtra("p1_age"));
            passenger1.put("p1_gender",getIntent().getStringExtra("p1_gender"));
            passenger1.put("p1_berth",getIntent().getStringExtra("p1_berth"));
            passengerList.add(passenger1);
        }
        usersRef.child(uid).child("Booking").child(uniqueId).updateChildren(train_details).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Intent intent = new Intent(BookingPaymentActivity.this, prevJourneyActivity.class);
                for(int i=1;i<=passenger_cnt;i++){
                    usersRef.child(uid).child("Booking").child(uniqueId).child("Passengers").child("Passenger-"+i).updateChildren(passengerList.get(passenger_cnt-i)).addOnCompleteListener(task1 -> {
                        if(task.isSuccessful()){
                           }else{
                            String message = task.getException().getMessage();
                            Toast.makeText(BookingPaymentActivity.this, "Error occurred. " + message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                Toast.makeText(BookingPaymentActivity.this,"Booking Successful",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }else{
                String message = task.getException().getMessage();
                Toast.makeText(BookingPaymentActivity.this, "Error occurred. " + message, Toast.LENGTH_SHORT).show();
            }
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
                Intent wIntent = new Intent(BookingPaymentActivity.this, WalletActivity.class);
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