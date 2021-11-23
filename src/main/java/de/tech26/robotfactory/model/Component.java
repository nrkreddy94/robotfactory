package de.tech26.robotfactory.model;

/**
 * Component has all details about robot part
 * 
 * @author Jagadheeswar Reddy
 *
 */
public class Component {

	private char code; // single character
	private double price; // decimal value
	private int available; // count integer
	private String name; // component name

	public char getCode() {
		return code;
	}

	public void setCode(char code) {
		this.code = code;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Component [code=" + code + ", price=" + price + ", available=" + available + ", name=" + name + "]";
	}

}
