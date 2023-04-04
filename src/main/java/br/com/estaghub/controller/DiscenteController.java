package br.com.estaghub.controller;

import br.com.estaghub.domain.*;
import br.com.estaghub.dto.*;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.mapper.impl.DiscenteMapperImpl;
import br.com.estaghub.mapper.impl.PlanoAtividadesMapperImpl;
import br.com.estaghub.mapper.impl.TCEMapperImpl;
import br.com.estaghub.mapper.impl.TermoAditivoMapperImpl;
import br.com.estaghub.service.DocumentoService;
import br.com.estaghub.util.FileUtil;
import br.com.estaghub.util.S3Util;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "DiscenteController", value = "/discenteController")
@MultipartConfig(fileSizeThreshold=1024*1024*10,  	// 10 MB
location = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos")
public class DiscenteController extends HttpServlet {
    private static final String SUCESS_DISCENTE = "/discente.jsp";
    private static final String NOVO_STEP3 = "/emitirTCE.jsp";
    private static final String DISCENTE_ASSINA_DOCUMENTO = "/assinarDocumentoDiscente.jsp";
    private static final String FINAL_STEP = "/finalStep.jsp";
    private static final String NOVO_ESTAGIO_REJEITADO = "/justificativaNovoEstagio.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
           if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteCadastroNovoEstagio1"))) {
                discenteNovoEstagioStep(req, resp);
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteJustificativa2"))){
               discenteJustificativa(req, resp);
           }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteElaborarPlanoAtividade"))){
                discenteElaborarPlanoAtividades(req, resp);
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteElaborarTCE"))){
                discenteElaborarTCE(req, resp);
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteAssinar"))){
                discenteAssinarDocumento(req, resp);
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteCadastroRenovacaoEstagio1"))){
                discenteRenovacaoEstagio(req, resp);
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteTermoAditivo"))){
                discenteElaborarTermoAditivo(req, resp);
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteFinalizar"))){
                discenteFinalizarPedido(req, resp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void discenteJustificativa(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String justificativa="";
        for (Part part : req.getParts()){
            if ("textAreaJustificativa".equals(part.getName())){
                justificativa = IOUtils.toString(part.getInputStream());
            }
        }
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        Discente discente = new Discente();
        if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name())){
            discenteJustificativa(justificativa, session, pedido, discente, "NOVO_ESTAGIO", TipoPedido.NOVO, StatusPedido.NOVO_STEP1, StatusPedido.NOVO_STEP2_JUSTIFICADO);
        }else{
            discenteJustificativa(justificativa, session, pedido, discente, "RENOVACAO_ESTAGIO", TipoPedido.RENOVACAO, StatusPedido.RENOVACAO_STEP1, StatusPedido.RENOVACAO_STEP3_JUSTIFICADO);
        }
        req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
    }

    private static void discenteJustificativa(String justificativa, HttpSession session, Pedido pedido, Discente discente, String nomePedido, TipoPedido tipoPedido, StatusPedido primeiroStatusPedido,  StatusPedido segundoStatusPedido) {
        pedido.changeJustificativaDiscentePedido(String.valueOf(pedido.getPedidoByDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())),tipoPedido).get().getId()), justificativa);
        if (Objects.isNull(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getJustificativaDocente())){
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),primeiroStatusPedido);
            session.setAttribute(nomePedido, pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), tipoPedido).get());
        }else{
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),segundoStatusPedido);
            session.setAttribute(nomePedido, pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), tipoPedido).get());
        }
    }

    private void discenteFinalizarPedido(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        if (req.getParts().stream().anyMatch(part -> "submitButtonDiscenteFinalizar".equals(part.getName()))){
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.PEDIDO_ENCERRADO);
            session.setAttribute("NOVO_ESTAGIO",null);
            session.setAttribute("RENOVACAO_ESTAGIO",null);
            req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
        }
    }
    private void discenteElaborarTermoAditivo(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        Discente discente = new Discente();
        TermoAditivoCreationDTO termoAditivoCreationDTO = new TermoAditivoCreationDTO();
        TermoAditivoMapperImpl termoAditivoMapper = new TermoAditivoMapperImpl();
        DocumentoService documentoService = new DocumentoService();
        Documento documento = new Documento();
        req.getParts().stream().forEach(part -> {
            try{
                if (part.getName().equals("dataInicioAditivo")){
                    termoAditivoCreationDTO.setDataAntiga(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("dataFimAditivo")){
                    termoAditivoCreationDTO.setDataNova(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("nomeSeguradora")){
                    termoAditivoCreationDTO.setNomeSeguradora(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("codigoApolice")){
                    termoAditivoCreationDTO.setCodApolice(IOUtils.toString(part.getInputStream()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        TermoAditivo termoAditivo = termoAditivoMapper.toDiscenteCreateDocumento(termoAditivoCreationDTO);
        if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO).isEmpty()){
            if (req.getParts().stream().anyMatch(part -> "fileTermoAditivoAssinado".equals(part.getName()))){
                FileUtil.armazenarDocumentoAssinadoDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), req.getParts().stream().filter(part -> "fileTermoAditivoAssinado".equals(part.getName())).findFirst().get(),TipoPedido.RENOVACAO);
                FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.RENOVACAO, TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE, req.getParts().stream().filter(part -> "fileTermoAditivoAssinado".equals(part.getName())).findFirst());
                elaborarDocumento(req, session, pedido, documento, "TERMO_ADITIVO",  "TERMO_ADITIVO_URL", TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE, StatusPedido.NOVO_PEDIDO_FIM, TipoPedido.RENOVACAO);
                session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                req.getRequestDispatcher(FINAL_STEP).forward(req, resp);
            }else{
                documentoService.gerarTermoAditivo(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.TERMO_ADITIVO, termoAditivoCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())),pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
                FileUtil.criarDocumentoEstagio(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.RENOVACAO, TipoDocumento.TERMO_ADITIVO);
                Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO).get().getId();
                documento.addTermoAditivoInDocumento(String.valueOf(idDocumento),termoAditivo);
                elaborarDocumento(req, session, pedido, documento, "TERMO_ADITIVO",  "TERMO_ADITIVO_URL", TipoDocumento.TERMO_ADITIVO, StatusPedido.RENOVACAO_STEP4_DISCENTE_ASSINADO, TipoPedido.RENOVACAO);
                session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
            }
        }
    }
    private void discenteAssinarDocumento(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        Discente discente = new Discente();
        Documento documento = new Documento();
        if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name())){
            if ("NOVO_STEP3_DISCENTE_ASSINADO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
                if (req.getParts().stream().anyMatch(part -> "planoAtividadesAssinado".equals(part.getName()))){
                    assinarDocumento(req, session, pedido, discente, documento, "planoAtividadesAssinado", TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE, TipoPedido.NOVO);
                }
                if (req.getParts().stream().anyMatch(part -> "tceAssinado".equals(part.getName()))){
                    assinarDocumento(req, session, pedido, discente, documento, "tceAssinado", TipoDocumento.TCE_ASSINADO_DISCENTE, TipoPedido.NOVO);
                }
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP4);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
            }
        }else if ("RENOVACAO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name())){
            if ("RENOVACAO_STEP4_DISCENTE_ASSINADO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
                if (req.getParts().stream().anyMatch(part -> "termoAditivoAssinado".equals(part.getName()))){
                    assinarDocumento(req, session, pedido, discente, documento, "termoAditivoAssinado", TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE, TipoPedido.RENOVACAO);
                }
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_PEDIDO_FIM);
                session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                session.setAttribute("TERMO_ADITIVO", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get());
                session.setAttribute("TERMO_ADITIVO_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get().getNome()));
                req.getRequestDispatcher(FINAL_STEP).forward(req, resp);
            }
        }
    }

    private static void assinarDocumento(HttpServletRequest req, HttpSession session, Pedido pedido, Discente discente, Documento documento, String nomeDocumento, TipoDocumento tipoDocumento, TipoPedido tipoPedido) throws IOException, ServletException {
        FileUtil.armazenarDocumentoAssinadoDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), req.getParts().stream().filter(part -> nomeDocumento.equals(part.getName())).findFirst().get(),tipoPedido);
        FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), tipoPedido, tipoDocumento, req.getParts().stream().filter(part -> nomeDocumento.equals(part.getName())).findFirst());
        S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
    }

    private void discenteElaborarTCE(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        Discente discente = new Discente();
        TCECreationDTO tceCreationDTO = new TCECreationDTO();
        TCEMapperImpl tceMapper = new TCEMapperImpl();
        DocumentoService documentoService = new DocumentoService();
        Documento documento = new Documento();
        req.getParts().stream().forEach(part -> {
            try{
                if (part.getName().equals("cnpjEmpresa")){
                    tceCreationDTO.setCnpjEmpresa(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("horarioInicio")){
                    tceCreationDTO.setHorarioInicio(IOUtils.toString(part.getInputStream()));
                } else if (part.getName().equals("horarioFim")) {
                    tceCreationDTO.setHorarioFim(IOUtils.toString(part.getInputStream()));
                } else if (part.getName().equals("intervaloEstagio")) {
                    tceCreationDTO.setIntervalo(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("totalHoras")) {
                    tceCreationDTO.setTotalHoras(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("dataInicio")) {
                    tceCreationDTO.setDataInicio(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("dataFim")) {
                    tceCreationDTO.setDataFim(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("bolsaEstagio")) {
                    tceCreationDTO.setBolsa(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("auxilioTransporte")) {
                    tceCreationDTO.setAuxTransporte(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("codigoApolice")) {
                    tceCreationDTO.setCodApolice(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("nomeSeguradora")) {
                    tceCreationDTO.setNomeSeguradora(IOUtils.toString(part.getInputStream()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        TCE tce = tceMapper.toDiscenteCreateDocumento(tceCreationDTO);
        if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).isEmpty()){
            if (req.getParts().stream().anyMatch(part -> "fileTCEAssinado".equals(part.getName()))){
                FileUtil.armazenarDocumentoAssinadoDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), req.getParts().stream().filter(part -> "fileTCEAssinado".equals(part.getName())).findFirst().get(),TipoPedido.NOVO);
                FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.NOVO, TipoDocumento.TCE_ASSINADO_DISCENTE, req.getParts().stream().filter(part -> "fileTCEAssinado".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP4);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
            }else{
                documentoService.gerarTCE(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.TCE, tceCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())));
                FileUtil.criarDocumentoEstagio(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.NOVO, TipoDocumento.TCE);
                Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getId();
                documento.addTCEInDocumento(String.valueOf(idDocumento),tce);
                S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getNome());
                session.setAttribute("PLANO_ATIVIDADES", documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get());
                session.setAttribute("PLANO_ATIVIDADES_URL", S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getNome()));
                session.setAttribute("TCE",documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get());
                session.setAttribute("TCE_URL",S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getNome()));
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
            }
        }else if ("NOVO_STEP4_TCE".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
            if (req.getParts().stream().anyMatch(part -> "fileTCEAssinado".equals(part.getName()))){
                if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE).isPresent()){
                    FileUtil.deleteFile(documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                    S3Util.deleteFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                    documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE);
                }
                FileUtil.armazenarDocumentoAssinadoDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), req.getParts().stream().filter(part -> "fileTCEAssinado".equals(part.getName())).findFirst().get(),TipoPedido.NOVO);
                FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.NOVO, TipoDocumento.TCE_ASSINADO_DISCENTE, req.getParts().stream().filter(part -> "fileTCEAssinado".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP4);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
            }else{
                if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE).isPresent()){
                    FileUtil.deleteFile(documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getNome());
                    S3Util.deleteFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getNome());
                }
                Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getId();
                documentoService.gerarTCE(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.TCE, tceCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())));
                documento.addTCEInDocumento(String.valueOf(idDocumento),tce);
                elaborarDocumento(req, session, pedido, documento,"TCE",  "TCE_URL", TipoDocumento.TCE, StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO, TipoPedido.NOVO);
                session.setAttribute("NOVO_ESTAGIO", pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
            }
        }
    }

    private void discenteElaborarPlanoAtividades(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        Discente discente = new Discente();
        List<Optional<String>> atividades = new ArrayList<>();
        PlanoAtividadesCreationDTO planoAtividadesCreationDTO = new PlanoAtividadesCreationDTO();
        PlanoAtividadesMapperImpl planoAtividadesMapper = new PlanoAtividadesMapperImpl();
        DocumentoService documentoService = new DocumentoService();
        Documento documento = new Documento();
        req.getParts().stream().forEach(part -> {
            try{
                if (part.getName().equals("primeiraAtividade") || part.getName().equals("segundaAtividade") || part.getName().equals("terceiraAtividade") || part.getName().equals("quartaAtividade") || part.getName().equals("quintaAtividade")){
                    atividades.add(Optional.ofNullable(IOUtils.toString(part.getInputStream())));
                }
                if (part.getName().equals("nomeEmpresa")){
                    planoAtividadesCreationDTO.setNomeEmpresa(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("nomeResponsavelEmpresa")) {
                    planoAtividadesCreationDTO.setResponsavelEmpresa(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("enderecoEmpresa")) {
                    planoAtividadesCreationDTO.setEnderecoEmpresa(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("telefoneEmpresa")) {
                    planoAtividadesCreationDTO.setTelefoneEmpresa(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("emailEmpresa")) {
                    planoAtividadesCreationDTO.setEmailEmpresa(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("nomeSupervisor")) {
                    planoAtividadesCreationDTO.setNomeSupervisor(IOUtils.toString(part.getInputStream()));
                }else if (part.getName().equals("formacaoSupervisor")) {
                    planoAtividadesCreationDTO.setFormacaoSupervisor(IOUtils.toString(part.getInputStream()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        PlanoAtividades planoAtividades = planoAtividadesMapper.toDiscenteCreateDocumento(planoAtividadesCreationDTO, atividades);
        if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).isEmpty()){
            documentoService.gerarPlanoAtividades(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.PLANO_ATIVIDADES, atividades, planoAtividadesCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())),pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
            FileUtil.criarDocumentoEstagio(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.NOVO, TipoDocumento.PLANO_ATIVIDADES);
            Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getId();
            documento.addPlanoAtividadesInDocumento(String.valueOf(idDocumento), planoAtividades);
            elaborarDocumento(req, session, pedido, documento,"PLANO_ATIVIDADES",  "PLANO_ATIVIDADES_URL", TipoDocumento.PLANO_ATIVIDADES, StatusPedido.NOVO_STEP3, TipoPedido.NOVO);
            session.setAttribute("NOVO_ESTAGIO", pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
            req.getRequestDispatcher(NOVO_STEP3).forward(req, resp);
        }else if ("NOVO_STEP4_PLANO_ATIVIDADES".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
            elaborarPlanoAtividades(req, session, pedido, discente, atividades, planoAtividadesCreationDTO, documentoService, documento, planoAtividades, StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO);
            req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO).forward(req, resp);
        }else if ("NOVO_STEP4_ATIVIDADES_TCE".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())) {
            elaborarPlanoAtividades(req, session, pedido, discente, atividades, planoAtividadesCreationDTO, documentoService, documento, planoAtividades, StatusPedido.NOVO_STEP4_TCE);
            session.setAttribute("DOCUMENTO",documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())),TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).get());
            req.getRequestDispatcher(NOVO_STEP3).forward(req, resp);
        }
    }

    private static void elaborarPlanoAtividades(HttpServletRequest req, HttpSession session, Pedido pedido, Discente discente, List<Optional<String>> atividades, PlanoAtividadesCreationDTO planoAtividadesCreationDTO, DocumentoService documentoService, Documento documento, PlanoAtividades planoAtividades, StatusPedido statusPedido) {
        deletarDocumento(req, session, documento, TipoDocumento.PLANO_ATIVIDADES);
        documentoService.gerarPlanoAtividades(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.PLANO_ATIVIDADES, atividades, planoAtividadesCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
        Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getId();
        documento.addPlanoAtividadesInDocumento(String.valueOf(idDocumento), planoAtividades);
        elaborarDocumento(req, session, pedido, documento, "PLANO_ATIVIDADES",  "PLANO_ATIVIDADES_URL", TipoDocumento.PLANO_ATIVIDADES, statusPedido, TipoPedido.NOVO);
        session.setAttribute("NOVO_ESTAGIO", pedido.getPedidoByDiscente((Discente) session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
    }

    private static void deletarDocumento(HttpServletRequest req, HttpSession session, Documento documento, TipoDocumento tipoDocumento) {
        if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),tipoDocumento).isPresent()) {
            FileUtil.deleteFile(documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
            S3Util.deleteFileS3(S3Util.getContextParameter(req, "access-key"), S3Util.getContextParameter(req, "secret-key"), S3Util.getContextParameter(req, "estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
        }
    }

    private static void elaborarDocumento(HttpServletRequest req, HttpSession session, Pedido pedido, Documento documento, String primeiroNomeDocumento, String segundoNomeDocumento, TipoDocumento tipoDocumento, StatusPedido statusPedido, TipoPedido tipoPedido) {
        S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome());
        session.setAttribute(primeiroNomeDocumento, documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get());
        session.setAttribute(segundoNomeDocumento,S3Util.getFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"), documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), tipoDocumento).get().getNome()));
        pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),statusPedido);
    }

    private void discenteRenovacaoEstagio(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try{
            Collection<Part> parts = req.getParts();
            DiscenteRenovacaoPedidoCreationDTO discenteRenovacaoPedidoCreationDTO = new DiscenteRenovacaoPedidoCreationDTO();
            DiscenteMapperImpl discenteMapper = new DiscenteMapperImpl();
            Curso curso = new Curso();
            Pedido pedido = new Pedido();
            Discente discenteSession = (Discente) req.getSession().getAttribute("DISCENTE");
            parts.stream().forEach(part -> {
                try{
                    if (part.getName().equals("cpfDiscente")){
                        discenteRenovacaoPedidoCreationDTO.setCpf(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("rgDiscente")) {
                        discenteRenovacaoPedidoCreationDTO.setRg(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("orgaoExpedidorRgDiscente")) {
                        discenteRenovacaoPedidoCreationDTO.setOrgaoExpedidorRg(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("iraDiscente")) {
                        discenteRenovacaoPedidoCreationDTO.setIra(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("periodoDiscente")) {
                        discenteRenovacaoPedidoCreationDTO.setPeriodo(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("cargaHorariaCumpridaDiscente")) {
                        discenteRenovacaoPedidoCreationDTO.setCargaHorariaCumprida(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("enderecoDiscente")) {
                        discenteRenovacaoPedidoCreationDTO.setEndereco(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("selectCursoDiscente")) {
                        discenteRenovacaoPedidoCreationDTO.setCurso(curso.getCursoById(Long.parseLong(IOUtils.toString(part.getInputStream()))).get());
                    }else if (part.getName().equals("nomeEmpresa")) {
                        discenteRenovacaoPedidoCreationDTO.setNomeEmpresa(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("enderecoEmpresa")) {
                        discenteRenovacaoPedidoCreationDTO.setEnderecoEmpresa(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("textResumo")) {
                        discenteRenovacaoPedidoCreationDTO.setResumoAtividades(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("radioTempoEstagio")) {
                        discenteRenovacaoPedidoCreationDTO.setTempoEstagio(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("radioAvaliacao")) {
                        discenteRenovacaoPedidoCreationDTO.setContribuicaoEstagio(IOUtils.toString(part.getInputStream()));
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
            if (FileUtil.armazenarDocumentoDiscente(discente, req.getParts(),TipoPedido.RENOVACAO)){
                HttpSession session = req.getSession();
                Documento documento = new Documento();
                pedido.setTipo(TipoPedido.RENOVACAO);
                pedido.setStatus(StatusPedido.RENOVACAO_STEP1);
                discente.addInfoNovoPedidoInDiscente(discente);
                pedido.setDiscente(discente);
                pedido.criarPedido(pedido);
                FileUtil.criarDocumento(pedido, discente, TipoPedido.RENOVACAO, TipoDocumento.HISTORICO_ACADEMICO, req.getParts().stream().filter(part -> "historicoDiscente".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.HISTORICO_ACADEMICO).get().getNome());
                FileUtil.criarDocumento(pedido, discente, TipoPedido.RENOVACAO, TipoDocumento.GRADE_HORARIO, req.getParts().stream().filter(part -> "gradeHorarioDiscente".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.GRADE_HORARIO).get().getNome());
                session.setAttribute("ID_PEDIDO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get().getId());
                if (Float.parseFloat(discente.getIra()) < 5){
                    pedido.changeStatusPedido(String.valueOf(pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"),TipoPedido.RENOVACAO).get().getId()),StatusPedido.RENOVACAO_STEP3_REJEITADO);
                    session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                    req.getRequestDispatcher(NOVO_ESTAGIO_REJEITADO).forward(req,resp);
                }else{
                    session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                    req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void discenteNovoEstagioStep(HttpServletRequest req, HttpServletResponse resp) {
        try{
            Collection<Part> parts = req.getParts();
            DiscenteNovoPedidoCreationDTO discenteNovoPedidoCreationDTO = new DiscenteNovoPedidoCreationDTO();
            DiscenteMapperImpl discenteMapper = new DiscenteMapperImpl();
            Curso curso = new Curso();
            Pedido pedido = new Pedido();
            Documento documento = new Documento();
            Discente discenteSession = (Discente) req.getSession().getAttribute("DISCENTE");
            parts.stream().forEach(part -> {
                try{
                    if (part.getName().equals("cpfDiscente")){
                        discenteNovoPedidoCreationDTO.setCpf(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("rgDiscente")) {
                        discenteNovoPedidoCreationDTO.setRg(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("orgaoExpedidorRgDiscente")) {
                        discenteNovoPedidoCreationDTO.setOrgaoExpedidorRg(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("iraDiscente")) {
                        discenteNovoPedidoCreationDTO.setIra(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("periodoDiscente")) {
                        discenteNovoPedidoCreationDTO.setPeriodo(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("cargaHorariaCumpridaDiscente")) {
                        discenteNovoPedidoCreationDTO.setCargaHorariaCumprida(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("enderecoDiscente")) {
                        discenteNovoPedidoCreationDTO.setEndereco(IOUtils.toString(part.getInputStream()));
                    }else if (part.getName().equals("selectCursoDiscente")) {
                        discenteNovoPedidoCreationDTO.setCurso(curso.getCursoById(Long.parseLong(IOUtils.toString(part.getInputStream()))).get());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            Discente discente = discenteMapper.toDiscenteCreateNovoEstagio(discenteNovoPedidoCreationDTO);
            discente.setId(discenteSession.getId());
            if (FileUtil.armazenarDocumentoDiscente(discente, req.getParts(),TipoPedido.NOVO)){
                HttpSession session = req.getSession();
                pedido.setTipo(TipoPedido.NOVO);
                pedido.setStatus(StatusPedido.NOVO_STEP1);
                discente.addInfoNovoPedidoInDiscente(discente);
                pedido.setDiscente(discente);
                pedido.criarPedido(pedido);
                FileUtil.criarDocumento(pedido, discente, TipoPedido.NOVO, TipoDocumento.HISTORICO_ACADEMICO, req.getParts().stream().filter(part -> "historicoDiscente".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.HISTORICO_ACADEMICO).get().getNome());
                FileUtil.criarDocumento(pedido, discente, TipoPedido.NOVO, TipoDocumento.GRADE_HORARIO, req.getParts().stream().filter(part -> "gradeHorarioDiscente".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(S3Util.getContextParameter(req,"access-key"),S3Util.getContextParameter(req,"secret-key"),S3Util.getContextParameter(req,"estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.GRADE_HORARIO).get().getNome());
                session.setAttribute("ID_PEDIDO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get().getId());
                if (Float.parseFloat(discente.getIra()) < 6 || (Float.parseFloat(discente.getCargaHorariaCumprida()) / 15) < 80){
                    pedido.changeStatusPedido(String.valueOf(pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"),TipoPedido.NOVO).get().getId()),StatusPedido.NOVO_STEP2_REJEITADO);
                    session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                    req.getRequestDispatcher(NOVO_ESTAGIO_REJEITADO).forward(req,resp);
                }else{
                    session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                    req.getRequestDispatcher(SUCESS_DISCENTE).forward(req, resp);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
