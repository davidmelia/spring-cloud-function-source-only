package example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

public class DaveSandpit {

  @Test
  void testName() throws Exception {
    System.setProperty("MAIN_CLASS", "example.FunctionApplication");
    ObjectMapper om = new ObjectMapper();
    // ByteArrayInputStream is = new ByteArrayInputStream(om.writeValueAsBytes(new ScheduledEvent()));
    ByteArrayInputStream is = new ByteArrayInputStream(om.writeValueAsBytes(Map.of()));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    new FunctionInvoker().handleRequest(is, outputStream, null);

    System.out.println(outputStream.toString());
  }

}
