package com.camunda.job.scheduler.mq;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.camunda.job.scheduler.utilty.MQUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@ExternalTaskSubscription("receiveMessage")
public class MQReceiveExternalTaskHandler implements ExternalTaskHandler {

     @Value("${mq.ip}")
    private String ip;

    @Value("${mq.port}")
    private int port;

    @Value("${mq.queueManager}")
    private String queueManager;

    @Value("${mq.channel}")
    private String channel;

    @Value("${mq.encryption}")
    private String encryption;

    @Value("${mq.cipherSuite}")
    private String cipherSuite;

    @Value("${mq.certPwd}")
    private String certPwd;

    @Value("${mq.clientJks}")
    private String clientJks;

    private final ResourceLoader resourceLoader;

    private final ObjectMapper objectMapper;
    
    public MQReceiveExternalTaskHandler(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            
             MQQueueConnectionFactory connectionFactory = MQUtility.createMqConnectionFactory(
                    ip, port, queueManager, channel, cipherSuite, clientJks, certPwd, resourceLoader);

                QueueConnection connection = connectionFactory.createQueueConnection();
                connection.start();
                QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

                Queue queueOut = new MQQueue("LIQ_POC_OUT");

                QueueReceiver receiver = session.createReceiver(queueOut);

                TextMessage message = (TextMessage) receiver.receive(5000);

                Object messageContent = null;

                if(message != null){

                    String messageText = message.getText();
                    log.info("Received message from queue: LIQ_POC_OUT, content: {}", messageText);
                    
                    try{
                          messageContent = objectMapper.readValue(messageText, Map.class);
                    } catch(Exception e1){
                            
                        try {

                            messageContent = objectMapper.readValue(messageText, List.class);
                            
                        } catch (Exception e2) {
                            log.error("Error deserializing message: {}", messageText, e2);
                            messageContent = MQUtility.createDefaultMessage();
                        }
                    }

                }

                if(messageContent == null){
                    messageContent = MQUtility.createDefaultMessage();
                }

                MQUtility.processReceivedMessage(messageContent);

                externalTaskService.complete(externalTask, Collections.singletonMap("messageContent", messageContent));

            // } else {
                //   log.warn("No message received");
            // }

                receiver.close();
                connection.stop();
                connection.close();
                
        } catch (Exception e) {
            log.error("Error receiving message from MQ", e);
            externalTaskService.handleBpmnError(externalTask, "RECEIVE_MESSAGE_ERROR", e.getMessage());
        }
    }
    
}
