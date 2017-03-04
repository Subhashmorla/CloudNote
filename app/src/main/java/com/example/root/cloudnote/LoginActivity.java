package com.example.root.cloudnote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailwrapper;
    private TextInputLayout passwordwrapper;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private Button mRegisterButton;
    private FirebaseAuth mAuth;
    private ProgressDialog processdialog;
    private String TAG=LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailwrapper= (TextInputLayout) findViewById(R.id.textinput_email);
        passwordwrapper= (TextInputLayout) findViewById(R.id.textinput_password);
        emailwrapper.setHint("Email");
        passwordwrapper.setHint("Password");

        mEmail= (EditText) findViewById(R.id.email_edit_text);
        mPassword= (EditText) findViewById(R.id.password_edit_text);
        mLoginButton= (Button) findViewById(R.id.login_button);
        mRegisterButton= (Button) findViewById(R.id.register_button);

        mAuth=FirebaseAuth.getInstance();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });




    }

    private void startSignIn() {
        String emailText=mEmail.getText().toString();
        String passwordText=mPassword.getText().toString();
        processdialog=new ProgressDialog(this);
        processdialog.setMessage("Logging In...");
        processdialog.show();

        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        processdialog.dismiss();
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Login Successful",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });

    }
}
