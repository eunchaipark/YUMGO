package first;

import java.sql.Date;

import lombok.Data;

@Data
public class FridgeItem {
    private int fridgeItemId;
    private int userId;
    private Integer foodId;
    private Date expirationDate;
    private Date insertedAt;
    private String foodName;
    private String username;  
}
