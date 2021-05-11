package com.example.taskmanagement;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddTaskActivity extends ActionBarActivity implements OnItemSelectedListener {
	EditText taskname,createdate,duedate;
	Spinner importance,complete;
	Button add;
	String item,item1;
	
	String[] importancelist = { "","Low", "Medium","High" };
	String[] completelist = { "","Yes", "No" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);
		importance=(Spinner)findViewById(R.id.spinner1);
		complete=(Spinner)findViewById(R.id.spinner2);
		add=(Button)findViewById(R.id.btnAdd);
		taskname=(EditText)findViewById(R.id.etTaskName);
		createdate=(EditText)findViewById(R.id.etTaskCreateDate);
		duedate=(EditText)findViewById(R.id.etTaskDueDate);
		
		importance.setOnItemSelectedListener(this); 
		complete.setOnItemSelectedListener(this); 
		
		
		//Creating the ArrayAdapter instance having the country list  
        ArrayAdapter<?> aa = new ArrayAdapter<Object>(this,android.R.layout.simple_spinner_item,importancelist);  
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //Setting the ArrayAdapter data on the Spinner  
        importance.setAdapter(aa);
        
        ArrayAdapter<?> ab = new ArrayAdapter<Object>(this,android.R.layout.simple_spinner_item,completelist);  
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //Setting the ArrayAdapter data on the Spinner  
        complete.setAdapter(ab);
        
        
       final Data db = new Data(this);
		//final SQLiteDatabase db = db1.getWritableDatabase();
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String tname,cDate,dDate,importa,compl;
				tname=taskname.getText().toString();
				cDate=createdate.getText().toString();
				dDate=duedate.getText().toString();
				importa=item;
				compl=item1;
				try {
					
					db.AddRecord(tname, importa, compl, cDate,dDate);
					Toast.makeText(getApplicationContext(), "Record Saved successfully.",
							Toast.LENGTH_LONG).show();
					clear();
					Intent i = new Intent(getApplicationContext(), TasksActivity.class);
					startActivity(i);
				}

				catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"An error occured!\n"+e.getMessage(), Toast.LENGTH_LONG)
							.show();
					e.printStackTrace();
				}
			}
		});
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
			item=importancelist[position];
		if(complete.hasWindowFocus()){
			item1=completelist[position];
		}	
	}
	

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public void clear(){
    }
	@Override
	public void onBackPressed() {
		// Display alert message when back button has been pressed
		backButtonHandler();
		return;
	}

	public void backButtonHandler() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				AddTaskActivity.this);
		// Setting Dialog Title
		alertDialog.setTitle("Exit?");
		// Setting Dialog Message
		alertDialog.setMessage("Leave this page!");
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
						
						  Intent intent ; intent = new
						  Intent(getApplicationContext(), TasksActivity.class);
						  startActivity(intent); finish();
						 
						//System.exit(0);
					}
				});
		
		// Showing Alert Message
		alertDialog.show();
	}
}
