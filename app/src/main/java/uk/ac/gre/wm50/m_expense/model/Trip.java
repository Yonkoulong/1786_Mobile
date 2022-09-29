package uk.ac.gre.wm50.m_expense.model;

import java.util.HashMap;
import java.util.Map;

import uk.ac.gre.wm50.m_expense.Constants;

public class Trip {
    private String id;
    private String nameOfTheTrip;
    private String destination;
    private String dateOfTheTrip;
    private String requiresRiskAssessment;
    private String description;

    public Trip() {
        this(
                null,
                Constants.EMPTY_STRING,
                Constants.EMPTY_STRING,
                Constants.EMPTY_STRING,
                Constants.EMPTY_STRING,
                Constants.EMPTY_STRING
        );
    }

    public Trip(String id, String nameOfTheTrip, String destination, String dateOfTheTrip, String requiresRiskAssessment, String description) {
        this.id = id;
        this.nameOfTheTrip = nameOfTheTrip;
        this.destination = destination;
        this.dateOfTheTrip = dateOfTheTrip;
        this.requiresRiskAssessment = requiresRiskAssessment;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameOfTheTrip() {
        return nameOfTheTrip;
    }

    public void setNameOfTheTrip(String nameOfTheTrip) {
        this.nameOfTheTrip = nameOfTheTrip;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateOfTheTrip() {
        return dateOfTheTrip;
    }

    public void setDateOfTheTrip(String dateOfTheTrip) {
        this.dateOfTheTrip = dateOfTheTrip;
    }

    public String getRequiresRiskAssessment() {
        return requiresRiskAssessment;
    }

    public void setRequiresRiskAssessment(String requiresRiskAssessment) {
        this.requiresRiskAssessment = requiresRiskAssessment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", nameOfTheTrip='" + nameOfTheTrip + '\'' +
                ", destination='" + destination + '\'' +
                ", dateOfTheTrip='" + dateOfTheTrip + '\'' +
                ", requiresRiskAssessment='" + requiresRiskAssessment + '\'' +
                ", description='" + description +
                '}';

    }

    public Map<String, Object> getMapWithoutId() {
        Map<String, Object> tMap = new HashMap<>();
        tMap.put("nameOfTheTrip", this.nameOfTheTrip);
        tMap.put("destination", this.destination);
        tMap.put("dateOfTheTrip", this.dateOfTheTrip);
        tMap.put("requiresRiskAssessment", this.requiresRiskAssessment);
        tMap.put("description", this.description);
        return tMap;
    }
}
