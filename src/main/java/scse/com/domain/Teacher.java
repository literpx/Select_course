package scse.com.domain;

public class Teacher {
	private int teacherNo;
	private String teacherName;
	public int getTeacherNo() {
		return teacherNo;
	}
	public void setTeacherNo(int teacherNo) {
		this.teacherNo = teacherNo;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	@Override
	public String toString() {
		return "Teacher [teacherNo=" + teacherNo + ", teacherName=" + teacherName + "]";
	}
}
