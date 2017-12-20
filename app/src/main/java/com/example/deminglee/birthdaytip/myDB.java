package com.example.deminglee.birthdaytip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(name text primary key, birth text, gift text)";
    db.execSQL(CREATE_TABLE);
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    //用来更新数据库版本,DB_VERSION变化时调用
  }
  public int insert(String name, String birth, String gift) {
    if (name.equals("")) return 1;//名字为空
    SQLiteDatabase dbcheck = getReadableDatabase();
    Cursor cursor = dbcheck.query(TABLE_NAME, new String[]{"name"}, null, null, null, null, null, null);
    while (cursor.moveToNext()) {
      if (cursor.getString(0).equals(name)) return 2;//名字是否已存在
    }
    cursor.close();
  
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("name", name);
    values.put("birth", birth);
    values.put("gift", gift);
    db.insert(TABLE_NAME, null, values);
    db.close();
    return 0;
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
  public void delete(String name) {
    SQLiteDatabase db = getWritableDatabase();
    String whereClause = "name = ?";
    String[] whereArgs = { name };
    db.delete(TABLE_NAME, whereClause, whereArgs);
    db.close();
  }
  
  private ArrayList<Map<String, String>> getlist(Cursor cursor) {
    ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
    while (cursor.moveToNext()) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("name", cursor.getString(0));
      map.put("birth", cursor.getString(1));
      map.put("gift", cursor.getString(2));
      list.add(map);
    }
    return list;
  }
  public ArrayList<Map<String, String>> queryArrayList() {
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.query(TABLE_NAME, new String[]{"name", "birth", "gift"}, null, null, null, null, null, null);
    return getlist(cursor);
  }
}
