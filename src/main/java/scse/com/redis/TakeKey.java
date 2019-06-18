package scse.com.redis;

public class TakeKey extends BasePrefix{
	private static final int THREE_DAY = 3600*24*3;
	
	private TakeKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static TakeKey stu_takeIns=new TakeKey(THREE_DAY,"stuTakeIn");
	public static TakeKey stu_takes=new TakeKey(THREE_DAY,"stuTake");
}
