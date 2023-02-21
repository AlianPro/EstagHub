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
    <link rel="icon" type="image/x-icon" href="assets/img/favicon.ico" />
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
            <ul class="navbar-nav ms-auto me-3 my-3 my-lg-0">
                <li class="nav-item">
                    <a class="nav-link me-lg-0" href="#sobre">Sobre</a>
                </li>
            </ul>
            <button class="btn btn-primary rounded-pill px-3 mb-2 mb-lg-0" data-bs-toggle="modal" data-bs-target="#loginModal">
                <span class="d-flex align-items-center">
                    <i class="bi-chat-text-fill me-2"></i>
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
            <div class="col-sm-8 col-md-6">
                <!-- Features section device mockup-->
                <div class="features-device-mockup">
                    <svg class="circle" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
                        <defs>
                            <linearGradient id="circleGradient" gradientTransform="rotate(45)">
                                <stop class="gradient-start-color" offset="0%"></stop>
                                <stop class="gradient-end-color" offset="100%"></stop>
                            </linearGradient>
                        </defs>
<%--                        <circle cx="50" cy="50" r="50"></circle></svg--%>
<%--                    ><svg class="shape-1 d-none d-sm-block" viewBox="0 0 240.83 240.83" xmlns="http://www.w3.org/2000/svg">--%>
<%--                    <rect x="-32.54" y="78.39" width="305.92" height="84.05" rx="42.03" transform="translate(120.42 -49.88) rotate(45)"></rect>--%>
<%--                    <rect x="-32.54" y="78.39" width="305.92" height="84.05" rx="42.03" transform="translate(-49.88 120.42) rotate(-45)"></rect></svg--%>
<%--                ><svg class="shape-2 d-none d-sm-block" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg"><circle cx="50" cy="50" r="50"></circle></svg>--%>
<%--                    <div class="device-wrapper">--%>
<%--                        <div class="device" data-device="iPhoneX" data-orientation="portrait" data-color="black">--%>
<%--                            <div class="screen bg-black">--%>
                                <!-- PUT CONTENTS HERE:-->
                                <!-- * * This can be a video, image, or just about anything else.-->
                                <!-- * * Set the max width of your media to 100% and the height to-->
                                <!-- * * 100% like the demo example below.-->
<%--                        <div class="col-sm-8 col-md-6">--%>
                            <img class="img-fluid rounded-circle" src="https://source.unsplash.com/u8Jn2rzYIps/900x900" alt="..." style="width: 100%; height: 100%"/>
<%--                        </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
                </div>
            </div>
            <div class="col-12 col-lg-5">
            <!-- Feature item-->
            <div class="text-center">
                <i class="bi-phone icon-feature text-gradient d-block mb-3"></i>
                <h3 class="font-alt">EstagHub</h3>
                <p class="text-muted mb-1">Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of</p>
                <button class="btn btn-primary rounded-pill px-3 mb-2 mb-lg-0" data-bs-toggle="modal" data-bs-target="#cadastroDiscenteModal">
                    <span class="d-flex align-items-center">
                        <i class="bi-chat-text-fill me-2"></i>
                        <span class="small">Discente</span>
                    </span>
                </button>
                <button class="btn btn-primary rounded-pill px-3 mb-2 mb-lg-0" data-bs-toggle="modal" data-bs-target="#cadastroSupervisorModal">
                    <span class="d-flex align-items-center">
                        <i class="bi-chat-text-fill me-2"></i>
                        <span class="small">Supervisor</span>
                    </span>
                </button>
            </div>
        </div>
        </div>
    </div>
</header>
<!-- Quote/testimonial aside-->
<aside class="text-center bg-gradient-primary-to-secondary">
    <div class="container px-5">
        <div class="row gx-5 justify-content-center">
            <div class="col-xl-8">
                <div class="h2 fs-1 text-white mb-4">"There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form"</div>
                <img src="assets/img/tnw-logo.svg" alt="..." style="height: 3rem" />
            </div>
        </div>
    </div>
</aside>
<!-- App features section-->
<%--<section id="features">--%>
<%--    <div class="container px-5">--%>
<%--        <div class="row gx-5 align-items-center">--%>
<%--            <div class="col-lg-8 order-lg-1 mb-5 mb-lg-0">--%>
<%--                <div class="container-fluid px-5">--%>
<%--                    <div class="row gx-5">--%>
<%--                        <div class="col-md-6 mb-5">--%>
<%--                            <!-- Feature item-->--%>
<%--                            <div class="text-center">--%>
<%--                                <i class="bi-phone icon-feature text-gradient d-block mb-3"></i>--%>
<%--                                <h3 class="font-alt">Device Mockups</h3>--%>
<%--                                <p class="text-muted mb-0">Ready to use HTML/CSS device mockups, no Photoshop required!</p>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                        <div class="col-md-6 mb-5">--%>
<%--                            <!-- Feature item-->--%>
<%--                            <div class="text-center">--%>
<%--                                <i class="bi-camera icon-feature text-gradient d-block mb-3"></i>--%>
<%--                                <h3 class="font-alt">Flexible Use</h3>--%>
<%--                                <p class="text-muted mb-0">Put an image, video, animation, or anything else in the screen!</p>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                    <div class="row">--%>
<%--                        <div class="col-md-6 mb-5 mb-md-0">--%>
<%--                            <!-- Feature item-->--%>
<%--                            <div class="text-center">--%>
<%--                                <i class="bi-gift icon-feature text-gradient d-block mb-3"></i>--%>
<%--                                <h3 class="font-alt">Free to Use</h3>--%>
<%--                                <p class="text-muted mb-0">As always, this theme is free to download and use for any purpose!</p>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                        <div class="col-md-6">--%>
<%--                            <!-- Feature item-->--%>
<%--                            <div class="text-center">--%>
<%--                                <i class="bi-patch-check icon-feature text-gradient d-block mb-3"></i>--%>
<%--                                <h3 class="font-alt">Open Source</h3>--%>
<%--                                <p class="text-muted mb-0">Since this theme is MIT licensed, you can use it commercially!</p>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div class="col-lg-4 order-lg-0">--%>
<%--                <!-- Features section device mockup-->--%>
<%--                <div class="features-device-mockup">--%>
<%--                    <svg class="circle" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">--%>
<%--                        <defs>--%>
<%--                            <linearGradient id="circleGradient" gradientTransform="rotate(45)">--%>
<%--                                <stop class="gradient-start-color" offset="0%"></stop>--%>
<%--                                <stop class="gradient-end-color" offset="100%"></stop>--%>
<%--                            </linearGradient>--%>
<%--                        </defs>--%>
<%--                        <circle cx="50" cy="50" r="50"></circle></svg--%>
<%--                    ><svg class="shape-1 d-none d-sm-block" viewBox="0 0 240.83 240.83" xmlns="http://www.w3.org/2000/svg">--%>
<%--                    <rect x="-32.54" y="78.39" width="305.92" height="84.05" rx="42.03" transform="translate(120.42 -49.88) rotate(45)"></rect>--%>
<%--                    <rect x="-32.54" y="78.39" width="305.92" height="84.05" rx="42.03" transform="translate(-49.88 120.42) rotate(-45)"></rect></svg--%>
<%--                ><svg class="shape-2 d-none d-sm-block" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg"><circle cx="50" cy="50" r="50"></circle></svg>--%>
<%--                    <div class="device-wrapper">--%>
<%--                        <div class="device" data-device="iPhoneX" data-orientation="portrait" data-color="black">--%>
<%--                            <div class="screen bg-black">--%>
<%--                                <!-- PUT CONTENTS HERE:-->--%>
<%--                                <!-- * * This can be a video, image, or just about anything else.-->--%>
<%--                                <!-- * * Set the max width of your media to 100% and the height to-->--%>
<%--                                <!-- * * 100% like the demo example below.-->--%>
<%--                                <video muted="muted" autoplay="" loop="" style="max-width: 100%; height: 100%"><source src="assets/img/demo-screen.mp4" type="video/mp4" /></video>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</section>--%>
<!-- Basic features section-->
<section class="bg-light" id="sobre">
    <div class="container px-5">
        <div class="row gx-5 align-items-center justify-content-center justify-content-lg-between">
            <div class="col-12 col-lg-5">
                <h2 class="display-4 lh-1 mb-4">Where does it come from</h2>
                <p class="lead fw-normal text-muted mb-5 mb-lg-0">Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of</p>
            </div>
            <div class="col-sm-8 col-md-6">
                <div class="px-5 px-sm-0"><img class="img-fluid rounded-circle" src="https://source.unsplash.com/u8Jn2rzYIps/900x900" alt="..." /></div>
            </div>
        </div>
    </div>
</section>
<!-- Call to action section-->
<%--<section class="cta">--%>
<%--    <div class="cta-content">--%>
<%--        <div class="container px-5">--%>
<%--            <h2 class="text-white display-1 lh-1 mb-4">--%>
<%--                Stop waiting.--%>
<%--                <br />--%>
<%--                Start building.--%>
<%--            </h2>--%>
<%--            <a class="btn btn-outline-light py-3 px-4 rounded-pill" href="https://startbootstrap.com/theme/new-age" target="_blank">Download for free</a>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</section>--%>
<!-- App badge section-->
<%--<section class="bg-gradient-primary-to-secondary" id="download">--%>
<%--    <div class="container px-5">--%>
<%--        <h2 class="text-center text-white font-alt mb-4">Get the app now!</h2>--%>
<%--        <div class="d-flex flex-column flex-lg-row align-items-center justify-content-center">--%>
<%--            <a class="me-lg-3 mb-4 mb-lg-0" href="#!"><img class="app-badge" src="assets/img/google-play-badge.svg" alt="..." /></a>--%>
<%--            <a href="#!"><img class="app-badge" src="assets/img/app-store-badge.svg" alt="..." /></a>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</section>--%>
<!-- Footer-->
<footer class="bg-black text-center py-5">
    <div class="container px-5">
        <div class="text-white-50 small">
            <div class="mb-2">&copy; EstagHub 2023. All Rights Reserved.</div>
            <a href="#!">Privacy</a>
            <span class="mx-1">&middot;</span>
            <a href="#!">Terms</a>
            <span class="mx-1">&middot;</span>
            <a href="#!">FAQ</a>
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
<!-- Cadastro Discente Modal-->
<%--<div class="modal fade" id="cadastroDiscenteModal" tabindex="-1" aria-labelledby="cadastroDiscenteModalLabel" aria-hidden="true">--%>
<%--    <div class="modal-dialog modal-dialog-centered">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header bg-gradient-primary-to-secondary p-4">--%>
<%--                <h5 class="modal-title font-alt text-white" id="cadastroDiscenteModalLabel">Cadastrar Discente</h5>--%>
<%--                <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>--%>
<%--            </div>--%>
<%--            <div class="modal-body border-0 p-4">--%>
<%--                <form id="discenteForm" action="principalController" name="discenteForm" method="post">--%>
<%--                    <input hidden="hidden" name="discenteInputForm" value="discente"/>--%>
<%--                    <div class="form-floating mb-3">--%>
<%--                        <input class="form-control" id="nameDiscente" type="text" name="nomeDiscente" placeholder="Digite seu nome completo"/>--%>
<%--                        <label for="nameDiscente">Nome Completo</label>--%>
<%--                        <div class="invalid-login">A name is required.</div>--%>
<%--                    </div>--%>
<%--                    <div class="form-floating mb-3">--%>
<%--                        <input class="form-control" id="emailDiscente" type="email" name="emailDiscente" placeholder="name@example.com"/>--%>
<%--                        <label for="emailDiscente">Email</label>--%>
<%--                        <div class="invalid-login">An email is required.</div>--%>
<%--                        <div class="invalid-login">Email is not valid.</div>--%>
<%--                    </div>--%>
<%--                    <div class="form-floating mb-3">--%>
<%--                        <input class="form-control" id="senhaDiscente" type="password" name="senhaDiscente" placeholder="Digite sua senha"/>--%>
<%--                        <label for="senhaDiscente">Senha</label>--%>
<%--                        <div class="invalid-login">An email is required.</div>--%>
<%--                        <div class="invalid-login">Email is not valid.</div>--%>
<%--                    </div>--%>
<%--                    <div class="form-floating mb-3">--%>
<%--                        <input class="form-control" id="matriculaDiscente" type="text" name="matriculaDiscente" placeholder="Digite sua matrícula"/>--%>
<%--                        <label for="matriculaDiscente">Matrícula</label>--%>
<%--                        <div class="invalid-login">An email is required.</div>--%>
<%--                        <div class="invalid-login">Email is not valid.</div>--%>
<%--                    </div>--%>
<%--                    <div class="form-floating mb-3">--%>
<%--                        <input class="form-control" id="telefoneDiscente" type="tel" name="telefoneDiscente" placeholder="(00) 00000-0000"/>--%>
<%--                        <label for="telefoneDiscente">Telefone</label>--%>
<%--                        <div class="invalid-login">A phone number is required.</div>--%>
<%--                    </div>--%>
<%--                    <div class="d-none" id="submitSuccessMessageDiscente">--%>
<%--                        <div class="text-center mb-3">--%>
<%--                            <div class="fw-bolder">Form submission successful!</div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                    <div class="d-none" id="submitErrorMessageDiscente"><div class="text-center text-danger mb-3">Error sending message!</div></div>--%>
<%--                    <div class="d-grid"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonDiscente" name="submitButtonDiscente" value="discente" type="submit">Enviar</button></div>--%>
<%--                </form>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>
<!-- akiiiiiii-->
<div class="modal fade" id="cadastroDiscenteModal" tabindex="-1" aria-labelledby="cadastroDiscenteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-gradient-primary-to-secondary p-4">
                <h5 class="modal-title font-alt text-white" id="cadastroDiscenteModalLabel">Cadastrar Discente</h5>
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
                                    <input class="form-control" id="telefoneDiscente" type="tel" name="telefoneDiscente" required placeholder="(00) 00000-0000"/>
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

<!-- akiiiiiii-->
<!-- Cadastro Supervisor Modal-->
<div class="modal fade" id="cadastroSupervisorModal" tabindex="-1" aria-labelledby="cadastroSupervisorModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-gradient-primary-to-secondary p-4">
                <h5 class="modal-title font-alt text-white" id="cadastroSupervisorModalLabel">Cadastrar Supervisor</h5>
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
                                    <input class="form-control" id="telefoneSupervisor" type="tel" required name="telefoneSupervisor" placeholder="(00) 00000-0000"/>
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
                                    <input class="form-control" id="telefoneEmpresa" type="tel" required name="telefoneEmpresa" placeholder="(00) 00000-0000"/>
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
<!-- SmartWizard html -->
<%--<div id="smartwizard">--%>
<%--    <ul class="nav">--%>
<%--        <li class="nav-item">--%>
<%--            <a class="nav-link" href="#step-1">--%>
<%--                <div class="num">1</div>--%>
<%--                Step Title--%>
<%--            </a>--%>
<%--        </li>--%>
<%--        <li class="nav-item">--%>
<%--            <a class="nav-link" href="#step-2">--%>
<%--                <span class="num">2</span>--%>
<%--                Step Title--%>
<%--            </a>--%>
<%--        </li>--%>
<%--        <li class="nav-item">--%>
<%--            <a class="nav-link" href="#step-3">--%>
<%--                <span class="num">3</span>--%>
<%--                Step Title--%>
<%--            </a>--%>
<%--        </li>--%>
<%--        <li class="nav-item">--%>
<%--            <a class="nav-link " href="#step-4">--%>
<%--                <span class="num">4</span>--%>
<%--                Step Title--%>
<%--            </a>--%>
<%--        </li>--%>
<%--    </ul>--%>

<%--    <div class="tab-content">--%>
<%--        <div id="step-1" class="tab-pane" role="tabpanel" aria-labelledby="step-1">--%>
<%--            Step content--%>
<%--        </div>--%>
<%--        <div id="step-2" class="tab-pane" role="tabpanel" aria-labelledby="step-2">--%>
<%--            Step content--%>
<%--        </div>--%>
<%--        <div id="step-3" class="tab-pane" role="tabpanel" aria-labelledby="step-3">--%>
<%--            Step content--%>
<%--        </div>--%>
<%--        <div id="step-4" class="tab-pane" role="tabpanel" aria-labelledby="step-4">--%>
<%--            Step content--%>
<%--        </div>--%>
<%--    </div>--%>

<%--    <!-- Include optional progressbar HTML -->--%>
<%--    <div class="progress">--%>
<%--        <div class="progress-bar" role="progressbar" style="width: 0%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>--%>
<%--    </div>--%>
<%--</div>--%>
<!-- Cadastro Supervisor Modal-->
<%--<div class="modal fade" id="cadastroSupervisorModal" tabindex="-1" aria-labelledby="cadastroSupervisorModalLabel" aria-hidden="true">--%>
<%--    <div class="modal-dialog modal-dialog-centered">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header bg-gradient-primary-to-secondary p-4">--%>
<%--                <h5 class="modal-title font-alt text-white" id="cadastroSupervisorModalLabel">Cadastrar Supervisor</h5>--%>
<%--                <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>--%>
<%--            </div>--%>
<%--            <div class="modal-body border-0 p-4">--%>
<%--                <form id="supervisorForm" action="principalController" name="supervisorForm" method="post">--%>
<%--                    <fieldset data-step="1">--%>
<%--                        <div class="form-floating mb-3">--%>
<%--                            <input class="form-control" id="nameSupervisor" type="text" name="nameSupervisor" placeholder="Digite seu nome completo"/>--%>
<%--                            <label for="nameSupervisor">Nome Completo</label>--%>
<%--                            <div class="invalid-login">A name is required.</div>--%>
<%--                        </div>--%>
<%--                        <div class="form-floating mb-3">--%>
<%--                            <input class="form-control" id="emailSupervisor" type="email" name="emailSupervisor" placeholder="name@example.com"/>--%>
<%--                            <label for="emailSupervisor">Email</label>--%>
<%--                            <div class="invalid-login">An email is required.</div>--%>
<%--                            <div class="invalid-login">Email is not valid.</div>--%>
<%--                        </div>--%>
<%--                        <div class="form-floating mb-3">--%>
<%--                            <input class="form-control" id="senhaSupervisor" type="password" name="senhaSupervisor" placeholder="Digite sua senha"/>--%>
<%--                            <label for="senhaSupervisor">Senha</label>--%>
<%--                            <div class="invalid-login">A name is required.</div>--%>
<%--                        </div>--%>
<%--                        <div class="form-floating mb-3">--%>
<%--                            <input class="form-control" id="pedidoSupervisor" type="text" name="pedidoSupervisor" placeholder="Digite o número do pedido"/>--%>
<%--                            <label for="pedidoSupervisor">Número do pedido</label>--%>
<%--                            <div class="invalid-login">A name is required.</div>--%>
<%--                        </div>--%>
<%--                        <div class="form-floating mb-3">--%>
<%--                            <input class="form-control" id="cargoSupervisor" type="text" name="cargoSupervisor" placeholder="Digita seu cargo"/>--%>
<%--                            <label for="cargoSupervisor">Cargo</label>--%>
<%--                            <div class="invalid-login">A name is required.</div>--%>
<%--                        </div>--%>
<%--                        <div class="form-floating mb-3">--%>
<%--                            <input class="form-control" id="telefoneSupervisor" type="tel" name="telefoneSupervisor" placeholder="(00) 00000-0000"/>--%>
<%--                            <label for="telefoneSupervisor">Telefone</label>--%>
<%--                            <div class="invalid-login">A phone number is required.</div>--%>
<%--                        </div>--%>
<%--                        <div class="d-none" id="submitSuccessMessageSupervisor">--%>
<%--                            <div class="text-center mb-3">--%>
<%--                                <div class="fw-bolder">Form submission successful!</div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                        <div class="d-none" id="submitErrorMessageSupervisor"><div class="text-center text-danger mb-3">Error sending message!</div></div>--%>
<%--                        <div class="d-grid"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonSupervisor1" name="submitButtonSupervisor1" value="supervisor1" data-step-to="next">Next</button></div>--%>
<%--                    </fieldset>--%>
<%--                    <fieldset data-step="2">--%>
<%--                        <div class="form-floating mb-3">--%>
<%--                            <input class="form-control" id="nomeEmpresa" type="text" name="nomeEmpresa" placeholder="Digite o nome da empresa"/>--%>
<%--                            <label for="nomeEmpresa">Nome da empresa</label>--%>
<%--                            <div class="invalid-login">A name is required.</div>--%>
<%--                        </div>--%>
<%--                        <div class="form-floating mb-3">--%>
<%--                            <input class="form-control" id="cnpjEmpresa" type="text" name="cnpjEmpresa" placeholder="Digite o cnpj da empresa"/>--%>
<%--                            <label for="cnpjEmpresa">CNPJ da empresa</label>--%>
<%--                            <div class="invalid-login">A name is required.</div>--%>
<%--                        </div>--%>
<%--                        <div class="d-none" id="submitSuccessMessageEmpresa">--%>
<%--                            <div class="text-center mb-3">--%>
<%--                                <div class="fw-bolder">Form submission successful!</div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                        <div class="d-none" id="submitErrorMessageEmpresa"><div class="text-center text-danger mb-3">Error sending message!</div></div>--%>
<%--                    </fieldset>--%>
<%--                    <div class="d-grid"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonSupervisor2" name="submitButtonSupervisor2" value="supervisor2">Enviar</button></div>--%>
<%--                </form>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>
<!-- Cadastro Empresa Modal-->
<%--<div class="modal fade" id="cadastroEmpresaModal" tabindex="-1" aria-labelledby="cadastroEmpresaModalLabel" aria-hidden="true">--%>
<%--    <div class="modal-dialog modal-dialog-centered">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header bg-gradient-primary-to-secondary p-4">--%>
<%--                <h5 class="modal-title font-alt text-white" id="cadastroEmpresaModalLabel">Cadastrar Empresa</h5>--%>
<%--                <button class="btn-close btn-close-white" type="button" data-bs-dismiss="modal" aria-label="Close"></button>--%>
<%--            </div>--%>
<%--            <div class="modal-body border-0 p-4">--%>
<%--                <form id="empresaForm" action="principalController" name="empresaForm" method="post">--%>
<%--                    <div class="form-floating mb-3">--%>
<%--                        <input class="form-control" id="nomeEmpresa" type="text" name="nomeEmpresa" placeholder="Digite o nome da empresa"/>--%>
<%--                        <label for="nomeEmpresa">Nome da empresa</label>--%>
<%--                        <div class="invalid-login">A name is required.</div>--%>
<%--                    </div>--%>
<%--                    <div class="form-floating mb-3">--%>
<%--                        <input class="form-control" id="cnpjEmpresa" type="text" name="cnpjEmpresa" placeholder="Digite o cnpj da empresa"/>--%>
<%--                        <label for="cnpjEmpresa">CNPJ da empresa</label>--%>
<%--                        <div class="invalid-login">A name is required.</div>--%>
<%--                    </div>--%>
<%--                    <div class="d-none" id="submitSuccessMessageEmpresa">--%>
<%--                        <div class="text-center mb-3">--%>
<%--                            <div class="fw-bolder">Form submission successful!</div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                    <div class="d-none" id="submitErrorMessageEmpresa"><div class="text-center text-danger mb-3">Error sending message!</div></div>--%>
<%--                    <div class="d-grid"><button class="btn btn-primary rounded-pill btn-lg" id="submitButtonEmpresa" name="submitButtonEmpresa" value="empresa" type="submit">Enviar</button></div>--%>
<%--                </form>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

</body>
</html>