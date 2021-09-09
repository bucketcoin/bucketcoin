package net.bucketcoin.contract.proc;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.jetbrains.annotations.Contract;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.*;
import java.io.File;
import java.util.List;
import java.util.Set;

import static net.bucketcoin.contract.proc.ContractFileOperations.verify;

public final class ContractCompiler {

	private ContractCompiler() {
		//no instance
	}

	/**
	 *
	 * @param javaContractFile The contract file.
	 * @return whether the file was compiled successfully
	 */
	@Contract(pure = true)
	public static boolean compileJava(File javaContractFile) {

		if(!verify(javaContractFile)) {
			return false;
		}

		final var compiler = new EclipseCompiler();
		final var diagnostics = new DiagnosticCollector<JavaFileObject>();
		final var manager = compiler.getStandardFileManager(diagnostics, null, null );
		final var sources = manager.getJavaFileObjectsFromFiles(List.of(javaContractFile));
		final var task = compiler.getTask(null, manager, diagnostics, null, null, sources);

		return task.call();

	}

}
