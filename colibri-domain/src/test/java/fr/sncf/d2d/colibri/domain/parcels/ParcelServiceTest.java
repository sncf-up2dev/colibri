package fr.sncf.d2d.colibri.domain.parcels;

import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParcelServiceTest {

    ParcelRepository repository = new InMemParcelRepository();
    ParcelService sut = new ParcelService(
            Logger.getAnonymousLogger(),
            repository
    );

    @Test
    void retrieveOne() {
        // Given
        Parcel parcel = randomParcel();
        this.sut.create(parcel);
        // When
        Parcel retrieved = this.sut.retrieve(parcel.getId());
        // Then
        assertThat(retrieved)
                .usingComparator(this::compareParcels)
                .isEqualTo(parcel);
    }

    @Test
    void retrieveOneNotFound() {
        assertThatThrownBy(() -> this.sut.retrieve("not_an_id"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void testRetrieveAll() {
        // Given
        List<Parcel> parcel = Stream.generate(this::randomParcel).limit(10).toList();
        parcel.forEach(this.sut::create);
        // When
        List<Parcel> retrieved = this.sut.retrieve();
        // Then
        assertThat(retrieved)
                .usingComparatorForType(this::compareParcels, Parcel.class)
                .isSubsetOf(parcel);
    }

    @Test
    void delete() {
        // Given
        Parcel parcel = randomParcel();
        this.sut.create(parcel);
        // When
        this.sut.delete(parcel.getId());
        // Then
        assertThatThrownBy(() -> this.sut.retrieve(parcel.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteNotFound() {
        assertThatThrownBy(() -> this.sut.delete("not_an_id"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update() {
        // Given
        Parcel parcel = randomParcel();
        this.sut.create(parcel);
        String newAddress = UUID.randomUUID().toString();
        // When
        this.sut.update(parcel.getId(), u -> u.setAddress(newAddress));
        // Then
        Parcel retrieved = this.sut.retrieve(parcel.getId());
        assertThat(retrieved.getAddress()).isEqualTo(newAddress);
    }

    @Test
    void updateNotFound() {
        assertThatThrownBy(() -> this.sut.update("not_an_id", ignore -> {}))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create() {
        // Given
        Parcel parcel = randomParcel();
        // When
        Parcel created = this.sut.create(parcel);
        // Then
        assertThat(created)
                .usingComparator(this::compareParcels)
                .isEqualTo(parcel);
        Parcel retrieved = this.sut.retrieve(parcel.getId());
        assertThat(retrieved)
                .usingComparator(this::compareParcels)
                .isEqualTo(parcel);
    }

    int compareParcels(Parcel p1, Parcel p2) {
        return p1.getAddress().equals(p2.getAddress()) && p1.getWeight() == p2.getWeight() ? 0 : -1;
    }

    Parcel randomParcel() {
        return Parcel.builder()
                .address(UUID.randomUUID().toString())
                .weight(13)
                .build();
    }
}