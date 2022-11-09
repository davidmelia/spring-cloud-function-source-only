package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

@Slf4j
public class CallFunctionInvoker {

  @Test
  public void runFunctionInvoker() throws IOException {
    System.setProperty("MAIN_CLASS", FunctionApplication.class.getCanonicalName());
    FunctionInvoker handler = new FunctionInvoker("function");
    ObjectMapper om = new ObjectMapper();
    ByteArrayInputStream is = new ByteArrayInputStream(om.writeValueAsBytes(Map.of()));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    handler.handleRequest(is, outputStream, null);
    log.info("the result is {}", outputStream.toString());
    assertEquals("\"OK\"", outputStream.toString());
  }

}
