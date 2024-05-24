package fr.sncf.d2d.colibri.domain.parcels;

import fr.sncf.d2d.colibri.domain.common.Model;

import java.util.Objects;
import java.util.Set;

/**
 * Postal parcel.
 */
public class Parcel implements Model {
    private String id;
    private String address;
    private final double weight;
    private Status status;
    private String postmanId;
    private String trackingCode;

    /**
     * Constructor.
     *
     * @param id            parcel ID
     * @param address       parcel address
     * @param weight        parcel weight
     * @param status        parcel status
     * @param postmanId     post officer ID
     * @param trackingCode  tracking code
     */
    public Parcel(String id, String address, double weight, Status status, String postmanId, String trackingCode) {
        Objects.requireNonNull(address, "Parcel address cannot be null");
        this.id = id;
        this.address = address;
        this.weight = weight;
        this.status = Objects.requireNonNullElse(status, Status.PENDING);
        this.postmanId = postmanId;
        this.trackingCode = trackingCode;
    }

    public Parcel(String address, double weight, Status status, String postmanId, String trackingCode) {
        this(null, address, weight, status, postmanId, trackingCode);
    }

    public Parcel(String address, double weight) {
        this(null, address, weight, Status.PENDING, null, null);
    }

    public String getId() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public double getWeight() {
        return this.weight;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getPostmanId() {
        return this.postmanId;
    }

    public String getTrackingCode() {
        return this.trackingCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPostmanId(String postmanId) {
        this.postmanId = postmanId;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public enum Status {
        PENDING,
        TRANSIT(PENDING),
        DELIVERED(PENDING, TRANSIT),
        LOST(PENDING, TRANSIT),
        RETURNED(DELIVERED);

        private final Set<Status> sources;

        Status(Status... otherSources) {
            this.sources = Set.of(otherSources);
        }

        public boolean canSucceed(Status other) {
            return this.sources.contains(other);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Postal parcel builder class.
     */
    public final static class Builder {
        private String id;
        private String address;
        private double weight;
        private Status status = Status.PENDING;
        private String postmanId;
        private String trackingCode;

        private Builder() {}

        public Builder id(String id) {
            Objects.requireNonNull(id, "Parcel ID cannot be null");
            this.id = id;
            return this;
        }

        public Builder address(String address) {
            Objects.requireNonNull(address, "Parcel address cannot be null");
            this.address = address;
            return this;
        }

        public Builder weight(double weight) {
            this.weight = weight;
            return this;
        }

        public Builder status(Status status) {
            Objects.requireNonNull(status, "Parcel status cannot be null");
            this.status = status;
            return this;
        }

        public Builder postmanId(String postmanId) {
            this.postmanId = postmanId;
            return this;
        }

        public Builder trackingCode(String trackingCode) {
            this.trackingCode = trackingCode;
            return this;
        }

        /**
         * Build parcel.
         *
         * @return build parcel.
         */
        public Parcel build() {
            return new Parcel(
                this.id,
                this.address,
                this.weight,
                this.status,
                this.postmanId,
                this.trackingCode
            );
        }
    }

}
