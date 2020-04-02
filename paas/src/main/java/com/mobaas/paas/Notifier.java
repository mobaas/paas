package com.mobaas.paas;

import java.util.Map;

public interface Notifier {
	
	void notify(String title, String text, Map<String, Object> config);
	
}
