package nugnikoll.memocard;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class app_memo extends Application{
	protected String tag;
	protected database db;

	@Override
	public void onCreate(){
		super.onCreate();
		tag = "app";
		init_database();
	}

	private void init_database(){
		String str_database = "database";
		String str_name = "memo_card.db";
		String path = File.separator + "data"
			+ Environment.getDataDirectory().getAbsolutePath()
			+ File.separator + getPackageName()
			+ File.separator + str_database
			+ File.separator + str_name;
		File fobj = new File(path);

		if(true){
		//if(!fobj.exists()){
			String str_folder = File.separator + "data"
				+ Environment.getDataDirectory().getAbsolutePath()
				+ File.separator + getPackageName()
				+ File.separator + str_database
				+ File.separator;
			File folder_obj = new File(str_folder);
			if(!folder_obj.exists()){
				folder_obj.mkdirs();
				Log.i(tag, "make a directory for database");
			}
			Log.i(tag, "create a database");
			try{
				InputStream fin = getAssets().open(str_name);
				FileOutputStream fout = new FileOutputStream(fobj);
				int len = -1;
				byte[] buffer = new byte[1024];
				while((len = fin.read(buffer)) != -1){
					fout.write(buffer, 0, len);
					fout.flush();
				}
				fout.close();
				fin.close();
			}catch(IOException err){
				err.printStackTrace();
				System.exit(0);
			}
		}

		db = new database(this, path);
	}

	public database get_database(){
		return db;
	}
}
