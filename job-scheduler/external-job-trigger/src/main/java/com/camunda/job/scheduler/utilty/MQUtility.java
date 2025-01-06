package com.camunda.job.scheduler.utilty;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueueConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQUtility {

    public static MQQueueConnectionFactory createMqConnectionFactory(String ip, int port, String queueManager, String channel, String cipherSuite, String clientJks, String certPwd, ResourceLoader resourceLoader) throws Exception {
        MQQueueConnectionFactory connectionFactory   = new MQQueueConnectionFactory();
        connectionFactory.setHostName(ip);
        connectionFactory.setPort(port);
        connectionFactory.setQueueManager(queueManager);
        connectionFactory.setChannel(channel);
        connectionFactory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
        connectionFactory.setSSLCipherSuite(cipherSuite);
        connectionFactory.setSSLSocketFactory(createSSLSocketFactory(clientJks, certPwd, resourceLoader));
        return connectionFactory;
    }

    public static SSLSocketFactory createSSLSocketFactory(String clientJks, String certPwd, ResourceLoader resourceLoader) throws GeneralSecurityException, IOException {
        Resource resource = resourceLoader.getResource(clientJks);

        if(!resource.exists()){
            throw new IOException("Keystore file not found: " + clientJks);
        }
        
        try(InputStream keyStoreStream = resource.getInputStream()) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keyStoreStream, certPwd.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, certPwd.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            
            return sslContext.getSocketFactory();
        }
    }

    public static void sendMessage(QueueSession session, Queue queueDestination, String messageContent) throws  JMSException {
        QueueSender sender = session.createSender(queueDestination);
        TextMessage message = session.createTextMessage();
        message.setText(messageContent);
        sender.send(message);
        sender.close();
    }

    public static void processReceivedMessage(Object messageContent){
        try {
            if(messageContent instanceof Map){
                Map<String,Object> message = (Map<String,Object>) messageContent;
                log.info("Processed Single Message: {}, message");
            } else if (messageContent instanceof List) {
                List<Map<String, Object>> messageList = (List<Map<String, Object>>) messageContent;
                for(Map<String, Object> message: messageList){
                    log.info("Processes message from list: {}", message);
                }
            } else {
                log.warn("Received an unexpected message type: {}", messageContent.getClass().getName());
            }
        } catch (Exception e) {
            log.error("Error processing received message", e);
        }
    }

    // public static Map<String, Object> createDefaultMessage(){
    //     Map<String, Object> defaultMessage = new HashMap<>();
    //     defaultMessage.put("interfaceId", "909ADD1_LIQ0");
    //     defaultMessage.put("statusCode", 19);
    //     defaultMessage.put("message", "error");
    //     defaultMessage.put("errorMessage", "Es konnten nicht alle Wechselkurse aktualisiert werden.");
    //     defaultMessage.put("businessDate", "2024-11-19");
    //     return defaultMessage;
    // }
    public static Object createDefaultMessage(){
  
        List<Map<String, Object>> defaultMessageList = new ArrayList<>();
        
        Map<String, Object> defaultMessage = new HashMap<>();
        defaultMessage.put("interfaceId", "909ADD1_LIQ0");
        defaultMessage.put("statusCode", 19);
        defaultMessage.put("message", "success");
        // defaultMessage.put("errorMessage", "Es konnten nicht alle Wechselkurse aktualisiert werden.");
        defaultMessage.put("businessDate", "2024-11-19");
        defaultMessageList.add(defaultMessage);

        Map<String, Object> defaultMessage1 = new HashMap<>();
        defaultMessage1.put("interfaceId", "909ADD2_LIQ1");
        defaultMessage1.put("statusCode", 19);
        defaultMessage1.put("message", "success");
        // defaultMessage1.put("errorMessage", "Es konnten nicht alle Wechselkurse aktualisiert werden.");
        defaultMessage1.put("businessDate", "2024-11-19");
        defaultMessageList.add(defaultMessage1);

        Map<String, Object> defaultMessage2 = new HashMap<>();
        defaultMessage2.put("interfaceId", "909ADD3_LIQ2");
        defaultMessage2.put("statusCode", 19);
        defaultMessage2.put("message", "success");
        // defaultMessage2.put("errorMessage", "Es konnten nicht alle Wechselkurse aktualisiert werden.");
        defaultMessage2.put("businessDate", "2024-11-19");
        defaultMessageList.add(defaultMessage2);


        return defaultMessageList;
    }
}