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

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.library.SortedClassLibraryBuilder;
import lombok.SneakyThrows;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.text.StringEscapeUtils;

import javax.tools.JavaFileObject;
import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;


public final class ContractFileOperations {

	private static final ArrayList<String> allowedImports = new ArrayList<>() {{

		add("net.bucketcoin.contract.permitted.*");
		add("net.bucketcoin.contract.Contract");

	}};

	/**
	 * Verifies the validity of the source file for broadcasting to the network and
	 * for compilation on this running node.
	 * @param source The .java source file to validate
	 * @return Whether the source file is valid for broadcasting.
	 */
	@SneakyThrows
	public static boolean verify(File source) {
		final JavaProjectBuilder builder = new JavaProjectBuilder(new SortedClassLibraryBuilder() {{
			appendClassLoader(ClassLoader.getSystemClassLoader());
		}});
		var src = builder.addSource(source);
		var imports = src.getImports();
		for(String s1 : imports) {
			if(!allowedImports.contains(s1)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Converts a contract file to a {@link String} representation.
	 * @param contractFile The contract file to make a string representation of.
	 * @return the {@link} String representation of the contract file.
	 * @throws IOException if an IO error occurred.
	 * @throws FileNotFoundException if the file passed to this method is non-existent.
	 * @throws InvalidParameterException if the file passed to this method is either opened for writing and not reading, or it does not support character access.
	 * @see javax.tools.FileObject#openReader(boolean)
	 */
	@Contract("null -> fail")
	public static String getContractAsString(File contractFile) throws IOException {
		if(!contractFile.exists()) throw new FileNotFoundException("Contract file passed does not exist.");
		JavaFileObject file = (JavaFileObject) contractFile;
		try {
			Reader reader = file.openReader(false);
			return read(reader);
		} catch(IOException varA1) {
			try {
				var reader = new BufferedReader(
						new FileReader(contractFile)
				);
				return read(reader);
			} catch(IOException varA2) {
				throw new IOException(varA2.getMessage());
			}
		} catch(IllegalStateException varB1) {
			throw new InvalidParameterException("The file passed is opened for writing and not reading.");
		} catch(UnsupportedOperationException varC1) {
			throw new InvalidParameterException("The file passed does not support character access.");
		}
	}

	private static String read(@NotNull Reader reader) throws IOException {
		var s = new StringBuilder();
		int num;
		while((num = reader.read()) != -1)
		{
			s.append((char) num);
		}
		return StringEscapeUtils.unescapeJava(s.toString());
	}

	/**
	 * Writes a template contract source file in the specified parameter <code>fileToWrite</code>.
	 * @param fileToWrite The file to write the contract source file to. Must have a '.java' extension. Will be created if it does not exist
	 * @throws IOException if an I/O error occurs.
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void writeContractFile(@NotNull File fileToWrite) throws IOException {
		// do necessary checks before writing
		if(!fileToWrite.exists()) fileToWrite.createNewFile();
		if(!fileToWrite.canWrite()) {
			var x = fileToWrite.setWritable(true);
			if(!x) throw new IOException("Cannot write to file " + fileToWrite.getName());
		}
		if(!fileToWrite.canRead()) {
			var x = fileToWrite.setReadable(true);
			if(!x) throw new IOException("Cannot set file " + fileToWrite.getName() + " to a readable state");
		}
		if(!fileToWrite.getName().endsWith(".java")) throw new IllegalArgumentException("File extension must be \".java\"!");
		//start writing to the file
		var writer = new PrintWriter(fileToWrite);
		@Language("JAVA")
		var s = """
				// package your.company.project
				
				/* These two imports are mandatory imports and the
				   only allowed imports. Any inclusion of other
				   imports will result in a failure to compile
				   and rejection across the blockchain network. */
				   
				import net.bucketcoin.contract.Contract;
				import net.bucketcoin.contract.permitted.*;
				    
				public final class ContractExample implements Contract {
					
					/*
				    * The (one-time) gas fee for an approval Message.
				    */
					@Override
					public double getGasFee() {
						return 1;
					}
				    
				    /*
				    * Your execution logic in this method
				    */
					@Override
					public boolean execute() {
						return true;
					}
				    
				    /*
				    * The Bucketcoin Smart Contract Interface (BSCI) version.
				    */
					@Override
					public int getVersion() {
						return 1;
					}
				}""";
		writer.println(s);
		writer.close();
	}

}
