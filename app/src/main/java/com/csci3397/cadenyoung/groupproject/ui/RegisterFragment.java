package com.csci3397.cadenyoung.groupproject.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.csci3397.cadenyoung.groupproject.HomeMainActivity;
import com.csci3397.cadenyoung.groupproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {

    Button registerButton;
    EditText newEmailText;
    EditText newPasswordText;
    EditText firstNameText;
    EditText lastNameText;
    FirebaseAuth firebaseAuth;

//TODO add checks for empty text fields
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

    private void createNewUser() {
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
    }

    private void createAccount(String email, String password){
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                            createNewUser();
                            moveToHomepage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void moveToHomepage() {
        Intent intent = new Intent(this.getActivity(), HomeMainActivity.class);
        //intent.putExtra("name",name);
        startActivity(intent);
    }

    private boolean validateForm() {
        boolean valid = true;

        String firstName = firstNameText.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            firstNameText.setError("Required");
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        String lastName = lastNameText.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            lastNameText.setError("Required");
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        String email = newEmailText.getText().toString();
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
            newPasswordText.setError(null);
        }

        return valid;
    }
}