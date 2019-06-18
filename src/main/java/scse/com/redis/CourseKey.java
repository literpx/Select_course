package scse.com.redis;

public class CourseKey extends BasePrefix{
	private static final int COURSE_GROUP_EXPIRE = 3600*24*3;
	private static final int THIRTY_SECONDS = 30;
	
	
	private CourseKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static CourseKey course_name_list=new CourseKey(COURSE_GROUP_EXPIRE,"nameList");
	public static CourseKey course_id_list=new CourseKey(COURSE_GROUP_EXPIRE,"cidList");
	public static CourseKey course_group_id=new CourseKey(COURSE_GROUP_EXPIRE,"gid");
	public static CourseKey course_count_group=new CourseKey(COURSE_GROUP_EXPIRE,"group_count");
	public static CourseKey course_group_list=new CourseKey(COURSE_GROUP_EXPIRE,"cgList");
	public static CourseKey course_list=new CourseKey(COURSE_GROUP_EXPIRE,"course");
	public static CourseKey select_success=new CourseKey(THIRTY_SECONDS,"success");
	public static CourseKey group=new CourseKey(COURSE_GROUP_EXPIRE,"group");
}
