package sapphire.oms;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import sapphire.kernel.server.KernelServer;

/**
 * Manages Sapphire kernel servers. Tracks which servers are up, which regions each server belongs to, etc.
 * @author iyzhang
 *
 */
public class KernelServerManager {
	Logger logger = Logger.getLogger("sapphire.oms.KernelServerManager");
	
	private ConcurrentHashMap<InetSocketAddress, KernelServer> servers;
	private ConcurrentHashMap<String, ArrayList<InetSocketAddress>> regions;

	public KernelServerManager() throws IOException, NotBoundException, JSONException {
		servers = new ConcurrentHashMap<InetSocketAddress, KernelServer>();
		regions = new ConcurrentHashMap<String, ArrayList<InetSocketAddress>>();
	}
	
	public void registerKernelServer(InetSocketAddress address) throws RemoteException, NotBoundException {
		//this.servers.putIfAbsent(address, null);
		logger.info("New kernel server: " + address.toString());
		// TODO For now, let each server be a region
		ArrayList<InetSocketAddress> region = new ArrayList<InetSocketAddress>();
		region.add(address);
		regions.put(address.toString(), region);
	}
	
    /**
     * Registers the kernel server to the specified region
     * @param region
     * @param address
     * @throws RemoteException
     * @throws NotBoundException
     */
    public void registerKernelServerWithRegion(String region, InetSocketAddress address) throws RemoteException, NotBoundException {
        logger.info("New kernel server: " + address.toString() + " in region " + region);

        ArrayList<InetSocketAddress> serverList = regions.get(region);
        if (null == serverList) {
            serverList = new ArrayList<InetSocketAddress>();
        }

        serverList.add(address);
        regions.put(region, serverList);
    }

	/**
     */
    public ArrayList<InetSocketAddress> getServers() {
    	ArrayList<InetSocketAddress> nodes = new ArrayList<InetSocketAddress>();

    	for (ArrayList<InetSocketAddress> addresses : this.regions.values()) {
    	    for (InetSocketAddress address: addresses) {
                nodes.add(address);
            }
        }

        return nodes;
    }

    public ArrayList<String> getRegions() {
    	return new ArrayList<String>(regions.keySet());
    }
    
    public KernelServer getServer(InetSocketAddress address) {
    	if (servers.containsKey(address)) {
    		return servers.get(address);
    	} else {
    		KernelServer server = null;
    		try {
    			Registry registry = LocateRegistry.getRegistry(address.getHostName(), address.getPort());
    			server = (KernelServer) registry.lookup("SapphireKernelServer");
    			servers.put(address, server);
    		} catch (Exception e) {
    			logger.log(Level.SEVERE, "Could not find kernel server: "+e.toString());
    		}
			return server;
    	}
    }
    
    public InetSocketAddress getServerInRegion(String region) {
    	return regions.get(region).get(0);
    }

    /**
     * Gets all the servers in the region
     * @param region
     * @return list of kernel server host addresses in the given region otherwise null
     */
    public ArrayList<InetSocketAddress> getServersInRegion(String region) {
        return regions.get(region);
    }
}
