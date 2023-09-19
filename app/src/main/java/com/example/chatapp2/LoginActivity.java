package com.example.chatapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText inputEmail,inputPassword;
    TextView btnlogin,forgotPassword,createNewAccount;
    ProgressDialog mLoadingBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail=(EditText) findViewById(R.id.inputEmail);
        inputPassword=(EditText) findViewById(R.id.inputPassword);
        btnlogin=(TextView) findViewById(R.id.btnlogin);
        forgotPassword=(TextView) findViewById(R.id.forgotpassword);
        createNewAccount=(TextView) findViewById(R.id.createnewaccount);

        mLoadingBar=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptLogin();
            }
        });
    }

    private void AttemptLogin() {

        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();

        if(email.isEmpty()||!email.contains("@gmail")){
            showError(inputEmail,"Email is not valid");
        }else if(password.isEmpty()||password.length()<5){
            showError(inputPassword,"Password must be grater than 5 letter");
        }else {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait,while your credential ");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mLoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,SetupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        mLoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void showError(EditText field, String text) {
        field.setError(text);
        field.requestFocus();
    }
}