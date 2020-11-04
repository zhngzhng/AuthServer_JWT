package njgis.opengms.authserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityFlowController {
    @RequestMapping(value = "/login")
    public String toLoginPage(){
        return "login";
    }
    @GetMapping("/register")
    public String toRegisterPage(){
        return "register";
    }
    @GetMapping("/resetPwd")
    public String toResetPwdPage(){
        return "resetPwdPage";
    }
}
