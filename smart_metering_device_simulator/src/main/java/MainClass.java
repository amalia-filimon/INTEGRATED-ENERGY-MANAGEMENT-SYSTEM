import java.io.IOException;

public class MainClass {

    public static void main(String[] args) {
        Simulator simulator = new Simulator("D:/SD/DS2023_30641_Filimon_Amalia_Assignment_1/sensor.csv");
        try {
            simulator.loadDeviceConfig("config.json");
            simulator.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

