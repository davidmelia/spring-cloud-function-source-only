package example;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class StreamBridgePublisher {

  // Output Binding will create up-front?

  private final StreamBridge streamBridge;
  private final GenericApplicationContext context;

  public Mono<String> send() {
    Object kafkaEvent =
        MessageBuilder.withPayload(Map.of("Key", String.format("Value @ %s", ZonedDateTime.now(ZoneOffset.UTC)))).setHeaderIfAbsent(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString()).build();
    log.info("Sending message to binding = {}", kafkaEvent);
    if (streamBridge.send("test-out-0", kafkaEvent)) {
      log.info("Message sent to binding = {}", kafkaEvent);
      return Mono.just("OK");
    } else {
      log.error("Error occurred while sending message = {} to the binding.", kafkaEvent);
      return Mono.error(new RuntimeException("event publishing failed"));
    }
  }

}
