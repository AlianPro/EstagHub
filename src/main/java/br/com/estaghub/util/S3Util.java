package br.com.estaghub.util;

import br.com.estaghub.enums.TipoDocumento;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class S3Util {
    public static String OUTPUT_FILE = "/home/alianpro/Documents/Projetos/estaghub/src/main/java/br/com/estaghub/docs/pedidos";


    public static void uploadFileS3(String accessKey, String secretKey, String bucketName, String nameFile) {
        try {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3client = new AmazonS3Client(credentials);
            File file = new File(OUTPUT_FILE+File.separator+nameFile);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());
            PutObjectRequest request = new PutObjectRequest(bucketName, nameFile,fileInputStream,metadata);
            s3client.putObject(request);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getFileS3(String accessKey, String secretKey, String bucketName, String nameFile) {
        try {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3client = new AmazonS3Client(credentials);
            return s3client.getObject(bucketName, nameFile).getObjectContent().getHttpRequest().getURI().toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static List<String> getTceOrTermoAditivoFileS3(String accessKey, String secretKey, String bucketName, TipoDocumento tipoDocumento) {
        String nameFile = "";
        List<String> files = new ArrayList<>();
        if ("TCE".equals(tipoDocumento.name())){
            nameFile = "7-TERMO-DE-COMPROMISSO-DE-ESTAGIO-NAO-OBRIGATORIO-EXTERNO-ALUNOS-DA-UFRRJ.doc";
        } else if ("TERMO_ADITIVO".equals(tipoDocumento.name())) {
            nameFile = "MODELO-TERMO-ADITIVO-5.doc";
        }
        try {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3client = new AmazonS3Client(credentials);
            files.add(nameFile);
            files.add(s3client.getObject(bucketName, nameFile).getObjectContent().getHttpRequest().getURI().toString());
            return files;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public static void deleteFileS3(String accessKey, String secretKey, String bucketName, String nameFile) {
        try {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3client = new AmazonS3Client(credentials);
            s3client.deleteObject(bucketName, nameFile);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
