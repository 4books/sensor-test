package com.example.sensor.service;

import com.example.sensor.data.SensorData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    private final SensorDataService sensorDataService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public KafkaListenerService(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @KafkaListener(topics = "sensor-data", groupId = "sensor-group")
    public void listen(String message) {
        processMessage(message);
    }

    public void processMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String sensorType = jsonNode.get("type").asText();
            long timestamp = jsonNode.get("timestamp").asLong();
            double value = jsonNode.get("value").asDouble();

            SensorData sensorData = new SensorData();
            sensorData.setTimestamp(timestamp);
            sensorData.setValue(value);

            System.out.println("sensorData = " + sensorData);

            sensorDataService.saveSensorData(sensorType, sensorData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
