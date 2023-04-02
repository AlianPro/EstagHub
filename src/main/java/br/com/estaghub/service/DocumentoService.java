package br.com.estaghub.service;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.dto.PlanoAtividadesCreationDTO;
import br.com.estaghub.dto.TCECreationDTO;
import br.com.estaghub.dto.TermoAditivoCreationDTO;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.enums.TipoPedido;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DocumentoService {
    public static String PLANO_ATIVIDADE = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/PLANO-DE-ATIVIDADES-DE-ESTÁGIO.doc";
    public static String TCE = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/7-TERMO-DE-COMPROMISSO-DE-ESTAGIO-NAO-OBRIGATORIO-EXTERNO-ALUNOS-DA-UFRRJ.doc";
    public static String TERMO_ADITIVO = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/MODELO-TERMO-ADITIVO-5.doc";

    private HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
        Range r = doc.getRange();
        for (int i = 0; i < r.numSections(); ++i) {
            Section s = r.getSection(i);
            for (int j = 0; j < s.numParagraphs(); j++) {
                Paragraph p = s.getParagraph(j);
                for (int k = 0; k < p.numCharacterRuns(); k++) {
                    CharacterRun run = p.getCharacterRun(k);
                    String text = run.text();
                    if (text.contains(findText)) {
                        run.replaceText(findText, replaceText);
                    }
                }
            }
        }
        return doc;
    }
    private HWPFDocument openDocument(String doc) throws Exception {
        HWPFDocument document = new HWPFDocument(new POIFSFileSystem(
                new File(doc)));
        return document;
    }

    private void saveDocument(HWPFDocument doc, String file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            doc.write(out);
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String createNomeArquivo(String idDiscente, TipoDocumento tipoDocumento) {
        if (tipoDocumento == TipoDocumento.PLANO_ATIVIDADES){
           return String.format("/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos/%s-%s-PLANO-DE-ATIVIDADES-DE-ESTÁGIO.doc",idDiscente,tipoDocumento.name());
        }else if (tipoDocumento == TipoDocumento.TCE) {
            return String.format("/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos/%s-%s-7-TERMO-DE-COMPROMISSO-DE-ESTAGIO-NAO-OBRIGATORIO-EXTERNO-ALUNOS-DA-UFRRJ.doc",idDiscente,tipoDocumento.name());
        }else if (tipoDocumento == TipoDocumento.TERMO_ADITIVO) {
            return String.format("/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos/%s-%s-MODELO-TERMO-ADITIVO-5.doc",idDiscente,tipoDocumento.name());
        }
        return "";
    }
    public void gerarPlanoAtividades(String idDiscente, TipoDocumento tipoDocumento, List<Optional<String>> atividades, PlanoAtividadesCreationDTO planoAtividadesCreationDTO, Discente discente, Pedido pedido){
        try{
            DocumentoService instance = new DocumentoService();
            DecimalFormat dmFormat = new DecimalFormat("00");
            if (tipoDocumento == TipoDocumento.PLANO_ATIVIDADES){
                HWPFDocument doc = instance.openDocument(PLANO_ATIVIDADE);
                if (doc != null) {
                    doc = instance.replaceText(doc, "{primeiraAtividade}", atividades.get(0).isPresent()? atividades.get(0).get() : "");
                    doc = instance.replaceText(doc, "{segundaAtividade}", atividades.get(1).isPresent()? atividades.get(1).get() : "");
                    doc = instance.replaceText(doc, "{terceiraAtividade}", atividades.get(2).isPresent()? atividades.get(2).get() : "");
                    doc = instance.replaceText(doc, "{quartaAtividade}", atividades.get(3).isPresent()? atividades.get(3).get() : "");
                    doc = instance.replaceText(doc, "{quintaAtividade}", atividades.get(4).isPresent()? atividades.get(4).get() : "");
                    doc = instance.replaceText(doc, "{nomeDiscente}", discente.getNome());
                    doc = instance.replaceText(doc, "{matriculaDiscente}", discente.getMatricula());
                    doc = instance.replaceText(doc, "{cursoDiscente}", discente.getCurso().getNome());
                    doc = instance.replaceText(doc, "{periodoCurso}", discente.getPeriodo());
                    doc = instance.replaceText(doc, "{cpfDiscente}", discente.getCpf());
                    doc = instance.replaceText(doc, "{emailDiscente}", discente.getEmail());
                    doc = instance.replaceText(doc, "{nomeEmpresa}", planoAtividadesCreationDTO.getNomeEmpresa());
                    doc = instance.replaceText(doc, "{nomeResponsavelEmpresa}", planoAtividadesCreationDTO.getResponsavelEmpresa());
                    doc = instance.replaceText(doc, "{enderecoEmpresa/telefoneEmpresa/emailEmpresa}", planoAtividadesCreationDTO.getEnderecoEmpresa()+" / "+planoAtividadesCreationDTO.getTelefoneEmpresa()+" / "+planoAtividadesCreationDTO.getEmailEmpresa());
                    doc = instance.replaceText(doc, "{nomeSupervisor}", planoAtividadesCreationDTO.getNomeSupervisor());
                    doc = instance.replaceText(doc, "{formacaoSupervisor}", planoAtividadesCreationDTO.getFormacaoSupervisor());
                    doc = instance.replaceText(doc, "{nomeOrientador}", pedido.getDocenteOrientador().getNome());
                    doc = instance.replaceText(doc, "{matriculaOrientador}", pedido.getDocenteOrientador().getSiape());
                    doc = instance.replaceText(doc, "{dia}", dmFormat.format(LocalDate.now().getDayOfMonth()));
                    doc = instance.replaceText(doc, "{mes}", dmFormat.format(LocalDate.now().getMonthValue()));
                    doc = instance.replaceText(doc, "{ano}", String.valueOf(LocalDate.now().getYear()).substring(2,4));
                    instance.saveDocument(doc, createNomeArquivo(idDiscente,tipoDocumento));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void gerarTCE(String idDiscente, TipoDocumento tipoDocumento, TCECreationDTO tceCreationDTO, Discente discente){
        try{
            DocumentoService instance = new DocumentoService();
            Pedido pedido = new Pedido();
            Documento documento = new Documento();
            DecimalFormat dmFormat = new DecimalFormat("00");
            if (tipoDocumento == TipoDocumento.TCE){
                HWPFDocument doc = instance.openDocument(TCE);
                if (doc != null) {
                    doc = instance.replaceText(doc, "{nomeDiscente}", discente.getNome());
                    doc = instance.replaceText(doc, "{nomeConcedente}", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteById(Long.parseLong(idDiscente)), TipoPedido.NOVO).get().getId(),TipoDocumento.PLANO_ATIVIDADES).get().getPlanoAtividades().getNomeEmpresa());
                    doc = instance.replaceText(doc, "{cnpjConcedente}", tceCreationDTO.getCnpjEmpresa());
                    doc = instance.replaceText(doc, "{periodoCurso}", discente.getPeriodo());
                    doc = instance.replaceText(doc, "{nomeCurso}", discente.getCurso().getNome());
                    doc = instance.replaceText(doc, "{matriculaDiscente}", discente.getMatricula());
                    doc = instance.replaceText(doc, "{rgDiscente}", discente.getRg());
                    doc = instance.replaceText(doc, "{orgaoExpedidor}", discente.getOrgaoExpedidorRg());
                    doc = instance.replaceText(doc, "{cpfDiscente}", discente.getCpf());
                    doc = instance.replaceText(doc, "{enderecoDiscente}", discente.getEndereco());
                    doc = instance.replaceText(doc, "{horarioInicio}", tceCreationDTO.getHorarioInicio());
                    doc = instance.replaceText(doc, "{horarioFim}", tceCreationDTO.getHorarioFim());
                    doc = instance.replaceText(doc, "{intervalo}", tceCreationDTO.getIntervalo());
                    doc = instance.replaceText(doc, "{totalHoras}", tceCreationDTO.getTotalHoras());
                    doc = instance.replaceText(doc, "{diaInicio}", tceCreationDTO.getDataInicio().substring(0,2));
                    doc = instance.replaceText(doc, "{mesInicio}", tceCreationDTO.getDataInicio().substring(3,5));
                    doc = instance.replaceText(doc, "{anoInicio}", tceCreationDTO.getDataInicio().substring(6,10));
                    doc = instance.replaceText(doc, "{diaFim}", tceCreationDTO.getDataFim().substring(0,2));
                    doc = instance.replaceText(doc, "{mesFim}", tceCreationDTO.getDataFim().substring(3,5));
                    doc = instance.replaceText(doc, "{anoFim}", tceCreationDTO.getDataFim().substring(6,10));
                    doc = instance.replaceText(doc, "{bolsa}", tceCreationDTO.getBolsa());
                    doc = instance.replaceText(doc, "{auxTransporte}", tceCreationDTO.getAuxTransporte());
                    doc = instance.replaceText(doc, "{codApolice}", tceCreationDTO.getCodApolice());
                    doc = instance.replaceText(doc, "{nomeSeguradora}", tceCreationDTO.getNomeSeguradora());
                    doc = instance.replaceText(doc, "{dia}", dmFormat.format(LocalDate.now().getDayOfMonth()));
                    doc = instance.replaceText(doc, "{mes}", dmFormat.format(LocalDate.now().getMonthValue()));
                    doc = instance.replaceText(doc, "{ano}", String.valueOf(LocalDate.now().getYear()));
                    instance.saveDocument(doc, createNomeArquivo(idDiscente,tipoDocumento));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void gerarTermoAditivo(String idDiscente, TipoDocumento tipoDocumento, TermoAditivoCreationDTO termoAditivoCreationDTO, Discente discente, Pedido pedido){
        try{
            DocumentoService instance = new DocumentoService();
            DecimalFormat dmFormat = new DecimalFormat("00");
            if (tipoDocumento == TipoDocumento.TERMO_ADITIVO){
                HWPFDocument doc = instance.openDocument(TERMO_ADITIVO);
                if (doc != null) {
                    doc = instance.replaceText(doc, "{nomeEmpresa}", pedido.getSupervisor().getEmpresa().getNome());
                    doc = instance.replaceText(doc, "{nomeDiscente}", discente.getNome());
                    doc = instance.replaceText(doc, "{periodoCurso}", discente.getPeriodo());
                    doc = instance.replaceText(doc, "{nomeCurso}", discente.getCurso().getNome());
                    doc = instance.replaceText(doc, "{diaAntigo}", termoAditivoCreationDTO.getDataAntiga().substring(0,2));
                    doc = instance.replaceText(doc, "{mesAntigo}", termoAditivoCreationDTO.getDataAntiga().substring(3,5));
                    doc = instance.replaceText(doc, "{anoAntigo}", termoAditivoCreationDTO.getDataAntiga().substring(6,10));
                    doc = instance.replaceText(doc, "{diaNovo}", termoAditivoCreationDTO.getDataNova().substring(0,2));
                    doc = instance.replaceText(doc, "{mesNovo}", termoAditivoCreationDTO.getDataNova().substring(3,5));
                    doc = instance.replaceText(doc, "{anoNovo}", termoAditivoCreationDTO.getDataNova().substring(6,10));
                    doc = instance.replaceText(doc, "{nomeSeguradora}", termoAditivoCreationDTO.getNomeSeguradora());
                    doc = instance.replaceText(doc, "{codSeguradora}", termoAditivoCreationDTO.getCodApolice());
                    doc = instance.replaceText(doc, "{dia}", dmFormat.format(LocalDate.now().getDayOfMonth()));
                    doc = instance.replaceText(doc, "{mes}", dmFormat.format(LocalDate.now().getMonthValue()));
                    doc = instance.replaceText(doc, "{ano}", String.valueOf(LocalDate.now().getYear()));
                    instance.saveDocument(doc, createNomeArquivo(idDiscente,tipoDocumento));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
