package net.bucketcoin.contract.proc;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.library.SortedClassLibraryBuilder;
import lombok.SneakyThrows;

import java.io.*;
import java.util.ArrayList;


public final class ContractFileOperations {

	private static final ArrayList<String> allowedImports = new ArrayList<>() {{

		add("net.bucketcoin.contract.permitted.*");
		add("net.bucketcoin.contract.interfaces.Contract");

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
	 * Writes a template contract source file in the specified parameter <code>fileToWrite</code>.
	 * @param fileToWrite The file to write the contract source file to. Must have a '.java' extension. Will be created if it does not exist
	 * @throws IOException if an I/O error occurs.
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void writeContractFile(File fileToWrite) throws IOException {
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
		writer.println("""
				// package your.company.project
				
				// These two imports are mandatory imports and the
				// only allowed imports. Any inclusion of other /
				// imports will result in a failure to compile |
				// and rejection across the blockchain network. \
				import net.bucketcoin.contract.interfaces.Contract;
				import net.bucketcoin.contract.permitted.*;
				    
				public class ContractExample implements Contract {
					
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
				}""");
		writer.close();
	}

}
