package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProductPage(Model model, 
        @RequestParam("page") Optional<String> pageOptional) {
        
        int page = 1;
        try {
            if(pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        Pageable pageable = PageRequest.of(page - 1, 4);
        Page<Product> pageProduct = this.productService.getAllProducts(pageable);
        List<Product> products = pageProduct.getContent();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", pageProduct.getTotalPages());

        return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createProductPage(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
            @RequestParam("imageProduct") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "admin/product/create";
        }
        String imgName = this.uploadService.handleSaveUploadFile(file, "product");

        product.setImage(imgName);
        this.productService.handleSaveProduct(product);


        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetail(@PathVariable("id") long id, Model model) {
        model.addAttribute("product", this.productService.getProductById(id));
        return "admin/product/detail";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getMethodName(Model model, @PathVariable("id") long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);

        return "admin/product/update";
    }

    @PostMapping("/admin/product/update/{id}")
    public String postUpdateProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
            @RequestParam("imageProduct") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "admin/product/update";
        }

        Product currentProduct = this.productService.getProductById(product.getId());
        if (currentProduct != null) {
            if (!file.isEmpty()) {
                String imgName = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(imgName);
            }
            currentProduct.setName(product.getName());
            currentProduct.setPrice(product.getPrice());
            currentProduct.setDetailDesc(product.getDetailDesc());
            currentProduct.setShortDesc(product.getShortDesc());
            currentProduct.setQuantity(product.getQuantity());
            currentProduct.setFactory(product.getFactory());
            currentProduct.setTarget(product.getTarget());
            this.productService.handleSaveProduct(currentProduct);
        }
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPafe(@PathVariable("id") long id, Model model ) {
        model.addAttribute("product", this.productService.getProductById(id));
        return "admin/product/delete";
    }
    
    @PostMapping("/admin/product/delete")
    public String postDeleteproduct(@ModelAttribute("product") Product product) {
        this.productService.handleDeleteProduct(product.getId());
        return "redirect:/admin/product";
    }
    
    
}
