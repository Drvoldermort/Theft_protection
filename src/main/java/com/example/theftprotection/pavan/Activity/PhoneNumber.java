package com.example.pavan.theftprotection.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pavan.theftprotection.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumber extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    public String TAG = "PhoneNumber";
    Button btnsignout;
    FirebaseAuth mAuth;
    String codeSent;

    Button GetVerf,SubmitVerf;
    EditText PhoneNum,OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        btnsignout = findViewById(R.id.signOut);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGoogleSignInClient.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Toast.makeText(PhoneNumber.this,"SignedOut",Toast.LENGTH_SHORT).show();

            }
        });

        mAuth = FirebaseAuth.getInstance();

        GetVerf = findViewById(R.id.btnVerify);
        SubmitVerf = findViewById(R.id.btnauthPhone);
        PhoneNum = findViewById(R.id.editTextPhone);
        OTP= findViewById(R.id.editTextPhoneVerify);
        GetVerf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTP.setVisibility(View.VISIBLE);
                SubmitVerf.setVisibility(View.VISIBLE);
                sendVerificationCode();

            }
        });
        SubmitVerf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                verifySignInCode();
            }
        });

    }

    private void verifySignInCode() {
        String code = OTP.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,code);
        signInWithPhoneAuthCredential(credential);
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        finish();
    }

    private void sendVerificationCode() {
        String phone = PhoneNum.getText().toString();
        if(phone.isEmpty())
        {
            Toast.makeText(this,"Phone Number Required",Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.length()<10)
        {
            Toast.makeText(this,"Phone Number Invalid",Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,
                mCallbacks);

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PhoneNumber.this,"Phone Number Authenticated",Toast.LENGTH_SHORT).show();

                            FirebaseUser phoneuser = task.getResult().getUser();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(PhoneNumber.this,"Phone Number Not Authenticated",Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
