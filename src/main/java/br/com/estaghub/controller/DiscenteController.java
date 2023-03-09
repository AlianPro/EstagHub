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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@WebServlet(name = "DiscenteController", value = "/discenteController")
@MultipartConfig(fileSizeThreshold=1024*1024*10,  	// 10 MB
location = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos")
public class DiscenteController extends HttpServlet {
    private static final String SUCESS_DISCENTE = "/discente.jsp";
    private static final String NOVO_STEP3 = "/emitirTCE.jsp";
    private static final String DISCENTE_ASSINA_DOCUMENTO = "/assinarDocumentoDiscente.jsp";
    private static final String FINAL_STEP = "/finalStep.jsp";
    private static final String SUCESS_DOCENTE = "/docente.jsp";
    private static final String SUCESS_SUPERVISOR = "/supervisor.jsp";
    private static final String NOVO_ESTAGIO = "/novoEstagio.jsp";
    private static final String NOVO_ESTAGIO_REJEITADO = "/justificativaNovoEstagio.jsp";
    private static final String RENOVACAO_ESTAGIO = "/renovacaoEstagio.jsp";
    private static final String LOGOUT = "/index.jsp";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            if ("logout".equals(req.getParameter("buttonLogoutDiscente"))){
                HttpSession session = req.getSession();
                session.invalidate();
                RequestDispatcher view = req.getRequestDispatcher(LOGOUT);
                view.forward(req,resp);
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteCadastroNovoEstagio1"))) {
                discenteNovoEstagioStep(req, resp);
            }else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteJustificativa2"))){
                String justificativa="";
                for (Part part : req.getParts()){
                    if ("textAreaJustificativa".equals(part.getName())){
                        justificativa = IOUtils.toString(part.getInputStream());
                    }
                }
                HttpSession session = req.getSession();
                Pedido pedido = new Pedido();
                pedido.changeJustificativaDiscentePedido(session.getAttribute("ID_PEDIDO").toString(),justificativa);
                if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name())){
                    pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP2_JUSTIFICADO);
                    session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                }else{
                    pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.RENOVACAO_STEP3_JUSTIFICADO);
                    session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                }
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_DISCENTE);
                view.forward(req,resp);
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
    private void discenteFinalizarPedido(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        if (req.getParts().stream().anyMatch(part -> "submitButtonDiscenteFinalizar".equals(part.getName()))){
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.PEDIDO_ENCERRADO);
            RequestDispatcher view = req.getRequestDispatcher(SUCESS_DISCENTE);
            view.forward(req, resp);
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
                if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).isPresent()){
                    documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE);
                }
                FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.RENOVACAO, TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE, req.getParts().stream().filter(part -> "fileTermoAditivoAssinado".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get().getNome());
                session.setAttribute("TERMO_ADITIVO_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get().getNome()));
                session.setAttribute("TERMO_ADITIVO",documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get());
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_PEDIDO_FIM);
                session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                RequestDispatcher view = req.getRequestDispatcher(FINAL_STEP);
                view.forward(req, resp);
            }else{
                documentoService.gerarTermoAditivo(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.TERMO_ADITIVO, termoAditivoCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())),pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
                FileUtil.criarDocumentoEstagio(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.RENOVACAO, TipoDocumento.TERMO_ADITIVO);
                Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO).get().getId();
                documento.addTermoAditivoInDocumento(String.valueOf(idDocumento),termoAditivo);
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO).get().getNome());
                session.setAttribute("TERMO_ADITIVO_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO).get().getNome()));
                session.setAttribute("TERMO_ADITIVO",documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO).get());
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.RENOVACAO_STEP4_DISCENTE_ASSINADO);
                session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                RequestDispatcher view = req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO);
                view.forward(req, resp);
            }
        }
    }
    private void discenteAssinarDocumento(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        Discente discente = new Discente();
        TCECreationDTO tceCreationDTO = new TCECreationDTO();
        TCEMapperImpl tceMapper = new TCEMapperImpl();
        DocumentoService documentoService = new DocumentoService();
        Documento documento = new Documento();
        if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name())){
            if ("NOVO_STEP3_DISCENTE_ASSINADO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
                if (req.getParts().stream().anyMatch(part -> "planoAtividadesAssinado".equals(part.getName()))){
                    FileUtil.armazenarDocumentoAssinadoDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), req.getParts().stream().filter(part -> "planoAtividadesAssinado".equals(part.getName())).findFirst().get(),TipoPedido.NOVO);
                    if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).isPresent()){
                        documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE);
                    }
                    FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.NOVO, TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE, req.getParts().stream().filter(part -> "planoAtividadesAssinado".equals(part.getName())).findFirst());
                    S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES_ASSINADO_DISCENTE).get().getNome());
                }
                if (req.getParts().stream().anyMatch(part -> "tceAssinado".equals(part.getName()))){
                    FileUtil.armazenarDocumentoAssinadoDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), req.getParts().stream().filter(part -> "tceAssinado".equals(part.getName())).findFirst().get(),TipoPedido.NOVO);
                    if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE).isPresent()){
                        documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE);
                    }
                    FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.NOVO, TipoDocumento.TCE_ASSINADO_DISCENTE, req.getParts().stream().filter(part -> "tceAssinado".equals(part.getName())).findFirst());
                    S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                }
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP4);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_DISCENTE);
                view.forward(req, resp);
            }
        }else if ("RENOVACAO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getTipo().name())){
            if ("RENOVACAO_STEP4_DISCENTE_ASSINADO".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
                if (req.getParts().stream().anyMatch(part -> "termoAditivoAssinado".equals(part.getName()))){
                    FileUtil.armazenarDocumentoAssinadoDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), req.getParts().stream().filter(part -> "termoAditivoAssinado".equals(part.getName())).findFirst().get(),TipoPedido.RENOVACAO);
                    if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).isPresent()){
                        documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE);
                    }
                    FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.RENOVACAO, TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE, req.getParts().stream().filter(part -> "termoAditivoAssinado".equals(part.getName())).findFirst());
                    S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get().getNome());
                }
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_PEDIDO_FIM);
                session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                session.setAttribute("TERMO_ADITIVO", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get());
                session.setAttribute("TERMO_ADITIVO_URL", S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getId(), TipoDocumento.TERMO_ADITIVO_ASSINADO_DISCENTE).get().getNome()));
                RequestDispatcher view = req.getRequestDispatcher(FINAL_STEP);
                view.forward(req, resp);
            }
        }
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
                if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE).isPresent()){
                    documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE);
                }
                FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.NOVO, TipoDocumento.TCE_ASSINADO_DISCENTE, req.getParts().stream().filter(part -> "fileTCEAssinado".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP4);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_DISCENTE);
                view.forward(req, resp);
            }else{
                documentoService.gerarTCE(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.TCE, tceCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())));
                FileUtil.criarDocumentoEstagio(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.NOVO, TipoDocumento.TCE);
                Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getId();
                documento.addTCEInDocumento(String.valueOf(idDocumento),tce);
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getNome());
                session.setAttribute("TCE_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getNome()));
                session.setAttribute("TCE",documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get());
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                RequestDispatcher view = req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO);
                view.forward(req, resp);
            }
        }else if ("NOVO_STEP4_TCE".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
            if (req.getParts().stream().anyMatch(part -> "fileTCEAssinado".equals(part.getName()))){
                FileUtil.armazenarDocumentoAssinadoDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), req.getParts().stream().filter(part -> "fileTCEAssinado".equals(part.getName())).findFirst().get(),TipoPedido.NOVO);
                if (documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE).isPresent()){
                    documento.removeDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()),TipoDocumento.TCE_ASSINADO_DISCENTE);
                }
                FileUtil.criarDocumento(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())), discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())), TipoPedido.NOVO, TipoDocumento.TCE_ASSINADO_DISCENTE, req.getParts().stream().filter(part -> "fileTCEAssinado".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE_ASSINADO_DISCENTE).get().getNome());
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP4);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_DISCENTE);
                view.forward(req, resp);
            }else{
                Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getId();
                documentoService.gerarTCE(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.TCE, tceCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())));
                documento.addTCEInDocumento(String.valueOf(idDocumento),tce);
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getNome());
                session.setAttribute("TCE_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get().getNome()));
                session.setAttribute("TCE",documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.TCE).get());
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO);
                session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                RequestDispatcher view = req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO);
                view.forward(req, resp);
            }
        }
    }

    private void discenteElaborarPlanoAtividades(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Pedido pedido = new Pedido();
        Discente discente = new Discente();
        ArrayList<String> atividades = new ArrayList<>();
        PlanoAtividadesCreationDTO planoAtividadesCreationDTO = new PlanoAtividadesCreationDTO();
        PlanoAtividadesMapperImpl planoAtividadesMapper = new PlanoAtividadesMapperImpl();
        DocumentoService documentoService = new DocumentoService();
        Documento documento = new Documento();
        req.getParts().stream().forEach(part -> {
            try{
                if (part.getName().equals("primeiraAtividade") || part.getName().equals("segundaAtividade") || part.getName().equals("terceiraAtividade") || part.getName().equals("quartaAtividade") || part.getName().equals("quintaAtividade")){
                    atividades.add(IOUtils.toString(part.getInputStream()));
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
            documento.addPlanoAtividadesInDocumento(String.valueOf(idDocumento),planoAtividades);
            S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getNome());
            session.setAttribute("PLANO_ATIVIDADES_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getNome()));
            session.setAttribute("PLANO_ATIVIDADES",documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get());
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP3);
            session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
            RequestDispatcher view = req.getRequestDispatcher(NOVO_STEP3);
            view.forward(req, resp);
        }else if ("NOVO_STEP4_PLANO_ATIVIDADES".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())){
            Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getId();
            documentoService.gerarPlanoAtividades(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.PLANO_ATIVIDADES, atividades, planoAtividadesCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())),pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
            documento.addPlanoAtividadesInDocumento(String.valueOf(idDocumento),planoAtividades);
            S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getNome());
            session.setAttribute("PLANO_ATIVIDADES_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getNome()));
            session.setAttribute("PLANO_ATIVIDADES",documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get());
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP3_DISCENTE_ASSINADO);
            session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
            RequestDispatcher view = req.getRequestDispatcher(DISCENTE_ASSINA_DOCUMENTO);
            view.forward(req, resp);
        }else if ("NOVO_STEP4_ATIVIDADES_TCE".equals(pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())).getStatus().name())) {
            Long idDocumento = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getId();
            documentoService.gerarPlanoAtividades(session.getAttribute("ID_DISCENTE").toString(), TipoDocumento.PLANO_ATIVIDADES, atividades, planoAtividadesCreationDTO, discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())),pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString())));
            documento.addPlanoAtividadesInDocumento(String.valueOf(idDocumento),planoAtividades);
            S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getNome());
            session.setAttribute("PLANO_ATIVIDADES_URL",S3Util.getFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get().getNome()));
            session.setAttribute("PLANO_ATIVIDADES",documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()), TipoDocumento.PLANO_ATIVIDADES).get());
            pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.NOVO_STEP4_TCE);
            session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
            session.setAttribute("DOCUMENTO",documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteById(Long.parseLong(session.getAttribute("ID_DISCENTE").toString())),TipoPedido.NOVO).get().getId(), TipoDocumento.TCE).get());
            RequestDispatcher view = req.getRequestDispatcher(NOVO_STEP3);
            view.forward(req, resp);
        }
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
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.HISTORICO_ACADEMICO).get().getNome());
                FileUtil.criarDocumento(pedido, discente, TipoPedido.RENOVACAO, TipoDocumento.GRADE_HORARIO, req.getParts().stream().filter(part -> "gradeHorarioDiscente".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.GRADE_HORARIO).get().getNome());
                if (Float.parseFloat(discente.getIra()) < 5){
                    pedido.changeStatusPedido(String.valueOf(pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"),TipoPedido.RENOVACAO).get().getId()),StatusPedido.RENOVACAO_STEP3_REJEITADO);
                    session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                    RequestDispatcher view = req.getRequestDispatcher(NOVO_ESTAGIO_REJEITADO);
                    view.forward(req,resp);
                }else{
                    session.setAttribute("RENOVACAO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.RENOVACAO).get());
                    RequestDispatcher view = req.getRequestDispatcher(SUCESS_DISCENTE);
                    view.forward(req,resp);
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
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.HISTORICO_ACADEMICO).get().getNome());
                FileUtil.criarDocumento(pedido, discente, TipoPedido.NOVO, TipoDocumento.GRADE_HORARIO, req.getParts().stream().filter(part -> "gradeHorarioDiscente".equals(part.getName())).findFirst());
                S3Util.uploadFileS3(getContextParameter("access-key"),getContextParameter("secret-key"),getContextParameter("estaghub-bucket"),documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getId(), TipoDocumento.GRADE_HORARIO).get().getNome());
                if (Float.parseFloat(discente.getIra()) < 6 || (Float.parseFloat(discente.getCargaHorariaCumprida()) / 15) < 80){
                    pedido.changeStatusPedido(String.valueOf(pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"),TipoPedido.NOVO).get().getId()),StatusPedido.NOVO_STEP2_REJEITADO);
                    session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                    RequestDispatcher view = req.getRequestDispatcher(NOVO_ESTAGIO_REJEITADO);
                    view.forward(req,resp);
                }else{
                    session.setAttribute("NOVO_ESTAGIO",pedido.getPedidoByDiscente((Discente)session.getAttribute("DISCENTE"), TipoPedido.NOVO).get());
                    RequestDispatcher view = req.getRequestDispatcher(SUCESS_DISCENTE);
                    view.forward(req,resp);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getContextParameter(String name) {
        return getServletContext().getInitParameter(name);
    }
}
