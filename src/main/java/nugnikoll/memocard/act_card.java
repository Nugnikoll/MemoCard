package nugnikoll.memocard;

import android.animation.TimeInterpolator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Vector;

public class act_card extends AppCompatActivity implements View.OnClickListener{

	protected database db;
	protected ScrollView scroll_card;
	protected ViewGroup view_normal;
	protected Scene scene_normal;
	protected RelativeLayout linear_key;
	protected LinearLayout linear_content, linear_true, linear_false, linear_next;
	protected TextView[] text_key;
	protected TextView text_content;

	boolean flag_key;
	protected String table;
	protected int index, table_size;
	protected enum mode_type{
		mode_normal,
		mode_content
	}
	protected Vector<Integer> vec_index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_card);

		scroll_card = findViewById(R.id.scroll_card);
		view_normal = (ViewGroup)getLayoutInflater().inflate(R.layout.scene_card, scroll_card, false);
		scene_normal = new Scene(scroll_card, view_normal);
		scene_normal.enter();

		linear_key = view_normal.findViewById(R.id.linear_key);
		linear_key.setOnClickListener(this);
		linear_content = view_normal.findViewById(R.id.linear_content);
		linear_true = view_normal.findViewById(R.id.linear_true);
		linear_true.setOnClickListener(this);
		linear_false = view_normal.findViewById(R.id.linear_false);
		linear_false.setOnClickListener(this);
		linear_next = view_normal.findViewById(R.id.linear_next);
		linear_next.setOnClickListener(this);
		text_key = new TextView[]{
			view_normal.findViewById(R.id.text_key1),
			view_normal.findViewById(R.id.text_key2)
		};
		text_content = findViewById(R.id.text_content);

		app_memo app = (app_memo) getApplication();
		db = app.get_database();
		SharedPreferences prefer = getSharedPreferences("config", MODE_PRIVATE);
		table = prefer.getString("select_table", "");

		flag_key = false;
		index = 0;
		table_size = db.get_table_size(table);
		vec_index = new Vector<>();
		for(int i = 0; i != table_size; ++i){
			vec_index.add(i + 1);
		}
		Collections.shuffle(vec_index);

		set_card(index);
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
			case R.id.action_back:
				finish();
				return true;
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
		TransitionSet transet = new TransitionSet()
			.setOrdering(TransitionSet.ORDERING_SEQUENTIAL)
			.addTransition(new Fade(Fade.OUT).setDuration(0))
            .addTransition(new ChangeBounds().setDuration(500))
			.addTransition(new Fade(Fade.IN).setDuration(500));
		TransitionManager.beginDelayedTransition(view_normal, transet);
		switch(view.getId()){
		case R.id.linear_key:
			set_card(index);
			set_mode(mode_type.mode_content);
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
	}

	protected void set_card(int pos){
		Vector<String> vec = db.get_card(table, vec_index.get(pos));
		text_key[flag_key ? 0 : 1].setVisibility(View.INVISIBLE);
		text_key[flag_key ? 1 : 0].setText(vec.get(0).trim());
		text_key[flag_key ? 1 : 0].setVisibility(View.VISIBLE);
		flag_key = !flag_key;
		text_content.setText(vec.get(1).trim());
	}

	protected void next_card(){
		++index;
		if(index >= table_size){
			index = 0;
		}
		set_card(index);
		set_mode(mode_type.mode_normal);
	}

	protected void set_mode(mode_type mode){
		switch (mode){
		case mode_normal:
			text_key[0].setMinHeight(700);
			text_key[1].setMinHeight(700);
			text_content.setMinHeight(0);
			text_content.setVisibility(View.GONE);
			break;
		case mode_content:
			text_key[0].setMinHeight(0);
			text_key[1].setMinHeight(0);
			text_content.setMinHeight(600);
			text_content.setVisibility(View.VISIBLE);
			break;
		default:
		}
	}
}

