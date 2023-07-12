package br.com.estaghub.filters;

import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@WebFilter(value = "/supervisorController/*")
public class SupervisorFilter implements Filter {
    private static final String INDEX = "/index.jsp";
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Pedido pedido = new Pedido();
        Supervisor currentSupervisor = null;
        if (Objects.nonNull(req.getSession().getAttribute("SUPERVISOR"))){
            currentSupervisor = new Supervisor().getSupervisorByEmail(((Supervisor) req.getSession().getAttribute("SUPERVISOR")).getEmail()).get();
        }
        if (Objects.isNull(currentSupervisor)){
            req.setAttribute("UNAUTHORIZED", "true");
            req.getRequestDispatcher(INDEX).forward(req,resp);
        }else{
            req.getSession().setAttribute("SUPERVISOR",currentSupervisor);
            req.getSession().setAttribute("PEDIDOS_RENOVACAO", pedido.getAllPedidosOfSupervisor(currentSupervisor));
            req.getSession().setAttribute("LIST_DISCENTES_RENOV_STEP1", pedido.getAllRenovPedidosInStep1WithoutSupervisor().stream().map(p->p.getDiscente()).collect(Collectors.toList()));
            filterChain.doFilter(req,resp);
        }
    }
}
