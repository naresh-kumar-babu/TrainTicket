package com.example.trainticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class WalletActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private TextView wallet_balance;
    private EditText wallet_money;
    private Button addmoneyBtn;
    private DatabaseReference usersRef;
    private double money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_nav_dash);
        bottomNavigationView.setSelectedItemId(R.id.menu_wallet);
        wallet_money = findViewById(R.id.wallet_money);
        wallet_balance = findViewById(R.id.wallet_balance);
        addmoneyBtn = findViewById(R.id.addmoneyBtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        wallet_money.setVisibility(View.GONE);

        if(mAuth.getCurrentUser()!=null){
            usersRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    money = Double.parseDouble(snapshot.child("wallet_balance").getValue().toString());
                    wallet_balance.setText(String.format("%.2f", money));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        addmoneyBtn.setOnClickListener(view -> {
            if(wallet_money.getVisibility()==View.GONE){
                    wallet_money.setVisibility(View.VISIBLE);
                    addmoneyBtn.setText("Proceed to Payment");
                    return;
            }
            if(wallet_money.getVisibility()==View.VISIBLE) {
                String addmoney = wallet_money.getText().toString();
                if (TextUtils.isEmpty(addmoney)) {
                    Toast.makeText(WalletActivity.this, "Please enter the amount to be added", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(addmoney) > 10000) {
                    Toast.makeText(WalletActivity.this, "More than Rs.10,000 can't be added in a single transaction", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(addmoney)<1){
                    Toast.makeText(WalletActivity.this,"Invalid amount",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(addmoney)) {
                    Intent intent = new Intent(WalletActivity.this,BookingPaymentActivity.class);
                    intent.putExtra("purpose","wallet");
                    intent.putExtra("money",String.valueOf(Double.parseDouble(addmoney) + money));
                    startActivity(intent);
                }
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.menu_dashboard:
                    Intent dIntent = new Intent(WalletActivity.this, DashboardActivity.class);
                    startActivity(dIntent);
                    return true;
                case R.id.menu_transactions:
                    Intent tIntent = new Intent(WalletActivity.this, prevJourneyActivity.class);
                    startActivity(tIntent);
                    return true;
                case R.id.menu_wallet:
                    return true;
                case R.id.menu_profile:
                    Intent pIntent = new Intent(WalletActivity.this, ProfileActivity.class);
                    startActivity(pIntent);
                    return true;
            }
            return false;
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
                Intent wIntent = new Intent(WalletActivity.this, ChangePassswordActivity.class);
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