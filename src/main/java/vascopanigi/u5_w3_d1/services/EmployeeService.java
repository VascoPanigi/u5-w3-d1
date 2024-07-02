package vascopanigi.u5_w3_d1.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vascopanigi.u5_w3_d1.entities.Employee;
import vascopanigi.u5_w3_d1.exceptions.BadRequestException;
import vascopanigi.u5_w3_d1.exceptions.NotFoundException;
import vascopanigi.u5_w3_d1.payloads.NewEmployeeDTO;
import vascopanigi.u5_w3_d1.repositories.EmployeeRepository;

import java.io.IOException;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PasswordEncoder bcrypt;

    private final Faker faker;

    @Autowired
    public EmployeeService(Faker faker) {
        this.faker = faker;
    }

    public Page<Employee> getAllEmployees(int pageNum, int pageSize, String sortBy){
        if(pageSize>50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        return employeeRepository.findAll(pageable);
    }

    public Employee save(NewEmployeeDTO body) {
        this.employeeRepository.findByEmail(body.email()).ifPresent(
                user -> {
                    throw new BadRequestException("This email address: " + body.email() + " is already used. Try again");
                }
        );
        Employee newEmployee = new Employee(body.name(), body.surname(), faker.name().username(), body.email(), bcrypt.encode(body.password()));
        newEmployee.setAvatarURL("https://ui-avatars.com/api/?name=" + newEmployee.getName() + "+" + newEmployee.getSurname());
        return employeeRepository.save(newEmployee);
    }

    public Employee findById(UUID employeeId) {
        return this.employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
    }

    public Employee findByIdAndUpdate(UUID employeeId, Employee modifiedUser) {
        Employee found = this.findById(employeeId);
        found.setName(modifiedUser.getName());
        found.setSurname(modifiedUser.getSurname());
        found.setEmail(modifiedUser.getEmail());
        found.setAvatarURL("https://ui-avatars.com/api/?name=" + modifiedUser.getName() + "+" + modifiedUser.getSurname());
        return this.employeeRepository.save(found);
    }

    public void findByIdAndDelete(UUID employeeId) {
        Employee found = this.findById(employeeId);
        this.employeeRepository.delete(found);
    }

    //----- gestione della save nel cloud -----
    public String uploadAvatarImage(MultipartFile file) throws IOException {
        return (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
    }

    public Employee patchNewAvatar(UUID employeeId, String avatarUrl) {
        Employee employee = findById(employeeId);
        employee.setAvatarURL(avatarUrl);
        return this.employeeRepository.save(employee);
    }

    public Employee findByEmail(String email){
        return employeeRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Employee with email: " + email + " not found"));
    }
}
