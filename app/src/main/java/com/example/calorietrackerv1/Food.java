package com.example.calorietrackerv1;

import java.math.BigDecimal;

/**
 * This class is responsible to create and manage a Food Object, which is similar than
 * the object created in the server.
 */
public class Food {

    private Long foodId;
    private String foodName;
    private Category categoryId;
    private int calorieAmount;
    private Serving servingId;
    private BigDecimal servingAmount;
    private int fat;

    public Food(Long foodId, String foodName, Category categoryId,int calorieAmount, Serving servingId, BigDecimal servingAmount, int fat) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.categoryId = categoryId;
        this.calorieAmount = calorieAmount;
        this.servingId = servingId;
        this.servingAmount = servingAmount;
        this.fat = fat;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(int calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public BigDecimal getServingAmount() {
        return servingAmount;
    }

    public void setServingAmount(BigDecimal servingAmount) {
        this.servingAmount = servingAmount;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public Serving getServingId() {
        return servingId;
    }

    public void setServingId(Serving servingId) {
        this.servingId = servingId;
    }
}