package guru.sfg.beer.inventory.service.services;

import org.springframework.stereotype.Component;

import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.common.events.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewInventoryListener {
	private final BeerInventoryRepository beerInventoryRepository;
	
	public void listen(NewInventoryEvent newInventoryEvent) {
		
	}
}
