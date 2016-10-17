/* This file is part of the db4o object database http://www.db4o.com

Copyright (C) 2004 - 2010  Versant Corporation http://www.versant.com

db4o is free software; you can redistribute it and/or modify it under
the terms of version 3 of the GNU General Public License as published
by the Free Software Foundation.

db4o is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License along
with this program.  If not, see http://www.gnu.org/licenses/. */
package com.db4o.internal;

import java.io.*;
import java.lang.reflect.*;

import com.db4o.foundation.*;
import com.db4o.reflect.*;
import com.db4o.reflect.core.*;

/**
 * @sharpen.ignore
 */
@decaf.Remove
class DalvikVM extends JDK_5 {
	
	@decaf.Remove
	public final static class Factory implements JDKFactory {
		public JDK tryToCreate() {
			if (!"Dalvik".equals(System.getProperty("java.vm.name"))) {
				return null;
			}
			return new DalvikVM();
		}
	};
	
	@Override
	public ReflectConstructor serializableConstructor(Reflector reflector, final Class clazz) {
		
		return new ReflectConstructor() {
			
			public Object newInstance(Object[] parameters) {
				try {
					return DalvikVM.this.newInstanceSkippingConstructor(clazz);
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
			
			public ReflectClass[] getParameterTypes() {
				return new ReflectClass[0];
			}
		};
	}

	private Object newInstanceSkippingConstructor(final Class clazz) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			
		Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
		newInstance.setAccessible(true);
		return newInstance.invoke(null, clazz, Object.class);
	}
	
	public static class SkipConstructorCheck {
		public SkipConstructorCheck() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}
	}
	
	private TernaryBool supportSkipConstructorCall = TernaryBool.UNSPECIFIED;
	
	@Override
	boolean supportSkipConstructorCall() {
		if (supportSkipConstructorCall.isUnspecified()) {
			try {
				newInstanceSkippingConstructor(SkipConstructorCheck.class);
				supportSkipConstructorCall = TernaryBool.YES;
			} catch (Exception e) {
				supportSkipConstructorCall = TernaryBool.NO;
			}
		}
		return supportSkipConstructorCall.definiteYes();
	}

}
