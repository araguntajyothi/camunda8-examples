package com.example.demo.handler;

import org.springframework.stereotype.Component;

import com.example.demo.services.TrackingOrderService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class ProcessPaymentHandler {

    private final TrackingOrderService trackingOrderService;

    public ProcessPaymentHandler(TrackingOrderService trackingOrderService) {
        this.trackingOrderService = trackingOrderService;
    }

    @JobWorker(type = "processPayment", autoComplete = true)
    public void handleProcessPayment(ActivatedJob job) {
        System.out.println("(" + job.getKey() + ") Handling job: " + job.getType());
        trackingOrderService.processPayment(job);
        System.out.println("(" + job.getKey() + ") Payment processed.");
    }
}
