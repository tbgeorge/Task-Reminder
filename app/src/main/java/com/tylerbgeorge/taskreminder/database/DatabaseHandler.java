package com.tylerbgeorge.taskreminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 12/10/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //All Static Variables
    //Database Version
    private static final int DATABASE_VERSION = 2;

    //Database Name
    private static final String DATABASE_NAME = "taskReminder";

    //Table names
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_TASK_LINKS = "task_links";

    //Common column names
    private static final String KEY_ID = "id";

    //TASKS Table - Column Names
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "descripition";

    //TASK-LINKS Table - Column Names
    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_LINK_TEXT = "link";

    //Table Create Statements
    //Task table create statement
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_DESCRIPTION + " TEXT" + ")";

    //Task-Links table create statement
    private static final String CREATE_TASK_LINKS_TABLE = "CREATE TABLE " + TABLE_TASK_LINKS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASK_ID + " INTEGER,"
            + KEY_LINK_TEXT + " TEXT" + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_TASK_LINKS_TABLE);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older tables if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_LINKS);

        //Create tables again
        onCreate(db);
    }

    //Add new task
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName()); //task name
        values.put(KEY_DESCRIPTION, task.getDescription()); //task description

        //Inserting rows
        long id = db.insert(TABLE_TASKS, null, values);

        //Insert rows into links table
        addLinks(id, task.getLinks());

        db.close(); //close database connection
    }

    private void addLinks(long taskId, List<String> links) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(String link : links) {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_ID, taskId);
            values.put(KEY_LINK_TEXT, link);

            db.insert(TABLE_TASK_LINKS, null, values);
        }

        db.close();
    }

    //Get single task
    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID,
                KEY_NAME, KEY_DESCRIPTION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

        List<String> links = new ArrayList<String>();
        String selectQuery = "SELECT " + KEY_LINK_TEXT + " FROM " + TABLE_TASK_LINKS + " WHERE "
                + KEY_TASK_ID + " = '" + id + "'";
        Log.e("GetLinks", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                links.add(c.getString(c.getColumnIndex(KEY_LINK_TEXT)));
            } while (c.moveToNext());
        }

        Task task = new Task(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)), links);

        cursor.close();
        c.close();
        db.close();

        //return task
        return task;
    }

    //Get All tasks
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                task.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));

                List<String> links = new ArrayList<String>();
                String linkQuery = "SELECT " + KEY_LINK_TEXT + " FROM " + TABLE_TASK_LINKS + " WHERE "
                        + KEY_TASK_ID + " = '" + task.getID() + "'";

                Cursor c = db.rawQuery(linkQuery, null);
                if(c.moveToFirst()) {
                    do {
                        links.add(c.getString(c.getColumnIndex(KEY_LINK_TEXT)));
                    } while (c.moveToNext());
                }

                c.close();
                task.setLinks(links);

                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        // return task list
        return taskList;
    }

    //Get number of tasks
    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }

    //Update single task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        deleteLinks(task.getID());
        addLinks((long) task.getID(), task.getLinks());

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_DESCRIPTION, task.getDescription());

        // updating row
        int result = db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getID()) });

        db.close();

        return result;
    }

    private void deleteLinks(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK_LINKS, KEY_TASK_ID + " = ?",
                new String[] { String.valueOf(taskId) });
        db.close();
    }

    //Delete single task
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getID()) });
        db.close();
    }
}
