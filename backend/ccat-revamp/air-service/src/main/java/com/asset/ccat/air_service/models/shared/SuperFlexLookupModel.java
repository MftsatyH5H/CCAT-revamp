package com.asset.ccat.air_service.models.shared;

public class SuperFlexLookupModel {

    private Integer id;
    private String name;
    private String description;
    private String packageID;
    private Integer typeID;
    private Integer OfferID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getOfferID() {
        return OfferID;
    }

    public void setOfferID(Integer offerID) {
        OfferID = offerID;
    }

    @Override
    public String toString() {
        return "SuperFlexLookupModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", packageID='" + packageID + '\'' +
                ", typeID=" + typeID +
                ", OfferID=" + OfferID +
                '}';
    }
}
