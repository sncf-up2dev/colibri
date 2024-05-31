package fr.sncf.d2d.colibri.domain.parcels;

import fr.sncf.d2d.colibri.domain.common.Page;
import fr.sncf.d2d.colibri.domain.common.PageSpecs;
import fr.sncf.d2d.colibri.domain.common.Service;
import fr.sncf.d2d.colibri.domain.common.IllegalOperationException;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;

import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Service
public class ParcelService {

    private final Logger logger;
    private final ParcelRepository repository;
    private final Random random = new Random();

    public ParcelService(
            Logger logger,
            ParcelRepository repository
    ) {
        this.logger = logger;
        this.repository = repository;
    }

    public Parcel retrieve(String id) {
        this.logger.info(() -> "Retrieving parcel with ID %s.".formatted(id));
        return this.repository.retrieve(id).orElseThrow(NotFoundException::new);
    }

    public List<Parcel> retrieve() {
        this.logger.info("Retrieving all parcels.");
        return this.repository.retrieve();
    }

    public Page<Parcel> retrieve(PageSpecs pageSpecs) {
        this.logger.info("Retrieving parcel's page number %d.".formatted(pageSpecs.pageNumber()));
        return this.repository.retrieve(pageSpecs);
    }

    public void delete(String id) {
        this.logger.info(() -> "Deleting parcel with ID %s.".formatted(id));
        this.repository.delete(id);
    }

    public Parcel update(String id, Consumer<Parcel> updater) {
        this.logger.info(() -> "Updating parcel with ID %s.".formatted(id));
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
        this.logger.info("Creating new parcel.");
        byte[] bytes = new byte[24];
        this.random.nextBytes(bytes);
        String trackingCode = Base64.getUrlEncoder().encodeToString(bytes);
        parcel.setTrackingCode(trackingCode);
        return this.repository.create(parcel);
    }
}
