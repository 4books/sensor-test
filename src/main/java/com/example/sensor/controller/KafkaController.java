package com.example.sensor.controller;

import com.example.sensor.service.KafkaListenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final KafkaListenerService kafkaListenerService;

    public KafkaController(KafkaListenerService kafkaListenerService) {
        this.kafkaListenerService = kafkaListenerService;
    }

    @PostMapping("/trigger")
    public ResponseEntity<String> triggerListener(@RequestBody String message) {
        kafkaListenerService.processMessage(message);
        return ResponseEntity.ok("Message processed: " + message);
    }
}
