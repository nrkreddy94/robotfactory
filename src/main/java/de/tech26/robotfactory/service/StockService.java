package de.tech26.robotfactory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tech26.robotfactory.component.impl.ArmsComponent;
import de.tech26.robotfactory.component.impl.FaceComponent;
import de.tech26.robotfactory.component.impl.MaterialComponent;
import de.tech26.robotfactory.component.impl.MobilityComponent;
import de.tech26.robotfactory.exception.UnProcessableException;
import de.tech26.robotfactory.exception.ValidationException;
import de.tech26.robotfactory.model.Component;
import de.tech26.robotfactory.model.OrderRequest;
import de.tech26.robotfactory.model.OrderResponse;
import de.tech26.robotfactory.model.RobotBuilder;
import de.tech26.robotfactory.repo.StockRepo;
import de.tech26.robotfactory.utils.ComponentType;
import de.tech26.robotfactory.utils.RandomUtil;

/**
 * StockService Provides Stock Repo features as services
 * 
 * @author Jagadheeswar Reddy
 *
 */
@Service
public class StockService {
	private static final Log logger = LogFactory.getLog(StockService.class);

	@Autowired
	private FaceComponent faceComponent;

	@Autowired
	private ArmsComponent armsComponent;

	@Autowired
	private MobilityComponent mobilityComponent;

	@Autowired
	private MaterialComponent materialComponent;

	@Autowired
	StockRepo stockRepo;

	@Autowired
	RobotBuilder robotBuilder;

	/**
	 * Get all available face components
	 * 
	 * @return List<Component>
	 */
	public List<Component> getAllFaceComponents() {
		return categoryComponents(faceComponent.getComponents());
	}

	/**
	 * Get all available arms components
	 * 
	 * @return List<Component>
	 */
	public List<Component> getAllArmsComponents() {
		return categoryComponents(armsComponent.getComponents());
	}

	/**
	 * Get all available mobility components
	 * 
	 * @return List<Component>
	 */
	public List<Component> getAllMobilityComponents() {
		return categoryComponents(mobilityComponent.getComponents());
	}

	/**
	 * Get all available material components
	 * 
	 * @return List<Component>
	 */
	public List<Component> getAllMaterialComponents() {
		return categoryComponents(materialComponent.getComponents());
	}

	/**
	 * Get all available components from stocks
	 * 
	 * @return List<Component>
	 */
	public List<Component> getAllComponents() {
		return stockRepo.getAllComponents();
	}

	/**
	 * Get component for give code
	 * 
	 * @return Component
	 */
	public Component getComponentByCode(Character code) {
		return stockRepo.getComponentByCode(code);
	}

	/**
	 * Get all available components with updated stocks
	 * 
	 * @return List<Component>
	 */
	public List<Component> addOrUpdate(Component component) {

		boolean isValidComponent = validateComponent(component);
		if (!isValidComponent)
			throw new ValidationException("Bad Request! Please check component values");
		
		stockRepo.updateStocks(component.getCode(), component);
		return stockRepo.getAllComponents();
	}


	/**
	 * update component availability - decrement number once component applied
	 * 
	 * @param code
	 * @return
	 */
	public Component updateComponentAvailability(Character code) {

		Component part = stockRepo.getComponentByCode(code);
		if (part.getAvailable() > 0) {
			part.setAvailable(part.getAvailable() - 1);
			stockRepo.updateStocks(code, part);
			part = stockRepo.getComponentByCode(code);
		}
		return part;
	}

	/**
	 * Takes orderRequest as input and process order with all validations
	 * 
	 * @param orderRequest
	 * @return
	 */
	public OrderResponse processOrder(OrderRequest orderRequest) {
		logger.info("Entering processOrder");

		robotBuilder.initialize(); // For each order list should be refreshed

		// Check order request contains all required configurations
		if (orderRequest.getComponents().size() != ComponentType.values().length)
			throw new ValidationException("Bad Request! Please check configurations");

		// An order will be valid if it contains one, and only one, part of face,
		// material, arms and mobility.
		boolean isValidRequest = validateOrderRequest(orderRequest);
		if (!isValidRequest)
			throw new UnProcessableException("UnProcessable Configuration! Please try with different configurations");

		// Check component availability in stock
		boolean isAllComponentsAvailable = checkComponentAvailability(orderRequest);
		if (!isAllComponentsAvailable)
			throw new UnProcessableException(
					"UnProcessable Configuration! Stock Shortage, Please try with different configurations");

		// apply components and get order price
		Double price = applyCopmonents(orderRequest.getComponents());

		// prepare response object
		OrderResponse order = new OrderResponse();
		order.setOrderid(RandomUtil.generateCode(6)); // generate alpha numeric random string with 6 chars length
		order.setTotal(price);
		logger.info("Leaving processOrder");
		return order;
	}

	/**
	 * Apply all components provided in order request configuration
	 * 
	 * @param components
	 * @return
	 */
	private Double applyCopmonents(List<Character> components) {
		logger.info("Entering applyCopmonents");
		components.stream().forEach(code -> {
			robotBuilder.apply(stockRepo.getComponentByCode(code));
			updateComponentAvailability(code);
		});
		logger.info("Leaving applyCopmonents");
		return robotBuilder.totalPrice();
	}

	/**
	 * An order will be valid if it contains one, and only one, part of face,
	 * material, arms and mobility.
	 * 
	 * @param orderRequest
	 * @return
	 */
	private boolean validateOrderRequest(OrderRequest orderRequest) {

		boolean isValidFace = orderRequest.getComponents().stream()
				.anyMatch(component -> faceComponent.contains(component));
		boolean isValidArm = orderRequest.getComponents().stream()
				.anyMatch(component -> armsComponent.contains(component));
		boolean isValidMobility = orderRequest.getComponents().stream()
				.anyMatch(component -> mobilityComponent.contains(component));
		boolean isValidMaterial = orderRequest.getComponents().stream()
				.anyMatch(component -> materialComponent.contains(component));

		logger.info("isValidFace =" + isValidFace + "isValidArm =" + isValidArm + "isValidMobility =" + isValidMobility
				+ "isValidMaterial =" + isValidMaterial);
		return (isValidFace && isValidArm && isValidMobility && isValidMaterial) ? true : false;
	}

	/**
	 * Check component availability in stocks
	 * 
	 * @param orderRequest
	 * @return
	 */
	private boolean checkComponentAvailability(OrderRequest orderRequest) {

		boolean allComponentsAvailable = orderRequest.getComponents().stream()
				.allMatch(code -> stockRepo.getComponentByCode(code).getAvailable() > 0);

		return allComponentsAvailable;
	}

	/**
	 * Get components with component code
	 * 
	 * @param codes
	 * @return
	 */
	private List<Component> categoryComponents(Set<Character> codes) {
		List<Component> components = new ArrayList<>();
		codes.stream().forEach(code -> components.add(getComponentByCode(code)));
		return components;
	}
	
	/**
	 * validate component has proper data or not
	 * @param component
	 * @return
	 */
	private boolean validateComponent(Component component) {
		if (!(component.getCode() >= 'A' && component.getCode() <= 'Z'))
			return false;

		if (component.getName() == null && component.getName().isEmpty())
			return false;

		return true;

	}
}
