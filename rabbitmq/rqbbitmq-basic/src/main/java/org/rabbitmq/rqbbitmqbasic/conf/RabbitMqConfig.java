package org.rabbitmq.rqbbitmqbasic.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.routing.key}")
    private String routeKey;

    /**
     * 지정된 Exchange 이름으로 `Direct Exchange Bean`을 생성한다.
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }

    /**
     * 지정된 Queue 이름으로 `Queue Bean`을 생성한다.
     */
    @Bean
    public Queue queue() {
        // Exchange 의 어떤 Queue 에 넣을지
        return new Queue(queueName);
    }

    /**
     * 주어진 `Queue`와 `Exchange`를 Binding 하고 `Routing Key`를 이용하여  `Binding Bean`을 생성한다.
     * (`Exchange`에 `Queue`를 등록한다고 이해해도 무방)
     */
    @Bean
    public Binding binding(DirectExchange directExchange, Queue queue) {
        // `exchange`와 `queue`가 연결되도록 바인딩
        return BindingBuilder.bind(queue).to(directExchange).with(routeKey);
    }

    /**
     * `ConnectionFactory`로 연결 후 실제 작업을 위한 `Template Bean`을 생성한다.
     * 이때 `ConnectionFactory`의 값은 자동으로 `application.yaml`에 있는 값과 매핑된다.
     */
    @Bean
    public RabbitTemplate rabbitTemplate( // 매개변수 -> Bean으로 등록되어 있는 값을 매핑
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    /**
     * 직렬화 (메시지를 JSON 으로 변환하는 Message Converter)
     */
    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
