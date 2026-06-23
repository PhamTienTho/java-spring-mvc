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
                        const avatarFile = $("#avatarFile");
                        avatarFile.change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#avatarPreview").attr("src", imgURL);
                            $("#avatarPreview").css({ "display": "block" });
                        });
                    });
                </script>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Manage Users</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                                </ol>
                                <div>
                                    <div class="mt-5">
                                        <div class="row">
                                            <div class="col-md-6 col-12 mx-auto">
                                                <h3>Create a user</h3>
                                                <hr />
                                                <form:form method="post" action="/admin/user/create" modelAttribute="user"
                                                enctype="multipart/form-data">
                                                    <div class="row">
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Email address</label>
                                                            <form:input type="email" cssClass="form-control" cssErrorClass="form-control is-invalid"  path="email"/>
                                                            <form:errors path="email" cssClass="invalid-feedback" />
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Password:</label>
                                                            <form:input type="password" cssClass="form-control" cssErrorClass="form-control is-invalid" path="password"/>
                                                            <form:errors path="password" cssClass="invalid-feedback" />
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Full Name:</label>
                                                            <form:input cssClass="form-control" cssErrorClass="form-control is-invalid" path="fullName"/>
                                                            <form:errors path="fullName" cssClass="invalid-feedback" />
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Phone number:</label>
                                                            <form:input type="number" class="form-control" path="phone"/>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Address:</label>
                                                            <form:input class="form-control" path="address"/>
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label class="form-label">Role:</label>
                                                            <form:select class="form-select" path="role.name">
                                                                <form:option value="ADMIN">ADMIN</form:option>
                                                                <form:option value="USER">USER</form:option>
                                                            </form:select>
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <label for="avatarFile" class="form-label">Avatar:</label>
                                                            <input class="form-control" type="file" id="avatarFile"
                                                                accept=".png, .jpg, .jpeg" name="avatarFile" />
                                                        </div>
                                                        <div class="mb-3 col-12 col-md-6">
                                                            <img style="max-height: 250px; display: none; border-radius: 20%; border: 3px solid #4e73df; padding: 3px; box-shadow: 0 0 10px rgba(78,115,223,0.4); object-fit: cover; width: 150px; height: 150px;"
                                                                alt="avatar preview" id="avatarPreview">
                                                        </div>
                                                    </div>
                                                    <button type="submit" class="btn btn-primary">Create</button>
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