package client.utility.events;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyChangeSupport {

	private final static Logger logger = LoggerFactory.getLogger(PropertyChangeSupport.class);

	private final List<IPropertyChangeListener> listeners;
	private final Map<PropertyType<?>, List<IPropertyChangeObserver<?>>> observer;
	private final Object source;

	public PropertyChangeSupport(Object source) {
		if (source == null) {
			throw new NullPointerException();
		}

		this.source = source;
		this.listeners = new ArrayList<>();
		this.observer = new ConcurrentHashMap<>();
	}

	public <T> void addPropertyChangeObserver(PropertyType<T> propertyType, IPropertyChangeObserver<T> newObserver) {
		if (observer == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}

		observer.computeIfAbsent(propertyType, k -> new CopyOnWriteArrayList<>()).add(newObserver);
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		if (listener == null) {
			throw new InvalidParameterException("Listeners cannot be null");
		}

		listeners.add(listener);

	}

	public <T> void removePropertyChangeObserver(PropertyType<T> type, IPropertyChangeObserver<T> removeObserver) {
		List<IPropertyChangeObserver<?>> observers = observer.get(type);

		if (observers != null) {
			observers.remove(removeObserver);
		}
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		if (listener == null) {
			throw new InvalidParameterException();
		}
		listeners.remove(listener);
	}

	@SuppressWarnings("unchecked")
	public <T> void firePropertyChange(PropertyType<T> propType, T oldValue, T newValue) {

		if (Objects.equals(oldValue, newValue)) {
			return;
		}

		PropertyChangeEvent<T> event = new PropertyChangeEvent<>(source, oldValue, newValue, propType);

		List<IPropertyChangeObserver<?>> observers = observer.get(propType);
		if (observers != null) {
			for (IPropertyChangeObserver<?> o : observers) {
				try {
					((IPropertyChangeObserver<T>) o).propertyChange(event);
				} catch (Exception e) {
					handleObserverException(e, o);
				}
			}
		}
		for (IPropertyChangeListener listener : listeners) {
			try {
				listener.propertyChange(event);
			} catch (Exception e) {
				handleExceptions(e, listener);
			}
		}
	}

	private void handleObserverException(Exception e, IPropertyChangeObserver<?> o) {
		logger.error("Error: {}, when handling listener: {}", e, o.getClass().getName());

	}

	private void handleExceptions(Exception e, IPropertyChangeListener listener) {
		logger.error("Error: {}, when handling listener: {}", e, listener.getClass().getName());

	}

}
