package ift604.common.transport;

import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Marshall {
	/**
	 * Serializes a serializable instance into an array of bytes
	 * @param a The instance to serialize
	 * @return Just(byte[]) if the serialization suceeded or Nothing() otherwise
	 */
	public static <Ta extends Serializable> Maybe<byte[]> toBytes(Ta a) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(a);
			oos.flush();
			byte[] r = baos.toByteArray();
			return Maybe.<byte[]>Just(r, "object serialized");
		} catch (IOException e) {
			return Maybe.<byte[]>Nothing(ExceptionExtension.stringnify(e));
		}
	}

	/**
	 * Deserializes an array of bytes into its corresponding instance
	 * @param bytes The array of bytes containing the serialized instance
	 * @param ac The expected class of the serialized instance
	 * @param <Ta> The expected type of the serialized instance
	 * @return Just(Ta) if the deserialization suceeded or Nothing() otherwise
	 */
	@SuppressWarnings("unchecked")
	public static <Ta extends Serializable> Maybe<Ta> fromBytes(byte[] bytes, Class<Ta> ac) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			if (o.getClass().equals(ac)) {
				return Maybe.<Ta>Just((Ta) o, "object deserialized");
			} else {
				return Maybe.<Ta>Nothing("expected an object of class " + ac.getName());
			}
		} catch (IOException e) {
			return Maybe.<Ta>Nothing(ExceptionExtension.stringnify(e));
		} catch (ClassNotFoundException e) {
			return Maybe.<Ta>Nothing(ExceptionExtension.stringnify(e));
		}
	}
}
