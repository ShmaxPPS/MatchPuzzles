package com.example.maxim.chopstics.views;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maxim.chopstics.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PacksAdapter extends RecyclerView.Adapter<PacksAdapter.PackViewHolder> {

    private FragmentActivity mActivity;
    private List<PackModel> mPacks;

    public PacksAdapter(FragmentActivity activity, List<PackModel> packs) {
        mActivity = activity;
        mPacks = packs;
    }

    @Override
    public PackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PackViewHolder(LayoutInflater.from(mActivity).inflate(
                R.layout.pack_view, parent, false));
    }

    @Override
    public void onBindViewHolder(PackViewHolder holder, int position) {
        PackModel pack = mPacks.get(position);
        holder.packName.setText(pack.getName());
        holder.itemView.setBackgroundColor(Color.parseColor(pack.getColor()));

        Picasso.with(mActivity)
                .load(pack.getImageUrl())
                .networkPolicy(NetworkPolicy.OFFLINE).into(holder.packImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(mActivity)
                        .load(pack.getImageUrl()).into(holder.packImage);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPacks.size();
    }

    class PackViewHolder extends RecyclerView.ViewHolder {

        ImageView packImage = null;
        TextView packName = null;
        TextView packProgressText = null;
        ProgressBar packProgressBar = null;

        public PackViewHolder(View itemView) {
            super(itemView);

            packImage = (ImageView) itemView.findViewById(R.id.pack_image);
            packName = (TextView) itemView.findViewById(R.id.pack_name);
            packProgressText = (TextView) itemView.findViewById(R.id.pack_progress_text);
            packProgressBar = (ProgressBar) itemView.findViewById(R.id.pack_progress_bar);
        }
    }

}
