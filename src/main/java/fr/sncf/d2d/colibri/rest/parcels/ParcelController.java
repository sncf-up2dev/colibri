package fr.sncf.d2d.colibri.rest.parcels;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import fr.sncf.d2d.colibri.domain.parcels.ParcelService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parcels")
public class ParcelController {

    private final ParcelService service;

    public ParcelController(ParcelService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ParcelPayload retrieve(
            @PathVariable String id
    ) {
        return ParcelPayload.from(this.service.retrieve(id));
    }

    @GetMapping
    public List<ParcelPayload> retrieve() {
        return ParcelPayload.from(this.service.retrieve());
    }

    @PostMapping
    public ParcelPayload create(
            @RequestBody
            ParcelCreationPayload payload
    ) {
        Parcel parcel = this.service.create(payload.toParcel());
        return ParcelPayload.from(parcel);
    }

    @RequestMapping(method = { RequestMethod.PATCH, RequestMethod.PUT }, path = "/{id}")
    public ParcelPayload update(
        @PathVariable String id,
        @RequestBody ParcelModificationPayload payload
    ) {
        Parcel parcel = this.service.update(id, p -> {
            payload.address.ifPresent(p::setAddress);
            payload.status.ifPresent(p::setStatus);
            payload.postmanId.ifPresent(p::setPostmanId);
        });
        return ParcelPayload.from(parcel);
    }

    @DeleteMapping
    public void delete(
            @PathVariable String id
    ) {
        this.service.delete(id);
    }
}
