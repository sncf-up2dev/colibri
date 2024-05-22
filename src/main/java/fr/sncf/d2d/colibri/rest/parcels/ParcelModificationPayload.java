package fr.sncf.d2d.colibri.rest.parcels;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import fr.sncf.d2d.colibri.rest.common.Omissible;

public class ParcelModificationPayload {

    Omissible<String> address = Omissible.na();
    Omissible<Parcel.Status> status = Omissible.na();
    Omissible<String> postmanId = Omissible.na();

    public Omissible<String> getAddress() {
        return address;
    }

    public void setAddress(Omissible<String> address) {
        this.address = address;
    }

    public Omissible<Parcel.Status> getStatus() {
        return status;
    }

    public void setStatus(Omissible<Parcel.Status> status) {
        this.status = status;
    }

    public Omissible<String> getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(Omissible<String> postmanId) {
        this.postmanId = postmanId;
    }
}
