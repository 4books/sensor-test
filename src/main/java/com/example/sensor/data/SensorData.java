package com.example.sensor.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SensorData {
    private long timestamp;
    private double value;
}
