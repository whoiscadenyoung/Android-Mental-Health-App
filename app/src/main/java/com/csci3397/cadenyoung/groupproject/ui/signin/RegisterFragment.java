package com.csci3397.cadenyoung.groupproject.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.csci3397.cadenyoung.groupproject.HomeMainActivity;
import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Stats;
import com.csci3397.cadenyoung.groupproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {

    private Button registerButton;
    private EditText newEmailText;
    private EditText newPasswordText;
    private EditText firstNameText;
    private EditText lastNameText;
    private FirebaseAuth firebaseAuth;
    private String firstName;
    private String lastName;
    private String email;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        registerButton = view.findViewById(R.id.registerButton);
        newEmailText = view.findViewById(R.id.newEmailText);
        newPasswordText = view.findViewById(R.id.newPasswordText);
        firstNameText = view.findViewById(R.id.firstNameText);
        lastNameText = view.findViewById(R.id.lastNameText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }else {
                    String email = newEmailText.getText().toString();
                    String password = newPasswordText.getText().toString();
                    createAccount(email, password);
                }
            }
        });
        return view;
    }


    private void createAccount(String email, String password){
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) return;

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            moveToHomepage();
                            addToDB();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                newEmailText.setError("The email address is already in use by another account");
                            }
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                newEmailText.setError("The email address is badly formatted");
                            }
                        }
                    }
                });
    }

    private void moveToHomepage() {
        Intent intent = new Intent(this.getActivity(), HomeMainActivity.class);
        startActivity(intent);
    }

    private boolean validateForm() {
        boolean valid = true;

        firstName = firstNameText.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            firstNameText.setError("Required");
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        lastName = lastNameText.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            lastNameText.setError("Required");
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        email = newEmailText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            newEmailText.setError("Required");
            valid = false;
        } else {
            newEmailText.setError(null);
        }

        String password = newPasswordText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            newPasswordText.setError("Required");
            valid = false;
        } else {
            if(password.length() < 6) {
                newPasswordText.setError("Password must contain at least 6 characters");
                valid = false;
            }else {
                newPasswordText.setError(null);
            }
        }
        return valid;
    }

    private void addToDB() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("users");
        User user = new User(firstName + " " + lastName, email, firebaseAuth.getUid(), "never");

        String userID = firebaseAuth.getUid();
        assert userID != null;
        myRef.child(userID).setValue(user);

        Stats stats = new Stats(50);
        db.getReference("stats").child(userID).setValue(stats);
    }

}