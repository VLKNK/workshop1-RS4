package rsvier.workshop.dao;
import java.util.*;

import rsvier.workshop.domain.*;


public interface OrderLineDAO {
	public List<OrderLine> getAllOrderLines();
	public List<OrderLine> getAllOrderLinesFromProduct(Product product);
	public List<OrderLine> getAllOrderLinesFromOrder(Order order);
	public OrderLine getOrderLine(int orderLineId);
	public void createOrderLine(OrderLine orderLine);
	public void deleteOrderLine(OrderLine orderLine);
	public void updateOrderLine(OrderLine orderLine);
	
}