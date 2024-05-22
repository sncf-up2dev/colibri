package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.common.CrudRepository;
import fr.sncf.d2d.colibri.domain.common.Entity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class JdbcCrudRepository<T extends Entity> implements CrudRepository<T> {

    protected final NamedParameterJdbcTemplate jdbc;
    protected final String tableName;
    protected final String idField;

    public JdbcCrudRepository(NamedParameterJdbcTemplate jdbc, String tableName, String idField) {
        this.jdbc = jdbc;
        this.tableName = tableName;
        this.idField = idField;
    }

    public JdbcCrudRepository(NamedParameterJdbcTemplate jdbc, String tableName) {
        this.jdbc = jdbc;
        this.tableName = tableName;
        this.idField = "id";
    }

    @Override
    public Optional<T> retrieve(String id) {
        String statement = """
                SELECT *
                FROM %s
                WHERE %s = :id
                """.formatted(this.tableName, this.idField);
        Map<String, ?> params = Map.of("id", id);
        try {
            return jdbc.queryForObject(statement, params, this::mapRow);
        } catch (EmptyResultDataAccessException ignore) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> retrieve() {
        String statement = """
                SELECT *
                FROM %s
                """.formatted(this.tableName);
        Map<String, ?> params = Map.of();
        return jdbc.query(statement, params, this::mapRows);
    }

    @Override
    public T create(T entity) {
        MutationStrings ms = this.prepareMutationStrings();
        String fieldNames = ms.fieldNames();
        String valuesClause = ms.valuesClause();
        Map<String, ?> params = this.prepareMutationParams(entity);
        String statement = """
                INSERT INTO %s (%s)
                VALUES (%s)
                """.formatted(this.tableName, fieldNames, valuesClause);
        jdbc.execute(statement, params, ps -> null);
        return this.retrieve(entity.getId()).orElseThrow(IllegalStateException::new);
    }

    @Override
    public T update(T entity) {
        MutationStrings ms = this.prepareMutationStrings();
        String fieldNames = ms.fieldNames();
        String valuesClause = ms.valuesClause();
        Map<String, Object> params = new HashMap<>(this.prepareMutationParams(entity));
        params.remove(this.idField);
        params.put("id", entity.getId());
        String statement = """
                UPDATE %s
                SET (%s)
                VALUES (%s)
                WHERE %s = :id
                """.formatted(this.tableName, fieldNames, valuesClause, this.idField);
        jdbc.execute(statement, params, ps -> null);
        return this.retrieve(entity.getId()).orElseThrow(IllegalStateException::new);
    }

    @Override
    public void delete(String id) {
        String statement = """
                DELETE FROM %s
                WHERE %s = :id
                """.formatted(this.tableName, this.idField);
        Map<String, ?> params = Map.of("id", id);
        jdbc.execute(statement, params, ps -> null);
    }

    @Override
    public T save(T entity) {
        return entity.getId() == null ? this.create(entity) : this.update(entity);
    }

    protected abstract Map<String, Function<T, ?>> getFieldMappers();

    protected List<T> mapRows(ResultSet rs) {
        try {
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(this.mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    protected Optional<T> mapRow(ResultSet rs, int ignore) {
        try {
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(this.mapRow(rs));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    protected abstract T mapRow(ResultSet rs) throws SQLException;

    protected Map<String, ?> prepareMutationParams(T entity) {
        Map<String, Object> params = new HashMap<>();
        this.getFieldMappers().forEach((k, v) -> params.put(k, v.apply(entity)));
        return params;
    }

    protected MutationStrings prepareMutationStrings() {
        List<String> keys = List.copyOf(this.getFieldMappers().keySet());
        String fieldNames = String.join(", ", keys);
        String valuesClause = String.join(", ", keys.stream().map(":"::concat).toList());
        return new MutationStrings(fieldNames, valuesClause);
    }

    protected record MutationStrings(
            String fieldNames,
            String valuesClause
    ) {}
}
