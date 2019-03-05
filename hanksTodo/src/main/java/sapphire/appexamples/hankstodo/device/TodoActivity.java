package sapphire.appexamples.hankstodo.device;

import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import amino.run.app.Language;
import amino.run.app.MicroServiceSpec;
import amino.run.app.Registry;
import amino.run.common.MicroServiceID;
import amino.run.kernel.server.KernelServer;
import amino.run.kernel.server.KernelServerImpl;
import sapphire.appexamples.hankstodo.app.TodoList;
import sapphire.appexamples.hankstodo.app.TodoListManager;

import static java.lang.Thread.sleep;

public class TodoActivity {
	public static TodoListManager tlm;

	public static void setObject(String[] args){
		java.rmi.registry.Registry registry;
		try {
			registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
			Registry server = (Registry) registry.lookup("SapphireOMS");

			KernelServer nodeServer = new KernelServerImpl(new InetSocketAddress(args[2], Integer.parseInt(args[3])), new InetSocketAddress(args[0], Integer.parseInt(args[1])));

			MicroServiceSpec spec = MicroServiceSpec.newBuilder()
					.setLang(Language.java)
					.setJavaClassName(TodoListManager.class.getName())
					.create();

			MicroServiceID microServiceID = server.create(spec.toString());
			tlm = (TodoListManager)server.acquireStub(microServiceID);
			System.out.println("Received tlm: " + tlm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createNewToDoList(String id) {
		TodoList tl = null;
		try {
			tl = tlm.newTodoList(id);
			// Consensus policy needs some time after creating new Sapphire object; otherwise,
			// leader election may fail.
			sleep(7000);
			System.out.println("Received tl: " + tl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addTaskItem(String subject, String content) {
		TodoList tl = null;
		try {
			tl = tlm.newTodoList(subject);
			String outcome = tl.addToDo(subject, content);
			System.out.println(outcome);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeTaskItem(String subject, String content) {
		TodoList tl = null;
		try {
			tl = tlm.newTodoList(subject);
			tl.removeToDo(subject, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String fetchToDoItems(String id) {
		TodoList tl;
		String myItems = null;
		try {
			tl = tlm.newTodoList(id);
			myItems = tl.getToDo(id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return myItems;
	}

	public static ArrayList<String> fetchToDoLists() {
		return tlm.getAllTodoLists();
	}

	public static void removeToDo(String id) {
		tlm.deleteTodoList(id);
	}
}
