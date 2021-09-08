package net.bucketcoin.algorithm;

import lombok.Getter;

import java.time.Duration;

public class DifficultyAlgorithm {

	@Getter
	private static long difficulty = 1L;

	private DifficultyAlgorithm() {
		//no instance
	}

	/**
	 * Calculates the difficulty (the amount of leading zeroes in a hash required by the miner)
	 * based on the number of {@linkplain net.bucketcoin.block.Block}s mined and
	 * the {@linkplain Duration} taken to mine the blocks, in the Bucketcoin formula.
	 * @param blocksMined The number of blocks mined (not only by this node) and broadcasted to the {@link net.bucketcoin.p2p.Node}.
	 * @param duration The duration taken to mine the blocks. This should be recorded by the {@link net.bucketcoin.Bucketcoin} class.
	 * @return The calculated difficulty.
	 */
	public static long calculateDifficulty(int blocksMined, Duration duration) {

		var minutes = duration.toMinutes();
		var current = minutes / (long) blocksMined;
		var target = getTarget();

		var x = target / current;
		var deviation = getDeviation(x);

		if(deviation > ((int) deviation + 0.5)) {
			x++;
		}

		return x - 4;

	}

	public static String getDifficultyString(long difficulty) {
		var s = new StringBuilder();
		for(int i = 0; i < difficulty; i++) {
			s.append('0');
		}
		return s.toString();
	}

	private static long getTarget() {
		return 11520 / 1500 - 6;
	}

	/**
	 * Calculates the Bucketcoin target deviation from the target.
	 * @param target The target to calculate.
	 * @return the deviation.
	 */
	private static double getDeviation(long target) {
		return -(target - 1L);
	}

}
