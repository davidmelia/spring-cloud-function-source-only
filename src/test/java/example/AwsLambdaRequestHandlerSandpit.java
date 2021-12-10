package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AwsLambdaRequestHandlerSandpit {

  // Running locally
  @Test
  public void runAwsLambdaRequestHandler() {


    try (AwsLambdaRequestHandler handler = new AwsLambdaRequestHandler()) {
      Object result = handler.handleRequest(Map.of(), null);
      log.info("the result is {}", String.valueOf(result));
      Assertions.assertEquals("OK", result);
    }
  }

  @Test
  public void runFunctionInvoker() throws IOException {

    NewAwsLambdaRequestHandler handler = new NewAwsLambdaRequestHandler();
    ObjectMapper om = new ObjectMapper();
    ByteArrayInputStream is = new ByteArrayInputStream(om.writeValueAsBytes(Map.of()));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    handler.handleRequest(is, outputStream, null);
    log.info("the result is {}", outputStream.toString());
    assertEquals("\"OK\"", outputStream.toString());
  }


}
