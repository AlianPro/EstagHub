package br.com.estaghub.filters;

import br.com.estaghub.domain.Empresa;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(value = "/principalController/*")
public class PrincipalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        req.setAttribute("LIST_EMPRESAS",new Empresa().listAllEmpresa());
        filterChain.doFilter(req,resp);
    }
}
