package com.example.trainticket;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassswordActivity extends AppCompatActivity {

    private Button cancelBtn, updateBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private AuthCredential credential;
    private CoordinatorLayout coordinatorLayout;
    private EditText curr_password, new_password, retype_password;
    private String currentPassword, newPassword, retypePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passsword);
        cancelBtn = findViewById(R.id.editProfile_cancel);
        updateBtn = findViewById(R.id.editProfile_update);
        curr_password = findViewById(R.id.current_password);
        new_password = findViewById(R.id.new_password);
        retype_password = findViewById(R.id.retype_password);
        mAuth = FirebaseAuth.getInstance();

        coordinatorLayout = findViewById(R.id.changePasswordlayout);

        cancelBtn.setOnClickListener(view -> finish());

        updateBtn.setOnClickListener(view -> {
            currentPassword = curr_password.getText().toString();
            newPassword = new_password.getText().toString();
            retypePassword = retype_password.getText().toString();
            if(TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(retypePassword))
            {
                Toast.makeText(ChangePassswordActivity.this,"All the fields are mandatory",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!(TextUtils.isEmpty(currentPassword) && TextUtils.isEmpty(newPassword) && TextUtils.isEmpty(retypePassword) )){
                if(newPassword.equals(retypePassword)) {
                    if (isValidPassword(newPassword)) {
                        user = mAuth.getCurrentUser();
                        credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), currentPassword);
                            // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(ChangePassswordActivity.this, "Password updated Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent mainIntent = new Intent(ChangePassswordActivity.this, ProfileActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                    } else {
                                        String message = task1.getException().getMessage();
                                        Toast.makeText(ChangePassswordActivity.this, "Error occurred. " + message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Snackbar.make(coordinatorLayout, "Authentication Failed", Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        Snackbar.make(coordinatorLayout,"Password should contain at least one Numeric, Special, Uppercase and Lowercase character.",Snackbar.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ChangePassswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
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
}