package ru.startandroid.mvpsample.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.startandroid.mvpsample.R;
import ru.startandroid.mvpsample.common.User;
import ru.startandroid.mvpsample.common.UserAdapter;
import ru.startandroid.mvpsample.common.UserTable;
import ru.startandroid.mvpsample.database.DbHelper;

public class ListActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    private DbHelper dbHelper;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        loadUsers();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        userAdapter = new UserAdapter();

        RecyclerView userList = (RecyclerView) findViewById(R.id.list);
        userList.setLayoutManager(layoutManager);
        userList.setAdapter(userAdapter);

        dbHelper = new DbHelper(this);

    }

    private void loadUsers() {
        ListActivity.LoadUsersTask loadUsersTask = new ListActivity.LoadUsersTask();
        loadUsersTask.execute();
    }


    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        hideProgress();
        loadUsers();
    }
    class LoadUsersTask extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Void... params) {
            List<User> users = new LinkedList<>();
            Cursor cursor = dbHelper.getReadableDatabase().query(UserTable.TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(UserTable.COLUMN.ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.EMAIL)));
                users.add(user);
            }
            cursor.close();
            return users;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            userAdapter.setData(users);
        }
    }






/*    class ClearUsersTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            dbHelper.getWritableDatabase().delete(UserTable.TABLE, null, null);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }*/


}


