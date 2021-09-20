package net.bucketcoin.contract;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Objects;

public final class AcceptedContract {

	private AcceptedContract(Contract contract) throws NoSuchMethodException {
		this.contract = contract;
	}

	private Contract contract;
	@SuppressWarnings("ConstantConditions")
	private @NotNull final Method executionMethod = Objects.requireNonNull(contract).getClass().getMethod("execute");

	@SneakyThrows
	public static AcceptedContract fromContract(@NotNull Contract contract) {
		return new AcceptedContract(contract);
	}

}
