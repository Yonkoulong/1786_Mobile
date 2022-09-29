package uk.ac.gre.wm50.m_expense.model;

import java.util.HashMap;
import java.util.Map;

import uk.ac.gre.wm50.m_expense.Constants;

public class Expense {
    private String id;
    private String idTrip;
    private String typeOfExpense;
    private double amountOfTheExpense;
    private String timeOfTheExpense;
    private String additionalComments;

    public Expense() {
        this(
                Constants.NEW_EXPENSE_ID,
                Constants.NEW_TRIP_ID,
                Constants.EMPTY_STRING,
                Constants.PRICE_DEFAULT,
                Constants.EMPTY_STRING,
                Constants.EMPTY_STRING
        );
    }

    public Expense(String id, String idTrip, String typeOfExpense, double amountOfTheExpense, String timeOfTheExpense, String additionalComments) {
        this.id = id;
        this.idTrip = idTrip;
        this.typeOfExpense = typeOfExpense;
        this.amountOfTheExpense = amountOfTheExpense;
        this.timeOfTheExpense = timeOfTheExpense;
        this.additionalComments = additionalComments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(String idTrip) {
        this.idTrip = idTrip;
    }

    public String getTypeOfExpense() {
        return typeOfExpense;
    }

    public void setTypeOfExpense(String typeOfExpense) {
        this.typeOfExpense = typeOfExpense;
    }

    public double getAmountOfTheExpense() {
        return amountOfTheExpense;
    }

    public void setAmountOfTheExpense(double amountOfTheExpense) {
        this.amountOfTheExpense = amountOfTheExpense;
    }

    public String getTimeOfTheExpense() {
        return timeOfTheExpense;
    }

    public void setTimeOfTheExpense(String timeOfTheExpense) {
        this.timeOfTheExpense = timeOfTheExpense;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", idTrip='" + idTrip + '\'' +
                ", typeOfExpense='" + typeOfExpense + '\'' +
                ", amountOfTheExpense='" + amountOfTheExpense + '\'' +
                ", timeOfTheExpense='" + timeOfTheExpense + '\'' +
                ", additionalComments='" + additionalComments +
                '}';
    }

    public Map<String, Object> getMapWithoutId() {
        Map<String, Object> tMap = new HashMap<>();
        tMap.put("idTrip", this.idTrip);
        tMap.put("typeOfExpense", this.typeOfExpense);
        tMap.put("amountOfTheExpense", this.amountOfTheExpense);
        tMap.put("timeOfTheExpense", this.timeOfTheExpense);
        tMap.put("additionalComments", this.additionalComments);
        return tMap;
    }
}
