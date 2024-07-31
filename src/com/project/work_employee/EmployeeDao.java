package com.project.work_employee;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/work_db";
	private String id = "admin";
	private String pw = "admin";

	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 자원정리 메소드
	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public boolean loginProcess(String id, String pw) throws SQLException {
		UserVo uservo = new UserVo(id, pw); // Ensure UserVo class is defined
		this.getConnection();

		String query = "SELECT login_ID, login_PW FROM employees WHERE login_ID = ? AND login_PW = ?";

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw); 

			rs = pstmt.executeQuery();

			// 4. 결과처리
			return rs.next(); 
		} finally {
			this.close(); 
		}
	}

	public void checkIn(String id, String pw) {

		try {
			this.getConnection();
			// 로그인 ID와 PW로 직원 정보 조회
			String findEmployeeIdQuery = "SELECT employee_id FROM employees WHERE login_ID = ? AND login_PW = ?";
			pstmt = conn.prepareStatement(findEmployeeIdQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				int employeeID = rs.getInt("employee_id");

				// 현재 시간을 가져와서 저장
				LocalDateTime currentTime = LocalDateTime.now();

				// 현재 시간이 9시 이전인지 확인하여 상태를 결정
				String workStatus = currentTime.isBefore(LocalDateTime.of(currentTime.toLocalDate(), LocalTime.of(9, 0))) ? "출근" : "지각";

				// workdate 테이블에 출근 시간과 상태 삽입
				String insertWorkDateQuery = "INSERT INTO workdate (employee_id, start_dateAndtime, work_start_status) VALUES (?, ?, ?)";
				pstmt = conn.prepareStatement(insertWorkDateQuery);
				pstmt.setInt(1, employeeID);
				pstmt.setString(2, currentTime.toString());
				pstmt.setString(3, workStatus);

				pstmt.executeUpdate();
				System.out.println("출근 기록이 성공적으로 추가되었습니다.");
			} else {
				System.out.println("로그인 ID와 PW가 일치하는 사용자를 찾을 수 없습니다.");
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			this.close(); // 자원 정리
		}
	}


	public void checkOut(String id, String pw) {
		try {
			this.getConnection();

			// 로그인 ID와 PW로 직원 정보 조회
			String findEmployeeIdQuery = "SELECT employee_id FROM employees WHERE login_ID = ? AND login_PW = ?";
			pstmt = conn.prepareStatement(findEmployeeIdQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				int employeeID = rs.getInt("employee_id");

				// 가장 최근의 출근 기록을 가져옴
				String findLatestWorkStatusQuery = "SELECT work_end_status FROM workdate WHERE employee_id = ? ORDER BY work_number DESC LIMIT 1";
				pstmt = conn.prepareStatement(findLatestWorkStatusQuery);
				pstmt.setInt(1, employeeID);
				ResultSet rs2 = pstmt.executeQuery();

				String initialStatus = "";
				if (rs2.next()) {
					initialStatus = rs2.getString("work_end_status");
				}

				// 현재 시간을 가져와서 저장
				LocalDateTime currentTime = LocalDateTime.now();

				// 현재 시간이 18시 이전인지 확인하여 상태를 결정
				String workStatus = "퇴근";
				if (currentTime.isBefore(LocalDateTime.of(currentTime.toLocalDate(), LocalTime.of(18, 0)))) {
					workStatus = "조퇴";
				}

				// workdate 테이블에 퇴근 시간과 상태 업데이트
				String updateWorkDateQuery = "UPDATE workdate SET end_dateAndtime = ?, work_end_status = ? WHERE employee_id = ? and work_end_status is null ";
				pstmt = conn.prepareStatement(updateWorkDateQuery);
				pstmt.setString(1, currentTime.toString());
				pstmt.setString(2, workStatus);
				pstmt.setInt(3, employeeID);

				int rowsUpdated = pstmt.executeUpdate();
				if (rowsUpdated > 0) {
					System.out.println("퇴근 기록이 성공적으로 업데이트되었습니다.");
				} else {
					System.out.println("퇴근 기록 업데이트에 실패했습니다.");
				}
			} else {
				System.out.println("로그인 ID와 PW가 일치하는 사용자를 찾을 수 없습니다.");
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			this.close(); // 자원 정리
		}
	}


	public boolean isIdDuplicate(String loginId) {
		boolean isDuplicate = false;
		try {
			this.getConnection();
			String query = "SELECT login_ID FROM employees WHERE login_ID = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, loginId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				isDuplicate = true;
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			this.close();
		}
		return isDuplicate;
	}

	public boolean isPwDuplicate(String loginPw) {
		boolean isDuplicate = false;
		try {
			this.getConnection();
			String query = "SELECT login_PW FROM employees WHERE login_PW = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, loginPw);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				isDuplicate = true;
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			this.close();
		}
		return isDuplicate;
	}

	public boolean isEmailDuplicate(String email) {
		boolean isDuplicate = false;
		try {
			this.getConnection();
			String query = "SELECT email FROM employees WHERE email = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				isDuplicate = true;
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			this.close();
		}
		return isDuplicate;
	}

	public void registerEmployee(UserVo newUser) {
		try {
			this.getConnection();

			// 부서 이름으로 부서 ID 찾기
			String findDepartmentIdQuery = "SELECT department_id FROM department WHERE department_name = ?";
			pstmt = conn.prepareStatement(findDepartmentIdQuery);
			pstmt.setString(1, newUser.getDepartment_name());
			rs = pstmt.executeQuery();

			int departmentID = 0;
			if (rs.next()) {
				departmentID = rs.getInt("department_id");
			} else {
				System.out.println("해당 부서를 찾을 수 없습니다.");
				return;
			}

			// employees 테이블에 데이터 삽입
			String insertEmployeeQuery = "INSERT INTO employees (emp_name, department_id, login_ID, login_PW, email, address, phone_number, hire_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(insertEmployeeQuery);
			pstmt.setString(1, newUser.getName());
			pstmt.setInt(2, departmentID);
			pstmt.setString(3, newUser.getId());
			pstmt.setString(4, newUser.getPw());
			pstmt.setString(5, newUser.getEmail());
			pstmt.setString(6, newUser.getAdress());
			pstmt.setString(7, newUser.getPhonenumber());
			pstmt.setString(8, newUser.getHire_date());

			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("회원 가입이 성공적으로 완료되었습니다.");
			} else {
				System.out.println("회원 가입에 실패했습니다.");
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			this.close(); // 자원 정리
		}
	}



	public UserVo showMyInfo(String id, String pw) {
		UserVo uservo = null;
		this.getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			// *sql문 준비
			String query = "";
			query += " select 	employee_id, emp_name, login_ID, login_PW, email, department_id, phone_number, address, hire_date ";
			query += " from employees ";
			query += " where login_ID = ? and login_PW = ? ";

			// *바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);

			// *실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			// 리스트로 만들기
			while (rs.next()) {
				int emp_ID = rs.getInt("employee_id");
				String name = rs.getString("emp_name");
				String loginID = rs.getString("login_ID");
				String loginPW = rs.getString("login_PW");
				String email = rs.getString("email");
				int departmentID = rs.getInt("department_id");
				String hp = rs.getString("phone_number");
				String addresss = rs.getString("address");
				String date = rs.getString("hire_date");

				uservo = new UserVo(emp_ID,name,departmentID,loginID,loginPW,email,addresss,hp,date);

			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return uservo;

	}

	public boolean updateEmployeeInfo(String loginID, String loginPW, int changeNum, String newValue) {
		boolean updateSuccessful = false;
		String column = "";
		String query = "";
		boolean isDuplicate = false;

		switch (changeNum) {
		case 1:
			column = "emp_name";
			break;
		case 2:
			column = "login_ID";
			if (isIdDuplicate(newValue)) {
				System.out.println("이미 사용 중인 ID입니다.");
				return updateSuccessful;
			}
			break;
		case 3:
			column = "login_PW";
			if (isPwDuplicate(newValue)) {
				System.out.println("이미 사용 중인 비밀번호입니다.");
				return updateSuccessful;
			}
			break;
		case 4:
			column = "email";
			if (isEmailDuplicate(newValue)) {
				System.out.println("이미 사용 중인 이메일입니다.");
				return updateSuccessful;
			}
			break;
		case 5:
			column = "address";
			break;
		case 6:
			column = "phone_number";
			break;
		default:
			System.out.println("잘못된 번호입니다.");
			return updateSuccessful;
		}

		query = "UPDATE employees SET " + column + " = ? WHERE login_ID = ? AND login_PW = ?";

		try {
			this.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, newValue);
			pstmt.setString(2, loginID);
			pstmt.setString(3, loginPW);
			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				updateSuccessful = true;
			} else {
				System.out.println("정보 업데이트에 실패했습니다.");
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			this.close();
		}

		return updateSuccessful;
	}


	public List<WorkVO> showMyHistory (String id, String pw) {
		List<WorkVO> workhs = new ArrayList<>();

		this.getConnection();

		try {
			String query = "";
			query += "select ";
			query += " w.employee_id, ";
			query += " w.start_dateAndtime, ";
			query += " w.work_start_status, ";
			query += " w.end_dateAndtime, ";
			query += " w.work_end_status ";
			query += " from workdate w , (select employee_id from employees where login_ID = ? and login_PW = ?) as e ";
			query += " where w.employee_id = e.employee_id; ";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int empID = rs.getInt("employee_id");
				String startTime = rs.getString("start_dateAndtime");
				String startStatus = rs.getString("work_start_status");
				String endTime = rs.getString("end_dateAndtime");
				String endStatus = rs.getString("work_end_status");
				
				WorkVO workvo = new WorkVO(empID,startTime,endTime,startStatus,endStatus);
				workhs.add(workvo);
			}
		}catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		
		

		return workhs;

	}




}


