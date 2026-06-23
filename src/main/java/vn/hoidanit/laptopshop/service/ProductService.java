package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.hoidanit.laptopshop.domain.CartUpdateResponse;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;

import java.util.Optional;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import vn.hoidanit.laptopshop.specification.ProductSpecification;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    // public Page<Product> getAllProductsWithSpecification(Pageable pageable,
    // String name){
    // return this.productRepository.findAll(ProductSpecification.nameLike(name),
    // pageable);
    // }

    // public Page<Product> getAllProductsWithSpecification(Pageable pageable,
    // Double minPrice){
    // return
    // this.productRepository.findAll(ProductSpecification.minPrice(minPrice),
    // pageable);
    // }

    // public Page<Product> getAllProductsWithSpecification(Pageable pageable,
    // Double maxPrice){
    // return
    // this.productRepository.findAll(ProductSpecification.maxPrice(maxPrice),
    // pageable);
    // }

    // public Page<Product> getAllProductsWithSpecification(Pageable page, String
    // factory) {
    // return
    // this.productRepository.findAll(ProductSpecification.matchFactory(factory),page);
    // }

    // public Page<Product> getAllProductsWithSpecification(Pageable page,
    // List<String> factory) {
    // return
    // this.productRepository.findAll(ProductSpecification.matchListFactory(factory),
    // page);
    // }

    // public Page<Product> getAllProductsWithSpecification(Pageable page,
    // List<String> price) {
    // Specification<Product> combinedSpecification = (root, query, criteriaBuilder)
    // -> criteriaBuilder.disjunction();
    // int count = 0;
    // for(String p : price) {
    // double max = 0;
    // double min = 0;
    // switch (p) {
    // case "10-toi-15-trieu":
    // min = 10000000;
    // max = 15000000;
    // count++;
    // break;
    // case "15-toi-20-trieu":
    // min = 15000000;
    // max = 20000000;
    // count++;
    // break;
    // case "20-toi-30-trieu":
    // min = 20000000;
    // max = 30000000;
    // count++;
    // break;
    // }
    // if(min != 0 && max !=0 ) {
    // combinedSpecification =
    // combinedSpecification.or(ProductSpecification.matchMultiplePrice(min, max));
    // }
    // }
    // if(count == 0) return this.productRepository.findAll(page);
    // return this.productRepository.findAll(combinedSpecification, page);

    // }

    public Page<Product> getAllProductsWithSpecification(Pageable page, ProductCriteriaDTO productCriteriaDTO) {
        Specification<Product> combinedSpecification = Specification.where(null);

        // 1. Lọc theo factory
        if (productCriteriaDTO.getFactory().isPresent()) {
            combinedSpecification = combinedSpecification
                    .and(ProductSpecification.matchListFactory(productCriteriaDTO.getFactory().get()));
        }

        // 2. Lọc theo target
        if (productCriteriaDTO.getTarget().isPresent()) {
            combinedSpecification = combinedSpecification
                    .and(ProductSpecification.matchListTarget(productCriteriaDTO.getTarget().get()));
        }

        // 3. Lọc theo mức giá
        if (productCriteriaDTO.getPrice().isPresent()) {

            Specification<Product> priceSpec = (root, query, cb) -> cb.disjunction();
            int count = 0;
            for (String p : productCriteriaDTO.getPrice().get()) {
                double min = 0, max = 0;
                switch (p) {
                    case "duoi-10-trieu":
                        min = 0;
                        max = 10000000;
                        count++;
                        break;
                    case "10-toi-15-trieu":
                        min = 10000000;
                        max = 15000000;
                        count++;
                        break;
                    case "15-toi-20-trieu":
                        min = 15000000;
                        max = 20000000;
                        count++;
                        break;
                    case "tren-20-trieu":
                        min = 20000000;
                        max = 100000000;
                        count++;
                        break;
                }
                if (min != 0 && max != 0)
                    priceSpec = priceSpec.or(ProductSpecification.matchMultiplePrice(min, max));
            }
            if (count > 0)
                combinedSpecification = combinedSpecification.and(priceSpec);
        }

        // 4. Lọc theo sort
        if (productCriteriaDTO.getSort().isPresent()) {
            String sort = productCriteriaDTO.getSort().get();
            Pageable sortedPage;
            switch (sort) {
                case "gia-tang-dan":
                    sortedPage = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by("price").ascending());
                    break;
                case "gia-giam-dan":
                    sortedPage = PageRequest.of(page.getPageNumber(), page.getPageSize(),
                            Sort.by("price").descending());
                    break;
                default:
                    sortedPage = page;
            }
            return this.productRepository.findAll(combinedSpecification, sortedPage);
        }

        return this.productRepository.findAll(combinedSpecification, page);

    }

    public Product handleSaveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public Product getProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void handleDeleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    public void handleAddProductToCart(String email, long productId, HttpSession session) {
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            Cart cart = this.cartRepository.findByUser(user);

            if (cart == null) {
                // tạo mới cart
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setSum(0);

                cart = this.cartRepository.save(newCart);
            }

            Product product = this.productRepository.findById(productId);
            if (product != null) {

                CartDetail cartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product);

                if (cartDetail == null) {
                    CartDetail newCartDetail = new CartDetail();
                    newCartDetail.setQuantity(1);
                    newCartDetail.setProduct(product);
                    newCartDetail.setCart(cart);
                    newCartDetail.setPrice(product.getPrice());

                    this.cartDetailRepository.save(newCartDetail);

                    int s = cart.getSum() + 1;
                    cart.setSum(s);
                    this.cartRepository.save(cart);

                    session.setAttribute("sum", s);
                } else {
                    cartDetail.setQuantity(cartDetail.getQuantity() + 1);

                    this.cartDetailRepository.save(cartDetail);
                }

            }

        }
    }

    public Cart getCartByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public CartUpdateResponse handleUpdateCartDetailQuantity(String email, long cartDetailId, String action) {
        User user = this.userService.getUserByEmail(email);
        if (user == null) {
            return null;
        }

        Optional<CartDetail> optionalCartDetail = this.cartDetailRepository.findById(cartDetailId);
        if (!optionalCartDetail.isPresent()) {
            return null;
        }

        CartDetail cartDetail = optionalCartDetail.get();
        Cart cart = cartDetail.getCart();

        // Kiểm tra xem cart có thuộc về user hiện tại không
        if (cart.getUser().getId() != user.getId()) {
            return null;
        }

        long quantity = cartDetail.getQuantity();
        boolean deleted = false;

        if ("minus".equals(action)) {
            if (quantity > 1) {
                quantity--;
                cartDetail.setQuantity(quantity);
                this.cartDetailRepository.save(cartDetail);
            }
            // Nếu quantity = 1, bỏ qua không làm gì cả, giữ nguyên = 1
        } else if ("plus".equals(action)) {
            quantity++;
            cartDetail.setQuantity(quantity);
            this.cartDetailRepository.save(cartDetail);
        }

        double itemTotal = cartDetail.getPrice() * quantity;

        // Tính lại cart total
        double cartTotal = 0;
        List<CartDetail> cartDetails = cart.getCartDetails();
        if (cartDetails != null) {
            for (CartDetail cd : cartDetails) {
                cartTotal += cd.getPrice() * cd.getQuantity();
            }
        }

        return new CartUpdateResponse(true, quantity, itemTotal, cartTotal, deleted);
    }

    public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {
        Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(cartDetailId);
        if (cartDetailOptional.isPresent()) {
            CartDetail cartDetail = cartDetailOptional.get();

            Cart cart = cartDetail.getCart();

            this.cartDetailRepository.deleteById(cartDetailId);
            if (cart.getSum() > 1) {
                int sum = cart.getSum() - 1;
                cart.setSum(sum);
                this.cartRepository.save(cart);
                session.setAttribute("sum", sum);
            } else {
                this.cartRepository.deleteById(cart.getId());
                session.setAttribute("sum", 0);
            }
        }
    }

    public void handlePlaceOrder(User user, String receiverName, String receiverAddress, String receiverPhone,
            HttpSession session) {
        Order order = new Order();

        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverAddress(receiverAddress);
        order.setReceiverPhone(receiverPhone);
        order.setStatus("PENDING");

        order = this.orderRepository.save(order);

        double totalPrice = 0;

        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null) {
            for (CartDetail cartDetail : cart.getCartDetails()) {
                OrderDetail orderDetail = new OrderDetail();

                orderDetail.setOrder(order);
                orderDetail.setPrice(cartDetail.getPrice());
                orderDetail.setProduct(cartDetail.getProduct());
                orderDetail.setQuantity(cartDetail.getQuantity());
                this.orderDetailRepository.save(orderDetail);

                totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
            }
            for (CartDetail cartDetail : cart.getCartDetails()) {
                this.cartDetailRepository.deleteById(cartDetail.getId());
            }
            this.cartRepository.deleteById(cart.getId());
            session.setAttribute("sum", 0);
        }
        order.setTotalPrice(totalPrice);
        this.orderRepository.save(order);

    }
}
