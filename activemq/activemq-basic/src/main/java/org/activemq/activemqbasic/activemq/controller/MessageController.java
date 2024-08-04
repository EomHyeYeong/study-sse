package org.activemq.activemqbasic.activemq.controller;

import lombok.RequiredArgsConstructor;
import org.activemq.activemqbasic.activemq.model.MessageDto;
import org.activemq.activemqbasic.activemq.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send/message")
    public ResponseEntity<String> sendMessage(
            @RequestBody MessageDto messageDto
    ) {
        this.messageService.sendMessage(messageDto);
        return ResponseEntity.ok("Message send to ActiveMQ!");
    }
}
