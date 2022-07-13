package guru.sfg.beer.inventory.service.services;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.events.AllocateOrderRequest;
import guru.sfg.brewery.model.events.AllocateOrderResponse;
import guru.sfg.brewery.model.events.AllocateOrderResponse.AllocateOrderResponseBuilder;
import guru.sfg.brewery.model.events.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AllocationListener {
	private final BeerInventoryRepository beerInventoryRepository;
	private final AllocationService allocationService;
	private final JmsTemplate jmsTemplate;
	
	@JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
	public void listen(AllocateOrderRequest request) {
		log.debug("Got request : " + request.toString());
		
		AllocateOrderResponseBuilder builder = AllocateOrderResponse.builder();
		
		builder.beerOrder(request.getBeerOrder());
		
		try {
			Boolean allocationResult = allocationService.allocateOrder(request.getBeerOrder());
			if(allocationResult) {
				builder.pendingInventory(false);
			}else {
				builder.pendingInventory(true);
			}
		} catch(Exception e ) {
			log.error("allocation err:" + e.getMessage());
			builder.isValid(false);
		}
		
		jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE,builder.build());
		
	}
}
