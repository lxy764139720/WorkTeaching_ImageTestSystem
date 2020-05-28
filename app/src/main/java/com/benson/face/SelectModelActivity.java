package com.benson.face;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectModelActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Scene[] scenes = {new Scene("发型推荐", R.drawable.hair),
            new Scene("求职正装", R.drawable.find_job),
            new Scene("约会穿搭", R.drawable.date),
            new Scene("日常穿搭", R.drawable.daily)};
    private List<Scene> sceneList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_model);
        initScenes();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        int spanCount = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        SceneAdapter adapter = new SceneAdapter(sceneList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SceneAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        Toast.makeText(SelectModelActivity.this, "发型推荐", Toast.LENGTH_SHORT).show();
                        selectFace(view);
                        break;
                    case 1:
                        Toast.makeText(SelectModelActivity.this, "求职正装", Toast.LENGTH_SHORT).show();
                        selectBody(view);
                        break;
                    case 2:
                        Toast.makeText(SelectModelActivity.this, "约会穿搭", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(SelectModelActivity.this, "日常穿搭", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //选择面部识别模块
    public void selectFace(View view) {
        try {
            startActivity(new Intent(this, FaceCameraActivity.class)); //页面跳转到面部拍照
            overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //选择全身识别模块
    public void selectBody(View view) {
        startActivity(new Intent(this, BodyCameraActivity.class)); //页面跳转到全身识别
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right);
    }

    private void initScenes() {
        sceneList.clear();
        sceneList.addAll(Arrays.asList(scenes).subList(0, 4));
    }

}
