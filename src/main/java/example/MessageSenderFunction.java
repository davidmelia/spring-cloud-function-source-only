package example;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component(value = "function")
@AllArgsConstructor
public class MessageSenderFunction implements Function<Flux<Map<String, String>>, Flux<String>> {

	private StreamBridge streamBridge;
	// private final MessageChannelPublisher messageChannelPublisher;

	@Override
	public Flux<String> apply(Flux<Map<String, String>> flux) {
		return flux.flatMap(v -> {
			Object kafkaEvent = MessageBuilder
					.withPayload(Map.of("Key", String.format("Value @ %s", ZonedDateTime.now(ZoneOffset.UTC))))
					.setHeaderIfAbsent(KafkaHeaders.KEY, UUID.randomUUID().toString()).build();
			log.info("Sending message to binding = {}", kafkaEvent);
			if (streamBridge.send("test-out-0", kafkaEvent)) {
				log.info("Message sent to binding = {}", kafkaEvent);
				return Mono.just("OK");
			} else {
				log.error("Error occurred while sending message = {} to the binding.", kafkaEvent);
				return Mono.error(new RuntimeException("event publishing failed"));
			}
		});
	}
}
