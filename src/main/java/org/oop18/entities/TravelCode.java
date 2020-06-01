package org.oop18.entities;

/**
 * @author - Haribo
 */
public class TravelCode {
    private Integer id;
    private Integer travelCode;
    private Integer travelCodeName;

    public TravelCode() {
    }

    public TravelCode(Integer id, Integer travelCode, Integer travelCodeName) {
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

    public Integer getTravelCodeName() {
        return travelCodeName;
    }

    public void setTravelCodeName(Integer travelCodeName) {
        this.travelCodeName = travelCodeName;
    }
}
