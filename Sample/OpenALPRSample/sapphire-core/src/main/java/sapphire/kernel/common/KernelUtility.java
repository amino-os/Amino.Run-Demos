package sapphire.kernel.common;

import java.net.InetSocketAddress;

import sapphire.common.Configuration;
import sun.security.krb5.Config;

/**
 * Utilties for Sapphire KernelServer
 * @author SMoon
 *
 */

public class KernelUtility {

	/***
	 * Note that IP address or host name should be should depending the network environment.
	 * For example, cloud instance will have to return public DNS name if IP address is private internal network use only.
	 * In a company network, it may need to return IP address instead of host name.
	 * @param host: current host name.
	 * @return host name string - either IP address or DNS name.
	 */
	public static String getHostName(InetSocketAddress host) {

		if (Configuration.useIpAddress) {
			System.out.println("(useIpAddress is on) KernelUtil will return: " + host.getAddress().getHostAddress());
			return host.getAddress().getHostAddress();
		} else {
			return host.getHostName();
		}
	}
}
