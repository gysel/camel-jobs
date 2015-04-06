package org.apache.camel.component;

public class Car {

	private String brand;
	private String model;
	private Integer nrOfWheels;
	private EngineType engineType;

	enum EngineType {
		DIESEL, PETROL, ELECTRIC
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getNrOfWheels() {
		return nrOfWheels;
	}

	public void setNrOfWheels(Integer nrOfWheels) {
		this.nrOfWheels = nrOfWheels;
	}

	public EngineType getEngineType() {
		return engineType;
	}

	public void setEngineType(EngineType engineType) {
		this.engineType = engineType;
	}

}
