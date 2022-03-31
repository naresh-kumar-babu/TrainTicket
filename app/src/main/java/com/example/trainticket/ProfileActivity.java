package com.example.trainticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private TextView prof_name,prof_dob,prof_email,prof_gender,prof_phone;
    private DatabaseReference usersRef;
    private ImageButton editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_nav_dash);
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);
        prof_name = findViewById(R.id.prof_name);
        prof_dob = findViewById(R.id.prof_dob);
        prof_email = findViewById(R.id.prof_email);
        prof_gender = findViewById(R.id.prof_gender);
        prof_phone = findViewById(R.id.prof_phone);
        editProfile = findViewById(R.id.editBtn);

        if(mAuth.getCurrentUser()!=null){
            prof_email.setText(mAuth.getCurrentUser().getEmail().toString());
            usersRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    prof_name.setText(snapshot.child("Name").getValue().toString());
                    prof_dob.setText(snapshot.child("DOB").getValue().toString());
                    prof_gender.setText(snapshot.child("Gender").getValue().toString());
                    prof_phone.setText(snapshot.child("Phone").getValue().toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();

        editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
            startActivity(intent);
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.menu_dashboard:
                    Intent dIntent = new Intent(ProfileActivity.this, DashboardActivity.class);
                    startActivity(dIntent);
                    return true;
                case R.id.menu_transactions:
                    Intent tIntent = new Intent(ProfileActivity.this, prevJourneyActivity.class);
                    startActivity(tIntent);
                    return true;
                case R.id.menu_wallet:
                    Intent wIntent = new Intent(ProfileActivity.this, WalletActivity.class);
                    startActivity(wIntent);
                    return true;
                case R.id.menu_profile:
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
                Intent wIntent = new Intent(ProfileActivity.this, ChangePassswordActivity.class);
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