package guru.sfg.beer.inventory.service.services;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.common.events.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewInventoryListener {
	private final BeerInventoryRepository beerInventoryRepository;
	
	@JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
	public void listen(NewInventoryEvent newInventoryEvent) {
		log.debug("Got inventory : " + newInventoryEvent.toString());
		
		beerInventoryRepository.save(BeerInventory.builder().beerId(newInventoryEvent.getBeerDto().getId())
				.upc(newInventoryEvent.getBeerDto().getUpc())
				.quantityOnHand(newInventoryEvent.getBeerDto().getQuantityOnHand())
				.build());
	}
}
