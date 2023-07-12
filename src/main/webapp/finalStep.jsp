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
    <script>
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
        function sendNextPage(tipoPedido){
            $.ajax({
                type: "POST",
                url: "discenteController",
                data: {
                    buttonPedido: 'pedido',
                    tipoPedido: tipoPedido
                },
                dataType: "json",
                success: function (data){
                    if('NOVO_PEDIDO_FIM' === data.page){
                        window.location.replace("finalStep.jsp");
                    }
                }
            });
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
        function finishPedido(){
            $.ajax({
                type: "POST",
                url: "discenteController",
                async: false,
                data: {
                    submitButtonDiscenteFinalizar: 'finalizarPedido'
                },
                dataType: "json",
                success: function (data){
                    if (data.finished){
                        alert("Pedido finalizado com sucesso!");
                        window.location.replace("discente.jsp");
                    }
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
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="discente.jsp">
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

        <!-- Nav Item - Novo estágio Collapse Menu -->
        <li class="nav-item">
            <c:if test="${RENOVACAO_ESTAGIO == null}">
                <div class="d-flex">
                    <a class="nav-link collapsed btn" onclick="sendNextPage('NOVO')">
                        <i class="fas fa-fw bi bi-clipboard-fill"></i>
                        <span>Novo Estágio</span>
                    </a>
                    <c:if test="${NOVO_ESTAGIO != null}">
                        <a type="btn" style="padding-right: 15px; padding-top:17px;" href="#" data-toggle="modal" data-target="#finishModal">
                            <img src="https://img.icons8.com/fluency/48/cancel.png" alt="cross-mark-emoji" width="16" height="16">
                        </a>
                    </c:if>
                </div>
            </c:if>
        </li>

        <!-- Nav Item - Renovação de estágio Collapse Menu -->
        <li class="nav-item">
            <c:if test="${NOVO_ESTAGIO == null}">
                <div class="d-flex">
                    <a class="nav-link collapsed btn" onclick="sendNextPage('RENOVACAO')">
                        <i class="fas fa-fw bi bi-clipboard-plus-fill"></i>
                        <span>Renovação de Estágio</span>
                    </a>
                    <c:if test="${RENOVACAO_ESTAGIO != null}">
                        <a type="btn" style="padding-right: 15px; padding-top:17px;" href="#" data-toggle="modal" data-target="#finishModal">
                            <img src="https://img.icons8.com/fluency/48/cancel.png" alt="cross-mark-emoji" width="16" height="16">
                        </a>
                    </c:if>
                </div>
            </c:if>
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
                            <span class="mr-2 d-none d-lg-inline text-gray-600 small"><c:out value="${DISCENTE.nome}"></c:out></span>
                            <img class="img-profile rounded-circle"
                                 src="assets/img/icon_profile.png">
                        </a>
                        <!-- Dropdown - User Information -->
                        <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                             aria-labelledby="userDropdown">
                            <a class="dropdown-item" href="editarPerfilDiscente.jsp">
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
                <h1 class="h3 mb-4 text-gray-800">Criar Pedido na DEST / Finalizar Pedido</h1>

            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <c:if test="${'NOVO' == NOVO_ESTAGIO.tipo.name()}">
                                <div class="form-floating mb-3">
                                    <label for="planoAtividadesAntigo">Download do Plano de Atividades Assinado:</label>
                                    <a id="planoAtividadesAntigo" href="${PLANO_ATIVIDADES_URL}">${PLANO_ATIVIDADES.nome}</a>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="tceAntigo">Download do TCE Assinado:</label>
                                    <a id="tceAntigo" href="${TCE_URL}">${TCE.nome}</a>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="proximosPassos">Proximos Passos:</label>
                                    <p class="form-control" id="proximosPassos" readonly>Realize o Download do Plano de Atividades Assinado e do TCE Assinado para ser Anexado no Formulário Final da DEST</p>
                                </div>
                            </c:if>
                            <c:if test="${'RENOVACAO' == RENOVACAO_ESTAGIO.tipo.name()}">
                                <div class="form-floating mb-3">
                                    <label for="termoAditivoAntigo">Download do Termo Aditivo Assinado:</label>
                                    <a id="termoAditivoAntigo" href="${TERMO_ADITIVO_URL}">${TERMO_ADITIVO.nome}</a>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="proximosPassosAditivo">Proximos Passos:</label>
                                    <p class="form-control" id="proximosPassosAditivo" readonly>Realize o Download do Termo Aditivo Assinado para ser Anexado no Formulário Final da DEST</p>
                                </div>
                            </c:if>
                            <div role="toolbar" style="text-align: right">
                                <button class="btn btn-primary" href="#" data-toggle="modal" data-target="#finishModal">Finalizar Pedido</button>
                                <a href="https://institucional.ufrrj.br/dest/formularios-tce/" target="_blank" class="btn btn-primary">Ir para DEST</a>
                            </div>
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

<!-- Finish Modal-->
<div class="modal fade" id="finishModal" tabindex="-1" role="dialog" aria-labelledby="finishModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="finishModalLabel">Finalizar pedido?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Selecione "Finalizar" abaixo se você realmente deseja finalizar este pedido.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Não</button>
                <button class="btn btn-primary" onclick="finishPedido()">Finalizar</button>
            </div>
        </div>
    </div>
</div>
<!-- Custom scripts for all pages-->
<script src="js/sb-admin-2.min.js"></script>
</body>

</html>