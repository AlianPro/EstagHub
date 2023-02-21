package br.com.estaghub;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Poc {

    public static final String OUTPUT_FILE = "src/main/java/br/com/estaghub/docs/7-TERMO-DE-COMPROMISSO-DE-ESTAGIO-NAO-OBRIGATORIO-EXTERNO-ALUNOS-DA-UFRRJ2.doc";

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
    private HWPFDocument openDocument() throws Exception {
        HWPFDocument document = new HWPFDocument(new POIFSFileSystem(
                    new File("src/main/java/br/com/estaghub/docs/7-TERMO-DE-COMPROMISSO-DE-ESTAGIO-NAO-OBRIGATORIO-EXTERNO-ALUNOS-DA-UFRRJ.doc")));
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

    public static void main(String[] args) throws Exception {
        Poc instance = new Poc();
        HWPFDocument doc = instance.openDocument();
        if (doc != null) {
            doc = instance.replaceText(doc, "{nomeDiscente}", "");
//            doc = instance.replaceText(doc, "Curso do Aluno", "dadad");
//            doc = instance.replaceText(doc, "E-mail", "dadad");
            instance.saveDocument(doc, OUTPUT_FILE);
        }
    }
}
