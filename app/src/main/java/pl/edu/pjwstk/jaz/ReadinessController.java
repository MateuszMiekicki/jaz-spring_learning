package pl.edu.pjwstk.jaz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadinessController {
    @GetMapping("/is-ready")
    public void isReady() {
        /*throw new UnsupportedOperationException("TODO");*/
    }
}