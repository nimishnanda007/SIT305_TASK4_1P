
package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TaskDatabase db;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = TaskDatabase.getInstance(this);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button addTaskBtn = findViewById(R.id.buttonAddTask);
        addTaskBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(() -> {
            List<Task> tasks = db.taskDao().getAll();

            runOnUiThread(() -> {
                adapter = new TaskAdapter(tasks, this);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }

}
