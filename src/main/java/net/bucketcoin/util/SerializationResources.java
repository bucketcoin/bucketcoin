package net.bucketcoin.util;

import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class SerializationResources {

	public static Charset getStandardCharset() {
		return StandardCharsets.ISO_8859_1;
	}

	public static byte @NotNull [] objectToBytes(Serializable o) throws IOException {

		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ObjectOutputStream out;
			out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.flush();
			return bos.toByteArray();
		}

	}

	public static boolean implementsInterface (TypeElement myTypeElement, TypeElement desiredInterface) {
		for (TypeMirror t : myTypeElement.getInterfaces())
			if (t.getClass().isAssignableFrom(myTypeElement.getClass()))
				return true;
		return false;
	}

}
