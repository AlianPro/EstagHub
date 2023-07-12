package br.com.estaghub.filters;

import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@WebFilter(value = "/docenteController/*")
public class DocenteFilter implements Filter {
    private static final String INDEX = "/index.jsp";
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Docente currentDocente = null;
        if (Objects.nonNull(req.getSession().getAttribute("DOCENTE"))){
            currentDocente = new Docente().getDocenteById(((Docente) req.getSession().getAttribute("DOCENTE")).getId()).get();
        }
        if (Objects.isNull(currentDocente)){
            req.setAttribute("UNAUTHORIZED", "true");
            req.getRequestDispatcher(INDEX).forward(req,resp);
        }else{
            Pedido pedido = new Pedido();
            req.getSession().setAttribute("DOCENTE", currentDocente);
            req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocente(currentDocente));
            filterChain.doFilter(req,resp);
        }
    }
}
