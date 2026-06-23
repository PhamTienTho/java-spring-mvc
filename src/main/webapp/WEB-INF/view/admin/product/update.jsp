<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <title>Trang quản trị</title>
                <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script>
                    $(document).ready(() => {
                        const imageProduct = $("#imageProduct");
                        imageProduct.change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#productPreview").attr("src", imgURL);
                            $("#productPreview").css({ "display": "block" });
                        });
                    })
                </script>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Products</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item active">Dashboard</li>
                                    <li class="breadcrumb-item active">Products</li>
                                </ol>
                                <a href="/admin/product" class="btn btn-primary mt-3">Back</a>
                                <div>
                                    <div class="mt-5">
                                        <div class="row">
                                            <div class="col-md-6 col-12 mx-auto">
                                                <h3>Create a product</h3>
                                                <hr />
                                                <form:form method="post" action="/admin/product/update/${product.id}"
                                                    modelAttribute="product" enctype="multipart/form-data">
                                                    <form:hidden path="id" />
                                                    <div class="row">
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Name:</label>
                                                            <form:input cssClass="form-control" cssErrorClass="form-control is-invalid" path="name" />
                                                            <form:errors path="name" cssClass="invalid-feedback" />
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Price:</label>
                                                            <form:input type="number" cssClass="form-control" cssErrorClass="form-control is-invalid" path="price" />
                                                            <form:errors path="price" cssClass="invalid-feedback" />
                                                        </div>
                                                        <div class="mb-3 col-12">
                                                            <label class="form-label">Detail description:</label>
                                                            <form:textarea cssClass="form-control" cssErrorClass="form-control is-invalid" path="detailDesc" />
                                                            <form:errors path="detailDesc" cssClass="invalid-feedback" />
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Short description</label>
                                                            <form:input cssClass="form-control" cssErrorClass="form-control is-invalid" path="shortDesc" />
                                                            <form:errors path="shortDesc" cssClass="invalid-feedback" />
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Quantity:</label>
                                                            <form:input type="number" cssClass="form-control" cssErrorClass="form-control is-invalid" path="quantity" />
                                                            <form:errors path="quantity" cssClass="invalid-feedback" />

                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Factory:</label>
                                                            <form:select class="form-select" path="factory">
                                                                <form:option value="Apple(MacBook)">Apple(MacBook)</form:option>
                                                                <form:option value="Asus">Asus</form:option>
                                                                <form:option value="Dell">Dell</form:option>
                                                                <form:option value="HP">HP</form:option>
                                                                <form:option value="Lenovo">Lenovo</form:option>
                                                                <form:option value="MSI">MSI</form:option>
                                                            </form:select>
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Target:</label>
                                                            <form:select class="form-select" path="target">
                                                                <form:option value="Gaming">Gaming</form:option>
                                                                <form:option value="Student">Student</form:option>
                                                                <form:option value="Business">Business</form:option>
                                                                <form:option value="Workstation">Workstation</form:option>
                                                            </form:select>
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label for="imageProduct" class="form-label">Image:</label>
                                                            <input class="form-control" type="file" id="imageProduct"
                                                                accept=".png, .jpg, .jpeg" name="imageProduct" />
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <img style="max-height: 250px; border-radius: 20%; border: 3px solid #4e73df; padding: 3px; box-shadow: 0 0 10px rgba(78,115,223,0.4); object-fit: cover; width: 150px; height: 150px;"
                                                                alt="product preview" id="productPreview"
                                                                src="/images/product/${product.image}">
                                                        </div>
                                                    </div>
                                                    <button type="submit" class="btn btn-warning">Update</button>
                                                </form:form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/scripts.js"></script>
            </body>

            </html>