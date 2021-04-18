package com.example.calorietrackerv1;


/**
 * This class is responsible to create and manage a Serving Object, which is similar than
 * the object created in the server.
 */
public class Serving {

    private Long servingId;
    private String servingUnit;

    public Serving(Long servingId, String servingUnit) {
        this.servingId = servingId;
        this.servingUnit = servingUnit;
    }

    public Long getServingId() {
        return servingId;
    }

    public void setServingId(Long servingId) {
        this.servingId = servingId;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

}