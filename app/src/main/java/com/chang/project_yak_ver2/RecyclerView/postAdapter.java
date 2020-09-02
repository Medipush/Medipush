package com.chang.project_yak_ver2.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chang.project_yak_ver2.Api.inputData;
import com.chang.project_yak_ver2.MainActivity;
import com.chang.project_yak_ver2.Model.DrugModel;
import com.chang.project_yak_ver2.Morning.Morning;
import com.chang.project_yak_ver2.R;
import com.chang.project_yak_ver2.mainScreen;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.chang.project_yak_ver2.Api.inputData.Delete_URL;

public class postAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private Context mContext;
    private ArrayList<DrugModel> postItems;
    private PostViewHolder holder;
    int mCheck; //1=Morning, 2=Lunch, 3=Dinner


    public postAdapter(Context context, ArrayList<DrugModel> listItem, int check) {
        mContext=context;
        postItems=listItem;
        mCheck=check;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View baseView=View.inflate(mContext, R.layout.m_post_drug,null);
        holder=new PostViewHolder(baseView, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        DrugModel item=postItems.get(position);
        holder.tvDrugName.setText(item.getDrugName());
    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    public void onLikeClicked(int position) {
        final int tmp= position;
        final DrugModel item =postItems.get(position);
        AlertDialog.Builder builder= new AlertDialog.Builder(mContext);
        builder.setTitle(item.getDrugName());
        builder.setMessage(item.getDrugName()+"을 삭제하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postItems.remove(tmp);
                notifyDataSetChanged();
                Log.d("FetchPostTask",item.getDrugName());
                ((mainScreen) mainScreen.mContext).deleteMed(item.getDrugName());
                DeletePostTask fetchPostTask = new DeletePostTask();
                fetchPostTask.execute(inputData.Get_Delete + ((MainActivity)MainActivity.mContext).getSSN() + Delete_URL  + item.getDrugName());
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }


    class DeletePostTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            String url = strings[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();

            } catch (IOException e) {
                Log.d("FetchPostTask",e.getMessage());
            }
            return null;
        }


    }
}