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
        function onReset(){
            document.getElementById('departamentoForm').reset();
            $('select[id^=selectDepartamento]').append('<option value="">Sigla - Nome do Departamento</option><c:forEach var="departamento" items="${LIST_DEPARTAMENTOS}"><option value="${departamento.id}">${departamento.sigla} - ${departamento.nome}</option></c:forEach>');
            $('div[id^=divButtonChooseDepartamento]').append('<button class="btn btn-primary" type="button" id="submitButtonEscolherDepartamento" name="submitButtonEscolherDepartamento" value="getInfoDepartamento" onclick="onChooseDepartamento()"><i class="fas fa-search fa-sm"></i></button>');
            $('form[id^=departamentoForm]').prop('hidden',true);
            $('div[id^=divInitial]').prop('hidden',false);
            document.getElementById('departamentoForm').classList.remove('was-validated');
            document.getElementById('chooseDepartamentoForm').classList.remove('was-validated');
            resetFieldsForm();
            $('input[id^=departamentoSelectInputHidden]').remove();
        }
        function onEditField(field){
            if ('name' === field){
                $('input[id^=nomeDepartamento]').prop('disabled',false);
                $('button[id^=editFieldName]').prop('hidden',true);
            }else if ('sigla' === field){
                $('input[id^=siglaDepartamento]').prop('disabled',false);
                $('button[id^=editFieldSigla]').prop('hidden',true);
            }else if ('status' === field){
                $('input[id^=departamentoAtivo], input[id^=departamentoDesativado]').prop('disabled',false);
                $('button[id^=editFieldStatusDepartamento]').prop('hidden',true);
            }
        }
        function onChooseDepartamento(){
            let formChooseDepartamento = document.getElementById('chooseDepartamentoForm');
            if(formChooseDepartamento) {
                if (!formChooseDepartamento.checkValidity()) {
                    formChooseDepartamento.classList.add('was-validated');
                    return false;
                }
            }
            $.ajax({
                type: "POST",
                url: "docenteComissaoController",
                cache: false,
                data: {
                    submitButtonEscolherDepartamento: $('button[id^=submitButtonEscolherDepartamento]').val(),
                    selectDepartamento: $('select[id^=selectDepartamento]').val()
                },
                dataType: "json",
                success: function (data) {
                    $('input[id^=nomeDepartamento]').val(data.nome);
                    $('input[id^=siglaDepartamento]').val(data.sigla);
                    if ('true' === data.isAtivo){
                        $('input[id^=departamentoAtivo]').prop('checked', true);
                    }else{
                        $('input[id^=departamentoDesativado]').prop('checked', true);
                    }
                    $('form[id^=departamentoForm]').append('<input id="departamentoSelectInputHidden" type="text" name="departamentoSelectInputHidden" hidden>');
                    $('input[id^=departamentoSelectInputHidden]').val($('select[id^=selectDepartamento]').val());
                    $('select[id^=selectDepartamento]').children().remove();
                    $('button[id^=submitButtonEscolherDepartamento]').remove();
                    $('div[id^=divInitial]').prop('hidden',true);
                    $('form[id^=departamentoForm]').prop('hidden',false);
                    $('hr[id^=lineSidebar]').remove();
                    resetFieldsForm();
                },
                error: function (xhr, status, error) {
                    alert('Erro em processar os dados!');
                }
            });
        }
        function resetFieldsForm(){
            $('input[id^=nomeDepartamento], input[id^=siglaDepartamento], input[id^=departamentoAtivo], input[id^=departamentoDesativado]').prop('disabled',true);
            $('button[id^=editFieldName], button[id^=editFieldSigla], button[id^=editFieldStatusDepartamento]').prop('hidden',false);
        }
        function onEditDepartamento(){
            let formEditDepartamento = document.getElementById('departamentoForm');
            if(formEditDepartamento) {
                if (!formEditDepartamento.checkValidity()) {
                    formEditDepartamento.classList.add('was-validated');
                    $('div[id^=confirmModal]').modal('hide');
                    return false;
                }
            }
            $.ajax({
                type: "POST",
                url: "docenteComissaoController",
                cache: false,
                data: {
                    submitButtonEditDepartamento: $('button[id^=submitButtonEditDepartamento]').val(),
                    idDepartamento: $('input[id^=departamentoSelectInputHidden]').val(),
                    nomeDepartamento: $('input[id^=nomeDepartamento]').val(),
                    siglaDepartamento: $('input[id^=siglaDepartamento]').val(),
                    radioStatus: $('input[name^=radioStatus]:checked').val()
                },
                dataType: "json",
                success: function (data) {
                    if (data.editarDepartamento) {
                        alert('Departamento editado com sucesso!');
                        window.location.replace('editarDepartamento.jsp');
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
                <h1 class="h3 mb-4 text-gray-800">Editar Departamento</h1>

            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <form class="needs-validation" novalidate id="chooseDepartamentoForm">
                                <div class="input-group mb-3" id="divInitial">
                                    <select class="form-control" name="selectDepartamento" id="selectDepartamento" required>
                                        <option value="">Sigla - Nome do Departamento</option>
                                        <c:forEach var="departamento" items="${LIST_DEPARTAMENTOS}">
                                            <option value="${departamento.id}">${departamento.sigla} - ${departamento.nome}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="input-group-append" id="divButtonChooseDepartamento">
                                        <button class="btn btn-primary" type="button" id="submitButtonEscolherDepartamento" name="submitButtonEscolherDepartamento" value="getInfoDepartamento" onclick="onChooseDepartamento()">
                                            <i class="fas fa-search fa-sm"></i>
                                        </button>
                                    </div>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Escolha um Departamento.
                                    </div>
                                </div>
                            </form>
                            <form class="needs-validation" novalidate id="departamentoForm" hidden>
                                <hr class="sidebar-divider" id="lineSidebar">
                                <label for="nomeDepartamento">Nome do Departamento</label>
                                <div class="input-group mb-3">
                                    <input class="form-control" id="nomeDepartamento" type="text" name="nomeDepartamento" required disabled placeholder="Nome do Departamento"/>
                                    <div class="input-group-append">
                                        <button class="btn btn-primary" type="button" id="editFieldName" name="editFieldName" onclick="onEditField('name')">
                                            <i class="bi bi-pencil-fill"></i>
                                        </button>
                                    </div>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe um nome para o departamento.
                                    </div>
                                </div>
                                <label for="siglaDepartamento">Sigla do Departamento</label>
                                <div class="input-group mb-3">
                                    <input class="form-control" id="siglaDepartamento" type="text" name="siglaDepartamento" required disabled placeholder="Sigla do Departamento"/>
                                    <div class="input-group-append">
                                        <button class="btn btn-primary" type="button" id="editFieldSigla" name="editFieldSigla" onclick="onEditField('sigla')">
                                            <i class="bi bi-pencil-fill"></i>
                                        </button>
                                    </div>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe uma sigla para o departamento.
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="radioStatus" id="departamentoAtivo" value="ativado" required disabled>
                                        <label class="form-check-label" for="departamentoAtivo">Departamento Ativo</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="radioStatus" id="departamentoDesativado" value="desativado" required disabled>
                                        <label class="form-check-label" for="departamentoDesativado">Departamento Desativado</label>
                                    </div>
                                    <button class="btn btn-sm btn-primary" style="margin-bottom:7px" type="button" id="editFieldStatusDepartamento" name="editFieldStatusDepartamento" onclick="onEditField('status')">
                                        <i class="bi bi-pencil-fill"></i>
                                    </button>
                                </div>
                                <div role="toolbar" style="text-align: right">
                                    <button class="btn" style="background-color: #eaecf4; border-color: #d1d3e2" type="button" id="submitButtonResetDepartamento" name="submitButtonResetDepartamento" onclick="onReset()">
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
                <h5 class="modal-title" id="confirmModalLabel">Editar Departamento?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Selecione "Enviar" abaixo se você realmente deseja realizar esta edição.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Não</button>
                <button class="btn btn-primary" type="button" id="submitButtonEditDepartamento" name="submitButtonEditDepartamento" value="editarDepartamento" onclick="onEditDepartamento()">Enviar</button>
            </div>
        </div>
    </div>
</div>

<!-- Logout Modal-->
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="logoutModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="logoutModalLabel">Pronto para sair?</h5>
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