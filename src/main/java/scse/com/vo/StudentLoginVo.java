package scse.com.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class StudentLoginVo {
	@NotNull
	private int stuNo;
	@NotNull
	@Length(min=32)
	private String password;
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
}
