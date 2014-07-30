package gov.nist.hit.ds.simSupport.site

import gov.nist.hit.ds.actorTransaction.*
import gov.nist.hit.ds.simSupport.client.ParamType
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.configElementTypes.RepositoryUniqueIdSimConfigElement
import gov.nist.hit.ds.simSupport.simulator.SimConfigFactory
import gov.nist.hit.ds.siteManagement.client.Site
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
    <transaction displayName="Stored Query" id="sq.b" code="sq." asyncCode="sq.as">
        <request action="urn:ihe:iti:2007:RegistryStoredQuery"/>
        <response action="urn:ihe:iti:2007:RegistryStoredQueryResponse"/>
    </transaction>
    <transaction displayName="Register" id="r.b" code="r.b" asyncCode="r.as">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
    </transaction>
    <transaction displayName="Provide and Register" id="pr.b" code="pr.b" asyncCode="pr.as">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
    </transaction>
    <transaction displayName="Retrieve" id="ret.b" code="ret.b" asyncCode="ret.as">
        <request action="urn:ihe:iti:2007:RetrieveDocumentSet"/>
        <response action="urn:ihe:iti:2007:RetrieveDocumentSetResponse"/>
        <property name="RepositoryUniqueId" value="1.2.3"/>
    </transaction>
    <transaction displayName="Update" id="update" code="update" asyncCode="update.as">
        <request action="urn:ihe:iti:2010:UpdateDocumentSet"/>
        <response action="urn:ihe:iti:2010:UpdateDocumentSetResponse"/>
    </transaction>
    <actor displayName="Document Registry" id="reg">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factories.DocumentRegistryActorFactory"/>
        <transaction id="r.b"/>
        <transaction id="sq.b"/>
        <transaction id="update"/>
    </actor>
    <actor displayName="Document Repository" id="rep">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factory.DocumentRepositoryActorFactory"/>
        <transaction id="pr.b"/>
        <transaction id="ret.b"/>
    </actor>
</ActorsTransactions>
'''
    void setup() {
        def aTfactory = new ActorTransactionTypeFactory()
        aTfactory.clear()
        aTfactory.loadFromString(config)
    }

    def 'ActorSimConfig should have 4 transactions'() {
        given:
        def actorTypeName = 'rep'
        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', 'base', new SimId('1234'), actorTypeName)

        when:
        def endpoints = actorSimConfig.getAll().findAll { it.type == ParamType.ENDPOINT }

        then:
        endpoints.size() == 4
    }

    def 'ActorSimConfig should have 2 Repository Declarations'() {
        given:
        def actorTypeName = 'rep'
        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', 'base', new SimId('1234'), actorTypeName)

        when:
        def repositories = actorSimConfig.getAll().findAll {
            it.type == ParamType.ENDPOINT && (it instanceof RepositoryUniqueIdSimConfigElement)
        }

        then:
        repositories.size() == 2
    }

    def 'Constructed Site should include pr.b transaction'() {
        given:
        def siteFactory = new SimSiteFactory()
        def aTfactory = new ActorTransactionTypeFactory()
        def actorTypeName = 'rep'
        ActorType actorType = aTfactory.getActorType(actorTypeName)
        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', 'base', new SimId('1234'), actorTypeName)

        when:
        Site site = siteFactory.buildSite(actorSimConfig, 'mysite')
        TransactionType transType = aTfactory.getTransactionType('pr.b')

        then:
        site.getEndpoint(transType, false, false) ==
                actorSimConfig.getEndpoint(transType, TlsType.NOTLS, AsyncType.SYNC)
    }

    def 'Constructed Site should include repositoryUniqueId'() {
        given: ''
        def siteFactory = new SimSiteFactory()
        def aTfactory = new ActorTransactionTypeFactory()
        def actorTypeName = 'rep'
        ActorType actorType = aTfactory.getActorType(actorTypeName)
        def actorSimConfig = new SimConfigFactory().buildSim('localhost', '8080', 'base', new SimId('1234'), actorTypeName)

        when: ''
        Site site = siteFactory.buildSite(actorSimConfig, 'mysite')
        def repositoryUniqueId = site.getRepositoryUniqueId()
        OMElement ele = new SeparateSiteLoader().siteToXML(site)
        println new OMFormatter(ele).toString()

        then:
        repositoryUniqueId.startsWith(RepositoryUniqueIdSimConfigElement.getRepositoryUniqueIdBase())
    }

}
