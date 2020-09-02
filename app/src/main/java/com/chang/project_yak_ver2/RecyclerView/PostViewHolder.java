package com.chang.project_yak_ver2.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chang.project_yak_ver2.R;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvDrugName;
    public Button btnFinish;
    private postAdapter mAdapter;

    public PostViewHolder(@NonNull View itemView, postAdapter postAdapter) {
        super(itemView);
        mAdapter = postAdapter;
        tvDrugName=itemView.findViewById(R.id.tv_drug);
        btnFinish=itemView.findViewById(R.id.btn_mFinish);
        btnFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        mAdapter.onLikeClicked(position);

    }
}