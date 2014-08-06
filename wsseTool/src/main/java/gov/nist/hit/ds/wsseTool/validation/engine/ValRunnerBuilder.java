package gov.nist.hit.ds.wsseTool.validation.engine;

import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

public class ValRunnerBuilder extends RunnerBuilder{

	TestData data;
	
	public ValRunnerBuilder(TestData data){
		this.data = data;
	}
	
	
	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {
		return new ValRunnerWithOrder(testClass, data);
	}

}
