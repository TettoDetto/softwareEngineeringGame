package client.utility.events;

import java.util.Objects;

public class PropertyChangeEvent<T> {

	private final Object source;
	private final T oldValue;
	private final T newValue;
	private final EPropertyChangeEventType type;

	public PropertyChangeEvent(Object source, T oldValue, T newValue, EPropertyChangeEventType propType) {
		this.type = propType;
		this.newValue = newValue;
		this.oldValue = oldValue;
		this.source = source;
	}

	public Object getSource() {
		return source;
	}

	public T getOldValue() {
		return oldValue;
	}

	public T getNewValue() {
		return newValue;
	}

	public EPropertyChangeEventType getType() {
		return type;
	}

	public boolean hasChanged() {
		return !Objects.equals(oldValue, newValue);
	}

}
