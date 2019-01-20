package nugnikoll.memocard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
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
	ScrollView scroll_card;
	ViewGroup view_normal, view_next;
	Scene scene_normal, scene_next;
	boolean flag_scene;
	LinearLayout[] linear_content, linear_check, linear_true, linear_false, linear_next;
	TextView[] text_key, text_content;
	Transition trans_card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_card);

		scroll_card = findViewById(R.id.scroll_card);
		view_normal = (ViewGroup)getLayoutInflater().inflate(R.layout.scene_card, scroll_card, false);
		scene_normal = new Scene(scroll_card, view_normal);
		view_next = (ViewGroup)getLayoutInflater().inflate(R.layout.scene_card, scroll_card, false);
		scene_next = new Scene(scroll_card, view_next);
		scene_normal.enter();
		flag_scene = false;

		linear_content = new LinearLayout[]{
			view_normal.findViewById(R.id.linear_content),
			view_next.findViewById(R.id.linear_content)
		};
		linear_check = new LinearLayout[]{
			view_normal.findViewById(R.id.linear_check),
			view_next.findViewById(R.id.linear_check)
		};
		linear_check[0].setOnClickListener(this);
		linear_check[1].setOnClickListener(this);
		linear_true = new LinearLayout[]{
			view_normal.findViewById(R.id.linear_true),
			view_next.findViewById(R.id.linear_true)
		};
		linear_true[0].setOnClickListener(this);
		linear_true[1].setOnClickListener(this);
		linear_false = new LinearLayout[]{
			view_normal.findViewById(R.id.linear_false),
			view_next.findViewById(R.id.linear_false)
		};
		linear_false[0].setOnClickListener(this);
		linear_false[1].setOnClickListener(this);
		linear_next = new LinearLayout[]{
			view_normal.findViewById(R.id.linear_next),
			view_next.findViewById(R.id.linear_next)
		};
		linear_next[0].setOnClickListener(this);
		linear_next[1].setOnClickListener(this);
		text_key = new TextView[]{
			view_normal.findViewById(R.id.text_key),
			view_next.findViewById(R.id.text_key)
		};
		text_content = new TextView[]{
			view_normal.findViewById(R.id.text_content),
			view_next.findViewById(R.id.text_content)
		};

		trans_card = TransitionInflater.from(this).inflateTransition(R.transition.trains_card);

		app_memo app = (app_memo) getApplication();
		db = app.get_database();
		table = "gre";
		index = 1;
		table_size = db.get_table_size(table);
		set_card(0, 1);
		set_mode(0, mode_type.mode_normal);
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
			set_card(flag_scene ? 0 : 1, index);
			set_mode(flag_scene ? 0 : 1, mode_type.mode_content);
			break;
		case R.id.linear_true:
			next_card();
			break;
		case R.id.linear_false:
			next_card();
			break;
		case R.id.linear_next:
			next_card();
			break;
		default:
			break;
		}
		next_scene();
	}

	protected void next_scene(){
		TransitionManager.go(flag_scene ? scene_normal : scene_next, trans_card);
	}

	protected void set_card(int index, int pos){
		Vector<String> vec = db.get_card(table, pos);
		text_key[index].setText(vec.get(0).trim());
		text_content[index].setText(vec.get(1).trim());
	}

	protected void next_card(){
		++index;
		if(index > table_size){
			index = 0;
		}
		set_card(flag_scene ? 0 : 1, index);
		set_mode(flag_scene ? 0 : 1, mode_type.mode_normal);
	}

	protected void set_mode(int index, mode_type mode){
		switch (mode){
		case mode_normal:
			linear_content[index].setVisibility(View.GONE);
			linear_check[index].setVisibility(View.VISIBLE);
			linear_true[index].setVisibility(View.GONE);
			linear_false[index].setVisibility(View.GONE);
			text_key[index].setMinHeight(600);
			text_content[index].setMinHeight(0);
			break;
		case mode_content:
			linear_content[index].setVisibility(View.VISIBLE);
			linear_check[index].setVisibility(View.GONE);
			linear_true[index].setVisibility(View.GONE);
			linear_false[index].setVisibility(View.GONE);
			text_key[index].setMinHeight(0);
			text_content[index].setMinHeight(600);
			break;
		default:
		}
	}
}

