package com.bradperkins.chatgroupapp.utilities;


import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

// PerkinsBradley_CE
public class FormUtils {

    //Email validator
    public static boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    //Validate Password
    public static boolean isValidPassword(final String password) {
        String str = password;
        int length = str.length();
        if (length>6){
            return true;
        } else{
            return false;
        }
    }

    //Password Match
    public static boolean passwordCheck(String pw1, String pw2) {
        return (pw1.equals(pw2));
    }

    public static boolean isValidUsername(String username) {

        String str = username;
        int length = str.length();
        if (length>6){
            return true;
        } else{
            return false;
        }
    }

    public static void closeKeyboard(EditText editText, Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
