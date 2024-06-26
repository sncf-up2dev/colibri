package fr.sncf.d2d.colibri.rest.parcels;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import fr.sncf.d2d.colibri.rest.common.Omissible;
import jakarta.validation.constraints.NotBlank;

public class ParcelModificationPayload {

    Omissible<@NotBlank String> address = Omissible.na();
    Omissible<Parcel.Status> status = Omissible.na();
    Omissible<String> postmanId = Omissible.na();

    public Omissible<String> getAddress() {
        return this.address;
    }

    public void setAddress(Omissible<String> address) {
        this.address = address;
    }

    public Omissible<Parcel.Status> getStatus() {
        return this.status;
    }

    public void setStatus(Omissible<Parcel.Status> status) {
        this.status = status;
    }

    public Omissible<String> getPostmanId() {
        return this.postmanId;
    }

    public void setPostmanId(Omissible<String> postmanId) {
        this.postmanId = postmanId;
    }
}
