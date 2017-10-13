package com.example.paras.transportmanagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity
{


    protected EditText passwordView ,userNameView;
    private Button loginButton;
    private CheckBox rememberMe ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        rememberMe = (CheckBox) findViewById(R.id.checkBox_login);

        passwordView.setOnEditorActionListener(new HandleImeGO(this));
        loginButton.setOnClickListener(new LoginButtonPressed(this));

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        if (sp1.contains("userName"))
        {
            userNameView.setText(sp1.getString("userName", ""));
            passwordView.setText(sp1.getString("password", ""));
        }

    }

    protected void login()
    {
        String userName = userNameView.getText().toString();
        String password = passwordView.getText().toString();

        if (rememberMe.isChecked())
        {
            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString("userName",userName );
            Ed.putString("password",password);
            Ed.commit();
        }
        else
            {
                SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.remove("userName");
                Ed.remove("password");
                Ed.commit();
            }

        if (userName.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "user = vivek \n pass = 12345", Toast.LENGTH_SHORT).show();
        }
        else if (userName.equalsIgnoreCase("vivek") && password.equalsIgnoreCase("12345"))
        {
            Intent launchWelcomeUser = new Intent(this , WelcomeUser.class);
            startActivity(launchWelcomeUser);
        }
        else Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();

    }

}

class HandleImeGO implements TextView.OnEditorActionListener
{   private LoginActivity loginActivityContext;

    public HandleImeGO(LoginActivity loginActivityContext)
    {
        this.loginActivityContext = loginActivityContext;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        boolean handled = false;

        if (textView.getId()==R.id.password)
        {
            if (actionId == EditorInfo.IME_ACTION_GO)
            {
                handled = true;
                loginActivityContext.login();
            }
        }
        else if (textView.getId()==R.id.username)
        {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                // intent to next activity
                loginActivityContext.passwordView.requestFocus();
                handled = true;
            }
        }


        return handled;
    }
}


class LoginButtonPressed implements View.OnClickListener
{
    public LoginButtonPressed(LoginActivity loginActivityContext)
    {
        this.loginActivityContext = loginActivityContext;
    }

    LoginActivity loginActivityContext;

    @Override
    public void onClick(View view)
    {
        loginActivityContext.login();
    }


}