package com.camunda.job.scheduler.mq;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;

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
@ExternalTaskSubscription("sendMessage")
public class MQSendExternalTaskHandler implements  ExternalTaskHandler{
    
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
    
    public MQSendExternalTaskHandler(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            
            List<String> interfaceIds = List.of("909ADD1_LIQ0", "909ADD2_LIQ2");
            String businessDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            for(String interfaceId : interfaceIds) {
                String messageContent = createJsonMessage(interfaceId, businessDate);

                MQQueueConnectionFactory connectionFactory = MQUtility.createMqConnectionFactory(
                    ip, port, queueManager, channel, cipherSuite, clientJks, certPwd, resourceLoader);

                QueueConnection connection = connectionFactory.createQueueConnection();
                connection.start();
                QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

                Queue queueIn = new MQQueue("LIQ_POC_IN");

                MQUtility.sendMessage(session, queueIn, messageContent);

                log.info("Message sent to queue: LIQ_POC_IN, content: {}", messageContent);

                session.close();
                connection.stop();
                connection.close();
            }


        } catch (Exception e) {
            log.error("Error Sending message to MQ", e);
            externalTaskService.handleBpmnError(externalTask, "SEND_ERROR_MESSAGE", e.getMessage());
        }
    }

    private String createJsonMessage(String interfaceId, String businessDate) throws Exception {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("interfaceId", interfaceId);
        messageData.put("businessDate", businessDate);
        messageData.put("messageType", "start");

        return objectMapper.writeValueAsString(messageData);

    }
}
