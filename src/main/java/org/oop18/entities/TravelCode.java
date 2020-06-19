package org.oop18.entities;

/**
 * @author - Haribo
 */
public class TravelCode {
    private Integer travelCode;
    private String travelCodeName;

    public TravelCode() {
    }

    public TravelCode(Integer travelCode, String travelCodeName) {
        this.travelCode = travelCode;
        this.travelCodeName = travelCodeName;
    }

    public Integer getTravelCode() {
        return travelCode;
    }

    public void setTravelCode(Integer travelCode) {
        this.travelCode = travelCode;
    }

    public String getTravelCodeName() {
        return travelCodeName;
    }

    public void setTravelCodeName(String travelCodeName) {
        this.travelCodeName = travelCodeName;
    }
}
