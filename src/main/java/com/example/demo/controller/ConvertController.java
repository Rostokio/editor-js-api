//package com.example.demo.controller;
//
//import org.apache.commons.io.IOUtils;
//import org.docx4j.XmlUtils;
//import org.docx4j.convert.in.xhtml.DivToSdt;
//import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
//import org.docx4j.openpackaging.exceptions.Docx4JException;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@RestController
//public class ConvertController {
//
//    @PostMapping(path = "/convert", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
//    public ResponseEntity<byte[]> getData(@RequestBody String xhtml) throws Docx4JException, IOException {
//
//        String html = xhtml.replaceAll("&nbsp;", "");
//        html = html.replaceAll("&laquo;", "«");
//        html = html.replaceAll("&raquo;", "»");
//        html = html.replaceAll("&ndash;", "");
//        html = html.replaceAll("&bull;", "•");
//        html = html.replaceAll("<br>", "<br></br>");
//        // To docx, with content controls
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
//
//        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
//        XHTMLImporter.setDivHandler(new DivToSdt());
//
//        wordMLPackage.getMainDocumentPart().getContent().addAll(
//                XHTMLImporter.convert(html, null));
//
//        System.out.println(XmlUtils.marshaltoString(wordMLPackage
//                .getMainDocumentPart().getJaxbElement(), true, true));
//
//        File file = new File("export.docx");
//        wordMLPackage.save(file);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, "multipart/form-data")
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file.docx\"")
//                .body(IOUtils.toByteArray(new FileInputStream(file)));
//    }
//}
