package com.example.maxim.chopstics.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maxim.chopstics.R;
import com.example.maxim.chopstics.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends BaseFragment {

    public static final String TAG = "RegisterFragment";

    private FirebaseAuth mAuth;

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegister;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mName = (EditText) view.findViewById(R.id.name);
        mEmail = (EditText) view.findViewById(R.id.email);
        mPassword = (EditText) view.findViewById(R.id.password);
        mRegister = (Button) view.findViewById(R.id.btn_register);

        mRegister.setOnClickListener(v -> register());

        return view;
    }

    private void register() {
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            showProgressDialog();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startGame();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(getActivity(), "Registration failed.",
                            Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            });
        }
    }

    private void startGame(){
        FirebaseUser user = mAuth.getCurrentUser();
        String name = mName.getText().toString();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                FragmentManager manager = getFragmentManager();
                manager.popBackStack("AuthFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                Toast.makeText(getActivity(), "Setting user name failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
