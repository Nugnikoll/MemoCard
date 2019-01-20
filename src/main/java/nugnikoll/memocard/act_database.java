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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * A login screen that offers login via email/password.
 */
public class act_database extends AppCompatActivity {
	protected database db;
	protected String text_input_table;
	protected Vector<String> vec_table;
	protected ListView list_table;
	protected TextView text_table;
	protected int pos_select;

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
		vec_table = db.get_table();
	}

	protected void update_list(){
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
			this, android.R.layout.simple_list_item_1, vec_table
		);
		list_table.setAdapter(adapter);
	}

	protected void create_table(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Create new table");

		final EditText input = new EditText(this);
		//input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				text_input_table = input.getText().toString();
				vec_table.add(text_input_table);
				update_list();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();
	}

	protected void click_table(int position){
		pos_select = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Table: " + vec_table.get(position));

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

		arrayAdapter.add("Select");
		arrayAdapter.add("Info");
		arrayAdapter.add("Move Upward");
		arrayAdapter.add("Move Downward");
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
					editor.putString("select_table", vec_table.get(pos_select));
					editor.apply();
					text_table.setText("table: " + vec_table.get(pos_select));
					break;
				case 1:
					table_info(pos_select);
					break;
				case 2:
					if(pos_select > 0){
						String temp = vec_table.get(pos_select - 1);
						vec_table.set(pos_select - 1, vec_table.get(pos_select));
						vec_table.set(pos_select, temp);
						db.swap("index", pos_select - 1, pos_select);
						update_list();
					}
					break;
				case 3:
					if(pos_select + 1 < vec_table.size()){
						String temp = vec_table.get(pos_select);
						vec_table.set(pos_select, vec_table.get(pos_select + 1));
						vec_table.set(pos_select + 1, temp);
						db.swap("index", pos_select, pos_select + 1);
						update_list();
					}
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

	protected void table_info(int pos){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Table: " + vec_table.get(pos));
		Vector<String> vec = db.get_table_detail(pos);
		builder.setMessage(
			"Author: " + vec.get(1)
			+ "\nType: " + vec.get(2)
			+ "\nInfo: " + vec.get(3)
		);
		builder.show();
	}
}

