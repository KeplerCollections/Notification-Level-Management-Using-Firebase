package com.kepler.notificationsystem.admin.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kepler.notificationsystem.dao.Push;
import com.kepler.notificationsystem.support.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.kepler.notificationsystem.support.Params.BATCH;
import static com.kepler.notificationsystem.support.Params.COURSE;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String TAG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notManager";

    // Table Names
    private static final String TABLE_MSG = "msg_tbl";

    // Common column names
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String MESSAGE = "msg";
    private static final String IMAGE = "image";
    private static final String FILE = "file";
    private static final String MESSGAE_TYPE = "msg_type";
    private static final String TOPIC = "topic";
    private static final String TOPIC_NAME = "topic_name";
    private static final String IS_BACKGROUND = "is_background";
    private static final String TIME_STAMP = "time_stamp";


    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_MSG = "CREATE TABLE "
            + TABLE_MSG + "(" + ID + " INTEGER PRIMARY KEY," + TITLE
            + " TEXT," + MESSAGE + " TEXT," + IMAGE
            + " TEXT," + FILE + " TEXT," + MESSGAE_TYPE
            + " INTEGER," + IS_BACKGROUND + " INTEGER," + TIME_STAMP
            + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_MSG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_MSG);

        // create new tables
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    //    insert msg
    public long insertMsg(Push push) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, push.getTitle());
        values.put(MESSAGE, push.getMessage());
        values.put(IS_BACKGROUND, push.is_background() ? 1 : 0);
        values.put(IMAGE, push.getImage());
        values.put(TIME_STAMP, push.getTimestamp());
//        values.put(COURSE, push.getCourse());
//        values.put(BATCH, push.getBatch());
        values.put(FILE, push.getFile());
        values.put(MESSGAE_TYPE, push.getMsg_type());

        // insert row
        long id = db.insert(TABLE_MSG, null, values);
        Logger.e(TAG, "inserted--" + id);
        return id;
    }

    // fetch all
    public List<Push> getAllMsgs() {
        List<Push> todos = new ArrayList<>();
        Push td;
        String selectQuery = "SELECT  * FROM " + TABLE_MSG;
        Logger.e(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                td = new Push();
                td.setId(c.getInt((c.getColumnIndex(ID))));
                td.setTitle((c.getString(c.getColumnIndex(TITLE))));
                td.setFile((c.getString(c.getColumnIndex(FILE))));
                td.setMessage((c.getString(c.getColumnIndex(MESSAGE))));
                td.setImage((c.getString(c.getColumnIndex(IMAGE))));
                td.setMsg_type((c.getInt(c.getColumnIndex(MESSGAE_TYPE))));
                if (c.getInt(c.getColumnIndex(IS_BACKGROUND)) == 0) {
                    td.setIs_background(false);
                } else {
                    td.setIs_background(true);
                }
                td.setTimestamp((c.getString(c.getColumnIndex(TIME_STAMP))));
                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }
        return todos;
    }

    // fetch top 1 row
    public Push getMessage() {
        Push td = null;
        String selectQuery = "SELECT * FROM " + TABLE_MSG + " ORDER BY id DESC LIMIT 1";
        Logger.e(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            td = new Push();
            td.setId(c.getInt((c.getColumnIndex(ID))));
            td.setTitle((c.getString(c.getColumnIndex(TITLE))));
            td.setFile((c.getString(c.getColumnIndex(FILE))));
            td.setMessage((c.getString(c.getColumnIndex(MESSAGE))));
            td.setImage((c.getString(c.getColumnIndex(IMAGE))));
            td.setMsg_type((c.getInt(c.getColumnIndex(MESSGAE_TYPE))));
            if (c.getInt(c.getColumnIndex(IS_BACKGROUND)) == 0) {
                td.setIs_background(false);
            } else {
                td.setIs_background(true);
            }
            td.setTimestamp((c.getString(c.getColumnIndex(TIME_STAMP))));
            // adding to todo list
        }
        return td;
    }

    // delete all
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MSG);
    }
}