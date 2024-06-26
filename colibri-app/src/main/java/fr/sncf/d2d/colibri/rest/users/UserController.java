package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.common.Page;
import fr.sncf.d2d.colibri.domain.users.AppUser;
import fr.sncf.d2d.colibri.domain.users.UserService;
import fr.sncf.d2d.colibri.rest.common.PageParams;
import fr.sncf.d2d.colibri.rest.common.PagePayload;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public UserPayload retrieve(
            @PathVariable String id
    ) {
        return UserPayload.from(this.service.retrieve(id));
    }

    @GetMapping
    public PagePayload<UserPayload> retrieve(
            PageParams pageParams
    ) {
        Page<AppUser> page = this.service.retrieve(pageParams.toPageSpecs());
        return PagePayload.from(page, UserPayload::from);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserPayload create(
            @Valid @RequestBody UserCreationPayload payload
    ) {
        AppUser user = this.service.create(payload.toAppUser());
        return UserPayload.from(user);
    }

    @RequestMapping(method = { RequestMethod.PATCH, RequestMethod.PUT }, path = "/{id}")
    public UserPayload update(
            @PathVariable String id,
            @Valid @RequestBody UserModificationPayload payload
    ) {
        AppUser user = this.service.update(id, model -> {
            payload.getRole().ifAvailable(model::setRole);
            payload.getPassword().ifAvailable(model::setPassword);
        });
        return UserPayload.from(user);
    }

    @DeleteMapping
    public void delete(
            @PathVariable String id
    ) {
        this.service.delete(id);
    }
}
