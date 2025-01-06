package com.example.demo.handler;

import org.springframework.stereotype.Component;

import com.example.demo.services.TrackingOrderService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class PackItemsHandler {

    private final TrackingOrderService trackingOrderService;

    public PackItemsHandler(TrackingOrderService trackingOrderService) {
        this.trackingOrderService = trackingOrderService;
    }

    @JobWorker(type = "packItems", autoComplete = true)
    public void handlePackItems(ActivatedJob job) {
        System.out.println("(" + job.getKey() + ") Handling job: " + job.getType());
        trackingOrderService.packItems(job);
        System.out.println("(" + job.getKey() + ") Items packed.");
    }
}
