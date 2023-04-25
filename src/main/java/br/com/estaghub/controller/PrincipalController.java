package br.com.estaghub.controller;

import br.com.estaghub.domain.*;
import br.com.estaghub.dto.DiscenteCreationDTO;
import br.com.estaghub.dto.EmpresaCreationDTO;
import br.com.estaghub.dto.SupervisorCreationDTO;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.mapper.impl.DiscenteMapperImpl;
import br.com.estaghub.mapper.impl.EmpresaMapperImpl;
import br.com.estaghub.mapper.impl.SupervisorMapperImpl;
import br.com.estaghub.util.S3Util;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "PrincipalController", value = "/principalController")
public class PrincipalController extends HttpServlet {
    private static final String SUCESS_DISCENTE = "/discente.jsp";
    private static final String SUCESS_DOCENTE = "/docente.jsp";
    private static final String SUCESS_DOCENTE_COMISSAO = "/docenteComissao.jsp";
    private static final String SUCESS_SUPERVISOR = "/supervisor.jsp";
    private static final String INDEX = "/index.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            if ("logout".equals(req.getParameter("buttonLogout"))){
                req.getSession().invalidate();
                req.getRequestDispatcher(INDEX).forward(req,resp);
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
            }
            if ("discente".equals(req.getParameter("submitButtonDiscente"))){
                criarDiscente(req, resp);
            }
            if ("supervisor".equals(req.getParameter("submitButtonSupervisor1"))){
                criarSupervisorComEmpresaVinculada(req, resp);
            }
            if ("supervisor".equals(req.getParameter("submitButtonSupervisor2"))){
                criarSupervisorComNovaEmpresa(req, resp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void criarSupervisorComNovaEmpresa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        String numPedido = req.getParameter("pedidoSupervisor");
        Pedido pedido = new Pedido();
        SupervisorMapperImpl supervisorMapper = new SupervisorMapperImpl();
        EmpresaMapperImpl empresaMapper = new EmpresaMapperImpl();
        Supervisor supervisor = supervisorMapper.toSupervisorCreateAccount(supervisorDTO);
        Empresa empresa = empresaMapper.toEmpresaCreateAccount(empresaDTO);
        if (!numPedido.equals("") && !pedido.getPedidoByIdWhereSupervisorNotSet(numPedido)){
            supervisor.criarSupervisor(supervisor, empresa, numPedido);
        }
        //todo em caso de erro retornar uma mensagem na tela antes de redirecionar
        req.getRequestDispatcher(INDEX).forward(req,resp);
    }

    private static void criarSupervisorComEmpresaVinculada(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SupervisorCreationDTO supervisorDTO = SupervisorCreationDTO.builder().nome(req.getParameter("nameSupervisor"))
                .email(req.getParameter("emailSupervisor"))
                .senha(req.getParameter("senhaSupervisor"))
                .cargo(req.getParameter("cargoSupervisor"))
                .telefone(req.getParameter("telefoneSupervisor"))
                .build();
        Pedido pedido = new Pedido();
        String numPedido = req.getParameter("pedidoSupervisor");
        String cnpjEmpresaVinculada = req.getParameter("selectVinculoSupervisorEmpresa");
        SupervisorMapperImpl supervisorMapper = new SupervisorMapperImpl();
        Supervisor supervisor = supervisorMapper.toSupervisorCreateAccount(supervisorDTO);
        if (!cnpjEmpresaVinculada.isBlank() && !pedido.getPedidoByIdWhereSupervisorNotSet(numPedido)){
            supervisor.vincularEmpresa(supervisor, cnpjEmpresaVinculada, numPedido);
        }
        //todo em caso de erro retornar uma mensagem na tela antes de redirecionar
        req.getRequestDispatcher(INDEX).forward(req,resp);
    }

    private static void criarDiscente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DiscenteCreationDTO discenteDTO = DiscenteCreationDTO.builder().nome(req.getParameter("nomeDiscente"))
                .email(req.getParameter("emailDiscente"))
                .senha(req.getParameter("senhaDiscente"))
                .matricula(req.getParameter("matriculaDiscente"))
                .telefone(req.getParameter("telefoneDiscente"))
                .build();
        DiscenteMapperImpl discenteMapper = new DiscenteMapperImpl();
        Discente discente = discenteMapper.toDiscenteCreateAccount(discenteDTO);
        discente.criarDiscente(discente);
        //todo verificar se criou o discente, se caso sim retornar uma mensagem de sucesso e redirecionar para o index, caso não retornar uma mensagem de erro pelo fato do email já ter um cadastro (isso é uma falha de segurança, mas é pra deixar mais intuitivo pro usuario)
        req.getRequestDispatcher(INDEX).forward(req,resp);
    }

    private static void loginSupervisor(HttpServletRequest req, HttpServletResponse resp, String email, String senha, Pedido pedido) throws ServletException, IOException {
        Supervisor supervisor = new Supervisor();
        if(supervisor.loginSupervisor(email, senha)){
            HttpSession session = req.getSession();
            session.setAttribute("SUPERVISOR",supervisor.getSupervisorByEmail(email));
            session.setAttribute("PEDIDOS_RENOVACAO", pedido.getAllPedidosOfSupervisor(supervisor.getSupervisorByEmail(email)));
            session.setAttribute("EMAIL", email);
            req.getRequestDispatcher(SUCESS_SUPERVISOR).forward(req,resp);
        }else{
            //todo em caso de erro retornar uma mensagem na tela antes de redirecionar
            req.getRequestDispatcher(INDEX).forward(req,resp);
        }
    }

    private static void loginDocente(HttpServletRequest req, HttpServletResponse resp, String email, String senha, Pedido pedido) throws ServletException, IOException {
        Docente docente = new Docente();
        Departamento departamento = new Departamento();
        if(docente.loginDocente(email, senha)){
            HttpSession session = req.getSession();
            if (docente.checkIfDocenteIsComissao(email)){
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE_COMISSAO);
                session.setAttribute("DOCENTE",docente.getDocenteByEmail(email).get());
                session.setAttribute("EMAIL_DOCENTE",docente.getDocenteByEmail(email).get().getEmail());
                session.setAttribute("LIST_PEDIDOS", pedido.getAllPedidos());
                session.setAttribute("LIST_DEPARTAMENTOS",departamento.getAllDepartamentos());
                view.forward(req, resp);
            }else{
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE);
                session.setAttribute("DOCENTE",docente.getDocenteByEmail(email).get());
                session.setAttribute("EMAIL_DOCENTE",docente.getDocenteByEmail(email).get().getEmail());
                session.setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocente(docente.getDocenteByEmail(email).get()));
                view.forward(req, resp);
            }
        }else{
            //todo em caso de erro retornar uma mensagem na tela antes de redirecionar
            req.getRequestDispatcher(INDEX).forward(req,resp);
        }
    }

    private static void loginDiscente(HttpServletRequest req, HttpServletResponse resp, String email, String senha, Pedido pedido) throws ServletException, IOException {
        Discente discente = new Discente();
        if(discente.loginDiscente(email, senha)){
            RequestDispatcher view = req.getRequestDispatcher(SUCESS_DISCENTE);
            HttpSession session = req.getSession();
            Curso curso = new Curso();
            Documento documento = new Documento();
            if (!discente.getDiscenteByEmail(email).isEmpty()){
                session.setAttribute("DISCENTE",discente.getDiscenteByEmail(email).get());
                session.setAttribute("ID_DISCENTE",discente.getDiscenteByEmail(email).get().getId());
                if (!pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).isEmpty()){
                    session.setAttribute("NOVO_ESTAGIO", pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get());
                    session.setAttribute("ID_PEDIDO", pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId());
                    if (!S3Util.getTceOrTermoAditivoFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), TipoDocumento.TCE).get(1).isBlank()){
                        session.setAttribute("TCE_MODELO_UFRRJ", S3Util.getTceOrTermoAditivoFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), TipoDocumento.TCE).get(0));
                        session.setAttribute("TCE_MODELO_UFRRJ_URL", S3Util.getTceOrTermoAditivoFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), TipoDocumento.TCE).get(1));
                    }
                    if ("NOVO_STEP4_PLANO_ATIVIDADES".equals(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getStatus().name()) || "NOVO_STEP4_ATIVIDADES_TCE".equals(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getStatus().name())){
                        if (documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(),TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES).isPresent()){
                            session.setAttribute("DOCUMENTO",documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(),TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES).get());
                        }
                    }else if ("NOVO_STEP3".equals(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getStatus().name())) {
                        if (documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(),TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).isPresent()){
                            session.setAttribute("DOCUMENTO",documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(),TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).get());
                        }
                    }else if ("NOVO_STEP4_TCE".equals(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getStatus().name())) {
                        if (documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(),TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).isPresent()){
                            session.setAttribute("DOCUMENTO",documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(),TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).get());
                        }
                    }else if ("NOVO_STEP4_ATIVIDADES_TCE".equals(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getStatus().name())) {
                        if (documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(),TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES).isPresent()){
                            session.setAttribute("DOCUMENTO",documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(),TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES).get());
                        }
                    }else if("NOVO_STEP3_DISCENTE_ASSINADO".equals(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getStatus().name())){
                        if (!documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).isPresent()){
                            session.setAttribute("PLANO_ATIVIDADES", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES).get());
                            session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES).get().getNome()));
                        }else{
                            session.setAttribute("PLANO_ATIVIDADES", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get());
                            session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome()));
                        }
                        if (!documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.TCE_ASSINADO_DISCENTE).isPresent()){
                            session.setAttribute("TCE", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).get());
                            session.setAttribute("TCE_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).get().getNome()));
                        }else{
                            session.setAttribute("TCE", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.TCE_ASSINADO_DISCENTE).get());
                            session.setAttribute("TCE_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome()));
                        }
                    }
                    else if ("NOVO_PEDIDO_FIM".equals(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getStatus().name())) {
                        session.setAttribute("PLANO_ATIVIDADES", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DOCENTE).get());
                        session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DOCENTE).get().getNome()));
                        session.setAttribute("TCE", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.TCE_ASSINADO_DOCENTE).get());
                        session.setAttribute("TCE_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.NOVO).get().getId(), TipoDocumento.TCE_ASSINADO_DOCENTE).get().getNome()));
                    }
                }else if (!pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).isEmpty()) {
                    session.setAttribute("RENOVACAO_ESTAGIO", pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get());
                    session.setAttribute("ID_PEDIDO", pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getId());
                    if(!S3Util.getTceOrTermoAditivoFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), TipoDocumento.TERMO_ADITIVO).get(1).isBlank()){
                        session.setAttribute("TERMO_ADITIVO_MODELO_UFRRJ", S3Util.getTceOrTermoAditivoFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), TipoDocumento.TERMO_ADITIVO).get(0));
                        session.setAttribute("TERMO_ADITIVO_MODELO_UFRRJ_URL", S3Util.getTceOrTermoAditivoFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), TipoDocumento.TERMO_ADITIVO).get(1));
                    }
                    if("RENOVACAO_STEP4_DISCENTE_ASSINADO".equals(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getStatus().name())){
                        if (documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getId(), TipoDocumento.TERMO_ADITIVO).isPresent()){
                            session.setAttribute("TERMO_ADITIVO", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getId(), TipoDocumento.TERMO_ADITIVO).get());
                            session.setAttribute("TERMO_ADITIVO_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getId(), TipoDocumento.TERMO_ADITIVO).get().getNome()));
                        }
                        if (documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).isPresent()){
                            session.setAttribute("TERMO_ADITIVO", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get());
                            session.setAttribute("TERMO_ADITIVO_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get().getNome()));
                        }
                    }else if ("NOVO_PEDIDO_FIM".equals(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getStatus().name())) {
                        session.setAttribute("TERMO_ADITIVO", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get());
                        session.setAttribute("TERMO_ADITIVO_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteByEmail(email).get(), TipoPedido.RENOVACAO).get().getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get().getNome()));
                    }
                }
                session.setAttribute("LIST_CURSOS",curso.getAllCursos());
            }
            view.forward(req, resp);
        }else{
            //todo em caso de erro retornar uma mensagem na tela antes de redirecionar
            req.getRequestDispatcher(INDEX).forward(req,resp);
        }
    }
}
