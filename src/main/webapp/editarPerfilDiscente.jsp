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
    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>
    <!-- Bootstrap icons-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.2/font/bootstrap-icons.css" rel="stylesheet" />
    <script>
        function onEditField(field){
            if ('name' === field){
                $('input[id^=nomeDiscente]').prop('disabled',false);
                $('button[id^=editFieldName]').prop('hidden',true);
            }else if ('matricula' === field){
                $('input[id^=matriculaDiscente]').prop('disabled',false);
                $('button[id^=editFieldMatricula]').prop('hidden',true);
            }else if ('telefone' === field){
                $('input[id^=telefoneDiscente]').prop('disabled',false);
                $('button[id^=editFieldTelefone]').prop('hidden',true);
            }
        }
        function onEditDiscente(){
            let formEditDiscente = document.getElementById('discenteForm');
            if(formEditDiscente) {
                if (!formEditDiscente.checkValidity()) {
                    formEditDiscente.classList.add('was-validated');
                    $('div[id^=confirmModal]').modal('hide');
                    return false;
                }
            }
            $.ajax({
                type: "POST",
                url: "discenteController",
                cache: false,
                data: {
                    submitButtonEditDiscente: $('button[id^=submitButtonEditDiscente]').val(),
                    nomeDiscente: $('input[id^=nomeDiscente]').val(),
                    matriculaDiscente: $('input[id^=matriculaDiscente]').val(),
                    telefoneDiscente: $('input[id^=telefoneDiscente]').val()
                },
                dataType: "json",
                success: function (data) {
                    if (data.editarDiscente) {
                        alert('Discente editado com sucesso!');
                        window.location.replace('editarPerfilDiscente.jsp');
                    }else{
                        alert(data.message);
                        $('div[id^=confirmModal]').modal('hide');
                    }
                },
                error: function (xhr, status, error) {
                    alert('Erro em processar os dados!');
                    $('div[id^=confirmModal]').modal('hide');
                }
            });
        }
        function onDeleteDiscente(){
            $.ajax({
                type: "POST",
                url: "discenteController",
                cache: false,
                data: {
                    submitButtonExcluirDiscente: $('button[id^=submitButtonExcluirDiscente]').val()
                },
                dataType: "json",
                success: function (data) {
                    if (data.excluirDiscente) {
                        alert('Conta excluída com sucesso!');
                        window.location.replace('editarPerfilDiscente.jsp');
                    }
                },
                error: function (xhr, status, error) {
                    alert('Erro em processar os dados!');
                    $('div[id^=excluirContaModal]').modal('hide');
                }
            });
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
                    if('PLANO_ATIVIDADES' === data.page){
                        window.location.replace("emitirPlanoAtividade.jsp");
                    }else if('JUSTIFICA' === data.page){
                        window.location.replace("justificativaNovoEstagio.jsp");
                    }else if('TCE' === data.page){
                        window.location.replace("emitirTCE.jsp");
                    }else if('ASSINAR' === data.page){
                        window.location.replace("assinarDocumentoDiscente.jsp");
                    }else if('NOVO_PEDIDO_FIM' === data.page){
                        window.location.replace("finalStep.jsp");
                    }else if('NOVO' === data.page){
                        window.location.replace("novoEstagio.jsp");
                    }else if('RENOVACAO_STEP4' === data.page){
                        window.location.replace("emitirTermoAditivo.jsp");
                    }else if('RENOVACAO' === data.page){
                        window.location.replace("renovacaoEstagio.jsp");
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
            <c:choose>
                <c:when test="${('NOVO_STEP1' == NOVO_ESTAGIO.status.name() || 'NOVO_STEP2_JUSTIFICADO' == NOVO_ESTAGIO.status.name() || 'NOVO_STEP4' == NOVO_ESTAGIO.status.name() || 'NOVO_STEP4_DOCENTE_ASSINADO' == NOVO_ESTAGIO.status.name()) && RENOVACAO_ESTAGIO == null}">
                    <div class="d-flex">
                        <a class="nav-link collapsed btn" href="#" data-toggle="modal" data-target="#statusPedidoModal">
                            <i class="fas fa-fw bi bi-clipboard-fill"></i>
                            <span>Novo Estágio</span>
                        </a>
                        <a type="btn" style="padding-right: 15px; padding-top:17px;" href="#" data-toggle="modal" data-target="#finishModal">
                            <img src="https://img.icons8.com/fluency/48/cancel.png" alt="cross-mark-emoji" width="16" height="16">
                        </a>
                    </div>
                </c:when>
                <c:when test="${RENOVACAO_ESTAGIO == null}">
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
                </c:when>
            </c:choose>
        </li>

        <!-- Nav Item - Renovação de estágio Collapse Menu -->
        <li class="nav-item">
            <c:choose>
                <c:when test="${('RENOVACAO_STEP2' == RENOVACAO_ESTAGIO.status.name() || 'RENOVACAO_STEP3_JUSTIFICADO' == RENOVACAO_ESTAGIO.status.name() || 'RENOVACAO_STEP1' == RENOVACAO_ESTAGIO.status.name() || 'RENOVACAO_STEP3' == RENOVACAO_ESTAGIO.status.name()) && NOVO_ESTAGIO == null}">
                    <div class="d-flex">
                        <a class="nav-link collapsed btn" href="#" data-toggle="modal" data-target="#statusPedidoModal">
                            <i class="fas fa-fw bi bi-clipboard-plus-fill"></i>
                            <span>Renovação de Estágio</span>
                        </a>
                        <a type="btn" style="padding-right: 15px; padding-top:17px;" href="#" data-toggle="modal" data-target="#finishModal">
                            <img src="https://img.icons8.com/fluency/48/cancel.png" alt="cross-mark-emoji" width="16" height="16">
                        </a>
                    </div>
                </c:when>
                <c:when test="${NOVO_ESTAGIO == null}">
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
                </c:when>
            </c:choose>
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
                <h1 class="h3 mb-4 text-gray-800">Editar Perfil</h1>
            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <form class="needs-validation" novalidate autocomplete="off" id="discenteForm">
                                <label for="nomeDiscente">Nome</label>
                                <div class="input-group mb-3">
                                    <input class="form-control" id="nomeDiscente" type="text" name="nomeDiscente" required disabled value="${DISCENTE.nome}"/>
                                    <div class="input-group-append">
                                        <button class="btn btn-primary" type="button" id="editFieldName" name="editFieldName" onclick="onEditField('name')">
                                            <i class="bi bi-pencil-fill"></i>
                                        </button>
                                    </div>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o seu Nome Completo.
                                    </div>
                                </div>
                                <div>
                                    <label for="matriculaDiscente">Matrícula</label>
                                </div>
                                <div class="input-group mb-3">
                                    <input class="form-control" id="matriculaDiscente" type="text" name="matriculaDiscente" required disabled value="${DISCENTE.matricula}"/>
                                    <div class="input-group-append">
                                        <button class="btn btn-primary" type="button" id="editFieldMatricula" name="editFieldMatricula" onclick="onEditField('matricula')">
                                            <i class="bi bi-pencil-fill"></i>
                                        </button>
                                    </div>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe a sua Matrícula.
                                    </div>
                                </div>
                                <label for="telefoneDiscente">Telefone</label>
                                <div class="input-group mb-3">
                                    <input class="form-control" id="telefoneDiscente" type="tel" name="telefoneDiscente" required disabled pattern="^\+?[0-9\s()-]{10,}$" value="${DISCENTE.telefone}"/>
                                    <div class="input-group-append">
                                        <button class="btn btn-primary" type="button" id="editFieldTelefone" name="editFieldTelefone" onclick="onEditField('telefone')">
                                            <i class="bi bi-pencil-fill"></i>
                                        </button>
                                    </div>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe um Telefone.
                                    </div>
                                </div>
                                <div role="toolbar" style="text-align: right">
                                    <button class="btn btn-danger" type="button" href="#" data-toggle="modal" data-target="#excluirContaModal">Excluir Conta</button>
                                    <button class="btn btn-primary" type="button" href="#" data-toggle="modal" data-target="#confirmModal">Enviar</button>
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

    <!-- Excluir Conta Modal-->
    <div class="modal fade" id="excluirContaModal" tabindex="-1" role="dialog" aria-labelledby="excluirContaModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="excluirContaModalLabel">Excluir Conta?</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">Selecione "Sim" abaixo se você realmente deseja realizar esta ação.</div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Não</button>
                    <button class="btn btn-primary" type="button" id="submitButtonExcluirDiscente" name="submitButtonExcluirDiscente" value="excluirDiscente" onclick="onDeleteDiscente()">Sim</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Confirm Modal-->
    <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="confirmModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmModalLabel">Editar Discente?</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">Selecione "Enviar" abaixo se você realmente deseja realizar esta edição.</div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Não</button>
                    <button class="btn btn-primary" type="button" id="submitButtonEditDiscente" name="submitButtonEditDiscente" value="editarDiscente" onclick="onEditDiscente()">Enviar</button>
                </div>
            </div>
        </div>
    </div>

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
    <!-- Status Pedido Modal-->
    <div class="modal fade" id="statusPedidoModal" tabindex="-1" role="dialog" aria-labelledby="statusPedidoModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="statusPedidoModalLabel">Status Pedido</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <c:choose>
                    <c:when test="${'NOVO_STEP1' == NOVO_ESTAGIO.status.name() || 'RENOVACAO_STEP2' == RENOVACAO_ESTAGIO.status.name()}">
                        <div class="modal-body">Comissão de estágio analisando o pedido.</div>
                    </c:when>
                    <c:when test="${'NOVO_STEP2_JUSTIFICADO' == NOVO_ESTAGIO.status.name() || 'RENOVACAO_STEP3_JUSTIFICADO' == RENOVACAO_ESTAGIO.status.name()}">
                        <div class="modal-body">Comissão de estágio analisando o recurso apresentado.</div>
                    </c:when>
                    <c:when test="${'NOVO_STEP4' == NOVO_ESTAGIO.status.name()}">
                        <div class="modal-body">Docente Orientador(a) analisando o Plano de Atividades e o TCE.</div>
                    </c:when>
                    <c:when test="${'NOVO_STEP4_DOCENTE_ASSINADO' == NOVO_ESTAGIO.status.name()}">
                        <div class="modal-body">Aguardando assinatura do(a) Docente Orientador(a).</div>
                    </c:when>
                    <c:when test="${'RENOVACAO_STEP1' == RENOVACAO_ESTAGIO.status.name()}">
                        <div class="modal-body">Aguardando informações do(a) Supervisor(a).</div>
                    </c:when>
                    <c:when test="${'RENOVACAO_STEP3' == RENOVACAO_ESTAGIO.status.name()}">
                        <div class="modal-body">Docente Orientador(a) analisando o relatório e as avaliações apresentadas.</div>
                    </c:when>
                </c:choose>
                <div class="modal-footer">
                    <button class="btn btn-primary" type="button" data-dismiss="modal">OK</button>
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