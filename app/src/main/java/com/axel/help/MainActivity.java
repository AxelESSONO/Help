package com.axel.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    private Button btnSignIn, btnRegister;
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

        MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        MaterialEditText edtFirst = register_layout.findViewById(R.id.edtFirstName);
        MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);
        Button registerDialog = register_layout.findViewById(R.id.btn_register_dialog);
        Button cancelDialog = register_layout.findViewById(R.id.btn_cancel_dialog);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
