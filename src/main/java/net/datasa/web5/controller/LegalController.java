package net.datasa.web5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("legal")
public class LegalController {

    @GetMapping("privacy")
    public String privacy() {
        return "legal/privacy";
    }

    @GetMapping("terms")
    public String terms() {
        return "legal/terms";
    }

    @GetMapping("contact")
    public String contact() {
        return "legal/contact";
    }
}
