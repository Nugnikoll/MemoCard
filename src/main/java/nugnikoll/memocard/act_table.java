package nugnikoll.memocard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Vector;

public class act_table extends AppCompatActivity{

	protected database db;
	protected TextView text_table;
	protected ProgressBar prog_learn, prog_review, prog_master;
	protected String table_name;

	@Override
	public void onCreate(Bundle instance){
		super.onCreate(instance);
		setContentView(R.layout.layout_table);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent itt = getIntent();

		table_name = itt.getStringExtra("table_name");
		text_table = findViewById(R.id.text_table);
		text_table.setText(table_name);
		prog_learn = findViewById(R.id.prog_learn);
		prog_review = findViewById(R.id.prog_review);
		prog_master = findViewById(R.id.prog_master);

		app_memo app = (app_memo) getApplication();
		db = app.get_database();
		Vector<Integer> vec = db.get_progress(table_name);
		int sum = vec.get(0) + vec.get(1) + vec.get(2) + vec.get(3);
		int val = vec.get(3);
		prog_master.setProgress(val * 100 / sum);
		val += vec.get(2);
		prog_review.setProgress(val * 100 / sum);
		val += vec.get(1);
		prog_learn.setProgress(val * 100 / sum);
	}
}