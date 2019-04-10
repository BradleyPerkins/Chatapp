package com.bradperkins.chatgroupapp;


import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class RegisterFragment extends Fragment {

    private RegListener mListener;

    private EditText regEmail;
    private EditText regPass1;
    private EditText regPass2;
    private EditText regUsername;

    private static ArrayList<String> list;

    private boolean alreadyExists = false;

    public RegisterFragment() { }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = DataHelper.childList(getContext());

        regEmail = getView().findViewById(R.id.reg_email_et);
        regUsername = getView().findViewById(R.id.reg_username_et);
        regPass1 = getView().findViewById(R.id.reg_pass1_et);
        regPass2 = getView().findViewById(R.id.reg_pass2_et);

        Button regBtn = getView().findViewById(R.id.reg_account_btn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = regUsername.getText().toString().trim();
                String email = regEmail.getText().toString().trim();
                String pw1 = regPass1.getText().toString().trim();
                String pw2 = regPass2.getText().toString().trim();

                for (int i=0; i<list.size(); i++){
                    if (!username.equals(list.get(i))){
                        alreadyExists = false;
                    }else {
                        alreadyExists = true;
                        regUsername.setError("Username Already Taken");
                        i = list.size();
                    }
                }

                if (!alreadyExists) {
                    if (FormUtils.isValidEmail(email)) {
                        if (FormUtils.isValidUsername(username)) {
                            if (FormUtils.isValidPassword(pw1)) {
                                if (FormUtils.passwordCheck(pw1, pw2)) {
                                    mListener.register(email, pw1, username);
//                                    FormUtils.closeKeyboard(regPass2, getActivity());
                                } else {
                                    regPass2.setError("Password must match");
                                }
                            } else {
                                regPass1.setError("Password needs to be at least 7 characters");
                            }
                        } else {
                            regUsername.setError("Username needs to be at least 7 characters");
                        }
                    } else {
                        regEmail.setError("Enter A Valid Email Address");
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegListener){
            mListener = (RegListener) context;
        }
    }

    public interface RegListener {
        void register(String email, String pass, String username);
    }

}
