package com.example.demo.alfresco;

/**
 * An example of how to do a join with a CMIS query to select properties
 * defined in an aspect.
 *
 * @author jpotts
 */
public class CmisAspectQuery extends CmisBasicQuery {

    public static void main(String[] args) {
        CmisAspectQuery caqe = new CmisAspectQuery();
        caqe.doExample();
    }

    public void doExample() {
        doQuery("SELECT * FROM cmis:document" +
                     " as D join cm:titled as T on D.cmis:objectId = T.cmis:objectId" +
                     " where D.cmis:name = '13.docx'", 5);
    }
}
