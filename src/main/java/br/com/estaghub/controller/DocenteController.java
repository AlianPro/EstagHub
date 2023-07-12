package br.com.estaghub.controller;

import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.service.DocumentoService;
import br.com.estaghub.service.EmailService;
import br.com.estaghub.util.S3Util;
import br.com.estaghub.util.ServletUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet(value = "/docenteController")
public class DocenteController extends HttpServlet {
    private static final String PEDIDOS_ORIENTADOR = "/pedidosDocente.jsp";
    private static final String NOVO_STEP4_ASSINAR = "/assinarDocumentoDocente.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            if ("editarDocente".equals(req.getParameter("submitButtonEditDocente"))) {
                editDocente(req, resp);
            }else if ("excluirDocente".equals(req.getParameter("submitButtonExcluirDocente"))) {
                Docente currentDocente = (Docente) req.getSession().getAttribute("DOCENTE");
                currentDocente.setIsActive(false);
                currentDocente.deleteDocente(currentDocente);
                req.getSession().invalidate();
                resp.getWriter().write("{\"excluirDocente\": true}");
            }else if ("pedido".equals(req.getParameter("buttonPedido"))) {
                visualizarPedido(req, resp);
            }else if ("step3".equals(req.getParameter("submitButtonDocenteAnalisaPedido"))) {
                orientadorAnalisarPedido(req, resp);
            }else if ("step4".equals(req.getParameter("submitButtonDocenteAnalisaAtividadeTCE"))) {
                analisarPlanoAtividadesTCE(req, resp);
            }else if (req.getContentType().startsWith("multipart/form-data")) {
                List<FileItem> fileItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
                if (fileItems.stream().anyMatch(fileItem -> "submitButtonDocenteAssinar".equals(fileItem.getFieldName()))){
                    assinarDocumentos(req, resp, fileItems);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void editDocente(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Docente currentDocente = (Docente) req.getSession().getAttribute("DOCENTE");
        Docente docenteToBeUpdated = new Docente();
        docenteToBeUpdated.setId(currentDocente.getId());
        boolean isEdited = false;
        if(Strings.isNotBlank(req.getParameter("nomeDocente")) && !req.getParameter("nomeDocente").equals(currentDocente.getNome())){
            docenteToBeUpdated.setNome(req.getParameter("nomeDocente"));
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("siapeDocente")) && !req.getParameter("siapeDocente").equals(currentDocente.getSiape())){
            docenteToBeUpdated.setSiape(req.getParameter("siapeDocente"));
            isEdited = true;
        }
        if (Objects.nonNull(docenteToBeUpdated.getSiape())? currentDocente.checkSiapeOfDocente(docenteToBeUpdated) : false){
            resp.getWriter().write("{\"message\": \"Já existe um(a) docente com essa matrícula siape informada!\"}");
        }else if (!isEdited){
            resp.getWriter().write("{\"message\": \"Não houve nenhuma alteração nesse perfil!\"}");
        }else{
            currentDocente.editProfileDocente(docenteToBeUpdated);
            resp.getWriter().write("{\"editarDocente\": true}");
        }
    }

    private static void assinarDocumentos(HttpServletRequest req, HttpServletResponse resp, List<FileItem> fileItems) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        String idPedido = session.getAttribute("ID_PEDIDO").toString();
        Documento documento = new Documento();
        if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido)).getTipo().name()) && "NOVO_STEP4_DOCENTE_ASSINADO".equals(pedido.getPedidoById(Long.parseLong(idPedido)).getStatus().name())){
            if (fileItems.stream().anyMatch(fileItem -> "planoAtividadesAssinadoDocente".equals(fileItem.getFieldName()))){
                assinarDocumento(req, session, fileItems, documento, "planoAtividadesAssinadoDocente", TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DOCENTE);
            }
            if (fileItems.stream().anyMatch(fileItem -> "tceAssinadoDocente".equals(fileItem.getFieldName()))){
                assinarDocumento(req, session, fileItems, documento, "tceAssinadoDocente", TipoDocumento.TCE_ASSINADO_DOCENTE);
            }
            pedido.changeStatusPedido(idPedido,StatusPedido.NOVO_PEDIDO_FIM);
            req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocente((Docente) session.getAttribute("DOCENTE")));
            Pedido pedidoToBeSign = pedido.getPedidoById(Long.parseLong(idPedido));
            req.getRequestDispatcher(PEDIDOS_ORIENTADOR).forward(req, resp);
            EmailService.sendInfoAboutPedido(req,pedidoToBeSign.getDiscente().getEmail(), pedidoToBeSign.getId().toString(),"O seu pedido <strong>#" + pedidoToBeSign.getId() + "</strong> recebeu uma atualização, verifique o sistema!");
        }
    }

    private static void assinarDocumento(HttpServletRequest req, HttpSession session, List<FileItem> fileItems, Documento documento, String nomeDocumento, TipoDocumento tipoDocumento) throws IOException, ServletException {
        DocumentoService.armazenarDocumento(((Docente) session.getAttribute("DOCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), fileItems.stream().filter(fileItem -> nomeDocumento.equals(fileItem.getFieldName())).findFirst().get());
        DocumentoService.criarDocumento((Pedido) session.getAttribute("PEDIDO"), ((Docente) session.getAttribute("DOCENTE")).getId().toString(), tipoDocumento, fileItems.stream().filter(fileItem -> nomeDocumento.equals(fileItem.getFieldName())).findFirst());
        S3Util.uploadFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
    }

    private static void analisarPlanoAtividadesTCE(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        Documento documento = new Documento();
        HttpSession session = req.getSession();
        String idPedido = session.getAttribute("ID_PEDIDO").toString();
        String decisao = req.getParameter("radioPedido");
        if ("rejeitado".equals(decisao)){
            String justificativa = req.getParameter("textAreaPedido");
            String corrigir = req.getParameter("radioCorrigir");
            if ("planoAtividades".equals(corrigir)){
                pedido.changeStatusPedido(idPedido,StatusPedido.NOVO_STEP4_PLANO_ATIVIDADES);
                Documento currentDocumentoPlanoAtividadesAssinadoDiscente = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get();
                deleteDocumentoAssinado(req, idPedido, currentDocumentoPlanoAtividadesAssinadoDiscente, TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE);
            }else if ("tce".equals(corrigir)) {
                pedido.changeStatusPedido(idPedido,StatusPedido.NOVO_STEP4_TCE);
                Documento currentDocumentoTCEAssinadoDiscente = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.TCE_ASSINADO_DISCENTE).get();
                deleteDocumentoAssinado(req, idPedido, currentDocumentoTCEAssinadoDiscente, TipoDocumento.TCE_ASSINADO_DISCENTE);
            }else if ("planoAtividadesTCE".equals(corrigir)) {
                pedido.changeStatusPedido(idPedido,StatusPedido.NOVO_STEP4_ATIVIDADES_TCE);
                Documento currentDocumentoPlanoAtividadesAssinadoDiscente = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get();
                Documento currentDocumentoTCEAssinadoDiscente = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.TCE_ASSINADO_DISCENTE).get();
                deleteDocumentoAssinado(req, idPedido, currentDocumentoPlanoAtividadesAssinadoDiscente, TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE);
                deleteDocumentoAssinado(req, idPedido, currentDocumentoTCEAssinadoDiscente, TipoDocumento.TCE_ASSINADO_DISCENTE);
            }
            pedido.changeJustificativaDocumentacaoPedido(idPedido,justificativa);
            req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocente((Docente) session.getAttribute("DOCENTE")));
            Pedido pedidoToBeAnalyzed = pedido.getPedidoById(Long.parseLong(idPedido));
            req.getRequestDispatcher(PEDIDOS_ORIENTADOR).forward(req, resp);
            EmailService.sendInfoAboutPedido(req,pedidoToBeAnalyzed.getDiscente().getEmail(), pedidoToBeAnalyzed.getId().toString(),"O seu pedido <strong>#" + pedidoToBeAnalyzed.getId() + "</strong> recebeu uma atualização, verifique o sistema!");
        }else if("aceito".equals(decisao)){
            pedido.changeStatusPedido(idPedido,StatusPedido.NOVO_STEP4_DOCENTE_ASSINADO);
            getPlanoAtividadesTCE(req, session, documento, idPedido);
            Pedido pedidoToBeAnalyzed = pedido.getPedidoById(Long.parseLong(idPedido));
            session.setAttribute("PEDIDO", pedidoToBeAnalyzed);
            req.getRequestDispatcher(NOVO_STEP4_ASSINAR).forward(req, resp);
            EmailService.sendInfoAboutPedido(req,pedidoToBeAnalyzed.getDiscente().getEmail(), pedidoToBeAnalyzed.getId().toString(),"O seu pedido <strong>#" + pedidoToBeAnalyzed.getId() + "</strong> recebeu uma atualização, verifique o sistema!");
        }
    }

    private static void deleteDocumentoAssinado(HttpServletRequest req, String idPedido, Documento documentoAssinado, TipoDocumento tipoDocumento) {
        DocumentoService.deleteFile(documentoAssinado.getNome());
        S3Util.deleteFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"), documentoAssinado.getNome());
        documentoAssinado.removeDocumento(Long.parseLong(idPedido),tipoDocumento);
    }

    private static void orientadorAnalisarPedido(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        HttpSession session = req.getSession();
        String decisao = req.getParameter("radioPedido");
        if("aceito".equals(decisao)){
            decisaoAnaliseOrientador(req, resp, pedido, session, StatusPedido.RENOVACAO_STEP4);
        }else if ("rejeitado".equals(decisao)) {
            String justificativa = req.getParameter("textAreaPedido");
            pedido.changeJustificativaRecursoPedido(session.getAttribute("ID_PEDIDO").toString(),justificativa);
            decisaoAnaliseOrientador(req, resp, pedido, session, StatusPedido.PEDIDO_ENCERRADO);
            Pedido pedidoToBeAnalyzed = (Pedido) req.getSession().getAttribute("PEDIDO");
            EmailService.sendInfoAboutPedido(req,pedidoToBeAnalyzed.getDiscente().getEmail(), pedidoToBeAnalyzed.getId().toString(),"O(a) docente orientador(a) rejeitou o seu pedido <strong>#" + pedidoToBeAnalyzed.getId() + "</strong> pelo seguinte motivo: "+justificativa);
            new Documento().getAllDocumentosFromThatPedido(Long.parseLong(req.getSession().getAttribute("ID_PEDIDO").toString())).forEach(currentDocumento -> {
                DocumentoService.deleteFile(currentDocumento.getNome());
                S3Util.deleteFileS3(ServletUtil.getContextParameter(req, "access-key"), ServletUtil.getContextParameter(req, "secret-key"), ServletUtil.getContextParameter(req, "estaghub-bucket"), currentDocumento.getNome());
            });
        }
    }

    private static void decisaoAnaliseOrientador(HttpServletRequest req, HttpServletResponse resp, Pedido pedido, HttpSession session, StatusPedido statusPedido) throws ServletException, IOException {
        Pedido pedidoToBeAnalyzed = pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()));
        pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),statusPedido);
        req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocente((Docente) session.getAttribute("DOCENTE")));
        req.getRequestDispatcher(PEDIDOS_ORIENTADOR).forward(req, resp);
        if ("RENOVACAO_STEP4".equals(statusPedido.name())){
            EmailService.sendInfoAboutPedido(req,pedidoToBeAnalyzed.getDiscente().getEmail(), pedidoToBeAnalyzed.getId().toString(),"O seu pedido <strong>#" + pedidoToBeAnalyzed.getId() + "</strong> recebeu uma atualização, verifique o sistema!");
        }
    }

    private static void visualizarPedido(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        HttpSession session = req.getSession();
        Documento documento = new Documento();
        session.setAttribute("ID_PEDIDO", req.getParameter("idPedido"));
        session.setAttribute("PEDIDO", pedido.getPedidoById(Long.parseLong(req.getParameter("idPedido"))));
        String statusPedido = req.getParameter("statusPedido");
        if (StatusPedido.NOVO_STEP4.name().equals(statusPedido) || StatusPedido.NOVO_STEP4_DOCENTE_ASSINADO.name().equals(statusPedido)){
            getPlanoAtividadesTCE(req, session, documento, req.getParameter("idPedido"));
        }
        resp.getWriter().write("{\"visualizarPedido\": true}");
    }

    private static void getPlanoAtividadesTCE(HttpServletRequest req, HttpSession session, Documento documento, String idPedido) {
        Documento currentDocumentoPlanoAtividadesAssinadoDiscente = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get();
        Documento currentDocumentoTCEAssinadoDiscente = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.TCE_ASSINADO_DISCENTE).get();
        session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"), currentDocumentoPlanoAtividadesAssinadoDiscente.getNome()));
        session.setAttribute("TCE_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"), currentDocumentoTCEAssinadoDiscente.getNome()));
        session.setAttribute("PLANO_ATIVIDADES", currentDocumentoPlanoAtividadesAssinadoDiscente);
        session.setAttribute("TCE", currentDocumentoTCEAssinadoDiscente);
    }
}
