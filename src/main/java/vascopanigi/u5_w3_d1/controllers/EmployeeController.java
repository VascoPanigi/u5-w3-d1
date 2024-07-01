package vascopanigi.u5_w3_d1.controllers;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vascopanigi.u5_w3_d1.entities.Employee;
import vascopanigi.u5_w3_d1.exceptions.BadRequestException;
import vascopanigi.u5_w3_d1.payloads.NewEmployeeDTO;
import vascopanigi.u5_w3_d1.payloads.NewEmployeeResponseDTO;
import vascopanigi.u5_w3_d1.services.EmployeeService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping
    public Page<Employee> getAllEmployees(@RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy){
        return this.employeeService.getAllEmployees(pageNum, pageSize, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewEmployeeResponseDTO saveEmployee(@RequestBody @Validated NewEmployeeDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return new NewEmployeeResponseDTO(this.employeeService.save(body).getId());
    }

    @GetMapping("/{employeeId}")
    public Employee findById(@PathVariable UUID employeeId) {
        return this.employeeService.findById(employeeId);
    }

    @PutMapping("/{employeeId}")
    public Employee findByIdAndUpdate(@PathVariable UUID employeeId, @RequestBody Employee body) {
        return this.employeeService.findByIdAndUpdate(employeeId, body);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID employeeId){this.employeeService.findByIdAndDelete(employeeId);}

    @PatchMapping("/{employeeId}/avatar")
    public Employee uploadAvatar(@PathVariable UUID employeeId, @RequestParam("avatar") MultipartFile file) throws IOException {
        String url = employeeService.uploadAvatarImage(file);
        return this.employeeService.patchNewAvatar(employeeId, url);
    }
}
