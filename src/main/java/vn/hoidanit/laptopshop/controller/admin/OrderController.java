package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;







@Controller
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderController(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @GetMapping("/admin/order")
    public String getOrderPage(Model model) {
        List<Order> orders = this.orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getOrderDetailPage(@PathVariable("id") long id, Model model) {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        if(orderOptional.isPresent()) {
            Order order = orderOptional.get();
            model.addAttribute("orderDetails", order.getOrderDetails());
        }
        return "admin/order/detail";
    }
    
    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(@PathVariable("id") long id, Model model) {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        if(orderOptional.isPresent()) {
            Order order = orderOptional.get();
            model.addAttribute("order", order);
        }
        return "admin/order/update";
    }

    @PostMapping("/update-order/{id}")
    public String handleUpdateOrder(@PathVariable("id") long id, @ModelAttribute("order") Order order) {
        Optional<Order> newOrderOptional = this.orderRepository.findById(id);
        if(newOrderOptional.isPresent()) {
            Order newOrder = newOrderOptional.get();
            newOrder.setStatus(order.getStatus());
        }
        return "redirect:/admin/order";
    }
    
    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrderPage(@PathVariable("id") long id, Model model) {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        if(orderOptional.isPresent()) {
            Order order = orderOptional.get();
            model.addAttribute("order", order);
        }
        return "admin/order/delete";
    }

    @PostMapping("/admin/delete-order")
    public String handleDeleteOrder(@ModelAttribute("order") Order order) {

        Optional<Order> newOrderOptional = this.orderRepository.findById(order.getId());
        if(newOrderOptional.isPresent()) {
            Order newOrder = newOrderOptional.get();

            List<OrderDetail> orderDetails = newOrder.getOrderDetails();
            for(OrderDetail orderDetail : orderDetails){
                this.orderDetailRepository.deleteById(orderDetail.getId());
            }
            this.orderRepository.deleteById(newOrder.getId());
        }
        
        return "redirect:/admin/order";
    }
    
    
    
    
}
