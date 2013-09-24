package gov.nist.hit.ds.wsseTool.validation.engine;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class ValStatement extends Statement {
    private final FrameworkMethod fTestMethod;
    private Object fTarget;

    public ValStatement(FrameworkMethod testMethod, Object target) {
        fTestMethod = testMethod;
        fTarget = target;
    }

    @Override
    public void evaluate() throws Throwable {
        fTestMethod.invokeExplosively(fTarget);
    }
}