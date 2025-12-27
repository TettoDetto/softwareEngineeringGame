package client.utility.events;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyChangeSupport {

	private final static Logger logger = LoggerFactory.getLogger(PropertyChangeSupport.class);

	private final List<IPropertyChangeListener> listeners;
	private final Object source;

	public PropertyChangeSupport(Object source) {
		if (source == null) {
			throw new NullPointerException();
		}

		this.source = source;
		this.listeners = new ArrayList<>();
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		if (listener == null) {
			throw new InvalidParameterException("Listeners cannot be null");
		}

		listeners.add(listener);

	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		if (listener == null) {
			throw new InvalidParameterException();
		}
		listeners.remove(listener);
	}


	public <T> void firePropertyChange(EPropertyChangeEventType type, T oldValue, T newValue) {

		if (Objects.equals(oldValue, newValue)) {
			return;
		}

		PropertyChangeEvent<T> event = new PropertyChangeEvent<>(source, oldValue, newValue, type);
		for (IPropertyChangeListener listener : listeners) {
			try {
				listener.propertyChange(event);
			} catch (Exception e) {
				handleExceptions(e, listener);
			}
		}
	}


	private void handleExceptions(Exception e, IPropertyChangeListener listener) {
		logger.error("Error: {}, when handling listener: {}", e, listener.getClass().getName());

	}
}
