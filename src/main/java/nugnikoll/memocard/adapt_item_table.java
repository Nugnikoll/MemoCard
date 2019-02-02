package nugnikoll.memocard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class adapt_item_table extends ArrayAdapter<String> {

	private Context context_table;
	private List<String> list_table;

	public adapt_item_table(
		Context context, int id, List<String> list_str
	){
		super(context, id, list_str);
		context_table = context;
		list_table = list_str;
	}

	@Override
    public View getView(int pos, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context_table
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View result = inflater.inflate(R.layout.layout_item_table, parent, false);
        TextView text_table = (TextView) result.findViewById(R.id.text_table);
        text_table.setText(list_table.get(pos));

        return result;
    }
}
