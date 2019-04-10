package com.bradperkins.chatgroupapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements RegisterFragment.RegListener{

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseDatabase database;
    private ProgressBar progSpin;

    private boolean alreadyExists = false;
    private boolean hasNetwork = false;
    private static ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progSpin = findViewById(R.id.reg_prog_spin);
        progSpin.setVisibility(View.INVISIBLE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        hasNetwork = UtilsHelper.hasNetwork(this);
        if (hasNetwork){
            getFragmentManager().beginTransaction().replace(R.id.reg_placeholder, RegisterFragment.newInstance()).commit();
        }else{
            Toast.makeText(this, "Please Connect to your Network", Toast.LENGTH_SHORT).show();
        }

        list = DataHelper.childList(this);
    }

    @Override
    public void register(final String email, final String pass, final String username) {
        progSpin.setVisibility(View.VISIBLE);
        final String userID = mAuth.getUid();
        for (int i=0; i<list.size(); i++){
            if (!username.equals(list.get(i))){
                alreadyExists = false;
            }else {
                Toast.makeText(this, "Username Already Exist", Toast.LENGTH_SHORT).show();
                alreadyExists = true;
                i = list.size();
                progSpin.setVisibility(View.INVISIBLE);
            }
        }

        if (hasNetwork) {
            if (!alreadyExists) {
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(RegisterActivity.this, "Registration Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username).build();

                                    user.updateProfile(profileUpdates);

                                    String regName = profileUpdates.getDisplayName();

                                    SharedPreferences sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("USER_NAME", regName);
                                    editor.putString("USER_EMAIL", email);
                                    editor.putString("USER_PASSWORD", pass);
                                    editor.putString("USER_ID", userID);
                                    editor.commit();

                                    mRef.child("users").child(username + timeStamp).setValue(username);
//                                    mRef.child("users").child(userID).setValue(username);

                                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                                    progSpin.setVisibility(View.INVISIBLE);

                                } else {
                                    progSpin.setVisibility(View.INVISIBLE);
                                    Toast.makeText(RegisterActivity.this, "Login or use a differnet email to register.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }else {
            Toast.makeText(this, "Connect to your Network", Toast.LENGTH_SHORT).show();
        }
    }
}
