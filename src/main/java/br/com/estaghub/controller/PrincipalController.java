package br.com.estaghub.controller;

import br.com.estaghub.domain.*;
import br.com.estaghub.dto.DiscenteCreationDTO;
import br.com.estaghub.dto.EmpresaCreationDTO;
import br.com.estaghub.dto.SupervisorCreationDTO;
import br.com.estaghub.mapper.impl.DiscenteMapperImpl;
import br.com.estaghub.mapper.impl.EmpresaMapperImpl;
import br.com.estaghub.mapper.impl.SupervisorMapperImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "PrincipalController", value = "/principalController")
public class PrincipalController extends HttpServlet {
    private static final String SUCESS_DISCENTE = "/discente.jsp";
    private static final String SUCESS_DOCENTE = "/docente.jsp";
    private static final String SUCESS_DOCENTE_COMISSAO = "/docenteComissao.jsp";
    private static final String SUCESS_SUPERVISOR = "/supervisor.jsp";
    private static final String FAILED = "/index.jsp";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            if ("login".equals(req.getParameter("submitButtonLogin"))){
                String loginOptions = req.getParameter("loginOptions");
                String email = req.getParameter("emailLogin");
                String senha = req.getParameter("senhaLogin");
                if ("discente".equals(loginOptions)){
                    Discente discente = new Discente();
                    if(discente.loginDiscente(email,senha)){
                        RequestDispatcher view = req.getRequestDispatcher(SUCESS_DISCENTE);
                        HttpSession session = req.getSession();
                        Curso curso = new Curso();
                        if (!discente.getDiscenteByEmail(email).isEmpty()){
                            session.setAttribute("DISCENTE",discente.getDiscenteByEmail(email).get());
                            session.setAttribute("LIST_CURSOS",curso.getAllCursos());
                        }
                        view.forward(req,resp);
                    }else{
                        RequestDispatcher view = req.getRequestDispatcher(FAILED);
                        view.forward(req,resp);
                    }
                }else if ("docente".equals(loginOptions)) {
                    Docente docente = new Docente();
                    Pedido pedido = new Pedido();
                    if(docente.loginDocente(email,senha)){
                        HttpSession session = req.getSession();
                        if (docente.checkIfDocenteIsComissao(email)){
                            RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE_COMISSAO);
                            session.setAttribute("DOCENTE",docente.getDocenteByEmail(email).get());
                            session.setAttribute("LIST_PEDIDOS",pedido.getAllPedidos());
                            view.forward(req,resp);
                        }else{
                            RequestDispatcher view = req.getRequestDispatcher(SUCESS_DOCENTE);
                            session.setAttribute("EMAIL",email);
                            view.forward(req,resp);
                        }
                    }else{
                        RequestDispatcher view = req.getRequestDispatcher(FAILED);
                        view.forward(req,resp);
                    }
                }else if ("supervisor".equals(loginOptions)) {
                    Supervisor supervisor = new Supervisor();
                    if(supervisor.loginSupervisor(email,senha)){
                        RequestDispatcher view = req.getRequestDispatcher(SUCESS_SUPERVISOR);
                        HttpSession session = req.getSession();
                        session.setAttribute("EMAIL",email);
                        view.forward(req,resp);
                    }else{
                        RequestDispatcher view = req.getRequestDispatcher(FAILED);
                        view.forward(req,resp);
                    }
                }
            }
            if ("discente".equals(req.getParameter("submitButtonDiscente"))){
                DiscenteCreationDTO discenteDTO = DiscenteCreationDTO.builder().nome(req.getParameter("nomeDiscente"))
                        .email(req.getParameter("emailDiscente"))
                        .senha(req.getParameter("senhaDiscente"))
                        .matricula(req.getParameter("matriculaDiscente"))
                        .telefone(req.getParameter("telefoneDiscente"))
                        .build();
                DiscenteMapperImpl discenteMapper = new DiscenteMapperImpl();
                Discente discente = discenteMapper.toDiscenteCreateAccount(discenteDTO);
                discente.criarDiscente(discente);
            }
            if ("supervisor".equals(req.getParameter("submitButtonSupervisor1"))){
                SupervisorCreationDTO supervisorDTO = SupervisorCreationDTO.builder().nome(req.getParameter("nameSupervisor"))
                        .email(req.getParameter("emailSupervisor"))
                        .senha(req.getParameter("senhaSupervisor"))
                        .cargo(req.getParameter("cargoSupervisor"))
                        .telefone(req.getParameter("telefoneSupervisor"))
                        .build();
                Pedido pedido = new Pedido();
                String numPedido = req.getParameter("pedidoSupervisor");
                String cnpjEmpresaVinculada = req.getParameter("selectVinculoSupervisorEmpresa");
                SupervisorMapperImpl supervisorMapper = new SupervisorMapperImpl();
                Supervisor supervisor = supervisorMapper.toSupervisorCreateAccount(supervisorDTO);
                if (!cnpjEmpresaVinculada.isBlank() && pedido.getPedidoByIdWhereSupervisorNotSet(numPedido)){
                    supervisor.vincularEmpresa(supervisor, cnpjEmpresaVinculada, numPedido);
                }
            }
            if ("supervisor".equals(req.getParameter("submitButtonSupervisor2"))){
                SupervisorCreationDTO supervisorDTO = SupervisorCreationDTO.builder().nome(req.getParameter("nameSupervisor"))
                        .email(req.getParameter("emailSupervisor"))
                        .senha(req.getParameter("senhaSupervisor"))
                        .cargo(req.getParameter("cargoSupervisor"))
                        .telefone(req.getParameter("telefoneSupervisor"))
                        .build();
                EmpresaCreationDTO empresaDTO = EmpresaCreationDTO.builder().nome(req.getParameter("nomeEmpresaSupervisor"))
                        .cnpj(req.getParameter("cnpjEmpresaSupervisor"))
                        .endereco(req.getParameter("enderecoEmpresa"))
                        .email(req.getParameter("emailEmpresa"))
                        .telefone(req.getParameter("telefoneEmpresa"))
                        .build();
                String numPedido = req.getParameter("pedidoSupervisor");
                Pedido pedido = new Pedido();
                SupervisorMapperImpl supervisorMapper = new SupervisorMapperImpl();
                EmpresaMapperImpl empresaMapper = new EmpresaMapperImpl();
                Supervisor supervisor = supervisorMapper.toSupervisorCreateAccount(supervisorDTO);
                Empresa empresa = empresaMapper.toEmpresaCreateAccount(empresaDTO);
                if (!numPedido.equals("") && pedido.getPedidoByIdWhereSupervisorNotSet(numPedido)){
                    supervisor.criarSupervisor(supervisor, empresa, numPedido);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
