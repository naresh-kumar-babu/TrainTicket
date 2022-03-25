package com.example.trainticket;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =101 ;
    private FirebaseAuth mAuth;
    String email, password;
    private EditText EmailET, PasswordET;
    private Button LoginButton;
    private TextView ForgotLink,tvLogin,signupLink;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        EmailET = findViewById(R.id.email_et);
        PasswordET = findViewById(R.id.password_et);
        LoginButton = findViewById(R.id.login_button);
        tvLogin = findViewById(R.id.tvLogin);
        ForgotLink = findViewById(R.id.tvForgot);
        signupLink = findViewById(R.id.btRegister);
        final ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        if(mAuth.getCurrentUser() != null)
        {
            usersRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("role").getValue().toString().equals("user")) {
                        Toast.makeText(LoginActivity.this,"Logged In Successfully",Toast.LENGTH_SHORT);
                        Intent mainIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        Intent mainIntent = new Intent(LoginActivity.this, AdmindbActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                email = EmailET.getText().toString();
                password = PasswordET.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, "Please fill all the credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)))
                {
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
                                        usersRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                LoginButton.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.green));
                                                progressBar.setVisibility(View.VISIBLE);
                                                System.out.println(snapshot.child("role").getValue().toString());
                                                if (snapshot.child("role").getValue().toString().equals("user")) {
                                                    Intent mainIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                    finish();
                                                } else {
                                                    Intent mainIntent = new Intent(LoginActivity.this, AdmindbActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                    finish();
                                                }

                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    else
                                    {
                                      /*  progressBar.setVisibility(View.GONE);
                                        LoginButton.setBackgroundTintList(null);*/
                                        String message = task.getException().getMessage();
                                        Toast.makeText(LoginActivity.this, "Error occured. "+message, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

        ForgotLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                email = EmailET.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(LoginActivity.this, "Please provide your email address to reset your password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "Password reset link has been sent to your inbox.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error occurred. "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signupLink.setOnClickListener(view -> {
            Intent signupIntent =  new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signupIntent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View v) {
        if (v==LoginButton){
            Intent intent   = new Intent(LoginActivity.this,SignupActivity.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
            startActivity(intent,activityOptions.toBundle());
        }
    }
}