package scse.com.access;

import scse.com.domain.Student;

public class StudentContext {
	
	private static ThreadLocal<Student> studentHolder=new ThreadLocal<Student>();
	
	public static void setStdent(Student student) {
		studentHolder.set(student);
	}
	
	public static Student getStudent() {
		return studentHolder.get();
	}
}
