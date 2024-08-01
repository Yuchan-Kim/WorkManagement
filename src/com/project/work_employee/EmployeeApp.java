package com.project.work_employee;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EmployeeApp {

    public static void main(String[] args) throws SQLException {

        EmployeeDao employeedao = new EmployeeDao();
        Scanner sc = new Scanner(System.in);

        String login_id;
        String login_pw;

        while (true) {
            // Main menu
            System.out.println();
            System.out.println("===========================================");
            System.out.println("|     1. 출근       2. 회원가입       3. 종료  |");
            System.out.println("===========================================");
            System.out.print("번호를 입력하세요 >> ");

            int input = sc.nextInt();
            sc.nextLine(); // Consume newline left-over

            if (input == 1) {
                boolean login = false;
                do {
                    // Login process
                    System.out.println();
                    System.out.println("--------------- ID 입력 ----------------");
                    System.out.print(">> ID: ");
                    login_id = sc.nextLine().trim();
                    System.out.println("--------------- PW 입력 ----------------");
                    System.out.print(">> PW: ");
                    login_pw = sc.nextLine().trim();
                    System.out.println();

                    login = employeedao.loginProcess(login_id, login_pw);
                    if (!login) {
                        System.out.println("=================== Warning ===================");
                        System.out.println("   아이디 혹은 비밀번호가 틀렸습니다. 다시 입력해주세요.   ");
                        System.out.println("===============================================");
                    } else {
                        employeedao.checkIn(login_id, login_pw);			
                        System.out.println("============= Success =============");
                        System.out.println("             로그인 성공!            ");
                        System.out.println("===================================");
                    }
                } while (!login);

                int input01;
                boolean shouldExit = false;

                while (true) {
                    // Logged-in menu
                    System.out.println("===========================================");
                    System.out.println("| 1. 히스토리  2. 내 정보 확인 및 수정  3. 종료   |");
                    System.out.println("===========================================");
                    System.out.print("번호를 입력하세요 >> ");
                    input01 = sc.nextInt();
                    sc.nextLine(); // Consume newline left-over

                    if (input01 == 1) {
                        // Display work history
                        List<WorkVO> workhs = employeedao.showMyHistory(login_id, login_pw); 
                        for (WorkVO work : workhs) {
                            work.showMyHistory();
                        }

                    } else if (input01 == 2) {
                        // Display and update personal information
                        employeedao.showMyInfo(login_id, login_pw).showInfo();
                        System.out.println();
                        System.out.println("============= Warning =============");
                        System.out.println("    수정하고 싶은 번호를 입력해주세요.     ");
                        System.out.println("   #으로 표시된 곳은 변경 불가능합니다.    ");
                        System.out.println("===================================");
                        System.out.println("변경 종료는 0번");
                        System.out.print(">> ");

                        int changeNum = sc.nextInt();
                        sc.nextLine(); // Consume newline left-over

                        while (changeNum != 0) {
                            System.out.print("변경 값: ");
                            String newValue = sc.nextLine().trim();

                            boolean success = employeedao.updateEmployeeInfo(login_id, login_pw, changeNum, newValue);
                            if (success) {
                                System.out.println("============= Success =============");
                                System.out.println("   정보가 성공적으로 업데이트되었습니다.   ");
                                System.out.println("===================================");
                            } else {
                                System.out.println("============= Warning =============");
                                System.out.println("       정보 업데이트에 실패했습니다.       ");
                                System.out.println("===================================");
                            }

                            System.out.println("===================================");
                            System.out.println("      수정하고 싶은 번호를 입력해주세요.    ");
                            System.out.println("     #으로 표시된 곳은 변경 불가능합니다.   ");
                            System.out.println("===================================");
                            System.out.println("변경 종료는 0번");
                            System.out.print(">> ");
                            changeNum = sc.nextInt();
                            sc.nextLine(); // Consume newline left-over
                        }

                    } else if (input01 == 3) {
                        // Logout process
                        shouldExit = true;
                        employeedao.checkOut(login_id, login_pw);
                        System.out.println();
                        System.out.println("===================================");
                        System.out.println("               로그아웃              ");
                        System.out.println("===================================");
                        break;
                    } else {
                        System.out.println("============= Warning =============");
                        System.out.println("         번호를 다시 입력해주세요.         ");
                        System.out.println("===================================");
                    }
                }

                if (shouldExit) {
                    break;
                }

            } else if (input == 2) {
                // Registration process
                System.out.println();
                System.out.println("===================================");
                System.out.println("           회원가입을 시작합니다.            ");
                System.out.println("===================================");

                System.out.print("이름: ");
                String name = sc.nextLine().trim();
                System.out.println("===================================");
                System.out.print("부서 이름(1.HR 2.Operations 3.Marketing): ");
                String departmentName = sc.nextLine().trim();

                // Check for duplicate ID
                String loginID;
                do {
                    System.out.println("===================================");
                    System.out.print("로그인 ID: ");
                    loginID = sc.nextLine().trim();
                    if(employeedao.isIdDuplicate(loginID)) {
                    	 System.out.println("─────────────────────────────────────────────────────");
                         System.out.println(" ⚠️ 이미 사용 중인 아이디입니다: " + loginID );
                         System.out.println("─────────────────────────────────────────────────────");
                    } else {
                        System.out.println("─────────────────────────────────────────────────────");
                        System.out.println(" ✅ 사용 가능한 아이디입니다: " + loginID );
                        System.out.println("─────────────────────────────────────────────────────");
                    }
                     
                } while (employeedao.isIdDuplicate(loginID));

                // Check for duplicate password
                String loginPW;
                do {
                    System.out.println("===================================");
                    System.out.print("로그인 PW: ");
                    loginPW = sc.nextLine().trim();
                    if (employeedao.isPwDuplicate(loginPW)) {
                    	System.out.println("────────────────────────────────────────");
                        System.out.println("       ⚠️ 이미 사용 중인 비밀번호입니다.        ");
                        System.out.println("────────────────────────────────────────");
                    }else {
                    	 System.out.println("────────────────────────────────────────");
                         System.out.println("        ✅ 사용 가능한 비밀번호입니다.        ");
                         System.out.println("────────────────────────────────────────");
                    }
                } while (employeedao.isPwDuplicate(loginPW));

                // Check for duplicate email
                String email;
                do {
                    System.out.println("===================================");
                    System.out.print("이메일: ");
                    email = sc.nextLine().trim();
                    if (employeedao.isEmailDuplicate(email)) {
                    	 System.out.println("─────────────────────────────────────────────────────");
                         System.out.println(" ⚠️ 이미 사용 중인 이메일입니다: " + email );
                         System.out.println("─────────────────────────────────────────────────────");
                    }else {
                    	 System.out.println("─────────────────────────────────────────────────────");
                         System.out.println(" ✅ 사용 가능한 이메일입니다: " + email );
                         System.out.println("─────────────────────────────────────────────────────");
                    }
                } while (employeedao.isEmailDuplicate(email));

                System.out.println("===================================");
                System.out.print("주소: ");
                String address = sc.nextLine().trim();

                System.out.println("===================================");
                System.out.print("전화번호: ");
                String phoneNumber = sc.nextLine().trim();

                System.out.println("===================================");
                System.out.print("입사 날짜 (YYYY-MM-DD): ");
                String hireDate = sc.nextLine().trim();

                // Create new user
                UserVo newUser = new UserVo(name, departmentName, loginID, loginPW, email, address, phoneNumber, hireDate);
                employeedao.registerEmployee(newUser);

            } else if (input == 3) {
                // Exit process
                System.out.println("============= LOG OFF =============");
                System.out.println("                 종료                  ");
                System.out.println("===================================");
                break;
            } else {
                System.out.println("============= Warning =============");
                System.out.println("         번호를 다시 입력해주세요.        ");
                System.out.println("===================================");
            }
        }
    }
}
