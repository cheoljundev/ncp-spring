package hello.ncpspring.controller;

import hello.ncpspring.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;
    @GetMapping
    public String issueImage(Model model){
        model.addAttribute("key", captchaService.issueImageCaptcha());
        return "index";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/image-captcha")
    public void verifyImage(@RequestParam("key") String key, @RequestParam("value") String value ){
        boolean result = captchaService.verifyImageCaptcha(key, value);
        if (!result) {
            throw new IllegalStateException("키가 틀립니다.");
        }
    }
}
