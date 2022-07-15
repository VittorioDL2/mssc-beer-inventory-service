package guru.sfg.beer.inventory.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JmsConfig {

	public static final String NEW_INVENTORY_QUEUE = "new-inventory";
	public static final String ALLOCATE_ORDER_QUEUE = "allocate-order";
	public static final String ALLOCATE_ORDER_RESULT_QUEUE = "allocate-order-result";
	public static final String DEALLOCATE_ORDER_QUEUE = "deallocate-order";
	
	@Bean
	public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
		MappingJackson2MessageConverter mj2mc = new MappingJackson2MessageConverter();
		mj2mc.setTargetType(MessageType.TEXT);
		mj2mc.setTypeIdPropertyName("_type");
		mj2mc.setObjectMapper(objectMapper);
		return mj2mc;
	}

}
