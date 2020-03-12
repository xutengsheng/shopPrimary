package com.xts.shop.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xts.shop.R;
import com.xts.shop.view.FlowLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.fl)
    FlowLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        intiData();
    }

    private void intiData() {
        EditText et_input_word;
        ArrayList<String> list = new ArrayList<>();
        list.add("阶段主任主任");
        list.add("学生");
        list.add("阶段主任主任");
        list.add("讲师123");
        list.add("阶段主任主任");
        list.add("班主任");
        list.add("阶段主任主任");
        list.add("阶");
        for (int i = 0; i < list.size(); i++) {
            TextView tv_lable = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_lable, null);
            tv_lable.setText(list.get(i));
            int finalI = i;
            tv_lable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SearchActivity.this, list.get(finalI), Toast.LENGTH_SHORT).show();
                }
            });
            fl.addView(tv_lable);
        }
    }
}
