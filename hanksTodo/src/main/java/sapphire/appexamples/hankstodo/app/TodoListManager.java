package sapphire.appexamples.hankstodo.app;

import java.util.Hashtable;
import java.util.Map;

import amino.run.app.DMSpec;
import amino.run.app.Language;
import amino.run.app.MicroService;
import amino.run.app.MicroServiceSpec;
import amino.run.common.MicroServiceCreationException;
import amino.run.policy.cache.CacheLeasePolicy;
import amino.run.policy.dht.DHTKey;

import static amino.run.runtime.MicroService.new_;

public class TodoListManager implements MicroService {
    Map<DHTKey, TodoList> todoLists = new Hashtable<DHTKey, TodoList>();

	public TodoList newTodoList(String name) throws MicroServiceCreationException {
		TodoList t = todoLists.get(new DHTKey(name));
		if (t == null) {

			MicroServiceSpec spec = MicroServiceSpec.newBuilder()
					.setLang(Language.java)
					.setJavaClassName(TodoList.class.getName()).addDMSpec(
							DMSpec.newBuilder()
									.setName(CacheLeasePolicy.class.getName())
									.create())
					.create();

			t = (TodoList) new_(spec, name);
			todoLists.put(new DHTKey(name), t);
		}
		System.out.println("Created new list");
		System.out.println("This managers lists" + todoLists.toString());
		return t;
	}
}
