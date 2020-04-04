/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author billy zhang
 * 
 */
public class ComponentFactory<T> {

	private Map<String, Class<? extends T>> classMap = new HashMap<>();
	private Map<String, T> componentMap = new HashMap<>();
	
	public void register(String name, Class<? extends T> componentClass) {
		classMap.put(name, componentClass);
	}
	
	public T getComponent(String name) {
		if (componentMap.containsKey(name)) {
			return componentMap.get(name);
		}
		
		if (classMap.containsKey(name)) {
			Class<? extends T> clazz = classMap.get(name);
			try {
				T component = clazz.newInstance();
				componentMap.put(name, component);
				
				return component;
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return getDefault();
	}
	
	protected T getDefault() {
		return null;
	}
}
