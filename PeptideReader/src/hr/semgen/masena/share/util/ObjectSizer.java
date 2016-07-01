package hr.semgen.masena.share.util;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.IdentityHashMap;
 
public class ObjectSizer {
	private static Instrumentation instrumentation;
	
	public static void premain(String args, Instrumentation inst) {
		instrumentation = inst;
	}
	
	/**
	 * Return JVM-implementation dependent approximation of an Object
	 */
	public static long getObjectSize(Object o) {
		return instrumentation.getObjectSize(o);
	}
	
	/**
	 * Return JVM-implementation dependent approximation of an Object graph (does not take into account static fields)
	 */
	public static long getObjectGraphSize(final Object o) {
		if (o == null) throw new NullPointerException();
		
		return AccessController.doPrivileged(new PrivilegedAction<Long>() {
			public Long run() {
				return getObjectGraphSize(o, new IdentityHashMap<Object, Object>());			
			}
		});
	}
	
	private static long getObjectGraphSize(Object o, IdentityHashMap<Object, Object> visited) {
		if (o == null) return 0;
		if (visited.containsKey(o)) return 0;
		
		visited.put(o, null);
		
		long size = instrumentation.getObjectSize(o);
		
		Class<?> clazz = o.getClass();
		
		if (clazz.isArray()) {
			if (!clazz.getComponentType().isPrimitive()) {
				for (int i=0; i<Array.getLength(o); i++) {
					size += getObjectGraphSize(Array.get(o, i), visited);
				}
			}
		} else {
			while (!Object.class.equals(clazz)) {
				for (Field f : clazz.getDeclaredFields()) {
					if ((f.getModifiers() & Modifier.STATIC) != 0) continue;
					if (f.getType().isPrimitive()) continue;
					
					boolean oldAccessible = f.isAccessible();
					f.setAccessible(true);
					try {
						Object value = f.get(o);
						size += getObjectGraphSize(value, visited);
					} catch (Exception e) {
						throw new RuntimeException("Exception trying to access field "+f, e);
					} finally {
						f.setAccessible(oldAccessible);
					}
				}
				
				clazz = clazz.getSuperclass();
			}
		}
		
		return size;
	}
}