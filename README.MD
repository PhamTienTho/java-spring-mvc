# LaptopShop - Website Thương Mại Điện Tử Bán Laptop

Dự án website bán laptop được xây dựng theo mô hình MVC với Spring Boot, bao gồm đầy đủ các chức năng cho cả người dùng lẫn quản trị viên.

---

## Công nghệ sử dụng

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Java 17 |
| Framework | Spring Boot 3.2.2 |
| Web Layer | Spring MVC |
| ORM | Spring Data JPA + Hibernate 6 |
| Bảo mật | Spring Security |
| Quản lý Session | Spring Session JDBC |
| Database | MySQL |
| View Engine | JSP + JSTL |
| Build Tool | Maven |
| CSS Framework | Bootstrap 5 |
| Dev Tool | Spring Boot DevTools |

---

## Chức năng

### Phía người dùng (Client)

- **Xác thực**: Đăng ký, đăng nhập, đăng xuất, Remember Me
- **Trang sản phẩm**: Xem danh sách sản phẩm, xem chi tiết sản phẩm
- **Lọc & Tìm kiếm**:
  - Lọc theo hãng sản xuất (Apple, Asus, Dell, Lenovo,...)
  - Lọc theo mục đích sử dụng (Gaming, Sinh viên, Thiết kế đồ họa,...)
  - Lọc theo khoảng giá
  - Sắp xếp theo giá tăng/giảm dần
  - Phân trang — giữ nguyên bộ lọc khi chuyển trang
- **Giỏ hàng**: Thêm sản phẩm, cập nhật số lượng, xóa sản phẩm
- **Đặt hàng**: Checkout, nhập thông tin giao hàng
- **Lịch sử đơn hàng**: Xem danh sách đơn đã đặt

### Phía quản trị (Admin)

- **Dashboard**: Tổng quan thống kê
- **Quản lý người dùng**: Xem danh sách, thêm, sửa, xóa người dùng; phân quyền ADMIN/USER
- **Quản lý sản phẩm**: Thêm, sửa, xóa sản phẩm; upload ảnh sản phẩm; phân trang
- **Quản lý đơn hàng**: Xem danh sách, cập nhật trạng thái đơn hàng

---

## Kỹ thuật áp dụng

- **MVC Pattern** — tách biệt rõ ràng Controller, Service, Repository
- **Spring Data JPA Specification** — xây dựng bộ lọc động (dynamic query) kết hợp nhiều tiêu chí (AND/OR)
- **JPA Metamodel** (`Product_`) — type-safe query với Criteria API
- **DTO Pattern** — `ProductCriteriaDTO` để nhận tham số lọc từ request
- **Phân trang** — `Pageable` / `PageRequest` với giữ nguyên query string khi chuyển trang
- **Spring Security**:
  - Mã hóa mật khẩu BCrypt
  - Phân quyền theo role (`ROLE_ADMIN`, `ROLE_USER`)
  - Custom `AuthenticationSuccessHandler` — redirect theo role sau đăng nhập
  - CSRF protection
  - Remember Me với Spring Session
- **Spring Session JDBC** — lưu session xuống database, hỗ trợ Remember Me bền vững
- **File Upload** — upload và lưu ảnh sản phẩm / avatar (tối đa 50MB)
- **Bean Validation** — validate dữ liệu form phía server
- **RESTful endpoint** — API JSON (`@ResponseBody`) cho tính năng cập nhật giỏ hàng (AJAX)

---

## Yêu cầu môi trường

- Java 17+
- MySQL 8+
- Maven 3.8+

## Cấu hình & Chạy dự án

1. Tạo database MySQL tên `laptopshop`
2. Chỉnh thông tin kết nối trong `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/laptopshop
   spring.datasource.username=root
   spring.datasource.password=123456
   ```
3. Chạy ứng dụng:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Truy cập: `http://localhost:8081`
