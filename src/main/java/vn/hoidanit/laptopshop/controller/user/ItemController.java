package vn.hoidanit.laptopshop.controller.user;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.service.CustomUserDetails;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.VNPayService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ItemController {
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final VNPayService vNPayService;

    public ItemController(ProductService productService, CartRepository cartRepository,
            OrderRepository orderRepository, CartDetailRepository cartDetailRepository, VNPayService vnPayService) {
        this.productService = productService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.vNPayService = vnPayService;
    }

    @GetMapping("/product/{id}")
    public String getProductDetail(@PathVariable("id") long id, Model model) {
        model.addAttribute("product", this.productService.getProductById(id));
        return "client/product/detail";
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable("id") long id, @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        String email = userDetails.getUsername();
        long productId = id;

        this.productService.handleAddProductToCart(email, productId, session);

        return "redirect:/";
    }

    @GetMapping("/cart")
    public String GetCartPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = new User();
        user.setId(userDetails.getId());

        Cart cart = this.productService.getCartByUser(user);
        List<CartDetail> cartDetails = null;
        double totalPrice = 0;

        if (cart != null) {
            cartDetails = cart.getCartDetails();
            for (CartDetail cartDetail : cartDetails) {
                totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
            }
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        return "client/cart/show";
    }

    @PostMapping("/cart/update-quantity")
    @ResponseBody
    public ResponseEntity<?> updateCartQuantity(
            @RequestParam("cartDetailId") long cartDetailId,
            @RequestParam("action") String action,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String email = userDetails.getUsername();
        vn.hoidanit.laptopshop.domain.CartUpdateResponse response = this.productService
                .handleUpdateCartDetailQuantity(email, cartDetailId, action);

        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("Lỗi cập nhật giỏ hàng");
    }

    @PostMapping("/cart/delete/{id}")
    public String postMethodName(@PathVariable("id") long id, HttpSession session) {
        this.productService.handleRemoveCartDetail(id, session);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String getCheckoutPage(@AuthenticationPrincipal CustomUserDetails userDetail, Model model) {
        User user = new User();
        user.setId(userDetail.getId());

        Cart cart = this.productService.getCartByUser(user);
        List<CartDetail> cartDetails = null;
        double totalPrice = 0;

        if (cart != null) {
            cartDetails = cart.getCartDetails();
            for (CartDetail cartDetail : cartDetails) {
                totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
            }
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "client/cart/checkout";
    }

    @PostMapping("/place-order")
    public String handlePlaceOrder(
            HttpServletRequest request,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone,
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("totalPrice") String totalPrice, @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session) throws UnsupportedEncodingException {

        User user = new User();
        user.setId(userDetails.getId());

        final String uuid = UUID.randomUUID().toString().replace("-", "");

        this.productService.handlePlaceOrder(user, receiverName, receiverAddress, receiverPhone, paymentMethod, uuid,
                session);

        if (!paymentMethod.equals("COD")) {
            // todo: redirect to VNPAY
            String ip = this.vNPayService.getIpAddress(request);
            String vnpUrl = this.vNPayService.generateVNPayURL(Double.parseDouble(totalPrice), uuid, ip);

            return "redirect:" + vnpUrl;
        }

        return "redirect:/thanks";
    }

    @GetMapping("/thanks")
    public String getThanksPage(
            @RequestParam("vnp_ResponseCode") Optional<String> vnpayResponseCode,
            @RequestParam("vnp_TxnRef") Optional<String> paymentRef) {
                
        return "client/cart/thanks";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(@AuthenticationPrincipal CustomUserDetails userDetail, Model model) {
        User user = new User();
        user.setId(userDetail.getId());
        List<Order> orders = this.orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "client/order/order-history";
    }

    @PostMapping("/add-to-cart-from-detail")
    public String handleAddToCartFromDetail(@RequestParam("id") long id,
            @RequestParam("quantity") long quantity,
            @AuthenticationPrincipal CustomUserDetails userDetails, HttpSession session) {

        User user = new User();
        user.setId(userDetails.getId());

        Cart cart = this.cartRepository.findByUser(user);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setSum(0);

            cart = this.cartRepository.save(newCart);
        }

        Product product = this.productService.getProductById(id);
        if (product != null) {
            CartDetail cartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product);
            if (cartDetail == null) {
                cartDetail = new CartDetail();
                cartDetail.setCart(cart);
                cartDetail.setPrice(product.getPrice());
                cartDetail.setProduct(product);
                cartDetail.setQuantity(quantity);

                int s = cart.getSum() + 1;
                cart.setSum(s);
                this.cartRepository.save(cart);

                session.setAttribute("sum", s);

                this.cartDetailRepository.save(cartDetail);
            } else {
                cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
                this.cartDetailRepository.save(cartDetail);
            }
        }

        return "redirect:/product/" + id;
    }

    @GetMapping("/product")
    public String getAllProductPage(Model model, ProductCriteriaDTO productCriteriaDTO,
            HttpServletRequest request) {

        int page = 1;
        try {
            if (productCriteriaDTO.getPage().isPresent()) {
                page = Integer.parseInt(productCriteriaDTO.getPage().get());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        Pageable pageable = PageRequest.of(page - 1, 6);

        Page<Product> page2 = this.productService.getAllProductsWithSpecification(pageable, productCriteriaDTO);

        // Build query string without 'page' param to preserve filters in pagination
        String queryString = request.getQueryString();
        if (queryString != null) {
            queryString = queryString.replaceAll("(^|&)page=[^&]*", "").replaceAll("^&", "");
        }

        List<Product> products = page2.getContent();
        model.addAttribute("products", products);
        model.addAttribute("totalPage", page2.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("queryString", queryString != null ? queryString : "");

        return "client/product/show";
    }

}
