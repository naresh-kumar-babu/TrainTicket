package com.example.trainticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class prevJourneyActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_journey);
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_nav_dash);
        bottomNavigationView.setSelectedItemId(R.id.menu_transactions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.menu_dashboard:
                    Intent dIntent = new Intent(prevJourneyActivity.this, DashboardActivity.class);
                    startActivity(dIntent);
                    return true;
                case R.id.menu_transactions:
                    return true;
                case R.id.menu_wallet:
                    Intent wIntent = new Intent(prevJourneyActivity.this, WalletActivity.class);
                    startActivity(wIntent);
                    return true;
                case R.id.menu_profile:
                    Intent pIntent = new Intent(prevJourneyActivity.this, ProfileActivity.class);
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
                Intent wIntent = new Intent(prevJourneyActivity.this, ChangePassswordActivity.class);
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