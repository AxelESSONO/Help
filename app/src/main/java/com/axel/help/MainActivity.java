package com.axel.help;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    private Button btnSignIn, btnRegister;
    private RelativeLayout rootLayout;
    FirebaseAuth auth;
    DatabaseReference users;
    FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        //users = db.getReference("Users");

        btnRegister = (Button) findViewById(R.id.btn_register);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        rootLayout = (RelativeLayout) findViewById(R.id.rootlayout);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });
    }

    private void showRegisterDialog() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register,null);
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
                checkValidation(edtName, edtFirst, edtEmail, edtPassword, edtPhone, dialog);
            }
        });
    }

    private void checkValidation(MaterialEditText edtName, MaterialEditText edtFirst, MaterialEditText edtEmail, MaterialEditText edtPassword, MaterialEditText edtPhone, AlertDialog dialog) {

        if (TextUtils.isEmpty(edtName.getText().toString())){
            edtName.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Saisir votre nom.", Snackbar.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(edtFirst.getText().toString())){
            edtName.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Saisir votre prénom.", Snackbar.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(edtEmail.getText().toString())){
            edtName.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Saisir un email valide.", Snackbar.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(edtPassword.getText().toString())){
            edtName.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Choisir un mot de passe.", Snackbar.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(edtPhone.getText().toString())){
            edtName.setError("Champ obligatoire");
            Snackbar.make(rootLayout, "Saisir votre numero de téléphone.", Snackbar.LENGTH_SHORT).show();
        }else {
            auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                        }
                    });
        }
    }
}
