package de.tech26.robotfactory.model;

import java.util.List;

/**
 * OrderRequest to accept all required components as configuration to build
 * robot
 * 
 * @author Jagadheeswar Reddy
 *
 */
public class OrderRequest {
	private List<Character> components;

	public List<Character> getComponents() {
		return components;
	}

	public void setComponents(List<Character> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "OrderRequest [components=" + components + "]";
	}
}
