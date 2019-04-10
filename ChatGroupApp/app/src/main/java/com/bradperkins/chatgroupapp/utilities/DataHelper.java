package com.bradperkins.chatgroupapp.utilities;

// Date 4/10/19
// 
// Bradley Perkins

// AID - 1809

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// PerkinsBradley_CE
public class DataHelper {

    private static ArrayList<String> list;

    public static ArrayList<String> childList(final Context context){
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mFirebaseDatabase.getReference();
        list = new ArrayList<>();
        mRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String pos = snapshot.getKey();
                    list.add((String) snapshot.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return list;
    }

    public static void saveUserData(SharedPreferences sharedPref, String username, String email, String password){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("USER_NAME", username);
        editor.putString("USER_EMAIL", email);
        editor.putString("USER_PASSWORD", password);
        editor.commit();
    }

}
