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
            $("#submitButtonDiscenteCadastroNovoEstagio1").click(function (){
                let formLogin = document.getElementById('discenteForm');
                if(formLogin) {
                    const forms = document.querySelectorAll('.needs-validation');
                    Array.prototype.slice.call(forms).forEach((formLogin) => {
                        formLogin.addEventListener('submit', (event) => {
                            if (!formLogin.checkValidity()) {
                                event.preventDefault();
                                event.stopPropagation();
                                // return false;
                            }else{
                                // $.ajax({
                                //     type: "POST",
                                //     url: "discenteController",
                                //     data: {
                                //         submitButtonDiscenteCadastroNovoEstagio1: 'step1',
                                //         cpfDiscente: $('input[id^=cpfDiscente]').val(),
                                //         rgDiscente: $('input[id^=rgDiscente]').val(),
                                //         orgaoExpedidorRgDiscente: $('input[id^=orgaoExpedidorRgDiscente]').val(),
                                //         iraDiscente: $('input[id^=iraDiscente]').val(),
                                //         periodoDiscente: $('input[id^=periodoDiscente]').val(),
                                //         cargaHorariaCumpridaDiscente: $('input[id^=cargaHorariaCumpridaDiscente]').val(),
                                //         selectCursoDiscente: $('select[id^=selectCurso]').val()
                                //     }
                                // });
                                // return false;
                                // window.location='discente.jsp';
                                // document.getElementById("discenteForm").reset();
                            }
                            formLogin.classList.add('was-validated');
                        }, false);
                    });
                }
            });
            // let formLogin = document.getElementById('discenteForm');
            // if(formLogin) {
            //     const forms = document.querySelectorAll('.needs-validation');
            //     Array.prototype.slice.call(forms).forEach((formLogin) => {
            //         formLogin.addEventListener('submit', (event) => {
            //             if (!formLogin.checkValidity()) {
            //                 event.preventDefault();
            //                 event.stopPropagation();
            //             }
            //             formLogin.classList.add('was-validated');
            //         }, false);
            //     });
            // }
        });
        // $(function() {
        //     function sendNextStep() {
        //         let formLogin = document.getElementById('discenteForm');
        //         if (formLogin) {
        //             const forms = document.querySelectorAll('.needs-validation');
        //             Array.prototype.slice.call(forms).forEach((formLogin) => {
        //                 formLogin.addEventListener('submit', (event) => {
        //                     if (!formLogin.checkValidity()) {
        //                         event.preventDefault();
        //                         event.stopPropagation();
        //                         return false;
        //                     } else {
        //                         alert("dadsd");
        //                         return true;
        //                     }
        //                     formLogin.classList.add('was-validated');
        //                 }, false);
        //             });
        //         }
        //     }
        // });
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
                        // else{
                        //     $.ajax({
                        //         type: "POST",
                        //         url: "discenteController",
                        //         data: {
                        //             submitButtonDiscenteCadastroNovoEstagio1: 'step1',
                        //             cpfDiscente: $('input[id^=cpfDiscente]').val(),
                        //             rgDiscente: $('input[id^=rgDiscente]').val(),
                        //             orgaoExpedidorRgDiscente: $('input[id^=orgaoExpedidorRgDiscente]').val(),
                        //             iraDiscente: $('input[id^=iraDiscente]').val(),
                        //             periodoDiscente: $('input[id^=periodoDiscente]').val(),
                        //             cargaHorariaCumpridaDiscente: $('input[id^=cargaHorariaCumpridaDiscente]').val(),
                        //             selectCursoDiscente: $('select[id^=selectCurso]').val()
                        //         }
                        //     });
                        //     return true;
                        // }
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
                    <c:when test="${'RENOVACAO_STEP1' == RENOVACAO_ESTAGIO.status.name()}">
                        <li class="nav-item">
                            <a class="nav-link collapsed" href="#">
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
                <h1 class="h3 mb-4 text-gray-800">Cadastro Novo Estágio</h1>
            </div>
            <!-- /.container-fluid -->
            <div class="modal-body border-0 p-4">
                <div class="sw sw-theme-basic sw-justified">
                    <div class="tab-content">
                        <div class="tab-pane" style="display: block">
                            <form class="needs-validation" enctype="multipart/form-data" accept-charset="utf-8" novalidate id="discenteForm" name="discenteForm" action="discenteController" method="post">
                                <div class="form-floating mb-3">
                                    <label for="cpfDiscente">CPF</label>
                                    <input class="form-control" id="cpfDiscente" type="text" name="cpfDiscente" required placeholder="000.000.000-00"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe um CPF.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="rgDiscente">RG</label>
                                    <input class="form-control" id="rgDiscente" type="text" name="rgDiscente" required placeholder="00.000.000-0"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe um RG.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="orgaoExpedidorRgDiscente">Orgão Expedidor - RG</label>
                                    <input class="form-control" id="orgaoExpedidorRgDiscente" type="text" name="orgaoExpedidorRgDiscente" required placeholder="Digite o Orgão Expedidor do seu RG"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Orgão Expedidor do RG.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="iraDiscente">IRA</label>
                                    <input class="form-control" id="iraDiscente" type="text" name="iraDiscente" required placeholder="Índice de Rendimento Acadêmico"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe seu IRA.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="periodoDiscente">Período</label>
                                    <input class="form-control" id="periodoDiscente" type="text" name="periodoDiscente" required placeholder="Período do Curso"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe seu Período.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="cargaHorariaCumpridaDiscente">Carga Horária Cumprida</label>
                                    <input class="form-control" id="cargaHorariaCumpridaDiscente" type="text" name="cargaHorariaCumpridaDiscente" required placeholder="Carga Hóraria Cumprida em Horas"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe sua Carga Horária Cumprida.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="enderecoDiscente">Endereço</label>
                                    <input class="form-control" id="enderecoDiscente" type="text" name="enderecoDiscente" required placeholder="Endereço Completo"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe seu Endereço Completo.
                                    </div>
                                </div>
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
                                    <label for="enderecoEmpresa">Endereço da Empresa</label>
                                    <input class="form-control" id="enderecoEmpresa" type="text" name="enderecoEmpresa" required placeholder="Endereço da Empresa"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe o Endereço da Empresa.
                                    </div>
                                </div>
                                <div class="form-outline mb-3" id="textAreaDiv">
                                    <label class="form-label" for="textResumo">Resumo das Atividades Exercidas no Estágio Atualmente</label>
                                    <textarea class="form-control" id="textResumo" name="textResumo" required></textarea>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Informe um Resumo das Atividades Exercidas no Estágio Atualmente.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <select name="selectCursoDiscente" class="form-select" id="selectCursoDiscente" required>
                                        <option value="">Curso</option>
                                        <c:forEach var="curso" items="${LIST_CURSOS}">
                                            <option value="${curso.id}">${curso.nome}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Escolha seu Curso.
                                    </div>
                                </div>
                                <div class="form-outline mb-3">
                                    <label class="form-check-label">Há quanto tempo você Estagia nesta Empresa?</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="radioTempoEstagio" id="radioTempoEstagio1" value="Há 6 meses" required/>
                                    <label class="form-check-label" for="radioTempoEstagio1">Há 6 meses</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="radioTempoEstagio" id="radioTempoEstagio2" value="Há 1 ano" required/>
                                    <label class="form-check-label" for="radioTempoEstagio2">Há 1 ano</label>
                                </div>
                                <div class="form-check form-check-inline mb-3">
                                    <input class="form-check-input" type="radio" name="radioTempoEstagio" id="radioTempoEstagio3" value="Há mais de 1 ano" required/>
                                    <label class="form-check-label" for="radioTempoEstagio3">Há mais de 1 ano</label>
                                </div>
                                <div class="form-outline mb-3">
                                    <label class="form-check-label">Quanto o Estágio Contribuiu para sua Vida Acadêmica (Aquisição de Conhecimento na Área de Estudo do seu Curso)?</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <label class="form-check-label">Contribuiu muito pouco</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="radioAvaliacao" id="radioAvaliacao1" value="avaliacao1" required/>
                                    <label class="form-check-label" for="radioAvaliacao1">1</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="radioAvaliacao" id="radioAvaliacao2" value="avaliacao2" required/>
                                    <label class="form-check-label" for="radioAvaliacao2">2</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="radioAvaliacao" id="radioAvaliacao3" value="avaliacao3" required/>
                                    <label class="form-check-label" for="radioAvaliacao3">3</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="radioAvaliacao" id="radioAvaliacao4" value="avaliacao4" required/>
                                    <label class="form-check-label" for="radioAvaliacao4">4</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="radioAvaliacao" id="radioAvaliacao5" value="avaliacao5" required/>
                                    <label class="form-check-label" for="radioAvaliacao5">5</label>
                                </div>
                                <div class="form-check form-check-inline mb-3">
                                    <label class="form-check-label">Contribuiu muito</label>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="historicoDiscente">Anexe o Histórico Acadêmico:</label>
                                    <input id="historicoDiscente" name="historicoDiscente" required type="file" accept=".pdf"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Anexe o seu Histórico Acadêmico.
                                    </div>
                                </div>
                                <div class="form-floating mb-3">
                                    <label for="gradeHorarioDiscente">Anexe a Grade de Horário:</label>
                                    <input id="gradeHorarioDiscente" name="gradeHorarioDiscente" required type="file" accept=".pdf"/>
                                    <div class="valid-feedback">
                                        Perfeito!
                                    </div>
                                    <div class="invalid-feedback">
                                        Ops! Anexe a sua Grade de Horário.
                                    </div>
                                </div>
                                <div role="toolbar" style="text-align: right">
                                    <button class="btn btn-primary" type="submit" id="submitButtonDiscenteCadastroRenovacaoEstagio1" name="submitButtonDiscenteCadastroRenovacaoEstagio1" value="step1">Enviar</button>
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