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
import com.google.firebase.auth.AuthResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

   CircleImageView mcircleImageView;
   TextInputEditText mTextInputUsername;
   TextInputEditText mTextInputEmailR;
   TextInputEditText mTextInputPasswordR;
   TextInputEditText mTextInputConfirmPassword;
   Button mButtonRegister;
   //FirebaseAuth mAuth;
   //FirebaseFirestore mFirestore;
    AuthProviders mAuthProviders;
    UsersProviders mUserProviders;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mcircleImageView=findViewById(R.id.circleimageback);
        mTextInputUsername=findViewById(R.id.textInputUsername);
        mTextInputEmailR=findViewById(R.id.textInputEmailR);
        mTextInputPasswordR=findViewById(R.id.textinputPaswordR);
        mTextInputConfirmPassword=findViewById(R.id.textInputConfirPasswordR);
        mButtonRegister=findViewById(R.id.buttonRegister);

        //mAuth=FirebaseAuth.getInstance();
        mAuthProviders=new AuthProviders();
        //mFirestore=FirebaseFirestore.getInstance();
        mUserProviders=new UsersProviders();

        mDialog=new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false).build();


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mcircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register() {
        String username=mTextInputUsername.getText().toString();
        String email=mTextInputEmailR.getText().toString();
        String password=mTextInputPasswordR.getText().toString();
        String confirmpassword=mTextInputConfirmPassword.getText().toString();
        
        if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmpassword.isEmpty()){
            if(isEmailValid(email)){
                if (password.equals(confirmpassword)){
                      if(password.length()>=6){
                          createUser(email,password, username);
                      }else {
                          Toast.makeText(this, "la contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                      }
                }else{
                    Toast.makeText(this, "las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(this, "insertó todos los campos pero el email No es valido", Toast.LENGTH_SHORT).show();
            }
            
        }else {
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }
       
    }

    private void createUser(final String email,String password, final String username) {
        mDialog.show();
        mAuthProviders.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id=mAuthProviders.getUid();
                    User user=new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);
                    mUserProviders.create(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "El usuario se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(RegisterActivity.this,HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity.this, "no se pudo almacenar en la base de datos", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    Toast.makeText(RegisterActivity.this, "El usuario se registro correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    mDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}