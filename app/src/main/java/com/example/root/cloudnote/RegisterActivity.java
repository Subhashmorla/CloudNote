package com.example.root.cloudnote;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout emailwrapper;
    private TextInputLayout passwordwrapper;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegisterButton;
    private FirebaseAuth mAuth;
    private ProgressDialog processdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailwrapper= (TextInputLayout) findViewById(R.id.textinput_register_email);
        passwordwrapper= (TextInputLayout) findViewById(R.id.textinput_register_password);
        emailwrapper.setHint("Email");
        passwordwrapper.setHint("Password");

        mEmail= (EditText) findViewById(R.id.email_register_edit_text);
        mPassword= (EditText) findViewById(R.id.password_register_edit_text);
        mRegisterButton= (Button) findViewById(R.id.register_activity_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    private void startRegister() {
        String email=mEmail.getText().toString();
        String password=mPassword.getText().toString();
        processdialog=new ProgressDialog(this);
        processdialog.setMessage("Registering...");
        processdialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {
                processdialog.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Register failed",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Registration Successful",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });


    }
}

