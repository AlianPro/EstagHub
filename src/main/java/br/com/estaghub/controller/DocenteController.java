package br.com.estaghub.controller;

import br.com.estaghub.domain.*;
import br.com.estaghub.dto.CursoCreationDTO;
import br.com.estaghub.dto.DepartamentoCreationDTO;
import br.com.estaghub.dto.DocenteCreationDTO;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.mapper.impl.CursoMapperImpl;
import br.com.estaghub.mapper.impl.DepartamentoMapperImpl;
import br.com.estaghub.mapper.impl.DocenteMapperImpl;
import br.com.estaghub.util.FileUtil;
import br.com.estaghub.util.S3Util;

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
    private static final String SUCESS_DOCENTE_COMISSAO = "/docenteComissao.jsp";
    private static final String LOGOUT = "/index.jsp";
    private static final String PEDIDOS_COMISSAO = "/pedidosDocenteComissao.jsp";
    private static final String PEDIDOS_ORIENTADOR = "/pedidosDocente.jsp";
    private static final String NOVO_STEP2_JUSTIFICADO = "/novoStep2Justificado.jsp";
    private static final String NOVO_STEP3 = "/emitirTCE.jsp";
    private static final String NOVO_STEP4 = "/novoStep4.jsp";
    private static final String RENOVACAO_STEP4 = "/renovacaoStep4.jsp";
    private static final String NOVO_STEP4_ASSINAR = "/assinarDocumentoDocente.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            if ("logout".equals(req.getParameter("buttonLogoutDocenteComissao"))){
                req.getSession().invalidate();
                req.getRequestDispatcher(LOGOUT).forward(req,resp);
            }else if ("pedido".equals(req.getParameter("buttonPedido"))) {
                visualizarPedido(req, resp);
            }else if ("step1".equals(req.getParameter("submitButtonDocenteAnalisaNovoPedido1"))) {
                analisarPedido(req, resp);
            }else if ("step2".equals(req.getParameter("submitButtonDocenteAnalisaRecursoNovoPedido2"))) {
                analisarRecurso(req, resp);
            }else if ("step3".equals(req.getParameter("submitButtonDocenteAnalisaPedido"))) {
                orientadorAnalisarPedido(req, resp);
            }else if ("step4".equals(req.getParameter("submitButtonDocenteAnalisaAtividadeTCE"))) {
                analisarPlanoAtividadesTCE(req, resp);
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDocenteAssinar"))) {
                assinarDocumentos(req, resp);
            }else if ("novoDocente".equals(req.getParameter("submitButtonDocenteCadastroNovoDocente"))) {
                criarDocente(req, resp);
            }else if ("novoCurso".equals(req.getParameter("submitButtonDocenteCadastroNovoCurso"))) {
                criarCurso(req, resp);
            }else if ("novoDepartamento".equals(req.getParameter("submitButtonDocenteCadastroDepartamento"))) {
                criarDepartamento(req, resp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void criarDepartamento(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Departamento departamento = new Departamento();
        DepartamentoMapperImpl departamentoMapper = new DepartamentoMapperImpl();
        departamento.criarDepartamento(departamentoMapper.toDocenteCreateDepartamento(DepartamentoCreationDTO.builder().nome(req.getParameter("nomeDepartamento"))
                .sigla(req.getParameter("siglaDepartamento"))
                .build()));
        req.getRequestDispatcher(SUCESS_DOCENTE_COMISSAO).forward(req, resp);
    }

    private static void criarCurso(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Curso curso = new Curso();
        Departamento departamento = new Departamento();
        CursoMapperImpl cursoMapper = new CursoMapperImpl();
        curso.criarCurso(cursoMapper.toDocenteCreateCurso(CursoCreationDTO.builder().nome(req.getParameter("nomeCurso"))
                .departamento(departamento.getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento"))))
                .build()));
        req.getRequestDispatcher(SUCESS_DOCENTE_COMISSAO).forward(req, resp);
    }

    private static void criarDocente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        req.getRequestDispatcher(SUCESS_DOCENTE_COMISSAO).forward(req, resp);
    }

    private static void assinarDocumentos(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        Docente docente = new Docente();
        Documento documento = new Documento();
        Object emailDocente = session.getAttribute("EMAIL_DOCENTE");
        if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name())){
            if ("NOVO_STEP4_DOCENTE_ASSINADO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
                if (req.getParts().stream().anyMatch(part -> "planoAtividadesAssinado".equals(part.getName()))){
                    assinarDocumento(req, session, pedido, docente, documento, "planoAtividadesAssinado", TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DOCENTE);
                }
                if (req.getParts().stream().anyMatch(part -> "tceAssinado".equals(part.getName()))){
                    assinarDocumento(req, session, pedido, docente, documento, "tceAssinado", TipoDocumento.TCE_ASSINADO_DOCENTE);
                }
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_PEDIDO_FIM);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
                session.setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocente(docente.getDocenteByEmail(emailDocente.toString()).get()));
                req.getRequestDispatcher(PEDIDOS_ORIENTADOR).forward(req, resp);
            }
        }
    }

    private static void assinarDocumento(HttpServletRequest req, HttpSession session, Pedido pedido, Docente docente, Documento documento, String nomeDocumento, TipoDocumento tipoDocumento) throws IOException, ServletException {
        FileUtil.armazenarDocumentoAssinadoDocente(docente.getDocenteByEmail(session.getAttribute("EMAIL_DOCENTE").toString()).get(), req.getParts().stream().filter(part -> nomeDocumento.equals(part.getName())).findFirst().get());
        FileUtil.criarDocumentoDocente(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), docente.getDocenteByEmail(session.getAttribute("EMAIL_DOCENTE").toString()).get(), tipoDocumento, req.getParts().stream().filter(part -> nomeDocumento.equals(part.getName())).findFirst());
        S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
    }

    private static void analisarPlanoAtividadesTCE(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                FileUtil.deleteFile(documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome());
                S3Util.deleteFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome());
                documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE);
            }else if ("tce".equals(corrigir)) {
                pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP4_TCE);
                FileUtil.deleteFile(documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                S3Util.deleteFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE);
            }else if ("planoAtividadesTCE".equals(corrigir)) {
                pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP4_ATIVIDADES_TCE);
                FileUtil.deleteFile(documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome());
                FileUtil.deleteFile(documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                S3Util.deleteFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome());
                S3Util.deleteFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE);
                documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE);
            }
            pedido.changeJustificativaDocumentacaoPedido(idPedido.toString(),justificativa);
            session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidosOfDocente(docente.getDocenteByEmail(emailDocente.toString()).get()));
            req.getRequestDispatcher(PEDIDOS_ORIENTADOR).forward(req, resp);
        }else if("aceito".equals(decisao)){
            pedido.changeStatusPedido(idPedido.toString(),StatusPedido.NOVO_STEP4_DOCENTE_ASSINADO);
            session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidosOfDocente(docente.getDocenteByEmail(emailDocente.toString()).get()));
            session.setAttribute("PLANO_ATIVIDADES", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get());
            session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome()));
            session.setAttribute("TCE", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get());
            session.setAttribute("TCE_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido.toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome()));
            session.setAttribute("NOVO_ESTAGIO", pedido.getPedidoById(Long.parseLong(idPedido.toString())));
            req.getRequestDispatcher(NOVO_STEP4_ASSINAR).forward(req, resp);
        }
    }

    private static void orientadorAnalisarPedido(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        Docente docente = new Docente();
        HttpSession session = req.getSession();
        Object idPedido = session.getAttribute("ID_PEDIDO");
        Object emailDocente = session.getAttribute("EMAIL_DOCENTE");
        String decisao = req.getParameter("radioPedido");
        if("aceito".equals(decisao)){
            decisaoAnaliseOrientador(req, resp, pedido, docente, session, idPedido, emailDocente, StatusPedido.RENOVACAO_STEP4);
        } else if ("rejeitado".equals(decisao)) {
            decisaoAnaliseOrientador(req, resp, pedido, docente, session, idPedido, emailDocente, StatusPedido.PEDIDO_ENCERRADO);
        }
    }

    private static void decisaoAnaliseOrientador(HttpServletRequest req, HttpServletResponse resp, Pedido pedido, Docente docente, HttpSession session, Object idPedido, Object emailDocente, StatusPedido statusPedido) throws ServletException, IOException {
        pedido.changeStatusPedido(idPedido.toString(),statusPedido);
        session.setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocente(docente.getDocenteByEmail(emailDocente.toString()).get()));
        req.getRequestDispatcher(PEDIDOS_ORIENTADOR).forward(req, resp);
    }

    private static void analisarRecurso(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        Docente docente = new Docente();
        HttpSession session = req.getSession();
        Object idPedido = session.getAttribute("ID_PEDIDO");
        String decisao = req.getParameter("radioPedido");
        if ("rejeitado".equals(decisao)){
            String justificativa = req.getParameter("textAreaPedido");
            pedido.changeStatusPedido(idPedido.toString(),StatusPedido.PEDIDO_ENCERRADO);
            pedido.changeJustificativaRecursoPedido(idPedido.toString(),justificativa);
            session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
            req.getRequestDispatcher(PEDIDOS_COMISSAO).forward(req, resp);
        }else if("aceito".equals(decisao)){
            if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido.toString())).getTipo().name())){
                aceitarRecurso(req, resp, pedido, docente, session, idPedido, StatusPedido.NOVO_STEP2);
            }else{
                aceitarRecurso(req, resp, pedido, docente, session, idPedido, StatusPedido.RENOVACAO_STEP3);
            }
        }
    }

    private static void aceitarRecurso(HttpServletRequest req, HttpServletResponse resp, Pedido pedido, Docente docente, HttpSession session, Object idPedido, StatusPedido statusPedido) throws ServletException, IOException {
        String idDocente = req.getParameter("selectDocenteOrientador");
        pedido.changeStatusPedido(idPedido.toString(),statusPedido);
        pedido.addDocenteOrientadorInPedido(idPedido.toString(), docente.getDocenteById(Long.parseLong(idDocente)));
        session.setAttribute("LIST_PEDIDOS", pedido.getAllPedidos());
        req.getRequestDispatcher(PEDIDOS_COMISSAO).forward(req, resp);
    }

    private static void analisarPedido(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        Docente docente = new Docente();
        HttpSession session = req.getSession();
        Object idPedido = session.getAttribute("ID_PEDIDO");
        Object emailDocente = session.getAttribute("EMAIL_DOCENTE");
        String decisao = req.getParameter("radioPedido");
        if ("rejeitado".equals(decisao)){
            if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido.toString())).getTipo().name())) {
                rejeitarPedido(req, resp, pedido, docente, session, idPedido, emailDocente, StatusPedido.NOVO_STEP2_REJEITADO);
            }else{
                rejeitarPedido(req, resp, pedido, docente, session, idPedido, emailDocente, StatusPedido.RENOVACAO_STEP3_REJEITADO);
            }
        }else if("aceito".equals(decisao)){
            if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido.toString())).getTipo().name())){
                aceitarPedido(req, resp, pedido, docente, session, idPedido, emailDocente, StatusPedido.NOVO_STEP2);
            }else{
                aceitarPedido(req, resp, pedido, docente, session, idPedido, emailDocente, StatusPedido.RENOVACAO_STEP3);
            }
        }
    }

    private static void rejeitarPedido(HttpServletRequest req, HttpServletResponse resp, Pedido pedido, Docente docente, HttpSession session, Object idPedido, Object emailDocente, StatusPedido statusPedido) throws ServletException, IOException {
        String justificativa = req.getParameter("textAreaPedido");
        pedido.changeStatusPedido(idPedido.toString(),statusPedido);
        pedido.changeJustificativaDocentePedido(idPedido.toString(),justificativa);
        pedido.addDocenteComissaoInPedido(idPedido.toString(), docente.getDocenteByEmail(emailDocente.toString()).get());
        session.setAttribute("LIST_PEDIDOS", pedido.getAllPedidos());
        req.getRequestDispatcher(PEDIDOS_COMISSAO).forward(req, resp);
    }

    private static void aceitarPedido(HttpServletRequest req, HttpServletResponse resp, Pedido pedido, Docente docente, HttpSession session, Object idPedido, Object emailDocente, StatusPedido statusPedido) throws ServletException, IOException {
        String idDocente = req.getParameter("selectDocenteOrientador");
        pedido.changeStatusPedido(idPedido.toString(),statusPedido);
        pedido.addDocenteComissaoInPedido(idPedido.toString(), docente.getDocenteByEmail(emailDocente.toString()).get());
        pedido.addDocenteOrientadorInPedido(idPedido.toString(), docente.getDocenteById(Long.parseLong(idDocente)));
        session.setAttribute("LIST_PEDIDOS", pedido.getAllPedidos());
        req.getRequestDispatcher(PEDIDOS_COMISSAO).forward(req, resp);
    }

    private static void visualizarPedido(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        if (StatusPedido.NOVO_STEP1.name().equals(statusPedido)) {
            getHistoricoGradeHorario(req, session, documento, idPedido);
        }else if (StatusPedido.NOVO_STEP2_JUSTIFICADO.name().equals(statusPedido)){
            session.setAttribute("DOCENTES_NAO_COMISSAO",docente.getAllDocentesNoComissao());
            req.getRequestDispatcher(NOVO_STEP2_JUSTIFICADO).forward(req, resp);
        }else if (StatusPedido.NOVO_STEP2.name().equals(statusPedido)){
            req.getRequestDispatcher(NOVO_STEP3).forward(req, resp);
        }else if (StatusPedido.NOVO_STEP3.name().equals(statusPedido)){
            req.getRequestDispatcher(NOVO_STEP4).forward(req, resp);
        }else if (StatusPedido.NOVO_STEP4.name().equals(statusPedido)){
            getPlanoAtividadesTCE(req, session, documento, idPedido);
            req.getRequestDispatcher(NOVO_STEP4).forward(req, resp);
        }else if (StatusPedido.NOVO_STEP4_DOCENTE_ASSINADO.name().equals(statusPedido)){
            getPlanoAtividadesTCE(req, session, documento, idPedido);
            req.getRequestDispatcher(NOVO_STEP4_ASSINAR).forward(req, resp);
        }else if (StatusPedido.RENOVACAO_STEP2.name().equals(statusPedido)){
            getHistoricoGradeHorario(req, session, documento, idPedido);
        }else if (StatusPedido.RENOVACAO_STEP3.name().equals(statusPedido)){
            req.getRequestDispatcher(RENOVACAO_STEP4).forward(req, resp);
        }
    }

    private static void getHistoricoGradeHorario(HttpServletRequest req, HttpSession session, Documento documento, String idPedido) {
        session.setAttribute("HISTORICO_ACADEMICO_URL",S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.HISTORICO_ACADEMICO).get().getNome()));
        session.setAttribute("GRADE_HORARIO_URL",S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.GRADE_HORARIO).get().getNome()));
        session.setAttribute("HISTORICO_ACADEMICO", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.HISTORICO_ACADEMICO).get());
        session.setAttribute("GRADE_HORARIO", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.GRADE_HORARIO).get());
    }

    private static void getPlanoAtividadesTCE(HttpServletRequest req, HttpSession session, Documento documento, String idPedido) {
        session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome()));
        session.setAttribute("TCE_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome()));
        session.setAttribute("PLANO_ATIVIDADES", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get());
        session.setAttribute("TCE", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.TCE_ASSINADO_DISCENTE).get());
    }
}
