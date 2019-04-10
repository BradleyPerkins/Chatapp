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
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener{

    private FirebaseAuth mAuth;
    private ProgressBar progSpin;
    private DatabaseReference mRef;

    Context context;

    private boolean hasNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progSpin = findViewById(R.id.login_prog_spin);
        progSpin.setVisibility(View.INVISIBLE);

        //Load saved user data is exists
        SharedPreferences sharedPref = getSharedPreferences("USER_DATA" ,this.MODE_PRIVATE);
        String userEmail = sharedPref.getString("USER_EMAIL", "");
        String userPass = sharedPref.getString("USER_PASSWORD", "");

        hasNetwork = UtilsHelper.hasNetwork(this);

        if (!hasNetwork){
            if (userEmail != null && userPass != null){
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
            }
            Toast.makeText(context, "Check Your Network Connection", Toast.LENGTH_SHORT).show();
        }

        mAuth = FirebaseAuth.getInstance();
        getFragmentManager().beginTransaction().replace(R.id.login_placeholder, LoginFragment.newInstance()).commit();

        if (!userEmail.isEmpty() && !userPass.isEmpty()){
            login(userEmail, userPass);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }


    @Override
    public void login(String email, String password) {
        progSpin.setVisibility(View.VISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    getDrinks(titles);
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    progSpin.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void register() {

    }

    @Override
    public void forgot() {

    }
}
