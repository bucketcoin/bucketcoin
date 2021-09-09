package net.bucketcoin.contract.proc;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class ContractProcessor extends AbstractProcessor {
	/**
	 * {@inheritDoc}
	 *
	 * @param annotations
	 * @param roundEnv
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		return false;
	}
}
