import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimConfigFactory
import gov.nist.hit.ds.simSupport.site.SimSiteFactory
import gov.nist.hit.ds.siteManagement.client.Site
import gov.nist.hit.ds.siteManagement.loader.SeparateSiteLoader
import gov.nist.hit.ds.utilities.xml.OMFormatter
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 7/2/14.
 */

def aTfactory = new ActorTransactionTypeFactory()
aTfactory.clear()
aTfactory.loadFromResource('at.xml')

def actorTypeName = 'reg'
def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', '8081', 'simwar', new SimId('1234'), actorTypeName)
println actorSimConfig
OMElement simEle = actorSimConfig.toXML()
println new OMFormatter(simEle).toString()

Site site = new SimSiteFactory().buildSite(actorSimConfig, 'mysite')
OMElement siteEle = new SeparateSiteLoader().siteToXML(site)
println new OMFormatter(siteEle).toString()
