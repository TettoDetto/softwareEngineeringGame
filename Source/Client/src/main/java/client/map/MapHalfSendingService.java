package client.map;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.placers.MapNode;
import client.map.validation.MapValidator;
import client.map.validation.ValidationResult;
import client.network.Network;
import messagesbase.messagesfromserver.FullMap;

public class MapHalfSendingService {
	private final int MAX_ATTEMPTS = 10;
	private final Logger logger = LoggerFactory.getLogger(MapHalfSendingService.class);
	private Network currentNetwork;
	private FullMap fullMap;
	private Consumer<ValidationResult> onValidationResult;

	/**
	 * Used for creating, validating and sending a HalfMap
	 * 
	 * 
	 * @param currentNetwork The network instance used when starting the game. Used to send the map over the network to the server
	 */
	public MapHalfSendingService(Network currentNetwork, FullMap fullMap, Consumer<ValidationResult> onValidationResult) {
		this.currentNetwork = currentNetwork;
		this.fullMap = fullMap;
		this.onValidationResult = onValidationResult;
	}
	
	public void generateAndSendMap() {
		
		for (int i = 0; i < MAX_ATTEMPTS; i++) {
			
			MapNode[][] mapHalf = generateMapHalf();
			ValidationResult result = validateAndSend(mapHalf);
			
			if (onValidationResult != null) {
				onValidationResult.accept(result);
			}
			
			if (result.getIsValidMap()) {
				logger.info("Sent half map, moving to movement!");
				return;
			}
			
			else {
				logger.error("The map creator made a mistake while creating. The map has to be redone!");
				continue;
			}
		}
	}
		
	/**
	 * 
	 * Calls the according {@link MapHalfGenerator} and generates a MapHalf. If the {@link FullMap} 
	 * which is at the server when calling the {@link MapHalfGenerator} is not empty, the {@link SecondMapHalfGenerator} is called. 
	 * Else the {@link FirstHalfMapGenerator} is called.
	 * 
	 * @return A MapHalf, which has yet to be validated
	 */
	private MapNode[][] generateMapHalf() {
		if (!fullMap.isEmpty()) {
			return new SecondMapHalfGenerator(fullMap).getMap();
		}
		else {
			return new FirstMapHalfGenerator().getMap();
		}
	}
	
	/**
	 * Calls the {@link MapValidator} which returns a {@link ValidationResult}. The {@link ValidationResult} holds information 
	 * about the success of the {@link MapValidator}. If the result is valid, the MapHalf is sent, if not, the method returns.
	 * 
	 * @param mapHalf The generated MapHalf
	 * @return The {@link ValidationResult}
	 */
	private ValidationResult validateAndSend(MapNode[][] mapHalf) {
		
		MapValidator validator = new MapValidator(mapHalf);
		ValidationResult result = validator.isValidMap();
		
		if (result.getIsValidMap()) {
			currentNetwork.sendMapHalf(mapHalf);
		}

		
		return result;
	}
	
}
