package de.tech26.robotfactory.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.tech26.robotfactory.exception.ValidationException;
import de.tech26.robotfactory.model.Component;
import de.tech26.robotfactory.model.OrderRequest;
import de.tech26.robotfactory.model.OrderResponse;
import de.tech26.robotfactory.service.StockService;

/**
 * RobotoFactoryController for build robot with requested configuration and
 * manage components and stocks
 * 
 * @author Jagadheeswar Reddy
 *
 */
@RestController
public class RobotoFactoryController {
	private static final Log logger = LogFactory.getLog(RobotoFactoryController.class);

	@Autowired
	StockService stockService;

	/**
	 * Take OrderRequest and manufacture robot
	 * 
	 * @param orderRequest
	 * @return
	 */
	@RequestMapping(value = { "/orders" }, method = RequestMethod.POST)
	public ResponseEntity<OrderResponse> orders(@RequestBody OrderRequest orderRequest) {
		logger.info("Entering orders with orderRequest =" + orderRequest);
		OrderResponse orderResponse = null;
		try {
			orderResponse = stockService.processOrder(orderRequest);
			logger.info("Leaving  orders " + orderResponse);
		} catch (ValidationException exception) {
			throw exception;
		}
		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.CREATED);
	}

	/**
	 * return all available component in stocks
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/stocks" }, method = RequestMethod.GET)
	public List<Component> stocks() {
		logger.info("Entering stocks");
		List<Component> stocks = stockService.getAllComponents();
		logger.info("Leaving stocks");
		return stocks;
	}

	/**
	 * Take component as in put and update stocks
	 * 
	 * @param component
	 * @return
	 */
	@RequestMapping(value = { "/stocks/update" }, method = RequestMethod.POST)
	public ResponseEntity<List<Component>> stockUpdate(@RequestBody Component component) {
		logger.info("Entering stockUpdate with component =" + component);
		List<Component> updatedStoks = stockService.addOrUpdate(component);
		logger.info("Leaving stockUpdate");
		return new ResponseEntity<List<Component>>(updatedStoks, HttpStatus.OK);
	}

	/**
	 * Take component code as input and return component
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = { "/component" }, method = RequestMethod.GET)
	public Component component(@RequestParam Character code) {
		logger.info("Entering component  with code = " + code);
		Component component = stockService.getComponentByCode(code);
		logger.info("Leaving component");
		return component;
	}

	/**
	 * Take component code as input and return updated component
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = { "/updateComponentAvailability" }, method = RequestMethod.POST)
	public Component updateComponentAvailability(@RequestParam Character code) {
		logger.info("Entering updateComponentAvailability with code =" + code);
		Component component = stockService.updateComponentAvailability(code);
		logger.info("Leaving updateComponentAvailability");
		return component;
	}

	/**
	 * Get all face components
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/face/component" }, method = RequestMethod.GET)
	public List<Component> faceComponent() {
		logger.info("Entering faceComponent ");
		List<Component> components = stockService.getAllFaceComponents();
		logger.info("Leaving faceComponent ");
		return components;
	}

	/**
	 * Get all arms components
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/arms/component" }, method = RequestMethod.GET)
	public List<Component> armsComponent() {
		logger.info("Entering armsComponent ");
		List<Component> components = stockService.getAllArmsComponents();
		logger.info("Leaving armsComponent ");
		return components;
	}

	/**
	 * Get all mobility components
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/mobility/component" }, method = RequestMethod.GET)
	public List<Component> mobilityComponent() {
		logger.info("Entering mobilityComponent ");
		List<Component> components = stockService.getAllMobilityComponents();
		logger.info("Leaving mobilityComponent ");
		return components;
	}

	/**
	 * Get all material components
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/material/component" }, method = RequestMethod.GET)
	public List<Component> materialComponent() {
		logger.info("Entering materialComponent ");
		List<Component> components = stockService.getAllMaterialComponents();
		logger.info("Leaving materialComponent ");
		return components;
	}

}
