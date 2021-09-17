package example;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

@Slf4j
public class AwsLambdaRequestHandler extends SpringBootRequestHandler<Map<String, String>, String> {

  public AwsLambdaRequestHandler() {
    super(FunctionApplication.class);
    log.info("AwsLambdaRequestHandler()");
  }

}
