package scse.com.domain;

public class Classroom {
	private String classroomId;
	private String info;
	public String getClassroomId() {
		return classroomId;
	}
	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "Classroom [classroomId=" + classroomId + ", info=" + info + "]";
	}
}
