package com.example.calorietrackerv1;

import java.sql.Date;

/**
 * This class is responsible to create and manage a Consumption Object, which is similar than
 * the object created in the server.
 */
public class Consumption {

    private Long consumptionId;
    private User userId;
    private Food foodId;
    private Date consumptionDate;
    private int quantity;



    public Consumption(Long consumptionId, User userId, Food foodId, Date consumptionDate, int quantity) {
        this.consumptionId = consumptionId;
        this.userId = userId;
        this.foodId = foodId;
        this.consumptionDate = consumptionDate;
        this.quantity = quantity;
    }

    public Long getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(Long consumptionId) {
        this.consumptionId = consumptionId;
    }

    public Date getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(Date consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Food getFoodId() {
        return foodId;
    }

    public void setFoodId(Food foodId) {
        this.foodId = foodId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}