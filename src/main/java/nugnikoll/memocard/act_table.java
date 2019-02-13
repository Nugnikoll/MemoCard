package nugnikoll.memocard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

public class act_table extends AppCompatActivity{

	private TextView text_table;
	private ProgressBar prog_learn, prog_review, prog_master;
	private String table_name;

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
	}
}