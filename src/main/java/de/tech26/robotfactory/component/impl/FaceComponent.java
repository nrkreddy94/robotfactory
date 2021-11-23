package de.tech26.robotfactory.component.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import de.tech26.robotfactory.component.AddComponentInterface;
import de.tech26.robotfactory.component.ComponentContainsInterface;
import de.tech26.robotfactory.component.GetComponentInterface;
import de.tech26.robotfactory.component.RemoveComponentInterface;

/**
 * FaceComponent to hold face related all components
 * 
 * @author Jagadheeswar Reddy
 *
 */
@Component
public class FaceComponent
		implements AddComponentInterface, RemoveComponentInterface, ComponentContainsInterface, GetComponentInterface {

	private Set<Character> faceComponents = new HashSet<>();// should not have duplicate values

	@Override
	public Set<Character> getComponents() {
		return faceComponents;
	}

	@Override
	public boolean contains(Character component) {
		return faceComponents.contains(component);
	}

	@Override
	public void removeComponent(Character component) {
		faceComponents.remove(component);

	}

	@Override
	public void addComponent(Character component) {
		faceComponents.add(component);

	}

}
