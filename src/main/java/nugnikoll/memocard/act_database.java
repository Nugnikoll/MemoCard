package nugnikoll.memocard;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class act_database extends AppCompatActivity {
	protected database db;
	protected Vector<String> vec_table;
	protected ListView list_table;
	protected TextView text_table;
	protected String table_click;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_database);

		app_memo app = (app_memo) getApplication();
		db = app.get_database();
		vec_table = new Vector<>();
		list_table = findViewById(R.id.list_database);
		list_table.setOnItemClickListener(
			new AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					click_table(position);
				}
			}
		);
		text_table = findViewById(R.id.text_table);

		init_list();
		update_list();

		SharedPreferences prefer = getSharedPreferences("config", MODE_PRIVATE);
		String select_table = prefer.getString("select_table", "gre_000");
		text_table.setText("table: " + select_table);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_database, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_back:
				finish();
				return true;
			case R.id.action_add:
				create_table();
				return true;
			case R.id.action_login:
				Intent itt = new Intent(this, act_login.class);
				startActivityForResult(itt, 1);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	protected void init_list(){
		vec_table = db.get_index();
	}

	protected void update_list(){
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
			this, android.R.layout.simple_list_item_1, vec_table
		);
		list_table.setAdapter(adapter);
	}

	protected void create_table(){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Create new table");

		final EditText input_table = new EditText(this);
		input_table.setHint("table name");
		final EditText input_info = new EditText(this);
		input_info.setHint("info");
		LinearLayout linear_dialog = new LinearLayout(this);
        linear_dialog.setOrientation(LinearLayout.VERTICAL);
        linear_dialog.addView(input_table);
        linear_dialog.addView(input_info);

		builder.setView(linear_dialog);

		builder.setPositiveButton("OK", null);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		final AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String text_input_table = input_table.getText().toString().trim();
				String text_input_info = input_info.getText().toString();
				if(text_input_table.length() > 0){
					text_input_table = text_input_table.replaceAll("`", "``");
					text_input_info = text_input_info.replaceAll("`", "``");
					db.insert_table(text_input_table, text_input_info);
					init_list();
					update_list();
					dialog.dismiss();
				}else{
					Toast.makeText(
						act_database.this,
						"The table name cannot be empty!",
						Toast.LENGTH_LONG
					).show();
				}
			}
		});
	}

	protected void click_table(int position){
		table_click = vec_table.get(position);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Table: " + table_click);

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

		arrayAdapter.add("Select");
		arrayAdapter.add("Info");
		arrayAdapter.add("Delete");

		//builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		//	@Override
		//	public void onClick(DialogInterface dialog, int which) {
		//		dialog.dismiss();
		//	}
		//});

		builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int pos) {
				String strName = arrayAdapter.getItem(pos);

				switch (pos){
				case 0:
					SharedPreferences prefer = getSharedPreferences("config", MODE_PRIVATE);
					SharedPreferences.Editor editor = prefer.edit();
					editor.putString("select_table", table_click);
					editor.apply();
					text_table.setText("table: " + table_click);
					break;
				case 1:
					table_info(table_click);
					break;
				case 2:
					db.delete_table(table_click);
					init_list();
					update_list();
					break;
				default:
					AlertDialog.Builder builderInner = new AlertDialog.Builder(act_database.this);
					builderInner.setMessage(strName);
					builderInner.setTitle("Your Selected Item is");
					builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builderInner.show();
				}
			}
		});
		builder.show();
	}

	protected void table_info(String table_name){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Table: " + table_name);
		Vector<String> vec = db.get_table(table_name);
		builder.setMessage(
			"Author: " + vec.get(1)
			+ "\nType: " + vec.get(2)
			+ "\nInfo: " + vec.get(3)
		);
		builder.show();
	}
}

