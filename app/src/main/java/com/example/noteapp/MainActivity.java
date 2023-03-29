package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private boolean otpSent = false;
    private String verification_id ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mobileNo = findViewById(R.id.mobileno);
        final PinView pinViewotp = findViewById(R.id.pinview);
        final FloatingActionButton submitBtn = findViewById(R.id.otpbutton);

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpSent){
                    final String getOtp = pinViewotp.getText().toString();

                    if (verification_id.isEmpty()){
                        Toast.makeText(MainActivity.this, "Unable to Verifiy", Toast.LENGTH_SHORT).show();
                    }else {

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id,getOtp);

                        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                    FirebaseUser user = task.getResult().getUser();

                                    Toast.makeText(MainActivity.this, "Verified", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(MainActivity.this,NoteMenu.class);
                                    startActivity(i);

                                }else {
                                    Toast.makeText(MainActivity.this, "Something went Wrong !!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                }else {
                    final String getMobileno = mobileNo.getText().toString();

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber("+91"+getMobileno)
                            .setTimeout(60L,TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    Toast.makeText(MainActivity.this, "Otp sent Successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(MainActivity.this, "Something went Wrong"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);

                                  //  pinViewotp.setVisibility(View.VISIBLE);

                                    verification_id = s;
                                    otpSent = true;
                                }
                            }).build();

                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });
    }

}