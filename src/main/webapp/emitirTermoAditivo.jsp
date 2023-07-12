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
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.3/dist/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js" integrity="sha512-T/tUfKSV1bihCnd+MxKD0Hm1uBBroVYBOYSk1knyvQ9VyZJpc/ALb4P0r6ubwVPSGB2GvjeoMAJJImBG12TiaQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/locales/bootstrap-datepicker.pt-BR.min.js" integrity="sha512-mVkLPLQVfOWLRlC2ZJuyX5+0XrTlbW2cyAwyqgPkLGxhoaHNSWesYMlcUjX8X+k45YB8q90s88O7sos86636NQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" integrity="sha512-mSYUmp1HYZDFaVKK//63EcZq4iFWFjxSL+Z3T/aCt4IO9Cejm03q3NKKYN6pFQzY0SBOr8h+eCIAZHPXcpZaNw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>
    <!-- Bootstrap icons-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.2/font/bootstrap-icons.css" rel="stylesheet" />
    <script>
        $(document).ready(function (){
            let formLogin = document.getElementById('discenteForm');
            if(formLogin) {
                const forms = document.querySelectorAll('.needs-validation');
                Array.prototype.slice.call(forms).forEach((formLogin) => {
                    formLogin.addEventListener('submit', (event) => {
                        if (!formLogin.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        formLogin.classList.add('was-validated');
                    }, false);
                });
            }
            let formTermoAditivoAssinado = document.getElementById('termoAditivoAssinado');
            if(formTermoAditivoAssinado) {
                const forms = document.querySelectorAll('.needs-validation');
                Array.prototype.slice.call(forms).forEach((formTermoAditivoAssinado) => {
                    formTermoAditivoAssinado.addEventListener('submit', (event) => {
                        if (!formTermoAditivoAssinado.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        formTermoAditivoAssinado.classList.add('was-validated');
                    }, false);
                });
            }
            $('input[id^=dataInicioAditivo], input[id^=dataFimAditivo]').datepicker({
                format: "dd/mm/yyyy",
                language: "pt-BR",
                autoclose: true,
                todayHighlight: true
            });
        });
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
                    if('RENOVACAO_STEP4' === data.page){
                        window.location.replace("emitirTermoAditivo.jsp");
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
        function hideTextAreaTermoAditivo(option){
            if ('empresa' === option || 'branco' === option){
                $("#discenteForm").children().prop('disabled',true);
                $("#discenteForm").prop('hidden',true);
                $("#termoAditivoAssinado").children().prop('disabled',false);
                $("#termoAditivoAssinado").prop('hidden',false);
            }else {
                $("#discenteForm").children().prop('disabled',false);
                $("#discenteForm").prop('hidden',false);
                $("#termoAditivoAssinado").children().prop('disabled',true);
                $("#termoAditivoAssinado").prop('hidden',true);
            }
            if ('empresa' === option || 'gerar' === option){
                $("#modeloTermoAditivoUFRRJ").children().prop('disabled',true);
                $("#modeloTermoAditivoUFRRJ").prop('hidden',true);
            }else {
                $("#modeloTermoAditivoUFRRJ").children().prop('disabled',false);
                $("#modeloTermoAditivoUFRRJ").prop('hidden',false);
            }
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
                <h1 class="h3 mb-4 text-gray-800">Emitir Termo Aditivo</h1>
            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <div class="form-outline mb-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="radioTermoAditivo" id="termoAditivoEmpresa" required onclick="hideTextAreaTermoAditivo('empresa')">
                                    <label class="form-check-label" for="termoAditivoEmpresa">Enviar Termo Aditivo Usando o Modelo da Empresa</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="radioTermoAditivo" id="termoAditivoBranco" required onclick="hideTextAreaTermoAditivo('branco')">
                                    <label class="form-check-label" for="termoAditivoBranco">Enviar Termo Aditivo Usando o Modelo da UFRRJ em Branco</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="radioTermoAditivo" id="termoAditivoGerar" required onclick="hideTextAreaTermoAditivo('gerar')">
                                    <label class="form-check-label" for="termoAditivoGerar">Gerar Termo Aditivo</label>
                                </div>
                            </div>
                            <div class="form-floating mb-3" id="modeloTermoAditivoUFRRJ" hidden>
                                <label for="termoAditivoDiscente">Visualizar o Modelo de Termo Aditivo da UFRRJ:</label>
                                <a id="termoAditivoDiscente" href="${TERMO_ADITIVO_MODELO_UFRRJ_URL}">${TERMO_ADITIVO_MODELO_UFRRJ}</a>
                            </div>
                            <form class="needs-validation" enctype="multipart/form-data" novalidate id="termoAditivoAssinado" name="discenteForm" action="discenteController" method="post" hidden>
                                <div class="form-floating mb-3">
                                    <label for="fileTCEAssinado">Anexe o Termo Aditivo Preenchido e Assinado:</label>
                                    <input id="fileTCEAssinado" name="fileTermoAditivoAssinado" required type="file"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Anexe o Termo Aditivo Preenchido e Assinado.
                                    </div>
                                </div>
                                <div role="toolbar" style="text-align: right">
                                    <button class="btn btn-primary" type="submit" id="submitButtonDiscenteTermoAditivoAnexo" name="submitButtonDiscenteTermoAditivo" value="termoAditivo">Enviar</button>
                                </div>
                            </form>
                            <form class="needs-validation" enctype="multipart/form-data" novalidate id="discenteForm" name="discenteForm" action="discenteController" method="post" hidden>
                                <div class="form-floating mb-3">
                                    <label for="dataInicioAditivo">Data Antiga para Fim de Estágio</label>
                                    <input class="form-control" id="dataInicioAditivo" type="text" name="dataInicioAditivo" required placeholder="dd/mm/yyyy"/>
                                    <div class="input-group-addon" >
                                        <span class="glyphicon glyphicon-th"></span>
                                    </div>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe a Data Antiga para o Fim do Estágio.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="dataFimAditivo">Data Nova para Fim de Estágio</label>
                                    <input class="form-control" id="dataFimAditivo" type="text" name="dataFimAditivo" required placeholder="dd/mm/yyyy"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe a nova Data para o Fim do Estágio.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="nomeSeguradora">Nome Seguradora</label>
                                    <input class="form-control" id="nomeSeguradora" type="text" name="nomeSeguradora" required placeholder="Nome Seguradora"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Nome da Seguradora.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="codigoApoliceTCE">Código Apólice</label>
                                    <input class="form-control" id="codigoApoliceTCE" type="text" name="codigoApolice" required placeholder="Código Apólice do Seguro"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Código Apólice.
                                    </div>
                                </div>
                                <div role="toolbar" style="text-align: right">
                                    <button class="btn btn-primary" type="submit" id="submitButtonDiscenteTermoAditivo" name="submitButtonDiscenteTermoAditivo" value="termoAditivo">Enviar</button>
                                </div>
                            </form>
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
</div>
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
                <div class="modal-body">Selecione "Finalizar" abaixo se você realmente deseja finalizar esse pedido.</div>
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