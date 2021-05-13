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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.csci3397.cadenyoung.groupproject.AlertDialogFragment;
import com.csci3397.cadenyoung.groupproject.HomeMainActivity;
import com.csci3397.cadenyoung.groupproject.MainActivity;
import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Stats;
import com.csci3397.cadenyoung.groupproject.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {

    private SignInButton signInButton;
    private Button loginButton;
    private Button registerPageButton;
    private EditText emailText;
    private EditText passwordText;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private String email;String password;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private AlertDialogFragment dialog;
    private FirebaseUser firebaseUser;

    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize firebase auth and database
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        signInButton = view.findViewById(R.id.signInButton);
        loginButton = view.findViewById(R.id.loginButton);
        registerPageButton = view.findViewById(R.id.registerPageButton);

        emailText = view.findViewById(R.id.emailText);
        passwordText = view.findViewById(R.id.passwordText);
        dialog = new AlertDialogFragment();

        signInButton.setSize(SignInButton.SIZE_STANDARD);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            if( ((MainActivity) getActivity()).isNetworkAvailable() ) {
                signIn(email, password);
            } else {
                ((MainActivity) getActivity()).alertUserError(dialog);
            }}
        });

        registerPageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if(((MainActivity) getActivity()).isNetworkAvailable()) {
                moveToRegisterPage();
            } else {
                ((MainActivity) getActivity()).alertUserError(dialog);
            }}
        });

        //Initialize sign in options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("766404629826-f0k3quh22862hjh376rdmkdia9m9obpe.apps.googleusercontent.com").requestEmail().build();

        //Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);

        signInButton.setOnClickListener(v -> {
            if(((MainActivity) getActivity()).isNetworkAvailable()) {
                //Initialize sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                //Start activity for result
                startActivityForResult(intent, 100);
            } else {
                ((MainActivity) getActivity()).alertUserError(dialog);
            }
        });

        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) checkUserInDB();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if(requestCode == 100) {
            //When request code is equal to 100
            //Initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            //Check condition
            if(signInAccountTask.isSuccessful()) {
                //When google sign in successful
                try {
                    //Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    //Check condition
                    if(googleSignInAccount != null) {
                        //When sign in account is not equal to null
                        //Initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        //Check credential
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(requireActivity(), task -> {
                            //Check condition
                            if(task.isSuccessful()) {
                                //When task is successful
                                //Redirect to homepage
                                checkUserInDB();
                            } else {
                                Toast.makeText(getActivity(), "Sign In Unsuccessful" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            } else{
                Log.d("Sign In", signInAccountTask.getException().toString());
            }
        }
    }

    private void addUserToDB(FirebaseUser user) {
        myRef = db.getReference("users");
        String name = user.getDisplayName();
        String email = user.getEmail();
        String userID = user.getUid();

        //Add user to user table
        User currentUser = new User(name, email, userID, "never");
        myRef.child(userID).setValue(currentUser);

        //Add default stats for user
        Stats stats = new Stats(50);
        db.getReference("stats").child(userID).setValue(stats);
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    moveToHomepage();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        passwordText.setError("The password is invalid or the user does not have a password");
                    }
                    if(task.getException() instanceof FirebaseAuthInvalidUserException) {
                        emailText.setError("Email is not registered");
                    }

                }
            });
    }

    private void checkUserInDB() {
        if(((MainActivity) getActivity()).isNetworkAvailable()) {
            //Instantiate database and userID
            firebaseUser = firebaseAuth.getCurrentUser();
            String userID = firebaseAuth.getUid();
            //Log.d("userID is null", userID);
            myRef = db.getReference("users");

            myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user == null) {
                        addUserToDB(firebaseUser);
                    }
                    moveToHomepage();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Database read from user in google sign in", "unsuccessful");
                }

            });
        }
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