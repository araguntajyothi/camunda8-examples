package com.example.demo.handler;

import org.springframework.stereotype.Component;

import com.example.demo.services.TrackingOrderService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class OrderHandler {

    
    private final TrackingOrderService trackingOrderService;

    public OrderHandler(TrackingOrderService trackingOrderService) {
        this.trackingOrderService = trackingOrderService;
    }

    @JobWorker(type = "trackOrderStatus", autoComplete = true)
    public void handleTrackOrderStatus(ActivatedJob job) throws InterruptedException {
        System.out.println("(" + job.getKey() + ") Handling job: " + job.getType());
        trackingOrderService.trackOrderStatus(job);
    }
}
