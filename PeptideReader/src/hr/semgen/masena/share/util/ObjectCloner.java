package hr.semgen.masena.share.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectCloner {
	private static final Logger log = LoggerFactory.getLogger(ObjectCloner.class);

	
	/**
	 * so that nobody can accidentally create an ObjectCloner object
	 */
	private ObjectCloner() {
	}

	/**
	 * returns a deep copy of an object
	 * 
	 * @param oldObj
	 * @return
	 * @throws Exception
	 */
	static public <T> Object deepCopy(T oldObj) throws Exception {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); // A
			oos = new ObjectOutputStream(bos); // B
			// serialize and pass the object
			oos.writeObject(oldObj); // C
			oos.flush(); // D
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray()); // E
			ois = new ObjectInputStream(bin); // F
			// return the new object
			return ois.readObject(); // G
		} catch (Exception e) {
			log.error("Exception in ObjectCloner ", e);
			throw (e);
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(ois);
		}
	}

}