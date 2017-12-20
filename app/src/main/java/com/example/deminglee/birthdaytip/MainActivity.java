package com.example.deminglee.birthdaytip;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  private myDB db = new myDB(this);
  private List<Map<String, String>> item;
  private SimpleAdapter simpleAdapter;
  private ListView listView;
  private Button addbutton;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    listView = (ListView) findViewById(R.id.items);
    addbutton = (Button) findViewById(R.id.addbutton);
    
    item = db.queryArrayList();
    refreshList();
    
    addbutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, Additem.class);
        startActivityForResult(intent, 1);
      }
    });
    
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, final int position, long idp) {
        final LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        View contentView = factory.inflate(R.layout.dialoglayout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(contentView);
        
        final String name = item.get(position).get("name");
        String birth = item.get(position).get("birth");
        String gift = item.get(position).get("gift");
  
        TextView name_text = (TextView) contentView.findViewById(R.id.name_change);
        final EditText birth_change = (EditText) contentView.findViewById(R.id.birth_change);
        final EditText gift_change = (EditText) contentView.findViewById(R.id.gift_change);
        TextView tel = (TextView) contentView.findViewById(R.id.tel);
        
        name_text.setText(name);
        birth_change.setText(birth);
        gift_change.setText(gift);
        
        String number = new String();
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while (cursor.moveToNext()) {
          String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
          String check_name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
          if (!check_name.equals(name)) continue;
          //判断有没有电话号码
          int isHas = Integer.parseInt(cursor.getString(
                  cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
          ));
          //如果有，按照id查询
          if (isHas > 0) {
            Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                    null, null
            );
            while (c.moveToNext()) {
              number += c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";
            }
            c.close();
            break;
          }
        }
        if (!number.equals("")) {
          tel.setText(number);
        } else {
          tel.setText("无");
        }
        
        builder.setTitle("                  (๑•̀ㅂ•́)و✧     ")
                .setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    String new_birth = birth_change.getText().toString();
                    String new_gift = gift_change.getText().toString();
                    db.update(name, new_birth, new_gift);
                    refreshList();
                  }
                })
                .setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                  }
                }).create().show();
      }
    });
    
    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long idp) {
        final String name = item.get(position).get("name");
        final AlertDialog.Builder alterDialog = new AlertDialog.Builder(MainActivity.this);
        alterDialog.setTitle("删除条目")
                .setMessage("删除条目"+name+"?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    db.delete(name);
                    refreshList();
                  }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                  }
                }).create().show();
        return true;
      }
    });
  }
  
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    refreshList();
  }
  
  private void refreshList() {
    item = db.queryArrayList();
    simpleAdapter = new SimpleAdapter(getApplicationContext(), item, R.layout.item,
            new String[]{"name", "birth", "gift"},
            new int[]{R.id.name, R.id.birth, R.id.gift});
    listView.setAdapter(simpleAdapter);
  }
}
