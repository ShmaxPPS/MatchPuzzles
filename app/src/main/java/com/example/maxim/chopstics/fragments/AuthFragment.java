package com.example.maxim.chopstics.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.maxim.chopstics.R;
import com.example.maxim.chopstics.activities.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthFragment extends BaseFragment implements
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "AuthFragment";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private GoogleApiClient mGoogleApiClient;

    private Button mPlay;
    private Button mGoogle;
    private Button mEmail;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        mAuth = FirebaseAuth.getInstance();

        mPlay = (Button) view.findViewById(R.id.btn_play);
        mGoogle = (Button) view.findViewById(R.id.btn_google_sign_in);
        mEmail = (Button) view.findViewById(R.id.btn_custom_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mPlay.setOnClickListener(v -> startGame());
        mGoogle.setOnClickListener(v -> googleSignIn());
        mEmail.setOnClickListener(v -> emailSignIn());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getActivity(), "Sign in problem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                startGame();
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(getActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }

            hideProgressDialog();
        });
    }

    private void emailSignIn() {
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(getActivity(), "You're already signed",
                    Toast.LENGTH_SHORT).show();
        } else {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            LoginFragment fragment = new LoginFragment();
            ft.replace(R.id.fragmentFrame, fragment, LoginFragment.TAG);
            ft.addToBackStack(TAG);
            ft.commit();
        }
    }

    private void googleSignIn() {
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(getActivity(), "You're already signed",
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    private void startGame() {
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "You should sign in",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

}
