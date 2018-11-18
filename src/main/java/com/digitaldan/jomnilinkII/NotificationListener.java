package com.digitaldan.jomnilinkII;

import com.digitaldan.jomnilinkII.MessageTypes.ObjectStatus;
import com.digitaldan.jomnilinkII.MessageTypes.systemEvents.SystemEvent;

public interface NotificationListener {
	void objectStatusNotification(ObjectStatus status);

	void systemEventNotification(SystemEvent event);
}
