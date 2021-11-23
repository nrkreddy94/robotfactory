package de.tech26.robotfactory.utils;

import org.springframework.beans.factory.annotation.Autowired;

import de.tech26.robotfactory.component.impl.ArmsComponent;
import de.tech26.robotfactory.component.impl.FaceComponent;
import de.tech26.robotfactory.component.impl.MaterialComponent;
import de.tech26.robotfactory.component.impl.MobilityComponent;
import de.tech26.robotfactory.model.Component;

/**
 * ComponentCategoryUtil to add components to right group/category
 * 
 * @author Jagadheeswar Reddy
 *
 */
@org.springframework.stereotype.Component
public class ComponentCategoryUtil {

	@Autowired
	private FaceComponent faceComponent;
	@Autowired
	private ArmsComponent armsComponent;
	@Autowired
	private MobilityComponent mobilityComponent;
	@Autowired
	private MaterialComponent materialComponent;

	/**
	 * Takes component as input and find out belongs to which group and add it to
	 * same
	 * 
	 * @param component
	 */
	public void categorizeComponent(Component component) {

		if (component.getName().toUpperCase().contains(ComponentType.FACE.toString()))
			faceComponent.addComponent(component.getCode());

		if (component.getName().toUpperCase().contains(ComponentType.ARM.toString()))
			armsComponent.addComponent(component.getCode());

		if (component.getName().toUpperCase().contains(ComponentType.MOBILITY.toString()))
			mobilityComponent.addComponent(component.getCode());

		if (component.getName().toUpperCase().contains(ComponentType.MATERIAL.toString()))
			materialComponent.addComponent(component.getCode());
	}
}
