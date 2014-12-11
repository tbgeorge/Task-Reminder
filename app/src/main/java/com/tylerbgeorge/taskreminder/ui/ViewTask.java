package com.tylerbgeorge.taskreminder.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    LinearLayout linkList;

    private final Context myApp = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task);

        db = new DatabaseHandler(this);
        taskName = (TextView) findViewById(R.id.task_name);
        taskDesc = (TextView) findViewById(R.id.task_description);
        linkList = (LinearLayout) findViewById(R.id.display_link_list);

        Task task = db.getTask(Integer.parseInt(this.getIntent().getStringExtra("taskID")));
        taskName.setText(task.getName());
        taskDesc.setText(task.getDescription());

        for(String link : task.getLinks()) {
            TextView linkView = new TextView(myApp);
            linkView.setTextColor(Color.parseColor("#000088"));

            if (!link.startsWith("http://") && !link.startsWith("https://"))
                link = "http://" + link;

            SpannableString content = new SpannableString(link);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            linkView.setText(content);

            linkList.addView(linkView);

            final String finalLink = link;
            linkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalLink));
                    startActivity(browserIntent);
                }
            });

        }

    }
}
