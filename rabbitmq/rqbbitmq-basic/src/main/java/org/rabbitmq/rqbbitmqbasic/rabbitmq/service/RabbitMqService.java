package org.rabbitmq.rqbbitmqbasic.rabbitmq.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rabbitmq.rqbbitmqbasic.common.Producer;
import org.rabbitmq.rqbbitmqbasic.rabbitmq.model.MessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMqService {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.routing.key}")
    private String routeKey;

    private final RabbitTemplate rabbitTemplate;
    private final Producer producer;

    /**
     * Producer 역할
     * `Queue`로 메시지를 발행한다.
     */
    public void sendMessage(MessageDto messageDto) {
        log.info("message send: {}", messageDto.toString());
//        this.rabbitTemplate.convertAndSend(exchangeName, routeKey, messageDto);
        producer.producer(exchangeName, routeKey, messageDto);
    }

    /**
     * Consumer 역할
     * `Queue`에서 메시지를 수신한다.
     */
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(MessageDto messageDto) {
        log.info("Receive Message: {}", messageDto.toString());
    }
}
