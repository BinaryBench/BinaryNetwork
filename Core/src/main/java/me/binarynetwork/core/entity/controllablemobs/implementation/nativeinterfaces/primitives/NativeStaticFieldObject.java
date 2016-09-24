package me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.primitives;

import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.system.NativeFieldPrivate;

public class NativeStaticFieldObject<C extends Object, R extends Object> extends NativeFieldPrivate<C> {

	public NativeStaticFieldObject(final Class<C> nativeClass, final String fieldName) {
		super(nativeClass, fieldName);
	}
	
	@SuppressWarnings("unchecked")
	public R get() {
		try {
			return (R) this.getField(null).get(null);
		} catch(Throwable e) {
			this.handleException(e);
			return null;
		}
	}
	
	public void set(final R value) {
		try {
			this.getField(null).set(null, value);
		} catch(Throwable e) {
			this.handleException(e);
		}
	}
	
}
