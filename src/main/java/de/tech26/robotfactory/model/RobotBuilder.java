package de.tech26.robotfactory.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * RobotBuilder for manufacture the robot with provided configurations
 * 
 * @author Jagadheeswar Reddy
 *
 */
@org.springframework.stereotype.Component
public class RobotBuilder {
	private List<Component> components = null;
	private Double price;

	@PostConstruct
	public void initialize() {
		components = new ArrayList<>();
	}

	/**
	 * Apply component to manufacture the robot with given configurations
	 * 
	 * @param componet
	 */
	public void apply(Component componet) {
		components.add(componet);
	}

	/**
	 * Calculate all applied components price
	 * 
	 * @return
	 */
	public Double totalPrice() {
		price = components.stream().mapToDouble(component -> component.getPrice()).sum();
		return price;
	}

}
