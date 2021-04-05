package com.example.demo.alfresco;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.client.runtime.OperationContextImpl;
import org.apache.chemistry.opencmis.commons.data.ContentStream;

import java.io.InputStream;

/**
 * An extremely basic CMIS query example. This is a port of the "Execute a
 * Query" example that ships with the Groovy console in the Workbench plus
 * an OperationContext to show how to limit the number of results returned.
 *
 * @author jpotts
 */
public class CmisBasicQuery extends BaseOnPremExample {
    public static void main(String[] args) {
        CmisBasicQuery sce = new CmisBasicQuery();
        sce.doExample();
    }

    public void doExample() {
//        doQuery("SELECT * FROM cmis:document where cmis:objectId = 'ff822489-1322-4926-81f0-555314818c7f;1.0'", 5);
    }

    public InputStream downloadDocument(String id) {
        Session cmisSession = getCmisSession();
        ContentStream contentStream = cmisSession.getContentStream(new ObjectIdImpl(id));
        System.out.println(contentStream);
        return contentStream.getStream();
    }

    public ItemIterable<QueryResult> getDocuments() {
        Session cmisSession = getCmisSession();
        OperationContext oc = new OperationContextImpl();
        oc.setMaxItemsPerPage(250);
        return cmisSession.query("SELECT * FROM cmis:document", false, oc);
    }

    public void doQuery(String cql, int maxItems) {
        Session cmisSession = getCmisSession();

        OperationContext oc = new OperationContextImpl();
        oc.setMaxItemsPerPage(maxItems);

        ItemIterable<QueryResult> results = cmisSession.query(cql, false, oc);
        cmisSession.getContentStream(new ObjectIdImpl("ff822489-1322-4926-81f0-555314818c7f;1.0")).getStream();
//        ItemIterable<Document> checkedOutDocs = cmisSession.getCheckedOutDocs();
//        System.out.println(checkedOutDocs.getTotalNumItems());
//        for (Document doc: checkedOutDocs) {
//            System.out.println(doc.getContentStream().getStream());
//            System.out.println(doc.getAllVersions());
//        }
//        for (QueryResult result : results) {
//            for (PropertyData<?> prop : result.getProperties()) {
//                System.out.println(prop.getQueryName() + ": " + prop.getFirstValue());
//            }
//            System.out.println("--------------------------------------");
//        }

//        System.out.println("--------------------------------------");
//        System.out.println("Total number: " + results.getTotalNumItems());
//        System.out.println("Has more: " + results.getHasMoreItems());
//        System.out.println("--------------------------------------");
    }

}
