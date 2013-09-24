package gov.nist.hit.ds.wsseTool.validation.engine;

import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;

public class ValRunnerWithOrder extends ValRunner {

	public ValRunnerWithOrder(Class<?> klass, TestData testdata)
			throws Throwable {
		super(klass, testdata);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		List<FrameworkMethod> list = new ArrayList(super.computeTestMethods());
		
		Collections.sort(list, new Comparator<FrameworkMethod>(){

			public int compare(FrameworkMethod f1, FrameworkMethod f2) {
				Order o1 = f1.getAnnotation(Order.class);
				Order o2 = f2.getAnnotation(Order.class);

				if (o1 == null || o2 == null)
					return -1;

				return o1.order() - o2.order();
			}
			});
		
		
		return list;
	}

}
