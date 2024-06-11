package com.example.sensor.service;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaProducerService {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private KafkaProducer<String, String> producer;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    public void sendSensorData(String data) {
        ProducerRecord<String, String> record = new ProducerRecord<>("sensor-data", data);
        producer.send(record);
    }

    public void generateTestData(int numberOfMessages) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        String[] sensorTypes = {"temperature", "humidity", "pressure"};
        System.out.println("numberOfMessages = " + numberOfMessages);
        for (int i = 0; i < numberOfMessages; i++) {
            executor.submit(() -> {
                String sensorType = sensorTypes[(int) (Math.random() * sensorTypes.length)];
                String data = String.format("{\"type\":\"%s\",\"timestamp\":%d,\"value\":%f}",
                        sensorType, System.currentTimeMillis(), Math.random() * 100);
                sendSensorData(data);
            });
        }
        executor.shutdown();
        System.out.println("Success shutting down");
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
