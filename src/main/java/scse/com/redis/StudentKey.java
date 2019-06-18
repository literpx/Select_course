package scse.com.redis;

public class StudentKey extends BasePrefix{
	
	public static final int TOKEN_EXPIRE = 3600*24;//一天
	
	private StudentKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static StudentKey token=new StudentKey(TOKEN_EXPIRE,"tk");
}
