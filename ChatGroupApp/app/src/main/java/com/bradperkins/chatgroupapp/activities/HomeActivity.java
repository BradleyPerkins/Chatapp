package com.bradperkins.chatgroupapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bradperkins.chatgroupapp.GroupObj;
import com.bradperkins.chatgroupapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private DatabaseReference mRef;

    private ArrayList<GroupObj> groupList;

    private GroupObj group;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        groupList = new ArrayList<>();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();


        firebaseData();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_group:
                //Get current time stamp
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String messageTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Date());

                mRef.child("groups").child(timeStamp).child("title").setValue("Group 2");


                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Pull User Drinks from Firebase
    private void firebaseData() {
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pullData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //Pull User Drinks from Firebase
    private void pullData(DataSnapshot dataSnapshot) {
        group = new GroupObj();
        for (final DataSnapshot drinksSnapshot : dataSnapshot.getChildren()) {
            group = drinksSnapshot.getValue(GroupObj.class);
            groupList.add(group);
        }

        Log.d("ZZZZ", groupList.size() + "Size");
    }


}
