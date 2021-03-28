package shared.util;

import java.beans.PropertyChangeListener;

public interface Subject {
	void addListener(String propertyName, PropertyChangeListener listener);

	void addListener(PropertyChangeListener listener);

	void removeListener(String propertyName, PropertyChangeListener listener);

	void removeListener(PropertyChangeListener listener);

	}

