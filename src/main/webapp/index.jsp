<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <meta charset="UTF-8">
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
        <!-- CSS Smart-->
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        <script>
            $(function() {
                const modals = {
                    'loginModal': 'submitButtonLogin',
                    'forgotPasswordModal': 'submitButtonForgotPassword',
                    'newPasswordModal': 'submitButtonNewPassword',
                    'cadastroDiscenteModal': 'submitButtonDiscente',
                    'confirmEmailDiscenteModal': 'submitButtonConfirmEmailDiscente',
                    'cadastroSupervisorModal': 'buttonNextSupervisor1',
                    'confirmEmailSupervisor1Modal': 'submitButtonConfirmEmailSupervisor1',
                    'confirmEmailSupervisor2Modal': 'submitButtonConfirmEmailSupervisor2'
                };
                for (const modalId in modals) {
                    $('#' + modalId).on('keypress', function (e) {
                        if ('13' == e.which) {
                            $('#' + modals[modalId]).click();
                        }
                    });
                }
                <c:if test="${UNAUTHORIZED}">
                    alert('Sua sessão foi expirada. É necessário autenticar-se novamente!');
                </c:if>
                $.fn.select2.defaults.set("width", "100%");
                $('#selectVinculoSupervisorEmpresa').select2({
                    dropdownParent: $('#cadastroSupervisorModalStep3')
                });
            });
            function onConfirmSupervisor1() {
                let form = document.getElementById('form-3');
                document.getElementById("form-4").reset();
                if (form) {
                    if (!form.checkValidity()) {
                        form.classList.add('was-validated');
                        return false;
                    }
                    $.ajax({
                        type: "POST",
                        url: "principalController",
                        cache: false,
                        data: {
                            submitButtonSupervisor1: $('button[id^=submitButtonSupervisor1]').val(),
                            emailSupervisor: $('input[id^=emailSupervisor]').val(),
                            selectVinculoSupervisorEmpresa: $('select[id^=selectVinculoSupervisorEmpresa]').val()
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.criarSupervisorComEmpresaVinculada) {
                                alert('Um código para confirmar o email foi enviado para o email do(a) supervisor(a) informado!');
                                $('div[id^=cadastroSupervisorModalStep3]').modal('hide');
                                $('div[id^=confirmEmailSupervisor1Modal]').modal('show');
                                $.ajax({
                                    type: "POST",
                                    url: "principalController",
                                    data: {
                                        sendEmail: 'confirmarEmail'
                                    }
                                });
                            }else{
                                alert(data.message);
                            }
                        },
                        error: function (xhr, status, error) {
                            alert('Erro em processar os dados!');
                        }
                    });
                }
            }
            function onConfirmSupervisor2() {
                let form = document.getElementById('form-4');
                document.getElementById("form-3").reset();
                if (form) {
                    if (!form.checkValidity()) {
                        form.classList.add('was-validated');
                        return false;
                    }
                    $.ajax({
                        type: "POST",
                        url: "principalController",
                        cache: false,
                        data: {
                            submitButtonSupervisor2: $('button[id^=submitButtonSupervisor2]').val(),
                            emailSupervisor: $('input[id^=emailSupervisor]').val(),
                            cnpjEmpresaSupervisor: $('input[id^=cnpjEmpresa]').val(),
                            emailEmpresa: $('input[id^=emailEmpresa]').val()
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.criarSupervisorComNovaEmpresa) {
                                alert('Um código para confirmar o email foi enviado para o email do(a) supervisor(a) informado!');
                                $('div[id^=cadastroSupervisorModalStep4]').modal('hide');
                                $('div[id^=confirmEmailSupervisor2Modal]').modal('show');
                                $.ajax({
                                    type: "POST",
                                    url: "principalController",
                                    data: {
                                        sendEmail: 'confirmarEmail'
                                    }
                                });
                            }else{
                                alert(data.message);
                            }
                        },
                        error: function (xhr, status, error) {
                            alert('Erro em processar os dados!');
                        }
                    });
                }
            }
            function onLogin(){
                let formLogin = document.getElementById('loginForm');
                if(formLogin) {
                    if (!formLogin.checkValidity()) {
                        formLogin.classList.add('was-validated');
                        return false;
                    }
                }
                $.ajax({
                    type: "POST",
                    url: "principalController",
                    cache: false,
                    data: {
                        submitButtonLogin: $('button[id^=submitButtonLogin]').val(),
                        loginOptions: $('input[name^=loginOptions]:checked').val(),
                        emailLogin: $('input[id^=emailLogin]').val(),
                        senhaLogin: $('input[id^=senhaLogin]').val()
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.discente) {
                            window.location.replace('discente.jsp');
                        }else if(data.docenteComissao){
                            window.location.replace('docenteComissao.jsp');
                        }else if(data.docente){
                            window.location.replace('docente.jsp');
                        }else if(data.supervisor){
                            window.location.replace('supervisor.jsp');
                        }else{
                            alert(data.message);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('Erro em processar os dados!');
                    }
                });
            }
            function onForgotPassword(){
                let formForgotPassword = document.getElementById('forgotPasswordForm');
                if(formForgotPassword) {
                    if (!formForgotPassword.checkValidity()) {
                        formForgotPassword.classList.add('was-validated');
                        return false;
                    }
                }
                $.ajax({
                    type: "POST",
                    url: "principalController",
                    cache: false,
                    data: {
                        submitButtonForgotPassword: $('button[id^=submitButtonForgotPassword]').val(),
                        loginForgotOptions: $('input[name^=loginForgotOptions]:checked').val(),
                        emailForgotLogin: $('input[id^=emailForgotLogin]').val()
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.forgotPassword) {
                            alert('Um código para resetar a sua senha foi enviado para o email informado!');
                            $('div[id^=forgotPasswordModal]').modal('hide');
                            document.getElementById('newPasswordForm').reset();
                            $('div[id^=newPasswordModal]').modal('show');
                            $.ajax({
                                type: "POST",
                                url: "principalController",
                                data: {
                                    sendEmail: 'confirmarEmail'
                                }
                            });
                        }else{
                            alert(data.message);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('Erro em processar os dados!');
                    }
                });
            }
            function onNewPassword(){
                let formNewPassword = document.getElementById('newPasswordForm');
                if(formNewPassword) {
                    if (!formNewPassword.checkValidity()) {
                        formNewPassword.classList.add('was-validated');
                        return false;
                    }
                }
                $.ajax({
                    type: "POST",
                    url: "principalController",
                    cache: false,
                    data: {
                        submitButtonNewPassword: $('button[id^=submitButtonNewPassword]').val(),
                        otp: $('input[id^=otp]').val(),
                        senhaNewLogin: $('input[id^=senhaNewLogin]').val(),
                        confirmarSenhaNewLogin: $('input[id^=confirmarSenhaNewLogin]').val()
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.newPassword) {
                            alert("Senha alterada com sucesso!");
                            $('div[id^=newPasswordModal]').modal('hide');
                            document.getElementById('loginForm').reset();
                            $('div[id^=loginModal]').modal('show');
                        }else{
                            alert(data.message);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('Erro em processar os dados!');
                    }
                });
            }
            function onCreateDiscente(){
                let formCreateDiscente = document.getElementById('discenteForm');
                if(formCreateDiscente) {
                    if (!formCreateDiscente.checkValidity()) {
                        formCreateDiscente.classList.add('was-validated');
                        return false;
                    }
                }
                $.ajax({
                    type: "POST",
                    url: "principalController",
                    cache: false,
                    data: {
                        submitButtonDiscente: 'confirmarEmail',
                        emailDiscente: $('input[id^=emailDiscente]').val(),
                        matriculaDiscente: $('input[id^=matriculaDiscente]').val()
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.confirmarEmail) {
                            alert('Um código para confirmar o email foi enviado para o email informado!');
                            $('div[id^=cadastroDiscenteModal]').modal('hide');
                            $('div[id^=confirmEmailDiscenteModal]').modal('show');
                            $.ajax({
                                type: "POST",
                                url: "principalController",
                                data: {
                                    sendEmail: 'confirmarEmail'
                                }
                            });
                        }else{
                            alert(data.message);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('Erro em processar os dados!');
                    }
                });
            }
            function onConfirmEmailDiscente(){
                let formConfirmEmailDiscente = document.getElementById('confirmEmailDiscenteForm');
                if(formConfirmEmailDiscente) {
                    if (!formConfirmEmailDiscente.checkValidity()) {
                        formConfirmEmailDiscente.classList.add('was-validated');
                        return false;
                    }
                }
                $.ajax({
                    type: "POST",
                    url: "principalController",
                    cache: false,
                    data: {
                        submitButtonDiscente: 'discente',
                        nomeDiscente: $('input[id^=nomeDiscente]').val(),
                        emailDiscente: $('input[id^=emailDiscente]').val(),
                        senhaDiscente: $('input[id^=senhaDiscente]').val(),
                        matriculaDiscente: $('input[id^=matriculaDiscente]').val(),
                        telefoneDiscente: $('input[id^=telefoneDiscente]').val(),
                        otpEmail: $('input[id^=otpEmail]').val()
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.criarDiscente) {
                            alert('Discente criado com sucesso!');
                            window.location.replace('index.jsp');
                        }else{
                            alert(data.message);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('Erro em processar os dados!');
                    }
                });
            }
            function onConfirmEmailSupervisor1(){
                let formConfirmEmailSupervisor1 = document.getElementById('confirmEmailSupervisor1Form');
                if(formConfirmEmailSupervisor1) {
                    if (!formConfirmEmailSupervisor1.checkValidity()) {
                        formConfirmEmailSupervisor1.classList.add('was-validated');
                        return false;
                    }
                }
                $.ajax({
                    type: "POST",
                    url: "principalController",
                    cache: false,
                    data: {
                        submitButtonEmailSupervisor1: 'confirmarEmail',
                        nameSupervisor: $('input[id^=nameSupervisor]').val(),
                        emailSupervisor: $('input[id^=emailSupervisor]').val(),
                        senhaSupervisor: $('input[id^=senhaSupervisor]').val(),
                        cargoSupervisor: $('input[id^=cargoSupervisor]').val(),
                        telefoneSupervisor: $('input[id^=telefoneSupervisor]').val(),
                        selectVinculoSupervisorEmpresa: $('select[id^=selectVinculoSupervisorEmpresa]').val(),
                        otpEmail: $('input[id^=otpEmailSupervisor1]').val()
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.criarSupervisorComEmpresaVinculada) {
                            alert('Supervisor criado com sucesso!');
                            window.location.replace('index.jsp');
                        }else{
                            alert(data.message);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('Erro em processar os dados!');
                    }
                });
            }
            function onConfirmEmailSupervisor2(){
                let formConfirmEmailSupervisor2 = document.getElementById('confirmEmailSupervisor2Form');
                if(formConfirmEmailSupervisor2) {
                    if (!formConfirmEmailSupervisor2.checkValidity()) {
                        formConfirmEmailSupervisor2.classList.add('was-validated');
                        return false;
                    }
                }
                $.ajax({
                    type: "POST",
                    url: "principalController",
                    cache: false,
                    data: {
                        submitButtonEmailSupervisor2: 'confirmarEmail',
                        nameSupervisor: $('input[id^=nameSupervisor]').val(),
                        emailSupervisor: $('input[id^=emailSupervisor]').val(),
                        senhaSupervisor: $('input[id^=senhaSupervisor]').val(),
                        cargoSupervisor: $('input[id^=cargoSupervisor]').val(),
                        telefoneSupervisor: $('input[id^=telefoneSupervisor]').val(),
                        nomeEmpresaSupervisor: $('input[id^=nomeEmpresa]').val(),
                        cnpjEmpresaSupervisor: $('input[id^=cnpjEmpresa]').val(),
                        enderecoEmpresa: $('input[id^=enderecoEmpresa]').val(),
                        emailEmpresa: $('input[id^=emailEmpresa]').val(),
                        telefoneEmpresa: $('input[id^=telefoneEmpresa]').val(),
                        otpEmail: $('input[id^=otpEmailSupervisor2]').val()
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.criarSupervisorComNovaEmpresa) {
                            alert('Supervisor criado com sucesso!');
                            window.location.replace('index.jsp');
                        }else{
                            alert(data.message);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('Erro em processar os dados!');
                    }
                });
            }
            function onCreateSupervisorStep1(){
                let form1Supervisor = document.getElementById('form-1');
                if(form1Supervisor) {
                    if (!form1Supervisor.checkValidity()) {
                        form1Supervisor.classList.add('was-validated');
                        return false;
                    }
                }
                $.ajax({
                    type: "POST",
                    url: "principalController",
                    cache: false,
                    data: {
                        checkEmailSupervisor: 'true',
                        emailSupervisor: $('input[id^=emailSupervisor]').val()
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.checkedEmailSupervisor) {
                            $('div[id^=cadastroSupervisorModal]').modal('hide');
                            $('div[id^=cadastroSupervisorModalStep2]').modal('show');
                        }else{
                            alert(data.message);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('Erro em processar os dados!');
                    }
                });
            }
            function onPressResetForms(){
                const formIds = [
                    'loginForm',
                    'forgotPasswordForm',
                    'newPasswordForm',
                    'discenteForm',
                    'confirmEmailDiscenteForm',
                    'confirmEmailSupervisor1Form',
                    'confirmEmailSupervisor2Form',
                    'form-1',
                    'form-2',
                    'form-3',
                    'form-4'
                ];

                formIds.forEach(formId => {
                    document.getElementById(formId).reset();
                    let form = document.getElementById(formId);
                    if (!form.checkValidity()) {
                        form.classList.remove('was-validated');
                    }
                });
            }
        </script>
        <style>
            .btn-group input[type="radio"]:checked + label {
                background-color: #007bff;
                color: #fff;
            }
            .select2-container .select2-selection--single {
                height: calc(3.5rem + 2px);
                padding: 0.85rem 2.25rem 0 0.75rem;
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
                        <span class="d-flex align-items-center" onclick="onPressResetForms()">
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
                            <svg class="circle" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg"/>
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
                            <span class="d-flex align-items-center" onclick="onPressResetForms()">
                                <i class="bi bi-person-add me-1"></i>
                                <span class="small">Discente</span>
                            </span>
                        </button>
                        <button class="btn btn-primary rounded-pill px-3 mb-2 mb-lg-0" data-bs-toggle="modal" data-bs-target="#cadastroSupervisorModal">
                            <span class="d-flex align-items-center" onclick="onPressResetForms()">
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
                    <a href="https://github.com/AlianPro/EstagHub" target="_blank">
                        <i class="btn bi bi-github"></i>
                    </a>
                    <div>
                        &copy; EstagHub 2023. All Rights Reserved.
                    </div>
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
                        <form class="needs-validation" novalidate id="loginForm">
                            <div class="mb-3" style="text-align: center">
                                <input type="radio" class="btn-check" name="loginOptions" id="discenteOption" value="discente" autocomplete="off" required checked>
                                <label class="btn btn-outline-primary rounded-pill" for="discenteOption">Discente</label>
                                <input type="radio" class="btn-check" name="loginOptions" id="docenteOption" value="docente" autocomplete="off" required>
                                <label class="btn btn-outline-primary rounded-pill" for="docenteOption">Docente</label>
                                <input type="radio" class="btn-check" name="loginOptions" id="supervisorOption" value="supervisor" autocomplete="off" required>
                                <label class="btn btn-outline-primary rounded-pill" for="supervisorOption">Supervisor</label>
                            </div>
                            <div class="form-floating mb-3">
                                <input class="form-control" id="emailLogin" type="email" name="emailLogin" placeholder="name@example.com" required/>
                                <label for="emailLogin">Email</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe um email.
                                </div>
                            </div>
                            <div class="form-floating mb-3">
                                <input class="form-control" id="senhaLogin" type="password" name="senhaLogin" placeholder="Digite uma senha" required/>
                                <label for="senhaLogin">Senha</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe uma senha.
                                </div>
                            </div>
                            <div class="d-grid mb-3"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonLogin" name="submitButtonLogin" value="login" type="button" onclick="onLogin()">Enviar</button></div>
                            <div class="text-center">
                                <a class="btn" style="font-size: 0.875em; color:#2937f0" data-bs-toggle="modal" data-bs-target="#forgotPasswordModal" onclick="document.getElementById('forgotPasswordForm').reset()">Esqueceu a senha?</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Forgot Password Modal-->
        <div class="modal fade" id="forgotPasswordModal" tabindex="-1" aria-labelledby="forgotPasswordModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="forgotPasswordModalLabel">Esqueceu a senha?</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <p class="text-center small mb-3" style="color: gray">Entendemos, imprevistos acontecem. Apenas digite seu endereço de email abaixo e enviaremos um email com um código para redefinir sua senha!</p>
                        <hr>
                        <form class="needs-validation" novalidate id="forgotPasswordForm">
                            <div class="mb-3" style="text-align: center">
                                <input type="radio" class="btn-check" name="loginForgotOptions" id="discenteForgotOption" value="discente" autocomplete="off" required checked>
                                <label class="btn btn-outline-primary rounded-pill" for="discenteForgotOption">Discente</label>
                                <input type="radio" class="btn-check" name="loginForgotOptions" id="docenteForgotOption" value="docente" autocomplete="off" required>
                                <label class="btn btn-outline-primary rounded-pill" for="docenteForgotOption">Docente</label>
                                <input type="radio" class="btn-check" name="loginForgotOptions" id="supervisorForgotOption" value="supervisor" autocomplete="off" required>
                                <label class="btn btn-outline-primary rounded-pill" for="supervisorForgotOption">Supervisor</label>
                            </div>
                            <div class="form-floating mb-3">
                                <input class="form-control" id="emailForgotLogin" type="email" name="emailForgotLogin" placeholder="name@example.com" required/>
                                <label for="emailForgotLogin">Email</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe um email.
                                </div>
                            </div>
                            <div class="d-grid mb-3"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonForgotPassword" name="submitButtonForgotPassword" value="forgotPassword" type="button" onclick="onForgotPassword()">Enviar</button></div>
                            <div class="text-center">
                                <a class="btn" style="font-size: 0.875em; color:#2937f0" data-bs-toggle="modal" data-bs-target="#loginModal" onclick="document.getElementById('loginForm').reset()">Realizar Login!</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- New Password Modal-->
        <div class="modal fade" id="newPasswordModal" tabindex="-1" aria-labelledby="newPasswordModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="newPasswordModalLabel">Nova senha</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <form class="needs-validation" novalidate id="newPasswordForm">
                            <div class="form-floating mb-3">
                                <input class="form-control" id="otp" type="text" name="otp" placeholder="Digite o código enviado para o seu email" required/>
                                <label for="otp">Código</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe o código enviado para o seu email.
                                </div>
                            </div>
                            <div class="form-floating mb-3">
                                <input class="form-control" id="senhaNewLogin" type="password" name="senhaNewLogin" placeholder="Digite uma senha" required/>
                                <label for="senhaNewLogin">Senha</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe uma senha.
                                </div>
                            </div>
                            <div class="form-floating mb-3">
                                <input class="form-control" id="confirmarSenhaNewLogin" type="password" name="confirmarSenhaNewLogin" placeholder="Digite a senha novamente para confirmar" required/>
                                <label for="confirmarSenhaNewLogin">Senha novamente</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe a senha novamente para confirmar.
                                </div>
                            </div>
                            <div class="d-grid"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonNewPassword" name="submitButtonNewPassword" value="newPassword" type="button" onclick="onNewPassword()">Enviar</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Confirm Email Discente Modal-->
        <div class="modal fade" id="confirmEmailDiscenteModal" tabindex="-1" aria-labelledby="confirmEmailDiscenteModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="confirmEmailDiscenteModalLabel">Confirmar email</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <form class="needs-validation" novalidate id="confirmEmailDiscenteForm">
                            <div class="form-floating mb-3">
                                <input class="form-control" id="otpEmailDiscente" type="text" name="otpEmailDiscente" placeholder="Digite o código enviado para o seu email" required/>
                                <label for="otpEmailDiscente">Código</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe o código enviado para o seu email.
                                </div>
                            </div>
                            <div class="d-grid"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonConfirmEmailDiscente" name="submitButtonConfirmEmailDiscente" value="confirmEmailDiscente" type="button" onclick="onConfirmEmailDiscente()">Enviar</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Confirm Email Supervisor 1 Modal-->
        <div class="modal fade" id="confirmEmailSupervisor1Modal" tabindex="-1" aria-labelledby="confirmEmailSupervisor1ModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="confirmEmailSupervisor1ModalLabel">Confirmar email</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <form class="needs-validation" novalidate id="confirmEmailSupervisor1Form">
                            <div class="form-floating mb-3">
                                <input class="form-control" id="otpEmailSupervisor1" type="text" name="otpEmailSupervisor1" placeholder="Digite o código enviado para o seu email" required/>
                                <label for="otpEmailDiscente">Código</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe o código enviado para o seu email.
                                </div>
                            </div>
                            <div class="d-grid"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonConfirmEmailSupervisor1" name="submitButtonConfirmEmailSupervisor1" value="confirmEmailSupervisor1" type="button" onclick="onConfirmEmailSupervisor1()">Enviar</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Confirm Email Supervisor 2 Modal-->
        <div class="modal fade" id="confirmEmailSupervisor2Modal" tabindex="-1" aria-labelledby="confirmEmailSupervisor2ModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="confirmEmailSupervisor2ModalLabel">Confirmar email</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <form class="needs-validation" novalidate id="confirmEmailSupervisor2Form">
                            <div class="form-floating mb-3">
                                <input class="form-control" id="otpEmailSupervisor2" type="text" name="otpEmailSupervisor2" placeholder="Digite o código enviado para o seu email" required/>
                                <label for="otpEmailDiscente">Código</label>
                                <div class="valid-feedback">
                                    Perfeito!
                                </div>
                                <div class="invalid-feedback">
                                    Ops! Informe o código enviado para o seu email.
                                </div>
                            </div>
                            <div class="d-grid"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonConfirmEmailSupervisor2" name="submitButtonConfirmEmailSupervisor2" value="confirmEmailSupervisor2" type="button" onclick="onConfirmEmailSupervisor2()">Enviar</button></div>
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
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <div class="sw sw-theme-basic sw-justified">
                            <div class="tab-content">
                                <div class="tab-pane" style="display: block">
                                    <form class="needs-validation" novalidate id="discenteForm">
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
                                            <button class="btn btn-primary" id="submitButtonDiscente" name="submitButtonDiscente" type="button" onclick="onCreateDiscente()">Enviar</button>
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
                        <h5 class="modal-title font-alt text-white" id="cadastroSupervisorModalLabel">Criar Supervisor(a)</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <div class="sw sw-theme-basic sw-justified">
                            <div class="tab-content">
                                <div class="tab-pane" style="display: block">
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
                                        <div class="toolbar" role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" type="button" id="buttonNextSupervisor1" onclick="onCreateSupervisorStep1()">Próximo</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Cadastro Supervisor Step 2 Modal-->
        <div class="modal fade" id="cadastroSupervisorModalStep2" tabindex="-1" aria-labelledby="cadastroSupervisorModalLabelStep2" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="cadastroSupervisorModalLabelStep2">Criar Supervisor(a)</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <div class="sw sw-theme-basic sw-justified">
                            <div class="tab-content">
                                <div class="tab-pane" style="display: block">
                                    <form class="needs-validation" novalidate id="form-2">
                                        <div class="d-grid gap-2 d-md-block form-floating mb-3" style="text-align: center">
                                            <c:if test="${not empty LIST_EMPRESAS}">
                                                <button class="btn btn-primary" id="buttonVincularEmpresa" type="button" data-bs-toggle="modal" data-bs-target="#cadastroSupervisorModalStep3">Vincular Empresa</button>
                                            </c:if>
                                            <button class="btn btn-primary" type="button" data-bs-toggle="modal" data-bs-target="#cadastroSupervisorModalStep4">Cadastrar Empresa</button>
                                        </div>
                                        <div class="toolbar" role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" type="button" id="buttonPreviousSupervisor2" data-bs-toggle="modal" data-bs-target="#cadastroSupervisorModal">Voltar</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Cadastro Supervisor Step 3 Modal-->
        <div class="modal fade" id="cadastroSupervisorModalStep3" tabindex="-1" aria-labelledby="cadastroSupervisorModalLabelStep3" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="cadastroSupervisorModalLabelStep3">Criar Supervisor(a)</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <div class="sw sw-theme-basic sw-justified">
                            <div class="tab-content">
                                <div class="tab-pane" style="display: block">
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
                                        <div class="toolbar" role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" type="button" id="buttonPreviousSupervisor3" data-bs-toggle="modal" data-bs-target="#cadastroSupervisorModalStep2">Voltar</button>
                                            <button class="btn btn-primary" id="submitButtonSupervisor1" name="submitButtonSupervisor1" type="button" value="supervisor" onclick="onConfirmSupervisor1()">Enviar</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Cadastro Supervisor Step 4 Modal-->
        <div class="modal fade" id="cadastroSupervisorModalStep4" tabindex="-1" aria-labelledby="cadastroSupervisorModalLabelStep4" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary-to-secondary p-4">
                        <h5 class="modal-title font-alt text-white" id="cadastroSupervisorModalLabelStep4">Criar Supervisor(a)</h5>
                        <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body border-0 p-4">
                        <div class="sw sw-theme-basic sw-justified">
                            <div class="tab-content">
                                <div class="tab-pane" style="display: block">
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
                                        <div class="toolbar" role="toolbar" style="text-align: right">
                                            <button class="btn btn-primary" type="button" id="buttonPreviousSupervisor4" data-bs-toggle="modal" data-bs-target="#cadastroSupervisorModalStep2">Voltar</button>
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