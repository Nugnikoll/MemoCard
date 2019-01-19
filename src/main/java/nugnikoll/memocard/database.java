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
		Cursor cs = db.rawQuery("select * from `index`", null);
		while(cs.moveToNext()){
			item = cs.getString(cs.getColumnIndex("table"));
			vec_table.add(item);
		}
		cs.close();
		return vec_table;
	}

	public Vector<String> get_table_detail(int pos){
		Vector<String> vec = new Vector<>();
		Cursor cs = db.rawQuery(
			"select * from `index` where id = "
			+ Integer.toString(pos)
			, null
		);
		while(cs.moveToNext()){
			vec.add(cs.getString(cs.getColumnIndex("table")));
			vec.add(cs.getString(cs.getColumnIndex("author")));
			vec.add(cs.getString(cs.getColumnIndex("type")));
			vec.add(cs.getString(cs.getColumnIndex("info")));
		}
		cs.close();
		return vec;
	}

	public void swap(String table, int pos1, int pos2){
		String p1 = Integer.toString(pos1);
		String p2 = Integer.toString(pos2);
		db.execSQL(
			"update `" + table
			+ "` set id = -1 where id = " + p1 + ";"
		);
		db.execSQL(
			"update `" + table
			+ "` set id = " + p1 + " where id = " + p2 + ";"
		);
		db.execSQL(
			"update `" + table
			+ "` set id = " + p2 + " where id = -1;"
		);
	}

	public int get_table_size(String table){
		int result = -1;
		Cursor cs = db.rawQuery(
			"select count(*) from `" + table + "`"
			, null
		);
		while(cs.moveToNext()){
			result = Integer.valueOf(cs.getString(0));
		}
		cs.close();
		return result;
	}

	public Vector<String> get_card(String table, int pos){
		Vector<String> vec = new Vector<>();
		Cursor cs = db.rawQuery(
			"select * from `" + table + "` where _rowid_ = "
			+ Integer.toString(pos)
			, null
		);
		while(cs.moveToNext()){
			vec.add(cs.getString(cs.getColumnIndex("key")));
			vec.add(cs.getString(cs.getColumnIndex("content")));
		}
		cs.close();
		return vec;
	}

	public Vector<String> get_quote(String table, int pos){
		Vector<String> vec = new Vector<>();
		Cursor cs = db.rawQuery(
			"select * from `" + table + "` where _rowid_ = "
			+ Integer.toString(pos)
			, null
		);
		while(cs.moveToNext()){
			vec.add(cs.getString(cs.getColumnIndex("text")));
			vec.add(cs.getString(cs.getColumnIndex("author")));
		}
		cs.close();
		return vec;
	}
}