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
import br.com.estaghub.service.DocumentoService;
import br.com.estaghub.service.EmailService;
import br.com.estaghub.util.S3Util;
import br.com.estaghub.util.ServletUtil;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@WebServlet(value = "/docenteComissaoController")
public class DocenteComissaoController extends HttpServlet {
    private static final String PEDIDOS_COMISSAO = "/pedidosDocenteComissao.jsp";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            if ("editarDocenteComissao".equals(req.getParameter("submitButtonEditDocenteComissao"))) {
                editDocenteComissao(req, resp);
            }else if ("excluirDocenteComissao".equals(req.getParameter("submitButtonExcluirDocenteComissao"))) {
                Docente currentDocente = (Docente) req.getSession().getAttribute("DOCENTE_COMISSAO");
                currentDocente.setIsActive(false);
                currentDocente.deleteDocente(currentDocente);
                req.getSession().invalidate();
                resp.getWriter().write("{\"excluirDocenteComissao\": true}");
            }else if ("pedido".equals(req.getParameter("buttonPedido"))) {
                visualizarPedido(req, resp);
            }else if ("step1".equals(req.getParameter("submitButtonDocenteAnalisaNovoPedido1"))) {
                analisarPedido(req, resp);
            }else if ("step2".equals(req.getParameter("submitButtonDocenteAnalisaRecursoNovoPedido2"))) {
                analisarRecurso(req, resp);
            }else if ("novoDocente".equals(req.getParameter("submitButtonDocenteCadastroNovoDocente"))) {
                criarDocente(req, resp);
            }else if ("getInfoDocente".equals(req.getParameter("submitButtonEscolherDocente"))) {
                getInfoDocente(req, resp);
            }else if ("editarDocente".equals(req.getParameter("submitButtonEditDocente"))) {
                editarDocente(req, resp);
            }else if ("novoCurso".equals(req.getParameter("submitButtonDocenteCadastroNovoCurso"))) {
                criarCurso(req, resp);
            }else if ("getInfoCurso".equals(req.getParameter("submitButtonEscolherCurso"))) {
                getInfoCurso(req, resp);
            }else if ("editarCurso".equals(req.getParameter("submitButtonEditCurso"))) {
                editarCurso(req, resp);
            }else if ("novoDepartamento".equals(req.getParameter("submitButtonDocenteCadastroDepartamento"))) {
                criarDepartamento(req, resp);
            }else if ("getInfoDepartamento".equals(req.getParameter("submitButtonEscolherDepartamento"))) {
                getInfoDepartamento(req, resp);
            }else if ("editarDepartamento".equals(req.getParameter("submitButtonEditDepartamento"))) {
                editarDepartamento(req, resp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void editDocenteComissao(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Docente currentDocente = (Docente) req.getSession().getAttribute("DOCENTE_COMISSAO");
        Docente docenteToBeUpdated = new Docente();
        docenteToBeUpdated.setId(currentDocente.getId());
        boolean isEdited = false;
        if(Strings.isNotBlank(req.getParameter("nomeDocenteComissao")) && !req.getParameter("nomeDocenteComissao").equals(currentDocente.getNome())){
            docenteToBeUpdated.setNome(req.getParameter("nomeDocenteComissao"));
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("siapeDocenteComissao")) && !req.getParameter("siapeDocenteComissao").equals(currentDocente.getSiape())){
            docenteToBeUpdated.setSiape(req.getParameter("siapeDocenteComissao"));
            isEdited = true;
        }
        if (Objects.nonNull(docenteToBeUpdated.getSiape())? currentDocente.checkSiapeOfDocente(docenteToBeUpdated) : false){
            resp.getWriter().write("{\"message\": \"Já existe um(a) docente com essa matrícula siape informada!\"}");
        }else if (!isEdited){
            resp.getWriter().write("{\"message\": \"Não houve nenhuma alteração nesse perfil!\"}");
        }else{
            currentDocente.editProfileDocente(docenteToBeUpdated);
            resp.getWriter().write("{\"editarDocenteComissao\": true}");
        }
    }

    private static void criarDepartamento(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Departamento departamento = new Departamento();
        DepartamentoMapperImpl departamentoMapper = new DepartamentoMapperImpl();
        if (!departamento.checkIfListOfDepartamentoAlreadyHaveThatDepartamento(req.getParameter("nomeDepartamento"), req.getParameter("siglaDepartamento"))){
            resp.getWriter().write("{\"message\": \"Já existe um departamento com esse nome ou essa sigla!\"}");
        }else{
            departamento.criarDepartamento(departamentoMapper.toDocenteCreateDepartamento(DepartamentoCreationDTO.builder().nome(req.getParameter("nomeDepartamento"))
                    .sigla(req.getParameter("siglaDepartamento"))
                    .build()));
            resp.getWriter().write("{\"criarDepartamento\": true}");
        }
    }

    private static void criarCurso(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Curso curso = new Curso();
        Departamento departamento = new Departamento();
        CursoMapperImpl cursoMapper = new CursoMapperImpl();
        if (!curso.checkIfDepartamentoAlreadyHaveThatCourse(req.getParameter("nomeCurso"), departamento.getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento"))))){
            resp.getWriter().write("{\"message\": \"Já existe um curso com esse nome nesse mesmo departamento!\"}");
        }else{
            curso.criarCurso(cursoMapper.toDocenteCreateCurso(CursoCreationDTO.builder().nome(req.getParameter("nomeCurso"))
                    .departamento(departamento.getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento"))))
                    .build()));
            resp.getWriter().write("{\"criarCurso\": true}");
        }
    }

    private static void criarDocente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Docente docente = new Docente();
        Departamento departamento = new Departamento();
        DocenteMapperImpl docenteMapper = new DocenteMapperImpl();
        HttpSession session = req.getSession();
        boolean isComissao = false;
        if ("comissao".equals(req.getParameter("radioComissao"))){
            isComissao=true;
        }
        Docente docenteToBeCreated = docenteMapper.toDocenteCreateAccount(DocenteCreationDTO.builder().nome(req.getParameter("nomeDocente"))
                .email(req.getParameter("emailDocente"))
                .senha(req.getParameter("senhaDocente"))
                .siape(req.getParameter("siapeDocente"))
                .departamento(departamento.getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento"))))
                .isDocenteComissao(isComissao).build(), (Docente)session.getAttribute("DOCENTE_COMISSAO"));
        if (docente.getDocenteByEmail(docenteToBeCreated.getEmail()).isPresent() || docente.checkSiapeOfDocente(docenteToBeCreated)){
            resp.getWriter().write("{\"message\": \"Já existe um(a) docente com esse email e/ou com o número do siape informado!\"}");
        }else{
            docente.criarDocente(docenteToBeCreated);
            resp.getWriter().write("{\"criarDocente\": true}");
        }
    }
    private static void getInfoDocente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Docente selectedDocente = new Docente();
        if (selectedDocente.getDocenteById(Long.parseLong(req.getParameter("selectDocente"))).isPresent()){
            selectedDocente = selectedDocente.getDocenteById(Long.parseLong(req.getParameter("selectDocente"))).get();
        }
        StringBuilder json = new StringBuilder();
        json.append("{")
        .append("\"nome\": \""+selectedDocente.getNome()+"\",")
        .append("\"email\": \""+selectedDocente.getEmail()+"\",")
        .append("\"siape\": \""+selectedDocente.getSiape()+"\",")
        .append("\"isAtivo\": \""+selectedDocente.getIsActive()+"\",")
        .append("\"isComissao\": \""+selectedDocente.getIsDocenteComissao()+"\",")
        .append("\"idDocenteDepartamento\": \""+selectedDocente.getDepartamento().getId()+"\",")
        .append("\"siglaDocenteDepartamento\": \""+selectedDocente.getDepartamento().getSigla()+"\",")
        .append("\"nomeDocenteDepartamento\": \""+selectedDocente.getDepartamento().getNome()+"\"")
        .append("}");
        resp.getWriter().write(json.toString());
    }
    private static void editarDocente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Docente docente = new Docente();
        if (docente.getDocenteById(Long.parseLong(req.getParameter("idDocente"))).isPresent()){
            docente = docente.getDocenteById(Long.parseLong(req.getParameter("idDocente"))).get();
        }
        Docente docenteToBeUpdated = new Docente();
        Departamento departamento = new Departamento();
        docenteToBeUpdated.setId(Long.parseLong(req.getParameter("idDocente")));
        boolean isEdited = false;
        if(Strings.isNotBlank(req.getParameter("nomeDocente")) && !req.getParameter("nomeDocente").equals(docente.getNome())){
            docenteToBeUpdated.setNome(req.getParameter("nomeDocente"));
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("emailDocente")) && !req.getParameter("emailDocente").equals(docente.getEmail())){
            docenteToBeUpdated.setEmail(req.getParameter("emailDocente"));
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("siapeDocente")) && !req.getParameter("siapeDocente").equals(docente.getSiape())){
            docenteToBeUpdated.setSiape(req.getParameter("siapeDocente"));
            isEdited = true;
        }
        if("ativado".equals(req.getParameter("radioStatus")) && !docente.getIsActive()){
            docenteToBeUpdated.setIsActive(true);
            isEdited = true;
        }else if("desativado".equals(req.getParameter("radioStatus")) && docente.getIsActive()){
            docenteToBeUpdated.setIsActive(false);
            isEdited = true;
        }
        if("comissao".equals(req.getParameter("radioComissao")) && !docente.getIsDocenteComissao()){
            docenteToBeUpdated.setIsDocenteComissao(true);
            isEdited = true;
        }else if("simples".equals(req.getParameter("radioComissao")) && docente.getIsDocenteComissao()){
            docenteToBeUpdated.setIsDocenteComissao(false);
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("selectDepartamento")) && Long.parseLong(req.getParameter("selectDepartamento")) != docente.getDepartamento().getId()){
            docenteToBeUpdated.setDepartamento(departamento.getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento"))));
            isEdited = true;
        }
        if (docente.getDocenteByEmail(docenteToBeUpdated.getEmail()).isPresent()){
            resp.getWriter().write("{\"message\": \"Já existe um(a) docente com esse email informado!\"}");
        }else if (docente.checkSiapeOfDocente(docenteToBeUpdated)){
            resp.getWriter().write("{\"message\": \"Já existe um(a) docente com esse número do siape informado!\"}");
        }else if (!isEdited){
            resp.getWriter().write("{\"message\": \"Não houve nenhuma alteração nesse(a) docente!\"}");
        }else{
            docente.updateDocente(docenteToBeUpdated);
            resp.getWriter().write("{\"editarDocente\": true}");
        }
    }
    private static void getInfoCurso(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Curso selectedCurso = new Curso();
        if (selectedCurso.getCursoById(Long.parseLong(req.getParameter("selectCurso"))).isPresent()){
            selectedCurso = selectedCurso.getCursoById(Long.parseLong(req.getParameter("selectCurso"))).get();
        }
        StringBuilder json = new StringBuilder();
        json.append("{")
                .append("\"nome\": \""+selectedCurso.getNome()+"\",")
                .append("\"isAtivo\": \""+selectedCurso.getIsActive()+"\",")
                .append("\"idCursoDepartamento\": \""+selectedCurso.getDepartamento().getId()+"\",")
                .append("\"siglaCursoDepartamento\": \""+selectedCurso.getDepartamento().getSigla()+"\",")
                .append("\"nomeCursoDepartamento\": \""+selectedCurso.getDepartamento().getNome()+"\"")
                .append("}");
        resp.getWriter().write(json.toString());
    }
    private static void editarCurso(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Curso curso = new Curso();
        if (curso.getCursoById(Long.parseLong(req.getParameter("idCurso"))).isPresent()){
            curso = curso.getCursoById(Long.parseLong(req.getParameter("idCurso"))).get();
        }
        Curso cursoToBeUpdated = new Curso();
        Departamento departamento = new Departamento();
        cursoToBeUpdated.setId(Long.parseLong(req.getParameter("idCurso")));
        boolean isEdited = false;
        if(Strings.isNotBlank(req.getParameter("nomeCurso")) && !req.getParameter("nomeCurso").equals(curso.getNome())){
            cursoToBeUpdated.setNome(req.getParameter("nomeCurso"));
            isEdited = true;
        }
        if("ativado".equals(req.getParameter("radioStatus")) && !curso.getIsActive()){
            cursoToBeUpdated.setIsActive(true);
            isEdited = true;
        }else if("desativado".equals(req.getParameter("radioStatus")) && curso.getIsActive()){
            cursoToBeUpdated.setIsActive(false);
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("selectDepartamento")) && Long.parseLong(req.getParameter("selectDepartamento")) != curso.getDepartamento().getId()){
            cursoToBeUpdated.setDepartamento(departamento.getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento"))));
            if (Strings.isBlank(cursoToBeUpdated.getNome())){
                cursoToBeUpdated.setNome(curso.getNome());
            }
            isEdited = true;
        }
        if (!curso.checkIfDepartamentoAlreadyHaveThatCourse(cursoToBeUpdated.getNome(), departamento.getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento"))))){
            resp.getWriter().write("{\"message\": \"Já existe um curso com esse nome nesse mesmo departamento!\"}");
        }else if (!isEdited){
            resp.getWriter().write("{\"message\": \"Não houve nenhuma alteração nesse curso!\"}");
        }else{
            curso.updateCurso(cursoToBeUpdated);
            resp.getWriter().write("{\"editarCurso\": true}");
        }
    }
    private static void getInfoDepartamento(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Departamento selectedDepartamento = new Departamento().getDepartamentoById(Long.parseLong(req.getParameter("selectDepartamento")));
        StringBuilder json = new StringBuilder();
        json.append("{")
                .append("\"nome\": \""+selectedDepartamento.getNome()+"\",")
                .append("\"sigla\": \""+selectedDepartamento.getSigla()+"\",")
                .append("\"isAtivo\": \""+selectedDepartamento.getIsActive()+"\"")
                .append("}");
        resp.getWriter().write(json.toString());
    }
    private static void editarDepartamento(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Departamento departamento = new Departamento().getDepartamentoById(Long.parseLong(req.getParameter("idDepartamento")));
        Departamento departamentoToBeUpdated = new Departamento();
        departamentoToBeUpdated.setId(Long.parseLong(req.getParameter("idDepartamento")));
        boolean isEdited = false;
        if(Strings.isNotBlank(req.getParameter("nomeDepartamento")) && !req.getParameter("nomeDepartamento").equals(departamento.getNome())){
            departamentoToBeUpdated.setNome(req.getParameter("nomeDepartamento"));
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("siglaDepartamento")) && !req.getParameter("siglaDepartamento").equals(departamento.getSigla())){
            departamentoToBeUpdated.setSigla(req.getParameter("siglaDepartamento"));
            isEdited = true;
        }
        if("ativado".equals(req.getParameter("radioStatus")) && !departamento.getIsActive()){
            departamentoToBeUpdated.setIsActive(true);
            isEdited = true;
        }else if("desativado".equals(req.getParameter("radioStatus")) && departamento.getIsActive()){
            departamentoToBeUpdated.setIsActive(false);
            isEdited = true;
        }
        if (!departamento.checkIfListOfDepartamentoAlreadyHaveThatDepartamento(departamentoToBeUpdated.getNome(), departamentoToBeUpdated.getSigla())){
            resp.getWriter().write("{\"message\": \"Já existe um departamento com esse nome ou essa sigla!\"}");
        }else if (!isEdited){
            resp.getWriter().write("{\"message\": \"Não houve nenhuma alteração nesse departamento!\"}");
        }else{
            departamento.updateDepartamento(departamentoToBeUpdated);
            resp.getWriter().write("{\"editarDepartamento\": true}");
        }
    }

    private static void analisarRecurso(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        Docente docente = new Docente();
        HttpSession session = req.getSession();
        String idPedido = session.getAttribute("ID_PEDIDO").toString();
        String decisao = req.getParameter("radioPedido");
        if ("rejeitado".equals(decisao)){
            String justificativa = req.getParameter("textAreaPedido");
            pedido.changeJustificativaRecursoPedido(idPedido,justificativa);
            Pedido pedidoToBeAnalyzed = pedido.getPedidoById(Long.parseLong(idPedido));
            pedido.changeStatusPedido(idPedido,StatusPedido.PEDIDO_ENCERRADO);
            req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocenteComissao((Docente) session.getAttribute("DOCENTE_COMISSAO")));
            req.getRequestDispatcher(PEDIDOS_COMISSAO).forward(req, resp);
            EmailService.sendInfoAboutPedido(req,pedidoToBeAnalyzed.getDiscente().getEmail(), pedidoToBeAnalyzed.getId().toString(),"O recurso relacionado ao seu pedido <strong>#" + pedidoToBeAnalyzed.getId() + "</strong> não foi aceito e o seu pedido foi finalizado pelo seguinte motivo: " + justificativa);
            new Documento().getAllDocumentosFromThatPedido(Long.parseLong(idPedido)).forEach(currentDocumento -> {
                DocumentoService.deleteFile(currentDocumento.getNome());
                S3Util.deleteFileS3(ServletUtil.getContextParameter(req, "access-key"), ServletUtil.getContextParameter(req, "secret-key"), ServletUtil.getContextParameter(req, "estaghub-bucket"), currentDocumento.getNome());
            });
        }else if("aceito".equals(decisao)){
            if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido)).getTipo().name())){
                aceitarRecurso(req, resp, pedido, docente, session, idPedido, StatusPedido.NOVO_STEP2);
            }else{
                aceitarRecurso(req, resp, pedido, docente, session, idPedido, StatusPedido.RENOVACAO_STEP3);
            }
        }
    }

    private static void aceitarRecurso(HttpServletRequest req, HttpServletResponse resp, Pedido pedido, Docente docente, HttpSession session, String idPedido, StatusPedido statusPedido) throws ServletException, IOException {
        String idDocente = req.getParameter("selectDocenteOrientador");
        pedido.changeStatusPedido(idPedido,statusPedido);
        if (docente.getDocenteById(Long.parseLong(idDocente)).isPresent()){
            pedido.addDocenteOrientadorInPedido(idPedido, docente.getDocenteById(Long.parseLong(idDocente)).get());
        }
        req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocenteComissao((Docente) session.getAttribute("DOCENTE_COMISSAO")));
        Pedido pedidoToBeAnalyzed = pedido.getPedidoById(Long.parseLong(idPedido));
        req.getRequestDispatcher(PEDIDOS_COMISSAO).forward(req, resp);
        EmailService.sendInfoAboutPedido(req,pedidoToBeAnalyzed.getDiscente().getEmail(), pedidoToBeAnalyzed.getId().toString(),"O seu pedido <strong>#" + pedidoToBeAnalyzed.getId() + "</strong> recebeu uma atualização, verifique o sistema!");
        if ("RENOVACAO".equals(pedidoToBeAnalyzed.getTipo().name())){
            EmailService.sendInfoAboutPedido(req,pedidoToBeAnalyzed.getDocenteOrientador().getEmail(), pedidoToBeAnalyzed.getId().toString(),"O pedido <strong>#" + pedidoToBeAnalyzed.getId() + "</strong> do(a) discente <strong>" + pedidoToBeAnalyzed.getDiscente().getNome() + "</strong> precisa da sua avaliação!");
        }
    }

    private static void analisarPedido(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        Docente docente = new Docente();
        HttpSession session = req.getSession();
        String idPedido = session.getAttribute("ID_PEDIDO").toString();
        String decisao = req.getParameter("radioPedido");
        if ("rejeitado".equals(decisao)){
            if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido)).getTipo().name())) {
                rejeitarPedido(req, resp, pedido, session, idPedido, StatusPedido.NOVO_STEP2_REJEITADO);
            }else{
                rejeitarPedido(req, resp, pedido, session, idPedido, StatusPedido.RENOVACAO_STEP3_REJEITADO);
            }
        }else if("aceito".equals(decisao)){
            if ("NOVO".equals(pedido.getPedidoById(Long.parseLong(idPedido)).getTipo().name())){
                aceitarPedido(req, resp, pedido, docente, session, idPedido, StatusPedido.NOVO_STEP2);
            }else{
                aceitarPedido(req, resp, pedido, docente, session, idPedido, StatusPedido.RENOVACAO_STEP3);
            }
        }
    }

    private static void rejeitarPedido(HttpServletRequest req, HttpServletResponse resp, Pedido pedido, HttpSession session, String idPedido, StatusPedido statusPedido) throws ServletException, IOException {
        String justificativa = req.getParameter("textAreaPedido");
        pedido.changeStatusPedido(idPedido,statusPedido);
        pedido.changeJustificativaDocentePedido(idPedido,justificativa);
        pedido.addDocenteComissaoInPedido(idPedido, (Docente) session.getAttribute("DOCENTE_COMISSAO"));
        req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocenteComissao((Docente) session.getAttribute("DOCENTE_COMISSAO")));
        Pedido pedidoToBeJustify = pedido.getPedidoById(Long.parseLong(idPedido));
        req.getRequestDispatcher(PEDIDOS_COMISSAO).forward(req, resp);
        EmailService.sendInfoAboutPedido(req,pedidoToBeJustify.getDiscente().getEmail(), pedidoToBeJustify.getId().toString(),"O seu pedido <strong>#" + pedidoToBeJustify.getId() + "</strong> recebeu uma atualização, verifique o sistema!");
    }

    private static void aceitarPedido(HttpServletRequest req, HttpServletResponse resp, Pedido pedido, Docente docente, HttpSession session, String idPedido, StatusPedido statusPedido) throws ServletException, IOException {
        String idDocente = req.getParameter("selectDocenteOrientador");
        pedido.changeStatusPedido(idPedido,statusPedido);
        pedido.addDocenteComissaoInPedido(idPedido, (Docente) session.getAttribute("DOCENTE_COMISSAO"));
        if (docente.getDocenteById(Long.parseLong(idDocente)).isPresent()){
            pedido.addDocenteOrientadorInPedido(idPedido, docente.getDocenteById(Long.parseLong(idDocente)).get());
        }
        req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocenteComissao((Docente) session.getAttribute("DOCENTE_COMISSAO")));
        Pedido pedidoToBeJustify = pedido.getPedidoById(Long.parseLong(idPedido));
        req.getRequestDispatcher(PEDIDOS_COMISSAO).forward(req, resp);
        EmailService.sendInfoAboutPedido(req,pedidoToBeJustify.getDiscente().getEmail(), pedidoToBeJustify.getId().toString(),"O seu pedido <strong>#" + pedidoToBeJustify.getId() + "</strong> recebeu uma atualização, verifique o sistema!");
        if ("RENOVACAO".equals(pedidoToBeJustify.getTipo().name())){
            EmailService.sendInfoAboutPedido(req,pedidoToBeJustify.getDocenteOrientador().getEmail(), pedidoToBeJustify.getId().toString(),"O pedido <strong>#" + pedidoToBeJustify.getId() + "</strong> do(a) discente <strong>" + pedidoToBeJustify.getDiscente().getNome() + "</strong> precisa da sua avaliação!");
        }
    }

    private static void visualizarPedido(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        Docente docente = new Docente();
        HttpSession session = req.getSession();
        Documento documento = new Documento();
        session.setAttribute("ID_PEDIDO", req.getParameter("idPedido"));
        session.setAttribute("PEDIDO", pedido.getPedidoById(Long.parseLong(req.getParameter("idPedido"))));
        String statusPedido = req.getParameter("statusPedido");
        String idPedido = req.getParameter("idPedido");
        if (StatusPedido.NOVO_STEP1.name().equals(statusPedido) || StatusPedido.RENOVACAO_STEP2.name().equals(statusPedido)) {
            session.setAttribute("DOCENTES_NAO_COMISSAO",docente.getAllDocentesOutComissao());
            getHistoricoGradeHorario(req, session, documento, idPedido);
        }else if (StatusPedido.NOVO_STEP2_JUSTIFICADO.name().equals(statusPedido) || StatusPedido.RENOVACAO_STEP3_JUSTIFICADO.name().equals(statusPedido)){
            session.setAttribute("DOCENTES_NAO_COMISSAO",docente.getAllDocentesOutComissao());
        }else if (StatusPedido.NOVO_STEP4.name().equals(statusPedido) || StatusPedido.NOVO_STEP4_DOCENTE_ASSINADO.name().equals(statusPedido)){
            getPlanoAtividadesTCE(req, session, documento, idPedido);
        }
        resp.getWriter().write("{\"visualizarPedido\": true}");
    }

    private static void getHistoricoGradeHorario(HttpServletRequest req, HttpSession session, Documento documento, String idPedido) {
        Documento currentDocumentoHistorico = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.HISTORICO_ACADEMICO).get();
        Documento currentDocumentoGrade = documento.getDocumentoByIdPedidoAndTipoDocumento(Long.parseLong(idPedido), TipoDocumento.GRADE_HORARIO).get();
        session.setAttribute("HISTORICO_ACADEMICO_URL",S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"), currentDocumentoHistorico.getNome()));
        session.setAttribute("GRADE_HORARIO_URL",S3Util.getFileS3(ServletUtil.getContextParameter(req,"access-key"),ServletUtil.getContextParameter(req,"secret-key"),ServletUtil.getContextParameter(req,"estaghub-bucket"), currentDocumentoGrade.getNome()));
        session.setAttribute("HISTORICO_ACADEMICO", currentDocumentoHistorico);
        session.setAttribute("GRADE_HORARIO", currentDocumentoGrade);
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
