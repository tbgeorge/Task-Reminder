package com.tylerbgeorge.taskreminder.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tylerbgeorge.taskreminder.R;
import com.tylerbgeorge.taskreminder.database.DatabaseHandler;
import com.tylerbgeorge.taskreminder.database.Task;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    DatabaseHandler db;
    Button createTaskBtn;
    LinearLayout taskList;

    private final Context myApp = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        createTaskBtn = (Button) findViewById(R.id.create_new_task_button);
        taskList = (LinearLayout) findViewById(R.id.task_list);

        createTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTask.class);
                startActivity(intent);
            }
        });

        populateTaskList();

//        resetDatabase();

    }

    private void resetDatabase() {
        List<Task> tasks = db.getAllTasks();
        for(Task task : tasks)
            db.deleteTask(task);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTaskList();
    }

    private void populateTaskList() {
        taskList.removeAllViews();

        List<Task> tasks = db.getAllTasks();
        int counter = 0;
        for(final Task task : tasks) {
            TextView taskView = new TextView(myApp);
            taskView.setText(task.getName());

            if(counter % 2 == 1)
                taskView.setBackgroundColor(Color.parseColor("#cccccc"));
            else
                taskView.setBackgroundColor(Color.parseColor("#eeeeee"));
            taskView.setTextColor(Color.parseColor("#111111"));
            taskView.setPadding(5, 0, 5, 0);
            Log.d("Populate", "ID: " + task.getID());

            taskView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ViewTask.class);
                    intent.putExtra("taskID", "" + task.getID());
                    startActivity(intent);
                }
            });
            counter++;
            taskList.addView(taskView);
        }
    }
}
