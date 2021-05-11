package com.example.taskmanagement;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	ListView listAccounts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle("Task manager part 1 ");

		listAccounts = (ListView) this.findViewById(R.id.listAccounts);
		listAccounts.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView,
					int arg2, long arg3) {
				TextView textAccountId = (TextView)
				selectedView.findViewById(R.id.tvId);
				TextView textimportance = (TextView)
						selectedView.findViewById(R.id.tvTaskImportance);
				// Log.d("Accounts", "Selected Account Id : " +
				//textAccountId.getText().toString());
				Intent intent = new Intent(MainActivity.this,ViewTaskActivity.class);
				 intent.putExtra("accountid",
				textAccountId.getText().toString());
				 intent.putExtra("taskimportance",
						 textimportance.getText().toString());
				startActivity(intent);
			}
		});
	}
	public String myName(String name){
		return name;
	}

	@Override
	public void onStart() {
		super.onStart();
		try {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Data dbhelper = new Data(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor accounts = db.query(false, "Task", null, null, null, null,
					null, null, null);

			String from[] = { "_id", "tname", "timportance", "tcomplete",
					"tcreated", "tduedate" };
			int to[] = { R.id.tvId, R.id.tvTaskName, R.id.tvTaskImportance,
					R.id.tvTaskComplete, R.id.tvTaskCreatinDate,
					R.id.tvTaskDueDate };

			@SuppressWarnings("deprecation")
			SimpleCursorAdapter ca = new SimpleCursorAdapter(this,
					R.layout.task_list, accounts, from, to);

			ListView listAccounts = (ListView) this
					.findViewById(R.id.listAccounts);
			listAccounts.setAdapter(ca);
			dbhelper.close();
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.AddTask1) {
			Intent i = new Intent(getApplicationContext(),
					AddTaskActivity.class);
			startActivity(i);
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}
	
}
