/*
 *    Copyright 2021 The Bucketcoin Authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.bucketcoin.pow;

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
	 * @param duration The duration taken to mine the blocks. This should be recorded by the {@link net.bucketcoin.crypto.Bucketcoin} class.
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

		difficulty = x - 4;

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
		return 11520 / 1500;
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
