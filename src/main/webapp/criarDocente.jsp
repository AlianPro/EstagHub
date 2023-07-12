<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt">

<head>

    <title>EstagHub</title>

    <!-- Custom fonts for this template-->
    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link rel="icon" type="image/x-icon" href="assets/img/rural_logo_branca.png"/>
    <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">
    <!-- Custom styles for this template-->
    <link href="css/sb-admin-2.css" rel="stylesheet">
    <!-- Bootstrap core JavaScript-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>
    <!-- Bootstrap icons-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.2/font/bootstrap-icons.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script>
        $(document).ready(function (){
            $('#selectDepartamento').select2();
        });
        function logout(){
            $.ajax({
                type: "POST",
                url: "principalController",
                async: false,
                data: {
                    buttonLogout: 'logout'
                }
            });
        }
        function onCreateDocente(){
            let formCreateDocente = document.getElementById('docenteForm');
            if(formCreateDocente) {
                if (!formCreateDocente.checkValidity()) {
                    formCreateDocente.classList.add('was-validated');
                    return false;
                }
            }
            $.ajax({
                type: "POST",
                url: "docenteComissaoController",
                cache: false,
                data: {
                    submitButtonDocenteCadastroNovoDocente: $('button[id^=submitButtonDocenteCadastroNovoDocente]').val(),
                    nomeDocente: $('input[id^=nomeDocente]').val(),
                    emailDocente: $('input[id^=emailDocente]').val(),
                    senhaDocente: $('input[id^=senhaDocente]').val(),
                    siapeDocente: $('input[id^=siapeDocente]').val(),
                    radioComissao: $('input[name^=radioComissao]:checked').val(),
                    selectDepartamento: $('select[id^=selectDepartamento]').val()
                },
                dataType: "json",
                success: function (data) {
                    if (data.criarDocente) {
                        alert('Docente criado com sucesso!');
                        window.location.replace('criarDocente.jsp');
                    }else{
                        alert(data.message);
                    }
                },
                error: function (xhr, status, error) {
                    alert('Erro em processar os dados!');
                }
            });
        }
    </script>
    <style>
        .select2-container .select2-selection--single {
            height: calc(2.25rem + 2px);
            padding: 0.4rem 2.25rem 0 0.75rem;
            font-size: 1rem;
            font-weight: 500;
            line-height: 1.25;
            color: #212529;
            background-color: #fff;
            border: 1px solid #ced4da;
            border-radius: .25rem;
            transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
            -moz-padding-start: calc(0.75rem - 3px);
            width: 12rem;
        }
        .select2-container--open .select2-dropdown{
            width: 12rem !important;
        }
        .select2-container .select2-selection--single:focus {
            border-color: #ced4da;
            outline: 0;
            box-shadow: 0 0 0 .25rem rgba(206, 212, 218, 1);
        }
        .select2-container .select2-selection--single .select2-selection__arrow {
            height: 100%;
            left: 10rem;
            top: auto;
            bottom: 0;
            border-color: #ced4da;
        }
        .select2-container--default .select2-results__option--highlighted.select2-results__option--selectable{
            background-color:#2b36f0 !important;
        }
    </style>
</head>

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- Sidebar - Brand -->
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="docenteComissao.jsp">
            <div class="sidebar-brand-icon rotate-n-15">
                <img class="img-fluid" src="assets/img/rural_logo_branca.png"/>
            </div>
            <div class="sidebar-brand-text mx-3">EstagHub</div>
        </a>

        <!-- Divider -->
        <hr class="sidebar-divider my-0">

        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">
            Ações
        </div>

        <!-- Nav Item - Pedidos Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="pedidosDocenteComissao.jsp">
                <i class="fas fa-fw bi bi-stack"></i>
                <span>Pedidos</span>
            </a>
        </li>
        <!-- Nav Item - Gerenciar Docente Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapsePagesDocente" aria-expanded="true" aria-controls="collapsePages">
                <i class="fas fa-fw bi bi-person-fill-gear"></i>
                <span>Gerenciar Docente</span>
            </a>
            <div id="collapsePagesDocente" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar" style="">
                <div class="bg-light py-2 collapse-inner rounded">
                    <a class="collapse-item" href="criarDocente.jsp">Criar Docente</a>
                    <a class="collapse-item" href="editarDocente.jsp">Editar Docente</a>
                </div>
            </div>
        </li>
        <!-- Nav Item - Gerenciar Curso Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapsePagesCurso" aria-expanded="true" aria-controls="collapsePages">
                <i class="fas fa-fw bi bi-mortarboard-fill"></i>
                <span>Gerenciar Curso</span>
            </a>
            <div id="collapsePagesCurso" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar" style="">
                <div class="bg-light py-2 collapse-inner rounded">
                    <a class="collapse-item" href="criarCurso.jsp">Criar Curso</a>
                    <a class="collapse-item" href="editarCurso.jsp">Editar Curso</a>
                </div>
            </div>
        </li>
        <!-- Nav Item - Gerenciar Departamento Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapsePagesDepartamento" aria-expanded="true" aria-controls="collapsePages">
                <i class="fas fa-fw bi bi-building-fill-gear"></i>
                <span>Gerenciar Departamento</span>
            </a>
            <div id="collapsePagesDepartamento" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar" style="">
                <div class="bg-light py-2 collapse-inner rounded">
                    <a class="collapse-item" href="criarDepartamento.jsp">Criar Departamento</a>
                    <a class="collapse-item" href="editarDepartamento.jsp">Editar Departamento</a>
                </div>
            </div>
        </li>
        <!-- Divider -->
        <hr class="sidebar-divider d-none d-md-block">

        <!-- Sidebar Toggler (Sidebar) -->
        <div class="text-center d-none d-md-inline">
            <button class="rounded-circle border-0" id="sidebarToggle"></button>
        </div>

    </ul>
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                <!-- Sidebar Toggle (Topbar) -->
                <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                    <i class="fa fa-bars"></i>
                </button>

                <!-- Topbar Navbar -->
                <ul class="navbar-nav ml-auto">

                    <!-- Nav Item - User Information -->
                    <li class="nav-item dropdown no-arrow">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="mr-2 d-none d-lg-inline text-gray-600 small"><c:out value="${DOCENTE_COMISSAO.nome}"></c:out></span>
                            <img class="img-profile rounded-circle"
                                 src="assets/img/icon_profile.png">
                        </a>
                        <!-- Dropdown - User Information -->
                        <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                             aria-labelledby="userDropdown">
                            <a class="dropdown-item" href="editarPerfilDocenteComissao.jsp">
                                <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                                Profile
                            </a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                                <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                Logout
                            </a>
                        </div>
                    </li>

                </ul>

            </nav>
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <h1 class="h3 mb-4 text-gray-800">Criar Docente</h1>

            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <form class="needs-validation" novalidate id="docenteForm">
                                <div class="form-floating mb-3">
                                    <label for="nomeDocente">Nome</label>
                                    <input class="form-control" id="nomeDocente" type="text" name="nomeDocente" required placeholder="Nome Completo"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe um Nome Completo.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="emailDocente">Email</label>
                                    <input class="form-control" id="emailDocente" type="email" name="emailDocente" required placeholder="name@example.com"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe um Email.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="senhaDocente">Senha</label>
                                    <input class="form-control" id="senhaDocente" type="password" name="senhaDocente" required placeholder="Digite sua Senha"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe uma Senha.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="siapeDocente">Siape</label>
                                    <input class="form-control" id="siapeDocente" type="text" name="siapeDocente" required placeholder="Digite sua Matrícula SIAPE"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe a Matrícula SIAPE.
                                    </div>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="radioComissao" id="docenteComisao" value="comissao" required>
                                    <label class="form-check-label" for="docenteComisao">Docente Comissão</label>
                                </div>
                                <div class="form-check form-check-inline mb-3">
                                    <input class="form-check-input" type="radio" name="radioComissao" id="docente" value="simples" required>
                                    <label class="form-check-label" for="docente">Docente Simples</label>
                                </div>
                                <div class="form-floating mb-3">
                                    <select name="selectDepartamento" class="form-select" id="selectDepartamento" required>
                                        <option value="">Departamento</option>
                                        <c:forEach var="departamento" items="${LIST_DEPARTAMENTOS_ATIVOS}">
                                            <option value="${departamento.id}">${departamento.sigla} - ${departamento.nome}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Escolha um Departamento.
                                    </div>
                                </div>
                                <div role="toolbar" style="text-align: right">
                                    <button class="btn btn-primary" type="button" id="submitButtonDocenteCadastroNovoDocente" name="submitButtonDocenteCadastroNovoDocente" value="novoDocente" onclick="onCreateDocente()">Enviar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- End of Main Content -->
        <!-- Footer -->
        <footer class="sticky-footer bg-white">
            <div class="container my-auto">
                <div class="copyright text-center my-auto">
                    <span>Copyright &copy; EstagHub 2023</span>
                </div>
            </div>
        </footer>
        <!-- End of Footer -->
    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- Logout Modal-->
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Pronto para sair?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Selecione "Logout" abaixo se você está pronto para terminar essa sessão.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Não</button>
                <a href="index.jsp" id="buttonLogout" type="submit" class="btn btn-primary" onclick="logout()">Logout</a>
            </div>
        </div>
    </div>
</div>
<!-- Custom scripts for all pages-->
<script src="js/sb-admin-2.min.js"></script>
</body>

</html>