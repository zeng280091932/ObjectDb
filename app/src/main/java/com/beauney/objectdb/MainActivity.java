package com.beauney.objectdb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beauney.objectdb.db.manager.UserDBHelper;
import com.beauney.objectdb.model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mUsernameEdt;

    private EditText mPasswordEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameEdt = findViewById(R.id.username);
        mPasswordEdt = findViewById(R.id.password);
    }

    public void addData(View view) {
        String username = mUsernameEdt.getText().toString();
        String password = mPasswordEdt.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            showToast("不能输入空数据");
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        UserDBHelper.getInstance(this).insert(user);
    }

    public void delData(View view) {
        String username = mUsernameEdt.getText().toString();
        if (TextUtils.isEmpty(username)) {
            showToast("不能输入空数据");
            return;
        }
        UserDBHelper.getInstance(this).delete(username);
        showToast("删除数据");
    }

    public void updateData(View view) {
        String username = mUsernameEdt.getText().toString();
        String password = mPasswordEdt.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            showToast("不能输入空数据");
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        UserDBHelper.getInstance(this).update(user);
        showToast("修改数据");
    }

    public void selectData(View view) {
        List<User> users = UserDBHelper.getInstance(this).findAll();
        showToast("查询数据:" + users);
    }

    private void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
