package com.tylerbgeorge.taskreminder.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tylerbgeorge.taskreminder.R;
import com.tylerbgeorge.taskreminder.database.DatabaseHandler;
import com.tylerbgeorge.taskreminder.database.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 12/10/2014.
 */
public class CreateTask extends Activity {

    DatabaseHandler db;
    EditText taskName;
    EditText taskDesc;
    EditText linkEdit;
    LinearLayout linkList;
    Button addLink;
    Button createTask;

    private final List<String> links = new ArrayList<String>();

    private final Context myApp = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        db = new DatabaseHandler(this);
        createTask = (Button) findViewById(R.id.create_task_button);
        taskName = (EditText) findViewById(R.id.task_name_edit);
        taskDesc = (EditText) findViewById(R.id.task_description_edit);
        linkEdit = (EditText) findViewById(R.id.link_edit);
        addLink = (Button) findViewById(R.id.add_link);
        linkList = (LinearLayout) findViewById(R.id.link_list);


        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = linkEdit.getText().toString();
                if(link.equalsIgnoreCase(""))
                    Toast.makeText(myApp, "You cannot add a blank link", Toast.LENGTH_LONG).show();
                else {
                    links.add(link);
                    reloadLinks();
                }
            }
        });

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = taskName.getText().toString();
                String desc = taskDesc.getText().toString();
                if(name.equalsIgnoreCase(""))
                    Toast.makeText(myApp, "The Task Name field cannot be empty", Toast.LENGTH_LONG).show();
                else if(desc.equalsIgnoreCase(""))
                    Toast.makeText(myApp, "The Task Description field cannot be empty", Toast.LENGTH_LONG).show();
                else {
                    db.addTask(new Task(name, desc, links));
                    Toast.makeText(myApp, "The Task " + name + " has been created", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void reloadLinks() {
        linkList.removeAllViews();

        int counter = 0;
        for(String link : links) {
            LinearLayout linkRow = new LinearLayout(myApp);
            LinearLayout.LayoutParams linkRowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linkRow.setOrientation(LinearLayout.HORIZONTAL);
            linkRowParams.setMargins(dipToPixels(myApp, 11f), 0, dipToPixels(myApp, 10f), 0);
            linkRow.setLayoutParams(linkRowParams);

            TextView linkText = new TextView(myApp);
            TableRow.LayoutParams linkTextParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, .7f);
            linkTextParams.setMargins(0, dipToPixels(myApp, 14f), 0, 0);
            linkText.setText(link);
            linkText.setLayoutParams(linkTextParams);

            Button removeLink = new Button(myApp);
            TableRow.LayoutParams removeLinkParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, .2f);
            removeLink.setText("Remove");
            removeLink.setTextSize(dipToPixels(myApp, 3f));
            removeLink.setLayoutParams(removeLinkParams);

            final int index = counter;
            removeLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    links.remove(index);
                    reloadLinks();
                }
            });

            linkRow.addView(linkText);
            linkRow.addView(removeLink);
            linkList.addView(linkRow);
            linkEdit.setText("");
            counter++;
        }
    }

    public static int dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
