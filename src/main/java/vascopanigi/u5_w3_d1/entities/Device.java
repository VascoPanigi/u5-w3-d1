package vascopanigi.u5_w3_d1.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vascopanigi.u5_w3_d1.enums.DeviceStatus;
import vascopanigi.u5_w3_d1.enums.DeviceType;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DeviceStatus deviceStatus;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Device(DeviceStatus deviceStatus, DeviceType deviceType) {
        this.deviceStatus = deviceStatus;
        this.deviceType = deviceType;
    }

    public Device(DeviceStatus deviceStatus, DeviceType deviceType, Employee employee) {
        this.deviceStatus = deviceStatus;
        this.deviceType = deviceType;
        this.employee = employee;
    }
}
