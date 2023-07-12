package br.com.estaghub.controller;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.util.CryptUtil;
import br.com.estaghub.service.EmailService;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value = "/supervisorController")
public class SupervisorController extends HttpServlet {
    private static final String SUCESS_SUPERVISOR = "/supervisor.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        HttpSession session = req.getSession();
        try{
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            if ("editarSupervisor".equals(req.getParameter("submitButtonEditSupervisor"))) {
                editSupervisor(req, resp);
            }else if ("excluirSupervisor".equals(req.getParameter("submitButtonExcluirSupervisor"))) {
                Supervisor currentSupervisor = (Supervisor) req.getSession().getAttribute("SUPERVISOR");
                currentSupervisor.setIsActive(false);
                currentSupervisor.deleteSupervisor(currentSupervisor);
                req.getSession().invalidate();
                resp.getWriter().write("{\"excluirSupervisor\": true}");
            }else if ("pedido".equals(req.getParameter("buttonPedido"))) {
                session.setAttribute("ID_PEDIDO", req.getParameter("idPedido"));
                session.setAttribute("PEDIDO", pedido.getPedidoById(Long.parseLong(req.getParameter("idPedido"))));
                resp.getWriter().write("{\"visualizarPedido\": true}");
            }else if ("getInfoDiscente".equals(req.getParameter("submitButtonEscolherDiscente"))) {
                getInfoDiscente(req, resp);
            }else if ("vincularSupervisor".equals(req.getParameter("submitButtonVincularSupervisor"))) {
                vincularSupervisor(req, resp);
            }else if ("avaliacao".equals(req.getParameter("submitButtonSupervisorAvaliacao"))) {
                pedido.addAvaliacaoDesempenhoDiscenteInPedido(session.getAttribute("ID_PEDIDO").toString(),req.getParameter("textAvaliacao"));
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.RENOVACAO_STEP2);
                req.getSession().setAttribute("PEDIDOS_RENOVACAO", pedido.getAllPedidosOfSupervisor((Supervisor) req.getSession().getAttribute("SUPERVISOR")));
                Pedido pedidoToBeEvaluated = pedido.getPedidoById(Long.parseLong(session.getAttribute("ID_PEDIDO").toString()));
                req.getRequestDispatcher(SUCESS_SUPERVISOR).forward(req,resp);
                EmailService.sendInfoAboutPedido(req,pedidoToBeEvaluated.getDiscente().getEmail(), pedidoToBeEvaluated.getId().toString(),"O seu pedido <strong>#" + pedidoToBeEvaluated.getId() + "</strong> recebeu uma atualização, verifique o sistema!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void editSupervisor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Supervisor currentSupervisor = (Supervisor) req.getSession().getAttribute("SUPERVISOR");
        Supervisor supervisorToBeUpdated = new Supervisor();
        supervisorToBeUpdated.setId(currentSupervisor.getId());
        boolean isEdited = false;
        if(Strings.isNotBlank(req.getParameter("nomeSupervisor")) && !req.getParameter("nomeSupervisor").equals(currentSupervisor.getNome())){
            supervisorToBeUpdated.setNome(req.getParameter("nomeSupervisor"));
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("cargoSupervisor")) && !req.getParameter("cargoSupervisor").equals(currentSupervisor.getCargo())){
            supervisorToBeUpdated.setCargo(req.getParameter("cargoSupervisor"));
            isEdited = true;
        }
        if(Strings.isNotBlank(req.getParameter("telefoneSupervisor")) && !req.getParameter("telefoneSupervisor").equals(currentSupervisor.getTelefone())){
            supervisorToBeUpdated.setTelefone(req.getParameter("telefoneSupervisor"));
            isEdited = true;
        }
        if (!isEdited){
            resp.getWriter().write("{\"message\": \"Não houve nenhuma alteração nesse perfil!\"}");
        }else{
            currentSupervisor.editProfileSupervisor(supervisorToBeUpdated);
            resp.getWriter().write("{\"editarSupervisor\": true}");
        }
    }

    private static void getInfoDiscente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Discente selectedDiscente = new Discente();
        if (selectedDiscente.getDiscenteById(Long.parseLong(req.getParameter("selectDiscente"))).isPresent()){
            selectedDiscente = selectedDiscente.getDiscenteById(Long.parseLong(req.getParameter("selectDiscente"))).get();
        }
        StringBuilder json = new StringBuilder();
        json.append("{")
                .append("\"nome\": \""+selectedDiscente.getNome()+"\",")
                .append("\"nomeEmpresaPedido\": \""+new Pedido().getPedidoByDiscente(selectedDiscente, TipoPedido.RENOVACAO).get().getRenovacaoEstagio().getNomeEmpresa()+"\"")
                .append("}");
        resp.getWriter().write(json.toString());
    }
    private static void vincularSupervisor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Discente discente = new Discente();
        if (discente.getDiscenteById(Long.parseLong(req.getParameter("idDiscente"))).isPresent()){
            discente = discente.getDiscenteById(Long.parseLong(req.getParameter("idDiscente"))).get();
        }
        Pedido pedido = new Pedido();
        if (!req.getParameter("numeroPedido").equals(pedido.getPedidoByDiscente(discente, TipoPedido.RENOVACAO).get().getId().toString()) && !req.getParameter("cpfDiscente").replaceAll("[.-]","").equals(CryptUtil.decryptInfo(req,discente.getCpf()).replaceAll("[.-]",""))){
            resp.getWriter().write("{\"message\": \"Número do pedido e o cpf informado estão incorretos!\"}");
        }else{
            if(!req.getParameter("cpfDiscente").replaceAll("[.-]","").equals(CryptUtil.decryptInfo(req,discente.getCpf()).replaceAll("[.-]",""))){
                resp.getWriter().write("{\"message\": \"Número do cpf informado está incorreto!\"}");
            }else if(!req.getParameter("numeroPedido").equals(pedido.getPedidoByDiscente(discente, TipoPedido.RENOVACAO).get().getId().toString())){
                resp.getWriter().write("{\"message\": \"Número do pedido informado está incorreto!\"}");
            }else {
                pedido.addSupervisorNoPedido(new Supervisor().getSupervisorByEmail(((Supervisor) req.getSession().getAttribute("SUPERVISOR")).getEmail()).get(),req.getParameter("numeroPedido"));
                resp.getWriter().write("{\"vincularSupervisor\": true}");
            }
        }
    }
}
