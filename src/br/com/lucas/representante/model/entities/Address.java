package br.com.lucas.representante.model.entities;

public class Address {
    private String street;
    private int number;
    private String area;
    private String zipCode;
    private String city;
    private String state;
    private String pointOfReference;

    private Client resident;

    public Address(String street, int number, String area, String zipCode, String city, String state, String pointOfReference) {
        this.street = street;
        this.number = number;
        this.area = area;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.pointOfReference = pointOfReference;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", area='" + area + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pointOfReference='" + pointOfReference + '\'' +
                ", number=" + number +
                '}';
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if(number < 0)
            throw new IllegalArgumentException("Address number can't be negative.");
        this.number = number;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPointOfReference() {
        return pointOfReference;
    }

    public void setPointOfReference(String pointOfReference) {
        this.pointOfReference = pointOfReference;
    }

    public Client getResident() {
        return resident;
    }

    public void setResident(Client resident) {
        this.resident = resident;
    }
}
