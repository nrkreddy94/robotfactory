package de.tech26.robotfactory.model;

/**
 * OrderResponse with orderId and total price once robot manufactured
 * 
 * @author Jagadheeswar Reddy
 *
 */
public class OrderResponse {
	private String orderid; // would be alpha numeric
	private Double total; // would be decimal number

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "OrderResponse [orderid=" + orderid + ", total=" + total + "]";
	}

}
