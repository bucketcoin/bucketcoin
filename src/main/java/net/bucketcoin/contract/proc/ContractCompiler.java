package net.bucketcoin.contract.proc;

import lombok.SneakyThrows;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.jetbrains.annotations.Contract;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import static net.bucketcoin.contract.proc.ContractFileOperations.verify;

public final class ContractCompiler {

	private ContractCompiler() {
		//no instance
	}

	/**
	 * Compiles a Java contract file.
	 * @param javaContractFile The contract file.
	 * @param printStackTrace Whether the stack trace should be printed if compilation fails.
	 * @return whether the file was compiled successfully
	 */
	@SneakyThrows
	@Contract(pure = true)
	public static boolean compile(File javaContractFile, boolean printStackTrace) {

		if(!verify(javaContractFile)) {
			return false;
		}

		final var compiler = new EclipseCompiler();
		final var diagnostics = new DiagnosticCollector<JavaFileObject>();
		final var manager = compiler.getStandardFileManager(diagnostics, null, null );
		final var sources = manager.getJavaFileObjectsFromFiles(List.of(javaContractFile));
		final var task = compiler.getTask(null, manager, diagnostics, null, null, sources);

		for(JavaFileObject javaFileObject : sources) {
			var logfile = new File(javaContractFile.getParentFile().getAbsolutePath() + File.separator + "logfile.txt");
			var err = new FileOutputStream(logfile);
			var x = compiler.run(null, null, err, ((File) javaFileObject).getAbsolutePath());
			return x == 0;
		}

		return false;

	}

}
