package vascopanigi.u5_w3_d1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vascopanigi.u5_w3_d1.entities.Device;
import vascopanigi.u5_w3_d1.exceptions.BadRequestException;
import vascopanigi.u5_w3_d1.payloads.*;
import vascopanigi.u5_w3_d1.services.DeviceService;

import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public Page<Device> getAllDevices(@RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy){
        return this.deviceService.getAllDevices(pageNum, pageSize, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewDeviceResponseDTO saveDevice(@RequestBody @Validated NewDeviceDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return new NewDeviceResponseDTO(this.deviceService.save(body).getId());
    }

    @GetMapping("/{deviceId}")
    public Device findById(@PathVariable UUID deviceId) {
        return this.deviceService.findById(deviceId);
    }

    @PutMapping("/{deviceId}")
    public NewDeviceResponseDTO findByIdAndUpdate(@PathVariable UUID deviceId, @RequestBody NewDeviceDTO body) {
        return new NewDeviceResponseDTO(this.deviceService.findByIdAndUpdate(deviceId, body).getId());
    }

    @PatchMapping ("/{deviceId}")
    public SetEmployeeDeviceDTO findByIdAndSetEmployee(@PathVariable UUID deviceId, @RequestBody SetEmployeeDeviceDTO body) {
        return new SetEmployeeDeviceDTO(this.deviceService.findByIdAndSetEmployee(deviceId, body).getId());
    }

    @DeleteMapping("/{deviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID deviceId){this.deviceService.findByIdAndDelete(deviceId);}
}
