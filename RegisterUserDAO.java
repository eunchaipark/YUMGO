package first;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;


public class RegisterUserDAO {

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
                System.out.println("사용자 등록 실패");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
  
            ds.closeConnection(con);
            scanner.close();
        }
    }
}
