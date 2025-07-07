package first;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import first.DataSource;
//import first.FoodInfo;

public class FoodInfoDAO {

	public List<FoodInfo> searchByFoodName(String name) {
	    List<FoodInfo> list = new ArrayList<>();
	    String sql = "SELECT food_id, prdlst_nm, pog_daycnt, prdlst_dcnm, bssh_nm FROM FOOD_INFO WHERE prdlst_nm LIKE ?";

	    DataSource ds = new DataSource();

	    try (Connection conn = ds.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, "%" + name + "%");
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                FoodInfo food = new FoodInfo();
	                food.setFoodId(rs.getInt("food_id"));
	                food.setPrdlstNm(rs.getString("prdlst_nm"));
	                food.setPogDaycnt(rs.getString("pog_daycnt"));
	                food.setPrdlstDcnm(rs.getString("prdlst_dcnm"));
	                food.setBsshNm(rs.getString("bssh_nm"));
	                list.add(food);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	    
	}
	public Integer getFoodIdByName(String foodName) {
	    String sql = "SELECT food_id FROM FOOD_INFO WHERE prdlst_nm = ?";
	    try (Connection conn = new DataSource().getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, foodName);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("food_id");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // 존재하지 않으면 null 반환
	}


}
