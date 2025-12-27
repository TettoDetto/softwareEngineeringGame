package client.map.validation;

public enum EValidationResults {
	INVALID_START_COORDINATES, UNREACHABLE_LAND, UNEXPECTED_ERROR, MAP_CORRECT;
	
	@Override
	public String toString() {
		switch(this) {
		case INVALID_START_COORDINATES:
			return "Invalid starting coordinates, they were outside of the map";
		case UNREACHABLE_LAND:
			return "The validator found an unreachable field on the map";
		case UNEXPECTED_ERROR:
			return "An unexpected error occured during validation";
		case MAP_CORRECT:
			return "The map is correct";
		default:
			return "";
		}
		
	}
}
