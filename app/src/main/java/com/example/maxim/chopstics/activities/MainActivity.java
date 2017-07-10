package com.example.maxim.chopstics.activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.maxim.chopstics.R;
import com.example.maxim.chopstics.views.PackModel;
import com.example.maxim.chopstics.views.PacksAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mPacksReference;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mLeftDrawerList;
    private ArrayAdapter<String> mNavigationDrawerAdapter;
    private String[] mLeftSliderData = {"Logout", "About", "Contact Me"};

    private RecyclerView mRecyclerView = null;
    private LinearLayoutManager mLayoutManager = null;
    private PacksAdapter mPacksAdapter = null;

    private List<PackModel> mPacks;
    private List<String> mPacksIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mPacksReference = FirebaseDatabase.getInstance().getReference("packs");

        mPacks = new ArrayList<>();
        mPacksIds = new ArrayList<>();

        mLeftDrawerList = (ListView) findViewById(R.id.left_drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationDrawerAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, mLeftSliderData);
        mLeftDrawerList.setAdapter(mNavigationDrawerAdapter);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle(mUser.getDisplayName());
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mLeftDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mPacksAdapter = new PacksAdapter(this, mPacks);
        mRecyclerView.setAdapter(mPacksAdapter);

        updatePacksList();
    }

    private void updatePacksList() {
        mPacksReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                mPacksIds.add(dataSnapshot.getKey());
                mPacks.add(dataSnapshot.getValue(PackModel.class));
                mPacksAdapter.notifyItemInserted(mPacks.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                PackModel newPack = dataSnapshot.getValue(PackModel.class);
                String packKey = dataSnapshot.getKey();

                int packIndex = mPacksIds.indexOf(packKey);
                if (packIndex > -1) {
                    mPacks.set(packIndex, newPack);
                    mPacksAdapter.notifyItemChanged(packIndex);
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + packKey);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String packKey = dataSnapshot.getKey();
                int packIndex = mPacksIds.indexOf(packKey);

                if (packIndex > -1) {
                    mPacksIds.remove(packIndex);
                    mPacks.remove(packIndex);

                    mPacksAdapter.notifyItemRemoved(packIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + packKey);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChild:" + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled:", databaseError.toException());
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        if (position == 0) {
            mAuth.signOut();
            Intent intent = new Intent(this, LaunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings ||
                mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }


}
