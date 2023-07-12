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
            let formLogin = document.getElementById('docenteForm');
            if(formLogin) {
                const forms = document.querySelectorAll('.needs-validation');
                Array.prototype.slice.call(forms).forEach((formLogin) => {
                    formLogin.addEventListener('submit', (event) => {
                        if (!formLogin.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }else{
                        }
                        formLogin.classList.add('was-validated');
                    }, false);
                });
            }
            $('#selectDocenteOrientador').select2();
        });
        function showTextArea(){
            $("#textAreaDiv").children().prop('disabled',false);
            $("#textAreaDiv").prop('hidden',false);
            $("#selectDocente").children().prop('disabled',true);
            $("#selectDocente").prop('hidden',true);
        }
        function hideTextArea(){
            $("#textAreaDiv").children().prop('disabled',true);
            $("#textAreaDiv").prop('hidden',true);
            $("#selectDocente").children().prop('disabled',false);
            $("#selectDocente").prop('hidden',false);
        }
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
            width: 15rem;
        }
        .select2-container .select2-selection--single:focus {
            border-color: #ced4da;
            outline: 0;
            box-shadow: 0 0 0 .25rem rgba(206, 212, 218, 1);
        }
        .select2-container .select2-selection--single .select2-selection__arrow {
            height: 100%;
            left: 13rem;
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
                <h1 class="h3 mb-4 text-gray-800">Analisar Pedido</h1>

            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <div class="form-floating mb-3">
                                <label for="justificativaDiscente">Justificativa sobre o(s) Requisito(s) não Cumprido(s)</label>
                                <p class="form-control" id="justificativaDiscente" readonly>${PEDIDO.justificativaDiscente}</p>
                            </div>
                            <form class="needs-validation" novalidate accept-charset="utf-8" id="docenteForm" name="docenteForm" action="docenteComissaoController" method="post">
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="radioPedido" id="aceitarPedido" value="aceito" required onclick="hideTextArea()">
                                    <label class="form-check-label" for="aceitarPedido">Aceitar</label>
                                </div>
                                <div class="form-check form-check-inline mb-3">
                                    <input class="form-check-input" type="radio" name="radioPedido" id="rejeitarPedido" value="rejeitado" required onclick="showTextArea()">
                                    <label class="form-check-label" for="rejeitarPedido">Rejeitar</label>
                                </div>
                                <div class="form-outline mb-3" id="textAreaDiv" hidden>
                                    <label class="form-label" for="textAreaPedido">Justificativa</label>
                                    <textarea class="form-control" id="textAreaPedido" name="textAreaPedido" required></textarea>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe uma justificativa para rejeitar esse recurso.
                                    </div>
                                </div>
                                <div class="form-floating mb-3" id="selectDocente" hidden>
                                    <select name="selectDocenteOrientador" class="form-select" id="selectDocenteOrientador" required>
                                        <option value="">Docente Orientador(a)</option>
                                        <c:forEach var="docente" items="${DOCENTES_NAO_COMISSAO}">
                                            <option value="${docente.id}">${docente.nome}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Escolha um(a) Docente Orientador(a).
                                    </div>
                                </div>
                                <div role="toolbar" style="text-align: right">
                                    <button class="btn btn-primary" type="submit" id="submitButtonDocenteAnalisaRecursoNovoPedido2" name="submitButtonDocenteAnalisaRecursoNovoPedido2" value="step2">Enviar</button>
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