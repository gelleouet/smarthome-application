package smarthome.automation

import grails.validation.Validateable


@Validateable
class ProductionChartCommand extends AbstractChartCommand<ProductionChartCommand> {
	List<ProducteurEnergieAction> actions = []


	static constraints = {
		navigation nullable: true
	}
}
