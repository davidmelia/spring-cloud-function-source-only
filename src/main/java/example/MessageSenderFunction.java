package example;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.DirectWithAttributesChannel;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component("function")
@AllArgsConstructor
public class MessageSenderFunction implements Function<Flux<Map<String, String>>, Flux<String>> {

  private final StreamBridgePublisher streamBridgePublisher;
  //private final MessageChannelPublisher messageChannelPublisher;

  @Override
  public Flux<String> apply(Flux<Map<String, String>> flux) {
    return flux.flatMap(v -> {
      log.info("Starting...");

      var result = streamBridgePublisher
          .send()
          .doOnSuccess(r -> log.info("Ending. Result={}", r));

      return result;
    });
  }
}