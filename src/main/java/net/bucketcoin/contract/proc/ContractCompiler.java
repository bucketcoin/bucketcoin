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

package net.bucketcoin.contract.proc;

import lombok.SneakyThrows;
import net.bucketcoin.exception.ContractException;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.jetbrains.annotations.Contract;

import javax.tools.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import static net.bucketcoin.contract.proc.ContractFileOperations.verify;

public final class ContractCompiler {

	private ContractCompiler() {
		//no instance
	}

	/**
	 * Compiles a Java contract file.
	 * If the compilation fails then a file with name 'log.txt' is generated.
	 * @param javaContractFile The contract file.
	 * @return whether the file was compiled successfully
	 */
	@SneakyThrows
	@Contract(pure = true)
	@SuppressWarnings("NullArgumentToVariableArgMethod")
	public static boolean compile(File javaContractFile, boolean resolve) {

		if(!verify(javaContractFile)) {
			return false;
		}

		final var compiler = new EclipseCompiler();
		final var diagnostics = new DiagnosticCollector<JavaFileObject>();
		final var manager = compiler.getStandardFileManager(diagnostics, null, null );
		final var sources = manager.getJavaFileObjectsFromFiles(List.of(javaContractFile));
		final var task = compiler.getTask(null, manager, diagnostics, null, null, sources);

		for(JavaFileObject javaFileObject : sources) {
			var logfile = new File(javaContractFile.getParentFile().getAbsolutePath() + File.separator + "log.txt");
			var err = new FileOutputStream(logfile);
			var x = compiler.run(null, null, err, ((File) javaFileObject).getAbsolutePath());

			try {
				URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { ((File) javaFileObject).getParentFile().toURI().toURL() });
				Class<?> cls = Class.forName(FilenameUtils.getBaseName(javaFileObject.getName()), true, classLoader);
				Object instance = cls.getDeclaredConstructor().newInstance();
				Method method = cls.getDeclaredMethod("add", null);
				System.out.println(method.invoke(instance, null));
				return true;
			} catch (Exception e) {
				try {
					throw new ContractException(e);
				} catch(ContractException contractException) {
					contractException.printStackTrace(new PrintWriter(new FileWriter(logfile)));
				}
			}

		}

		return false;

	}

}
