package com.example.root.cloudnote;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputLayout emailwrappper;
    private EditText mEmail;
    private Button mResetButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailwrappper= (TextInputLayout) findViewById(R.id.textinput_email_forgot);
        emailwrappper.setHint("Email");
        mEmail= (EditText) findViewById(R.id.email_edit_text_forgot);
        mResetButton= (Button) findViewById(R.id.reset_button);

        mAuth=FirebaseAuth.getInstance();

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    private void reset() {
        final String email=mEmail.getText().toString();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Something wrong",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this,"Password reset link sent to Email "+email,Toast.LENGTH_SHORT).show();

                }
                finish();

            }
        });


    }
}
