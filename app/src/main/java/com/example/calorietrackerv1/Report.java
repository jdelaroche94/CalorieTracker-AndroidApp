package com.example.calorietrackerv1;

import java.util.Date;

/**
 * This class is responsible to create and manage a Report Object, which is similar than
 * the object created in the server.
 */
public class Report {

    private User userId;
    private Date reportDate;
    private int calorieConsumed;
    private int caloriesBurned;
    private int stepsTaken;
    private int calorieGoal;

    public Report() {
    }


    public Report(User userId, Date reportDate, int calorieConsumed, int caloriesBurned, int stepsTaken, int calorieGoal) {
        this.userId = userId;
        this.reportDate = reportDate;
        this.calorieConsumed = calorieConsumed;
        this.caloriesBurned = caloriesBurned;
        this.stepsTaken = stepsTaken;
        this.calorieGoal = calorieGoal;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
    public int getCalorieConsumed() {
        return calorieConsumed;
    }

    public void setCalorieConsumed(int calorieConsumed) {
        this.calorieConsumed = calorieConsumed;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public int getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }



}
