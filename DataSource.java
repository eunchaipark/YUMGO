package first;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
	private static String driverClassName = "oracle.jdbc.OracleDriver"; 
	String url = "jdbc:oracle:thin:@//192.168.0.15:1523/XEPDB1";
    String username = // username;
    String password = //password;

	static {
		try {
			Class.forName(driverClassName);
			System.out.println("드라이버가 로드되었습니다.");
		}catch(Exception e) {
			System.out.println("드라이버 로딩 실패: " + e.getMessage());
		}
	}

	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, username, password);
		}catch(SQLException e) {
			System.out.println("DB 연결 실패: " + e.getMessage());
		}
		return con;
	}

	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch(Exception e) {
				// nothing
			}
		}
	}
}
