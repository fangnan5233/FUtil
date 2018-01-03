package com.fang.lang;

import java.lang.reflect.Constructor;

public class Except {

	public static RuntimeException newException(String message, Throwable cause) {
		return new RuntimeException(message, cause);
	}

	public static RuntimeException newException(String message) {
		return new RuntimeException(message);
	}

	public static RuntimeException newException(Throwable cause) {
		return new RuntimeException(cause);
	}

	public static <T extends Throwable> T newException(String message, Class<T> cls) {
		try {
			Constructor<T> c = cls.getConstructor(String.class);
			return c.newInstance(message);
		} catch (Exception e) {
			throw new RuntimeException(message);
		}
	}

	public static <T extends Throwable> T newException(Throwable cause, Class<T> cls) {
		try {
			Constructor<T> c = cls.getConstructor(Throwable.class);
			return c.newInstance(cause);
		} catch (Exception e) {
			throw new RuntimeException(cause);
		}
	}

	public static <T extends Throwable> T newException(String message, Throwable cause, Class<T> cls) {
		try {
			Constructor<T> c = cls.getConstructor(String.class, Throwable.class);
			return c.newInstance(message, cause);
		} catch (Exception e) {
			throw new RuntimeException(message, cause);
		}
	}
}