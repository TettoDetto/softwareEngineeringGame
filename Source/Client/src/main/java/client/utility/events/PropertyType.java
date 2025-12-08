package client.utility.events;

import java.util.Objects;

public class PropertyType<T> {

	private final String name;
	private final EPropertyChangeEventType type;

	private PropertyType(String name, EPropertyChangeEventType type) {
		this.name = name;
		this.type = type;
	}

	public static <T> PropertyType<T> of(String name, EPropertyChangeEventType type) {
		return new PropertyType<>(name, type);
	}

	public String getName() {
		return name;
	}

	public EPropertyChangeEventType getType() {
		return type;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof PropertyType<?> here)) {
			return false;
		}
		return Objects.equals(name, here.name) && Objects.equals(type, here.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, type);
	}

}
