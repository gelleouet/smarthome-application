package smarthome.automation

import grails.validation.Validateable;


@Validateable
class ChartCommand extends AbstractChartCommand<ChartCommand> {
	String groupe
	Chart chart
	
	
	static constraints = {
		groupe nullable: true
		chart nullable: true
		navigation nullable: true
	}


	@Override
	ChartCommand cloneForLastYear() {
		ChartCommand command = super.cloneForLastYear()
		command.groupe = groupe
		command.chart = chart
		return command
	}
}
