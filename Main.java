package first;

import java.util.List;
import java.util.Scanner;

//import first.FoodInfoDAO;
//import first.FridgeItemDAO;
//import first.RecipeDAO;
//import first.UserDAO;
//import first.FoodInfo;
//import first.FridgeItem;
//import first.Recipe;
//import first.UserInfo;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static DataSource dataSource = new DataSource();
    private static RecipeDAO recipeDAO = new RecipeDAO(dataSource);
    private static FridgeItemDAO fridgeItems = new FridgeItemDAO();

    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 0:
                    UserDAO.registerUser();
                    break;
                case 1:
                    fridgeItems.showAllFridgeItems();
                    break;
                case 2:
                    fridgeItems.showUserFridgeItems();
                    break;
                case 3:
                    searchFood();
                    break;
                case 4:
                    recommendRecipesByIngredients();
                    break;
                case 5:
                    addFridgeItem();
                    break;
                case 6:
                    deleteFridgeItemsMenu();
                    break;
                case 7:
                    searchRecipeByName();
                    break;
                case 8:
                    deleteRecipe();
                    break;
                case 9:
                    UserDAO.deleteUser();
                    break;
                case 10:
                    System.out.println("프로그램 종료.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n==== 냉장고 관리 시스템 ====");
        System.out.println("0. 사용자 등록");
        System.out.println("1. 냉장고 음식 조회");
        System.out.println("2. 사용자별 넣어둔 음식 조회");
        System.out.println("3. 음식 궁금 검색");
        System.out.println("4. 재료에 따른 레시피 추천");
        System.out.println("5. 냉장고 음식 넣기");
        System.out.println("6. 냉장고 음식 삭제");
        System.out.println("   6.1 유통기한 지난거 삭제");
        System.out.println("   6.2 먹은거 삭제");
        System.out.println("7. 레시피 검색");
        System.out.println("8. 레시피 삭제");
        System.out.println("9. 사용자 삭제 (부가기능)");
        System.out.println("10. 종료");
        System.out.print("선택 > ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void searchFood() {
        System.out.print("검색할 음식명 입력: ");
        String name = scanner.nextLine();

        FoodInfoDAO dao = new FoodInfoDAO();
        List<FoodInfo> foods = dao.searchByFoodName(name);

        if (foods.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
        } else {
            for (FoodInfo food : foods) {
                System.out.printf("ID: %d, 이름: %s, 유통기한: %s, 유형: %s, 제조사: %s%n",
                        food.getFoodId(), food.getPrdlstNm(), food.getPogDaycnt(),
                        food.getPrdlstDcnm(), food.getBsshNm());
            }
        }
    }

    private static void recommendRecipesByIngredients() {
        System.out.print("재료 이름을 입력하세요: ");
        String ingredient = scanner.nextLine();
        try {
            List<Recipe> recipes = recipeDAO.getRecipesByIngredientName(ingredient);
            if (recipes.isEmpty()) {
                System.out.println("해당 재료를 포함하는 레시피가 없습니다.");
            } else {
                System.out.println(ingredient + "를 포함하는 레시피 목록:");
                for (Recipe r : recipes) {
                    System.out.printf("- %s: %s (조리 시간: %s, 칼로리: %s)%n",
                            r.getName(), r.getSummary(), r.getCookingTime(), r.getCalorie());
                }
            }
        } catch (Exception e) {
            System.out.println("레시피 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private static void addFridgeItem() {
        System.out.print("사용자 이름 입력: ");
        String username = scanner.nextLine();

        UserInfo user = new UserDAO().getUserByUsername(username);
        if (user == null) {
            System.out.println("등록되지 않은 사용자입니다.");
            return;
        }

        System.out.print("음식 이름 입력: ");
        String foodName = scanner.nextLine();

        Integer foodId = new FoodInfoDAO().getFoodIdByName(foodName);

        System.out.print("유통기한 입력 (yyyy-mm-dd): ");
        java.sql.Date expirationDate;
        try {
            expirationDate = java.sql.Date.valueOf(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("유통기한 형식이 올바르지 않습니다.");
            return;
        }

        FridgeItem item = new FridgeItem();
        item.setUserId(user.getUserId());
        item.setFoodId(foodId);
        item.setExpirationDate(expirationDate);
        item.setFoodName(foodName);
        item.setUsername(username);

        int result = fridgeItems.insertFridgeItemAuto(item);
        System.out.println(result > 0 ? "냉장고에 음식이 성공적으로 등록되었습니다." : "등록 실패");
    }

    private static void deleteFridgeItemsMenu() {
        System.out.println("6.1 유통기한 지난거 삭제");
        System.out.println("6.2 먹은거 삭제");
        System.out.print("삭제할 유형 선택 (1 or 2): ");
        String option = scanner.nextLine();

        switch (option) {
            case "1":
                int expiredCount = fridgeItems.deleteExpiredItems();
                System.out.println("유통기한 지난 음식 삭제 완료: " + expiredCount + "개 삭제");
                break;
            case "2":
                System.out.print("사용자 이름 입력: ");
                String username = scanner.nextLine();
                System.out.print("삭제할 음식 이름 입력: ");
                String foodName = scanner.nextLine();
                int eatenCount = fridgeItems.deleteByUsernameAndFoodName(username, foodName);
                System.out.println(eatenCount > 0 ? "삭제 성공: " + eatenCount + "개 삭제" : "삭제할 항목이 없습니다.");
                break;
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }

    private static void deleteRecipe() {
        System.out.print("삭제할 레시피 이름을 입력하세요: ");
        String recipeName = scanner.nextLine();
        try {
            int deletedCount = recipeDAO.deleteRecipeByName(recipeName);
            if (deletedCount > 0) {
                System.out.println("레시피 '" + recipeName + "' 삭제 완료.");
            } else {
                System.out.println("삭제할 레시피를 찾지 못했습니다.");
            }
        } catch (Exception e) {
            System.out.println("레시피 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    private static void searchRecipeByName() {
        System.out.print("검색할 레시피 이름 입력: ");
        String recipeName = scanner.nextLine();

        try {
            Recipe recipe = recipeDAO.getRecipeByName(recipeName);
            if (recipe != null) {
                System.out.println("레시피 정보:");
                System.out.println("이름: " + recipe.getName());
                System.out.println("요약: " + recipe.getSummary());
                System.out.println("조리 시간: " + recipe.getCookingTime());
                System.out.println("칼로리: " + recipe.getCalorie());
            } else {
                System.out.println("❗ '" + recipeName + "' 레시피를 찾을 수 없습니다.");
                String url = "https://www.google.com/search?q=" +
                        java.net.URLEncoder.encode(recipeName + " 레시피", "UTF-8");
                System.out.println("웹에서 검색해보시겠습니까?");
                System.out.println(" → " + url);
                System.out.print("브라우저로 열까요? (Y/N): ");
                String answer = scanner.nextLine().trim().toUpperCase();
                if (answer.equals("Y") && java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                    System.out.println("브라우저가 열렸습니다.");
                } else {
                    System.out.println("웹 검색을 건너뛰었습니다.");
                }
            }
        } catch (Exception e) {
            System.out.println("레시피 검색 중 오류: " + e.getMessage());
        }
    }
}
