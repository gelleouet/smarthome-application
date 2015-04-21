package smarthome.automation.deviceType.catalogue

/**
 * Périphérique Télé-info EDF
 * 
 * @author gregory
 *
 */
class TeleInformation extends AbstractDeviceType {
	/**
	 * Le template par défaut pour préparer les données du chart
	 *
	 * @return
	 */
	def chartDataTemplate() {
		'/deviceType/catalogue/teleInformationChartDatas'
	}
}
