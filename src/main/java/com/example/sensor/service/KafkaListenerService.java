package com.example.sensor.service;

import com.example.sensor.data.SensorData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    @Autowired
    private SensorDataService sensorDataService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "sensor-data", groupId = "sensor-group")
    public void listen(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String sensorType = jsonNode.get("type").asText();
            long timestamp = jsonNode.get("timestamp").asLong();
            double value = jsonNode.get("value").asDouble();

            SensorData sensorData = new SensorData();
            sensorData.setTimestamp(timestamp);
            sensorData.setValue(value);

            sensorDataService.saveSensorData(sensorType, sensorData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}