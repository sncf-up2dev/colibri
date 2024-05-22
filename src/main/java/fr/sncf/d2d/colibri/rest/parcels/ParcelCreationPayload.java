package fr.sncf.d2d.colibri.rest.parcels;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;

public record ParcelCreationPayload(
    String address,
    double weight,
    String postmanId
) {

    public Parcel toParcel() {
        return Parcel.builder()
                .address(address)
                .weight(weight)
                .postmanId(postmanId)
                .build();
    }
}

