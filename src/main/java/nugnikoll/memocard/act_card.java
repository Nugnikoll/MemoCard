package nugnikoll.memocard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class act_card extends AppCompatActivity implements View.OnClickListener{

	database db;
	private String table;
	private int index, table_size;
	enum mode_type{
		mode_normal,
		mode_content
	};
	LinearLayout linear_content, linear_check, linear_true, linear_false, linear_next;
	TextView text_key, text_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_card);

		linear_content = findViewById(R.id.linear_content);
		linear_check = findViewById(R.id.linear_check);
		linear_check.setOnClickListener(this);
		linear_true = findViewById(R.id.linear_true);
		linear_true.setOnClickListener(this);
		linear_false = findViewById(R.id.linear_false);
		linear_false.setOnClickListener(this);
		linear_next = findViewById(R.id.linear_next);
		linear_next.setOnClickListener(this);
		text_key = findViewById(R.id.text_key);
		text_content = findViewById(R.id.text_content);

		app_memo app = (app_memo) getApplication();
		db = app.get_database();
		table = "gre";
		index = 1;
		table_size = db.get_table_size(table);
		set_card(1);
		set_mode(mode_type.mode_normal);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_card, menu);
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
		switch(view.getId()){
		case R.id.linear_check:
			set_mode(mode_type.mode_content);
			break;
		case R.id.linear_true:
			next_card();
			set_mode(mode_type.mode_normal);
			break;
		case R.id.linear_false:
			next_card();
			set_mode(mode_type.mode_normal);
			break;
		case R.id.linear_next:
			next_card();
			set_mode(mode_type.mode_normal);
			break;
		default:
			break;
		}
	}

	protected void set_card(int pos){
		Vector<String> vec = db.get_card(table, pos);
		text_key.setText(vec.get(0).trim());
		text_content.setText(vec.get(1).trim());
	}

	protected void next_card(){
		++index;
		if(index > table_size){
			index = 0;
		}
		set_card(index);
	}

	protected void set_mode(mode_type mode){
		switch (mode){
		case mode_normal:
			linear_content.setVisibility(View.GONE);
			linear_check.setVisibility(View.VISIBLE);
			linear_true.setVisibility(View.GONE);
			linear_false.setVisibility(View.GONE);
			text_key.setMinHeight(600);
			text_content.setMinHeight(0);
			break;
		case mode_content:
			linear_content.setVisibility(View.VISIBLE);
			linear_check.setVisibility(View.GONE);
			text_key.setMinHeight(0);
			text_content.setMinHeight(600);
			break;
		default:
		}
	}
}

