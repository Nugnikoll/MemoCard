package nugnikoll.memocard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class act_main extends AppCompatActivity implements View.OnClickListener{

	protected database db;
	protected Button button_start, button_database, button_setting;
	protected TextView text_table, text_quote, text_author;
	protected int quote_size;
	protected String select_table;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);

		button_start = findViewById(R.id.button_start);
		button_start.setOnClickListener(this);
		button_database = findViewById(R.id.button_database);
		button_database.setOnClickListener(this);
		button_setting = findViewById(R.id.button_setting);
		button_setting.setOnClickListener(this);
		text_table = findViewById(R.id.text_table);
		text_quote = findViewById(R.id.text_quote);
		text_author = findViewById(R.id.text_author);

		app_memo app = (app_memo) getApplication();
		db = app.get_database();
		quote_size = db.get_table_size("quote");
		Random rnd = new Random();
		int pos = rnd.nextInt(quote_size);
		set_quote(pos + 1);
		show_table();
	}

	@Override
	public void onResume(){
		super.onResume();
		show_table();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_login:
				Intent itt = new Intent(this, act_login.class);
				startActivityForResult(itt, 1);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View view){
		Intent itt;
		switch(view.getId()){
		case R.id.button_start:
			if(!check_table()){
				Toast.makeText(this, "Invalid table selected.", Toast.LENGTH_LONG).show();
			}else{
				itt = new Intent(this, act_card.class);
				startActivityForResult(itt, 1);
			}
			break;
		case R.id.button_database:
			itt = new Intent(this, act_database.class);
			startActivityForResult(itt, 1);
			break;
		case R.id.button_setting:
			//itt = new Intent(this, act_database.class);
			//startActivityForResult(itt, 1);
			break;
		default:
			break;
		}
	}

	protected void show_table(){
		SharedPreferences prefer = getSharedPreferences("config", MODE_PRIVATE);
		select_table = prefer.getString("select_table", "gre_000");
		text_table.setText(select_table);
	}

	protected void set_quote(int pos){
		Vector<String> vec = db.get_quote("quote", pos);
		text_quote.setText(vec.get(0));
		text_author.setText(vec.get(1));
	}

	protected boolean check_table(){
		Vector<String> vec = db.get_table_detail(select_table);
		if(!vec.get(2).equals("card")){
			return false;
		}else{
			return true;
		}
	}
}

