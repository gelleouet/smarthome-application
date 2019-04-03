package smarthome.automation;

public enum ChartTypeEnum {
	Bar("BarChart"),
	Histogram("Histogram"),
	Column("ColumnChart"), 
	Combo("ComboChart"), 
	Line("LineChart"), 
	Pie("PieChart"), 
	Scatter("ScatterChart"), 
	Bubble("BubbleChart"),
	Area("AreaChart");
	
	
	private ChartTypeEnum(String factory) {
		this.factory = factory;
	}

	private String factory;

	public String getFactory() {
		return factory;
	}
	
	
}
