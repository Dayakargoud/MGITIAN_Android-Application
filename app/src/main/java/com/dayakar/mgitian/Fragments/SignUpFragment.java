package com.dayakar.mgitian.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Activities.MainActivity;
import com.dayakar.mgitian.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Button mSignup;
    private TextInputLayout mEmail,mPassword,mUsername;
    private TextView login_text;
    private ProgressDialog mProgress;



    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();

        mSignup= v.findViewById(R.id.buttonSignup);
        mEmail= v.findViewById(R.id.emailSignUp);
        mPassword= v.findViewById(R.id.passwordSignup);
        mUsername= v.findViewById(R.id.usernameSignup);
        login_text= v.findViewById(R.id.signUp_login_text);
        mProgress=new ProgressDialog(getContext());

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getEditText().getText().toString().trim();
                String password=mPassword.getEditText().getText().toString().trim();
                String username=mUsername.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    mUsername.setError("Username cannot be empty");
                }else if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                }else if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                }else{
                    attemptSignUp(email,password,username);
                    mEmail.setError(null);
                    mPassword.setError(null);
                    mUsername.setError(null);

                }
            }
        });
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_contaner, new LoginFragment())
                        .commit();

            }
        });

        return v;


    }
    private void attemptSignUp(String email, String password, final String userName){

        mProgress.setTitle("Signing in...");
        mProgress.setMessage("Please wait");
        mProgress.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user=mAuth.getCurrentUser();
                if (task.isSuccessful()) {

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userName).build();
                    user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseUser us=FirebaseAuth.getInstance().getCurrentUser();

                            if(us!=null){
                               // signUpListener.newUsersignUpListener(us);
                            }
                        }
                    });



                    getActivity().startActivity(new Intent(getContext(), MainActivity.class));
                    Toast.makeText(getContext(), "Registration Successfull", Toast.LENGTH_SHORT).show();
                      mProgress.dismiss();
                    getActivity().finish();
                }else {


                    Toast.makeText(getContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();


            }
        });




    }


}
