package com.project.work_employee;

public class DepartmentVO {
	private int departmentID;
	private String departmentName;
	private int managerID;
	
	public DepartmentVO(int departmentID, String departmentName, int managerID) {
		this.departmentID = departmentID;
		this.departmentName = departmentName;
		this.managerID = managerID;
	}

	public int getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int getManagerID() {
		return managerID;
	}

	public void setManagerID(int managerID) {
		this.managerID = managerID;
	}

	@Override
	public String toString() {
		return "DepartmentVO [departmentID=" + departmentID + ", departmentName=" + departmentName + ", managerID="
				+ managerID + "]";
	}
	
	
	
	
}
