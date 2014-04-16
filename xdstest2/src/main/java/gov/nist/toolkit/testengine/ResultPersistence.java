package gov.nist.toolkit.testengine;

import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.toolkit.installation.Installation;
import gov.nist.toolkit.results.client.Result;

import java.io.*;

public class ResultPersistence {

	public void write(Result result, String testSession) throws IOException, XdsException {

		if (isEmpty(result.testName))
			throw new XdsException("No test name specified in Result - cannot persist", null);

		String outFile = getFilePath(result.testName, testSession, true);
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		fos = new FileOutputStream(outFile.toString());
		out = new ObjectOutputStream(fos);
		out.writeObject(result);
		out.close();

	}

	public Result read(String testName, String testSession) throws XdsException  {
		try {
			FileInputStream fis = new FileInputStream(getFilePath(testName, testSession, false));
			ObjectInputStream in = new ObjectInputStream(fis);
			Result result = (Result) in.readObject();
			in.close();
			return result;
		} 
		catch (IOException e) {
			throw new XdsException(e.getMessage(), null, e);
		} catch (ClassNotFoundException e) {
			throw new XdsException(e.getMessage(), null, e);
		}
	}

	String getFilePath(String testName,String testSession, boolean write) throws IOException {
		File dir = new File(
				Installation.installation().propertyServiceManager().getTestLogCache().toString() + File.separator + 
				testSession + File.separator + 
				"Results");
		if (write)
			dir.mkdirs();
		
		return dir.toString() + File.separator + testName + ".ser";
	}


	boolean isEmpty(String x) { return x == null || x.equals(""); }

}
