package models.manager;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class CalendarManager {
	@Inject
	Provider<EntityManager> entitiyManagerProvider;

	// TODO: Implement at some point. But not part of the sprint yet.
}
