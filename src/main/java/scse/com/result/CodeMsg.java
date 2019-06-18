package scse.com.result;


public class CodeMsg {
	private int code;
	private String msg;
	
	//通用错误码
	public static final CodeMsg SUCCESS=new CodeMsg(0,"success");
	public static final CodeMsg SERVER_ERROR=new CodeMsg(500100,"服务端错误");
	public static final CodeMsg BIND_ERROR=new CodeMsg(500101,"参数校验异常：%s");
	
	//登陆模块
	public static final CodeMsg STUDENT_NOT_EXIST=new CodeMsg(500201,"学号不存在");
	public static final CodeMsg PASSWORD_ERROR=new CodeMsg(500202,"密码错误");
	public static final CodeMsg SESSION_ERROR=new CodeMsg(500203,"session不存在或已失效，请重新登陆");
	
	//课程模块
	public static final CodeMsg COURSE_NOT_EXIST=new CodeMsg(500301,"课程不存在");
	public static final CodeMsg COURSE_IS_SELECT=new CodeMsg(500302,"课程不能重复选择");
	public static final CodeMsg COURSE_CHANGE=new CodeMsg(500303,"是否需要调换课程?");
	public static final CodeMsg COURSE_IS_CONFLICT=new CodeMsg(500304,"课程有冲突");
	public static final CodeMsg CREDITS_INSUFFICIENT=new CodeMsg(500305,"可用学分不足");
	public static final CodeMsg CREDITS_IS_FULL=new CodeMsg(500306,"该课程人已满");
	
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		//参数追加
		String message = String.format(this.msg, args); 
		return new CodeMsg(code, message);
	}
	
	private CodeMsg(int code,String msg) {
		this.code=code;
		this.msg=msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
