package scse.com.redis;
public interface KeyPrefix {
	/*
	 * 超时时间	
	 */
	public int expireSeconds();
	/*
	 * 前缀
	 */
	public String getPrefix();
	
}
