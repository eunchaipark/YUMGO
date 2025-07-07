package first;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    private DataSource dataSource;

    public RecipeDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 재료 이름으로 레시피 조회
    public List<Recipe> getRecipesByIngredientName(String ingredientName) throws SQLException {
        String sql = "SELECT DISTINCT r.NAME, r.SUMMARY, r.COOKING_TIME, r.CALORIE " +
                "FROM RECIPE r JOIN RECIPE_INGREDIENT i ON r.RECIPE_ID = i.RECIPE_ID " +
                "WHERE i.INGREDIENT_NAME = ?";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ingredientName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Recipe r = new Recipe();
                    r.setName(rs.getString("NAME"));
                    r.setSummary(rs.getString("SUMMARY"));
                    r.setCookingTime(rs.getString("COOKING_TIME"));
                    r.setCalorie(rs.getString("CALORIE"));
                    recipes.add(r);
                }
            }
        }
        return recipes;
    }

    // 레시피 이름으로 재료 조회
    public List<RecipeIngredient> getIngredientsByRecipeName(String recipeName) throws SQLException {
        String sql = "SELECT DISTINCT i.INGREDIENT_NAME " +
                "FROM RECIPE r JOIN RECIPE_INGREDIENT i ON r.RECIPE_ID = i.RECIPE_ID " +
                "WHERE r.NAME = ?";
        List<RecipeIngredient> ingredients = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, recipeName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setIngredientName(rs.getString("INGREDIENT_NAME"));
                    ingredients.add(ri);
                }
            }
        }
        return ingredients;
    }

    // 레시피 이름으로 레시피 삭제 (재료는 cascade FK로 자동 삭제)
    public int deleteRecipeByName(String recipeName) throws SQLException {
        String sql = "DELETE FROM RECIPE WHERE NAME = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, recipeName);
            return pstmt.executeUpdate();
        }
    }
    
    public Recipe getRecipeByName(String recipeName) throws SQLException {
        String sql = "SELECT NAME, SUMMARY, COOKING_TIME, CALORIE FROM RECIPE WHERE NAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, recipeName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Recipe r = new Recipe();
                    r.setName(rs.getString("NAME"));
                    r.setSummary(rs.getString("SUMMARY"));
                    r.setCookingTime(rs.getString("COOKING_TIME"));
                    r.setCalorie(rs.getString("CALORIE"));
                    return r;
                } else {
                    return null;
                }
            }
        }
    }
    
}
