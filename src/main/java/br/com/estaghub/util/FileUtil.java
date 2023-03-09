package br.com.estaghub.util;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.enums.TipoPedido;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;

public class FileUtil {
    public static String OUTPUT_FILE = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos";
    public static Boolean armazenarDocumentoDiscente(Discente discente, Collection<Part> parts, TipoPedido tipoPedido) {
        Pedido pedido = new Pedido();
        if (pedido.checkIfDiscenteAlreadyHavePedido(discente,tipoPedido)){
            try{
                int count=0;
                File fileSaveDir = new File(OUTPUT_FILE);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }
                for (Part part : parts) {
                    if (part.getName().equals("historicoDiscente")){
                        part.write(OUTPUT_FILE + File.separator + createNomeArquivo(String.valueOf(discente.getId()), "HISTORICO_ACADEMICO", part));
                        count++;
                    }else if(part.getName().equals("gradeHorarioDiscente")){
                        part.write(OUTPUT_FILE + File.separator + createNomeArquivo(String.valueOf(discente.getId()), "GRADE_HORARIO", part));
                        count++;
                    }
                }
                if (count==2){
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static void armazenarDocumentoAssinadoDiscente(Discente discente, Part part, TipoPedido tipoPedido) {
        Pedido pedido = new Pedido();
        if (!pedido.checkIfDiscenteAlreadyHavePedido(discente,tipoPedido)){
            try{
                File fileSaveDir = new File(OUTPUT_FILE);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }
                if (part.getName().equals("planoAtividadesAssinado")){
                    part.write(OUTPUT_FILE + File.separator + createNomeArquivo(String.valueOf(discente.getId()), "PLANO_ATIVIDADES_ASSINADO_DISCENTE", part));
                }else if(part.getName().equals("tceAssinado")){
                    part.write(OUTPUT_FILE + File.separator + createNomeArquivo(String.valueOf(discente.getId()), "TCE_ASSINADO_DISCENTE", part));
                }else if(part.getName().equals("termoAditivoAssinado")){
                    part.write(OUTPUT_FILE + File.separator + createNomeArquivo(String.valueOf(discente.getId()), "TERMO_ADITIVO_ASSINADO_DISCENTE", part));
                }else if(part.getName().equals("fileTCEAssinado")){
                    part.write(OUTPUT_FILE + File.separator + createNomeArquivo(String.valueOf(discente.getId()), "TCE_ASSINADO_DISCENTE", part));
                }else if(part.getName().equals("fileTermoAditivoAssinado")){
                    part.write(OUTPUT_FILE + File.separator + createNomeArquivo(String.valueOf(discente.getId()), "TERMO_ADITIVO_ASSINADO_DISCENTE", part));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void armazenarDocumentoAssinadoDocente(Docente docente, Part part) {
        try{
            File fileSaveDir = new File(OUTPUT_FILE);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }
            if (part.getName().equals("planoAtividadesAssinado")){
                part.write(OUTPUT_FILE + File.separator + createNomeArquivo(String.valueOf(docente.getId()), "PLANO_ATIVIDADES_ASSINADO_DOCENTE", part));
            }else if(part.getName().equals("tceAssinado")){
                part.write(OUTPUT_FILE + File.separator + createNomeArquivo(String.valueOf(docente.getId()), "TCE_ASSINADO_DOCENTE", part));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String createNomeArquivo(String id, String tipo, Part part) {
        return String.format("%s-%s-%s", id, tipo, part.getSubmittedFileName());
    }
    public static String createNomeArquivoEstagio(String idDiscente, TipoDocumento tipoDocumento) {
        if (tipoDocumento == TipoDocumento.PLANO_ATIVIDADES){
            return String.format("%s-%s-PLANO-DE-ATIVIDADES-DE-ESTÁGIO.doc",idDiscente,tipoDocumento.name());
        }else if (tipoDocumento == TipoDocumento.TCE) {
            return String.format("%s-%s-7-TERMO-DE-COMPROMISSO-DE-ESTAGIO-NAO-OBRIGATORIO-EXTERNO-ALUNOS-DA-UFRRJ.doc",idDiscente,tipoDocumento.name());
        }else if (tipoDocumento == TipoDocumento.TERMO_ADITIVO) {
            return String.format("%s-%s-MODELO-TERMO-ADITIVO-5.doc",idDiscente,tipoDocumento.name());
        }
        return "";
    }

    public static void criarDocumento(Pedido pedido, Discente discente, TipoPedido tipoPedido, TipoDocumento tipoDocumento, Optional<Part> part) {
        pedido.getPedidoByDiscente(discente, tipoPedido).ifPresent(pedidoToBeSavedInDocumento ->{
            Documento documento = new Documento();
            documento.setPedido(pedidoToBeSavedInDocumento);
            documento.setTipoDocumento(tipoDocumento);
            part.ifPresent(value -> documento.setNome(FileUtil.createNomeArquivo(String.valueOf(discente.getId()), tipoDocumento.name(), value)));
            documento.criarDocumento(documento);
        });
    }
    public static void criarDocumentoDocente(Pedido pedido, Docente docente, TipoDocumento tipoDocumento, Optional<Part> part) {
        Documento documento = new Documento();
        documento.setPedido(pedido);
        documento.setTipoDocumento(tipoDocumento);
        part.ifPresent(value -> documento.setNome(FileUtil.createNomeArquivo(String.valueOf(docente.getId()), tipoDocumento.name(), value)));
        documento.criarDocumento(documento);
    }
    public static void criarDocumentoEstagio(Pedido pedido, Discente discente, TipoPedido tipoPedido, TipoDocumento tipoDocumento) {
        pedido.getPedidoByDiscente(discente, tipoPedido).ifPresent(pedidoToBeSavedInDocumento ->{
            Documento documento = new Documento();
            documento.setPedido(pedidoToBeSavedInDocumento);
            documento.setTipoDocumento(tipoDocumento);
            documento.setNome((FileUtil.createNomeArquivoEstagio(String.valueOf(discente.getId()), tipoDocumento)));
            documento.criarDocumento(documento);
        });
    }
}
