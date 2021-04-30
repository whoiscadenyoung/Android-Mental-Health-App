package com.csci3397.cadenyoung.groupproject.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.csci3397.cadenyoung.groupproject.database.UserHelperClass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {

    SignInButton signInButton;
    Button loginButton;
    Button registerPageButton;
    EditText emailText;
    EditText passwordText;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    String email;
    String password;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseDatabase db;
    DatabaseReference myRef;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        signInButton = view.findViewById(R.id.signInButton);
        loginButton = view.findViewById(R.id.loginButton);
        registerPageButton = view.findViewById(R.id.registerPageButton);
        emailText = view.findViewById(R.id.emailText);
        passwordText = view.findViewById(R.id.passwordText);

        signInButton.setSize(SignInButton.SIZE_STANDARD);
        //loginButton.setSize(SignInButton.SIZE_STANDARD);
        //registerPageButton.setSize(SignInButton.SIZE_STANDARD);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
                signIn(email, password);
            }

        });

        registerPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToRegisterPage();
            }
        });

        //Initialize sign in options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("766404629826-f0k3quh22862hjh376rdmkdia9m9obpe.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                //Start activity for result
                startActivityForResult(intent, 100);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        //Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //Check condition
        if (firebaseUser != null) {
            //When user is already signed in
            //Redirect to homepage
            moveToHomepage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if(requestCode == 100) {
            //When request code is equal to 100
            //Initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            //Check condition
            if(signInAccountTask.isSuccessful()) {
                //When google sign in successful
                //Initialize string
                String s = "Google sign in successful";
                //Display toast
                Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    //Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    //Check condition
                    if(googleSignInAccount != null) {
                        //When sign in account is not equal to null
                        //Initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken(),
                                        null);
                        //Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //Check condition
                                        if(task.isSuccessful()) {
                                            //When task is successful
                                            //Redirect to homepage
                                            addUserToDB();
                                            moveToHomepage();
                                            Toast.makeText(getActivity(), "sign in successful" , Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "sign in UNsuccessful" , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addUserToDB() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("users");
        if(firebaseAuth.getUid() == null) Log.d("userID", "null");
        String name = firebaseAuth.getCurrentUser().getDisplayName();
        String email = firebaseAuth.getCurrentUser().getEmail();
        String userID = firebaseAuth.getUid();

        myRef.child("users").child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    UserHelperClass user = new UserHelperClass(name, email, userID);
                    myRef.child(userID).setValue(user);
                    Log.d("registered", "into database");
                }
            }
        });
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            moveToHomepage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed. Incorrect email or password.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            //checkForMultiFactorFailure(task.getException());
                        }

                        /*if (!task.isSuccessful()) {
                            mBinding.status.setText(R.string.auth_failed);
                        }*/
                    }
                });
    }


    private void moveToHomepage() {
        Intent intent = new Intent(this.getActivity(), HomeMainActivity.class);
        startActivity(intent);
    }

    private void moveToRegisterPage() {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new RegisterFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailText.setError("Required");
            valid = false;
        } else {
            emailText.setError(null);
        }

        String password = passwordText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordText.setError("Required");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }
}