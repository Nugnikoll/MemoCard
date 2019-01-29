package nugnikoll.memocard;

import java.util.Random;

class card implements Comparable<card>{
	public String key;
	public String content;
	public int record;
	public int score;
	public double value;

	public card(String _key, String _content, int _record, int _score){
		key = _key;
		content = _content;
		record = _record;
		score = _score;
		Random rnd = new Random();
		value = score + rnd.nextGaussian() * 0.5;
	}

	@Override
	public int compareTo(card crd) {
		if(value > crd.value){
			return 1;
		}else if(value < crd.value) {
			return -1;
		}else{
			return 0;
		}
	}
}