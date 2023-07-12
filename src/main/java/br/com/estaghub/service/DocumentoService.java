package br.com.estaghub.service;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.enums.TipoDocumento;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class DocumentoService {
    public static String OUTPUT_FILE = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos";

    public static void armazenarDocumento(String idUsuario, String idPedido, FileItem fileItem) {
        try{
            File fileSaveDir = new File(OUTPUT_FILE);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }
            if (fileItem.getFieldName().equals("historicoDiscente")){
                fileItem.write(new File(OUTPUT_FILE + File.separator + createNomeArquivo(idUsuario, idPedido, "HISTORICO_ACADEMICO", fileItem)));
            }else if(fileItem.getFieldName().equals("gradeHorarioDiscente")){
                fileItem.write(new File(OUTPUT_FILE + File.separator + createNomeArquivo(idUsuario, idPedido, "GRADE_HORARIO", fileItem)));
            }else if (fileItem.getFieldName().equals("planoAtividadesAssinado")){
                fileItem.write(new File(OUTPUT_FILE + File.separator + createNomeArquivo(idUsuario, idPedido, "PLANO_ATIVIDADES_ASSINADO_DISCENTE", fileItem)));
            }else if(fileItem.getFieldName().equals("tceAssinado") || fileItem.getFieldName().equals("fileTCEAssinado")){
                fileItem.write(new File(OUTPUT_FILE + File.separator + createNomeArquivo(idUsuario, idPedido, "TCE_ASSINADO_DISCENTE", fileItem)));
            }else if(fileItem.getFieldName().equals("termoAditivoAssinado") || fileItem.getFieldName().equals("fileTermoAditivoAssinado")){
                fileItem.write(new File(OUTPUT_FILE + File.separator + createNomeArquivo(idUsuario, idPedido, "TERMO_ADITIVO_ASSINADO_DISCENTE", fileItem)));
            }else if (fileItem.getFieldName().equals("planoAtividadesAssinadoDocente")){
                fileItem.write(new File(OUTPUT_FILE + File.separator + createNomeArquivo(idUsuario, idPedido, "PLANO_ATIVIDADES_ASSINADO_DOCENTE", fileItem)));
            }else if(fileItem.getFieldName().equals("tceAssinadoDocente")){
                fileItem.write(new File(OUTPUT_FILE + File.separator + createNomeArquivo(idUsuario, idPedido, "TCE_ASSINADO_DOCENTE", fileItem)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String createNomeArquivo(String idUsuario, String idPedido, String tipo, FileItem fileItem) {
        return String.format("%s-%s-%s-%s", idUsuario, idPedido, tipo, fileItem.getName());
    }
    public static String createNomeArquivoEstagio(String idDiscente, String idPedido, TipoDocumento tipoDocumento) {
        if (tipoDocumento == TipoDocumento.PLANO_ATIVIDADES){
            return String.format("%s-%s-%s-PLANO-DE-ATIVIDADES-DE-ESTÁGIO.doc",idDiscente,idPedido,tipoDocumento.name());
        }else if (tipoDocumento == TipoDocumento.TCE) {
            return String.format("%s-%s-%s-7-TERMO-DE-COMPROMISSO-DE-ESTAGIO-NAO-OBRIGATORIO-EXTERNO-ALUNOS-DA-UFRRJ.doc",idDiscente,idPedido,tipoDocumento.name());
        }else if (tipoDocumento == TipoDocumento.TERMO_ADITIVO) {
            return String.format("%s-%s-%s-MODELO-TERMO-ADITIVO-5.doc",idDiscente,idPedido,tipoDocumento.name());
        }
        return "";
    }

    public static void criarDocumento(Pedido pedido, String idUsuario, TipoDocumento tipoDocumento, Optional<FileItem> fileItem) {
        Documento documento = new Documento();
        documento.setPedido(pedido);
        documento.setTipoDocumento(tipoDocumento);
        fileItem.ifPresent(file -> documento.setNome(DocumentoService.createNomeArquivo(idUsuario, pedido.getId().toString(), tipoDocumento.name(), file)));
        documento.criarDocumento(documento);
    }

    public static void criarDocumentoEstagio(Pedido pedido, Discente discente, TipoDocumento tipoDocumento) {
        Documento documento = new Documento();
        documento.setPedido(pedido);
        documento.setTipoDocumento(tipoDocumento);
        documento.setNome((DocumentoService.createNomeArquivoEstagio(String.valueOf(discente.getId()),pedido.getId().toString(), tipoDocumento)));
        documento.criarDocumento(documento);
    }
    public static void deleteFile(String name){
        String path = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos/"+name;
        try{
            Files.delete(Path.of(path));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
