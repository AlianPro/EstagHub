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
    <!-- Bootstrap icons-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.2/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Bootstrap core JavaScript-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script>
        $(document).ready(function (){
            $('#selectDiscente').select2();
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
        function onChooseDiscente(){
            let formChooseDiscente = document.getElementById('chooseDiscenteForm');
            if(formChooseDiscente) {
                if (!formChooseDiscente.checkValidity()) {
                    formChooseDiscente.classList.add('was-validated');
                    return false;
                }
            }
            $.ajax({
                type: "POST",
                url: "supervisorController",
                cache: false,
                data: {
                    submitButtonEscolherDiscente: $('button[id^=submitButtonEscolherDiscente]').val(),
                    selectDiscente: $('select[id^=selectDiscente]').val()
                },
                dataType: "json",
                success: function (data) {
                    document.getElementById('discenteForm').reset();
                    $('input[id^=nomeDiscente]').val(data.nome);
                    $('input[id^=nomeEmpresa]').val(data.nomeEmpresaPedido);
                    $('form[id^=discenteForm]').append('<input id="discenteSelectInputHidden" type="text" name="discenteSelectInputHidden" hidden>');
                    $('input[id^=discenteSelectInputHidden]').val($('select[id^=selectDiscente]').val());
                    $('select[id^=selectDiscente]').children().remove();
                    $('button[id^=submitButtonEscolherDiscente]').remove();
                    $('div[id^=divInitial]').prop('hidden',true);
                    $('form[id^=discenteForm]').prop('hidden',false);
                    $('hr[id^=lineSidebar]').remove();
                },
                error: function (xhr, status, error) {
                    alert('Erro em processar os dados!');
                }
            });
        }
        function onReset(){
            document.getElementById('discenteForm').reset();
            $('select[id^=selectDiscente]').append('<option value="">ID - Discente</option><c:forEach var="discente" items="${LIST_DISCENTES_RENOV_STEP1}"><option value="${discente.id}">${discente.id} - ${discente.nome}</option></c:forEach>');
            $('div[id^=divButtonChooseDiscente]').append('<button class="btn btn-primary" type="button" id="submitButtonEscolherDiscente" name="submitButtonEscolherDiscente" value="getInfoDiscente" onclick="onChooseDiscente()"><i class="fas fa-search fa-sm"></i></button>');
            $('form[id^=discenteForm]').prop('hidden',true);
            $('div[id^=divInitial]').prop('hidden',false);
            document.getElementById('discenteForm').classList.remove('was-validated');
            document.getElementById('chooseDiscenteForm').classList.remove('was-validated');
            $('input[id^=discenteSelectInputHidden]').remove();
        }
        function onVincularSupervisor(){
            let formVincularSupervisor = document.getElementById('discenteForm');
            if(formVincularSupervisor) {
                if (!formVincularSupervisor.checkValidity()) {
                    formVincularSupervisor.classList.add('was-validated');
                    $('div[id^=confirmModal]').modal('hide');
                    return false;
                }
            }
            $.ajax({
                type: "POST",
                url: "supervisorController",
                cache: false,
                data: {
                    submitButtonVincularSupervisor: $('button[id^=submitButtonVincularSupervisor]').val(),
                    idDiscente: $('input[id^=discenteSelectInputHidden]').val(),
                    cpfDiscente: $('input[id^=cpfDiscente]').val(),
                    numeroPedido: $('input[id^=numeroPedido]').val()
                },
                dataType: "json",
                success: function (data) {
                    if (data.vincularSupervisor) {
                        alert('Supervisor vinculado ao pedido com sucesso!');
                        window.location.replace('vincularPedidoSupervisor.jsp');
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
        }
        .select2-container .select2-selection--single:focus {
            border-color: #ced4da;
            outline: 0;
            box-shadow: 0 0 0 .25rem rgba(206, 212, 218, 1);
        }
        .select2-container .select2-selection--single .select2-selection__arrow {
            height: 100%;
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
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="supervisor.jsp">
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
            <a class="nav-link collapsed" href="pedidosSupervisor.jsp">
                <i class="fas fa-fw bi bi-stack"></i>
                <span>Pedidos</span>
            </a>
        </li>
        <!-- Nav Item - Vincular Pedido Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="vincularPedidoSupervisor.jsp">
                <i class="fas fa-fw bi bi-file-earmark-plus-fill"></i>
                <span>Vincular ao Pedido</span>
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
                            <span class="mr-2 d-none d-lg-inline text-gray-600 small"><c:out value="${SUPERVISOR.nome}"></c:out></span>
                            <img class="img-profile rounded-circle"
                                 src="assets/img/icon_profile.png">
                        </a>
                        <!-- Dropdown - User Information -->
                        <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                             aria-labelledby="userDropdown">
                            <a class="dropdown-item" href="editarPerfilSupervisor.jsp">
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
                <h1 class="h3 mb-4 text-gray-800">Vincular Supervisor(a) ao Pedido</h1>

            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <form class="needs-validation" novalidate id="chooseDiscenteForm">
                                <div class="input-group mb-3" id="divInitial">
                                    <select class="form-control" name="selectDiscente" id="selectDiscente" required>
                                        <option value="">ID - Discente</option>
                                        <c:forEach var="discente" items="${LIST_DISCENTES_RENOV_STEP1}">
                                            <option value="${discente.id}">${discente.id} - ${discente.nome}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="input-group-append" id="divButtonChooseDiscente">
                                        <button class="btn btn-primary" type="button" id="submitButtonEscolherDiscente" name="submitButtonEscolherDiscente" value="getInfoDiscente" onclick="onChooseDiscente()">
                                            <i class="fas fa-search fa-sm"></i>
                                        </button>
                                    </div>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Escolha um(a) Discente.
                                    </div>
                                </div>
                            </form>
                            <form class="needs-validation" novalidate id="discenteForm" hidden>
                                <hr class="sidebar-divider" id="lineSidebar">
                                <label for="nomeDiscente">Nome</label>
                                <div class="input-group mb-3">
                                    <input class="form-control" id="nomeDiscente" type="text" name="nomeDiscente" disabled/>
                                </div>
                                <label for="nomeEmpresa">Nome da Empresa</label>
                                <div class="input-group mb-3">
                                    <input class="form-control" id="nomeEmpresa" type="text" name="nomeEmpresa" disabled/>
                                </div>
                                <label for="cpfDiscente">CPF</label>
                                <div class="input-group mb-3">
                                    <input class="form-control" id="cpfDiscente" type="text" name="cpfDiscente" required placeholder="Digite o cpf do(a) Discente" pattern="(\d{3}\.?\d{3}\.?\d{3}-?\d{2})"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o CPF do(a) Discente.
                                    </div>
                                </div>
                                <label for="numeroPedido">Número do Pedido</label>
                                <div class="input-group mb-3">
                                    <input class="form-control" id="numeroPedido" type="text" name="numeroPedido" required placeholder="Digite o número do Pedido" pattern="^\d+$"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Número do Pedido.
                                    </div>
                                </div>
                                <div role="toolbar" style="text-align: right">
                                    <button class="btn" style="background-color: #eaecf4; border-color: #d1d3e2" type="button" id="submitButtonResetDiscente" name="submitButtonResetDiscente" onclick="onReset()">
                                        <i class="bi bi-arrow-clockwise"></i>
                                    </button>
                                    <button class="btn btn-primary" type="button" href="#" data-toggle="modal" data-target="#confirmModal">Enviar</button>
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

<!-- Confirm Modal-->
<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="confirmModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="confirmModalLabel">Se vincular a esse Pedido?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Selecione "Enviar" abaixo se você realmente deseja realizar esta vinculação.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Não</button>
                <button class="btn btn-primary" type="button" id="submitButtonVincularSupervisor" name="submitButtonVincularSupervisor" value="vincularSupervisor" onclick="onVincularSupervisor()">Enviar</button>
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
<!-- Custom scripts for all pages-->
<script src="js/sb-admin-2.min.js"></script>
</body>

</html>