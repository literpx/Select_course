package scse.com.domain;

public class Student {
	private int stuNo;
	private String password;
	private String salt;
	private String stuName;
	private String major;
	private int grade;
	private double haveCredits;
	private double mayCredits;
	public int getStuNo() {
		return stuNo;
	}
	public void setStuNo(int stuNo) {
		this.stuNo = stuNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public double getHaveCredits() {
		return haveCredits;
	}
	public void setHaveCredits(double haveCredits) {
		this.haveCredits = haveCredits;
	}
	public double getMayCredits() {
		return mayCredits;
	}
	public void setMayCredits(double mayCredits) {
		this.mayCredits = mayCredits;
	}
	@Override
	public String toString() {
		return "Student [stuNo=" + stuNo + ", password=" + password + ", salt=" + salt + ", stuName=" + stuName
				+ ", major=" + major + ", grade=" + grade + ", haveCredits=" + haveCredits + ", mayCredits="
				+ mayCredits + "]";
	}
	
}
