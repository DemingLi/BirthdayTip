package com.example.deminglee.birthdaytip;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Deming Lee on 2017/12/19.
 */

public class myDB extends SQLiteOpenHelper {
  private static final String DB_NAME = "Contacts.db";
  private static final String TABLE_NAME = "Contacts";
  private static final int DB_VERSION = 1;
  
  public myDB(Context c) {
    super(c, DB_NAME, null, DB_VERSION);
  }
  
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_TABLE = "create table " + TABLE_NAME + "(_id integer primary key, " + "name text, " + "birth text, " + "gift text);";
    db.execSQL(CREATE_TABLE);
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    //用来更新数据库版本,DB_VERSION变化时调用
  }
  public void insert(String name, String birth, String gift) {
    SQLiteDatabase db = getWritableDatabase();
    String insert_sql = "insert into " + TABLE_NAME + "(name, birth, gift) values('" + name + "', '" + birth + "', '" + gift + "')";
    db.execSQL(insert_sql);
    db.close();
  }
  public void update(String name, String birth, String gift) {
    SQLiteDatabase db = getWritableDatabase();
    String whereClause = "name = ?";
    String[] whereArgs = { name };
    ContentValues values = new ContentValues();
    values.put("birth", birth);
    values.put("gift", gift);
    db.update(TABLE_NAME, values, whereClause, whereArgs);
    db.close();
  }
  public void delete(String name, String birth, String gift) {
    SQLiteDatabase db = getWritableDatabase();
    String whereClause = "name = ?";
    String[] whereArgs = { name };
    db.delete(TABLE_NAME, whereClause, whereArgs);
    db.close();
  }
}
