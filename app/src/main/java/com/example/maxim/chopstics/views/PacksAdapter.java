package com.example.maxim.chopstics.views;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maxim.chopstics.R;

public class PacksAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    // Adapter's context.
    private FragmentActivity mActivity = null;

    public PacksAdapter(@NonNull FragmentActivity activity) {
        mActivity = activity;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleViewHolder(LayoutInflater.from(mActivity).inflate(
                R.layout.pack_view, parent, false));
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        View cardView = holder.itemView;
        cardView.setOnClickListener(v -> Toast.makeText(mActivity,
                " Card: " + position + " was pressed", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return 8;
    }
}
