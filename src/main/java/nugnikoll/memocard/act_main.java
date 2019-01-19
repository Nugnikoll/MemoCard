package nugnikoll.memocard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class act_main extends AppCompatActivity implements View.OnClickListener{

    protected Button button_start, button_database, button_setting;

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
		switch(view.getId()){
        case R.id.button_start:
            //Intent itt = new Intent(this, act_database.class);
            //startActivityForResult(itt, 1);
			break;
		case R.id.button_database:
            Intent itt = new Intent(this, act_database.class);
            startActivityForResult(itt, 1);
			break;
        case R.id.button_setting:
            //Intent itt = new Intent(this, act_database.class);
            //startActivityForResult(itt, 1);
			break;
		default:
			break;
		}
	}
}

