package com.example.demo.alfresco;

import com.example.demo.CommentRequest;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.client.runtime.OperationContextImpl;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CmisTest extends BaseOnPremExample {

    public List<PropertyData<?>> getDocumentProperties(String id) {
        Session cmisSession = getCmisSession();
        OperationContext oc = new OperationContextImpl();
        oc.setMaxItemsPerPage(250);
        return cmisSession.query("SELECT * FROM cmis:document where cmis:objectId = '" + id + "'",
                false,
                oc).iterator().next().getProperties();
    }

    public ItemIterable<QueryResult> getDocuments() {
        Session cmisSession = getCmisSession();
        OperationContext oc = new OperationContextImpl();
        oc.setMaxItemsPerPage(250);
        return cmisSession.query("SELECT * FROM cmis:document", false, oc);
    }

    public InputStream downloadDocument(String id) {
        Session cmisSession = getCmisSession();
        ContentStream contentStream = cmisSession.getContentStream(new ObjectIdImpl(id));
        System.out.println(contentStream);
        return contentStream.getStream();
    }


    public String addDocument(MultipartFile file) throws IOException {
        Session cmisSession = getCmisSession();
        Path filepath = Paths.get("src/main/resources", file.getOriginalFilename());
        file.transferTo(filepath);
        Document document = createDocument(cmisSession.getRootFolder(), filepath.toFile(), file.getContentType());
        return document.getId();
    }

    public String updateDocument(MultipartFile file) throws IOException {
        Session cmisSession = getCmisSession();
        String name = file.getOriginalFilename();
        OperationContext oc = new OperationContextImpl();
        oc.setMaxItemsPerPage(250);

        QueryResult qr = cmisSession.query(
                "SELECT cmis:objectId FROM cmis:document where cmis:name = '" + name + "'",
                false,
                oc).iterator().next();
        String originalId = qr.getPropertyByQueryName("cmis:objectId").getFirstValue().toString();
        Document originalDoc = (Document) cmisSession.getObject(originalId);

        ObjectId documentCopyId = originalDoc.checkOut();
        Document updatedDocument = (Document) cmisSession.getObject(documentCopyId);

        ContentStream contentStream =
                cmisSession.getObjectFactory()
                        .createContentStream(name, file.getSize(), file.getContentType(), file.getInputStream());

        ObjectId updatedId = updatedDocument.checkIn(false, null, contentStream, "just a minor change");
        return "ObjectId = " + updatedId.getId();
    }

    public void deleteDocument(String objectId, Boolean allVersions) {
        Session cmisSession = getCmisSession();
        cmisSession.delete(new ObjectIdImpl(objectId), allVersions);
    }

    public void addComment(CommentRequest request) throws IOException {
        comment(request.getVersionSeriesId(), request.getCommentText());
    }

    public InputStream getComment(String id) throws IOException {
        return getComments(id);
    }

    public void updateComment(String objectId, String commentId, String content) throws IOException {
        updateComments(objectId, commentId, content);
    }

    public void deleteComment(String objectId, String commentId) throws IOException {
        deleteComments(objectId, commentId);
    }

    public InputStream getProcesses() throws IOException {
        return getProcess();
    }
}
