package com.arturofilio.androidordereats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arturofilio.androidordereats.Common.Common;
import com.arturofilio.androidordereats.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    private static final String TAG = "SignIn";

    EditText editPhone, editPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText) findViewById(R.id.editPassword);
        editPhone = (MaterialEditText) findViewById(R.id.editPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Loading");

                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if user exists in the database
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {

                            //Get User information
                            mDialog.dismiss();

                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);

                            if (user.getPassword().equals(editPassword.getText().toString())) {

                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();

                            } else {

                                Toast.makeText(SignIn.this, "Wrong password, please try again", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not found, check credentials", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
