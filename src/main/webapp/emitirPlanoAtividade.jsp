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

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin-2.min.js"></script>
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
        function logout(){
            $.ajax({
                type: "POST",
                url: "principalController",
                data: {
                    buttonLogout: 'logout'
                },
                sucess: function (){
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
                    <c:when test="${RENOVACAO_ESTAGIO == null}">
                        <c:choose>
                            <c:when test="${'NOVO_STEP1' == NOVO_ESTAGIO.status.name()}">
                                <%--                    TODO tela para avisar q está esperando resposta do docente--%>
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="#">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_STEP2' == NOVO_ESTAGIO.status.name()}">
                                <%--TODO tela para avisar q está esperando resposta do docente--%>
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirPlanoAtividade.jsp">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_STEP2_REJEITADO' == NOVO_ESTAGIO.status.name()}">
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="justificativaNovoEstagio.jsp">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_STEP2_JUSTIFICADO' == NOVO_ESTAGIO.status.name()}">
                                <%--TODO tela para avisar q está esperando resposta do docente--%>
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="#">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_STEP3' == NOVO_ESTAGIO.status.name()}">
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirTCE.jsp">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_STEP3_DISCENTE_ASSINADO' == NOVO_ESTAGIO.status.name()}">
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="assinarDocumentoDiscente.jsp">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_STEP4' == NOVO_ESTAGIO.status.name()}">
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="#">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_STEP4_PLANO_ATIVIDADES' == NOVO_ESTAGIO.status.name()}">
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirPlanoAtividade.jsp">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_STEP4_TCE' == NOVO_ESTAGIO.status.name()}">
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirTCE.jsp">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_STEP4_ATIVIDADES_TCE' == NOVO_ESTAGIO.status.name()}">
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirPlanoAtividade.jsp">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:when test="${'NOVO_PEDIDO_FIM' == NOVO_ESTAGIO.status.name()}">
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="#">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a id="buttonNovoEstagio" class="nav-link collapsed" href="novoEstagio.jsp">
                                    <i class="fas fa-fw bi bi-clipboard-fill"></i>
                                    <span>Novo Estágio</span>
                                </a>
                            </c:otherwise>
                        </c:choose>
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
                                 src="assets/img/undraw_profile.svg">
                        </a>
                        <!-- Dropdown - User Information -->
                        <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                             aria-labelledby="userDropdown">


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
                <h1 class="h3 mb-4 text-gray-800">Elaborar Plano de Atividade</h1>
            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <c:choose>
                                <c:when test="${'NOVO_STEP4_PLANO_ATIVIDADES'== NOVO_ESTAGIO.status.name() || 'NOVO_STEP4_ATIVIDADES_TCE'== NOVO_ESTAGIO.status.name()}">
                                    <form class="needs-validation" enctype="multipart/form-data" novalidate id="discenteForm" name="discenteForm" action="discenteController" method="post">
                                        <div class="form-floating mb-3">
                                            <label for="erroDocumento">Erro(s) Identificado(s) pelo(a) Docente Orientador</label>
                                            <input class="form-control alert alert-danger" id="erroDocumento" type="text" name="erroDocumento" readonly disabled value="${NOVO_ESTAGIO.justificativaDocumentacao}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="nomeEmpresaPlanoAtividades">Nome da Empresa</label>
                                            <input class="form-control" id="nomeEmpresaPlanoAtividades" type="text" name="nomeEmpresa" value="${DOCUMENTO.planoAtividades.getNomeEmpresa()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="nomeResponsavelEmpresaPlanoAtividades">Nome do Responsável da Empresa pela Assinatura do TCE</label>
                                            <input class="form-control" id="nomeResponsavelEmpresaPlanoAtividades" type="text" name="nomeResponsavelEmpresa" value="${DOCUMENTO.planoAtividades.getResponsavelEmpresa()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="enderecoEmpresaPlanoAtividades">Endereço da Empresa</label>
                                            <input class="form-control" id="enderecoEmpresaPlanoAtividades" type="text" name="enderecoEmpresa" value="${DOCUMENTO.planoAtividades.getEnderecoEmpresa()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="telefoneEmpresaPlanoAtividades">Telefone da Empresa</label>
                                            <input class="form-control" id="telefoneEmpresaPlanoAtividades" type="text" name="telefoneEmpresa" value="${DOCUMENTO.planoAtividades.getTelefoneEmpresa()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="emailEmpresaPlanoAtividades">Email da Empresa</label>
                                            <input class="form-control" id="emailEmpresaPlanoAtividades" type="text" name="emailEmpresa" value="${DOCUMENTO.planoAtividades.getEmailEmpresa()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="nomeSupervisorPlanoAtividades">Nome do Supervisor</label>
                                            <input class="form-control" id="nomeSupervisorPlanoAtividades" type="text" name="nomeSupervisor" value="${DOCUMENTO.planoAtividades.getNomeSupervisor()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="formacaoSupervisorPlanoAtividades">Formação Profissional do Supervisor</label>
                                            <input class="form-control" id="formacaoSupervisorPlanoAtividades" type="text" name="formacaoSupervisor" value="${DOCUMENTO.planoAtividades.getFormacaoSupervisor()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="primeiraAtividadePlanoAtividades">Primeira Atividade</label>
                                            <input class="form-control" id="primeiraAtividadePlanoAtividades" type="text" name="primeiraAtividade" value="${DOCUMENTO.planoAtividades.getPrimeiraAtividade()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="segundaAtividadePlanoAtividades">Segunda Atividade</label>
                                            <input class="form-control" id="segundaAtividadePlanoAtividades" type="text" name="segundaAtividade" value="${DOCUMENTO.planoAtividades.getSegundaAtividade()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="terceiraAtividadePlanoAtividades">Terceira Atividade</label>
                                            <input class="form-control" id="terceiraAtividadePlanoAtividades" type="text" name="terceiraAtividade" value="${DOCUMENTO.planoAtividades.getTerceiraAtividade()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="quartaAtividadePlanoAtividades">Quarta Atividade</label>
                                            <input class="form-control" id="quartaAtividadePlanoAtividades" type="text" name="quartaAtividade" value="${DOCUMENTO.planoAtividades.getQuartaAtividade()}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="quintaAtividadePlanoAtividades">Quinta Atividade</label>
                                            <input class="form-control" id="quintaAtividadePlanoAtividades" type="text" name="quintaAtividade" value="${DOCUMENTO.planoAtividades.getQuintaAtividade()}"/>
                                        </div>
                                        <div role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" type="submit" id="submitButtonDiscentePlanoAtividade" name="submitButtonDiscenteElaborarPlanoAtividade" value="step3">Enviar</button>
                                        </div>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form class="needs-validation" enctype="multipart/form-data" novalidate id="discenteForm" name="discenteForm" action="discenteController" method="post">
                                        <div class="form-floating mb-3">
                                            <label for="nomeEmpresa">Nome da Empresa</label>
                                            <input class="form-control" id="nomeEmpresa" type="text" name="nomeEmpresa" required placeholder="Nome Completo da Empresa"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Nome da Empresa.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="nomeResponsavelEmpresa">Nome do Responsável da Empresa pela Assinatura do TCE</label>
                                            <input class="form-control" id="nomeResponsavelEmpresa" type="text" name="nomeResponsavelEmpresa" required placeholder="Nome Completo do Responsável da Empresa pela Assinatura do TCE"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Nome do Responsável da Empresa pela Assinatura do TCE.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="enderecoEmpresa">Endereço da Empresa</label>
                                            <input class="form-control" id="enderecoEmpresa" type="text" name="enderecoEmpresa" required placeholder="Endereço da Empresa"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Endereço da Empresa.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="telefoneEmpresa">Telefone da Empresa</label>
                                            <input class="form-control" id="telefoneEmpresa" type="text" name="telefoneEmpresa" required placeholder="(00) 00000-0000"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Telefone da Empresa.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="emailEmpresa">Email da Empresa</label>
                                            <input class="form-control" id="emailEmpresa" type="text" name="emailEmpresa" required placeholder="name@example.com"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Email da Empresa.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="nomeSupervisor">Nome do Supervisor</label>
                                            <input class="form-control" id="nomeSupervisor" type="text" name="nomeSupervisor" required placeholder="Nome Completo do Supervisor"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Nome do Supervisor.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="formacaoSupervisor">Formação Profissional do Supervisor</label>
                                            <input class="form-control" id="formacaoSupervisor" type="text" name="formacaoSupervisor" required placeholder="Formação Profissional do Supervisor"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe a Formação Profissional do Supervisor.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="primeiraAtividade">Primeira Atividade</label>
                                            <input class="form-control" id="primeiraAtividade" type="text" name="primeiraAtividade" required placeholder="Descrição da Atividade (Obrigatório)"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe a Descrição da Primeira Atividade.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="segundaAtividade">Segunda Atividade</label>
                                            <input class="form-control" id="segundaAtividade" type="text" name="segundaAtividade" required placeholder="Descrição da Atividade (Obrigatório)"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe a Descrição da Segunda Atividade.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="terceiraAtividade">Terceira Atividade</label>
                                            <input class="form-control" id="terceiraAtividade" type="text" name="terceiraAtividade" placeholder="Descrição da Atividade (Opcional)"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="quartaAtividade">Quarta Atividade</label>
                                            <input class="form-control" id="quartaAtividade" type="text" name="quartaAtividade" placeholder="Descrição da Atividade (Opcional)"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="quintaAtividade">Quinta Atividade</label>
                                            <input class="form-control" id="quintaAtividade" type="text" name="quintaAtividade" placeholder="Descrição da Atividade (Opcional)"/>
                                        </div>
                                        <div role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" type="submit" id="submitButtonDiscenteElaborarPlanoAtividade" name="submitButtonDiscenteElaborarPlanoAtividade" value="step3">Enviar</button>
                                        </div>
                                    </form>
                                </c:otherwise>
                            </c:choose>
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

</body>

</html>