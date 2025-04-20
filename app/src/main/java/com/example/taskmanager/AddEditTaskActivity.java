package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextDueDate;
    private TaskDatabase db;
    private int taskId = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        db = TaskDatabase.getInstance(this);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDueDate = findViewById(R.id.editTextDueDate);

        Intent intent = getIntent();
        if (intent.hasExtra("taskId")) {
            taskId = intent.getIntExtra("taskId", -1);
            Executors.newSingleThreadExecutor().execute(() -> {
                Task task = db.taskDao().getById(taskId);
                runOnUiThread(() -> {
                    if (task != null) {
                        editTextTitle.setText(task.getTitle());
                        editTextDescription.setText(task.getDescription());
                        editTextDueDate.setText(task.getDueDate());
                    }
                });
            });
        }

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            String dueDate = editTextDueDate.getText().toString();

            Executors.newSingleThreadExecutor().execute(() -> {
                if (taskId == -1) {
                    db.taskDao().insert(new Task(title, description, dueDate));
                } else {
                    Task updatedTask = new Task(title, description, dueDate);
                    updatedTask.setId(taskId);
                    db.taskDao().update(updatedTask);
                }
                runOnUiThread(this::finish);
            });
        });

        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(v -> {
            if (taskId != -1) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    Task taskToDelete = db.taskDao().getById(taskId);
                    if (taskToDelete != null) {
                        db.taskDao().delete(taskToDelete);
                    }
                    runOnUiThread(this::finish);
                });
            } else {
                finish();
            }
        });
    }
}
