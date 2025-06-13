import java.util.List;

public class DeviceConfig {
    private List<Device> devices;

    public List<Device> getDevices() {
        return devices;
    }

    public static class Device {
        private String device_id;

        public String getDevice_id() {
            return device_id;
        }
    }
}
