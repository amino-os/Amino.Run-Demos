package sapphire.appexamples.minnietwitter.app;

import sapphire.app.DMSpec;
import sapphire.app.Language;
import sapphire.app.SapphireObject;
import sapphire.app.SapphireObjectSpec;
import sapphire.policy.atleastoncerpc.AtLeastOnceRPCPolicy;
import static sapphire.runtime.Sapphire.*;

public class TwitterManager implements SapphireObject {
	private UserManager userManager;
	private TagManager tagManager;
	
	public TwitterManager() {
		SapphireObjectSpec tagManagerSpec,userManagerSpec;

		tagManagerSpec = SapphireObjectSpec.newBuilder()
				.setLang(Language.java)
				.setJavaClassName(TagManager.class.getName())
				.create();
	    userManagerSpec = SapphireObjectSpec.newBuilder()
				.setLang(Language.java)
				.setJavaClassName(UserManager.class.getName())
				.addDMSpec(
						DMSpec.newBuilder()
								.setName(AtLeastOnceRPCPolicy.class.getName())
								.create())
				.create();

		tagManager = (TagManager) new_(tagManagerSpec);
		userManager = (UserManager) new_(userManagerSpec, tagManager);
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
