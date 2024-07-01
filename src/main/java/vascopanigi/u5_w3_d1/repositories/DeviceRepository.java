package vascopanigi.u5_w3_d1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vascopanigi.u5_w3_d1.entities.Device;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
