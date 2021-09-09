package some.random;

import net.bucketcoin.contract.interfaces.Contract;

public class ContractExample implements Contract {
	@Override
	public double getGasFee() {
		return 1;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public int getVersion() {
		return 1;
	}
}
