package example;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class AwsLambdaRequestHandlerSandpit {

  // Running locally
  @Test
  public void runAwsLambdaRequestHandler() {
    System.setProperty("spring.cloud.stream.source", "test"); // this has no effect


    try (AwsLambdaRequestHandler handler = new AwsLambdaRequestHandler()) {
      Object result = handler.handleRequest(Map.of(), null);
      log.info("the result is {}", String.valueOf(result));
      Assertions.assertEquals("OK", result);
    }
  }

}
