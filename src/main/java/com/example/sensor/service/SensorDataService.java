package com.example.sensor.service;

import com.example.sensor.data.SensorData;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SensorDataService {


    private final InfluxDBClient influxDBClient;

    @Value("${influxdb.bucket}")
    private String bucket;

    @Value("${influxdb.org}")
    private String org;

    public SensorDataService(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    public void saveSensorData(String measurement, SensorData sensorData) {
        Point point = Point.measurement(measurement)
                .addField("value", sensorData.getValue())
                .time(Instant.ofEpochMilli(sensorData.getTimestamp()), WritePrecision.MS);

//        System.out.println("point = " + point.toLineProtocol());
        influxDBClient.getWriteApiBlocking().writePoint(bucket, org, point);
    }
}
