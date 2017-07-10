package com.example.maxim.chopstics.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxim.chopstics.R;
import com.example.maxim.chopstics.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends BaseFragment {

    public static final String TAG = "LoginFragment";

    private FirebaseAuth mAuth;

    private Button mAuthButton;
    private EditText mEmail;
    private EditText mPassword;

    private TextView mRegister;
    private TextView mForgotPassword;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        mAuthButton = (Button) view.findViewById(R.id.btn_login);
        mEmail = (EditText) view.findViewById(R.id.email);
        mPassword = (EditText) view.findViewById(R.id.password);
        mRegister = (TextView) view.findViewById(R.id.register);
        mForgotPassword = (TextView) view.findViewById(R.id.forgot_password);

        mAuthButton.setOnClickListener(v -> startSignIn());
        mRegister.setOnClickListener(v -> goToRegister());
        // mForgotPassword.setOnClickListener(v -> showDialog());

        return view;
    }

    private void startSignIn() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            showProgressDialog();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startGame();
                } else {
                    Toast.makeText(getActivity(), "Sign in problem", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            });
        }
    }

    private void goToRegister(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RegisterFragment fragment = new RegisterFragment();
        ft.replace(R.id.fragmentFrame, fragment, RegisterFragment.TAG);
        ft.addToBackStack(TAG);
        ft.commit();
    }

    private void startGame(){
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            FragmentManager manager = getFragmentManager();
            manager.popBackStack("AuthFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

}
