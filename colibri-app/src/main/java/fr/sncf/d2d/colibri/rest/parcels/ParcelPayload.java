package fr.sncf.d2d.colibri.rest.parcels;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;

public record ParcelPayload(
        String id,
        String address,
        double weight,
        Parcel.Status status,
        String postmanId,
        String trackingCode
) {
    public static ParcelPayload from(Parcel parcel) {
        return new ParcelPayload(
                parcel.getId(),
                parcel.getAddress(),
                parcel.getWeight(),
                parcel.getStatus(),
                parcel.getPostmanId(),
                parcel.getTrackingCode()
        );
    }
}
