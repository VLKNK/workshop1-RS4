package rsvier.workshop.controller;

import java.util.*;

import rsvier.workshop.controller.MainController.TypeOfController;
import rsvier.workshop.dao.*;
import rsvier.workshop.domain.*;
import rsvier.workshop.view.*;

public class OrderController extends Controller {

	private OrderView orderView = new OrderView();
	private OrderDAO orderDao = new OrderDAOImp();
	private CustomerController customerController = new CustomerController();
	private OrderLineController orderLineController = new OrderLineController();
	private CustomerView customerView = new CustomerView();

	@Override
	public void runView() {
		orderView.printHeaderMessage();
		orderView.printMenuMessage();
		searchCreateOrderSwitch(orderView.getIntInput());
	}

	
	public void searchCreateOrderSwitch(int menuNumber) {

		switch (menuNumber) {

		case 1: // Search order
			selectOrderSearchMenuSwitch();
			break;

		case 2: // Create order for a customer
			doCreateOrder((customerController.searchCustomerByLastName()));
			break;

		case 0: // Go back to previous menu
			MainController.setController(TypeOfController.EMPLOYEE);
			break;

		default:
			orderView.printMenuInputIsWrong();
			runView();
			break;
		}
	}

	
	// Search order by order-id or customer last name
	public void searchOrderSwitch() {

		orderView.printAskSearchOrderByNumberOrByName();
		int menuNumber = orderView.getIntInput();

		switch (menuNumber) {
		case 1:// Search order by order ID
			searchOrderByOrderId();
			break;

		case 2:// Search order by customer last name
			updateOrDeleteOrderSwitch(searchOrderByLastName(customerController.searchCustomerByLastName()));
			break;

		case 0: // Back to previous menu
			runView();
			break;

		default: // Back to this same menu
			orderView.printMenuInputIsWrong();
			searchOrderSwitch();
			break;
		}
	}

	public void updateOrDeleteOrderSwitch(Order order) {

		orderView.printAskUserToUpdateOrDeleteOrder();
		int menuNumber = orderView.getIntInput();

		switch (menuNumber) {

		case 1:// Update order
			updateExistingOrder(order);
			break;

		case 2: // Delete order
			doDeleteOrder(order);
			break;

		case 0:// Go back to previous Menu

		default:
			orderView.printMenuInputIsWrong();
			runView();

		}

	}

	
	public void updateExistingOrder(Order order) {

		
		boolean updating = true;

		while (updating) {
			
			orderView.printUpdateExistingOrder();
			int menuNumber = orderView.getIntInput();

			switch (menuNumber) {

			case 1: // Go to orderLine
				orderLineController.editOrDeleteOrderLineSwitchMenu(order);
				break;

			case 2: // Add orderLines to order
				break;

			case 3: // Save changes in the database
				
				//DEZE MOET NOG GEIMPLEMENTEERD WORDEN MET DAO
				updating = false;
				break;

			case 4: // Abort operation and don't apply the changes made in the order
				updating = false;
				break;
				
			default:
				break;
			}
		}

	}


	public void selectOrderSearchMenuSwitch() {

		orderView.printAskSearchOrderByNumberOrByName();
		int menuNumber = orderView.getIntInput();

		switch (menuNumber) {
		case 1:// Search order by order ID
			searchOrderByOrderId();
			break;

		case 2:// Search order by customer last name
			updateOrDeleteOrderSwitch(searchOrderByLastName(customerController.searchCustomerByLastName()));
			break;

		case 0: // Back to previous menu
			runView();
			break;

		default: // Back to this same menu
			orderView.printMenuInputIsWrong();
			selectOrderSearchMenuSwitch();
			break;
		}
	}

	
	// Search order by Last name
	public Order searchOrderByLastName(Person person) {
		
		//One person can have more than one order, so first get all orders
		//and store them in a order list
		
		List<Order> orderList = new ArrayList<>();

		if (person == null) {

			runView();

		} else {

			orderList = orderDao.getAllOrdersFromPerson(person);

			if (orderList.size() == 0) {

				orderView.printYouDontHaveOrders();
				runView();
				return null;
			}

			if (orderList.size() == 1) {

				orderView.printOrdersFound("Order nummer:", 1, orderList.get(0));

				return orderList.get(0);

			} else {
				
				//if the person has more than 1 order, a list is printed
				for (int i = 0; i < orderList.size(); i++) {

					orderView.printOrdersFound("Order nummer:", i + 1, orderList.get(i));
				}
			}

		}
		
		//The user is then asked to select an order
		int choice = selectOrder();
		while (choice < 1 | choice > orderList.size()) {
			orderView.printMenuInputIsWrong();
			choice = selectOrder();
		}

		return (orderList.get(choice - 1));
	}

	
	public int selectOrder() {

		orderView.printAskSelectOrder();

		return orderView.getIntInput();
	}

	
	// Search order by OrderId
	public void searchOrderByOrderId() {

		orderView.printAskOrderId();
		Order order = orderDao.getOrderById(orderView.getIntInput());

		if (order == null) {
			orderView.printOrderNotFound();
			runView();
		} else {
			updateOrDeleteOrderSwitch(order);
		}
	}

	
	public void doDeleteOrder(Order order) {

		if (orderView.confirmYesOrNo().equalsIgnoreCase("J")) {
			orderDao.deleteOrder(order);
			orderView.printOrderSuccessFullyDeleted();
			runView();
		} else {
			runView();
		}

	}

	
	public void doCreateOrder(Person person) {

		// check if person was found
		if (person == null) {
			
			runView();
		
		} else {

			// Build an order object with the person object
			Order.OrderBuilder orderBuilder = new Order.OrderBuilder();
			orderBuilder.person(person);
			Order order = orderBuilder.build();
			order.getPerson().toString();
			order.toString();

			// Pass the order to the switch in the orderLineController
			orderLineController.orderLineMenuSwitch(order);

		}

	}
}