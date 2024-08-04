package org.rabbitmq.rqbbitmqbasic.rabbitmq.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rabbitmq.rqbbitmqbasic.rabbitmq.model.MessageDto;
import org.rabbitmq.rqbbitmqbasic.rabbitmq.service.RabbitMqService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RabbitMqController {

    private final RabbitMqService rabbitMqService;

    @PostMapping("/send/message")
    public ResponseEntity<String> sendMessage(
            @RequestBody MessageDto messageDto
    ) {
        this.rabbitMqService.sendMessage(messageDto);
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }
}
