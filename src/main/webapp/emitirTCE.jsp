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

    <!-- Core plugin JavaScript-->
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

        <!-- Nav Item - Novo estágio Collapse Menu -->
        <li class="nav-item">
            <c:choose>
                <c:when test="${RENOVACAO_ESTAGIO == null}">
                    <c:choose>
                        <c:when test="${'NOVO_STEP1' == NOVO_ESTAGIO.status.name()}">
                            <%--                    TODO tela para avisar q está esperando resposta do docente--%>
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="#">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_STEP2' == NOVO_ESTAGIO.status.name()}">
                            <%--TODO tela para avisar q está esperando resposta do docente--%>
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirPlanoAtividade.jsp">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_STEP2_REJEITADO' == NOVO_ESTAGIO.status.name()}">
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="justificativaNovoEstagio.jsp">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_STEP2_JUSTIFICADO' == NOVO_ESTAGIO.status.name()}">
                            <%--TODO tela para avisar q está esperando resposta do docente--%>
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="#">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_STEP3' == NOVO_ESTAGIO.status.name()}">
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirTCE.jsp">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_STEP3_DISCENTE_ASSINADO' == NOVO_ESTAGIO.status.name()}">
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="assinarDocumentoDiscente.jsp">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_STEP4' == NOVO_ESTAGIO.status.name()}">
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="#">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_STEP4_PLANO_ATIVIDADES' == NOVO_ESTAGIO.status.name()}">
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirPlanoAtividade.jsp">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_STEP4_TCE' == NOVO_ESTAGIO.status.name()}">
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirTCE.jsp">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_STEP4_ATIVIDADES_TCE' == NOVO_ESTAGIO.status.name()}">
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="emitirPlanoAtividade.jsp">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:when test="${'NOVO_PEDIDO_FIM' == NOVO_ESTAGIO.status.name()}">
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="#">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a id="buttonNovoEstagio" class="nav-link collapsed" href="novoEstagio.jsp">
                                <i class="fas fa-fw fa-cog"></i>
                                <span>Novo Estágio</span>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
            </c:choose>
        </li>

        <c:if test="${'NOVO_STEP3_DISCENTE_ASSINADO' == NOVO_ESTAGIO.status.name()}">
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
                <h1 class="h3 mb-4 text-gray-800">Elaborar TCE</h1>
            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <c:choose>
                                <c:when test="${'NOVO_STEP4_TCE'== NOVO_ESTAGIO.status.name() || 'NOVO_STEP4_ATIVIDADES_TCE'== NOVO_ESTAGIO.status.name()}">
                                    <form class="needs-validation" enctype="multipart/form-data" novalidate id="discenteForm" name="discenteForm" action="discenteController" method="post">
                                        <div class="form-floating mb-3">
                                            <label for="erroDocumento">Erro(s) Identificado(s) pelo(a) Docente Orientador</label>
                                            <input class="form-control alert alert-danger" id="erroDocumento" type="text" name="erroDocumento" readonly disabled value="${NOVO_ESTAGIO.justificativaDocumentacao}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="nomeEmpresaTCE">Nome da Empresa</label>
                                            <input class="form-control" id="nomeEmpresaTCE" type="text" name="nomeEmpresa" required placeholder="Nome da Empresa" value="${DOCUMENTO.tce.getNomeEmpresa()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Nome da Empresa.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="cnpjEmpresaTCE">CNPJ da Empresa</label>
                                            <input class="form-control" id="cnpjEmpresaTCE" type="text" name="cnpjEmpresa" required placeholder="CNPJ da Empresa" value="${DOCUMENTO.tce.getCnpjEmpresa()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o CNPJ da Empresa.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="horarioInicioTCE">Horário Início</label>
                                            <input class="form-control" id="horarioInicioTCE" type="time" name="horarioInicio" required value="${DOCUMENTO.tce.getHorarioInicio()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Horário de Início.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="horarioFimTCE">Horário Fim</label>
                                            <input class="form-control" id="horarioFimTCE" type="time" name="horarioFim" required value="${DOCUMENTO.tce.getHorarioFim()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Horário de Fim.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="intervaloEstagioTCE">Intervalo</label>
                                            <input class="form-control" id="intervaloEstagioTCE" type="text" name="intervaloEstagio" required placeholder="Tempo de Intervalo em Horas" value="${DOCUMENTO.tce.getTotalHoras()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Intervalo.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="totalHorasTCE">Total Horas</label>
                                            <input class="form-control" id="totalHorasTCE" type="text" name="totalHoras" required placeholder="Total Horas Semanais" value="${DOCUMENTO.tce.getIntervalo()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Total Horas.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="dataInicioTCE">Data Início</label>
                                            <input class="form-control" id="dataInicioTCE" type="date" name="dataInicio" required value="${DOCUMENTO.tce.getDataInicio()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe a Data de Início.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="dataFimTCE">Data Fim</label>
                                            <input class="form-control" id="dataFimTCE" type="date" name="dataFim" required value="${DOCUMENTO.tce.getDataFim()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe a Data de Fim.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="bolsaEstagioTCE">Bolsa</label>
                                            <input class="form-control" id="bolsaEstagioTCE" type="text" name="bolsaEstagio" required placeholder="Valor da Bolsa de Estágio por extenso" value="${DOCUMENTO.tce.getBolsa()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Valor da Bolsa.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="auxilioTransporteTCE">Auxílio Transporte</label>
                                            <input class="form-control" id="auxilioTransporteTCE" type="text" name="auxilioTransporte" required placeholder="Valor do Auxílio Transporte por extenso" value="${DOCUMENTO.tce.getAuxTransporte()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Valor do Auxílio Transporte.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="codigoApoliceTCE">Código Apólice</label>
                                            <input class="form-control" id="codigoApoliceTCE" type="text" name="codigoApolice" required placeholder="Código Apólice do Seguro" value="${DOCUMENTO.tce.getCodApolice()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Código Apólice.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="nomeSeguradoraTCE">Nome Seguradora</label>
                                            <input class="form-control" id="nomeSeguradoraTCE" type="text" name="nomeSeguradora" required placeholder="Nome da Seguradora" value="${DOCUMENTO.tce.getNomeSeguradora()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Nome da Seguradora.
                                            </div>
                                        </div>
                                        <div role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" type="submit" id="submitButtonDiscenteTCE" name="submitButtonDiscenteElaborarTCE" value="step4">Enviar</button>
                                        </div>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form class="needs-validation" enctype="multipart/form-data" novalidate id="discenteForm" name="discenteForm" action="discenteController" method="post">
                                <div class="form-floating mb-3">
                                    <label for="nomeEmpresa">Nome da Empresa</label>
                                    <input class="form-control" id="nomeEmpresa" type="text" name="nomeEmpresa" required placeholder="Nome da Empresa"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Nome da Empresa.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="cnpjEmpresa">CNPJ da Empresa</label>
                                    <input class="form-control" id="cnpjEmpresa" type="text" name="cnpjEmpresa" required placeholder="CNPJ da Empresa"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o CNPJ da Empresa.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="horarioInicio">Horário Início</label>
                                    <input class="form-control" id="horarioInicio" type="time" name="horarioInicio" required/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Horário de Início.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="horarioFim">Horário Fim</label>
                                    <input class="form-control" id="horarioFim" type="time" name="horarioFim" required />
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Horário de Fim.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="intervaloEstagio">Intervalo</label>
                                    <input class="form-control" id="intervaloEstagio" type="text" name="intervaloEstagio" required placeholder="Tempo de Intervalo em Horas"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Intervalo.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="totalHoras">Total Horas</label>
                                    <input class="form-control" id="totalHoras" type="text" name="totalHoras" required placeholder="Total Horas Semanais"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Total Horas.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="dataInicio">Data Início</label>
                                    <input class="form-control" id="dataInicio" type="date" name="dataInicio" required/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe a Data de Início.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="dataFim">Data Fim</label>
                                    <input class="form-control" id="dataFim" type="date" name="dataFim" required/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe a Data de Fim.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="bolsaEstagio">Bolsa</label>
                                    <input class="form-control" id="bolsaEstagio" type="text" name="bolsaEstagio" required placeholder="Valor da Bolsa de Estágio por extenso"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Valor da Bolsa.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="auxilioTransporte">Auxílio Transporte</label>
                                    <input class="form-control" id="auxilioTransporte" type="text" name="auxilioTransporte" required placeholder="Valor do Auxílio Transporte por extenso"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Valor do Auxílio Transporte.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="codigoApolice">Código Apólice</label>
                                    <input class="form-control" id="codigoApolice" type="text" name="codigoApolice" required placeholder="Código Apólice do Seguro"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Código Apólice.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="nomeSeguradora">Nome Seguradora</label>
                                    <input class="form-control" id="nomeSeguradora" type="text" name="nomeSeguradora" required placeholder="Nome da Seguradora"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Nome da Seguradora.
                                    </div>
                                </div>
                                <div role="toolbar" style="text-align: right">
                                    <button class="btn btn-primary" type="submit" id="submitButtonDiscenteElaborarTCE" name="submitButtonDiscenteElaborarTCE" value="step4">Enviar</button>
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