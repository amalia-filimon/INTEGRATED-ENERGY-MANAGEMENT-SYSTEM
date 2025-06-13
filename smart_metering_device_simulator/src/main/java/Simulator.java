import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class Simulator implements Runnable {
    private final static String QUEUE_NAME = "measurements_queue";
    private final String csvFilePath;
    private boolean running = true;
    private Thread thread;
    private Channel channel;
    private Connection connection;
    private DeviceConfig deviceConfig;

    public void loadDeviceConfig(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        deviceConfig = mapper.readValue(new File(configFilePath), DeviceConfig.class);
    }

    public Simulator(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public void connectToRabbitMQ() {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri("amqps://cmwzcozf:yL5D_puV1ARemqbCrclDx5mEdoGkPL_j@woodpecker.rmq.cloudamqp.com/cmwzcozf");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(this);
            running = true;
            thread.start();
        }
    }

    public void run() {
        connectToRabbitMQ();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            //citesc din fisier alternativ, adica o linie pt primul deviceId, urmatoarea linie pt al 2 lea si tot asa
            int deviceIndex = 0; // Index pentru a alterna intre dispozitive
            List<DeviceConfig.Device> devices = deviceConfig.getDevices();
            int deviceCount = devices.size(); // Nr total de dispozitive

            while ((line = br.readLine()) != null && running) {
                DeviceConfig.Device device = devices.get(deviceIndex);

                long timestamp = Instant.now().getEpochSecond();
                JSONObject json = new JSONObject();
                json.put("timestamp", timestamp);
                json.put("device_id", device.getDevice_id());
                json.put("measurement_value", Double.parseDouble(line.split(",")[0]));

                sendMessage(json.toString());

                deviceIndex = (deviceIndex + 1) % deviceCount; // Urmatorul index de dispozitiv
                Thread.sleep(5000); // 10 sec intre mesaje
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeRabbitMQ();
        }
    }



    private void sendMessage(String message) {
        try {
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeRabbitMQ() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
