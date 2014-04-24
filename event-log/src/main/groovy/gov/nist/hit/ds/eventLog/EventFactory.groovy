package gov.nist.hit.ds.eventLog

import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.simple.SimpleType
import org.apache.log4j.Logger

public class EventFactory {
	static Logger logger = Logger.getLogger(EventFactory)

    /**
     * Repository == null makes this an in-memory Event, it is never saved
     * to the repository.
     * @param repository
     * @return
     */
    Event buildEvent(Repository repository) {
        def eventAsset = null
        if (repository) {
            def assetName = nowAsFilenameBase()
            logger.debug('New event <${assetName}>')
            def type = new SimpleType('event')
            eventAsset = repository.createAsset(assetName, 'Event', type)
        }
        new Event(eventAsset)
    }

//	public Event buildEvent(SimId simId, String actorShortName, String transactionShortName) throws RepositoryException {
//		Event event = null;
//			RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
//			Repository repos = fact.createNamedRepository(
//					"SimLogs",
//					"SimLogs",
//					new SimpleType("eventRepos"),               // repository type
//					actorShortName + "-" + simId    // repository name
//					);
//			Asset eventAsset = repos.createAsset(
//					nowAsFilenameBase(),
//					transactionShortName + " Event",
//					new SimpleType("simEvent"));
//			event = new Event(eventAsset);
//			logger.debug("New Event asset <" + eventAsset.getId() + ">");
//		return event;
//
//	}

    String nowAsFilenameBase() {
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
