package com.example.trainticket;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.DatePicker;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;

public class SignupActivity extends AppCompatActivity {

    private EditText EmailET, PasswordET, RePasswordET, NameET,PhoneET;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView DateT,MaleT, FemaleT, OtherT;
    boolean maleb,femaleb, otherb;
    int year,month,day;
    private Button SignupButton;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String uid;
    private CoordinatorLayout coordinatorLayout;
    String name, date, email, password, re_password,gender,phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final ProgressBar progressBar = findViewById(R.id.spin_kit_reg);
        Sprite doubleBounce = new FoldingCube();
        progressBar.setIndeterminateDrawable(doubleBounce);

        NameET = findViewById(R.id.pd_name);
        DateT = findViewById(R.id.pd_dob);
        MaleT = findViewById(R.id.male_select);
        FemaleT = findViewById(R.id.female_select);
        OtherT = findViewById(R.id.other_select);
        PhoneET = findViewById(R.id.signup_phoneno);
        EmailET = findViewById(R.id.signup_email);
        PasswordET = findViewById(R.id.signup_password);
        RePasswordET = findViewById(R.id.signup_confirm_password);
        SignupButton = findViewById(R.id.signupButton);

        coordinatorLayout = findViewById(R.id.signUpCoordinatorLayout);

        mAuth = FirebaseAuth.getInstance();

        DateT.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    SignupActivity.this,
                    android.R.style.Theme_Material_Light_Dialog,
                    mDateSetListener,
                    year,month,day
            );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.show();
        });
        mDateSetListener = (datePicker, i, i1, i2) -> {
            year = i;
            month = i1+1;
            day = i2;
            date = day + "/" + month + "/" + year;
            DateT.setText(date);
        };

        MaleT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaleT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                MaleT.setTextColor(getResources().getColor(R.color.colorWhite));
                OtherT.setTextColor(getResources().getColor(R.color.grey));
                FemaleT.setTextColor(getResources().getColor(R.color.grey));
                FemaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                OtherT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                maleb = true;
                femaleb = false;
                otherb = false;
            }
        });
        FemaleT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                FemaleT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                FemaleT.setTextColor(getResources().getColor(R.color.colorWhite));
                MaleT.setTextColor(getResources().getColor(R.color.grey));
                OtherT.setTextColor(getResources().getColor(R.color.grey));
                OtherT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                maleb = false;
                femaleb = true;
                otherb = false;

            }
        });
        OtherT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                FemaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                OtherT.setTextColor(getResources().getColor(R.color.colorWhite));
                MaleT.setTextColor(getResources().getColor(R.color.grey));
                FemaleT.setTextColor(getResources().getColor(R.color.grey));
                OtherT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                maleb = false;
                femaleb = false;
                otherb = true;

            }
        });

        SignupButton.setOnClickListener(view -> {
            if(maleb || femaleb || otherb)
            {
                if (maleb)
                    gender = "Male";
                if(femaleb)
                    gender = "Female";
                if(otherb)
                    gender = "Other";
            }
            name = NameET.getText().toString();
            email = EmailET.getText().toString();
            password = PasswordET.getText().toString();
            re_password = RePasswordET.getText().toString();
            phoneno = PhoneET.getText().toString();
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(date) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(phoneno) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(re_password))
            {
                Toast.makeText(SignupActivity.this,"All the fields are mandatory",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                Toast.makeText(SignupActivity.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isValidMobile(phoneno))
            {
                Toast.makeText(SignupActivity.this,"Invalid Phone Number",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!(TextUtils.isEmpty(name) && TextUtils.isEmpty(date) && TextUtils.isEmpty(gender) &&  TextUtils.isEmpty(phoneno) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(re_password)))
            {
                if(password.equals(re_password))
                {
                    if(isValidPassword(password)) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        usersRef = FirebaseDatabase.getInstance().getReference("Users");
                                        uid = mAuth.getCurrentUser().getUid();
                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("Name", name);
                                        result.put("DOB", date);
                                        result.put("Gender",gender);
                                        result.put("Phone",phoneno);
                                        result.put("role", "user");
                                        usersRef.child(uid).updateChildren(result).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                SignupButton.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.green));
                                                progressBar.setVisibility(View.VISIBLE);
                                                Toast.makeText(SignupActivity.this, "Sign Up completed Successfully!", Toast.LENGTH_SHORT).show();
                                                Intent mainIntent = new Intent(SignupActivity.this, DashboardActivity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                            } else {
                                                String message = task1.getException().getMessage();
                                                Toast.makeText(SignupActivity.this, "Error occurred. " + message, Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });


                                    /* FirebaseUser user = mAuth.getCurrentUser();
                                       user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {*/
                                       /*         Toast.makeText(SignupActivity.this, "Verification Link Has Been Sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignupActivity.this, "Could not send Verification link. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });*/

                                        /*Toast.makeText(SignupActivity.this, "Great! You're one among us now", Toast.LENGTH_SHORT).show();
                                        Intent mainIntent = new Intent(SignupActivity.this, Verifyemail.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);

                                         */
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SignupActivity.this, "Error occurred. " + message, Toast.LENGTH_SHORT).show();
                                    }

                                });
                    }else{
                        Snackbar.make(coordinatorLayout,"Password should contain atleast one Numeric, Special, Uppercase and Lowercase character.",Snackbar.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean isValidMobile(String phone) {
        return phone.matches("[6-9][0-9]{9}");
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}