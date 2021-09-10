package net.bucketcoin.crypto.state;

import lombok.Getter;
import lombok.SneakyThrows;
import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class StateTrie {

	@SuppressWarnings("InstantiationOfUtilityClass")
	private static final StateTrie adr = new StateTrie(); // This is a singleton class.

	public static StateTrie getInstance() {
		return adr;
	}

	@SneakyThrows
	private StateTrie() {
		var opt = new Options();
		opt.createIfMissing();
		try(var db = factory.open(new File("BCKTAddressRecord"), opt)) {
			AtomicInteger k = new AtomicInteger();
			db.forEach(entry -> k.getAndIncrement());
		}
	}

	@Getter
	public static record AddressProperties(int nonce, double balance, StorageTrie storageRoot, String codeHash) {}

}
