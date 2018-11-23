package org.ignite.bugreport.issue20181122;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * I want to use Sets to get rid of duplicates, but {@link InetSocketAddress}
 * doesn't implement {@link Comparable} interface
 * 
 * @author sergeyk
 *
 */
public class InetSocketAddressComparable extends InetSocketAddress implements Comparable<InetSocketAddressComparable> {
	private static final long serialVersionUID = 3497996586166070598L;

	public InetSocketAddressComparable(String hostname, int port) {
		super(hostname, port);
	}

	public InetSocketAddressComparable(InetAddress addr, int port) {
		super(addr, port);
	}

	public InetSocketAddressComparable(InetSocketAddress x) {
		super(x.getAddress(), x.getPort());
	}

	@Override
	public int compareTo(InetSocketAddressComparable o) {
		return toString().compareTo(o.toString());
	}
}