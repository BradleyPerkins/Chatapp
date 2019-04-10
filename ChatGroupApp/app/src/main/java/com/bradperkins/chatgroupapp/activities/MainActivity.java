package com.bradperkins.chatgroupapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bradperkins.chatgroupapp.fragments.LoginFragment;
import com.bradperkins.chatgroupapp.R;
import com.bradperkins.chatgroupapp.utilities.DataHelper;
import com.bradperkins.chatgroupapp.utilities.FormUtils;
import com.bradperkins.chatgroupapp.utilities.UtilsHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener {

    private FirebaseAuth mAuth;
    private ProgressBar progSpin;
    private DatabaseReference mRef;

    Context context;

    private boolean hasNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        context = this;

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

        if (!userEmail.isEmpty() && !userPass.isEmpty()){
            login(userEmail, userPass);
        }

        getFragmentManager().beginTransaction().replace(R.id.login_placeholder, LoginFragment.newInstance()).commit();
    }

    @Override
    public void login(final String email, final String password) {
        progSpin.setVisibility(View.VISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    SharedPreferences sharedPref = getSharedPreferences("USER_DATA" , Context.MODE_PRIVATE);
                    DataHelper.saveUserData(sharedPref, currentUser.getDisplayName(), email, password);

                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progSpin.setVisibility(View.INVISIBLE);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Login Failed");
                    builder.setMessage("Wrong Username or password. " +
                            "Try again or tap Forgot password to reset it. " +
                            "Or Tap on Register to create a new Account");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    progSpin.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    @Override
    public void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void forgot() {
        final EditText forgotEmailET;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Email to Reset Password");
        forgotEmailET = new EditText(this);
        builder.setView(forgotEmailET);
        builder.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = forgotEmailET.getText().toString().trim();
                boolean validEmail = FormUtils.isValidEmail(email);
                if (validEmail){
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Check Email to Reset", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(context, "Enter a valid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog forgotDialog = builder.create();
        forgotDialog.show();
    }
}
