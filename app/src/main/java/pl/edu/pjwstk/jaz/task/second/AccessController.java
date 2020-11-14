package pl.edu.pjwstk.jaz.task.second;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessController {
    @GetMapping("/forAll")
    public String forAll() {
        return "forAll";
    }

    @GetMapping("/forAuthenticated")
    public String forAuthenticated() {
        return "forAuthenticated";
    }

    @GetMapping("/forAuthorized")
    public String forAuthorized() {
        return "forAuthorized";
    }
}
