package gov.nist.hit.ds.simSupport.simrepo;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.util.Calendar;
import java.util.Date;

/**
 * Initialization and access functions for simulator repository.
 * @author bmajur
 *
 */
public class SimRepoFactory {
//	static Logger logger = Logger.getLogger(SimRepoFactory.class);

    public void installRepositoryLinkage(Simulator simulator) {
        ArtifactId repositoryId = null;
        try {
            repositoryId = simulator.getSimAsset().getRepository();
        } catch (RepositoryException e) {
            throw new ToolkitRuntimeException(e);
        }
        simulator.setRepositoryId(repositoryId);
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
