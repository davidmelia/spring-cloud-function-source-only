package example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@Slf4j
public class FunctionApplication {

  /*
   * You need this main method or explicit <start-class>example.FunctionConfiguration</start-class> in
   * the POM to ensure boot plug-in makes the correct entry
   */
  public static void main(String[] args) {
    SpringApplication.run(FunctionApplication.class, args);
  }


}
