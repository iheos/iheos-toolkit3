package gov.nist.toolkit.wsseTool.engine;

import gov.nist.toolkit.wsseTool.engine.annotations.Data;
import gov.nist.toolkit.wsseTool.engine.annotations.Validation;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class MyRunner extends BlockJUnit4ClassRunner {

	protected Field dataField;
	protected TestData data;

	/**
	 * Used only by the eclipse runner to test validation in eclipse. It
	 * supposed that the testing class as a public static TestData data field
	 * containing the data to test against.
	 * 
	 * @param klass
	 *            the test class.
	 * @throws Throwable
	 *             if the runner cannot find a public static TestData data field
	 *             in the test class.
	 */
	public MyRunner(Class<?> klass) throws Throwable {
		super(klass);
		dataField = getTestClassDataField();
	}

	public MyRunner(Class<?> klass, TestData testdata) throws Throwable {
		super(klass);
		dataField = getTestClassDataField();
		this.data = testdata;
	}

	private Field getTestClassDataField() throws Throwable {

		List<FrameworkField> dataFields = super.getTestClass()
				.getAnnotatedFields(Data.class);
		if (dataFields.size() != 1) {
			throw new InitializationError(
					"found several @Data annotated fields. Need only one.");
		}
		if(!dataFields.get(0).isPublic()){
			throw new InitializationError(
					"@Data annotated field must public.");
		}

		return dataFields.get(0).getField();
	}

	/*
	 * When invoking a test.
	 * If data has been passed to the runner, we set up the test @Data field accordingly.
	 */
	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object test) {

		if (data != null) {
			try {
				dataField.set(test, data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new MyStatement(method, test);
	}

	/*
	 * we use the Validation assertion to find methods to run.
	 */
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		return getTestClass().getAnnotatedMethods(Validation.class);
	}

}
