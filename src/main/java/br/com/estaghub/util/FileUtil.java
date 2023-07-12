package br.com.estaghub.util;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.embeddable.PlanoAtividades;
import br.com.estaghub.domain.embeddable.TCE;
import br.com.estaghub.domain.embeddable.TermoAditivo;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.enums.TipoPedido;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

public class FileUtil {
    public static String PLANO_ATIVIDADE = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/PLANO-DE-ATIVIDADES-DE-ESTÁGIO.doc";
    public static String TCE = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/7-TERMO-DE-COMPROMISSO-DE-ESTAGIO-NAO-OBRIGATORIO-EXTERNO-ALUNOS-DA-UFRRJ.doc";
    public static String TERMO_ADITIVO = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/MODELO-TERMO-ADITIVO-5.doc";

    private static void replaceText(HWPFDocument doc, String findText, String replaceText) {
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
    }
    private static HWPFDocument openDocument(String doc) throws Exception {
        return new HWPFDocument(new POIFSFileSystem(new File(doc)));
    }

    private static void saveDocument(HWPFDocument doc, String file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            doc.write(out);
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String createNomeArquivo(String idDiscente, String idPedido, TipoDocumento tipoDocumento) {
        if (tipoDocumento == TipoDocumento.PLANO_ATIVIDADES){
           return String.format("/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos/%s-%s-%s-PLANO-DE-ATIVIDADES-DE-ESTÁGIO.doc",idDiscente,idPedido,tipoDocumento.name());
        }else if (tipoDocumento == TipoDocumento.TCE) {
            return String.format("/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos/%s-%s-%s-7-TERMO-DE-COMPROMISSO-DE-ESTAGIO-NAO-OBRIGATORIO-EXTERNO-ALUNOS-DA-UFRRJ.doc",idDiscente,idPedido,tipoDocumento.name());
        }else if (tipoDocumento == TipoDocumento.TERMO_ADITIVO) {
            return String.format("/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos/%s-%s-%s-MODELO-TERMO-ADITIVO-5.doc",idDiscente,idPedido,tipoDocumento.name());
        }
        return "";
    }
    public static void gerarPlanoAtividades(HttpServletRequest req, String idDiscente, String idPedido, TipoDocumento tipoDocumento, PlanoAtividades planoAtividades, Discente discente, Pedido pedido){
        try{
            DecimalFormat dmFormat = new DecimalFormat("00");
            if (tipoDocumento == TipoDocumento.PLANO_ATIVIDADES){
                HWPFDocument doc = FileUtil.openDocument(PLANO_ATIVIDADE);
                FileUtil.replaceText(doc, "{primeiraAtividade}", Objects.nonNull(planoAtividades.getPrimeiraAtividade())? planoAtividades.getPrimeiraAtividade() : "");
                FileUtil.replaceText(doc, "{segundaAtividade}", Objects.nonNull(planoAtividades.getSegundaAtividade())? planoAtividades.getSegundaAtividade() : "");
                FileUtil.replaceText(doc, "{terceiraAtividade}", Objects.nonNull(planoAtividades.getTerceiraAtividade())? planoAtividades.getTerceiraAtividade() : "");
                FileUtil.replaceText(doc, "{quartaAtividade}", Objects.nonNull(planoAtividades.getQuartaAtividade())? planoAtividades.getQuartaAtividade() : "");
                FileUtil.replaceText(doc, "{quintaAtividade}", Objects.nonNull(planoAtividades.getQuintaAtividade())? planoAtividades.getQuintaAtividade() : "");
                FileUtil.replaceText(doc, "{nomeDiscente}", discente.getNome());
                FileUtil.replaceText(doc, "{matriculaDiscente}", discente.getMatricula());
                FileUtil.replaceText(doc, "{cursoDiscente}", discente.getCurso().getNome());
                FileUtil.replaceText(doc, "{periodoCurso}", discente.getPeriodo());
                FileUtil.replaceText(doc, "{cpfDiscente}", CryptUtil.decryptInfo(req,discente.getCpf()));
                FileUtil.replaceText(doc, "{emailDiscente}", discente.getEmail());
                FileUtil.replaceText(doc, "{nomeEmpresa}", planoAtividades.getNomeEmpresa());
                FileUtil.replaceText(doc, "{nomeResponsavelEmpresa}", planoAtividades.getResponsavelEmpresa());
                FileUtil.replaceText(doc, "{enderecoEmpresa/telefoneEmpresa/emailEmpresa}", planoAtividades.getEnderecoEmpresa()+" / "+planoAtividades.getTelefoneEmpresa()+" / "+planoAtividades.getEmailEmpresa());
                FileUtil.replaceText(doc, "{nomeSupervisor}", planoAtividades.getNomeSupervisor());
                FileUtil.replaceText(doc, "{formacaoSupervisor}", planoAtividades.getFormacaoSupervisor());
                FileUtil.replaceText(doc, "{nomeOrientador}", pedido.getDocenteOrientador().getNome());
                FileUtil.replaceText(doc, "{matriculaOrientador}", pedido.getDocenteOrientador().getSiape());
                FileUtil.replaceText(doc, "{dia}", dmFormat.format(LocalDate.now().getDayOfMonth()));
                FileUtil.replaceText(doc, "{mes}", dmFormat.format(LocalDate.now().getMonthValue()));
                FileUtil.replaceText(doc, "{ano}", String.valueOf(LocalDate.now().getYear()).substring(2,4));
                FileUtil.saveDocument(doc, createNomeArquivo(idDiscente,idPedido,tipoDocumento));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void gerarTCE(HttpServletRequest req, String idDiscente, String idPedido, TipoDocumento tipoDocumento, TCE tce, Discente discente){
        try{
            Pedido pedido = new Pedido();
            Documento documento = new Documento();
            DecimalFormat dmFormat = new DecimalFormat("00");
            if (tipoDocumento == TipoDocumento.TCE){
                HWPFDocument doc = FileUtil.openDocument(TCE);
                FileUtil.replaceText(doc, "{nomeDiscente}", discente.getNome());
                FileUtil.replaceText(doc, "{nomeConcedente}", documento.getDocumentoByIdPedidoAndTipoDocumento(pedido.getPedidoByDiscente(discente.getDiscenteById(Long.parseLong(idDiscente)).get(), TipoPedido.NOVO).get().getId(),TipoDocumento.PLANO_ATIVIDADES).get().getPlanoAtividades().getNomeEmpresa());
                FileUtil.replaceText(doc, "{cnpjConcedente}", tce.getCnpjEmpresa());
                FileUtil.replaceText(doc, "{periodoCurso}", discente.getPeriodo());
                FileUtil.replaceText(doc, "{nomeCurso}", discente.getCurso().getNome());
                FileUtil.replaceText(doc, "{matriculaDiscente}", discente.getMatricula());
                FileUtil.replaceText(doc, "{rgDiscente}", CryptUtil.decryptInfo(req,discente.getRg()));
                FileUtil.replaceText(doc, "{orgaoExpedidor}", CryptUtil.decryptInfo(req,discente.getOrgaoExpedidorRg()));
                FileUtil.replaceText(doc, "{cpfDiscente}", CryptUtil.decryptInfo(req,discente.getCpf()));
                FileUtil.replaceText(doc, "{enderecoDiscente}", CryptUtil.decryptInfo(req,discente.getEndereco()));
                FileUtil.replaceText(doc, "{horarioInicio}", tce.getHorarioInicio());
                FileUtil.replaceText(doc, "{horarioFim}", tce.getHorarioFim());
                FileUtil.replaceText(doc, "{intervalo}", tce.getIntervalo());
                FileUtil.replaceText(doc, "{totalHoras}", tce.getTotalHoras());
                FileUtil.replaceText(doc, "{diaInicio}", tce.getDataInicio().substring(0,2));
                FileUtil.replaceText(doc, "{mesInicio}", tce.getDataInicio().substring(3,5));
                FileUtil.replaceText(doc, "{anoInicio}", tce.getDataInicio().substring(6,10));
                FileUtil.replaceText(doc, "{diaFim}", tce.getDataFim().substring(0,2));
                FileUtil.replaceText(doc, "{mesFim}", tce.getDataFim().substring(3,5));
                FileUtil.replaceText(doc, "{anoFim}", tce.getDataFim().substring(6,10));
                FileUtil.replaceText(doc, "{bolsa}", tce.getBolsa());
                FileUtil.replaceText(doc, "{auxTransporte}", tce.getAuxTransporte());
                FileUtil.replaceText(doc, "{codApolice}", tce.getCodApolice());
                FileUtil.replaceText(doc, "{nomeSeguradora}", tce.getNomeSeguradora());
                FileUtil.replaceText(doc, "{dia}", dmFormat.format(LocalDate.now().getDayOfMonth()));
                FileUtil.replaceText(doc, "{mes}", dmFormat.format(LocalDate.now().getMonthValue()));
                FileUtil.replaceText(doc, "{ano}", String.valueOf(LocalDate.now().getYear()));
                FileUtil.saveDocument(doc, createNomeArquivo(idDiscente,idPedido,tipoDocumento));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void gerarTermoAditivo(String idDiscente, String idPedido, TipoDocumento tipoDocumento, TermoAditivo termoAditivo, Discente discente, Pedido pedido){
        try{
            DecimalFormat dmFormat = new DecimalFormat("00");
            if (tipoDocumento == TipoDocumento.TERMO_ADITIVO){
                HWPFDocument doc = FileUtil.openDocument(TERMO_ADITIVO);
                FileUtil.replaceText(doc, "{nomeEmpresa}", pedido.getSupervisor().getEmpresa().getNome());
                FileUtil.replaceText(doc, "{nomeDiscente}", discente.getNome());
                FileUtil.replaceText(doc, "{periodoCurso}", discente.getPeriodo());
                FileUtil.replaceText(doc, "{nomeCurso}", discente.getCurso().getNome());
                FileUtil.replaceText(doc, "{diaAntigo}", termoAditivo.getDataAntiga().substring(0,2));
                FileUtil.replaceText(doc, "{mesAntigo}", termoAditivo.getDataAntiga().substring(3,5));
                FileUtil.replaceText(doc, "{anoAntigo}", termoAditivo.getDataAntiga().substring(6,10));
                FileUtil.replaceText(doc, "{diaNovo}", termoAditivo.getDataNova().substring(0,2));
                FileUtil.replaceText(doc, "{mesNovo}", termoAditivo.getDataNova().substring(3,5));
                FileUtil.replaceText(doc, "{anoNovo}", termoAditivo.getDataNova().substring(6,10));
                FileUtil.replaceText(doc, "{nomeSeguradora}", termoAditivo.getNomeSeguradora());
                FileUtil.replaceText(doc, "{codSeguradora}", termoAditivo.getCodApolice());
                FileUtil.replaceText(doc, "{dia}", dmFormat.format(LocalDate.now().getDayOfMonth()));
                FileUtil.replaceText(doc, "{mes}", dmFormat.format(LocalDate.now().getMonthValue()));
                FileUtil.replaceText(doc, "{ano}", String.valueOf(LocalDate.now().getYear()));
                FileUtil.saveDocument(doc, createNomeArquivo(idDiscente,idPedido,tipoDocumento));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
