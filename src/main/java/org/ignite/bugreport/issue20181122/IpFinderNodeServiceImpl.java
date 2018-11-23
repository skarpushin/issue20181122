package org.ignite.bugreport.issue20181122;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.ignite.spi.IgniteSpiContext;
import org.apache.ignite.spi.IgniteSpiException;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * That's a stripped-down version of a custom IpFinder which represents the one
 * I have in my project.
 */
public class IpFinderNodeServiceImpl implements TcpDiscoveryIpFinder {
	private Logger log = LoggerFactory.getLogger(IpFinderNodeServiceImpl.class);

	@SuppressWarnings("unused")
	private IgniteSpiContext spiCtx;
	private Set<InetSocketAddressComparable> locallyAdded = new TreeSet<>();
	private Set<InetSocketAddressComparable> locallyRemoved = new TreeSet<>();

	private TreeSet<InetSocketAddressComparable> addressesFromDb = new TreeSet<>(Arrays.asList(
			new InetSocketAddressComparable("10.9.9.1", 47500), new InetSocketAddressComparable("10.9.9.1", 47501)));

	@Override
	public void onSpiContextInitialized(IgniteSpiContext spiCtx) throws IgniteSpiException {
		this.spiCtx = spiCtx;
	}

	@Override
	public void onSpiContextDestroyed() {
		spiCtx = null;
	}

	@Override
	public void initializeLocalAddresses(Collection<InetSocketAddress> addrs) throws IgniteSpiException {
		locallyAdded.clear();
		if (addrs.isEmpty()) {
			log.info("Registered initial addresses as empty list");
			return;
		}

		addrs.stream().map(x -> new InetSocketAddressComparable(x)).forEach(x -> locallyAdded.add(x));
		log.info("Registered initial addresses: {}", addrs);
	}

	@Override
	public Collection<InetSocketAddress> getRegisteredAddresses() throws IgniteSpiException {
		Set<InetSocketAddressComparable> addrs = new TreeSet<>();
		addrs.addAll(locallyAdded);
		addrs.addAll(addressesFromDb);
		addrs.removeAll(locallyRemoved);

		List<InetSocketAddress> ret = addrs.stream().map(InetSocketAddress.class::cast).collect(Collectors.toList());
		log.info("getRegisteredAddresses)( returning: {}", ret);
		return ret;
	}

	@Override
	public boolean isShared() {
		return true;
	}

	@Override
	public void registerAddresses(Collection<InetSocketAddress> addrs) throws IgniteSpiException {
		log.info("Register additional addresses: {}", addrs);
		Set<InetSocketAddressComparable> addrsComparable = addrs.stream().map(x -> new InetSocketAddressComparable(x))
				.collect(Collectors.toSet());
		locallyAdded.addAll(addrsComparable);
		locallyRemoved.removeAll(addrsComparable);
	}

	@Override
	public void unregisterAddresses(Collection<InetSocketAddress> addrs) throws IgniteSpiException {
		log.info("UnRegister additional addresses: {}", addrs);
		Set<InetSocketAddressComparable> addrsComparable = addrs.stream().map(x -> new InetSocketAddressComparable(x))
				.collect(Collectors.toSet());
		locallyAdded.removeAll(addrsComparable);
		locallyRemoved.addAll(addrsComparable);
	}

	@Override
	public void close() {
		// no-op
	}
}
