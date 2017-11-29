
package smarthome.automation.workflow

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import smarthome.automation.scenario.FermetureVoletEnsoleillement;
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class FermetureVoletEnsoleillementSpec extends Specification {

	def workflow
	
    def setup() {
		workflow = new FermetureVoletEnsoleillement()
    }

    def cleanup() {
    }

    void "test execute"() {
		println workflow
    }
}
