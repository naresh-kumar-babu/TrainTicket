package com.example.trainticket;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {

    private Button cancelBtn, updateBtn;
    private EditText EmailET, NameET,PhoneET;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView DateT,MaleT, FemaleT, OtherT;
    boolean maleb,femaleb, otherb;
    int year,month,day;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String uid;
    String name, date, gender,phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        cancelBtn = findViewById(R.id.editProfile_cancel);
        updateBtn = findViewById(R.id.editProfile_update);
        NameET = findViewById(R.id.pd_name);
        DateT = findViewById(R.id.pd_dob);
        MaleT = findViewById(R.id.male_select);
        FemaleT = findViewById(R.id.female_select);
        OtherT = findViewById(R.id.other_select);
        PhoneET = findViewById(R.id.signup_phoneno);
        EmailET = findViewById(R.id.signup_email);

        mAuth = FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser()!=null){
            EmailET.setText(mAuth.getCurrentUser().getEmail());
            usersRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NameET.setText(snapshot.child("Name").getValue().toString());
                    date = snapshot.child("DOB").getValue().toString();
                    DateT.setText(date);
                    gender = snapshot.child("Gender").getValue().toString();
                    if(gender.equals("Male")){
                        MaleT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                        MaleT.setTextColor(getResources().getColor(R.color.colorWhite));
                        OtherT.setTextColor(getResources().getColor(R.color.grey));
                        FemaleT.setTextColor(getResources().getColor(R.color.grey));
                        FemaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                        OtherT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        maleb = true;
                        femaleb = false;
                        otherb = false;
                    }else if(gender.equals("Female")){
                        MaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        FemaleT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                        FemaleT.setTextColor(getResources().getColor(R.color.colorWhite));
                        MaleT.setTextColor(getResources().getColor(R.color.grey));
                        OtherT.setTextColor(getResources().getColor(R.color.grey));
                        OtherT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        maleb = false;
                        femaleb = true;
                        otherb = false;
                    }else{
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
                    PhoneET.setText(snapshot.child("Phone").getValue().toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        DateT.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    EditProfileActivity.this,
                    android.R.style.Theme_Material_Light_Dialog,
                    mDateSetListener,
                    year,month,day
            );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            cal.add(Calendar.YEAR,-15);
            dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            dialog.show();
        });
        mDateSetListener = (datePicker, i, i1, i2) -> {
            year = i;
            month = i1+1;
            day = i2;
            date = day + "/" + month + "/" + year;
            DateT.setText(date);
        };

        MaleT.setOnClickListener(view -> {
            MaleT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
            MaleT.setTextColor(getResources().getColor(R.color.colorWhite));
            OtherT.setTextColor(getResources().getColor(R.color.grey));
            FemaleT.setTextColor(getResources().getColor(R.color.grey));
            FemaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            OtherT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            maleb = true;
            femaleb = false;
            otherb = false;
        });
        FemaleT.setOnClickListener(view -> {
            MaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            FemaleT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
            FemaleT.setTextColor(getResources().getColor(R.color.colorWhite));
            MaleT.setTextColor(getResources().getColor(R.color.grey));
            OtherT.setTextColor(getResources().getColor(R.color.grey));
            OtherT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            maleb = false;
            femaleb = true;
            otherb = false;
        });
        OtherT.setOnClickListener(view -> {
            MaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            FemaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            OtherT.setTextColor(getResources().getColor(R.color.colorWhite));
            MaleT.setTextColor(getResources().getColor(R.color.grey));
            FemaleT.setTextColor(getResources().getColor(R.color.grey));
            OtherT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
            maleb = false;
            femaleb = false;
            otherb = true;
        });

        cancelBtn.setOnClickListener(view -> finish());
        updateBtn.setOnClickListener(view -> {
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
            phoneno = PhoneET.getText().toString();
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(date) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(phoneno))
            {
                Toast.makeText(EditProfileActivity.this,"All the fields are mandatory",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isValidName(name))
            {
                Toast.makeText(EditProfileActivity.this,"Invalid Name",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isValidMobile(phoneno))
            {
                Toast.makeText(EditProfileActivity.this,"Invalid Phone Number",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!(TextUtils.isEmpty(name) && TextUtils.isEmpty(date) && TextUtils.isEmpty(gender) &&  TextUtils.isEmpty(phoneno)))
            {
                usersRef = FirebaseDatabase.getInstance().getReference("Users");
                uid = mAuth.getCurrentUser().getUid();
                HashMap<String, Object> result = new HashMap<>();
                result.put("Name", name);
                result.put("DOB", date);
                result.put("Gender",gender);
                result.put("Phone",phoneno);
                usersRef.child(uid).updateChildren(result).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated Successfully!", Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                    } else {
                        String message = task1.getException().getMessage();
                        Toast.makeText(EditProfileActivity.this, "Error occurred. " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public boolean isValidName(final String name){
        Pattern pattern;
        Matcher matcher;
        final String NAME_PATTERN = "^[\\p{L} '-]+$";
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private boolean isValidMobile(String phone) {
        return phone.matches("[6-9][0-9]{9}");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}