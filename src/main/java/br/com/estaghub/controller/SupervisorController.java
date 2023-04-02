package br.com.estaghub.controller;

import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.enums.StatusPedido;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "SupervisorController", value = "/supervisorController")
public class SupervisorController extends HttpServlet {
    private static final String SUCESS_SUPERVISOR = "/supervisor.jsp";
    private static final String LOGOUT = "/index.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Pedido pedido = new Pedido();
        HttpSession session = req.getSession();
        try{
            if ("logout".equals(req.getParameter("buttonLogoutSupervisor"))){
                session.invalidate();
                RequestDispatcher view = req.getRequestDispatcher(LOGOUT);
                view.forward(req,resp);
            }else if ("pedido".equals(req.getParameter("buttonPedido"))) {
                session.setAttribute("ID_PEDIDO", req.getParameter("idPedido"));
                session.setAttribute("PEDIDO", pedido.getPedidoById(Long.parseLong(req.getParameter("idPedido"))));
                session.setAttribute("STATUS_PEDIDO", req.getParameter("statusPedido"));
            }else if ("avaliacao".equals(req.getParameter("submitButtonSupervisorAvaliacao"))) {
                Supervisor supervisor = new Supervisor();
                pedido.addAvaliacaoDesempenhoDiscenteInPedido(session.getAttribute("ID_PEDIDO").toString(),req.getParameter("textAvaliacao"));
                pedido.changeStatusPedido(session.getAttribute("ID_PEDIDO").toString(),StatusPedido.RENOVACAO_STEP2);
                session.setAttribute("PEDIDOS_RENOVACAO",pedido.getAllPedidosOfSupervisor(supervisor.getSupervisorByEmail(session.getAttribute("EMAIL").toString())));
                RequestDispatcher view = req.getRequestDispatcher(SUCESS_SUPERVISOR);
                view.forward(req,resp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
