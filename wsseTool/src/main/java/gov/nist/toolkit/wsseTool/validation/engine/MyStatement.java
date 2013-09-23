package gov.nist.toolkit.wsseTool.validation.engine;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class MyStatement extends Statement {
    private final FrameworkMethod fTestMethod;
    private Object fTarget;

    public MyStatement(FrameworkMethod testMethod, Object target) {
        fTestMethod = testMethod;
        fTarget = target;
    }

    @Override
    public void evaluate() throws Throwable {
        fTestMethod.invokeExplosively(fTarget);
    }
}