package first;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//import first.DataSource;
//import first.UserInfo;


public class UserDAO {

	// 유저 등록 
	public static void registerUser() {
    	
      	DataSource ds = new DataSource();
        Scanner scanner = new Scanner(System.in);
        System.out.print("등록할 사용자 이름을 입력하세요: ");
        String inputUsername = scanner.nextLine();

    
        String sql = "INSERT INTO USERS (USERNAME) VALUES (?)";


        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = ds.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, inputUsername);

            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("사용자 등록 완료 이름: " + inputUsername);
            } else {
            		scanner.close();
                System.out.println("사용자 등록 실패");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
  
            ds.closeConnection(con);
//            scanner.close();
        }
    }
	
	
	
	// username으로 UserInfo 객체 조회 (user_id + username 반환)
    public UserInfo getUserByUsername(String username) {
        String sql = "SELECT user_id, username FROM USERS WHERE username = ?";

        DataSource ds = new DataSource();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserInfo user = new UserInfo();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // 사용자 없음
    }
	
	
	
	// 유저 삭제
    public static void deleteUser() {
        DataSource ds = new DataSource();
        Scanner scanner = new Scanner(System.in);
        System.out.print("삭제할 사용자 이름을 입력하세요: ");
        String usernameToDelete = scanner.nextLine();

        System.out.println(usernameToDelete + " 사용자를 삭제합니다");
        System.out.print("[1] 확인  [0] 뒤로가기: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        if (choice != 1) {
            System.out.println("삭제가 취소되었습니다.");
            scanner.close();
            return;
        }

        String sql = "DELETE FROM USERS WHERE USERNAME = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = ds.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, usernameToDelete);

            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("사용자 삭제 완료: " + usernameToDelete);
            } else {
                System.out.println("해당 이름의 사용자가 존재하지 않습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ds.closeConnection(con);
             scanner.close();
        }
    }
}
