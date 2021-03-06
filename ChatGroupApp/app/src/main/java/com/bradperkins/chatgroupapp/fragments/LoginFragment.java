package com.bradperkins.chatgroupapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bradperkins.chatgroupapp.R;
import com.bradperkins.chatgroupapp.utilities.FormUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText logEmailET;
    private EditText logPassET;

    private LoginListener mListener;

    public LoginFragment() {}

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView regBtn = getView().findViewById(R.id.reg_btn);
        Button loginBtn = getView().findViewById(R.id.login_btn);
        TextView forgotBtn = getView().findViewById(R.id.forgot_btn);

        logEmailET = getView().findViewById(R.id.login_email_et);
        logPassET = getView().findViewById(R.id.login_pass_et);


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.register();
            }
        });
        logPassET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String emailStr = logEmailET.getText().toString().trim();
                    String passStr = logPassET.getText().toString().trim();

                    if (FormUtils.isValidEmail(emailStr)){
                        //Login listener
                        mListener.login(emailStr, passStr);

                    } else{
                        logEmailET.setError("Enter A Valid Email Address");
                    }
                    handled = true;
                }
                return handled;
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = logEmailET.getText().toString().trim();
                String passStr = logPassET.getText().toString().trim();
                if (!emailStr.equals("")) {
                    if (!passStr.equals("")) {
                        if (FormUtils.isValidEmail(emailStr)) {
                            //Login listener
                            mListener.login(emailStr, passStr);
                            FormUtils.closeKeyboard(logPassET, getActivity());
                        } else {
                            logEmailET.setError("Enter A Valid Email Address");
                        }
                    }else {
                        logPassET.setError("Enter Your Password");
                    }
                } else{
                    logEmailET.setError("Enter Your Email Address");
                }
            }
        });
        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.forgot();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener){
            mListener = (LoginListener) context;
        }
    }

    public interface LoginListener {
        void login(String email, String password);
        void register();
        void forgot();
    }

}
