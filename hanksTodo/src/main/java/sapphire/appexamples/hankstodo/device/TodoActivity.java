package sapphire.appexamples.hankstodo.device;

import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;

import amino.run.app.DMSpec;
import amino.run.app.Language;
import amino.run.app.MicroServiceSpec;
import amino.run.app.Registry;
import amino.run.common.MicroServiceCreationException;
import amino.run.common.MicroServiceID;
import amino.run.kernel.server.KernelServer;
import amino.run.kernel.server.KernelServerImpl;
import amino.run.policy.dht.DHTPolicy;
import sapphire.appexamples.hankstodo.app.TodoList;
import sapphire.appexamples.hankstodo.app.TodoListManager;



public class TodoActivity {
	public static TodoList tl;
	public static TodoListManager tlm;

	public static void setObject(String[] args){
		java.rmi.registry.Registry registry;
		try {
			registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
			Registry server = (Registry) registry.lookup("SapphireOMS");

			KernelServer nodeServer = new KernelServerImpl(new InetSocketAddress(args[2], Integer.parseInt(args[3])), new InetSocketAddress(args[0], Integer.parseInt(args[1])));

			MicroServiceSpec spec = MicroServiceSpec.newBuilder()
					.setLang(Language.java)
					.setJavaClassName("sapphire.appexamples.hankstodo.app.TodoListManager").addDMSpec(
					DMSpec.newBuilder()
							.setName(DHTPolicy.class.getName())
							.create())
					.create();

			MicroServiceID microServiceId = server.create(spec.toString());
			tlm = (TodoListManager)server.acquireStub(microServiceId);
			System.out.println("Received tlm: " + tlm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createNewToDoList(String listName) throws MicroServiceCreationException {
		tl = tlm.newTodoList(listName);
		System.out.println("Received tl1: " + tl);
	}

	public static void addTaskItem(String item) {
		String outcome = tl.addToDo(item);
		System.out.println(outcome);
	}

	public static void removeTaskItem(int pos) {
		tl.removeToDo(pos);
	}
}
