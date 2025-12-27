package client.map;

import java.util.function.Consumer;

import client.map.validation.ValidationResult;
import messagesbase.messagesfromserver.FullMap;

public interface IMapHalfService {

	void executeMapService(FullMap fullMap, Consumer<ValidationResult> onValidationResult);
}
