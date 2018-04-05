package sapphire.oms;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.JSONException;

import sapphire.kernel.server.KernelServer;

/**
 * Manages Sapphire kernel servers. Tracks which servers are up, which regions each server belongs to, etc.
 * @author iyzhang
 * TODO (smoon, 1/12/2018): regions is not used in the current code path; therefore, region is defined by IP address instead. *
 */
public class KernelServerManager {
	Logger logger = Logger.getLogger("sapphire.oms.KernelServerManager");

	private ConcurrentHashMap<InetSocketAddress, KernelServer> servers;
	private ConcurrentHashMap<String, ArrayList<InetSocketAddress>> regions;

	public KernelServerManager() throws IOException, NotBoundException, JSONException {
		servers = new ConcurrentHashMap<InetSocketAddress, KernelServer>();
		regions = new ConcurrentHashMap<String, ArrayList<InetSocketAddress>>();
		logger.setLevel(Level.ALL);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		handler.setFormatter(new SimpleFormatter());
		logger.addHandler(handler);
	}

    /**
     * Sets the address as a region.
     * @author iyzhang, smoon
     * @param address
     * @throws RemoteException
     * @throws NotBoundException
     */
	public void registerKernelServer(InetSocketAddress address) throws RemoteException, NotBoundException {
	    // TODO (smoon, 1/12/2018): For now, put address as a region name when region name is null but this should be changed later.
        this.registerKernelServer(address, address.toString());
	}

	/**
	 * Sets the address and region.
     * @author smoon
 	 * @param address
	 * @param region
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void registerKernelServer(InetSocketAddress address, String region) throws RemoteException, NotBoundException {
	    ArrayList<InetSocketAddress> addresses;
	    if (region == null || region.length() == 0) {
	    	logger.warning("Region parameter is null or empty. Converting region to address: " + address.toString());
	    	region = address.toString();
		}
	    logger.info("New kernel server: " + address.toString() + " Region: " + region);

	    addresses = regions.containsKey(region)? regions.get(region): new ArrayList<InetSocketAddress>();

        if (!addresses.contains(address)) {
		    addresses.add(address);
		    regions.put(region, addresses);
		    System.out.println("Current regions: ");

			for (Map.Entry<String, ArrayList<InetSocketAddress>> entry : regions.entrySet()) {
				System.out.println(entry.getKey() + entry.getValue().toString());
			}
		}
	}

	/**
	 * Get servers in all regions.
	 * @return all servers.
	 */
    public ArrayList<InetSocketAddress> getServers() {

		ArrayList<InetSocketAddress> nodes = new ArrayList<InetSocketAddress>();
		for (ArrayList<InetSocketAddress> addresses : regions.values()) {
			for (InetSocketAddress address: addresses) {
				nodes.add(address);
			}
		}
		return nodes;
    }

    public ArrayList<String> getRegions() {
		// servers.keySet() is only available > Android API 24. Therefore, below implementation replaces it.
		ArrayList<String> regionsKeys = new ArrayList<String>();
		logger.info("getRegions: ");

    	for (Map.Entry<String, ArrayList<InetSocketAddress>> entry : regions.entrySet()) {
    		regionsKeys.add(entry.getKey());
		}
		logger.info("Number of region keys: " + regionsKeys);
		return regionsKeys;
//		return new ArrayList<String>(regions.keySet());
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
    
    public InetSocketAddress getHostNameInRegion(String region) {
		System.out.println("getServerInRegion at " + region);
		for (InetSocketAddress regionStr: regions.get(region)) {
			System.out.println("Found:" +  regionStr);
		}

		return regions.get(region).get(0);
    }
}
