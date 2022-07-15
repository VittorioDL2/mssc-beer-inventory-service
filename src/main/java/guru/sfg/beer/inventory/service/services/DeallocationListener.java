package guru.sfg.beer.inventory.service.services;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.brewery.model.events.DeallocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeallocationListener {

	private final AllocationService allocationService;
	
	@JmsListener(destination = JmsConfig.DEALLOCATE_ORDER_QUEUE)
	public void listen(DeallocateOrderRequest evt) {
		log.debug("Deallocation of : " + evt.toString());
		
		allocationService.deallocateOrder(evt.getBeerOrder());
	}

}
