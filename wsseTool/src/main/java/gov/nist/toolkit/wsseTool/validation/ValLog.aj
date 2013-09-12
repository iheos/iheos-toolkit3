package gov.nist.toolkit.wsseTool.validation;

import org.slf4j.Logger;

public aspect ValLog {
	
	public int errorCount = 0;

	pointcut move():
		call(void Logger.error*(..));
	
	before(): move() {
		System.out.println("error found!");
		errorCount++;
	}
	
	pointcut endVal():
		call(void WsseHeaderValidator.validate(..));
	
	
	after(): endVal() {
		System.out.println("errors" + errorCount);
	}
	
}
