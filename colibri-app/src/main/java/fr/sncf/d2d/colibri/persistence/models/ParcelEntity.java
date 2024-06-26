package fr.sncf.d2d.colibri.persistence.models;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Optional;

@Entity(name = "Parcel")
public final class ParcelEntity {
    @Id
    String id;
    String address;
    double weight;
    @Enumerated(EnumType.STRING)
    Parcel.Status status;
    @ManyToOne @JoinColumn(name = "postman_id")
    AppUserEntity postman;
    String trackingCode;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Parcel.Status getStatus() {
        return this.status;
    }

    public void setStatus(Parcel.Status status) {
        this.status = status;
    }

    public AppUserEntity getPostman() {
        return this.postman;
    }

    public void setPostman(AppUserEntity postman) {
        this.postman = postman;
    }

    public String getTrackingCode() {
        return this.trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public static ParcelEntity from(Parcel parcel) {
        AppUserEntity userEntity = Optional.ofNullable(parcel.getPostmanId())
                .map(AppUserEntity::fromUsername)
                .orElse(null);
        ParcelEntity entity = new ParcelEntity();
        entity.setId(parcel.getId());
        entity.setAddress(parcel.getAddress());
        entity.setWeight(parcel.getWeight());
        entity.setStatus(parcel.getStatus());
        entity.setPostman(userEntity);
        entity.setTrackingCode(parcel.getTrackingCode());
        return entity;
    }

    public Parcel toParcel() {
        return Parcel.builder()
                .id(this.id)
                .address(this.address)
                .weight(this.weight)
                .status(this.status)
                .postmanId(null == postman ? null : this.postman.getId())
                .trackingCode(this.trackingCode)
                .build();
    }

}
