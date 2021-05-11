package com.example.taskmanagement;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TasksActivity extends ActionBarActivity {
	EditText tvTaskName, tvTaskimp, tvTaskComp, tvTaskcDate, tvTaskdDate;
	TextView tvid;
	ProgressDialog pDialog;
	int position, max, count = 0;
	Button pre, next;
	private Handler handler = new Handler();
	Data dbhelper = new Data(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);
		pre = (Button) findViewById(R.id.button1);
		next = (Button) findViewById(R.id.button2);
		tvTaskName = (EditText) findViewById(R.id.tvTaskName);
		tvTaskimp = (EditText) findViewById(R.id.tvTaskimp);
		tvTaskComp = (EditText) findViewById(R.id.tvTaskComp);
		tvTaskcDate = (EditText) findViewById(R.id.tvTaskcDate);
		tvTaskdDate = (EditText) findViewById(R.id.tvTaskdDate);
		tvid = (TextView) findViewById(R.id.tv_id);
		next.setBackgroundResource(R.drawable.arrow_right);
		pre.setBackgroundResource(R.drawable.arrow_left);

		pre.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (count > 0) {
					count--;
					position = count;
					setPosition(position);
					next.setEnabled(true);

					if (count == 0) {
						pre.setEnabled(false);
						// pre.setBackgroundColor(Color.RED);

					}
					move();
				}

			}
		});
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (count <= max - 1) {
					count++;
					position = count;
					setPosition(position);
					pre.setEnabled(true);
					//
					if (count == max - 1) {
						next.setEnabled(false);
						// next.setBackgroundColor(Color.RED);
					}
					move();
				}

			}
		});

		tvTaskName.setEnabled(false);
		tvTaskimp.setEnabled(false);
		tvTaskComp.setEnabled(false);
		tvTaskcDate.setEnabled(false);
		tvTaskdDate.setEnabled(false);

		tvTaskName.setFocusable(false);
		tvTaskimp.setFocusable(false);
		tvTaskComp.setFocusable(false);
		tvTaskcDate.setFocusable(false);
		tvTaskdDate.setFocusable(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tasks, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.edit) {
			Intent i=new Intent(getApplicationContext(),ViewTaskActivity.class);
			i.putExtra("accountid",tvid.getText().toString());
			i.putExtra("taskimportance", tvTaskimp.getText().toString());
			startActivity(i);
			return true;
		}
		if (id == R.id.Add) {
			Intent i=new Intent(getApplicationContext(),AddTaskActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void startThread() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {

					sleep(1);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.post(new Runnable() {
					public void run() {
						new ShowTask().execute();

					}
				});
			}
		};

		thread.start();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//String category = this.getIntent().getStringExtra("cat");
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor account = db.rawQuery("select * from Task", null);
		max = account.getCount();
		if (max == 0)
			next.setEnabled(false);
		// startManagingCursor(accounts);
		if (account.moveToFirst()) {
			// update view
			tvid.setText(account.getString(account.getColumnIndex("_id")));
			tvTaskName.setText(account.getString(account.getColumnIndex("tname")));
			tvTaskimp.setText(account.getString(account
					.getColumnIndex("timportance")));
			tvTaskComp.setText(account.getString(account.getColumnIndex("tcomplete")));
			tvTaskcDate.setText(account.getString(account.getColumnIndex("tcreated")));
			tvTaskdDate.setText(account.getString(account.getColumnIndex("tduedate")));
		}
		account.close();
		db.close();
		dbhelper.close();
		pre.setEnabled(false);
	}

	public void setPosition(int pos) {
		position = pos;
	}

	public int getPosition() {
		return position;
	}

	public void move() {
		//startThread();
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		try {
			Cursor account = db.rawQuery("select * from Task", null);
			int pos = getPosition();
			if (account.moveToPosition(pos)) {

				tvid.setText(account.getString(account.getColumnIndex("_id")));
				tvTaskName.setText(account.getString(account.getColumnIndex("tname")));
				tvTaskimp.setText(account.getString(account
						.getColumnIndex("timportance")));
				tvTaskComp.setText(account.getString(account
						.getColumnIndex("tcomplete")));
				tvTaskcDate.setText(account.getString(account
						.getColumnIndex("tcreated")));
				tvTaskdDate.setText(account.getString(account
						.getColumnIndex("tduedate")));
				if (account.isLast())
					next.setEnabled(false);

			}
			account.close();
			db.close();
			dbhelper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ShowTask extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TasksActivity.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.setProgress(0);
			pDialog.setMax(100);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			try {
				Cursor account = db.rawQuery("select * from Task", null);
				// int pos=getPosition();
				if (account.moveToFirst()) {

					tvid.setText(account.getString(account
							.getColumnIndex("_id")));
					tvTaskName.setText(account.getString(account
							.getColumnIndex("tname")));
					tvTaskimp.setText(account.getString(account
							.getColumnIndex("timportance")));
					tvTaskComp.setText(account.getString(account
							.getColumnIndex("tcomplete")));
					tvTaskcDate.setText(account.getString(account
							.getColumnIndex("tcreated")));
					tvTaskdDate.setText(account.getString(account
							.getColumnIndex("tduedate")));
					if (account.isLast())
						next.setEnabled(false);

				}
				account.close();
				db.close();
				dbhelper.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product uupdated
			pDialog.dismiss();
		}
	}
}
