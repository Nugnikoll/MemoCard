package nugnikoll.memocard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

public class database{
	private SQLiteDatabase db;

	public database(Context context, String path){
		db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
	}

	public Vector<String> get_table(){
		String item;
		Vector<String> vec_table = new Vector<>();
		Cursor cs = db.rawQuery("SELECT * from `index`", null);
		while(cs.moveToNext()){
			item = cs.getString(cs.getColumnIndex("table"));
			vec_table.add(item);
		}
		cs.close();
		return vec_table;
	}
}
