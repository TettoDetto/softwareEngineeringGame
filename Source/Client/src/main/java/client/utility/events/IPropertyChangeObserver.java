package client.utility.events;

public interface IPropertyChangeObserver<T> {

	void propertyChange(PropertyChangeEvent<T> event);

}
