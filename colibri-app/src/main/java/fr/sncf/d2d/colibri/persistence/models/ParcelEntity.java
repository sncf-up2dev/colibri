package fr.sncf.d2d.colibri.persistence.models;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public final class ParcelEntity {
    @Id
    String id;
    String address;
    double weight;
    Parcel.Status status;
    String postmanId;
    String trackingCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Parcel.Status getStatus() {
        return status;
    }

    public void setStatus(Parcel.Status status) {
        this.status = status;
    }

    public String getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(String postmanId) {
        this.postmanId = postmanId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public static ParcelEntity from(Parcel parcel) {
        ParcelEntity entity = new ParcelEntity();
        entity.setId(parcel.getId());
        entity.setAddress(parcel.getAddress());
        entity.setWeight(parcel.getWeight());
        entity.setStatus(parcel.getStatus());
        entity.setPostmanId(parcel.getPostmanId());
        entity.setTrackingCode(parcel.getTrackingCode());
        return entity;
    }

    public Parcel toParcel() {
        return Parcel.builder()
                .id(id)
                .address(address)
                .weight(weight)
                .status(status)
                .postmanId(postmanId)
                .trackingCode(trackingCode)
                .build();
    }

}
