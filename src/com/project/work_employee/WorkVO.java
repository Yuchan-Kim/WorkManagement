package com.project.work_employee;

public class WorkVO {
	private int workNum;
	private int employeeID;
	private String start_AndTime;
	private String end_AndTime;
	private String workStartStatus;
	private String workEndStatus;
	
	
	public WorkVO() {
		
	}
	
	public WorkVO(int employeeID, String start_AndTime, String end_AndTime, String workStartStatus,
			String workEndStatus) {
		this.employeeID = employeeID;
		this.start_AndTime = start_AndTime;
		this.end_AndTime = end_AndTime;
		this.workStartStatus = workStartStatus;
		this.workEndStatus = workEndStatus;
	}
	public int getWorkNum() {
		return workNum;
	}
	public void setWorkNum(int workNum) {
		this.workNum = workNum;
	}
	public int getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	public String getStart_AndTime() {
		return start_AndTime;
	}
	public void setStart_AndTime(String start_AndTime) {
		this.start_AndTime = start_AndTime;
	}
	public String getEnd_AndTime() {
		return end_AndTime;
	}
	public void setEnd_AndTime(String end_AndTime) {
		this.end_AndTime = end_AndTime;
	}
	public String getWorkStartStatus() {
		return workStartStatus;
	}
	public void setWorkStartStatus(String workStartStatus) {
		this.workStartStatus = workStartStatus;
	}
	public String getWorkEndStatus() {
		return workEndStatus;
	}
	public void setWorkEndStatus(String workEndStatus) {
		this.workEndStatus = workEndStatus;
	}

	
	public void showMyHistory() {
		System.out.println("********** [ 근무일지 ] **********");
		System.out.println("직원번호: " + employeeID);
		System.out.println("출근시간: " + start_AndTime);
		System.out.println("상태: " + workStartStatus);
		System.out.println("퇴근시간: " + end_AndTime);
		System.out.println("상태: " + workEndStatus);
		
	}
	
	
	
}
