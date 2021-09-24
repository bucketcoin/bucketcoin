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
