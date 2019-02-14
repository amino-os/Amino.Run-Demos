package amino.run.appexamples.minnietwitter.app;

import amino.run.app.DMSpec;
import amino.run.app.Language;
import amino.run.app.SapphireObject;
import amino.run.app.SapphireObjectSpec;
import amino.run.policy.atleastoncerpc.AtLeastOnceRPCPolicy;
import static amino.run.runtime.Sapphire.*;

public class TwitterManager implements SapphireObject {
	private UserManager userManager;
	private TagManager tagManager;
	
	public TwitterManager() {
		MicroServiceSpec tagManagerSpec,userManagerSpec;

		tagManagerSpec = MicroServiceSpec.newBuilder()
				.setLang(Language.java)
				.setJavaClassName(TagManager.class.getName())
				.create();
	    userManagerSpec = MicroServiceSpec.newBuilder()
				.setLang(Language.java)
				.setJavaClassName(UserManager.class.getName())
				.addDMSpec(
						DMSpec.newBuilder()
								.setName(AtLeastOnceRPCPolicy.class.getName())
								.create())
				.create();
		try {
			tagManager = (TagManager) new_(tagManagerSpec);
			userManager = (UserManager) new_(userManagerSpec, tagManager);
		} catch (MicroServiceCreationException e) {
			e.printStackTrace();
		}
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public TagManager getTagManager() {
		return tagManager;
	}

	public void deInitialize() {
		delete_(tagManager);
		delete_(userManager);
	}
}
