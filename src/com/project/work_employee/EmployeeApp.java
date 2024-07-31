package com.project.work_employee;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EmployeeApp {

	public static void main(String[] args) throws SQLException {

		//DAO 생성자
		EmployeeDao employeedao = new EmployeeDao();
		
		//스캐너 오픈
		Scanner sc = new Scanner(System.in);

		String login_id;
		String login_pw;

		while (true) {
			//메뉴 출력
			System.out.println("-------------------------------------------");
			System.out.println("|     1.출근        2. 회원가입       3. 종료  |");
			System.out.println("-------------------------------------------");
			System.out.print("번호를 입력하세요 >> ");

			//입력 값 받고 값에 따른 코드 진행

			int input = sc.nextInt();
			sc.nextLine(); // Consume newline left-over
			if (input == 1) {
				//로그인 프로세스
				boolean login = false;
				do {
					System.out.print("ID: ");
					login_id = sc.nextLine().trim();
					System.out.print("PW: ");
					login_pw = sc.nextLine().trim();
					login = employeedao.loginProcess(login_id, login_pw);
					if (!login) {
						System.out.println("아이디 혹은 비밀번호가 틀렸습니다. 다시 입력해주세요.");
					} else {
						employeedao.checkIn(login_id, login_pw);
						System.out.println("로그인 성공!");
					}
				} while (!login);

				//로그인 성공후
				int input01;
				boolean shouldExit = false; // 종료 여부를 나타내는 플래그
				while(true) {
					
					System.out.println("-------------------------------------------");
					System.out.println("|1.히스토리    2.내 정보 확인 및 수정    3. 종료  |");
					System.out.println("-------------------------------------------");
					System.out.print("번호를 입력하세요 >> ");
					input01 = sc.nextInt();
					sc.nextLine(); // Consume newline left-over
					if (input01 == 1) {
						//히스토리 출력 
						List<WorkVO> workhs = employeedao.showMyHistory(login_id, login_pw); 
						for(int i = 0; i < workhs.size(); i ++) {
							workhs.get(i).showMyHistory();
						}
		
					} else if (input01 == 2) {
						//내정보 출력
						employeedao.showMyInfo(login_id, login_pw).showInfo();
						System.out.println("수정하고 싶은 번호를 입력해주세요. ");
						System.out.println("#으로 표시된 곳은 변경 불가능합니다. ");
						System.out.println("변경 종료는 0번");
						System.out.println(">> ");
						int changeNum = sc.nextInt();
						sc.nextLine(); 
		                
						 // 내 정보 출력
		                while (changeNum != 0) {
		                    System.out.print("변경 값: ");
		                    String newValue = sc.nextLine().trim();

		                    boolean success = employeedao.updateEmployeeInfo(login_id, login_pw, changeNum, newValue);
		                    if (success) {
		                        System.out.println("정보가 성공적으로 업데이트되었습니다.");
		                    } else {
		                        System.out.println("정보 업데이트에 실패했습니다.");
		                    }

		                    // 다시 수정할 번호 입력 받기
		                    System.out.println("수정하고 싶은 번호를 입력해주세요.");
		                    System.out.println("#으로 표시된 곳은 변경 불가능합니다.");
		                    System.out.println("변경 종료는 0번");
		                    System.out.println(">> ");
		                    changeNum = sc.nextInt();
		                    sc.nextLine(); // Consume newline left-over
		                }
						
					} else if (input01 == 3) {
						//종료 프로세스
						shouldExit = true;
						employeedao.checkOut(login_id, login_pw);
						System.out.println("로그아웃");
						break;
					} else {
						System.out.println("번호를 다시 입력해주세요.");
					}
				}

				if(input01 == 3) {
					//종료 프로세스
					if(shouldExit) {
						break;
					}else {
						employeedao.checkOut(login_id, login_pw);
						System.out.println("로그아웃");
						break;
					}
					
				}

			} else if (input == 2) {
				// 회원가입 관련 코드 삽입
				System.out.println("회원가입을 시작합니다.");

				System.out.print("이름: ");
				String name = sc.nextLine().trim();

				System.out.println("부서 이름으로 입력해주세요. ");
				System.out.print("부서 이름(1.HR 2.Operations 3.Marketing): ");
				String departmentName = sc.nextLine().trim();

				//아이디 중복확인
				String loginID;
                do {
                    System.out.print("로그인 ID: ");
                    loginID = sc.nextLine().trim();
                    if (employeedao.isIdDuplicate(loginID)) {
                        System.out.println("이미 사용 중인 ID입니다. 다른 ID를 입력하세요.");
                    }
                } while (employeedao.isIdDuplicate(loginID));

                // 비밀번호 중복 확인
                String loginPW;
                do {
                    System.out.print("로그인 PW: ");
                    loginPW = sc.nextLine().trim();
                    if (employeedao.isPwDuplicate(loginPW)) {
                        System.out.println("이미 사용 중인 비밀번호입니다. 다른 비밀번호를 입력하세요.");
                    }
                } while (employeedao.isPwDuplicate(loginPW));

                // 이메일 중복 확인
                String email;
                do {
                    System.out.print("이메일: ");
                    email = sc.nextLine().trim();
                    if (employeedao.isEmailDuplicate(email)) {
                        System.out.println("이미 사용 중인 이메일입니다. 다른 이메일을 입력하세요.");
                    }
                } while (employeedao.isEmailDuplicate(email));

				System.out.print("주소: ");
				String address = sc.nextLine().trim();

				System.out.print("전화번호: ");
				String phoneNumber = sc.nextLine().trim();

				System.out.print("입사 날짜 (YYYY-MM-DD): ");
				String hireDate = sc.nextLine().trim();

				// UserVo 객체 생성
				UserVo newUser = new UserVo(name, departmentName, loginID, loginPW, email, address, phoneNumber, hireDate);
				 // 회원가입 메소드 호출
                employeedao.registerEmployee(newUser);

			} else if (input == 3) {
				//종료 프로세스
				System.out.println("종료");
				break;
			} else {
				System.out.println("번호를 다시 입력해주세요.");
			}
		}
	}
}
