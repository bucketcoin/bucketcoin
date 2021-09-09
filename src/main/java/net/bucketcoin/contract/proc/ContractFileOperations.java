package net.bucketcoin.contract.proc;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.library.SortedClassLibraryBuilder;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;


public final class ContractFileOperations {

	private static final ArrayList<String> allowedImports = new ArrayList<>() {{

		add("net.bucketcoin.contract.permitted.*");

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

}
