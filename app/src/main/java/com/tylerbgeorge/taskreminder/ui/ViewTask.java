package com.tylerbgeorge.taskreminder.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tylerbgeorge.taskreminder.R;
import com.tylerbgeorge.taskreminder.database.DatabaseHandler;
import com.tylerbgeorge.taskreminder.database.Task;

/**
 * Created by Tyler on 12/10/2014.
 */
public class ViewTask extends Activity {
    DatabaseHandler db;
    TextView taskName;
    TextView taskDesc;

    private final Context myApp = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task);

        db = new DatabaseHandler(this);
        taskName = (TextView) findViewById(R.id.task_name);
        taskDesc = (TextView) findViewById(R.id.task_description);

        Task task = db.getTask(Integer.parseInt(this.getIntent().getStringExtra("taskID")));
        taskName.setText(task.getName());
        taskDesc.setText(task.getDescription());

    }
}
