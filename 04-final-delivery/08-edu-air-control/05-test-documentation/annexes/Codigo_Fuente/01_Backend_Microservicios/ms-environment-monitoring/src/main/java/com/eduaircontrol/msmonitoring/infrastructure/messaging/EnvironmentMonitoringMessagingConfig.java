package com.eduaircontrol.msmonitoring.infrastructure.messaging;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topologia del consumidor.
 *
 * <p>Exchange, colas y binding se declaran desde el consumidor y no desde el
 * productor: quien recibe el mensaje es quien sabe que lo necesita.
 */
@Configuration
public class EnvironmentMonitoringMessagingConfig {

    public static final String DLQ_SUFFIX = ".dlq";

    /** Routing key con el que los fallos llegan a la cola de letras muertas. */
    public static final String DLQ_ROUTING_KEY = "environmental.data.recorded.dlq";

    @Bean
    public TopicExchange environmentalDataExchange(
            @Value("${app.messaging.measurements-exchange:eduaircontrol.environmental-data}")
            String name) {
        return new TopicExchange(name, true, false);
    }

    @Bean
    public TopicExchange environmentalDataDlxExchange(
            @Value("${app.messaging.measurements-exchange:eduaircontrol.environmental-data}")
            String name) {
        return new TopicExchange(name + DLQ_SUFFIX, true, false);
    }

    /**
     * Cola de trabajo.
     *
     * <p>El exchange de letras muertas se deriva del nombre del <b>exchange</b>, no
     * del de la cola: si se derivara del nombre de la cola apuntaria a un exchange
     * inexistente y los mensajes fallidos se perderian en silencio.
     */
    @Bean
    public Queue environmentalDataQueue(
            @Value("${app.messaging.measurements-queue:eduaircontrol.environmental-data.ms-environment-monitoring}")
            String name,
            TopicExchange environmentalDataExchange) {
        return QueueBuilder.durable(name)
                .deadLetterExchange(environmentalDataExchange.getName() + DLQ_SUFFIX)
                .deadLetterRoutingKey(DLQ_ROUTING_KEY)
                .build();
    }

    /** Cola de letras muertas con 7 dias de retencion, como fija la guia de eventos. */
    @Bean
    public Queue environmentalDataDlq(
            @Value("${app.messaging.measurements-queue:eduaircontrol.environmental-data.ms-environment-monitoring}")
            String name) {
        return QueueBuilder.durable(name + DLQ_SUFFIX)
                .withArgument("x-message-ttl", 604_800_000)
                .build();
    }

    @Bean
    public Binding environmentalDataBinding(
            Queue environmentalDataQueue,
            TopicExchange environmentalDataExchange,
            @Value("${app.messaging.measurements-routing-key:environmental.data.*}")
            String routingKey) {
        return BindingBuilder.bind(environmentalDataQueue)
                .to(environmentalDataExchange).with(routingKey);
    }

    @Bean
    public Binding environmentalDataDlqBinding(
            Queue environmentalDataDlq,
            TopicExchange environmentalDataDlxExchange) {
        return BindingBuilder.bind(environmentalDataDlq)
                .to(environmentalDataDlxExchange).with(DLQ_ROUTING_KEY);
    }

    /**
     * Ajustes del listener.
     *
     * <p>El modo de acknowledgement es {@code AUTO} a proposito: Spring confirma si
     * el metodo retorna y hace {@code nack} si lanza. Con {@code MANUAL} habria que
     * confirmar a mano cada mensaje, y basta con olvidar un {@code basicAck} para
     * perder lecturas sin que nada lo indique.
     *
     * <p>Los reintentos son 4 con espera exponencial desde 1s (1s, 2s, 4s, 8s), el
     * rango que fija {@code 02-domain/domain-events.md}. Al agotarlos, el mensaje va a
     * la cola de letras muertas. Un fallo transitorio de base de datos se recupera
     * sola; un payload corrupto no reintenta de mas.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory measurementListenerContainerFactory(
            ConnectionFactory connectionFactory,
            @Value("${app.messaging.prefetch:50}") int prefetch,
            @Value("${app.messaging.max-retries:4}") int maxRetries,
            @Value("${app.messaging.initial-retry-interval-ms:1000}") long initialIntervalMs) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        // false = al agotar los reintentos, el mensaje se envia a la DLQ en vez de
        // volver a la cola de origen.
        factory.setDefaultRequeueRejected(false);
        factory.setPrefetchCount(prefetch);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(4);
        // El recoverer por defecto solo registra y descarta; con el recuperador
        // siguiente, el mensaje se rechaza sin reencolar y la cola lo envia a la DLQ.
        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxRetries(maxRetries)
                .backOffOptions(initialIntervalMs, 2.0, 10_000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build());
        return factory;
    }
}