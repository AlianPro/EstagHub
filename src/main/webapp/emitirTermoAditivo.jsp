<%@ page import="br.com.estaghub.domain.Curso" %>
<%@ page import="br.com.estaghub.domain.Discente" %>
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
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.3/dist/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js" integrity="sha512-T/tUfKSV1bihCnd+MxKD0Hm1uBBroVYBOYSk1knyvQ9VyZJpc/ALb4P0r6ubwVPSGB2GvjeoMAJJImBG12TiaQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/locales/bootstrap-datepicker.pt-BR.min.js" integrity="sha512-mVkLPLQVfOWLRlC2ZJuyX5+0XrTlbW2cyAwyqgPkLGxhoaHNSWesYMlcUjX8X+k45YB8q90s88O7sos86636NQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" integrity="sha512-mSYUmp1HYZDFaVKK//63EcZq4iFWFjxSL+Z3T/aCt4IO9Cejm03q3NKKYN6pFQzY0SBOr8h+eCIAZHPXcpZaNw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin-2.min.js"></script>
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
            $('#dataInicioAditivo').datepicker({
                format: "dd/mm/yyyy",
                language: "pt-BR",
                autoclose: true,
                todayHighlight: true
            });
            $('#dataFimAditivo').datepicker({
                format: "dd/mm/yyyy",
                language: "pt-BR",
                autoclose: true,
                todayHighlight: true
            });
        });
        function goToNextStep(){
            let formLogin = document.getElementById('discenteForm');
            if(formLogin) {
                const forms = document.querySelectorAll('.needs-validation');
                Array.prototype.slice.call(forms).forEach((formLogin) => {
                    formLogin.addEventListener('submit', (event) => {
                        if (!formLogin.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                            return false;
                        }else{
                            return true;
                        }
                        formLogin.classList.add('was-validated');
                    }, false);
                });
            }
        }
        function logoutDiscente(){
            $.ajax({
                type: "POST",
                url: "discenteController",
                cache: false,
                data: {
                    buttonLogoutDiscente: $('button[id^=buttonLogoutDiscente]').val()
                }
            });
        }
        function hideTextAreaTermoAditivoPreenchido(){
            $("#modeloTermoAditivoUFRRJ").children().prop('disabled',true);
            $("#modeloTermoAditivoUFRRJ").prop('hidden',true);
            $("#termoAditivoAssinado").children().prop('disabled',false);
            $("#termoAditivoAssinado").prop('hidden',false);
        }
        function hideTextAreaTermoAditivoPreenchidoEmpresa(){
            $("#modeloTermoAditivoUFRRJ").children().prop('disabled',true);
            $("#modeloTermoAditivoUFRRJ").prop('hidden',true);
            $("#termoAditivoAssinado").children().prop('disabled',true);
            $("#termoAditivoAssinado").prop('hidden',true);
        }
        function hideTextAreaTermoAditivoPreenchidoUFRRJ(){
            $("#modeloTermoAditivoUFRRJ").children().prop('disabled',false);
            $("#modeloTermoAditivoUFRRJ").prop('hidden',false);
            $("#termoAditivoAssinado").children().prop('disabled',true);
            $("#termoAditivoAssinado").prop('hidden',true);
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

        <!-- Nav Item - Renovação de estágio Collapse Menu -->
        <c:choose>
            <c:when test="${NOVO_ESTAGIO == null}">
                <c:choose>
                    <c:when test="${'RENOVACAO_STEP3_JUSTIFICADO' == RENOVACAO_ESTAGIO.status.name() || 'RENOVACAO_STEP2' == RENOVACAO_ESTAGIO.status.name()}">
                        <li class="nav-item">
                            <a class="nav-link collapsed" href="#">
                                <i class="fas fa-fw fa-wrench"></i>
                                <span>Renovação de Estágio</span>
                            </a>
                        </li>
                    </c:when>
                    <c:when test="${'RENOVACAO_STEP3_REJEITADO' == RENOVACAO_ESTAGIO.status.name()}">
                        <li class="nav-item">
                            <a class="nav-link collapsed" href="justificativaNovoEstagio.jsp">
                                <i class="fas fa-fw fa-wrench"></i>
                                <span>Renovação de Estágio</span>
                            </a>
                        </li>
                    </c:when>
                    <c:when test="${'RENOVACAO_STEP4' == RENOVACAO_ESTAGIO.status.name()}">
                        <li class="nav-item">
                            <a class="nav-link collapsed" href="emitirTermoAditivo.jsp">
                                <i class="fas fa-fw fa-wrench"></i>
                                <span>Renovação de Estágio</span>
                            </a>
                        </li>
                    </c:when>
                    <c:when test="${'NOVO_PEDIDO_FIM' == RENOVACAO_ESTAGIO.status.name()}">
                        <li class="nav-item">
                            <a class="nav-link collapsed" href="#">
                                <i class="fas fa-fw fa-wrench"></i>
                                <span>Renovação de Estágio</span>
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link collapsed" href="renovacaoEstagio.jsp">
                                <i class="fas fa-fw fa-wrench"></i>
                                <span>Renovação de Estágio</span>
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:when>
        </c:choose>
        <c:if test="${'RENOVACAO_STEP4_DISCENTE_ASSINADO' == RENOVACAO_ESTAGIO.status.name()}">
            <li class="nav-item">
                <a class="nav-link collapsed" href="assinarDocumentoDiscente.jsp">
                    <i class="fas fa-fw fa-wrench"></i>
                    <span>Assinar Documentos</span>
                </a>
            </li>
        </c:if>

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
                <h1 class="h3 mb-4 text-gray-800">Emitir Termo Aditivo</h1>
            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <div class="form-outline mb-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="radioTermoAditivo" id="termoAditivoEmpresa" value="empresa" required onclick="hideTextAreaTermoAditivoPreenchidoEmpresa()">
                                    <label class="form-check-label" for="termoAditivoEmpresa">Enviar Termo Aditivo Usando o Modelo da Empresa</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="radioTermoAditivo" id="termoAditivoBranco" value="branco" required onclick="hideTextAreaTermoAditivoPreenchidoUFRRJ()">
                                    <label class="form-check-label" for="termoAditivoBranco">Enviar Termo Aditivo Usando o Modelo da UFRRJ em Branco</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="radioTermoAditivo" id="termoAditivoGerar" value="gerar" required onclick="hideTextAreaTermoAditivoPreenchido()">
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
                                    <input id="fileTCEAssinado" name="fileTermoAditivoAssinado" required type="file" accept=".doc, .docx, .pdf"/>
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
                            <form class="needs-validation" enctype="multipart/form-data" novalidate id="discenteForm" name="discenteForm" action="discenteController" method="post">
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
                    <button id="buttonLogoutDiscente" name="buttonLogoutDiscente" type="submit" value="logout" class="btn btn-primary" onclick="logoutDiscente()">Logout</button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

</html>