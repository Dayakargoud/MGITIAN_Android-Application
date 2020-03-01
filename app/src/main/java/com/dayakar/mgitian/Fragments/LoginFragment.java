package com.dayakar.mgitian.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Activities.AddEventActivity;
import com.dayakar.mgitian.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private Button login_button;
    private TextInputLayout email,password;
    private TextView singUp;
    private FirebaseAuth mAuth;
    private String passswordInput,emailInput;
    private ProgressDialog mProgressDialog;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_login, container, false);

        mAuth=FirebaseAuth.getInstance();
        setUpUIviews(v);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!is_email_valid() | !is_password_valid()){
                    return;

                }else {
                    attemp_login(emailInput,passswordInput);
                }
            }
        });
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_contaner, new SignUpFragment())
                        .commit();

            }
        });




        return v;
    }
    private void setUpUIviews(View v){
        login_button= v.findViewById(R.id.login_button);
        email= v.findViewById(R.id.login_email);
        password= v.findViewById(R.id.login_password);
        singUp= v.findViewById(R.id.login_signUp_text);
        mProgressDialog=new ProgressDialog(getContext());

    }
    private boolean is_email_valid(){


        emailInput=email.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            email.setError("Email can't be empty");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }


    }
    private boolean is_password_valid(){
        passswordInput=password.getEditText().getText().toString().trim();

        if(passswordInput.isEmpty()){
            password.setError("Password can't be empty");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }
    private void attemp_login(String email,String password) {
       mProgressDialog.setTitle("Logging...");
       mProgressDialog.setMessage("Please wait");
       mProgressDialog.setCancelable(false);
       mProgressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){
                               // loginListener.onLogin(user);
                                getActivity().onBackPressed();
                            }
                              mProgressDialog.dismiss();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();

                        }

                    }
                });

    }

}
