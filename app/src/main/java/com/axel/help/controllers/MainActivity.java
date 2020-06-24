package com.axel.help.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.axel.help.R;
import com.axel.help.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class MainActivity extends AppCompatActivity {

    private Button btnSignIn, btnRegister;
    private RelativeLayout rootLayout;
    FirebaseAuth auth;
    DatabaseReference users;
    FirebaseDatabase db;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Arkhip-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        setContentView(R.layout.activity_main);

        // init Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnRegister = (Button) findViewById(R.id.btn_register);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        rootLayout = (RelativeLayout) findViewById(R.id.rootlayout);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

    }

    private void showLoginDialog() {

        final AlertDialog.Builder mBuilderLogin = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);
        mBuilderLogin.setView(login_layout);
        final AlertDialog dialogLogin = mBuilderLogin.create();
        dialogLogin.show();

        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmailLogin);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPasswordLogin);

        final Button loginDialog = login_layout.findViewById(R.id.btn_login_dialog);
        Button LoginCancelDialog = login_layout.findViewById(R.id.btn_cancel_login_dialog);

        LoginCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogin.dismiss();
            }
        });

        loginDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoginDataValidation(edtEmail, edtPassword, dialogLogin, loginDialog);
            }
        });

    }

    private void checkLoginDataValidation(final MaterialEditText edtEmail, final MaterialEditText edtPassword, AlertDialog dialog, Button loginDialog) {

        dialog.dismiss();

        loginDialog.setEnabled(false);

        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Saisir un email valide.", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            Snackbar.make(rootLayout, "Choisir un mot de passe.", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (edtPassword.getText().toString().length() < 8) {
            Snackbar.make(rootLayout, "Mot de passe trop court.", Snackbar.LENGTH_SHORT).show();
            return;

        } else {

            loginUser(edtEmail.getText().toString(), edtPassword.getText().toString(), loginDialog);
        }

    }

    private void loginUser(String emailLogin, String passwordLogin, final Button loginDialog) {

        final SpotsDialog waitinDialog = new SpotsDialog(this);
        waitinDialog.show();

        auth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        waitinDialog.dismiss();
                        startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                waitinDialog.dismiss();
                Snackbar.make(rootLayout, "Erreur " + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                loginDialog.setEnabled(true);
            }
        });
    }

    private void showRegisterDialog() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);
        mBuilder.setView(register_layout);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtFirst = register_layout.findViewById(R.id.edtFirstName);
        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);
        Button registerDialog = register_layout.findViewById(R.id.btn_register_dialog);
        Button cancelDialog = register_layout.findViewById(R.id.btn_cancel_dialog);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        registerDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegisterDataValidation(edtName, edtFirst, edtEmail, edtPassword, edtPhone, dialog);
            }
        });
    }

    private void checkRegisterDataValidation(final MaterialEditText edtName, final MaterialEditText edtFirst, final MaterialEditText edtEmail, final MaterialEditText edtPassword, final MaterialEditText edtPhone, AlertDialog dialog) {

        if (TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Saisir votre nom.", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtFirst.getText().toString())) {
            edtFirst.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Saisir votre prénom.", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Saisir un email valide.", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            edtPassword.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Choisir un mot de passe.", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtPhone.getText().toString())) {
            edtPhone.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Saisir votre numero de téléphone.", Snackbar.LENGTH_SHORT).show();
            return;
        } else {

            registerUser(edtName, edtFirst, edtEmail, edtPassword, edtPhone);
        }
    }

    private void registerUser(final MaterialEditText edtName, final MaterialEditText edtFirst, final MaterialEditText edtEmail, final MaterialEditText edtPassword, final MaterialEditText edtPhone) {

        auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User userObject = new User();
                        userObject.setName(edtName.getText().toString());
                        userObject.setFirstName(edtFirst.getText().toString());
                        userObject.setEmail(edtEmail.getText().toString());
                        userObject.setPassword(edtPassword.getText().toString());
                        userObject.setPhone(edtPhone.getText().toString());

                        // Use email to key
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(userObject)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(rootLayout, "Votre compte a été créé avec succès", Snackbar.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout, "Erreur " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(rootLayout, "Erreur " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
