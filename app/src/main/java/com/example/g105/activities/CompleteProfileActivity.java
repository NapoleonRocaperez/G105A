package com.example.g105.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.g105.R;
import com.example.g105.models.User;
import com.example.g105.providers.AuthProviders;
import com.example.g105.providers.UsersProviders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextImputUsername;
    Button mButtonRegisterC;
    //FirebaseAuth mAuth;
    //FirebaseFirestore mFireStore;
    AuthProviders mAuthProviders;
    UsersProviders mUserProviders;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mTextImputUsername=findViewById(R.id.textInputUsernameC);
        mButtonRegisterC=findViewById(R.id.buttonRegisterC);

        //mAuth=FirebaseAuth.getInstance();
        mAuthProviders=new AuthProviders();
        //mFirestore=FirebaseFirestore.getInstance();
        mUserProviders=new UsersProviders();

        mDialog=new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false).build();

        mButtonRegisterC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        String username=mTextImputUsername.getText().toString();

        if(!username.isEmpty()){
               updaterUser(username);
        }else{
            Toast.makeText(this, "para continuar completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void updaterUser( String username) {
        String id=mAuthProviders.getUid();
        User user=new User();
        user.setUsername(username);
        user.setId(id);
        mDialog.show();
        mUserProviders.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if(task.isSuccessful()){
                    Intent intent=new Intent(CompleteProfileActivity.this,HomeActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(CompleteProfileActivity.this, "No se almaceno el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}