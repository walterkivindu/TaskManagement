package com.example.taskmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ViewTaskActivity extends ActionBarActivity {
	EditText etName, etTComplete, etTCreateDate, etTDueDate, etTImportance;
	ProgressDialog pDialog;
	ProgressDialog prgDialog;
	String accountId, taskImportance,taskComplete;
	Data dbhelper = new Data(this);
	Thread thread;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task);
		etName = (EditText) this.findViewById(R.id.etName);
		etTComplete = (EditText) this.findViewById(R.id.etTComplete);
		etTCreateDate = (EditText) this.findViewById(R.id.etTCreateDate);
		etTDueDate = (EditText) this.findViewById(R.id.etTDueDate);
		etTImportance = (EditText) this.findViewById(R.id.etTImportance);
		dissableFields();

		thread = new Thread(runable);
		thread.start();
		setTitle("Viewing task" + this.getIntent().getStringExtra("accountid"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_task, menu);
		return true;
	}

	MainActivity ma = new MainActivity();
	String name = ma.myName("Ma name");

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.dropdown_1) {
			// MenuItem menuItem = (MenuItem) findViewById(R.id.TaskImportance);
			// menuItem.setIcon(null);
			// menuItem.setIcon(getResources().getDrawable(R.drawable.importance1));
			taskImportance = "Low";
			startThread();

		} else if (id == R.id.dropdown_2) {
			taskImportance = "Medium";
			startThread();
		} else if (id == R.id.dropdown_3) {
			taskImportance = "High";
			startThread();

		}
		else if (id == R.id.dropdown_4) {
			etTComplete.setText("No");
			startThread();
		}
		else if (id == R.id.dropdown_5) {
			etTComplete.setText("Yes");
			startThread();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		int drawable = R.drawable.importance1;
		String im = this.taskImportance;
		if (im != null) {
			if (im.equalsIgnoreCase("High")) {
				drawable = R.drawable.importance3;
			} else if (im.equalsIgnoreCase("Medium")) {
				drawable = R.drawable.importance2;
			} else if (im.equalsIgnoreCase("Low")) {
				drawable = R.drawable.importance1;
			}
		}
		menu.findItem(R.id.TaskImportance).setIcon(
				getResources().getDrawable(drawable));
		menu.findItem(R.id.TaskComp).setIcon(
				getResources().getDrawable(R.drawable.tast_complete));
		return super.onPrepareOptionsMenu(menu);
	}

	public void dissableFields() {
		EditText name, importance, complete, create, due;
		name = (EditText) findViewById(R.id.etName);
		importance = (EditText) findViewById(R.id.etTImportance);
		complete = (EditText) findViewById(R.id.etTComplete);
		create = (EditText) findViewById(R.id.etTCreateDate);
		due = (EditText) findViewById(R.id.etTDueDate);

		//name.setEnabled(false);
		importance.setEnabled(false);
		complete.setEnabled(false);
		//create.setEnabled(false);
		//due.setEnabled(false);
	}

	public void clearFields() {
		EditText importance;
		importance = (EditText) findViewById(R.id.etTImportance);
		importance.setText("");
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
						clearFields();
						new UpdateTask().execute();
					}
				});
			}
		};

		thread.start();
	}

	public Runnable runable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				clearFields();
				new UpdateTask().execute();
			} catch (Exception e) {

			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		taskImportance = this.getIntent().getStringExtra("taskimportance");
		accountId = this.getIntent().getStringExtra("accountid");
		// sLog.d("Accounts", "Account Id : " + accountId);
		Data dbhelper = new Data(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor account = db.query("Task", null, "_id = ?",
				new String[] { accountId }, null, null, null);
		// startManagingCursor(accounts);
		if (account.moveToFirst()) {
			// update view
			etName.setText(account.getString(account.getColumnIndex("tname")));
			etTComplete.setText(account.getString(account
					.getColumnIndex("tcomplete")));
			etTCreateDate.setText(account.getString(account
					.getColumnIndex("tcreated")));
			etTDueDate.setText(account.getString(account
					.getColumnIndex("tduedate")));
			etTImportance.setText(account.getString(account
					.getColumnIndex("timportance")));
		}
		account.close();
		db.close();
		dbhelper.close();

	}


	@Override
	public void onBackPressed() {
		// Display alert message when back button has been pressed
		backButtonHandler();
		return;
	}

	public void backButtonHandler() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				ViewTaskActivity.this);
		// Setting Dialog Title
		alertDialog.setTitle("Exiting");
		// Setting Dialog Message
		alertDialog.setMessage("Save the changes?");
		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.confirm);
		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to invoke NO event
						dialog.cancel();
					}
				});

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startThread();
						Intent intent ; intent = new
								Intent(getApplicationContext(), TasksActivity.class);
						startActivity(intent); finish();

						//System.exit(0);
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	public void refreshView() {
		accountId = this.getIntent().getStringExtra("accountid");
		// sLog.d("Accounts", "Account Id : " + accountId);
		Data dbhelper = new Data(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor account = db.query("Task", null, "_id = ?",
				new String[] { accountId }, null, null, null);
		// startManagingCursor(accounts);
		if (account.moveToFirst()) {
			// update view
			etName.setText(account.getString(account.getColumnIndex("tname")));
			etTComplete.setText(account.getString(account
					.getColumnIndex("tcomplete")));
			etTCreateDate.setText(account.getString(account
					.getColumnIndex("tcreated")));
			etTDueDate.setText(account.getString(account
					.getColumnIndex("tduedate")));
			etTImportance.setText(account.getString(account
					.getColumnIndex("timportance")));
		}
		account.close();
		db.close();
		dbhelper.close();

	}




	class UpdateTask extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ViewTaskActivity.this);
			pDialog.setMessage("Saving ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.setProgress(0);
			pDialog.setMax(1000);
			pDialog.show();
		}

		/**
		 * Saving product
		 * */
		protected String doInBackground(String... args) {
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// getting updated data from EditTexts

			String name = etName.getText().toString();
			String complete = etTComplete.getText().toString();
			String createdate = etTCreateDate.getText().toString();
			String duedate = etTDueDate.getText().toString();

			try {
				ContentValues values = new ContentValues();
				values.put("tname", name);
				values.put("tcomplete", complete);
				values.put("tcreated", createdate);
				values.put("tduedate", duedate);
				values.put("timportance", taskImportance);

				long rows = db.update("Task", values, "_id = ?",
						new String[] { accountId });

				db.close();

				if (rows > 0) {
					Toast.makeText(ViewTaskActivity.this,
							"Task Importance changed!", Toast.LENGTH_SHORT)
							.show();

					finish();
				} else {
					// failed to update product
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// refresh the view and dismiss the dialog once task uupdated
			refreshView();
			pDialog.dismiss();
		}
	}
}
