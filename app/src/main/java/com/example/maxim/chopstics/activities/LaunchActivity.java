package com.example.maxim.chopstics.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.maxim.chopstics.R;
import com.example.maxim.chopstics.fragments.AuthFragment;
import com.example.maxim.chopstics.fragments.LoginFragment;

public class LaunchActivity extends AppCompatActivity {

    private static final String TAG = "LaunchActivity";

    private AuthFragment mAuthFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (mAuthFragment == null) {
            mAuthFragment = new AuthFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,
                mAuthFragment, LoginFragment.TAG).commit();

    }
}
