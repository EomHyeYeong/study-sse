package org.activemq.activemqbasic.activemq.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activemq.activemqbasic.activemq.model.MessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    @Value("${activemq.queue.name}")
    private String queueName;

    private final JmsTemplate jmsTemplate;

    /**
     * Producer
     * `Queue`로 메시지를 발행
     */
    public void sendMessage(MessageDto messageDto) {
        log.info("message sent: {}", messageDto.toString());
        jmsTemplate.convertAndSend(queueName, messageDto);
    }

    /**
     * Consumer
     * `Queue`로 메시자가 들어오면 메시지를 수신
     */
    @JmsListener(destination = "${activemq.queue.name}")
    public void receiveMessage(MessageDto messageDto) {
        log.info("Received message: {}", messageDto.toString());
    }
}
