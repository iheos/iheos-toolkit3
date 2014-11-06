package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import spock.lang.Specification

/**
 * Created by bmajur on 5/27/14.
 */
class CreateActorErrorTest extends Specification {
    static String config = '''
<ActorsTransactions>
    <actor displayName="Document Registry" id="reg">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factories.DocumentRegistryActorFactory"/>
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
'''

    def 'Missing transaction def should throw exception'() {
        when:
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(config)


        then:
        thrown ToolkitRuntimeException
    }
}
