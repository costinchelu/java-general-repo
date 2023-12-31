package io.spring.batch.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.PollableChannel;


@Configuration
public class IntegrationConfiguration {

	private static final String PARTITION_REQUESTS = "partition.requests";

	@Bean
	public DirectChannel outboundRequests() {
		return new DirectChannel();
	}

	@Bean
	public MessagingTemplate messageTemplate() {
		MessagingTemplate messagingTemplate = new MessagingTemplate(outboundRequests());
		messagingTemplate.setReceiveTimeout(60_000_000L);
		return messagingTemplate;
	}

	// listen on the outbound req channel and for every message that is coming in it's going to send a message via RabbitMQ
	// in this case it will send it to the partition requests queue
	@Bean
	@ServiceActivator(inputChannel = "outboundRequests")
	public AmqpOutboundEndpoint amqpOutboundEndpoint(AmqpTemplate template) {
		AmqpOutboundEndpoint endpoint = new AmqpOutboundEndpoint(template);
		endpoint.setExpectReply(true);
		endpoint.setOutputChannel(inboundRequests());
		endpoint.setRoutingKey(PARTITION_REQUESTS);
		return endpoint;
	}

	// RabbitMQ queue definition
	@Bean
	public Queue requestQueue() {
		return new Queue(PARTITION_REQUESTS, false);
	}

	// listen for Rabbit to send in messages
	@Bean
	@Profile("slave")
	public AmqpInboundChannelAdapter inbound(SimpleMessageListenerContainer listenerContainer) {
		AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(listenerContainer);
		adapter.setOutputChannel(inboundRequests());
		adapter.afterPropertiesSet();
		return adapter;
	}

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
		container.setQueueNames(PARTITION_REQUESTS);
		// prevents the situation where multiple JVMs start but just one of them is picking up work
		container.setAutoStartup(false);
		return container;
	}

	@Bean
	public PollableChannel outboundStaging() {
		return new NullChannel();
	}

	@Bean
	public QueueChannel inboundRequests() {
		return new QueueChannel();
	}
}
