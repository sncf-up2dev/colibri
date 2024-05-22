package fr.sncf.d2d.colibri.rest.parcels;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;

import java.util.Collection;
import java.util.List;

public record ParcelPayload(
        String id,
        String address,
        double weight,
        Parcel.Status status,
        String officerId,
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

    public static List<ParcelPayload> from(Collection<Parcel> parcel) {
        return parcel.stream().map(ParcelPayload::from).toList();
    }
}
