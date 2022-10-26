package example;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.function.adapter.aws.AWSCompanionAutoConfiguration;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper;
import org.springframework.cloud.function.context.config.RoutingFunction;
import org.springframework.cloud.function.json.JacksonMapper;
import org.springframework.cloud.function.json.JsonMapper;
import org.springframework.cloud.function.utils.FunctionClassUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class MyFunctionInvoker extends FunctionInvoker{

	private static Log logger = LogFactory.getLog(MyFunctionInvoker.class);
	
	public MyFunctionInvoker() {
		start();
	}
	
	private void start() {
		Class<?> startClass = FunctionClassUtils.getStartClass();
		String[] properties = new String[] {"--spring.cloud.function.web.export.enabled=false", "--spring.main.web-application-type=none"};
		ConfigurableApplicationContext context = ApplicationContextInitializer.class.isAssignableFrom(startClass)
				? FunctionalSpringApplication.run(new Class[] {startClass, AWSCompanionAutoConfiguration.class}, properties)
						: SpringApplication.run(new Class[] {startClass, AWSCompanionAutoConfiguration.class}, properties);

		Environment environment = context.getEnvironment();
		String functionName = "function";
		FunctionCatalog functionCatalog = context.getBean(FunctionCatalog.class);
		JsonMapper jsonMapper = context.getBean(JsonMapper.class);
		if (jsonMapper instanceof JacksonMapper) {
			((JacksonMapper) jsonMapper).configureObjectMapper(objectMapper -> {
				if (!objectMapper.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)) {
					SimpleModule module = new SimpleModule();
					module.addDeserializer(Date.class, new JsonDeserializer<Date>() {
						@Override
						public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
								throws IOException {
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(jsonParser.getValueAsLong());
							return calendar.getTime();
						}
					});
					objectMapper.registerModule(module);
					objectMapper.registerModule(new JodaModule());
					objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
				}
			});
		}
		
		Field jsonMapperField= ReflectionUtils.findField(FunctionInvoker.class, "jsonMapper");
		ReflectionUtils.makeAccessible(jsonMapperField);
		ReflectionUtils.setField(jsonMapperField, this, jsonMapper);
		

		if (logger.isInfoEnabled()) {
			logger.info("Locating function: '" + functionName + "'");
		}

		FunctionInvocationWrapper function = functionCatalog.lookup(functionName, "application/json");

		Set<String> names = functionCatalog.getNames(null);
		if (function == null && !CollectionUtils.isEmpty(names)) {

			if (logger.isInfoEnabled()) {
				if (names.size() == 1) {
					logger.info("Will default to RoutingFunction, since it is the only function available in FunctionCatalog."
							+ "Expecting 'spring.cloud.function.definition' or 'spring.cloud.function.routing-expression' as Message headers. "
							+ "If invocation is over API Gateway, Message headers can be provided as HTTP headers.");
				}
				else {
					logger.info("More then one function is available in FunctionCatalog. " + names
							+ " Will default to RoutingFunction, "
							+ "Expecting 'spring.cloud.function.definition' or 'spring.cloud.function.routing-expression' as Message headers. "
							+ "If invocation is over API Gateway, Message headers can be provided as HTTP headers.");
				}
			}
			function = functionCatalog.lookup(RoutingFunction.FUNCTION_NAME, "application/json");
		}

		if (function.isOutputTypePublisher()) {
			function.setSkipOutputConversion(true);
		}
		Assert.notNull(function, "Failed to lookup function " + functionName);

		if (!StringUtils.hasText(functionName)) {
			functionName = function.getFunctionDefinition();
		}

		if (logger.isInfoEnabled()) {
			logger.info("Located function: '" + functionName + "'");
		}
		
		Field functionField= ReflectionUtils.findField(FunctionInvoker.class, "function");
		ReflectionUtils.makeAccessible(functionField);
		ReflectionUtils.setField(functionField, this, function);
	}
}
