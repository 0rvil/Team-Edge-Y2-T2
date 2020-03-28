package com.project.fit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class startActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }

    public void login(View view) {
        EditText user_email = findViewById(R.id.user_email);
        EditText user_pass = findViewById(R.id.password_log_et);
        String Email = user_email.getText().toString();
        String Password = user_pass.getText().toString();
        if(TextUtils.isEmpty(Email)){
            user_email.setError("An Email Address Is Required.");
            return;
        } if (TextUtils.isEmpty(Password)){user_pass.setError("Password is required.");
            return;
        }
        if(Password.length()<6)
        {user_pass.setError("Password must be 6 characters long");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        fAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(startActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(startActivity.this,"Error ! "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    public void register(View view) {
        //Change these intents to later implement firebase login authentication
        Intent register = new Intent(this,registerActivity.class);
        startActivity(register);
        finish();
    }

}
