package guru.sfg.beer.inventory.service.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {

	private final BeerInventoryRepository beerInventoryRepository;
	
	@Override
	public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
		log.debug("Allocationg : " + beerOrderDto.getId());
		AtomicInteger totalOrdered = new AtomicInteger();
		AtomicInteger totalAllocated = new AtomicInteger();
		
		beerOrderDto.getBeerOrderLines().forEach(line -> {
			if(((line.getOrderQuantity() != null ? line.getOrderQuantity() : 0)
					- (line.getQuantityAllocated() != null ? line.getQuantityAllocated() : 0)) > 0 ) {
				allocateBeerOrderLine(line);
			}
			totalOrdered.set(totalOrdered.get() + line.getOrderQuantity());
			totalAllocated.set(totalAllocated.get() + (line.getQuantityAllocated() != null ? line.getQuantityAllocated() : 0));
		});
		return totalOrdered.get() == totalAllocated.get();
	}

	private void allocateBeerOrderLine(BeerOrderLineDto line) {
		List<BeerInventory> inventoryList = beerInventoryRepository.findAllByUpc(line.getUpc());
		
		inventoryList.forEach(entry -> {
			int inventory = entry.getQuantityOnHand() == null ? 0 : entry.getQuantityOnHand();
			int orderQty = line.getOrderQuantity() == null ? 0 : line.getOrderQuantity();
			int allocateQty = line.getQuantityAllocated() == null ? 0 : line.getQuantityAllocated();
			int qtyToAllocate = orderQty-allocateQty;
			
			if(inventory >= qtyToAllocate) {
				inventory = inventory - qtyToAllocate;
				line.setQuantityAllocated(orderQty);
				entry.setQuantityOnHand(inventory);
				
				beerInventoryRepository.save(entry);
			} else if(inventory > 0) {
				line.setQuantityAllocated(allocateQty + inventory);
				entry.setQuantityOnHand(0);
				
				beerInventoryRepository.delete(entry);
			}
			
		});
	}
}
