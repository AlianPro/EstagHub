<%@ page import="br.com.estaghub.domain.Empresa" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="pt">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>EstagHub</title>
        <link rel="icon" type="image/x-icon" href="assets/img/rural_logo_branca.png"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
        <!-- Bootstrap icons-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.2/font/bootstrap-icons.css" rel="stylesheet" />
        <!-- Google fonts-->
        <link rel="preconnect" href="https://fonts.gstatic.com" />
        <link href="https://fonts.googleapis.com/css2?family=Newsreader:ital,wght@0,600;1,600&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css2?family=Mulish:ital,wght@0,300;0,500;0,600;0,700;1,300;1,500;1,600;1,700&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css2?family=Kanit:ital,wght@0,400;1,400&amp;display=swap" rel="stylesheet" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="css/styles.css" rel="stylesheet" />
        <!-- Core theme JS-->
        <script src="js/scripts.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.3/dist/jquery.min.js"></script>
        <!-- CSS -->
        <link href="https://cdn.jsdelivr.net/npm/smartwizard@6/dist/css/smart_wizard_all.min.css" rel="stylesheet" type="text/css" />
        <!-- JavaScript -->
        <script>
            $(function() {
                let formLogin = document.getElementById('loginForm');
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
                let formDiscente = document.getElementById('discenteForm');
                if(formDiscente) {
                    const forms = document.querySelectorAll('.needs-validation');
                    Array.prototype.slice.call(forms).forEach((formDiscente) => {
                        formDiscente.addEventListener('submit', (event) => {
                            if (!formDiscente.checkValidity()) {
                                event.preventDefault();
                                event.stopPropagation();
                            }
                            formDiscente.classList.add('was-validated');
                        }, false);
                    });
                }
                $("#smartwizard-supervisor").smartWizard({
                    selected: 0,
                    toolbar: {
                        showNextButton: false,
                        showPreviousButton: false
                    },
                    enableUrlHash: false,
                    anchor: {
                        markPreviousStepsAsDone: false,
                        unDoneOnBackNavigation: true,
                        enableNavigation: false
                    },
                    keyboard: {
                        keyNavigation: false
                    }
                });
                $("#smartwizard-supervisor").on("leaveStep", function(e, anchorObject, currentStepIdx, nextStepIdx, stepDirection) {
                    // Validate only on forward movement
                    if (stepDirection == 'forward') {
                        let form = document.getElementById('form-' + (currentStepIdx + 1));
                        if (form) {
                            if (!form.checkValidity()) {
                                form.classList.add('was-validated');
                                $("#smartwizard-supervisor").smartWizard("setState", [currentStepIdx], 'error');
                                $("#smartwizard-supervisor").smartWizard('fixHeight');
                                return false;
                            }
                            $("#smartwizard-supervisor").smartWizard("unsetState", [currentStepIdx], 'error');
                        }
                    }
                });
                // Step show event
                $("#smartwizard-supervisor").on("showStep", function(e, anchorObject, stepIndex, stepDirection, stepPosition) {
                    // Get step info from Smart Wizard
                    let stepInfo = $("#smartwizard-supervisor").smartWizard("getStepInfo");
                    if(stepPosition === 'first') {
                        $("#buttonNextSupervisor1").click( function (){
                            goToStepSupervisor(1);
                        });
                    }else if(stepIndex == 1) {
                        $("#buttonPreviousSupervisor2").click( function (){
                            goToStepSupervisor(0);
                        });
                    } else if(stepIndex == 2) {
                        listAllEmpresa();
                        $("#buttonPreviousSupervisor3").click( function (){
                            goToStepSupervisor(1);
                        });
                    } else if(stepIndex == 3) {
                        $("#buttonPreviousSupervisor4").click( function (){
                            goToStepSupervisor(1);
                        });
                    }
                    // Focus first name
                    if (stepIndex == 0) {
                        setTimeout(() => {
                            $('#nameSupervisor').focus();
                        }, 0);
                    }
                });
                $("#state_selector").on("change", function() {
                    $("#smartwizard-supervisor").smartWizard("setState", [$('#step_to_style').val()], $(this).val(), !$('#is_reset').prop("checked"));
                    return true;
                });

                $("#style_selector").on("change", function() {
                    $("#smartwizard-supervisor").smartWizard("setStyle", [$('#step_to_style').val()], $(this).val(), !$('#is_reset').prop("checked"));
                    return true;
                });
            });
            function onConfirmSupervisor1() {
                // Retorno está errado, quando o cadastro da fail ele retorna mensagem de sucesso!
                let form = document.getElementById('form-3');
                document.getElementById("form-4").reset();
                if (form) {
                    if (!form.checkValidity()) {
                        form.classList.add('was-validated');
                        $("#smartwizard-supervisor").smartWizard("setState", [2], 'error');
                        $("#smartwizard-supervisor").smartWizard('fixHeight');
                        return false;
                    }
                    $("#smartwizard-supervisor").smartWizard("unsetState", [2], 'error');
                    $.ajax({
                        type: "POST",
                        url: "principalController",
                        cache: false,
                        data: {
                            submitButtonSupervisor1: $('button[id^=submitButtonSupervisor1]').val(),
                            nameSupervisor: $('input[id^=nameSupervisor]').val(),
                            emailSupervisor: $('input[id^=emailSupervisor]').val(),
                            senhaSupervisor: $('input[id^=senhaSupervisor]').val(),
                            pedidoSupervisor: $('input[id^=pedidoSupervisor]').val(),
                            cargoSupervisor: $('input[id^=cargoSupervisor]').val(),
                            telefoneSupervisor: $('input[id^=telefoneSupervisor]').val(),
                            selectVinculoSupervisorEmpresa: $('select[id^=selectVinculoSupervisorEmpresa]').val()
                        },
                        dataType: "json",
                        success: function (data) {
                            alert(data);
                            window.location.reload();
                        },
                        error: function (xhr, status, error) {
                            if (xhr.status == 500) {
                                alert("Erro 500!");
                            }else{
                                alert("Cadastro realizado com sucesso!");
                            }
                            window.location.reload();
                            closeModalSupervisor();
                        }
                    });
                }
            }
            function onConfirmSupervisor2() {
                // Retorno está errado, quando o cadastro da fail ele retorna mensagem de sucesso!
                let form = document.getElementById('form-4');
                document.getElementById("form-3").reset();
                if (form) {
                    if (!form.checkValidity()) {
                        form.classList.add('was-validated');
                        $("#smartwizard-supervisor").smartWizard("setState", [3], 'error');
                        $("#smartwizard-supervisor").smartWizard('fixHeight');
                        return false;
                    }
                    $("#smartwizard-supervisor").smartWizard("unsetState", [3], 'error');
                    $.ajax({
                        type: "POST",
                        url: "principalController",
                        cache: false,
                        data: {
                            submitButtonSupervisor2: $('button[id^=submitButtonSupervisor2]').val(),
                            nameSupervisor: $('input[id^=nameSupervisor]').val(),
                            emailSupervisor: $('input[id^=emailSupervisor]').val(),
                            senhaSupervisor: $('input[id^=senhaSupervisor]').val(),
                            pedidoSupervisor: $('input[id^=pedidoSupervisor]').val(),
                            cargoSupervisor: $('input[id^=cargoSupervisor]').val(),
                            telefoneSupervisor: $('input[id^=telefoneSupervisor]').val(),
                            nomeEmpresaSupervisor: $('input[id^=nomeEmpresa]').val(),
                            cnpjEmpresaSupervisor: $('input[id^=cnpjEmpresa]').val(),
                            emailEmpresa: $('input[id^=emailEmpresa]').val(),
                            enderecoEmpresa: $('input[id^=enderecoEmpresa]').val(),
                            telefoneEmpresa: $('input[id^=telefoneEmpresa]').val()
                        },
                        dataType: "json",
                        success: function (data) {
                            alert(data);
                            window.location.reload();
                        },
                        error: function (xhr, status, error) {
                            if (xhr.status == 500) {
                                alert("Erro 500!");
                            }else{
                                alert("Cadastro realizado com sucesso!");
                            }
                            window.location.reload();
                            closeModalSupervisor();
                        }
                    });
                }
            }
            function listAllEmpresa(){
                //TODO remover esse scriptlet
                <%
                    Empresa empresa = new Empresa();
                    request.setAttribute("LIST_EMPRESAS",empresa.listAllEmpresa());
                %>
                <c:if test="${empty LIST_EMPRESAS}">
                    $("#buttonVincularEmpresa").hide();
                </c:if>
            }
            function goToStepSupervisor(num) {
                $('#smartwizard-supervisor').smartWizard("goToStep", num, true);
            }
            function closeModalSupervisor() {
                // Reset wizard
                $("#smartwizard-supervisor").smartWizard("reset");
                // Reset form
                document.getElementById("form-1").reset();
                document.getElementById("form-2").reset();
                document.getElementById("form-3").reset();
                document.getElementById("form-4").reset();
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/smartwizard@6/dist/js/jquery.smartWizard.min.js" type="text/javascript"></script>
        <style>
            :root {
                --sw-border-color:  #eeeeee;
                --sw-toolbar-btn-color:  #ffffff;
                --sw-toolbar-btn-background-color:  #2937f0;
                --sw-toolbar-btn-border-color:  #2937f0;
                --sw-anchor-default-primary-color:  #f8f9fa;
                --sw-anchor-default-secondary-color:  #b0b0b1;
                --sw-anchor-active-primary-color:  #2937f0;
                --sw-anchor-active-secondary-color:  #ffffff;
                --sw-anchor-done-primary-color:  #2937f0;
                --sw-anchor-done-secondary-color:  #fefefe;
                --sw-anchor-disabled-primary-color:  #f8f9fa;
                --sw-anchor-disabled-secondary-color:  #dbe0e5;
                --sw-anchor-error-primary-color:  #dc3545;
                --sw-anchor-error-secondary-color:  #ffffff;
                --sw-anchor-warning-primary-color:  #ffc107;
                --sw-anchor-warning-secondary-color:  #ffffff;
            }
            .sw .toolbar{
                padding: 0;
                text-align: right;
            }
        </style>
    </head>
    <body id="page-top">
        <!-- Navigation-->
        <nav class="navbar navbar-expand-lg navbar-light fixed-top shadow-sm" id="mainNav">
            <div class="container px-5">
                <a class="navbar-brand fw-bold" href="#page-top">EstagHub</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    Menu
                    <i class="bi-list"></i>
                </button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav ms-auto me-3 my-3 my-lg-0"></ul>
                    <button class="btn btn-primary rounded-pill px-3 mb-2 mb-lg-0" data-bs-toggle="modal" data-bs-target="#loginModal">
                        <span class="d-flex align-items-center">
                            <i class="bi-box-arrow-in-right me-2"></i>
                            <span class="small">Login</span>
                        </span>
                    </button>
                </div>
            </div>
        </nav>
        <!-- Mashead header-->
        <header class="masthead">
            <div class="container px-5">
                <div class="row gx-5 align-items-center justify-content-center justify-content-lg-between">
                    <div class="col-sm-8 col-md-5">
                        <!-- Features section device mockup-->
                        <div class="features-device-mockup">
                            <svg class="circle" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
                                <defs>
                                    <linearGradient id="circleGradient" gradientTransform="rotate(45)">
                                        <stop class="gradient-start-color" offset="0%"></stop>
                                        <stop class="gradient-end-color" offset="100%"></stop>
                                    </linearGradient>
                                </defs>
                                    <img class="img-fluid" src="assets/img/rural_logo_preto.png" alt="..." style="width: 100%; height: 100%; border-radius: 1%;"/>
                        </div>
                    </div>
                    <div class="col-12 col-lg-5">
                    <!-- Feature item-->
                    <div class="text-center">
                        <i class="bi-hypnotize icon-feature text-gradient d-block"></i>
                        <h3 class="font-alt">EstagHub</h3>
                        <p class="text-muted mb-1">Sistema de Gerenciamento de Estágio</p>
                        <button class="btn btn-primary rounded-pill px-3 mb-2 mb-lg-0" data-bs-toggle="modal" data-bs-target="#cadastroDiscenteModal">
                            <span class="d-flex align-items-center">
                                <i class="bi bi-person-add me-1"></i>
                                <span class="small">Discente</span>
                            </span>
                        </button>
                        <button class="btn btn-primary rounded-pill px-3 mb-2 mb-lg-0" data-bs-toggle="modal" data-bs-target="#cadastroSupervisorModal">
                            <span class="d-flex align-items-center">
                                <i class="bi bi-person-add me-1"></i>
                                <span class="small">Supervisor</span>
                            </span>
                        </button>
                    </div>
                </div>
                </div>
            </div>
        </header>

        <!-- Footer-->
        <footer class="bg-white text-center py-1">
            <div class="container px-5">
                <div class="text-black-50 small">
                    <div class="mt-2">&copy; EstagHub 2023. All Rights Reserved.</div>
                </div>
            </div>
        </footer>
        <!-- Login Modal-->
        <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="loginModalLabel">Login</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <form class="needs-validation" novalidate id="loginForm" action="principalController" name="loginForm" method="post">
                            <div style="text-align: center" class="mb-3">
                                <input type="radio" class="btn-check" name="loginOptions" id="discenteOption" value="discente" autocomplete="off" checked>
                                <label class="btn btn-outline-primary rounded-pill" for="discenteOption">Discente</label>
                                <input type="radio" class="btn-check" name="loginOptions" id="docenteOption" value="docente" autocomplete="off">
                                <label class="btn btn-outline-primary rounded-pill" for="docenteOption">Docente</label>
                                <input type="radio" class="btn-check" name="loginOptions" id="supervisorOption" value="supervisor" autocomplete="off">
                                <label class="btn btn-outline-primary rounded-pill" for="supervisorOption">Supervisor</label>
                            </div>
                            <div class="form-floating mb-3">
                                <input class="form-control" id="email" type="email" name="emailLogin" placeholder="name@example.com" required/>
                                <label for="email">Email</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe um email.
                                </div>
                            </div>
                            <div class="form-floating mb-3">
                                <input class="form-control" id="senha" type="password" name="senhaLogin" placeholder="Digite uma senha" required/>
                                <label for="senha">Senha</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe uma senha.
                                </div>
                            </div>
                            <div class="d-grid"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonLogin" name="submitButtonLogin"  value="login" type="submit">Enviar</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="cadastroDiscenteModal" tabindex="-1" aria-labelledby="cadastroDiscenteModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="cadastroDiscenteModalLabel">Criar Discente</h5>
                        <button class="btn-close btn-close-white" id="buttonCloseDiscente" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <div class="sw sw-theme-basic sw-justified">
                            <div class="tab-content">
                                <div class="tab-pane" style="display: block">
                                    <form class="needs-validation" novalidate id="discenteForm" name="discenteForm" action="principalController" method="post">
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="nomeDiscente" type="text" name="nomeDiscente" required placeholder="Digite seu nome completo"/>
                                            <label for="nomeDiscente">Nome Completo</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um nome.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="emailDiscente" type="email" name="emailDiscente" required placeholder="name@example.com"/>
                                            <label for="emailDiscente">Email</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um email.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="senhaDiscente" type="password" name="senhaDiscente" required placeholder="Digite sua senha"/>
                                            <label for="senhaDiscente">Senha</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe uma senha.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="matriculaDiscente" type="text" name="matriculaDiscente" required placeholder="Digite sua matrícula"/>
                                            <label for="matriculaDiscente">Matrícula</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe uma matrícula.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="telefoneDiscente" type="tel" name="telefoneDiscente" required placeholder="(00) 00000-0000" pattern="^\+?[0-9\s()-]{10,}$"/>
                                            <label for="telefoneDiscente">Telefone</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um telefone.
                                            </div>
                                        </div>
                                        <div role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" id="submitButtonDiscente" name="submitButtonDiscente" type="submit" value="discente">Enviar</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Cadastro Supervisor Modal-->
        <div class="modal fade" id="cadastroSupervisorModal" tabindex="-1" aria-labelledby="cadastroSupervisorModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="cadastroSupervisorModalLabel">Criar Supervisor</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <div id="smartwizard-supervisor">
                            <ul class="nav">
                                <li class="nav-item">
                                    <a class="nav-link" href="#step-1"></a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="#step-2"></a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="#step-3"></a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="#step-4"></a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div id="step-1" class="tab-pane" role="tabpanel" aria-labelledby="step-1">
                                    <form class="needs-validation" novalidate id="form-1">
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="nameSupervisor" type="text" required name="nameSupervisor" placeholder="Digite seu nome completo"/>
                                            <label for="nameSupervisor">Nome Completo</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um nome.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="emailSupervisor" type="email" required name="emailSupervisor" placeholder="name@example.com"/>
                                            <label for="emailSupervisor">Email</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um email.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="senhaSupervisor" type="password" required name="senhaSupervisor" placeholder="Digite sua senha"/>
                                            <label for="senhaSupervisor">Senha</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe uma senha.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="pedidoSupervisor" type="text" required name="pedidoSupervisor" placeholder="Digite o número do pedido"/>
                                            <label for="pedidoSupervisor">Número do pedido</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um pedido.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="cargoSupervisor" type="text" required name="cargoSupervisor" placeholder="Digita seu cargo"/>
                                            <label for="cargoSupervisor">Cargo</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um cargo.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="telefoneSupervisor" type="tel" required name="telefoneSupervisor" placeholder="(00) 00000-0000" pattern="^\+?[0-9\s()-]{10,}$"/>
                                            <label for="telefoneSupervisor">Telefone</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um telefone.
                                            </div>
                                        </div>
                                        <div class="toolbar" role="toolbar">
                                            <button class="btn btn-primary" disabled>Voltar</button>
                                            <button class="btn btn-primary" type="button" id="buttonNextSupervisor1">Próximo</button>
                                            <button class="btn btn-primary" disabled>Enviar</button>
                                        </div>
                                    </form>
                                </div>
                                <div id="step-2" class="tab-pane" role="tabpanel" aria-labelledby="step-2">
                                    <form class="needs-validation" novalidate id="form-2">
                                        <div class="d-grid gap-2 d-md-block form-floating mb-3" style="text-align: center">
                                            <button class="btn btn-primary" id="buttonVincularEmpresa" type="button" onclick="goToStepSupervisor(2)">Vincular Empresa</button>
                                            <button class="btn btn-primary" type="button" onclick="goToStepSupervisor(3)">Cadastrar Empresa</button>
                                        </div>
                                        <div class="toolbar" role="toolbar">
                                            <button class="btn btn-primary" type="button" id="buttonPreviousSupervisor2">Voltar</button>
                                            <button class="btn btn-primary" disabled>Próximo</button>
                                            <button class="btn btn-primary" disabled>Enviar</button>
                                        </div>
                                    </form>
                                </div>
                                <div id="step-3" class="tab-pane" role="tabpanel" aria-labelledby="step-3">
                                    <form class="needs-validation" novalidate id="form-3">
                                        <div class="form-floating mb-3">
                                            <select name="selectVinculoSupervisorEmpresa" class="form-select" id="selectVinculoSupervisorEmpresa" required>
                                                <option value="">Empresa - CNPJ</option>
                                                <c:forEach var="empresa" items="${LIST_EMPRESAS}">
                                                    <option value="${empresa.cnpj}">${empresa.nome} - ${empresa.cnpj}</option>
                                                </c:forEach>
                                            </select>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Escolha uma empresa.
                                            </div>
                                        </div>
                                        <div class="toolbar" role="toolbar">
                                            <button class="btn btn-primary" type="button" id="buttonPreviousSupervisor3">Voltar</button>
                                            <button class="btn btn-primary" type="button" id="buttonNextSupervisor3" disabled>Próximo</button>
                                            <button class="btn btn-primary" id="submitButtonSupervisor1" name="submitButtonSupervisor1" type="button" value="supervisor"  onclick="onConfirmSupervisor1()">Enviar</button>
                                        </div>
                                    </form>
                                </div>
                                <div id="step-4" class="tab-pane" role="tabpanel" aria-labelledby="step-4">
                                    <form class="needs-validation" novalidate id="form-4">
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="nomeEmpresa" type="text" required name="nomeEmpresa" placeholder="Digite o nome da empresa"/>
                                            <label for="nomeEmpresa">Nome da empresa</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um nome.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="cnpjEmpresa" type="text" required name="cnpjEmpresa" placeholder="Digite o cnpj da empresa"/>
                                            <label for="cnpjEmpresa">CNPJ da empresa</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um cnpj.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="emailEmpresa" type="email" required name="emailEmpresa" placeholder="name@example.com"/>
                                            <label for="emailEmpresa">Email da empresa</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um email.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="enderecoEmpresa" type="text" required name="enderecoEmpresa" placeholder="Digite o endereco da empresa"/>
                                            <label for="enderecoEmpresa">Endereço da empresa</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um endereço.
                                            </div>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input class="form-control" id="telefoneEmpresa" type="tel" required name="telefoneEmpresa" placeholder="(00) 00000-0000" pattern="^\+?[0-9\s()-]{10,}$"/>
                                            <label for="telefoneEmpresa">Telefone da empresa</label>
                                            <div class="valid-feedback">
                                                Perfeito!
                                            </div>
                                            <div class="invalid-feedback">
                                                Ops! Informe um telefone.
                                            </div>
                                        </div>
                                        <div class="toolbar" role="toolbar">
                                            <button class="btn btn-primary" type="button" id="buttonPreviousSupervisor4">Voltar</button>
                                            <button class="btn btn-primary" type="button" id="buttonNextSupervisor4" disabled>Próximo</button>
                                            <button class="btn btn-primary" id="submitButtonSupervisor2" name="submitButtonSupervisor2" type="button" value="supervisor" onclick="onConfirmSupervisor2()">Enviar</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>