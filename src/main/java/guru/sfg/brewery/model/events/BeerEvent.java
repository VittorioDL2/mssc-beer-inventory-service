package guru.sfg.brewery.model.events;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class BeerEvent implements Serializable {
	
	static final long serialVersionUID = 123123L;
	private BeerDto beerDto;
	

}
