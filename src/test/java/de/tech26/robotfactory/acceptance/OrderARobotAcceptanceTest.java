package de.tech26.robotfactory.acceptance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.tech26.robotfactory.model.Component;
import de.tech26.robotfactory.model.OrderRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Test cases for RobotoFactoryController
 * 
 * @author Jagadheeswar Reddy
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderARobotAcceptanceTest {

	@LocalServerPort
	private int springBootPort = 0;

	/**
	 * Test case for robot order
	 */
	@Test
	public void shouldOrderARobot() {
		String[] components = { "I", "A", "D", "F" };

		String body = orderPayload(components);

		postOrder(body).then().assertThat().statusCode(HttpStatus.CREATED.value()).body("orderid", notNullValue())
				.body("total", equalTo(160.11f));
	}

	/**
	 * Test case for BAD_REQUEST
	 */
	@Test
	public void shouldNotAllowInvalidBody() {
		String[] components = { "BENDER" };
		String body = orderPayload(components);
		postOrder(body).then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
	}

	/**
	 * Test case for UNPROCESSABLE_ENTITY
	 */
	@Test
	public void shouldNotAllowInvalidRobotConfiguration() {
		String[] components = { "A", "C", "I", "D" };

		String body = orderPayload(components);
		postOrder(body).then().assertThat().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

	/**
	 * Test case for available components in stocks
	 */
	@Test
	public void testStocks() {
		stocks().then().assertThat().statusCode(HttpStatus.OK.value()).body("$.size()", Matchers.greaterThan(0));
	}

	/**
	 * Test case for available face components
	 * 
	 */
	@Test
	public void testFaceComponent() {
		faceComponent().then().assertThat().statusCode(HttpStatus.OK.value()).body("$.size()", Matchers.greaterThan(0));
	}

	/**
	 * Test case for available arms components
	 */
	@Test
	public void testArmsComponent() {
		armsComponent().then().assertThat().statusCode(HttpStatus.OK.value()).body("$.size()", Matchers.greaterThan(0));
	}

	/**
	 * Test case for available mobility components
	 */
	@Test
	public void testMobilityComponent() {
		mobilityComponent().then().assertThat().statusCode(HttpStatus.OK.value()).body("$.size()",
				Matchers.greaterThan(0));
	}

	/**
	 * Test case for available material components
	 */
	@Test
	public void testMaterialComponent() {
		materialComponent().then().assertThat().statusCode(HttpStatus.OK.value()).body("$.size()",
				Matchers.greaterThan(0));
	}

	/**
	 * Test case for get component with code
	 */
	@Test
	public void testComponent() {
		String code = "A";
		getComponent(code).then().assertThat().statusCode(HttpStatus.OK.value()).body("code", equalTo(code));
	}

	/**
	 * Adding new component to stocks
	 */
	@Test
	public void testUpdateStock() {

		Component component = new Component();
		component.setCode('N');
		component.setName("New Face");
		component.setPrice(24.07f);
		component.setAvailable(10);

		String body = componentPayload(component);

		Response response = updateStock(body);
		int size = response.jsonPath().getList("$").size();
		int newComponent = size - 1;

		response.then().assertThat().statusCode(HttpStatus.OK.value()).body("[" + newComponent + "].name",
				equalTo(component.getName()));
	}

	private Response postOrder(String body) {
		return RestAssured.given().body(body).contentType(ContentType.JSON).when().port(springBootPort).post("/orders");
	}

	private Response stocks() {
		return RestAssured.given().when().port(springBootPort).get("/stocks");
	}

	private Response faceComponent() {
		return RestAssured.given().when().port(springBootPort).get("/face/component");
	}

	private Response armsComponent() {
		return RestAssured.given().when().port(springBootPort).get("/arms/component");
	}

	private Response mobilityComponent() {
		return RestAssured.given().when().port(springBootPort).get("/mobility/component");
	}

	private Response materialComponent() {
		return RestAssured.given().when().port(springBootPort).get("/material/component");
	}

	private Response getComponent(String code) {
		return RestAssured.given().when().port(springBootPort).get("/component?code=" + code);
	}

	private Response updateStock(String body) {
		return RestAssured.given().body(body).contentType(ContentType.JSON).when().port(springBootPort)
				.post("/stocks/update");
	}

	/**
	 * Convert array of component codes to json string
	 * 
	 * @param components
	 * @return
	 */
	private String orderPayload(String[] components) {
		List<Character> list = Arrays.asList(components).stream().map(component -> ((String) component).charAt(0))
				.collect(Collectors.toList());
		OrderRequest order = new OrderRequest();
		order.setComponents(list);

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(order);

		} catch (JsonProcessingException e) {
			return null;
		}
	}

	/**
	 * convert component object to json string
	 * 
	 * @param component
	 * @return
	 */
	private String componentPayload(Component component) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(component);

		} catch (JsonProcessingException e) {
			return null;
		}
	}
}
