package sapphire.appexamples.minnietwitter.app;

import java.util.ArrayList;
import java.util.List;

import sapphire.app.DMSpec;
import sapphire.app.Language;
import sapphire.app.SapphireObject;
import sapphire.app.SapphireObjectSpec;
import sapphire.policy.atleastoncerpc.AtLeastOnceRPCPolicy;

import static sapphire.runtime.Sapphire.*;

public class User implements SapphireObject {
	private Timeline timeline;
	private UserInfo ui;
	List<User> followers;
	List<User> following;
	TagManager tagManager;
	
	public User(UserInfo ui, TagManager tm) {
		this.ui = ui;
		this.followers = new ArrayList<User>();
		this.following = new ArrayList<User>();
		tagManager = tm;
	}

	public void initialize(User u) {
		SapphireObjectSpec timelineSpec;
		timelineSpec = SapphireObjectSpec.newBuilder()
				.setLang(Language.java)
				.setJavaClassName(Timeline.class.getName())
				.addDMSpec(
						DMSpec.newBuilder()
								.setName(AtLeastOnceRPCPolicy.class.getName())
								.create())
				.create();

		timeline = (Timeline) new_(timelineSpec, u, tagManager);
		timeline.initialize(timeline);
	}

	public void deInitialize() {
		timeline.deInitialize();
		delete_(timeline);
	}
	
	public List<User> getFollowers(int from, int to) {
		return Util.checkedSubList(followers, from, to);
	}
	
	public List<User> getFollowing(int from, int to) {
		return Util.checkedSubList(following, from, to);
	}
	
	public UserInfo getUserInfo() {
		return ui;
	}
	
	public Timeline getTimeline() {
		return timeline;
	}
	
	public void addFollower(User u) {
		followers.add(u);
	}
	
	public void addFollowing(User u) {
		following.add(u);
	}
}
