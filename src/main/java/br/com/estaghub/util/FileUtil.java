package br.com.estaghub.util;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.enums.TipoPedido;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collection;

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
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String createNomeArquivo(String idDiscente, String tipo, Part part) {
        return String.format("%s-%s-%s", idDiscente, tipo, part.getSubmittedFileName());
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
}
