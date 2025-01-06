package com.example.demo.services;

import java.time.Duration;

import org.springframework.stereotype.Service;

import io.camunda.zeebe.client.api.response.ActivatedJob;

@Service
public class TrackingOrderService {

    private static final int TRACKING_TIME = 10; // in seconds

    public void trackOrderStatus(ActivatedJob job) throws InterruptedException {
        System.out.println("(" + job.getKey() + ") Tracking order status...");
        Thread.sleep(Duration.ofSeconds(TRACKING_TIME).toMillis());
        System.out.println("(" + job.getKey() + ") Order status tracked");
    }

    public Boolean packItems(ActivatedJob job) {
        System.out.println("(" + job.getKey() + ") Packing items...");
        return true;
    }

    public String processPayment(ActivatedJob job) {
        System.out.println("(" + job.getKey() + ") Processing payment...");
        return String.valueOf(System.currentTimeMillis());
    }
}
