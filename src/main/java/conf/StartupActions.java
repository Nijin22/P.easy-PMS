package conf;

import javax.inject.Singleton;

import ninja.lifecycle.Start;
import ninja.utils.NinjaProperties;

import com.google.inject.Inject;

@Singleton
public class StartupActions {

	@SuppressWarnings("unused") // Might be useful later...
	private NinjaProperties ninjaProperties;

	@Inject
	public StartupActions(NinjaProperties ninjaProperties) {
		this.ninjaProperties = ninjaProperties;
	}

	@Start(order = 100)
	public void generateDummyDataWhenInTest() {
		// We can generate example data in test mode here.

	}

}
