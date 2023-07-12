package br.com.estaghub.controller;

import br.com.estaghub.domain.*;
import br.com.estaghub.dto.DiscenteCreationDTO;
import br.com.estaghub.dto.EmpresaCreationDTO;
import br.com.estaghub.dto.SupervisorCreationDTO;
import br.com.estaghub.mapper.impl.DiscenteMapperImpl;
import br.com.estaghub.mapper.impl.EmpresaMapperImpl;
import br.com.estaghub.mapper.impl.SupervisorMapperImpl;
import br.com.estaghub.service.EmailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;

@WebServlet(value = "/principalController")
public class PrincipalController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            if ("logout".equals(req.getParameter("buttonLogout"))){
                req.getSession().invalidate();
            }else if ("confirmarEmail".equals(req.getParameter("sendEmail"))){
                String email = req.getSession().getAttribute("USUARIO_EMAIL").toString();
                String otp = req.getSession().getAttribute("OTP").toString();
                EmailService.sendCodForAuthOrResetPassword(req, email, otp);
            }else if ("login".equals(req.getParameter("submitButtonLogin"))){
                String loginOptions = req.getParameter("loginOptions");
                String email = req.getParameter("emailLogin");
                String senha = req.getParameter("senhaLogin");
                Pedido pedido = new Pedido();
                if ("discente".equals(loginOptions)){
                    loginDiscente(req, resp, email, senha, pedido);
                }else if ("docente".equals(loginOptions)) {
                    loginDocente(req, resp, email, senha, pedido);
                }else if ("supervisor".equals(loginOptions)) {
                    loginSupervisor(req, resp, email, senha, pedido);
                }
            }else if ("forgotPassword".equals(req.getParameter("submitButtonForgotPassword"))){
                String loginForgotOptions = req.getParameter("loginForgotOptions");
                String email = req.getParameter("emailForgotLogin");
                forgotPassword(req, resp, loginForgotOptions, email);
            }else if ("newPassword".equals(req.getParameter("submitButtonNewPassword"))){
                String tipoUsuario = req.getSession().getAttribute("USUARIO_TIPO").toString();
                String emailUsuario = req.getSession().getAttribute("USUARIO_EMAIL").toString();
                String otp = req.getSession().getAttribute("OTP").toString();
                String code = req.getParameter("otp").replaceAll(" ","");
                String senha = req.getParameter("senhaNewLogin");
                String confirmarSenha = req.getParameter("confirmarSenhaNewLogin");
                newPassword(resp, tipoUsuario, emailUsuario, otp, code, senha, confirmarSenha);
            }else if ("discente".equals(req.getParameter("submitButtonDiscente"))){
                criarDiscente(req, resp);
            }else if ("confirmarEmail".equals(req.getParameter("submitButtonEmailSupervisor1"))){
                criarSupervisorComEmpresaVinculada(req, resp);
            }else if ("confirmarEmail".equals(req.getParameter("submitButtonEmailSupervisor2"))){
                criarSupervisorComNovaEmpresa(req, resp);
            }else if ("true".equals(req.getParameter("checkEmailSupervisor"))){
                String email = req.getParameter("emailSupervisor");
                if (new Supervisor().getSupervisorByEmail(email).isEmpty()){
                    resp.getWriter().write("{\"checkedEmailSupervisor\": true}");
                }else {
                    resp.getWriter().write("{\"message\": \"Já existe um(a) supervisor(a) com esse email informado!\"}");
                }
            }else if ("confirmarEmail".equals(req.getParameter("submitButtonDiscente"))){
                String email = req.getParameter("emailDiscente");
                String matricula = req.getParameter("matriculaDiscente");
                Discente discente = new Discente();
                discente.setEmail(email);
                discente.setMatricula(matricula);
                confirmEmail(req, resp, email, matricula, discente);
            }else if ("supervisor".equals(req.getParameter("submitButtonSupervisor1"))){
                String cnpjEmpresaVinculada = req.getParameter("selectVinculoSupervisorEmpresa");
                if (new Empresa().getEmpresaByCnpj(cnpjEmpresaVinculada).isPresent()){
                    String email = req.getParameter("emailSupervisor");
                    String otp = String.valueOf(new Random().nextInt(Integer.MAX_VALUE));
                    req.getSession().setAttribute("USUARIO_EMAIL", email);
                    req.getSession().setAttribute("OTP", otp);
                    resp.getWriter().write("{\"criarSupervisorComEmpresaVinculada\": true}");
                }else {
                    resp.getWriter().write("{\"message\": \"Não foi possível encontrar essa empresa!\"}");
                }
            }else if ("supervisor".equals(req.getParameter("submitButtonSupervisor2"))){
                String emailEmpresa = req.getParameter("emailEmpresa");
                String cnpjEmpresa = req.getParameter("cnpjEmpresaSupervisor");
                Empresa empresa = new Empresa();
                if (empresa.checkIfPossibleToCreateEmpresa(emailEmpresa,cnpjEmpresa)){
                    String email = req.getParameter("emailSupervisor");
                    String otp = String.valueOf(new Random().nextInt(Integer.MAX_VALUE));
                    req.getSession().setAttribute("USUARIO_EMAIL", email);
                    req.getSession().setAttribute("OTP", otp);
                    resp.getWriter().write("{\"criarSupervisorComNovaEmpresa\": true}");
                }else {
                    if (empresa.getEmpresaByCnpj(cnpjEmpresa).isPresent() && empresa.getEmpresaByEmail(emailEmpresa).isPresent()){
                        resp.getWriter().write("{\"message\": \"Já existe uma empresa com esse cnpj e email informado!\"}");
                    }else if (empresa.getEmpresaByCnpj(cnpjEmpresa).isPresent()){
                        resp.getWriter().write("{\"message\": \"Já existe uma empresa com esse cnpj informado!\"}");
                    }else{
                        resp.getWriter().write("{\"message\": \"Já existe uma empresa com esse email informado!\"}");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void confirmEmail(HttpServletRequest req, HttpServletResponse resp, String email, String matricula, Discente discente) throws IOException {
        if (discente.checkIfPossibleToCreateDiscente(discente)){
            String otp = String.valueOf(new Random().nextInt(Integer.MAX_VALUE));
            req.getSession().setAttribute("USUARIO_EMAIL", email);
            req.getSession().setAttribute("OTP", otp);
            resp.getWriter().write("{\"confirmarEmail\": true}");
        }else {
            if (discente.getDiscenteByEmail(email).isPresent() && discente.getDiscenteByMatricula(matricula).isPresent()){
                resp.getWriter().write("{\"message\": \"Já existe um(a) discente com esse email e matrícula informado!\"}");
            }else if (discente.getDiscenteByEmail(email).isPresent()){
                resp.getWriter().write("{\"message\": \"Já existe um(a) discente com esse email informado!\"}");
            }else {
                resp.getWriter().write("{\"message\": \"Já existe um(a) discente com essa matrícula informada!\"}");
            }
        }
    }

    private static void newPassword(HttpServletResponse resp, String tipoUsuario, String emailUsuario, String otp, String code, String senha, String confirmarSenha) throws IOException {
        if ("discente".equals(tipoUsuario)){
            if (!otp.equals(code) && !senha.equals(confirmarSenha)){
                resp.getWriter().write("{\"message\": \"O código e/ou as senhas fornecidas não correspondem!\"}");
            }else{
                if(!otp.equals(code)){
                    resp.getWriter().write("{\"message\": \"Código incorreto!\"}");
                }else if(!senha.equals(confirmarSenha)){
                    resp.getWriter().write("{\"message\": \"As senhas fornecidas não correspondem!\"}");
                }else {
                    new Discente().changePasswordDiscente(emailUsuario, senha);
                    resp.getWriter().write("{\"newPassword\": true}");
                }
            }
        }else if ("docente".equals(tipoUsuario)) {
            if (!otp.equals(code) && !senha.equals(confirmarSenha)){
                resp.getWriter().write("{\"message\": \"O código e/ou as senhas fornecidas não correspondem!\"}");
            }else{
                if(!otp.equals(code)){
                    resp.getWriter().write("{\"message\": \"Código incorreto!\"}");
                }else if(!senha.equals(confirmarSenha)){
                    resp.getWriter().write("{\"message\": \"As senhas fornecidas não correspondem!\"}");
                }else {
                    new Docente().changePasswordDocente(emailUsuario, senha);
                    resp.getWriter().write("{\"newPassword\": true}");
                }
            }
        }else if ("supervisor".equals(tipoUsuario)) {
            if (!otp.equals(code) && !senha.equals(confirmarSenha)){
                resp.getWriter().write("{\"message\": \"O código e/ou as senhas fornecidas não correspondem!\"}");
            }else{
                if(!otp.equals(code)){
                    resp.getWriter().write("{\"message\": \"Código incorreto!\"}");
                }else if(!senha.equals(confirmarSenha)){
                    resp.getWriter().write("{\"message\": \"As senhas fornecidas não correspondem!\"}");
                }else {
                    new Supervisor().changePasswordSupervisor(emailUsuario, senha);
                    resp.getWriter().write("{\"newPassword\": true}");
                }
            }
        }
    }

    private static void forgotPassword(HttpServletRequest req, HttpServletResponse resp, String loginForgotOptions, String email) throws IOException {
        if ("discente".equals(loginForgotOptions)){
            if (new Discente().getDiscenteByEmail(email).isPresent()){
                forgotPasswordAndSetOTP(req, resp, loginForgotOptions, email);
            }else {
                resp.getWriter().write("{\"message\": \"Não foi possível identificar esse(a) discente, verifique o email informado!\"}");
            }
        }else if ("docente".equals(loginForgotOptions)) {
            if (new Docente().getDocenteByEmail(email).isPresent()){
                forgotPasswordAndSetOTP(req, resp, loginForgotOptions, email);
            }else {
                resp.getWriter().write("{\"message\": \"Não foi possível identificar esse(a) docente, verifique o email informado!\"}");
            }
        }else if ("supervisor".equals(loginForgotOptions)) {
            if (new Supervisor().getSupervisorByEmail(email).isPresent()){
                forgotPasswordAndSetOTP(req, resp, loginForgotOptions, email);
            }else {
                resp.getWriter().write("{\"message\": \"Não foi possível identificar esse(a) supervisor(a), verifique o email informado!\"}");
            }
        }
    }

    private static void forgotPasswordAndSetOTP(HttpServletRequest req, HttpServletResponse resp, String loginForgotOptions, String email) throws IOException {
        String otp = String.valueOf(new Random().nextInt(Integer.MAX_VALUE));
        req.getSession().setAttribute("USUARIO_TIPO", loginForgotOptions);
        req.getSession().setAttribute("USUARIO_EMAIL", email);
        req.getSession().setAttribute("OTP", otp);
        resp.getWriter().write("{\"forgotPassword\": true}");
    }

    private static void criarSupervisorComNovaEmpresa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String otp = req.getSession().getAttribute("OTP").toString();
        String code = req.getParameter("otpEmail").replaceAll(" ","");
        SupervisorCreationDTO supervisorDTO = SupervisorCreationDTO.builder().nome(req.getParameter("nameSupervisor"))
                .email(req.getParameter("emailSupervisor"))
                .senha(req.getParameter("senhaSupervisor"))
                .cargo(req.getParameter("cargoSupervisor"))
                .telefone(req.getParameter("telefoneSupervisor"))
                .build();
        EmpresaCreationDTO empresaDTO = EmpresaCreationDTO.builder().nome(req.getParameter("nomeEmpresaSupervisor"))
                .cnpj(req.getParameter("cnpjEmpresaSupervisor"))
                .endereco(req.getParameter("enderecoEmpresa"))
                .email(req.getParameter("emailEmpresa"))
                .telefone(req.getParameter("telefoneEmpresa"))
                .build();
        Supervisor supervisor = new SupervisorMapperImpl().toSupervisorCreateAccount(supervisorDTO);
        Empresa empresa = new EmpresaMapperImpl().toEmpresaCreateAccount(empresaDTO);
        if (otp.equals(code)){
            supervisor.criarSupervisor(supervisor, empresa);
            resp.getWriter().write("{\"criarSupervisorComNovaEmpresa\": true}");
        }else{
            resp.getWriter().write("{\"message\": \"Não foi possível criar o(a) supervisor(a), o código informado não é válido!\"}");
        }
    }

    private static void criarSupervisorComEmpresaVinculada(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String otp = req.getSession().getAttribute("OTP").toString();
        String code = req.getParameter("otpEmail").replaceAll(" ","");
        SupervisorCreationDTO supervisorDTO = SupervisorCreationDTO.builder().nome(req.getParameter("nameSupervisor"))
                .email(req.getParameter("emailSupervisor"))
                .senha(req.getParameter("senhaSupervisor"))
                .cargo(req.getParameter("cargoSupervisor"))
                .telefone(req.getParameter("telefoneSupervisor"))
                .build();
        String cnpjEmpresaVinculada = req.getParameter("selectVinculoSupervisorEmpresa");
        Supervisor supervisor = new SupervisorMapperImpl().toSupervisorCreateAccount(supervisorDTO);
        if (otp.equals(code)){
            supervisor.vincularEmpresa(supervisor, cnpjEmpresaVinculada);
            resp.getWriter().write("{\"criarSupervisorComEmpresaVinculada\": true}");
        }else{
            resp.getWriter().write("{\"message\": \"Não foi possível criar o(a) supervisor(a), o código informado não é válido!\"}");
        }
    }

    private static void criarDiscente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String otp = req.getSession().getAttribute("OTP").toString();
        String code = req.getParameter("otpEmail").replaceAll(" ","");
        DiscenteCreationDTO discenteDTO = DiscenteCreationDTO.builder().nome(req.getParameter("nomeDiscente"))
                .email(req.getParameter("emailDiscente"))
                .senha(req.getParameter("senhaDiscente"))
                .matricula(req.getParameter("matriculaDiscente"))
                .telefone(req.getParameter("telefoneDiscente"))
                .build();
        Discente discente = new DiscenteMapperImpl().toDiscenteCreateAccount(discenteDTO);
        if (otp.equals(code)){
            discente.criarDiscente(discente);
            resp.getWriter().write("{\"criarDiscente\": true}");
        }else{
            resp.getWriter().write("{\"message\": \"Não foi possível criar o(a) discente, o código informado não é válido!\"}");
        }
    }

    private static void loginSupervisor(HttpServletRequest req, HttpServletResponse resp, String email, String senha, Pedido pedido) throws ServletException, IOException {
        Supervisor supervisor = new Supervisor();
        if(supervisor.loginSupervisor(email, senha)){
            req.getSession().setAttribute("SUPERVISOR",supervisor.getSupervisorByEmail(email).get());
            req.getSession().setAttribute("PEDIDOS_RENOVACAO", pedido.getAllPedidosOfSupervisor(supervisor.getSupervisorByEmail(email).get()));
            req.getSession().setAttribute("LIST_DISCENTES_RENOV_STEP1", pedido.getAllRenovPedidosInStep1WithoutSupervisor().stream().map(p->p.getDiscente()).collect(Collectors.toList()));
            resp.getWriter().write("{\"supervisor\": true}");
        }else{
            resp.getWriter().write("{\"message\": \"Credenciais incorretas!\"}");
        }
    }

    private static void loginDocente(HttpServletRequest req, HttpServletResponse resp, String email, String senha, Pedido pedido) throws ServletException, IOException {
        Docente docente = new Docente();
        Curso curso = new Curso();
        Departamento departamento = new Departamento();
        if(docente.loginDocente(email, senha)){
            Docente currentDocente = docente.getDocenteByEmail(email).get();
            if (docente.checkIfDocenteIsComissao(email)){
                req.getSession().setAttribute("DOCENTE_COMISSAO",currentDocente);
                req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocenteComissao(currentDocente));
                req.getSession().setAttribute("LIST_DEPARTAMENTOS",departamento.getAllDepartamentos());
                req.getSession().setAttribute("LIST_DEPARTAMENTOS_ATIVOS",departamento.getAllDepartamentosWithStatusActive());
                req.getSession().setAttribute("LIST_DOCENTES",docente.getAllDocentes());
                req.getSession().setAttribute("LIST_CURSOS",curso.getAllCursos());
                resp.getWriter().write("{\"docenteComissao\": true}");
            }else{
                req.getSession().setAttribute("DOCENTE",currentDocente);
                req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocente(currentDocente));
                resp.getWriter().write("{\"docente\": true}");
            }
        }else{
            resp.getWriter().write("{\"message\": \"Credenciais incorretas!\"}");
        }
    }

    private static void loginDiscente(HttpServletRequest req, HttpServletResponse resp, String email, String senha, Pedido pedido) throws ServletException, IOException {
        Discente discente = new Discente();
        if(discente.loginDiscente(email, senha)){
            req.getSession().setAttribute("DISCENTE",discente.getDiscenteByEmail(email).get());
            resp.getWriter().write("{\"discente\": true}");
        }else{
            resp.getWriter().write("{\"message\": \"Credenciais incorretas!\"}");
        }
    }
}
