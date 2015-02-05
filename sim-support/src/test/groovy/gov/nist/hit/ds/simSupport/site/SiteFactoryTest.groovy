package gov.nist.hit.ds.simSupport.site
import gov.nist.hit.ds.actorTransaction.*
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.config.RetrieveTransactionSimConfigElement
import gov.nist.hit.ds.simSupport.config.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.simulator.SimConfigFactory
import gov.nist.hit.ds.siteManagement.client.Site
import gov.nist.hit.ds.siteManagement.client.TransactionBean
import gov.nist.hit.ds.siteManagement.loader.SeparateSiteLoader
import gov.nist.hit.ds.utilities.xml.OMFormatter
import org.apache.axiom.om.OMElement
import spock.lang.Specification
/**
 * Created by bmajur on 6/8/14.
 */
class SiteFactoryTest extends Specification {
    static String config = '''
<ActorsTransactions>
    <transaction name="Stored Query" code="sq.b" asyncCode="sq.as">
        <request action="urn:ihe:iti:2007:RegistryStoredQuery"/>
        <response action="urn:ihe:iti:2007:RegistryStoredQueryResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Register" code="r.b" asyncCode="r.as">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Provide and Register" code="pr.b" asyncCode="pr.as">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Retrieve" code="ret.b" asyncCode="ret.as" isRetrieve="true">
        <request action="urn:ihe:iti:2007:RetrieveDocumentSet"/>
        <response action="urn:ihe:iti:2007:RetrieveDocumentSetResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Update" code="update" asyncCode="update.as">
        <request action="urn:ihe:iti:2010:UpdateDocumentSet"/>
        <response action="urn:ihe:iti:2010:UpdateDocumentSetResponse"/>
        <implClass value="unused"/>
    </transaction>
    <actor name="Document Registry" id="reg">
        <implClass value="gov.nist.hit.ds.registrySim.factories.DocumentRegistryActorFactory"/>
        <transaction id="r.b"/>
        <transaction id="sq.b"/>
        <transaction id="update"/>
    </actor>
    <actor name="Document Repository" id="rep">
        <implClass value="gov.nist.hit.ds.registrySim.factory.DocumentRepositoryActorFactory"/>
        <transaction id="pr.b"/>
        <transaction id="ret.b"/>
    </actor>
</ActorsTransactions>
'''
    ActorTransactionTypeFactory atFactory

    void setup() {
        atFactory = new ActorTransactionTypeFactory()
        atFactory.clear()
        atFactory.loadFromString(config)
    }


    def 'ActorSimConfig should have 4 transactions'() {
        given:
        def actorType = atFactory.getActorType('rep')
        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', '8080','base', new SimId('1234'), actorType)

        when:
        def endpoints = actorSimConfig.getTransactions().findAll { it instanceof TransactionSimConfigElement }
        println endpoints

        then:
        endpoints.size() == 4  // two pr.b and two ret.b
    }

    def 'ActorSimConfig should have 1 Repository Declarations'() {
        given:
        def actorType = atFactory.getActorType('rep')
        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', '8080','base', new SimId('1234'), actorType)

        when:
        def repositories = actorSimConfig.getTransactions().findAll {
            it instanceof RetrieveTransactionSimConfigElement
        }

        then:
        repositories.size() == 2   // TLS and non-TLS
    }

    def 'Constructed Site should include pr.b transaction'() {
        given:
        def siteFactory = new SimSiteFactory()
        def aTfactory = new ActorTransactionTypeFactory()
        def actorType = atFactory.getActorType('rep')
        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', '8080','base', new SimId('1234'), actorType)

        when:
        Site site = siteFactory.buildSite(actorSimConfig, 'mysite')
        TransactionType transType = aTfactory.getTransactionType('pr.b')

        then:
        site.getEndpoint(transType, false, false) ==
                actorSimConfig.getEndpoint(transType, TlsType.NOTLS, AsyncType.SYNC).value
    }

    def 'Constructed Site should include repositoryUniqueId'() {
        given: ''
        def siteFactory = new SimSiteFactory()
        def actorType = atFactory.getActorType('rep')
//        ActorType actorType = aTfactory.getActorType(actorTypeName)
        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', '8080','base', new SimId('1234'), actorType)

        when: ''
        Site site = siteFactory.buildSite(actorSimConfig, 'mysite')
        println site.toStringAll()
        TransactionBean transBean = site.getRepositoryBean(false)

        then:
        transBean

        when:
        def repositoryUniqueId = site.getRepositoryUniqueId()
        OMElement ele = new SeparateSiteLoader().siteToXML(site)
        println new OMFormatter(ele).toString()

        then:
        repositoryUniqueId
    }

}
