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
import java.util.Random;
import java.util.Vector;

public class act_card extends AppCompatActivity implements View.OnClickListener{

	protected database db;
	protected ScrollView scroll_card;
	protected ViewGroup view_normal;
	protected Scene scene_normal;
	protected RelativeLayout linear_key;
	protected LinearLayout linear_content, linear_true, linear_false;
	protected TextView[] text_key;
	protected TextView text_content, text_false;

	boolean flag_key;
	protected String table;
	protected int index, table_size;
	protected enum mode_type{
		mode_normal,
		mode_content
	}
	protected mode_type mode_card;
	class card{
		public int index;
		public int record;

		public card(int _index){
			index = _index;
			record = 0;
		}
	}
	protected Vector<card> vec_card;
	protected TransitionSet transet_normal, transet_simple;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_card);

		scroll_card = findViewById(R.id.scroll_card);
		view_normal = (ViewGroup)getLayoutInflater().inflate(R.layout.scene_card, scroll_card, false);
		scene_normal = new Scene(scroll_card, view_normal);
		scene_normal.enter();

		linear_key = view_normal.findViewById(R.id.linear_key);
		linear_content = view_normal.findViewById(R.id.linear_content);
		linear_true = view_normal.findViewById(R.id.linear_true);
		linear_true.setOnClickListener(this);
		linear_false = view_normal.findViewById(R.id.linear_false);
		linear_false.setOnClickListener(this);
		text_key = new TextView[]{
			view_normal.findViewById(R.id.text_key1),
			view_normal.findViewById(R.id.text_key2)
		};
		text_content = findViewById(R.id.text_content);
		text_false = findViewById(R.id.text_false);

		app_memo app = (app_memo) getApplication();
		db = app.get_database();
		SharedPreferences prefer = getSharedPreferences("config", MODE_PRIVATE);
		table = prefer.getString("select_table", "");

		flag_key = false;
		index = 0;
		table_size = db.get_table_size(table);
		vec_card = new Vector<>();
		for(int i = 0; i != table_size; ++i){
			vec_card.add(new card(i + 1));
		}
		Collections.shuffle(vec_card);

		set_card();
		set_mode(mode_type.mode_normal);

		transet_normal = new TransitionSet()
			.setOrdering(TransitionSet.ORDERING_SEQUENTIAL)
			.addTransition(new Fade(Fade.OUT).setDuration(0))
            .addTransition(new ChangeBounds().setDuration(500))
			.addTransition(new Fade(Fade.IN).setDuration(500));

		transet_simple = new TransitionSet()
			.setOrdering(TransitionSet.ORDERING_SEQUENTIAL)
			.addTransition(new Fade(Fade.OUT).setDuration(500))
            .addTransition(new ChangeBounds().setDuration(500))
			.addTransition(new Fade(Fade.IN).setDuration(500));
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
		if(mode_card == mode_type.mode_normal && view.getId() == R.id.linear_true){
			TransitionManager.beginDelayedTransition(view_normal, transet_simple);
		}else{
			TransitionManager.beginDelayedTransition(view_normal, transet_normal);
		}
		switch(view.getId()){
		case R.id.linear_true:
			next_card(true);
			break;
		case R.id.linear_false:
			if(mode_card == mode_type.mode_normal){
				set_card();
				set_mode(mode_type.mode_content);
			}else{
				next_card(false);
			}
			break;
		default:
			break;
		}
	}

	protected void set_card(){
		Vector<String> vec = db.get_card(table, vec_card.get(0).index);
		text_key[flag_key ? 0 : 1].setVisibility(View.INVISIBLE);
		text_key[flag_key ? 1 : 0].setText(vec.get(0).trim());
		text_key[flag_key ? 1 : 0].setVisibility(View.VISIBLE);
		flag_key = !flag_key;
		text_content.setText(vec.get(1).trim());
	}

	protected void next_card(boolean flag){
		card crd = vec_card.get(0);
		if(flag){
			if(crd.record < 5){
				++crd.record;
			}
		}else{
			crd.record = 0;
		}
		vec_card.remove(0);
		int num = vec_card.size();
		int pos;
		Random rnd = new Random();
		switch(crd.record){
		case 0:
		case 1:
			pos = rnd.nextInt(5) + 5;
			break;
		case 2:
			pos = rnd.nextInt(10) + 10;
			break;
		case 3:
			pos = rnd.nextInt(15) + 20;
			break;
		case 4:
			pos = rnd.nextInt(15) + 35;
			break;
		case 5:
			pos = num;
			break;
		default:
			pos = 0;
			Log.e("card", "unexpected pos value");
			break;
		}
		if(pos > num){
			pos = num;
		}
		vec_card.insertElementAt(crd, pos);
		set_card();
		set_mode(mode_type.mode_normal);
	}

	protected void set_mode(mode_type mode){
		mode_card = mode;
		switch (mode){
		case mode_normal:
			text_key[0].setMinHeight(600);
			text_key[1].setMinHeight(600);
			text_content.setMinHeight(0);
			text_content.setVisibility(View.GONE);
			text_false.setText("Check answer");
			break;
		case mode_content:
			text_key[0].setMinHeight(0);
			text_key[1].setMinHeight(0);
			text_content.setMinHeight(500);
			text_content.setVisibility(View.VISIBLE);
			text_false.setText("I don't know");
			break;
		default:
		}
	}
}

