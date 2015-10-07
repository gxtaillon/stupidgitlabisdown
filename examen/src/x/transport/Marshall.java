package x.transport;

import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Marshall {
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
