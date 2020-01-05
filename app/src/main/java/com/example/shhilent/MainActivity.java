package com.example.shhilent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btn;
    private EditText emailText;
    private EditText passwordText;

    private String deviceToken = "";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.Login);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.input_password);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                deviceToken = instanceIdResult.getToken();
                // Do whatever you want with your token now
                // i.e. store it on SharedPreferences or DB
                // or directly send it to server
            }
        });

        // emu
        // c8yQneuYG-U:APA91bF3u0g-j6gk7KBDdriHTj2vxl5tkPCXgkSoZty8KBSSyoWCIZTx64Pl4NEb9riUnCBHL3vG9an_ujIX6GEeqkegXO1XDXHxKV59krQuMcpyqOFzCkj-3foM-qOiduBWwUHKAhaB
        // s10
        // emI_sDuZFmo:APA91bGlbcQ9VKojxDVO75YHY15fEdd9A9b9jHpNj8CTzb9HDY0Hxym3vFTV5n7Xte3_IFocWC8YvrV-Zv6uwvR8Ojs-rjNxviFLGfdtyZLBZ9fJ5OQ64tg8v7MGuktME2a2oCDfb2LD
        // jasper
        // fnmAxuPclGk:APA91bEz5lBa7HYW1CV_k8dIhSczX6aIQtMpZH_sF-a_rNpIAydw2dTgMlGqRjL27-jmbK3mUWLht8S6KszWMFg2YI6dZa1hOEWDhWYAx2lf2tNsll2fU-cQ2sMsLkreTtZ7E_H2qCd1
        // azie
        // d41i5PG1ALM:APA91bEbI8QFrvbbzu8gtOnh86puzZQYsqcy4ydQUQqoms7SZwSzx1RslspcnCU2BWVeOzkuwDbGozBvX1Vbsu4TX0DCRiCpiD4MT23jAC1cWAZuGQA_5RaPXnfI3GkVIWJM8YB_mNCP

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, meldingen.class));
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                signIn(email, password);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        if (getIntent().getExtras() != null) {
            insertNotification();
        }
    }

    private void insertNotification(){
        String title = "";
        String body = "";
        String date = "";
        String time = "";
        DbHandler dbHandler = new DbHandler(MainActivity.this);
        for (String key : getIntent().getExtras().keySet()) {
            Object value = getIntent().getExtras().get(key);
            switch (key) {
                case "title":
                    title = value.toString();
                    break;
                case "body":
                    body = value.toString();
                    break;
                case "date":
                    date = value.toString();
                    break;
                case "time":
                    time = value.toString();
                    break;
                default:
                    Log.d("EXTRA", "Key: " + key + " Value: " + value);
            }
        }

        if(title.length() > 0 && body.length() > 0 && date.length() > 0 && time.length() > 0) {
            dbHandler.insertMelding(title,body,date,time);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            startActivity(new Intent(MainActivity.this, meldingen.class));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        Log.d(TAG, "token: " + deviceToken);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //firestore data push
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map<String, Object> userDB = new HashMap<>();
                            userDB.put("messaging_key", deviceToken);

// Add a new document with a generated ID
                            db.collection("users").document(user.getUid())
                                    .update(userDB)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });

                            startActivity(new Intent(MainActivity.this, meldingen.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "email of wachtwoord is fout.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            mStatusTextView.setText(R.string.auth_failed);
//                        }
//                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailText.setError("Required.");
            valid = false;
        } else {
            emailText.setError(null);
        }

        String password = passwordText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordText.setError("Required.");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

}
