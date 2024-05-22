package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.common.IllegalOperationException;
import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import fr.sncf.d2d.colibri.domain.parcels.ParcelRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Repository
public class JdbcParcelRepository extends JdbcCrudRepository<Parcel> implements ParcelRepository {

    public static final String TABLE_NAME = "Parcel";

    public JdbcParcelRepository(
            NamedParameterJdbcTemplate jdbc
    ) {
        super(jdbc, TABLE_NAME);
    }

    @Override
    public Parcel create(Parcel entity) {
        if (entity.getId() != null) {
            throw new IllegalOperationException("Parcel ID cannot be provided on creation");
        }
        entity.setId(UUID.randomUUID().toString());
        return super.create(entity);
    }

    @Override
    protected Map<String, Function<Parcel, ?>> getFieldMappers() {
        return Map.ofEntries(
                Map.entry("id", Parcel::getId),
                Map.entry("address", Parcel::getAddress),
                Map.entry("weight", Parcel::getWeight),
                Map.entry("status", Parcel::getStatus),
                Map.entry("postmanId", Parcel::getPostmanId),
                Map.entry("trackingCode", Parcel::getTrackingCode)
        );
    }

    @Override
    protected Parcel mapRow(ResultSet rs) throws SQLException {
        return Parcel.builder()
                .id(rs.getString("id"))
                .address(rs.getString("address"))
                .weight(rs.getDouble("weight"))
                .postmanId(rs.getString("postmanId"))
                .status(Parcel.Status.valueOf(rs.getString("status")))
                .trackingCode(rs.getString("trackingCode"))
                .build();
    }
}
