package com.chang.project_yak_ver2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chang.project_yak_ver2.Api.inputData;

import java.util.ArrayList;
import java.util.List;

public class Cautions extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cautions_main);
        List<String> cautionsList=new ArrayList<String>();
        LinearLayout llScrollParent=findViewById(R.id.linearLayoutScroll);

        Log.d("FetchPostTask", "cautions");
        Intent StartIntent = getIntent();
        cautionsList = (ArrayList<String>) StartIntent.getSerializableExtra("array");

        for(int i=0;i<cautionsList.size();i++)
        {
            View v = View.inflate(this, R.layout.caution_item, null);
            TextView tvCaution = v.findViewById(R.id.tv_caution);
            tvCaution.setText((i+1)+". "+cautionsList.get(i));
            llScrollParent.addView(v);
        }

    }
}
