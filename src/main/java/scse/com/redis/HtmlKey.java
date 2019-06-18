package scse.com.redis;

public class HtmlKey extends BasePrefix{

	public static final int ONE_DAY = 3600*24;//一天
	
	public static final int THREE_DAY = 3600*24*3;//一天
	
	private HtmlKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static HtmlKey login=new HtmlKey(ONE_DAY,"login");
	public static HtmlKey start=new HtmlKey(ONE_DAY,"start");
	public static HtmlKey select_course_html=new HtmlKey(ONE_DAY,"selectCourse");
	public static HtmlKey course_html=new HtmlKey(ONE_DAY,"course");
	public static HtmlKey over_html=new HtmlKey(ONE_DAY,"over");
	public static HtmlKey error_html=new HtmlKey(ONE_DAY,"error");
}
