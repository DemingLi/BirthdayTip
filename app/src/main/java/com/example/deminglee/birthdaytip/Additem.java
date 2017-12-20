package com.example.deminglee.birthdaytip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Additem extends AppCompatActivity {
  myDB db = new myDB(this);
  private Button add_botton;
  private EditText add_name, add_birth, add_gift;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_additem);
    
    add_botton = (Button) findViewById(R.id.add_item_button);
    add_name = (EditText) findViewById(R.id.write_name);
    add_birth = (EditText) findViewById(R.id.write_birth);
    add_gift = (EditText) findViewById(R.id.write_gift);
    
    add_botton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String name = add_name.getText().toString();
        String birth = add_birth.getText().toString();
        String gift = add_gift.getText().toString();
        
        int result = db.insert(name, birth, gift);
        if (result == 0) {
          Intent intent = getIntent();
          setResult(1, intent);
          finish();
        } else if (result == 1) {//名字为空
          Toast.makeText(Additem.this, "名字为空，请完善", Toast.LENGTH_SHORT).show();
        } else if (result == 2) {//名字已存在
          Toast.makeText(Additem.this, "名字重复啦，请检查", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
}
