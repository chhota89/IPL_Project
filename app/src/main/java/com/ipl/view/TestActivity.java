package com.ipl.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ipl.R;

import java.util.Date;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = TestActivity.class.getSimpleName();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String mCustomToken= "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiJtb2hWQtdWlkIiwiaWF0IjoxNDk5MjU3OTY4LCJleHAiOjE0OTkyNjE1NjgsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHl0b29sa2l0Lmdvb2dsZWFwaXMuY29tL2dvb2dsZS5pZGVudGl0eS5pZGVudGl0eXRvb2xraXQudjEuSWRlbnRpdHlUb29sa2l0IiwiaXNzIjoiZmlyZWJhc2UtYWRtaW5zZGstbGdqMnhAcHJvamVjdC02MDc4NjcxMjY1NzU2MjExNjgzLmlhbS5nc2VydmljZWFjY291bnQuY29tIiwic3ViIjoiZmlyZWJhc2UtYWRtaW5zZGstbGdqMnhAcHJvamVjdC02MDc4NjcxMjY1NzU2MjExNjgzLmlhbS5nc2VydmljZWFjY291bnQuY29tIn0.W86JWojQHF54IOOTjtNDpe5qH_dqCvDQTkRJIJDAXkScjLlkyuWH_vox5C00Ev5BKk-aHM7IcwLor0-dZnC6uK66Yz7tvn64PiB21g9axamPueVD2k-vbFG6IutpU99El8Zfm1W4Yj_5GpNt7qrX2qCTm_-5Ld8I1y30jcfJoPPLks03llfVP3h8HhB7nxHErrcmjoGe_haII2hMcKTN8KkAuBm-_E7x6FqdcxQj_laYrvZM4NhLSYj2rAQL21FfTWX8GurTFoWpRelfA-PEYNXwC10oyNApiijAsDSP-KOvSkRwuqbVZaxMZYuTEkH7ittx0JChAlFTRDkKBSfJMw";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                FirebaseUser user;
                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);


                // ...
            }
        };


        //writeMessageOnFirebase();

        /*FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser ==null){
            Log.d(TAG, "onCreate: user is null");
            signInwithCustomToken();
        }else{
            writeMessageOnFirebase();
            Log.d(TAG, "onCreate: User is not null");
        }*/

        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(3)
                //.session(7)
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {

                    }
                }).build();

        ratingDialog.show();
    }

    private void signInwithCustomToken() {
        mAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            writeMessageOnFirebase();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(TestActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void writeMessageOnFirebase(){
        String date = new Date().toString();
        Log.d(TAG, "writeMessageOnFirebase: "+date);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Testing");
        myRef.push().setValue(date);
    }
}
