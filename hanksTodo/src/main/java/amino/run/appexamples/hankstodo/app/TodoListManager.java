package amino.run.appexamples.hankstodo.app;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import amino.run.app.DMSpec;
import amino.run.app.Language;
import amino.run.app.MicroService;
import amino.run.app.MicroServiceSpec;
import amino.run.common.MicroServiceCreationException;
import amino.run.policy.dht.DHTPolicy;
import amino.run.policy.replication.ConsensusRSMPolicy;

import static amino.run.runtime.MicroService.delete_;
import static amino.run.runtime.MicroService.new_;

public class TodoListManager implements MicroService {
    private static final Logger logger = Logger.getLogger(TodoListManager.class.getName());

    LinkedHashMap<String, TodoList> todoLists = new LinkedHashMap<>();

	public void doSomething(String input) { logger.info("Input received: " + input); }

	public TodoList newTodoList(String id) throws MicroServiceCreationException {
		TodoList t = todoLists.get(id);
		if (t == null) {
			MicroServiceSpec spec;
			spec = MicroServiceSpec.newBuilder()
					.setLang(Language.java)
					.setJavaClassName(TodoList.class.getName())
					.addDMSpec(
							DMSpec.newBuilder()
									.setName(DHTPolicy.class.getName())
									.create())
					.addDMSpec(
							DMSpec.newBuilder()
									.setName(ConsensusRSMPolicy.class.getName())
									.create())
					.create();

			t = (TodoList) new_(spec, id);
			todoLists.put(id, t);
			logger.info("Created new Todo list with ID: "+ id);
		} else {
			logger.info("ToDoList for ID: "+ id + " exists.");
		}
		return t;
	}

	public ArrayList<String> getAllTodoLists() {
		if(!todoLists.isEmpty()) {
			ArrayList<String> todoList = new ArrayList();
			for (Map.Entry<String, TodoList> todo:todoLists.entrySet()){
				todoList.add(todo.getKey());
			}
			return todoList;
		}
		return null;
	}

	public TodoList getToDoList(String id) {
		return todoLists.get(id);
	}

	public void deleteTodoList(String id) {
		TodoList t = todoLists.remove(id);
		if (t != null) {
			delete_(t);
		}
		logger.info("ToDoList: " + id + " deleted.");
	}
}
