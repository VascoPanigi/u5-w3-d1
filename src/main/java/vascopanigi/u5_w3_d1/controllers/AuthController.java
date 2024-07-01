package vascopanigi.u5_w3_d1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vascopanigi.u5_w3_d1.payloads.EmployeeLoginDTO;
import vascopanigi.u5_w3_d1.payloads.EmployeeLoginResponseDTO;
import vascopanigi.u5_w3_d1.services.AuthService;
import vascopanigi.u5_w3_d1.services.EmployeeService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public EmployeeLoginResponseDTO login(@RequestBody EmployeeLoginDTO payload){
        return new EmployeeLoginResponseDTO(authService.authenticateEmployeeAndGenerateToken(payload));
    }
}
