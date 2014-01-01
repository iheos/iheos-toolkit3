package gov.nist.hit.ds.simSupport.simrepo;

import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Initialization and access functions for simulator repository.
 * @author bmajur
 *
 */
public class SimRepoFactory {
	static Logger logger = Logger.getLogger(SimRepoFactory.class);
	Repository repos = null;
	
	/**
	 * Init the sim db environment.
	 */
	public void init()  {
		try {
			Installation.reset();
			Installation.installation().initialize();
			Configuration.configuration();   // Repository
			RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
			repos = fact.createNamedRepository(
					"SimState", 
					"SimState", 
					new SimpleType("simStateRepos"),               // repository type
					"SimState"    // repository name
					);
		} catch (Exception e) {
			throw new RuntimeException("SimRepoFactory initialization failed: ", e);
		}
	}
	
	public Asset createSimAsset()  {
		if (repos == null)
			init();
		try {
			return repos.createAsset(
					SimRepoFactory.nowAsFilenameBase(), 
					"Simulator", 
					new SimpleType("simAsset"));
		} catch (RepositoryException e) {
			throw new RuntimeException("Create simAsset failed", e);
		}
	}
	
	public void add(ActorSimConfig actorSimConfig) {
		
	}
	
	public static String nowAsFilenameBase() {
		Date date = new Date();

		Calendar c  = Calendar.getInstance();
		c.setTime(date);

		String year = Integer.toString(c.get(Calendar.YEAR));
		String month = Integer.toString(c.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1 )
			day = "0" + day;
		String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1)
			hour = "0" + hour;
		String minute = Integer.toString(c.get(Calendar.MINUTE));
		if (minute.length() == 1)
			minute = "0" + minute;
		String second = Integer.toString(c.get(Calendar.SECOND));
		if (second.length() == 1)
			second = "0" + second;
		String mili = Integer.toString(c.get(Calendar.MILLISECOND));
		if (mili.length() == 2)
			mili = "0" + mili;
		else if (mili.length() == 1)
			mili = "00" + mili;

		String dot = "_";

		String val =
				year +
				dot +
				month +
				dot +
				day + 
				dot +
				hour +
				dot +
				minute +
				dot +
				second +
				dot +
				mili
				;
		return val;
	}

}
