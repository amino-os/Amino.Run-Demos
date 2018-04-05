package sapphire.policy;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.logging.Logger;

import sapphire.kernel.common.GlobalKernelReferences;
import sapphire.kernel.server.KernelServerImpl;
import sapphire.oms.OMSServer;

/**
 * This class defines ShiftPolicy which serves the purpose of demonstration only.
 * Note that it does not provide any values; thus, should not be used outside of testing or demonstration.
 * It moves the Sapphire object to another Sapphire Kernel server when the number of RPC (RMI) is more than 5.
 */
public class MigrationPolicy extends SapphirePolicy {

	public static class MigrationClientPolicy extends DefaultSapphirePolicy.DefaultClientPolicy {
		SapphireServerPolicy server = null;
		SapphireGroupPolicy group = null;

		@Override
		public void onCreate(SapphireGroupPolicy group) {
			this.group = group;
		}

		@Override
		public void setServer(SapphireServerPolicy server) {
			this.server = server;
		}

		@Override
		public SapphireServerPolicy getServer() {
			return server;
		}

		@Override
		public SapphireGroupPolicy getGroup() {
			return group;
		}
	}

	public static class MigrationServerPolicy extends DefaultSapphirePolicy.DefaultServerPolicy {
		private static Logger logger = Logger.getLogger(SapphireServerPolicy.class.getName());

		private SapphireGroupPolicy group = null;

		@Override
		public void onCreate(SapphireGroupPolicy group) {
			this.group = group;
		}

		@Override
		public SapphireGroupPolicy getGroup() {
			return group;
		}

		@Override
		public void onMembershipChange() {
			super.onMembershipChange();
		}

		@Override
		public Object onRPC(String method, ArrayList<Object> params) throws Exception {

			if (isMigrateObject(method)) {
				migrateObject((InetSocketAddress) params.get(0));
				return null;
			} else {
				Object obj = super.onRPC(method, params);
				return obj;
			}
		}

		/**
		 * Migrates Sapphire Object to different Server
		 *
		 * @throws Exception migrateObject migrates the object to the specified Kernel Server
		 */
		public void migrateObject(InetSocketAddress destinationAddr) throws Exception {
			logger.info("Performing Explicit Migration of the object to Destination Kernel Server with address as " + destinationAddr);
//			OMSServer oms = GlobalKernelReferences.nodeServer.oms;
			OMSServer oms = GlobalKernelReferences.nodeServer.cloudOms;
			ArrayList<InetSocketAddress> servers = oms.getServers();

			KernelServerImpl localKernel = GlobalKernelReferences.nodeServer;
			InetSocketAddress localAddress = localKernel.getLocalHost();

			logger.info("Performing Explicit Migration of object from " + localAddress + " to " + destinationAddr);
//
//			if (!(servers.contains(destinationAddr))) {
//				// Even if servers do not look to contain destinationAddr, it could be due to the difference in private IP.
//				// For example, AWS has public and private IP addresses and this could result in a difference even though they
//				// point to the same if destinationAddr only contains public IPs.
//				boolean contains = false;
//
//				for (InetSocketAddress server: servers) {
//					if (destinationAddr.getPort() == server.getPort()) {
//						if (destinationAddr.getHostName() == server.getHostName() ||
//								destinationAddr.getAddress() == server.getAddress()) {
//							contains = true;
//							break;
//						}
//					}
//				}
//
//				if (!contains)
//					throw new Exception("The destinations address passed is not present as one of the Kernel Servers" + destinationAddr);
//			}
//
//			if (!localAddress.equals(destinationAddr)) {
//				if (localAddress.getPort() != destinationAddr.getPort() || localAddress.getHostName() != destinationAddr.getHostName()) {
//					localKernel.moveKernelObjectToServer(destinationAddr, this.oid);
//				}
//			}

			if (servers == null || servers.size() == 0) {
				logger.warning("No servers to migrate object to.");
			}
			InetSocketAddress chosenAddress = servers.get(0);
			localKernel.moveKernelObjectToRemoteServer(chosenAddress, this.oid);

			logger.info("Successfully performed Explicit Migration of object from " + localAddress + " to " + destinationAddr);
		}

		private Boolean isMigrateObject(String method) {
			// TODO better check than simple base name
			return method.contains(".migrateObject(");
		}
	}

	public static class MigrationGroupPolicy extends DefaultSapphirePolicy.DefaultGroupPolicy {
	}
}