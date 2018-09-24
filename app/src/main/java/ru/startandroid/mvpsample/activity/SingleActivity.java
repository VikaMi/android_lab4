package ru.startandroid.mvpsample.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.startandroid.mvpsample.R;
import ru.startandroid.mvpsample.choose.ChooseActivity;
import ru.startandroid.mvpsample.common.User;
import ru.startandroid.mvpsample.common.UserAdapter;
import ru.startandroid.mvpsample.common.UserTable;
import ru.startandroid.mvpsample.database.DbHelper;
import ru.startandroid.mvpsample.mvp.UsersActivity;

public class SingleActivity extends AppCompatActivity {


    private UserAdapter userAdapter;
    private DbHelper dbHelper;

    private EditText editTextName;
    private EditText editTextEmail;


    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        init();

    }

    private void addUser() {

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, R.string.empty_values, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues cv = new ContentValues(2);
        cv.put(UserTable.COLUMN.NAME, name);
        cv.put(UserTable.COLUMN.EMAIL, email);

        showProgress();
        SingleActivity.AddUserTask addUserTask = new SingleActivity.AddUserTask();
        addUserTask.execute(cv);
    }

    private void init() {

        editTextName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.email);


        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
                startActivity(new Intent(SingleActivity.this, ListActivity.class));
            }
        });
    }
    private void showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait));
    }
    class AddUserTask extends AsyncTask<ContentValues, Void, Void> {

        @Override
        protected Void doInBackground(ContentValues... params) {
            ContentValues cvUser = params[0];
            dbHelper.getWritableDatabase().insert(UserTable.TABLE, null, cvUser);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }



        /*findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUsers();
            }
        });*/


    }

   /* private void clearUsers() {
        showProgress();
        ClearUsersTask clearUsersTask = new ClearUsersTask();
        clearUsersTask.execute();
    }*/


}
