package br.com.estaghub.filters;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.domain.Departamento;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Pedido;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebFilter(value = "/docenteComissaoController/*")
public class DocenteComissaoFilter implements Filter {
    private static final String INDEX = "/index.jsp";
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Docente currentDocente = null;
        if (Objects.nonNull(req.getSession().getAttribute("DOCENTE_COMISSAO"))){
            currentDocente = new Docente().getDocenteById(((Docente) req.getSession().getAttribute("DOCENTE_COMISSAO")).getId()).get();
        }
        if (Objects.isNull(currentDocente)){
            req.setAttribute("UNAUTHORIZED", "true");
            req.getRequestDispatcher(INDEX).forward(req,resp);
        }else{
            Docente docente = new Docente();
            Pedido pedido = new Pedido();
            Departamento departamento = new Departamento();
            Curso curso = new Curso();
            req.getSession().setAttribute("DOCENTE_COMISSAO", currentDocente);
            req.getSession().setAttribute("LIST_PEDIDOS", pedido.getAllPedidosOfDocenteComissao(currentDocente));
            req.getSession().setAttribute("LIST_DEPARTAMENTOS",departamento.getAllDepartamentos());
            req.getSession().setAttribute("LIST_DEPARTAMENTOS_ATIVOS",departamento.getAllDepartamentosWithStatusActive());
            req.getSession().setAttribute("LIST_DOCENTES",docente.getAllDocentes());
            req.getSession().setAttribute("LIST_CURSOS",curso.getAllCursos());
            filterChain.doFilter(req,resp);
        }
    }
}
