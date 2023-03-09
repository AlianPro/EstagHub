<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt">

<head>

    <title>EstagHub</title>

    <!-- Custom fonts for this template-->
    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
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

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin-2.min.js"></script>
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
        function sendNextPage(idPedido, statusPedido){
            $.ajax({
                type: "POST",
                url: "docenteController",
                data: {
                    buttonPedido: 'pedido',
                    idPedido: idPedido,
                    statusPedido: statusPedido
                },
                success: function (){
                    return true;
                }
            });
        }
    </script>
</head>

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- Sidebar - Brand -->
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="docenteComissao.jsp">
            <div class="sidebar-brand-icon rotate-n-15">
                <i class="fas fa-laugh-wink"></i>
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
            <a class="nav-link collapsed" href="novoEstagio.jsp">
                <i class="fas fa-fw fa-cog"></i>
                <span>Pedidos</span>
            </a>
        </li>

        <!-- Nav Item - Criar Docente Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="criarDocente.jsp">
                <i class="fas fa-fw fa-wrench"></i>
                <span>Criar Docente</span>
            </a>
        </li>

        <!-- Nav Item - Criar Curso Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="criarCurso.jsp">
                <i class="fas fa-fw fa-wrench"></i>
                <span>Criar Curso</span>
            </a>
        </li>
        <!-- Nav Item - Criar Departamento Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="criarDepartamento.jsp">
                <i class="fas fa-fw fa-wrench"></i>
                <span>Criar Departamento</span>
            </a>
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
                            <span class="mr-2 d-none d-lg-inline text-gray-600 small"><c:out value="${DOCENTE.nome}"></c:out></span>
                            <img class="img-profile rounded-circle"
                                 src="assets/img/undraw_profile.svg">
                        </a>
                        <!-- Dropdown - User Information -->
                        <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                             aria-labelledby="userDropdown">
                            <a class="dropdown-item" href="#">
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
                            <form class="needs-validation" novalidate accept-charset="utf-8" id="docenteForm" name="docenteForm" action="docenteController" method="post">
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
                                <div class="form-floating mb-3" id="selectDocente">
                                    <select name="selectDocenteOrientador" class="form-select" id="selectDocenteOrientador" required>
                                        <option value="">Docente Orientador</option>
                                        <c:forEach var="docente" items="${DOCENTES_NAO_COMISSAO}">
                                            <option value="${docente.id}">${docente.nome}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Escolha um Docente Orientador.
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
                <button id="buttonLogoutDocenteComissao" name="buttonLogoutDocenteComissao" type="submit" value="logout" class="btn btn-primary" onclick="logoutDocenteComissao()">Logout</button>
            </div>
        </div>
    </div>
</div>

</body>

</html>