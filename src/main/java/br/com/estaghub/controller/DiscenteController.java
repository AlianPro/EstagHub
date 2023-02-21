package br.com.estaghub.controller;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.dto.DiscenteNovoPedidoCreationDTO;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.mapper.impl.DiscenteMapperImpl;
import br.com.estaghub.util.FileUtil;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@WebServlet(name = "DiscenteController", value = "/discenteController")
@MultipartConfig(fileSizeThreshold=1024*1024*10,  	// 10 MB
location = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos")
public class DiscenteController extends HttpServlet {
    private static final String SUCESS_DISCENTE = "/discente.jsp";
    private static final String SUCESS_DOCENTE = "/docente.jsp";
    private static final String SUCESS_SUPERVISOR = "/supervisor.jsp";
    private static final String NOVO_ESTAGIO = "/novoEstagio.jsp";
    private static final String RENOVACAO_ESTAGIO = "/renovacaoEstagio.jsp";
    private static final String STATUS = "/statusProcesso.jsp";
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
            }
            else if (req.getParts().stream().anyMatch(partButton -> partButton.getName().equals("submitButtonDiscenteCadastroNovoEstagio1"))) {
                discenteNovoEstagioStep1(req);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void discenteNovoEstagioStep1(HttpServletRequest req) {
        try{
            Collection<Part> parts = req.getParts();
            DiscenteNovoPedidoCreationDTO discenteNovoPedidoCreationDTO = new DiscenteNovoPedidoCreationDTO();
            DiscenteMapperImpl discenteMapper = new DiscenteMapperImpl();
            Curso curso = new Curso();
            Pedido pedido = new Pedido();
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
                pedido.setTipo(TipoPedido.NOVO);
                pedido.setStatus(StatusPedido.NOVO_STEP1);
                discente.addInfoNovoPedidoInDiscente(discente);
                pedido.setDiscente(discente);
                pedido.criarPedido(pedido);
                criarDocumento(pedido, discente, TipoPedido.NOVO, TipoDocumento.HISTORICO_ACADEMICO, getFileName(req.getParts()));
                criarDocumento(pedido, discente, TipoPedido.NOVO, TipoDocumento.GRADE_HORARIO, getFileName(req.getParts()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void criarDocumento(Pedido pedido, Discente discente, TipoPedido tipoPedido, TipoDocumento tipoDocumento, Optional<Part> part) {
        pedido.getPedidoByDiscenteId(discente, tipoPedido).ifPresent(pedidoToBeSavedInDocumento ->{
            Documento documento = new Documento();
            documento.setPedido(pedidoToBeSavedInDocumento);
            documento.setTipoDocumento(tipoDocumento);
            part.ifPresent(value -> documento.setNome(FileUtil.createNomeArquivo(String.valueOf(discente.getId()), tipoDocumento.name(), value)));
            documento.criarDocumento(documento);
        });
    }

    private Optional<Part> getFileName(Collection<Part> parts) {
        for (Part part : parts){
            if (part.getName().equals("historicoDiscente")){
                return Optional.ofNullable(part);
            } else if (part.getName().equals("gradeHorarioDiscente")) {
                return Optional.ofNullable(part);
            }
        }
        return Optional.empty();
    }
}
