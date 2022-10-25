package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallMySpringBootStreamHandler {

	/**
	 * Works great but SpringBootStreamHandler is deprecated.
	 * @throws IOException
	 */
	@Test
	public void runSpringBootStreamHandler() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(Map.of()));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (MySpringBootStreamHandler handler = new MySpringBootStreamHandler()) {
			handler.handleRequest(is, outputStream, null);
		}
		log.info("the result is {}", outputStream);
		assertEquals("\"OK\"", outputStream.toString());
	}

}
