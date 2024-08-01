package com.project.work_employee;

public class UserVo {

	//필드
	private int empID;
	private String name;
	private int department_id;
	private String department_name;
	private String id;
	private String pw;
	private String email;
	private String address;
	private String phonenumber;
	private String hire_date;

	//생성자
	public UserVo(String id, String pw) {  
		this.id = id;
		this.pw = pw;
	}

	public UserVo(String name, String daprtment_name, String id, String pw, String email, String adress, String phonenumber,
			String hire_date) {
		this.name = name;
		this.department_name = daprtment_name;
		this.id = id;
		this.pw = pw;
		this.email = email;
		this.address = adress;
		this.hire_date = hire_date;
		this.phonenumber = phonenumber;
	}
	
	public UserVo(int empID,String name, int department_id, String id, String pw, String email, String adress, String phonenumber,
			String hire_date) {
		this.empID = empID;
		this.name = name;
		this.department_id = department_id;
		this.id = id;
		this.pw = pw;
		this.email = email;
		this.address = adress;
		this.hire_date = hire_date;
		this.phonenumber = phonenumber;
	}

	//메소드 gs

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdress() {
		return address;
	}

	public void setAdress(String adress) {
		this.address = adress;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getHire_date() {
		return hire_date;
	}

	public void setHire_date(String hire_date) {
		this.hire_date = hire_date;
	}
	
	

	public int getEmpID() {
		return empID;
	}

	public void setEmpID(int empID) {
		this.empID = empID;
	}

	public int getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}


	public void showInfo() {
		System.out.println("┌------------ [ 내 정보 ] -------------");
		System.out.println("| 직원 아이디: " + "empID");
		System.out.println("| 부서 아이디: " + department_id);
		System.out.println("| 입사일: " + hire_date);
		System.out.println("|");
		System.out.println("| 1.이름: " + name);
		System.out.println("| 2.로그인 아이디: " + id);
		System.out.println("| 3.로그인 비밀번호: " + pw);
		System.out.println("| 4.이메일: " + email);
		System.out.println("| 5.주소: " + address);
		System.out.println("| 6.전화번호: " + phonenumber);
		
		System.out.println();
		
	}

	
	
}


