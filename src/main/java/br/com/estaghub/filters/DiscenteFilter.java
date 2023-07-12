package br.com.estaghub.filters;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.enums.TipoPedido;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebFilter(value = "/discenteController/*")
public class DiscenteFilter implements Filter {
    private static final String INDEX = "/index.jsp";
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Discente currentDiscente = null;
        if (Objects.nonNull(req.getSession().getAttribute("DISCENTE"))){
            currentDiscente = new Discente().getDiscenteById(((Discente) req.getSession().getAttribute("DISCENTE")).getId()).get();
        }
        if (Objects.isNull(currentDiscente)){
            req.setAttribute("UNAUTHORIZED", "true");
            req.getRequestDispatcher(INDEX).forward(req,resp);
        }else{
            Pedido pedido = new Pedido();
            req.getSession().setAttribute("DISCENTE", currentDiscente);
            if (pedido.getPedidoByDiscente(currentDiscente, TipoPedido.NOVO).isPresent()){
                Pedido currentPedidoNovoEstagio = pedido.getPedidoByDiscente(currentDiscente, TipoPedido.NOVO).get();
                req.getSession().setAttribute("NOVO_ESTAGIO", currentPedidoNovoEstagio);
                req.getSession().setAttribute("ID_PEDIDO", currentPedidoNovoEstagio.getId());
            }else if (pedido.getPedidoByDiscente(currentDiscente, TipoPedido.RENOVACAO).isPresent()){
                Pedido currentPedidoRenovacao = pedido.getPedidoByDiscente(currentDiscente, TipoPedido.RENOVACAO).get();
                req.getSession().setAttribute("RENOVACAO_ESTAGIO", currentPedidoRenovacao);
                req.getSession().setAttribute("ID_PEDIDO", currentPedidoRenovacao.getId());
            }else{
                req.getSession().setAttribute("NOVO_ESTAGIO", null);
                req.getSession().setAttribute("RENOVACAO_ESTAGIO", null);
            }
            filterChain.doFilter(req,resp);
        }
    }
}
