package org.oop18.entities;

/**
 * @author - Haribo
 */
public class TravelCode {
    private Integer id;
    private Integer travelCode;
    private String travelCodeName;

    public TravelCode() {
    }

    public TravelCode(Integer travelCode, String travelCodeName) {
        this.travelCode = travelCode;
        this.travelCodeName = travelCodeName;
    }

    public TravelCode(Integer id, Integer travelCode, String travelCodeName) {
        this.id = id;
        this.travelCode = travelCode;
        this.travelCodeName = travelCodeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
