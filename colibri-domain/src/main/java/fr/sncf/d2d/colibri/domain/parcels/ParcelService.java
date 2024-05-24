package fr.sncf.d2d.colibri.domain.parcels;

import fr.sncf.d2d.colibri.domain.common.IllegalOperationException;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@Service
public class ParcelService {

    private final ParcelRepository repository;
    private final Random random = new Random();

    public ParcelService(ParcelRepository repository) {
        this.repository = repository;
    }

    public Parcel retrieve(String id) {
        return this.repository.retrieve(id).orElseThrow(NotFoundException::new);
    }

    public List<Parcel> retrieve() {
        return this.repository.retrieve();
    }

    public void delete(String id) {
        this.repository.delete(id);
    }

    public Parcel update(String id, Consumer<Parcel> updater) {
        Parcel parcel = this.repository.retrieve(id).orElseThrow(NotFoundException::new);
        Parcel.Status oldStatus = parcel.getStatus();
        updater.accept(parcel);
        Parcel.Status newStatus = parcel.getStatus();
        if (oldStatus != newStatus && !newStatus.canSucceed(oldStatus)) {
            throw new IllegalOperationException("Status %s cannot succeed %s.".formatted(newStatus, oldStatus));
        }
        return this.repository.update(parcel);
    }

    public Parcel create(Parcel parcel) {
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        String trackingCode = Base64.getUrlEncoder().encodeToString(bytes);
        parcel.setTrackingCode(trackingCode);
        return this.repository.create(parcel);
    }
}
