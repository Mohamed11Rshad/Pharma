package com.mo.medreminder;

public class Medicine {

    private int id ;
    private String brandName;
    private String strength;
    private int photo;
    private String indications;

    public Medicine(int id, String brandName, String strength, int photo, String indications) {
        this.id = id;
        this.brandName = brandName;
        this.strength = strength;
        this.photo = photo;
        this.indications = indications;
    }

    public Medicine(String brandName, String strength, int photo, String indications) {
        this.brandName = brandName;
        this.strength = strength;
        this.photo = photo;
        this.indications = indications;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }
}
