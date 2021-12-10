package example;

import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component(value = "function")
@AllArgsConstructor
public class MessageSenderFunction implements Function<Flux<Map<String, String>>, Flux<String>> {

  // private final MessageChannelPublisher messageChannelPublisher;

  @Override
  public Flux<String> apply(Flux<Map<String, String>> flux) {
    return flux.flatMap(v -> {
      Object kafkaEvent = null;
      log.info("Sending message to binding = {}", kafkaEvent);
      log.info("Message sent to binding = {}", kafkaEvent);
      return Mono.just("OK");
    });
  }
}
