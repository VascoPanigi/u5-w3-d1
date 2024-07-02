package vascopanigi.u5_w3_d1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vascopanigi.u5_w3_d1.exceptions.BadRequestException;
import vascopanigi.u5_w3_d1.payloads.EmployeeLoginDTO;
import vascopanigi.u5_w3_d1.payloads.EmployeeLoginResponseDTO;
import vascopanigi.u5_w3_d1.payloads.NewDeviceResponseDTO;
import vascopanigi.u5_w3_d1.payloads.NewEmployeeDTO;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewDeviceResponseDTO saveEmployee(@RequestBody @Validated NewEmployeeDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return new NewDeviceResponseDTO(this.employeeService.save(body).getId());
    }
}
