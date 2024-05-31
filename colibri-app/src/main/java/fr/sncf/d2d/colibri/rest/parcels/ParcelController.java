package fr.sncf.d2d.colibri.rest.parcels;

import fr.sncf.d2d.colibri.domain.common.Page;
import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import fr.sncf.d2d.colibri.domain.parcels.ParcelService;
import fr.sncf.d2d.colibri.rest.common.PageParams;
import fr.sncf.d2d.colibri.rest.common.PagePayload;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public PagePayload<ParcelPayload> retrieve(
            PageParams pageParams
    ) {
        Page<Parcel> page = this.service.retrieve(pageParams.toPageSpecs());
        return PagePayload.from(page, ParcelPayload::from);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParcelPayload create(
            @Valid @RequestBody ParcelCreationPayload payload
    ) {
        Parcel parcel = this.service.create(payload.toParcel());
        return ParcelPayload.from(parcel);
    }

    @RequestMapping(method = { RequestMethod.PATCH, RequestMethod.PUT }, path = "/{id}")
    public ParcelPayload update(
        @PathVariable String id,
        @Valid @RequestBody ParcelModificationPayload payload
    ) {
        Parcel parcel = this.service.update(id, p -> {
            payload.address.ifAvailable(p::setAddress);
            payload.status.ifAvailable(p::setStatus);
            payload.postmanId.ifAvailable(p::setPostmanId);
        });
        return ParcelPayload.from(parcel);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String id
    ) {
        this.service.delete(id);
    }
}
