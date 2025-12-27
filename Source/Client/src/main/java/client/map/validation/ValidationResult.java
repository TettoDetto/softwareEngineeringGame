package client.map.validation;

public class ValidationResult {

	private boolean validMap;
	private EValidationResults result;
	
	public ValidationResult(boolean validMap, EValidationResults result) {
		this.validMap = validMap;
		this.result = result;
	}
	
	public boolean getIsValidMap() {
		return validMap;
	}
	
	public EValidationResults getResult() {
		return result;
	}
	
	
}
