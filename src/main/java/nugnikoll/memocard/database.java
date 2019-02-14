package nugnikoll.memocard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

public class database{
	protected SQLiteDatabase db;

	public database(Context context, String path){
		db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
	}

	public Vector<String> get_index(){
		String item;
		Vector<String> vec_table = new Vector<>();
		Cursor cs = db.rawQuery("select * from `index` order by `table`", null);
		while(cs.moveToNext()){
			item = cs.getString(cs.getColumnIndex("table"));
			vec_table.add(item);
		}
		cs.close();
		return vec_table;
	}

	public Vector<String> get_table(String table_name){
		Vector<String> vec = new Vector<>();
		Cursor cs = db.rawQuery(
			"select * from `index` where `table` = \""
			+ table_name + "\""
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

	public void insert_table(String table_name, String info){
		db.execSQL(
			"insert into `index` (`table`, `author`, `type`, `info`) values ('"
			+ table_name + "', 'user', 'card', '"
			+ info + "');"
		);
		db.execSQL(
			"create table if not exists `"
			+ table_name
			+ "` ( `key` text, `content` text, `record` integer, `score` integer, primary key(`key`));"
		);
	}

	public void delete_table(String table_name){
		db.execSQL(
			"delete from `index` where `table` = \"" + table_name + "\";"
		);
		db.execSQL(
			"drop table `" + table_name + "`;"
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

	public Vector<card> get_card(String table){
		Vector<card> vec = new Vector<>();
		Cursor cs = db.rawQuery(
			"select * from `" + table + "`"
			, null
		);
		String key, content;
		Integer record, score;
		while(cs.moveToNext()){
			key = cs.getString(cs.getColumnIndex("key"));
			content = cs.getString(cs.getColumnIndex("content"));
			try{
				record = cs.getInt(cs.getColumnIndex("record"));
				score = cs.getInt(cs.getColumnIndex("score"));
			}catch(Exception err){
				record = 0;
				score = 0;
			}
			vec.add(new card(key, content, record, score));
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

	void save_record(String table_name, Vector<card> vec){
		for(card crd: vec){
			db.execSQL(
				"update `" + table_name + "` set `record` = "
				+ crd.record + ", `score` = "
				+ crd.score + " where `key` = '" + crd.key + "';"
			);
		}
	}

	Vector<Integer> get_progress(String table_name){
		Vector<Integer> vec = new Vector<>();
		Cursor cs = db.rawQuery(
			"select count(*) from `" + table_name + "` where `score` is null"
			, null
		);
		while(cs.moveToNext()){
			vec.add(cs.getInt(0));
		}
		cs.close();
		cs = db.rawQuery(
			"select count(*) from `" + table_name + "` where `score` <= 0"
			, null
		);
		while(cs.moveToNext()){
			vec.add(cs.getInt(0));
		}
		cs.close();
		cs = db.rawQuery(
			"select count(*) from `" + table_name + "` where `score` > 0 and `score` <= 3"
			, null
		);
		while(cs.moveToNext()){
			vec.add(cs.getInt(0));
		}
		cs.close();
		cs = db.rawQuery(
			"select count(*) from `" + table_name + "` where `score` > 3"
			, null
		);
		while(cs.moveToNext()){
			vec.add(cs.getInt(0));
		}
		cs.close();
		return vec;
	}
}
