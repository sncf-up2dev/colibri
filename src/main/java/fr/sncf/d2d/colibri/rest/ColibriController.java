package fr.sncf.d2d.colibri.rest;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class ColibriController {

    @GetMapping
    public String hello() {
        return "Hello World";
    }
}
