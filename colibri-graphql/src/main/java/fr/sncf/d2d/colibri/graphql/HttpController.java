package fr.sncf.d2d.colibri.graphql;

import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import fr.sncf.d2d.colibri.domain.parcels.ParcelService;
import fr.sncf.d2d.colibri.domain.users.AppUser;
import fr.sncf.d2d.colibri.domain.users.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller("graphql")
public class HttpController {

    private final ParcelService parcelService;
    private final UserService userService;

    public HttpController(
            ParcelService parcelService,
            UserService userService) {
        this.parcelService = parcelService;
        this.userService = userService;
    }

    @QueryMapping
    public List<Parcel> parcels(
            @Argument String id) {
        return Optional.ofNullable(id)
                .map(this.parcelService::retrieve)
                .map(Collections::singletonList)
                .orElseGet(this.parcelService::retrieve);
    }

    @QueryMapping
    public List<AppUser> users(
            @Argument String username) {
        return Optional.ofNullable(username)
                .map(this.userService::retrieve)
                .map(Collections::singletonList)
                .orElseGet(this.userService::retrieve);
    }

    @SchemaMapping
    public AppUser postman(Parcel parcel) {
        return Optional.ofNullable(parcel.getPostmanId())
                .map(this.userService::retrieve)
                .orElse(null);
    }
}
