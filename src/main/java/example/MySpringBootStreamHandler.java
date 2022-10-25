package example;

import org.springframework.cloud.function.adapter.aws.SpringBootStreamHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySpringBootStreamHandler extends SpringBootStreamHandler {

	public MySpringBootStreamHandler() {
		super(FunctionApplication.class);
		log.info("MySpringBootRequestHandler()");
	}

}
