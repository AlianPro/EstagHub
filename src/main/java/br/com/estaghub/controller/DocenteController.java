package br.com.estaghub.controller;

import br.com.estaghub.domain.*;
import br.com.estaghub.dto.CursoCreationDTO;
import br.com.estaghub.dto.DepartamentoCreationDTO;
import br.com.estaghub.dto.DocenteCreationDTO;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.mapper.impl.CursoMapperImpl;
import br.com.estaghub.mapper.impl.DepartamentoMapperImpl;
import br.com.estaghub.mapper.impl.DocenteMapperImpl;
import br.com.estaghub.util.FileUtil;
import br.com.estaghub.util.S3Util;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "DocenteController", value = "/docenteController")
@MultipartConfig(fileSizeThreshold=1024*1024*10,  	// 10 MB
location = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos")
public class DocenteController extends HttpServlet {
    private static final String SUCESS_DISCENTE = "/discente.jsp";
    private static final String SUCESS_DOCENTE = "/docente.jsp";
    private static final String SUCESS_DOCENTE_COMISSAO = "/docenteComissao.jsp";
    private static final String SUCESS_SUPERVISOR = "/supervisor.jsp";
    private static final String NOVO_ESTAGIO = "/novoEstagio.jsp";
    private static final String RENOVACAO_ESTAGIO = "/renovacaoEstagio.jsp";
    private static final String LOGOUT = "/index.jsp";
    private static final String PEDIDOS_COMISSAO = "/pedidosDocenteComissao.jsp";
    private static final String PEDIDOS_DOCENTE = "/pedidosDocente.jsp";
    private static final String NOVO_STEP2 = "/novoStep2.jsp";
    private static final String NOVO_STEP2_JUSTIFICADO = "/novoStep2Justificado.jsp";
    private static final String NOVO_STEP3 = "/emitirTCE.jsp";
    private static final String NOVO_STEP4 = "/novoStep4.jsp";
    private static final String RENOVACAO_STEP2 = "/renovacaoStep2.jsp";
    private static final String RENOVACAO_STEP3 = "/renovacaoStep3.jsp";
    private static final String RENOVACAO_STEP4 = "/renovacaoStep4.jsp";
    private static final String NOVO_STEP4_ASSINAR = "/assinarDocumentoDocente.jsp";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            if ("logout".equals(req.getParameter("buttonLogoutDocenteComissao"))){
                HttpSession session = req.getSession();
                session.invalidate();
                RequestDispatcher view = req.getRequestDispatcher(LOGOUT);
                view.forward(req,resp);
            }else if ("pedido".equals(req.getParameter("buttonPedido"))) {
                Pedido pedido = new Pedido();
                Docente docente = new Docente();
                HttpSession session = req.getSession();
                Documento documento = new Documento();
                session.setAttribute("ID_PEDIDO", req.getParameter("idPedido"));
                session.setAttribute("PEDIDO", pedido.getPedidoById(Long.parseLong(req.getParameter("idPedido"))));
                session.setAttribute("STATUS_PEDIDO", req.getParameter("statusPedido"));
                session.setAttribute("DOCENTES",docente.getAllDocentes());
                session.setAttribute("DOCENTES_NAO_COMISSAO",docente.getAllDocentesNoComissao());
                String statusPedido = req.getParameter("statusPedido");
                String idPedido = req.getParameter("idPedido");
                String emailDocente = req.getParameter("EMAIL_DOCENTE");
                if (StatusPedido.NOVO_STEP1.name().equals(statusPedido)) {
                    session.setAttribute("HISTORICO_ACADEMICO_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.HISTORICO_ACADEMICO).get().getNome()));
                    session.setAttribute("GRADE_HORARIO_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.GRADE_HORARIO).get().getNome()));
                    session.setAttribute("HISTORICO_ACADEMICO", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.HISTORICO_ACADEMICO).get());
                    session.setAttribute("GRADE_HORARIO", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.GRADE_HORARIO).get());
                }else if (StatusPedido.NOVO_STEP2_JUSTIFICADO.name().equals(statusPedido)){
                    session.setAttribute("DOCENTES_NAO_COMISSAO",docente.getAllDocentesNoComissao());
                    RequestDispatcher view = req.getRequestDispatcher(NOVO_STEP2_JUSTIFICADO);
                    view.forward(req,resp);
                }else if (StatusPedido.NOVO_STEP2.name().equals(statusPedido)){
                    RequestDispatcher view = req.getRequestDispatcher(NOVO_STEP3);
                    view.forward(req,resp);
                }else if (StatusPedido.NOVO_STEP3.name().equals(statusPedido)){
                    RequestDispatcher view = req.getRequestDispatcher(NOVO_STEP4);
                    view.forward(req,resp);
                }else if (StatusPedido.NOVO_STEP4.name().equals(statusPedido)){
                    session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome()));
                    session.setAttribute("TCE_URL", S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome()));
                    session.setAttribute("PLANO_ATIVIDADES", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get());
                    session.setAttribute("TCE", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.TCE_ASSINADO_DISCENTE).get());
                    RequestDispatcher view = req.getRequestDispatcher(NOVO_STEP4);
                    view.forward(req,resp);
                }else if (StatusPedido.NOVO_STEP4_DOCENTE_ASSINADO.name().equals(statusPedido)){
                    session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome()));
                    session.setAttribute("TCE_URL", S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome()));
                    session.setAttribute("PLANO_ATIVIDADES", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get());
                    session.setAttribute("TCE", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.TCE_ASSINADO_DISCENTE).get());
                    session.setAttribute("NOVO_ESTAGIO", pedido.getPedidoById(Long.parseLong(idPedido)));
                    RequestDispatcher view = req.getRequestDispatcher(NOVO_STEP4_ASSINAR);
                    view.forward(req,resp);
                }else if (StatusPedido.RENOVACAO_STEP2.name().equals(statusPedido)){
                    session.setAttribute("HISTORICO_ACADEMICO_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.HISTORICO_ACADEMICO).get().getNome()));
                    session.setAttribute("GRADE_HORARIO_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.GRADE_HORARIO).get().getNome()));
                    session.setAttribute("HISTORICO_ACADEMICO", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.HISTORICO_ACADEMICO).get());
                    session.setAttribute("GRADE_HORARIO", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.GRADE_HORARIO).get());
                }else if (StatusPedido.RENOVACAO_STEP3.name().equals(statusPedido)){
                    RequestDispatcher view = req.getRequestDispatcher(RENOVACAO_STEP4);
                    view.forward(req,resp);
                }
            }else if ("step1".equals(req.getParameter("submitButtonDocenteAnalisaNovoPedido1"))) {
                Pedido pedido = new Pedido();
                Docente docente = new Docente();
                HttpSession session = req.getSession();
                Object idPedido = session.getAttribute("ID_PEDIDO");
                Object emailDocente = session.getAttribute("EMAIL_DOCENTE");
                String decisao = req.getParameter("radioPedido");
                if ("rejeitado".equals(decisao)){
                    if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido.toString())).getTipo().name())) {
                        String justificativa = req.getParameter("textAreaPedido");
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP2_REJEITADO);
                        pedido.changeJustificativaDocentePedido(idPedido.toString(),justificativa);
                        pedido.addDocenteComissaoInPedido(idPedido.toString(),docente.getDocenteByEmail(emailDocente.toString()).get());
                        session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
                        RequestDispatcher view = req.getRequestDispatcher(PEDIDOS_COMISSAO);
                        view.forward(req,resp);
                    }else{
                        String justificativa = req.getParameter("textAreaPedido");
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.RENOVACAO_STEP3_REJEITADO);
                        pedido.changeJustificativaDocentePedido(idPedido.toString(),justificativa);
                        pedido.addDocenteComissaoInPedido(idPedido.toString(),docente.getDocenteByEmail(emailDocente.toString()).get());
                        session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
                        RequestDispatcher view = req.getRequestDispatcher(PEDIDOS_COMISSAO);
                        view.forward(req,resp);
                    }
                }else if("aceito".equals(decisao)){
                    if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido.toString())).getTipo().name())){
                        String idDocente = req.getParameter("selectDocenteOrientador");
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP2);
                        pedido.addDocenteComissaoInPedido(idPedido.toString(),docente.getDocenteByEmail(emailDocente.toString()).get());
                        pedido.addDocenteOrientadorInPedido(idPedido.toString(),docente.getDocenteById(Long.parseLong(idDocente)));
                        session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
                        RequestDispatcher view = req.getRequestDispatcher(PEDIDOS_COMISSAO);
                        view.forward(req,resp);
                    }else{
                        String idDocente = req.getParameter("selectDocenteOrientador");
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.RENOVACAO_STEP3);
                        pedido.addDocenteComissaoInPedido(idPedido.toString(),docente.getDocenteByEmail(emailDocente.toString()).get());
                        pedido.addDocenteOrientadorInPedido(idPedido.toString(),docente.getDocenteById(Long.parseLong(idDocente)));
                        session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
                        RequestDispatcher view = req.getRequestDispatcher(PEDIDOS_COMISSAO);
                        view.forward(req,resp);
                    }
                }
            }else if ("step2".equals(req.getParameter("submitButtonDocenteAnalisaRecursoNovoPedido2"))) {
                Pedido pedido = new Pedido();
                Docente docente = new Docente();
                HttpSession session = req.getSession();
                Object idPedido = session.getAttribute("ID_PEDIDO");
                Object emailDocente = session.getAttribute("EMAIL_DOCENTE");
                String decisao = req.getParameter("radioPedido");
                if ("rejeitado".equals(decisao)){
                    if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido.toString())).getTipo().name())){
                        String justificativa = req.getParameter("textAreaPedido");
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.PEDIDO_ENCERRADO);
                        pedido.changeJustificativaRecursoPedido(idPedido.toString(),justificativa);
                        session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
                        RequestDispatcher view = req.getRequestDispatcher(PEDIDOS_COMISSAO);
                        view.forward(req,resp);
                    }else{
                        String justificativa = req.getParameter("textAreaPedido");
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.PEDIDO_ENCERRADO);
                        pedido.changeJustificativaRecursoPedido(idPedido.toString(),justificativa);
                        session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
                        RequestDispatcher view = req.getRequestDispatcher(PEDIDOS_COMISSAO);
                        view.forward(req,resp);
                    }
                }else if("aceito".equals(decisao)){
                    if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido.toString())).getTipo().name())){
                        String idDocente = req.getParameter("selectDocenteOrientador");
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP2);
                        pedido.addDocenteOrientadorInPedido(idPedido.toString(),docente.getDocenteById(Long.parseLong(idDocente)));
                        session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
                        RequestDispatcher view = req.getRequestDispatcher(PEDIDOS_COMISSAO);
                        view.forward(req,resp);
                    }else{
                        String idDocente = req.getParameter("selectDocenteOrientador");
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.RENOVACAO_STEP3);
                        pedido.addDocenteOrientadorInPedido(idPedido.toString(),docente.getDocenteById(Long.parseLong(idDocente)));
                        session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
                        RequestDispatcher view = req.getRequestDispatcher(PEDIDOS_COMISSAO);
                        view.forward(req,resp);
                    }
                }
            }else if ("step3".equals(req.getParameter("submitButtonDocenteAnalisaPedido"))) {
                Pedido pedido = new Pedido();
                Docente docente = new Docente();
                HttpSession session = req.getSession();
                Object idPedido = session.getAttribute("ID_PEDIDO");
                Object emailDocente = session.getAttribute("EMAIL_DOCENTE");
                String decisao = req.getParameter("radioPedido");
                if("aceito".equals(decisao)){
                    pedido.changeStatusPedido(idPedido.toString(),StatusPedido.RENOVACAO_STEP4);
                    session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidosOfDocente(docente.getDocenteByEmail(emailDocente.toString()).get()));
                    RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE);
                    view.forward(req,resp);
                } else if ("rejeitado".equals(decisao)) {
                    pedido.changeStatusPedido(idPedido.toString(),StatusPedido.PEDIDO_ENCERRADO);
                    session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidosOfDocente(docente.getDocenteByEmail(emailDocente.toString()).get()));
                    RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE);
                    view.forward(req,resp);
                }
            }else if ("step4".equals(req.getParameter("submitButtonDocenteAnalisaAtividadeTCE"))) {
                Pedido pedido = new Pedido();
                Docente docente = new Docente();
                Documento documento = new Documento();
                HttpSession session = req.getSession();
                Object idPedido = session.getAttribute("ID_PEDIDO");
                Object emailDocente = session.getAttribute("EMAIL_DOCENTE");
                String decisao = req.getParameter("radioPedido");
                if ("rejeitado".equals(decisao)){
                    String justificativa = req.getParameter("textAreaPedido");
                    String corrigir = req.getParameter("radioCorrigir");
                    if ("planoAtividades".equals(corrigir)){
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP4_PLANO_ATIVIDADES);
                        S3Util.deleteFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome());
                    }else if ("tce".equals(corrigir)) {
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP4_TCE);
                        S3Util.deleteFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                    }else if ("planoAtividadesTCE".equals(corrigir)) {
                        pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP4_ATIVIDADES_TCE);
                        S3Util.deleteFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome());
                        S3Util.deleteFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                    }
                    pedido.changeJustificativaDocumentacaoPedido(idPedido.toString(),justificativa);
                    session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidosOfDocente(docente.getDocenteByEmail(emailDocente.toString()).get()));
                    RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE);
                    view.forward(req,resp);
                }else if("aceito".equals(decisao)){
                    pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP4_DOCENTE_ASSINADO);
                    session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidosOfDocente(docente.getDocenteByEmail(emailDocente.toString()).get()));
                    session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome()));
                    session.setAttribute("TCE_URL", S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome()));
                    session.setAttribute("PLANO_ATIVIDADES", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get());
                    session.setAttribute("TCE", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get());
                    session.setAttribute("NOVO_ESTAGIO", pedido.getPedidoById(Long.parseLong(idPedido.toString())));
                    RequestDispatcher view = req.getRequestDispatcher(NOVO_STEP4_ASSINAR);
                    view.forward(req,resp);
                }
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDocenteAssinar"))) {
                HttpSession session = req.getSession();
                Pedido pedido = new Pedido();
                Docente docente = new Docente();
                Documento documento = new Documento();
                if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name())){
                    if ("NOVO_STEP4_DOCENTE_ASSINADO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
                        if (req.getParts().stream().anyMatch(part -> "planoAtividadesAssinado".equals(part.getName()))){
                            FileUtil.armazenarDocumentoAssinadoDocente(docente.getDocenteByEmail(session.getAttribute("EMAIL_DOCENTE").toString()).get(), req.getParts().stream().filter(part -> "planoAtividadesAssinado".equals(part.getName())).findFirst().get());
                            if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DOCENTE).isPresent()){
                                documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DOCENTE);
                            }
                            FileUtil.criarDocumentoDocente(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), docente.getDocenteByEmail(session.getAttribute("EMAIL_DOCENTE").toString()).get(), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DOCENTE, req.getParts().stream().filter(part -> "planoAtividadesAssinado".equals(part.getName())).findFirst());
                            S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DOCENTE).get().getNome());
                        }
                        if (req.getParts().stream().anyMatch(part -> "tceAssinado".equals(part.getName()))){
                            FileUtil.armazenarDocumentoAssinadoDocente(docente.getDocenteByEmail(session.getAttribute("EMAIL_DOCENTE").toString()).get(), req.getParts().stream().filter(part -> "tceAssinado".equals(part.getName())).findFirst().get());
                            if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DOCENTE).isPresent()){
                                documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DOCENTE);
                            }
                            FileUtil.criarDocumentoDocente(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), docente.getDocenteByEmail(session.getAttribute("EMAIL_DOCENTE").toString()).get(), TipoDocumento.TCE_ASSINADO_DOCENTE, req.getParts().stream().filter(part -> "tceAssinado".equals(part.getName())).findFirst());
                            S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DOCENTE).get().getNome());
                        }
                        pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_PEDIDO_FIM);
                        session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
                        RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE);
                        view.forward(req, resp);
                    }
                }
            }else if ("novoDocente".equals(req.getParameter("submitButtonDocenteCadastroNovoDocente"))) {
                Docente docente = new Docente();
                Departamento departamento = new Departamento();
                DocenteMapperImpl docenteMapper = new DocenteMapperImpl();
                HttpSession session = req.getSession();
                boolean isComissao= false;
                if ("comissao".equals(req.getParameter("radioComissao"))){
                    isComissao=true;
                }
                docente.criarDocente(docenteMapper.toDocenteCreateAccount(DocenteCreationDTO.builder().nome(req.getParameter("nomeDocente"))
                        .email(req.getParameter("emailDocente"))
                        .senha(req.getParameter("senhaDocente"))
                        .siape(req.getParameter("siapeDocente"))
                        .departamento(departamento.getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento"))))
                        .isDocenteComissao(isComissao).build(), docente.getDocenteByEmail(session.getAttribute("EMAIL_DOCENTE").toString()).get()));
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE_COMISSAO);
                view.forward(req,resp);
            }else if ("novoCurso".equals(req.getParameter("submitButtonDocenteCadastroNovoCurso"))) {
                Curso curso = new Curso();
                Departamento departamento = new Departamento();
                CursoMapperImpl cursoMapper = new CursoMapperImpl();
                curso.criarCurso(cursoMapper.toDocenteCreateCurso(CursoCreationDTO.builder().nome(req.getParameter("nomeCurso"))
                        .departamento(departamento.getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento"))))
                        .build()));
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE_COMISSAO);
                view.forward(req,resp);
            }else if ("novoDepartamento".equals(req.getParameter("submitButtonDocenteCadastroDepartamento"))) {
                Departamento departamento = new Departamento();
                DepartamentoMapperImpl departamentoMapper = new DepartamentoMapperImpl();
                departamento.criarDepartamento(departamentoMapper.toDocenteCreateDepartamento(DepartamentoCreationDTO.builder().nome(req.getParameter("nomeDepartamento"))
                        .sigla(req.getParameter("siglaDepartamento"))
                        .build()));
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE_COMISSAO);
                view.forward(req,resp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getContextParameter(String name) {
        return getServletContext().getInitParameter(name);
    }
}
