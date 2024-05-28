package fr.sncf.d2d.colibri.rest.parcels;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import jakarta.validation.constraints.NotBlank;

public record ParcelCreationPayload(
    @NotBlank String address,
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

