package de.tech26.robotfactory.component.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import de.tech26.robotfactory.component.AddComponentInterface;
import de.tech26.robotfactory.component.ComponentContainsInterface;
import de.tech26.robotfactory.component.GetComponentInterface;

/**
 * MaterialComponent to hold material related all components
 * 
 * @author Jagadheeswar Reddy
 *
 */
@Component
public class MaterialComponent implements AddComponentInterface, ComponentContainsInterface, GetComponentInterface {
	private Set<Character> materialComponents = new HashSet<>();// should not have duplicate values

	@Override
	public boolean contains(Character component) {
		return materialComponents.contains(component);
	}

	@Override
	public void addComponent(Character component) {
		materialComponents.add(component);

	}

	@Override
	public Set<Character> getComponents() {
		return materialComponents;
	}

}
