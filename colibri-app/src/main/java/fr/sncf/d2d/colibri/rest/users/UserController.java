package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.users.AppUser;
import fr.sncf.d2d.colibri.domain.users.UserService;
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

import java.util.List;

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
    public List<UserPayload> retrieve() {
        return UserPayload.from(this.service.retrieve());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserPayload create(
            @RequestBody
            UserModificationPayload payload
    ) {
        AppUser user = this.service.create(payload.toAppUser());
        return UserPayload.from(user);
    }

    @RequestMapping(method = { RequestMethod.PATCH, RequestMethod.PUT }, path = "/{id}")
    public UserPayload update(
            @PathVariable
            String id,
            @RequestBody
            UserModificationPayload payload
    ) {
        AppUser user = this.service.update(id, model -> {
            if (payload.role() != null) {
                model.setRole(payload.role());
            }
            if (payload.password() != null) {
                model.setPassword(payload.password());
            }
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
