package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class AwsLambdaRequestHandlerSandpit {

  // Running locally
  @Test
  public void runAwsLambdaRequestHandler() {
    System.setProperty("spring.cloud.stream.source", "test");

    try (AwsLambdaRequestHandler handler = new AwsLambdaRequestHandler()) {
      Object result = handler.handleRequest(Map.of(), null);
      log.info("the result is {}", String.valueOf(result));
      Assertions.assertEquals("OK", result);
    }
  }

  /**
   * Make sure you run with the VM argument -Duk.co.ii.env.name=localdev
   *
   * @throws IOException
   */
  @Test
  public void runNewAwsLambdaRequestHandler() throws IOException {
    System.setProperty("spring.profiles.active", "lambda,local-populator");
    System.setProperty("uk.co.ii.env.name", "devl");
    NewAwsLambdaRequestHandler handler = new NewAwsLambdaRequestHandler();
    ObjectMapper om = new ObjectMapper();
    ByteArrayInputStream is = new ByteArrayInputStream(om.writeValueAsBytes(Map.of()));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    handler.handleRequest(is, outputStream, null);
    log.info("the result is {}", outputStream.toString());
    assertEquals("\"dave\"", outputStream.toString());

  }


}
