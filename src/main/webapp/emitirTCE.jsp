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
            let formTceAssinado = document.getElementById('tceAssinado');
            if(formTceAssinado) {
                const forms = document.querySelectorAll('.needs-validation');
                Array.prototype.slice.call(forms).forEach((formTceAssinado) => {
                    formTceAssinado.addEventListener('submit', (event) => {
                        if (!formTceAssinado.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        formTceAssinado.classList.add('was-validated');
                    }, false);
                });
            }
            $('input[id^=dataInicio], input[id^=dataFim], input[id^=dataInicioTCE], input[id^=dataFimTCE]').datepicker({
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
                    if('TCE' === data.page){
                        window.location.replace("emitirTCE.jsp");
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
        function hideTextAreaTCE(option){
            if ('empresa' === option || 'branco' === option){
                $("#discenteForm").children().prop('disabled',true);
                $("#discenteForm").prop('hidden',true);
                $("#tceAssinado").children().prop('disabled',false);
                $("#tceAssinado").prop('hidden',false);
            }else {
                $("#discenteForm").children().prop('disabled',false);
                $("#discenteForm").prop('hidden',false);
                $("#tceAssinado").children().prop('disabled',true);
                $("#tceAssinado").prop('hidden',true);
            }
            if ('empresa' === option || 'gerar' === option){
                $("#modeloTCEUFRRJ").children().prop('disabled',true);
                $("#modeloTCEUFRRJ").prop('hidden',true);
            }else {
                $("#modeloTCEUFRRJ").children().prop('disabled',false);
                $("#modeloTCEUFRRJ").prop('hidden',false);
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
                <h1 class="h3 mb-4 text-gray-800">Elaborar TCE</h1>
            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <div class="form-outline mb-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="radioTCE" id="tceEmpresa" required onclick="hideTextAreaTCE('empresa')">
                                    <label class="form-check-label" for="tceEmpresa">Enviar TCE Usando o Modelo da Empresa</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="radioTCE" id="tceBranco" required onclick="hideTextAreaTCE('branco')">
                                    <label class="form-check-label" for="tceBranco">Enviar TCE Usando o Modelo da UFRRJ em Branco</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="radioTCE" id="tceGerar" required onclick="hideTextAreaTCE('gerar')">
                                    <label class="form-check-label" for="tceGerar">Gerar TCE</label>
                                </div>
                            </div>
                            <div class="form-floating mb-3" id="modeloTCEUFRRJ" hidden>
                                <label for="tceDiscente">Visualizar o Modelo em Branco do TCE da UFRRJ:</label>
                                <a id="tceDiscente" href="${TCE_MODELO_UFRRJ_URL}">${TCE_MODELO_UFRRJ}</a>
                            </div>
                            <c:choose>
                                <c:when test="${'NOVO_STEP4_TCE'== NOVO_ESTAGIO.status.name()}">
                                    <form class="needs-validation" enctype="multipart/form-data" novalidate id="tceAssinado" name="discenteForm" action="discenteController" method="post" hidden>
                                        <div class="form-floating mb-3">
                                            <label for="erroDocumentoPreenchido">Erro(s) Identificado(s) pelo(a) Docente Orientador(a)</label>
                                            <input class="form-control alert alert-danger" id="erroDocumentoPreenchido" type="text" name="erroDocumento" readonly disabled value="${NOVO_ESTAGIO.justificativaDocumentacao}"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="fileTCEAssinado">Anexe o TCE Preenchido e Assinado:</label>
                                            <input id="fileTCEAssinado" name="fileTCEAssinado" required type="file"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Anexe o TCE Preenchido e Assinado.
                                            </div>
                                        </div>
                                        <div role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" type="submit" id="submitButtonDiscenteElaborarTCEPreenchido" name="submitButtonDiscenteElaborarTCE" value="step4">Enviar</button>
                                        </div>
                                    </form>
                                    <form class="needs-validation" enctype="multipart/form-data" novalidate id="discenteForm" name="discenteForm" action="discenteController" method="post" hidden>
                                        <div class="form-floating mb-3">
                                            <label for="erroDocumento">Erro(s) Identificado(s) pelo(a) Docente Orientador(a)</label>
                                            <input class="form-control alert alert-danger" id="erroDocumento" type="text" name="erroDocumento" readonly disabled value="${NOVO_ESTAGIO.justificativaDocumentacao}"/>
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
                                            <input class="form-control" id="intervaloEstagioTCE" type="text" name="intervaloEstagio" required placeholder="Tempo de Intervalo em Horas (Exemplo: 2)" pattern="^\d+$" value="${DOCUMENTO.tce.getTotalHoras()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Intervalo.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="totalHorasTCE">Total Horas</label>
                                            <input class="form-control" id="totalHorasTCE" type="text" name="totalHoras" required placeholder="Total Horas Semanais (Exemplo: 30)" pattern="^\d+$" value="${DOCUMENTO.tce.getIntervalo()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Total Horas.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="dataInicioTCE">Data Início</label>
                                            <input class="form-control" id="dataInicioTCE" type="text" name="dataInicio" required value="${DOCUMENTO.tce.getDataInicio()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe a Data de Início.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="dataFimTCE">Data Fim</label>
                                            <input class="form-control" id="dataFimTCE" type="text" name="dataFim" required value="${DOCUMENTO.tce.getDataFim()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe a Data de Fim.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="bolsaEstagioTCE">Bolsa</label>
                                            <input class="form-control" id="bolsaEstagioTCE" type="text" name="bolsaEstagio" required placeholder="Valor da Bolsa de Estágio por extenso (Exemplo: Oitocentos reais)" pattern="^[\sa-zA-Z]+$" value="${DOCUMENTO.tce.getBolsa()}"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Valor da Bolsa.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="auxilioTransporteTCE">Auxílio Transporte</label>
                                            <input class="form-control" id="auxilioTransporteTCE" type="text" name="auxilioTransporte" required placeholder="Valor do Auxílio Transporte por extenso (Exemplo: Duzentos reais)" pattern="^[\sa-zA-Z]+$" value="${DOCUMENTO.tce.getAuxTransporte()}"/>
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
                                    <form class="needs-validation" enctype="multipart/form-data" novalidate id="tceAssinado" name="discenteForm" action="discenteController" method="post" hidden>
                                        <div class="form-floating mb-3">
                                            <label for="fileTCEAssinadoPrincipal">Anexe o TCE Preenchido e Assinado:</label>
                                            <input id="fileTCEAssinadoPrincipal" name="fileTCEAssinado" required type="file"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Anexe o TCE Preenchido e Assinado.
                                            </div>
                                        </div>
                                        <div role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" type="submit" id="submitButtonDiscenteElaborarTCEPrincipal" name="submitButtonDiscenteElaborarTCE" value="step4">Enviar</button>
                                        </div>
                                    </form>
                                    <form class="needs-validation" enctype="multipart/form-data" novalidate id="discenteForm" name="discenteForm" action="discenteController" method="post" hidden>
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
                                            <input class="form-control" id="intervaloEstagio" type="text" name="intervaloEstagio" required placeholder="Tempo de Intervalo em Horas (Exemplo: 2)" pattern="^\d+$"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Intervalo.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="totalHoras">Total Horas</label>
                                            <input class="form-control" id="totalHoras" type="text" name="totalHoras" required placeholder="Total Horas Semanais (Exemplo: 30)" pattern="^\d+$"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Total Horas.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="dataInicio">Data Início</label>
                                            <input class="form-control" id="dataInicio" type="text" name="dataInicio" required/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe a Data de Início.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="dataFim">Data Fim</label>
                                            <input class="form-control" id="dataFim" type="text" name="dataFim" required/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe a Data de Fim.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="bolsaEstagio">Bolsa</label>
                                            <input class="form-control" id="bolsaEstagio" type="text" name="bolsaEstagio" required placeholder="Valor da Bolsa de Estágio por extenso (Exemplo: Oitocentos reais)" pattern="^[\sa-zA-Z]+$"/>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe o Valor da Bolsa.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <label for="auxilioTransporte">Auxílio Transporte</label>
                                            <input class="form-control" id="auxilioTransporte" type="text" name="auxilioTransporte" required placeholder="Valor do Auxílio Transporte por extenso (Exemplo: Duzentos reais)" pattern="^[\sa-zA-Z]+$"/>
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