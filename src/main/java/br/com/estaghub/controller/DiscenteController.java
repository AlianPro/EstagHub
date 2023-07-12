package br.com.estaghub.controller;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.embeddable.PlanoAtividades;
import br.com.estaghub.domain.embeddable.RenovacaoEstagio;
import br.com.estaghub.domain.embeddable.TCE;
import br.com.estaghub.domain.embeddable.TermoAditivo;
import br.com.estaghub.dto.*;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.mapper.impl.DiscenteMapperImpl;
import br.com.estaghub.mapper.impl.PlanoAtividadesMapperImpl;
import br.com.estaghub.mapper.impl.TCEMapperImpl;
import br.com.estaghub.mapper.impl.TermoAditivoMapperImpl;
import br.com.estaghub.service.DocumentoService;
import br.com.estaghub.service.EmailService;
import br.com.estaghub.util.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(value = "/discenteController")
public class DiscenteController extends HttpServlet {
    private static final String SUCESS_DISCENTE = "/discente.jsp";
    private static final String NOVO_STEP3 = "/emitirTCE.jsp";
    private static final String DISCENTE_ASSINA_DOCUMENTO = "/assinarDocumentoDiscente.jsp";
    private static final String FINAL_STEP = "/finalStep.jsp";
    private static final String NOVO_ESTAGIO_REJEITADO = "/justificativaNovoEstagio.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            if ("editarDiscente".equals(req.getParameter("submitButtonEditDiscente"))) {
                editDiscente(req, resp);
            }else if ("excluirDiscente".equals(req.getParameter("submitButtonExcluirDiscente"))) {
                Discente currentDiscente = (Discente) req.getSession().getAttribute("DISCENTE");
                currentDiscente.setIsActive(false);
                currentDiscente.deleteDiscente(currentDiscente);
                req.getSession().invalidate();
                resp.getWriter().write("{\"excluirDiscente\": true}");
            }else if ("pedido".equals(req.getParameter("buttonPedido"))) {
                visualizarPedido(req, resp);
            }else if ("justificativa".equals(req.getParameter("submitButtonDiscenteJustificativa2"))){
               discenteJustificativa(req, resp);
            }else if ("step3".equals(req.getParameter("submitButtonDiscenteElaborarPlanoAtividade"))){
                discenteElaborarPlanoAtividades(req, resp);
            }else if ("finalizarPedido".equals(req.getParameter("submitButtonDiscenteFinalizar"))){
                discenteFinalizarPedido(req, resp);
            }else if (req.getContentType().startsWith("multipart/form-data")) {
                List<FileItem> fileItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
                if (fileItems.stream().anyMatch(fileItem -> fileItem.getFieldName().equals("submitButtonDiscenteCadastroNovoEstagio1"))){
                    discenteNovoEstagioStep(req, resp, fileItems);
                }else if (fileItems.stream().anyMatch(fileItem -> fileItem.getFieldName().equals("submitButtonDiscenteElaborarTCE"))){
                    discenteElaborarTCE(req, resp, fileItems);
                }else if (fileItems.stream().anyMatch(fileItem -> fileItem.getFieldName().equals("submitButtonDiscenteAssinar"))){
                    discenteAssinarDocumento(req, resp, fileItems);
                }else if (fileItems.stream().anyMatch(fileItem -> fileItem.getFieldName().equals("submitButtonDiscenteCadastroRenovacaoEstagio1"))){
                    discenteRenovacaoEstagio(req, resp, fileItems);
                }else if (fileItems.stream().anyMatch(fileItem -> fileItem.getFieldName().equals("submitButtonDiscenteTermoAditivo"))){
                    discenteElaborarTermoAditivo(req, resp, fileItems);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void editDiscente(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Discente currentDiscente = (Discente) req.getSession().getAttribute("DISCENTE");
        Discente discenteToBeUpdated = new Discente();
        discenteToBeUpdated.setId(currentDiscente.getId());
        boolean isEdited = false;
        if(Strings.isNotBlank(req.getParameter("nomeDiscente")) && !req.getParameter("nomeDiscente").equals(currentDiscente.getNome())){
            discenteToBeUpdated.setNome(req.getParameter("nomeDiscente"));
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("matriculaDiscente")) && !req.getParameter("matriculaDiscente").equals(currentDiscente.getMatricula())){
            discenteToBeUpdated.setMatricula(req.getParameter("matriculaDiscente"));
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("telefoneDiscente")) && !req.getParameter("telefoneDiscente").equals(currentDiscente.getTelefone())){
            discenteToBeUpdated.setTelefone(req.getParameter("telefoneDiscente"));
            isEdited = true;
        }
        if (currentDiscente.getDiscenteByMatricula(discenteToBeUpdated.getMatricula()).isPresent()){
            resp.getWriter().write("{\"message\": \"Já existe um(a) discente com essa matrícula informada!\"}");
        }else if (!isEdited){
            resp.getWriter().write("{\"message\": \"Não houve nenhuma alteração nesse perfil!\"}");
        }else{
            currentDiscente.editProfileDiscente(discenteToBeUpdated);
            resp.getWriter().write("{\"editarDiscente\": true}");
        }
    }

    private static void visualizarPedido(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        HttpSession session = req.getSession();
        Documento documento = new Documento();
        Curso curso = new Curso();
        String tipoPedido = req.getParameter("tipoPedido");
        Discente currentDiscente = (Discente) req.getSession().getAttribute("DISCENTE");
        if ("NOVO".equals(tipoPedido)){
            if (pedido.getPedidoByDiscente(currentDiscente, TipoPedido.NOVO).isPresent()){
                Pedido currentPedidoNovoEstagio = pedido.getPedidoByDiscente(currentDiscente, TipoPedido.NOVO).get();
                if ("NOVO_STEP3".equals(currentPedidoNovoEstagio.getStatus().name()) || "NOVO_STEP4_TCE".equals(currentPedidoNovoEstagio.getStatus().name())){
                    List<String> tceOrTermoAditivoFileS3 = S3Util.getTceOrTermoAditivoFileS3(ServletUtil.getContextParameter(req, "access-key"), ServletUtil.getContextParameter(req, "secret-key"), ServletUtil.getContextParameter(req, "estaghub-bucket"), TipoDocumento.TCE);
                    if (!tceOrTermoAditivoFileS3.get(1).isBlank()){
                        session.setAttribute("TCE_MODELO_UFRRJ", tceOrTermoAditivoFileS3.get(0));
                        session.setAttribute("TCE_MODELO_UFRRJ_URL", tceOrTermoAditivoFileS3.get(1));
                    }
                    if ("NOVO_STEP4_TCE".equals(currentPedidoNovoEstagio.getStatus().name()) && documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.TCE).isPresent()){
                        session.setAttribute("DOCUMENTO",documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.TCE).get());
                    }else{
                        session.setAttribute("DOCUMENTO",null);
                    }
                    resp.getWriter().write("{\"page\": \"TCE\"}");
                }else if ("NOVO_STEP3_DISCENTE_ASSINADO".equals(currentPedidoNovoEstagio.getStatus().name())){
                    if (documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).isEmpty()){
                        Documento currentDocumentoPlanoAtividades = documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.PLANO_ATIVIDADES).get();
                        session.setAttribute("PLANO_ATIVIDADES", currentDocumentoPlanoAtividades);
                        session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumentoPlanoAtividades.getNome()));
                    }
                    if (documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.TCE_ASSINADO_DISCENTE).isEmpty()){
                        Documento currentDocumentoTCE = documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.TCE).get();
                        session.setAttribute("TCE", currentDocumentoTCE);
                        session.setAttribute("TCE_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumentoTCE.getNome()));
                    }
                    resp.getWriter().write("{\"page\": \"ASSINAR\"}");
                }else if (("NOVO_STEP4_PLANO_ATIVIDADES".equals(currentPedidoNovoEstagio.getStatus().name()) || "NOVO_STEP4_ATIVIDADES_TCE".equals(currentPedidoNovoEstagio.getStatus().name())) && documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.PLANO_ATIVIDADES).isPresent()){
                    session.setAttribute("DOCUMENTO",documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.PLANO_ATIVIDADES).get());
                    if ("NOVO_STEP4_ATIVIDADES_TCE".equals(currentPedidoNovoEstagio.getStatus().name())){
                        List<String> tceOrTermoAditivoFileS3 = S3Util.getTceOrTermoAditivoFileS3(ServletUtil.getContextParameter(req, "access-key"), ServletUtil.getContextParameter(req, "secret-key"), ServletUtil.getContextParameter(req, "estaghub-bucket"), TipoDocumento.TCE);
                        if (!tceOrTermoAditivoFileS3.get(1).isBlank()){
                            session.setAttribute("TCE_MODELO_UFRRJ", tceOrTermoAditivoFileS3.get(0));
                            session.setAttribute("TCE_MODELO_UFRRJ_URL", tceOrTermoAditivoFileS3.get(1));
                        }
                    }
                    resp.getWriter().write("{\"page\": \"PLANO_ATIVIDADES\"}");
                }else if ("NOVO_PEDIDO_FIM".equals(currentPedidoNovoEstagio.getStatus().name())){
                    Documento currentDocumentoPlanoAtividadesAssinadoDocente = documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DOCENTE).get();
                    Documento currentDocumentoTCEAssinadoDocente = documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoNovoEstagio.getId(), TipoDocumento.TCE_ASSINADO_DOCENTE).get();
                    session.setAttribute("PLANO_ATIVIDADES", currentDocumentoPlanoAtividadesAssinadoDocente);
                    session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumentoPlanoAtividadesAssinadoDocente.getNome()));
                    session.setAttribute("TCE", currentDocumentoTCEAssinadoDocente);
                    session.setAttribute("TCE_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumentoTCEAssinadoDocente.getNome()));
                    resp.getWriter().write("{\"page\": \"NOVO_PEDIDO_FIM\"}");
                }else if ("NOVO_STEP2".equals(currentPedidoNovoEstagio.getStatus().name())){
                    List<String> tceOrTermoAditivoFileS3 = S3Util.getTceOrTermoAditivoFileS3(ServletUtil.getContextParameter(req, "access-key"), ServletUtil.getContextParameter(req, "secret-key"), ServletUtil.getContextParameter(req, "estaghub-bucket"), TipoDocumento.TCE);
                    if (!tceOrTermoAditivoFileS3.get(1).isBlank()){
                        session.setAttribute("TCE_MODELO_UFRRJ", tceOrTermoAditivoFileS3.get(0));
                        session.setAttribute("TCE_MODELO_UFRRJ_URL", tceOrTermoAditivoFileS3.get(1));
                    }
                    resp.getWriter().write("{\"page\": \"PLANO_ATIVIDADES\"}");
                }else if ("NOVO_STEP2_REJEITADO".equals(currentPedidoNovoEstagio.getStatus().name())){
                    resp.getWriter().write("{\"page\": \"JUSTIFICA\"}");
                }
            }else{
                session.setAttribute("LIST_CURSOS",curso.getAllCursos());
                resp.getWriter().write("{\"page\": \"NOVO\"}");
            }
        }else if("RENOVACAO".equals(tipoPedido)){
            if (pedido.getPedidoByDiscente(currentDiscente, TipoPedido.RENOVACAO).isPresent()){
                Pedido currentPedidoRenovacaoEstagio = pedido.getPedidoByDiscente(currentDiscente, TipoPedido.RENOVACAO).get();
                if("RENOVACAO_STEP4_DISCENTE_ASSINADO".equals(currentPedidoRenovacaoEstagio.getStatus().name())){
                    if (documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoRenovacaoEstagio.getId(), TipoDocumento.TERMO_ADITIVO).isPresent()){
                        Documento currentDocumentoTermoAditivo = documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoRenovacaoEstagio.getId(), TipoDocumento.TERMO_ADITIVO).get();
                        session.setAttribute("TERMO_ADITIVO", currentDocumentoTermoAditivo);
                        session.setAttribute("TERMO_ADITIVO_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumentoTermoAditivo.getNome()));
                    }
                    if (documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoRenovacaoEstagio.getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).isPresent()){
                        getTermoAditivoAssinado(req, currentPedidoRenovacaoEstagio, session, documento);
                    }
                    resp.getWriter().write("{\"page\": \"ASSINAR\"}");
                }else if ("NOVO_PEDIDO_FIM".equals(currentPedidoRenovacaoEstagio.getStatus().name())) {
                    getTermoAditivoAssinado(req, currentPedidoRenovacaoEstagio, session, documento);
                    resp.getWriter().write("{\"page\": \"NOVO_PEDIDO_FIM\"}");
                }else if ("RENOVACAO_STEP4".equals(currentPedidoRenovacaoEstagio.getStatus().name())){
                    List<String> tceOrTermoAditivoFileS3 = S3Util.getTceOrTermoAditivoFileS3(ServletUtil.getContextParameter(req, "access-key"), ServletUtil.getContextParameter(req, "secret-key"), ServletUtil.getContextParameter(req, "estaghub-bucket"), TipoDocumento.TERMO_ADITIVO);
                    if(!tceOrTermoAditivoFileS3.get(1).isBlank()){
                        session.setAttribute("TERMO_ADITIVO_MODELO_UFRRJ", tceOrTermoAditivoFileS3.get(0));
                        session.setAttribute("TERMO_ADITIVO_MODELO_UFRRJ_URL", tceOrTermoAditivoFileS3.get(1));
                    }
                    resp.getWriter().write("{\"page\": \"RENOVACAO_STEP4\"}");
                }else if ("RENOVACAO_STEP3_REJEITADO".equals(currentPedidoRenovacaoEstagio.getStatus().name())) {
                    resp.getWriter().write("{\"page\": \"JUSTIFICA\"}");
                }
            }else{
                session.setAttribute("LIST_CURSOS",curso.getAllCursos());
                resp.getWriter().write("{\"page\": \"RENOVACAO\"}");
            }
        }
    }
    private static void getTermoAditivoAssinado(HttpServletRequest req, Pedido currentPedidoRenovacaoEstagio, HttpSession session, Documento documento) {
        Documento currentDocumentoTermoAditivoAssinadoDiscente = documento.getDocumentoByIdPedidoAndTipoDocumento(currentPedidoRenovacaoEstagio.getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get();
        session.setAttribute("TERMO_ADITIVO", currentDocumentoTermoAditivoAssinadoDiscente);
        session.setAttribute("TERMO_ADITIVO_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumentoTermoAditivoAssinadoDiscente.getNome()));
    }

    private static void discenteJustificativa(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String justificativa="";
        if (Strings.isNotBlank(req.getParameter("textAreaJustificativa"))){
            justificativa = req.getParameter("textAreaJustificativa");
        }
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name())){
            justificativaDiscente(justificativa, session, pedido,"NOVO_ESTAGIO", TipoPedido.NOVO, StatusPedido.NOVO_STEP1, StatusPedido.NOVO_STEP2_JUSTIFICADO);
        }else{
            justificativaDiscente(justificativa, session, pedido, "RENOVACAO_ESTAGIO", TipoPedido.RENOVACAO, StatusPedido.RENOVACAO_STEP1, StatusPedido.RENOVACAO_STEP3_JUSTIFICADO);
        }
        req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
    }

    private static void justificativaDiscente(String justificativa, HttpSession session, Pedido pedido, String nomePedido, TipoPedido tipoPedido, StatusPedido primeiroStatusPedido, StatusPedido segundoStatusPedido) {
        pedido.changeJustificativaDiscentePedido(session.getAttribute("ID_PEDIDO").toString(), justificativa);
        if (!Strings.isNotBlank(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getJustificativaDocente())){
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),primeiroStatusPedido);
            session.setAttribute(nomePedido, pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), tipoPedido).get());
        }else{
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),segundoStatusPedido);
            session.setAttribute(nomePedido, pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), tipoPedido).get());
        }
    }

    private void discenteFinalizarPedido(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        new Pedido().changeStatusPedido(req.getSession().getAttribute("ID_PEDIDO").toString(),StatusPedido.PEDIDO_ENCERRADO);
        new Documento().getAllDocumentosFromThatPedido(Long.parseLong(req.getSession().getAttribute("ID_PEDIDO").toString())).forEach(currentDocumento -> {
            DocumentoService.deleteFile(currentDocumento.getNome());
            S3Util.deleteFileS3(ServletUtil.getContextParameter(req, "access-key"), ServletUtil.getContextParameter(req, "secret-key"), ServletUtil.getContextParameter(req, "estaghub-bucket"), currentDocumento.getNome());
        });
        req.getSession().setAttribute("NOVO_ESTAGIO",null);
        req.getSession().setAttribute("RENOVACAO_ESTAGIO",null);
        req.getSession().setAttribute("ID_PEDIDO",null);
        resp.getWriter().write("{\"finished\": \"true\"}");
    }
    private void discenteElaborarTermoAditivo(HttpServletRequest req, HttpServletResponse resp, List<FileItem> fileItems) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        TermoAditivoCreationDTO termoAditivoCreationDTO = new TermoAditivoCreationDTO();
        TermoAditivoMapperImpl termoAditivoMapper = new TermoAditivoMapperImpl();
        Documento documento = new Documento();
        fileItems.stream().forEach(fileItem -> {
            try{
                if ("dataInicioAditivo".equals(fileItem.getFieldName())){
                    termoAditivoCreationDTO.setDataAntiga(IOUtils.toString(fileItem.getInputStream()));
                }else if ("dataFimAditivo".equals(fileItem.getFieldName())){
                    termoAditivoCreationDTO.setDataNova(IOUtils.toString(fileItem.getInputStream()));
                }else if ("nomeSeguradora".equals(fileItem.getFieldName())){
                    termoAditivoCreationDTO.setNomeSeguradora(IOUtils.toString(fileItem.getInputStream()));
                }else if ("codigoApolice".equals(fileItem.getFieldName())){
                    termoAditivoCreationDTO.setCodApolice(IOUtils.toString(fileItem.getInputStream()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        TermoAditivo termoAditivo = termoAditivoMapper.toDiscenteCreateDocumento(termoAditivoCreationDTO);
        if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO).isEmpty()){
            if (fileItems.stream().anyMatch(fileItem -> "fileTermoAditivoAssinado".equals(fileItem.getFieldName()))){
                DocumentoService.armazenarDocumento(((Discente) session.getAttribute("DISCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), fileItems.stream().filter(fileItem -> "fileTermoAditivoAssinado".equals(fileItem.getFieldName())).findFirst().get());
                DocumentoService.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), ((Discente) session.getAttribute("DISCENTE")).getId().toString(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE, fileItems.stream().filter(fileItem -> "fileTermoAditivoAssinado".equals(fileItem.getFieldName())).findFirst());
                elaborarDocumento(req, session, pedido, documento, "TERMO_ADITIVO",  "TERMO_ADITIVO_URL", TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE, StatusPedido.NOVO_PEDIDO_FIM, TipoPedido.RENOVACAO);
                session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                req.getRequestDispatcher(FINAL_STEP).forward(req, resp);
            }else{
                FileUtil.gerarTermoAditivo(((Discente) session.getAttribute("DISCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), TipoDocumento.TERMO_ADITIVO, termoAditivo, (Discente) session.getAttribute("DISCENTE"),pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
                DocumentoService.criarDocumentoEstagio(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), (Discente) session.getAttribute("DISCENTE"), TipoDocumento.TERMO_ADITIVO);
                Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO).get().getId();
                documento.addTermoAditivoInDocumento(String.valueOf(idDocumento),termoAditivo);
                elaborarDocumento(req, session, pedido, documento, "TERMO_ADITIVO",  "TERMO_ADITIVO_URL", TipoDocumento.TERMO_ADITIVO, StatusPedido.RENOVACAO_STEP4_DISCENTE_ASSINADO, TipoPedido.RENOVACAO);
                session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
            }
        }
    }
    private void discenteAssinarDocumento(HttpServletRequest req, HttpServletResponse resp, List<FileItem> fileItems) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        Documento documento = new Documento();
        if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name()) && "NOVO_STEP3_DISCENTE_ASSINADO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
            if (fileItems.stream().anyMatch(fileItem -> "planoAtividadesAssinado".equals(fileItem.getFieldName()))){
                assinarDocumento(req, session, pedido, fileItems, documento, "planoAtividadesAssinado", TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE);
            }
            if (fileItems.stream().anyMatch(fileItem -> "tceAssinado".equals(fileItem.getFieldName()))){
                assinarDocumento(req, session, pedido, fileItems, documento, "tceAssinado", TipoDocumento.TCE_ASSINADO_DISCENTE);
            }
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP4);
            Pedido pedidoToBeSign = pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), TipoPedido.NOVO).get();
            session.setAttribute("NOVO_ESTAGIO",pedidoToBeSign);
            req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
            EmailService.sendInfoAboutPedido(req,pedidoToBeSign.getDocenteOrientador().getEmail(), pedidoToBeSign.getId().toString(),"O pedido <strong>#" + pedidoToBeSign.getId() + "</strong> do(a) discente <strong>" + pedidoToBeSign.getDiscente().getNome() + "</strong> precisa da sua avaliação!");
        }else if ("RENOVACAO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name()) && "RENOVACAO_STEP4_DISCENTE_ASSINADO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
            if (fileItems.stream().anyMatch(fileItem -> "termoAditivoAssinado".equals(fileItem.getFieldName()))){
                assinarDocumento(req, session, pedido, fileItems, documento, "termoAditivoAssinado", TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE);
            }
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_PEDIDO_FIM);
            session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
            Documento currentDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get();
            session.setAttribute("TERMO_ADITIVO", currentDocumento);
            session.setAttribute("TERMO_ADITIVO_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumento.getNome()));
            req.getRequestDispatcher(FINAL_STEP).forward(req, resp);
        }
    }

    private static void assinarDocumento(HttpServletRequest req, HttpSession session, Pedido pedido, List<FileItem> fileItems, Documento documento, String nomeDocumento, TipoDocumento tipoDocumento) throws IOException, ServletException {
        DocumentoService.armazenarDocumento(((Discente) session.getAttribute("DISCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), fileItems.stream().filter(fileItem -> nomeDocumento.equals(fileItem.getFieldName())).findFirst().get());
        DocumentoService.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), ((Discente) session.getAttribute("DISCENTE")).getId().toString(), tipoDocumento, fileItems.stream().filter(fileItem -> nomeDocumento.equals(fileItem.getFieldName())).findFirst());
        S3Util.uploadFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
    }

    private void discenteElaborarTCE(HttpServletRequest req, HttpServletResponse resp, List<FileItem> fileItems) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        TCECreationDTO tceCreationDTO = new TCECreationDTO();
        TCEMapperImpl tceMapper = new TCEMapperImpl();
        Documento documento = new Documento();
        fileItems.stream().forEach(fileItem -> {
            try{
                if ("cnpjEmpresa".equals(fileItem.getFieldName())){
                    tceCreationDTO.setCnpjEmpresa(IOUtils.toString(fileItem.getInputStream()));
                }else if ("horarioInicio".equals(fileItem.getFieldName())){
                    tceCreationDTO.setHorarioInicio(IOUtils.toString(fileItem.getInputStream()));
                } else if ("horarioFim".equals(fileItem.getFieldName())) {
                    tceCreationDTO.setHorarioFim(IOUtils.toString(fileItem.getInputStream()));
                } else if ("intervaloEstagio".equals(fileItem.getFieldName())) {
                    tceCreationDTO.setIntervalo(IOUtils.toString(fileItem.getInputStream()));
                }else if ("totalHoras".equals(fileItem.getFieldName())) {
                    tceCreationDTO.setTotalHoras(IOUtils.toString(fileItem.getInputStream()));
                }else if ("dataInicio".equals(fileItem.getFieldName())) {
                    tceCreationDTO.setDataInicio(IOUtils.toString(fileItem.getInputStream()));
                }else if ("dataFim".equals(fileItem.getFieldName())) {
                    tceCreationDTO.setDataFim(IOUtils.toString(fileItem.getInputStream()));
                }else if ("bolsaEstagio".equals(fileItem.getFieldName())) {
                    tceCreationDTO.setBolsa(IOUtils.toString(fileItem.getInputStream()));
                }else if ("auxilioTransporte".equals(fileItem.getFieldName())) {
                    tceCreationDTO.setAuxTransporte(IOUtils.toString(fileItem.getInputStream()));
                }else if ("codigoApolice".equals(fileItem.getFieldName())) {
                    tceCreationDTO.setCodApolice(IOUtils.toString(fileItem.getInputStream()));
                }else if ("nomeSeguradora".equals(fileItem.getFieldName())) {
                    tceCreationDTO.setNomeSeguradora(IOUtils.toString(fileItem.getInputStream()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        TCE tce = tceMapper.toDiscenteCreateDocumento(tceCreationDTO);
        if ("NOVO_STEP3".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
            if (fileItems.stream().anyMatch(fileItem -> "fileTCEAssinado".equals(fileItem.getFieldName()))){
                DocumentoService.armazenarDocumento(((Discente) session.getAttribute("DISCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), fileItems.stream().filter(fileItem -> "fileTCEAssinado".equals(fileItem.getFieldName())).findFirst().get());
                DocumentoService.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), ((Discente) session.getAttribute("DISCENTE")).getId().toString(), TipoDocumento.TCE_ASSINADO_DISCENTE, fileItems.stream().filter(fileItem -> "fileTCEAssinado".equals(fileItem.getFieldName())).findFirst());
                S3Util.uploadFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                Documento currentDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get();
                session.setAttribute("PLANO_ATIVIDADES", currentDocumento);
                session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumento.getNome()));
                session.setAttribute("TCE", null);
                session.setAttribute("TCE_URL", null);
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
            }else{
                FileUtil.gerarTCE(req, ((Discente) session.getAttribute("DISCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), TipoDocumento.TCE, tce, (Discente) session.getAttribute("DISCENTE"));
                DocumentoService.criarDocumentoEstagio(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), (Discente) session.getAttribute("DISCENTE"), TipoDocumento.TCE);
                Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getId();
                documento.addTCEInDocumento(String.valueOf(idDocumento),tce);
                Documento currentDocumentoPlanoAtividades = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get();
                elaborarDocumento(req, session, pedido, documento,"TCE",  "TCE_URL", TipoDocumento.TCE, StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO, TipoPedido.NOVO);
                session.setAttribute("PLANO_ATIVIDADES", currentDocumentoPlanoAtividades);
                session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumentoPlanoAtividades.getNome()));
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
            }
        }else if ("NOVO_STEP4_TCE".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
            if (fileItems.stream().anyMatch(fileItem -> "fileTCEAssinado".equals(fileItem.getFieldName()))){
                deletarDocumento(req, session, documento, TipoDocumento.TCE);
                DocumentoService.armazenarDocumento(((Discente) session.getAttribute("DISCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), fileItems.stream().filter(fileItem -> "fileTCEAssinado".equals(fileItem.getFieldName())).findFirst().get());
                DocumentoService.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), ((Discente) session.getAttribute("DISCENTE")).getId().toString(), TipoDocumento.TCE_ASSINADO_DISCENTE, fileItems.stream().filter(fileItem -> "fileTCEAssinado".equals(fileItem.getFieldName())).findFirst());
                S3Util.uploadFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).isEmpty()){
                    Documento currentDocumentoPlanoAtividades = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get();
                    session.setAttribute("PLANO_ATIVIDADES", currentDocumentoPlanoAtividades);
                    session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumentoPlanoAtividades.getNome()));
                    session.setAttribute("TCE", null);
                    session.setAttribute("TCE_URL", null);
                    pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO);
                    session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                    req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
                }else{
                    pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP4);
                    session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                    req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
                }
            }else{
                deletarDocumento(req, session, documento, TipoDocumento.TCE);
                if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).isEmpty()){
                    DocumentoService.criarDocumentoEstagio(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), (Discente) session.getAttribute("DISCENTE"), TipoDocumento.TCE);
                }
                Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getId();
                FileUtil.gerarTCE(req, ((Discente) session.getAttribute("DISCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), TipoDocumento.TCE, tce, (Discente) session.getAttribute("DISCENTE"));
                documento.addTCEInDocumento(String.valueOf(idDocumento),tce);
                elaborarDocumento(req, session, pedido, documento,"TCE",  "TCE_URL", TipoDocumento.TCE, StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO, TipoPedido.NOVO);
                if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).isEmpty()){
                    Documento currentDocumentoPlanoAtividades = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get();
                    session.setAttribute("PLANO_ATIVIDADES", currentDocumentoPlanoAtividades);
                    session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),currentDocumentoPlanoAtividades.getNome()));
                }else {
                    session.setAttribute("PLANO_ATIVIDADES", null);
                    session.setAttribute("PLANO_ATIVIDADES_URL", null);
                }
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
            }
        }
    }

    private void discenteElaborarPlanoAtividades(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        List<Optional<String>> atividades = new ArrayList<>();
        PlanoAtividadesCreationDTO planoAtividadesCreationDTO = new PlanoAtividadesCreationDTO();
        PlanoAtividadesMapperImpl planoAtividadesMapper = new PlanoAtividadesMapperImpl();
        Documento documento = new Documento();
        try{
            if (Strings.isNotBlank(req.getParameter("primeiraAtividade"))){
                atividades.add(Optional.ofNullable(req.getParameter("primeiraAtividade")));
            }
            if (Strings.isNotBlank(req.getParameter("segundaAtividade"))){
                atividades.add(Optional.ofNullable(req.getParameter("segundaAtividade")));
            }
            if (Strings.isNotBlank(req.getParameter("terceiraAtividade"))){
                atividades.add(Optional.ofNullable(req.getParameter("terceiraAtividade")));
            }
            if (Strings.isNotBlank(req.getParameter("quartaAtividade"))){
                atividades.add(Optional.ofNullable(req.getParameter("quartaAtividade")));
            }
            if (Strings.isNotBlank(req.getParameter("quintaAtividade"))){
                atividades.add(Optional.ofNullable(req.getParameter("quintaAtividade")));
            }
            if (Strings.isNotBlank(req.getParameter("nomeEmpresa"))){
                planoAtividadesCreationDTO.setNomeEmpresa(req.getParameter("nomeEmpresa"));
            }
            if (Strings.isNotBlank(req.getParameter("nomeResponsavelEmpresa"))) {
                planoAtividadesCreationDTO.setResponsavelEmpresa(req.getParameter("nomeResponsavelEmpresa"));
            }
            if (Strings.isNotBlank(req.getParameter("enderecoEmpresa"))) {
                planoAtividadesCreationDTO.setEnderecoEmpresa(req.getParameter("enderecoEmpresa"));
            }
            if (Strings.isNotBlank(req.getParameter("telefoneEmpresa"))) {
                planoAtividadesCreationDTO.setTelefoneEmpresa(req.getParameter("telefoneEmpresa"));
            }
            if (Strings.isNotBlank(req.getParameter("emailEmpresa"))) {
                planoAtividadesCreationDTO.setEmailEmpresa(req.getParameter("emailEmpresa"));
            }
            if (Strings.isNotBlank(req.getParameter("nomeSupervisor"))) {
                planoAtividadesCreationDTO.setNomeSupervisor(req.getParameter("nomeSupervisor"));
            }
            if (Strings.isNotBlank(req.getParameter("formacaoSupervisor"))) {
                planoAtividadesCreationDTO.setFormacaoSupervisor(req.getParameter("formacaoSupervisor"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        PlanoAtividades planoAtividades = planoAtividadesMapper.toDiscenteCreateDocumento(planoAtividadesCreationDTO, atividades);
        if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).isEmpty()){
            FileUtil.gerarPlanoAtividades(req, ((Discente) session.getAttribute("DISCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), TipoDocumento.PLANO_ATIVIDADES, planoAtividades, (Discente) session.getAttribute("DISCENTE"),pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
            DocumentoService.criarDocumentoEstagio(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), (Discente) session.getAttribute("DISCENTE"), TipoDocumento.PLANO_ATIVIDADES);
            Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getId();
            documento.addPlanoAtividadesInDocumento(String.valueOf(idDocumento), planoAtividades);
            elaborarDocumento(req, session, pedido, documento,"PLANO_ATIVIDADES", "PLANO_ATIVIDADES_URL", TipoDocumento.PLANO_ATIVIDADES, StatusPedido.NOVO_STEP3, TipoPedido.NOVO);
            session.setAttribute("NOVO_ESTAGIO", pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
            req.getRequestDispatcher(NOVO_STEP3).forward(req, resp);
        }else if ("NOVO_STEP4_PLANO_ATIVIDADES".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
            elaborarPlanoAtividades(req, session, pedido, documento, planoAtividades, StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO);
            if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE).isPresent()){
                session.setAttribute("TCE", null);
                session.setAttribute("TCE_URL", null);
            }
            req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
        }else if ("NOVO_STEP4_ATIVIDADES_TCE".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())) {
            elaborarPlanoAtividades(req, session, pedido, documento, planoAtividades, StatusPedido.NOVO_STEP4_TCE);
            if (documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"),TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).isPresent()){
                session.setAttribute("DOCUMENTO",documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"),TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).get());
            }else{
                session.setAttribute("DOCUMENTO",null);
            }
            req.getRequestDispatcher(NOVO_STEP3).forward(req, resp);
        }
    }

    private static void elaborarPlanoAtividades(HttpServletRequest req, HttpSession session, Pedido pedido, Documento documento, PlanoAtividades planoAtividades, StatusPedido statusPedido) {
        deletarDocumento(req, session, documento, TipoDocumento.PLANO_ATIVIDADES);
        FileUtil.gerarPlanoAtividades(req, ((Discente) session.getAttribute("DISCENTE")).getId().toString(), session.getAttribute("ID_PEDIDO").toString(), TipoDocumento.PLANO_ATIVIDADES, planoAtividades, (Discente) session.getAttribute("DISCENTE"), pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
        Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getId();
        documento.addPlanoAtividadesInDocumento(String.valueOf(idDocumento), planoAtividades);
        elaborarDocumento(req, session, pedido, documento, "PLANO_ATIVIDADES",  "PLANO_ATIVIDADES_URL", TipoDocumento.PLANO_ATIVIDADES, statusPedido, TipoPedido.NOVO);
        session.setAttribute("NOVO_ESTAGIO", pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
    }

    private static void deletarDocumento(HttpServletRequest req, HttpSession session, Documento documento, TipoDocumento tipoDocumento) {
        if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),tipoDocumento).isPresent()) {
            DocumentoService.deleteFile(documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
            S3Util.deleteFileS3(ServletUtil.getContextParameter(req, "access-key"), ServletUtil.getContextParameter(req, "secret-key"), ServletUtil.getContextParameter(req, "estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
            if (tipoDocumento.equals(TipoDocumento.TCE)){
                documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento);
            }
        }
    }

    private static void elaborarDocumento(HttpServletRequest req, HttpSession session, Pedido pedido, Documento documento, String primeiroNomeDocumento, String segundoNomeDocumento, TipoDocumento tipoDocumento, StatusPedido statusPedido, TipoPedido tipoPedido) {
        S3Util.uploadFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
        session.setAttribute(primeiroNomeDocumento, documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get());
        session.setAttribute(segundoNomeDocumento,S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome()));
        pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),statusPedido);
    }

    private void discenteRenovacaoEstagio(HttpServletRequest req, HttpServletResponse resp, List<FileItem> fileItems) throws IOException, ServletException {
        try{
            DiscenteRenovacaoPedidoCreationDTO discenteRenovacaoPedidoCreationDTO = new DiscenteRenovacaoPedidoCreationDTO();
            DiscenteMapperImpl discenteMapper = new DiscenteMapperImpl();
            Pedido pedido = new Pedido();
            Discente discenteSession = (Discente) req.getSession().getAttribute("DISCENTE");
            fileItems.forEach(fileItem -> {
                try{
                    if ("cpfDiscente".equals(fileItem.getFieldName())){
                        discenteRenovacaoPedidoCreationDTO.setCpf(CryptUtil.encryptInfo(req,IOUtils.toString(fileItem.getInputStream())));
                    }else if ("rgDiscente".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setRg(CryptUtil.encryptInfo(req,IOUtils.toString(fileItem.getInputStream())));
                    }else if ("orgaoExpedidorRgDiscente".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setOrgaoExpedidorRg(CryptUtil.encryptInfo(req,IOUtils.toString(fileItem.getInputStream())));
                    }else if ("iraDiscente".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setIra(IOUtils.toString(fileItem.getInputStream()).replaceAll(",","."));
                    }else if ("periodoDiscente".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setPeriodo(IOUtils.toString(fileItem.getInputStream()));
                    }else if ("cargaHorariaCumpridaDiscente".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setCargaHorariaCumprida(IOUtils.toString(fileItem.getInputStream()));
                    }else if ("enderecoDiscente".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setEndereco(CryptUtil.encryptInfo(req,IOUtils.toString(fileItem.getInputStream())));
                    }else if ("selectCursoDiscente".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setCurso(new Curso().getCursoById(Long.parseLong(IOUtils.toString(fileItem.getInputStream()))).get());
                    }else if ("nomeEmpresa".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setNomeEmpresa(IOUtils.toString(fileItem.getInputStream()));
                    }else if ("enderecoEmpresa".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setEnderecoEmpresa(IOUtils.toString(fileItem.getInputStream()));
                    }else if ("textResumo".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setResumoAtividades(IOUtils.toString(fileItem.getInputStream()));
                    }else if ("radioTempoEstagio".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setTempoEstagio(IOUtils.toString(fileItem.getInputStream()));
                    }else if ("radioAvaliacao".equals(fileItem.getFieldName())) {
                        discenteRenovacaoPedidoCreationDTO.setContribuicaoEstagio(IOUtils.toString(fileItem.getInputStream()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            Discente discente = discenteMapper.toDiscenteCreateRenovacaoEstagio(discenteRenovacaoPedidoCreationDTO);
            pedido.setRenovacaoEstagio(RenovacaoEstagio.builder().nomeEmpresa(discenteRenovacaoPedidoCreationDTO.getNomeEmpresa())
               .enderecoEmpresa(discenteRenovacaoPedidoCreationDTO.getEnderecoEmpresa())
               .resumoAtividades(discenteRenovacaoPedidoCreationDTO.getResumoAtividades())
               .tempoEstagio(discenteRenovacaoPedidoCreationDTO.getTempoEstagio())
               .contribuicaoEstagio(discenteRenovacaoPedidoCreationDTO.getContribuicaoEstagio())
               .build());
            discente.setId(discenteSession.getId());
            HttpSession session = req.getSession();
            Documento documento = new Documento();
            pedido.setTipo(TipoPedido.RENOVACAO);
            pedido.setStatus(StatusPedido.RENOVACAO_STEP1);
            discente.addInfoNovoPedidoInDiscente(discente);
            pedido.setDiscente(discente);
            pedido.criarPedido(pedido);
            DocumentoService.armazenarDocumento(discente.getId().toString(), pedido.getId().toString(), fileItems.stream().filter(fileItem -> "historicoDiscente".equals(fileItem.getFieldName())).findFirst().get());
            DocumentoService.criarDocumento(pedido, discente.getId().toString(), TipoDocumento.HISTORICO_ACADEMICO, fileItems.stream().filter(fileItem -> "historicoDiscente".equals(fileItem.getFieldName())).findFirst());
            S3Util.uploadFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.HISTORICO_ACADEMICO).get().getNome());
            DocumentoService.armazenarDocumento(discente.getId().toString(), pedido.getId().toString(), fileItems.stream().filter(fileItem -> "gradeHorarioDiscente".equals(fileItem.getFieldName())).findFirst().get());
            DocumentoService.criarDocumento(pedido, discente.getId().toString(), TipoDocumento.GRADE_HORARIO, fileItems.stream().filter(fileItem -> "gradeHorarioDiscente".equals(fileItem.getFieldName())).findFirst());
            S3Util.uploadFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.GRADE_HORARIO).get().getNome());
            Pedido pedidoToBeCreated = pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get();
            if (Float.parseFloat(discente.getIra()) < 5){
                pedido.changeStatusPedido(String.valueOf(pedidoToBeCreated.getId()),StatusPedido.RENOVACAO_STEP3_REJEITADO);
                session.setAttribute("RENOVACAO_ESTAGIO",pedidoToBeCreated);
                req.getRequestDispatcher(NOVO_ESTAGIO_REJEITADO).forward(req,resp);
                EmailService.sendInfoAboutPedido(req,discenteSession.getEmail(), pedidoToBeCreated.getId().toString(),"Seu pedido <strong>#" + pedidoToBeCreated.getId() + "</strong> foi realizado com sucesso!");
            }else{
                session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
                EmailService.sendInfoAboutPedido(req,discenteSession.getEmail(), pedidoToBeCreated.getId().toString(),"Seu pedido <strong>#" + pedidoToBeCreated.getId() + "</strong> foi realizado com sucesso!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void discenteNovoEstagioStep(HttpServletRequest req, HttpServletResponse resp, List<FileItem> fileItems) {
        try{
            DiscenteNovoPedidoCreationDTO discenteNovoPedidoCreationDTO = new DiscenteNovoPedidoCreationDTO();
            DiscenteMapperImpl discenteMapper = new DiscenteMapperImpl();
            Pedido pedido = new Pedido();
            Documento documento = new Documento();
            Discente discenteSession = (Discente) req.getSession().getAttribute("DISCENTE");
            fileItems.stream().forEach(fileItem -> {
                try{
                    if ("cpfDiscente".equals(fileItem.getFieldName())){
                        discenteNovoPedidoCreationDTO.setCpf(CryptUtil.encryptInfo(req,IOUtils.toString(fileItem.getInputStream())));
                    }else if ("rgDiscente".equals(fileItem.getFieldName())) {
                        discenteNovoPedidoCreationDTO.setRg(CryptUtil.encryptInfo(req,IOUtils.toString(fileItem.getInputStream())));
                    }else if ("orgaoExpedidorRgDiscente".equals(fileItem.getFieldName())) {
                        discenteNovoPedidoCreationDTO.setOrgaoExpedidorRg(CryptUtil.encryptInfo(req,IOUtils.toString(fileItem.getInputStream())));
                    }else if ("iraDiscente".equals(fileItem.getFieldName())) {
                        discenteNovoPedidoCreationDTO.setIra(IOUtils.toString(fileItem.getInputStream()).replaceAll(",","."));
                    }else if ("periodoDiscente".equals(fileItem.getFieldName())) {
                        discenteNovoPedidoCreationDTO.setPeriodo(IOUtils.toString(fileItem.getInputStream()));
                    }else if ("cargaHorariaCumpridaDiscente".equals(fileItem.getFieldName())) {
                        discenteNovoPedidoCreationDTO.setCargaHorariaCumprida(IOUtils.toString(fileItem.getInputStream()));
                    }else if ("enderecoDiscente".equals(fileItem.getFieldName())) {
                        discenteNovoPedidoCreationDTO.setEndereco(CryptUtil.encryptInfo(req,IOUtils.toString(fileItem.getInputStream())));
                    }else if ("selectCursoDiscente".equals(fileItem.getFieldName())) {
                        discenteNovoPedidoCreationDTO.setCurso(new Curso().getCursoById(Long.parseLong(IOUtils.toString(fileItem.getInputStream()))).get());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            Discente discente = discenteMapper.toDiscenteCreateNovoEstagio(discenteNovoPedidoCreationDTO);
            discente.setId(discenteSession.getId());
            HttpSession session = req.getSession();
            pedido.setTipo(TipoPedido.NOVO);
            pedido.setStatus(StatusPedido.NOVO_STEP1);
            discente.addInfoNovoPedidoInDiscente(discente);
            pedido.setDiscente(discente);
            pedido.criarPedido(pedido);
            DocumentoService.armazenarDocumento(discente.getId().toString(), pedido.getId().toString(), fileItems.stream().filter(fileItem -> "historicoDiscente".equals(fileItem.getFieldName())).findFirst().get());
            DocumentoService.criarDocumento(pedido, discente.getId().toString(), TipoDocumento.HISTORICO_ACADEMICO, fileItems.stream().filter(fileItem -> "historicoDiscente".equals(fileItem.getFieldName())).findFirst());
            S3Util.uploadFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.HISTORICO_ACADEMICO).get().getNome());
            DocumentoService.armazenarDocumento(discente.getId().toString(), pedido.getId().toString(), fileItems.stream().filter(fileItem -> "gradeHorarioDiscente".equals(fileItem.getFieldName())).findFirst().get());
            DocumentoService.criarDocumento(pedido, discente.getId().toString(), TipoDocumento.GRADE_HORARIO, fileItems.stream().filter(fileItem -> "gradeHorarioDiscente".equals(fileItem.getFieldName())).findFirst());
            S3Util.uploadFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.GRADE_HORARIO).get().getNome());
            Pedido pedidoToBeCreated = pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), TipoPedido.NOVO).get();
            if (Float.parseFloat(discente.getIra()) < 6 || (Float.parseFloat(discente.getCargaHorariaCumprida()) / 15) < 80){
                pedido.changeStatusPedido(String.valueOf(pedidoToBeCreated.getId()),StatusPedido.NOVO_STEP2_REJEITADO);
                session.setAttribute("NOVO_ESTAGIO",pedidoToBeCreated);
                req.getRequestDispatcher(NOVO_ESTAGIO_REJEITADO).forward(req,resp);
                EmailService.sendInfoAboutPedido(req,discenteSession.getEmail(), pedidoToBeCreated.getId().toString(),"Seu pedido <strong>#" + pedidoToBeCreated.getId() + "</strong> foi realizado com sucesso!");
            }else{
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
                EmailService.sendInfoAboutPedido(req,discenteSession.getEmail(), pedidoToBeCreated.getId().toString(),"Seu pedido <strong>#" + pedidoToBeCreated.getId() + "</strong> foi realizado com sucesso!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
