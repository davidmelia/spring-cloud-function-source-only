package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallFunctionInvoker {

	/**
	 * results in function-out-0=UNKNOWN_TOPIC_OR_PARTITION
	 * 
	 * <pre>
	 * 2022-10-25 11:13:16.089  WARN 68925 --- [ad | producer-1] org.apache.kafka.clients.NetworkClient   : [Producer clientId=producer-1] Error while fetching metadata with correlation id 33 : {function-out-0=UNKNOWN_TOPIC_OR_PARTITION}
	 * </pre>
	 */
	@Test
	public void runFunctionInvoker_UNKNOWN_TOPIC_OR_PARTITION() throws IOException {
		System.setProperty("spring.cloud.function.definition", "function");
		FunctionInvoker handler = new FunctionInvoker();
		ObjectMapper om = new ObjectMapper();
		ByteArrayInputStream is = new ByteArrayInputStream(om.writeValueAsBytes(Map.of()));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		handler.handleRequest(is, outputStream, null);
		log.info("the result is {}", outputStream.toString());
		assertEquals("\"dave\"", outputStream.toString());
	}

	/**
	 * Failed to establish route
	 * 
	 * <pre>
	 * java.lang.IllegalStateException: Failed to establish route, since neither were provided: 'spring.cloud.function.definition' as Message header or as application property or 'spring.cloud.function.routing-expression' as application property. Incoming message: GenericMessage [payload=byte[2], headers={id=0b099ec3-480d-6d92-077c-e42d31dd3dc9, timestamp=1666692939591}]
	 * </pre>
	 */
	@Test
	public void runFunctionInvoker_Failed_to_establish_route() throws IOException {
		FunctionInvoker handler = new FunctionInvoker();
		ObjectMapper om = new ObjectMapper();
		ByteArrayInputStream is = new ByteArrayInputStream(om.writeValueAsBytes(Map.of()));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		handler.handleRequest(is, outputStream, null);
		log.info("the result is {}", outputStream.toString());
		assertEquals("\"dave\"", outputStream.toString());
	}

}
