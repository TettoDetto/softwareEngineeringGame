package client.utility.events;

@FunctionalInterface
public interface EventListener<T> {
	void onEvent(T oldValue, T newValue);
}
