package de.tech26.robotfactory.component.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import de.tech26.robotfactory.component.AddComponentInterface;
import de.tech26.robotfactory.component.ComponentContainsInterface;
import de.tech26.robotfactory.component.GetComponentInterface;

/**
 * ArmsComponent to hold arms related all components
 * 
 * @author Jagadheeswar Reddy
 *
 */
@Component
public class ArmsComponent implements AddComponentInterface, ComponentContainsInterface, GetComponentInterface {
	private Set<Character> armsComponents = new HashSet<>(); // should not have duplicate values

	@Override
	public boolean contains(Character component) {
		return armsComponents.contains(component);
	}

	@Override
	public void addComponent(Character component) {
		armsComponents.add(component);

	}

	@Override
	public Set<Character> getComponents() {
		return armsComponents;
	}

}
