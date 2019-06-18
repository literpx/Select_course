package scse.com.redis;

public class TimeKey extends BasePrefix{

	private static final int TIME_EXPIRE = 3600*24*3;
	
	private TimeKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static TimeKey time_obj=new TimeKey(TIME_EXPIRE,"time");
	public static TimeKey week_obj=new TimeKey(TIME_EXPIRE,"week");
	public static TimeKey deadline_obj=new TimeKey(TIME_EXPIRE,"deadline");
}
