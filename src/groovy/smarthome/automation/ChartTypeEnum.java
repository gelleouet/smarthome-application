package smarthome.automation;

public enum ChartTypeEnum {
	Histogram("Histogram"),
	Column("ColumnChart"), 
	Combo("ComboChart"), 
	Line("LineChart"), 
	Pie("PieChart"), 
	Scatter("ScatterChart"), 
	Bubble("BubbleChart");
	
	
	private ChartTypeEnum(String factory) {
		this.factory = factory;
	}

	private String factory;

	public String getFactory() {
		return factory;
	}
	
	
}
