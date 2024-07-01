package vascopanigi.u5_w3_d1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vascopanigi.u5_w3_d1.entities.Device;
import vascopanigi.u5_w3_d1.enums.DeviceStatus;
import vascopanigi.u5_w3_d1.enums.DeviceType;
import vascopanigi.u5_w3_d1.exceptions.BadRequestException;
import vascopanigi.u5_w3_d1.exceptions.NotFoundException;
import vascopanigi.u5_w3_d1.payloads.NewDeviceDTO;
import vascopanigi.u5_w3_d1.payloads.SetEmployeeDeviceDTO;
import vascopanigi.u5_w3_d1.repositories.DeviceRepository;

import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private EmployeeService employeeService;

    public Page<Device> getAllDevices(int pageNum, int pageSize, String sortBy){
        if(pageSize>50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        return deviceRepository.findAll(pageable);
    }

    public Device save(NewDeviceDTO body) {
        Device newDevice = new Device(convertStatusToStr(body.deviceStatus()), convertTypeToStr(body.deviceType()));
        return deviceRepository.save(newDevice);
    }

    public Device findById(UUID deviceId) {
        return this.deviceRepository.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
    }

    public Device findByIdAndUpdate(UUID deviceId, NewDeviceDTO modifiedDevice) {
        Device found = this.findById(deviceId);
        found.setDeviceStatus(convertStatusToStr(modifiedDevice.deviceStatus()));
        found.setDeviceType(convertTypeToStr(modifiedDevice.deviceType()));
        return this.deviceRepository.save(found);
    }

    public Device findByIdAndSetEmployee(UUID deviceId, SetEmployeeDeviceDTO body) {
        Device found = this.findById(deviceId);
        if (found.getDeviceStatus() != DeviceStatus.AVAILABLE) {
            throw new BadRequestException("Device with ID " + deviceId + " is not available. It is currently " + found.getDeviceStatus());
        }
        found.setEmployee(employeeService.findById(body.employeeId()));
        found.setDeviceStatus(DeviceStatus.ASSIGNED);
        return this.deviceRepository.save(found);
    }

    public void findByIdAndDelete(UUID deviceId) {
        Device found = this.findById(deviceId);
        this.deviceRepository.delete(found);
    }

    public static DeviceType convertTypeToStr(String deviceType){
        try {
            return DeviceType.valueOf(deviceType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device type: " + deviceType + ". Choose between SMARTPHONE, LAPTOP, TABLET. Exception " + e);
        }
    }

    public static  DeviceStatus convertStatusToStr(String deviceStatus){
        try {
            return DeviceStatus.valueOf(deviceStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device status: " + deviceStatus + ". Choose between AVAILABLE, ASSIGNED, DISMISSED, MAINTENANCE. Exception " + e);
        }
    }
}
