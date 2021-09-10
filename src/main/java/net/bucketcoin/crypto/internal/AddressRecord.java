package net.bucketcoin.crypto.internal;

import lombok.SneakyThrows;
import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AddressRecord {

	@SuppressWarnings("InstantiationOfUtilityClass")
	private static final AddressRecord adr = new AddressRecord();

	public static AddressRecord getInstance() {
		return adr;
	}

	@SneakyThrows
	private AddressRecord() {
		var opt = new Options();
		opt.createIfMissing();
		try(var db = factory.open(new File("BCKTAddressRecord"), opt)) {
			AtomicInteger k = new AtomicInteger();
			db.forEach(entry -> k.getAndIncrement());
		}
	}

}
