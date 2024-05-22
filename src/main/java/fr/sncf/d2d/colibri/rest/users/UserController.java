package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.users.User;
import fr.sncf.d2d.colibri.domain.users.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public UserPayload create(
            @RequestBody
            UserPayload payload
    ) {
        User user = this.service.create(payload.toUser());
        return UserPayload.from(user);
    }

    @DeleteMapping
    public void delete(
            @PathVariable String id
    ) {
        this.service.delete(id);
    }
}
