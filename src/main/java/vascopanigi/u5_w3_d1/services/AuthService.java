package vascopanigi.u5_w3_d1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vascopanigi.u5_w3_d1.entities.Employee;
import vascopanigi.u5_w3_d1.exceptions.UnauthorizedException;
import vascopanigi.u5_w3_d1.payloads.EmployeeLoginDTO;
import vascopanigi.u5_w3_d1.security.JWTTools;

@Service
public class AuthService {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JWTTools jwtTools;

    public String authenticateEmployeeAndGenerateToken(EmployeeLoginDTO employeeLoginDTO){

        Employee employee = this.employeeService.findByEmail(employeeLoginDTO.email());
        if(employee.getPassword().equals(employeeLoginDTO.password())){
            return jwtTools.createToken(employee);
        } else {
            throw new UnauthorizedException("Login failed! Wrong credentials.");
        }
    }
}
