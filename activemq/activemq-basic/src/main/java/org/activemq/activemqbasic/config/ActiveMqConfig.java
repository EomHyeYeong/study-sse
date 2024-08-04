package org.activemq.activemqbasic.config;

import jakarta.jms.Queue;
import lombok.extern.slf4j.Slf4j;
import org.activemq.activemqbasic.activemq.model.MessageDto;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class ActiveMqConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${activemq.queue.name}")
    private String queueName;

    /**
     * 지정된 Queue 이름으로 Bean 생성
     */
    @Bean
    public Queue queue(
            ActiveMQProperties activeMQProperties
    ) {
        log.info("get BrokerUrl: {}", activeMQProperties.getBrokerUrl());
        log.info("get User: {}", activeMQProperties.getUser());
        log.info("get Password: {}", activeMQProperties.getPassword());
        return new ActiveMQQueue(queueName);
    }

    /**
     * `ActiveMQProperties`의 필드 초기화 후 Bean 생성
     */
    @Bean
    public ActiveMQProperties activeMQProperties() {
        ActiveMQProperties activeMQProperties = new ActiveMQProperties();
        activeMQProperties.setBrokerUrl(brokerUrl);
        activeMQProperties.setUser(user);
        activeMQProperties.setPassword(password);
        return activeMQProperties;
    }

    /**
     * Spring application 에서 activeMQ로 접근하기 위해 `ActiveConnectionFactory`로 연결
     */
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(
            ActiveMQProperties activeMQProperties
    ) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(activeMQProperties.getBrokerUrl());
        activeMQConnectionFactory.setUserName(activeMQProperties.getUser());
        activeMQConnectionFactory.setPassword(activeMQProperties.getPassword());
        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(
            ActiveMQProperties activeMQProperties
    ) {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory(activeMQProperties));
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setExplicitQosEnabled(true);    // 메시지 전송 시 QOS을 설정
        jmsTemplate.setDeliveryPersistent(false);   // 메시지의 영속성을 설정
        jmsTemplate.setReceiveTimeout(1000 * 3);    // 메시지를 수신하는 동안의 대기 시간을 설정(3초)
        jmsTemplate.setTimeToLive(1000 * 60 * 30);  // 메시지의 유효 기간을 설정(30분)
        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            ActiveMQProperties activeMQProperties
    ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory(activeMQProperties));
        factory.setMessageConverter(jacksonJmsMessageConverter());
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_typeId");
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("message", MessageDto.class);
        converter.setTypeIdMappings(typeIdMappings);
        return converter;
    }
}
