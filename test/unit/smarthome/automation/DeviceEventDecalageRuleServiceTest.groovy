package smarthome.automation

import smarthome.rule.DeviceEventDecalageRuleService;
import spock.lang.Specification;

@TestFor(DeviceEventDecalageRuleService)
class DeviceEventDecalageRuleServiceTest {
	DeviceEvent deviceEvent
	Date scheduledDate
	Date resultDate
	
	@Test
	void testAvantHeureEteSoir() {
		deviceEvent = new DeviceEvent(solstice: 'été', synchroSoleil: true,
			heureDecalage: '22:30', heureEte: true)
		scheduledDate = Date.parse("dd/MM/yyyy HH:mm", "26/03/2016 18:15")
		resultDate = service.execute(deviceEvent, true, [scheduledDate: scheduledDate])
		
		println "from scheduled $scheduledDate to $resultDate"
		assertEquals 'AvantHeureEte soir : heure', 20, resultDate[Calendar.HOUR_OF_DAY]
		assertEquals 'AvantHeureEte soir : minute', 29, resultDate[Calendar.MINUTE]
	}
	
	
	@Test
	void testApresHeureEteSoir() {
		deviceEvent = new DeviceEvent(solstice: 'été', synchroSoleil: true,
				heureDecalage: '22:30', heureEte: true)
		scheduledDate = Date.parse("dd/MM/yyyy HH:mm", "27/03/2016 18:15")
		resultDate = service.execute(deviceEvent, true, [scheduledDate: scheduledDate])
		
		println "from scheduled $scheduledDate to $resultDate"
		assertEquals 'ApresHeureEte soir : heure', 21, resultDate[Calendar.HOUR_OF_DAY]
		assertEquals 'ApresHeureEte soir : minute', 29, resultDate[Calendar.MINUTE]
		
	}
	
	@Test
	void testAvantHeureHiverSoir() {
		deviceEvent = new DeviceEvent(solstice: 'été', synchroSoleil: true,
			heureDecalage: '22:30', heureEte: true)
		scheduledDate = Date.parse("dd/MM/yyyy HH:mm", "29/10/2016 18:15")
		resultDate = service.execute(deviceEvent, true, [scheduledDate: scheduledDate])
		
		println "from scheduled $scheduledDate to $resultDate"
		assertEquals 'AvantHeureHiver soir : heure', 20, resultDate[Calendar.HOUR_OF_DAY]
		assertEquals 'AvantHeureHiver soir : minute', 28, resultDate[Calendar.MINUTE]
	}
	
	
	@Test
	void testApresHeureHiverSoir() {
		deviceEvent = new DeviceEvent(solstice: 'été', synchroSoleil: true,
				heureDecalage: '22:30', heureEte: true)
		scheduledDate = Date.parse("dd/MM/yyyy HH:mm", "30/10/2016 18:15")
		resultDate = service.execute(deviceEvent, true, [scheduledDate: scheduledDate])
		
		println "from scheduled $scheduledDate to $resultDate"
		assertEquals 'ApresHeureHiver soir : heure', 19, resultDate[Calendar.HOUR_OF_DAY]
		assertEquals 'ApresHeureHiver soir : minute', 27, resultDate[Calendar.MINUTE]
		
	}
	
	
	@Test
	void testSolsticeEte_14avrilSoir() {
		deviceEvent = new DeviceEvent(solstice: 'été', synchroSoleil: true,
				heureDecalage: '22:30', heureEte: true)
		scheduledDate = Date.parse("dd/MM/yyyy HH:mm", "14/04/2016 18:15")
		resultDate = service.execute(deviceEvent, true, [scheduledDate: scheduledDate])
		
		println "from scheduled $scheduledDate to $resultDate"
		assertEquals '14 avril soir : heure', 21, resultDate[Calendar.HOUR_OF_DAY]
		assertEquals '14 avril soir : minute', 54, resultDate[Calendar.MINUTE]
	}
	
	
	@Test
	void testSolsticeHiver_14avrilMatin() {
		deviceEvent = new DeviceEvent(solstice: 'hiver', synchroSoleil: true,
				heureDecalage: '08:15')
		scheduledDate = Date.parse("dd/MM/yyyy HH:mm", "14/04/2016 07:45")
		resultDate = service.execute(deviceEvent, true, [scheduledDate: scheduledDate])
		
		println "from scheduled $scheduledDate to $resultDate"
		assertEquals '14 avril matin : heure', 7, resultDate[Calendar.HOUR_OF_DAY]
		assertEquals '14 avril matin : minute', 56, resultDate[Calendar.MINUTE]
	}
}
