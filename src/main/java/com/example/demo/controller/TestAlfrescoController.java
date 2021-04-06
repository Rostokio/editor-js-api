package com.example.demo.controller;

import com.example.demo.CommentRequest;
import com.example.demo.alfresco.CmisTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.tika.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alfresco")
@Slf4j
public class TestAlfrescoController {

    private final CmisTest cmisTest;

    @GetMapping(params = {"id"},
            produces = "application/msword")
    public ResponseEntity<byte[]> getDocument(@RequestParam("id") String id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.downloadDocument(id)));
    }

    @GetMapping(path = "/properties",
            params = {"id"})
    public ResponseEntity<List<PropertyData<?>>> getDocumentProperties(@RequestParam("id") String id) throws IOException {
        return ResponseEntity.ok(cmisTest.getDocumentProperties(id));
    }

    @GetMapping(path = "/comments",
            params = {"id"})
    public ResponseEntity<byte[]> getDocumentComments(@RequestParam("id") String id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getComment(id)));
    }

    @GetMapping
    public ResponseEntity<List<List<PropertyData<?>>>> getDocuments() {
        List<List<PropertyData<?>>> list = new ArrayList<>();
        for (QueryResult result : cmisTest.getDocuments()) {
            list.add(result.getProperties());
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping(path = "/comments")
    public void addComment(@RequestBody CommentRequest request) throws IOException {
        cmisTest.addComment(request);
    }

    @PostMapping
    public ResponseEntity<String> addDocument(@RequestParam("doc") MultipartFile file) throws IOException {
        return ResponseEntity.ok(cmisTest.addDocument(file));
    }

    @PutMapping
    public ResponseEntity<String> updateDocument(@RequestParam("doc") MultipartFile file) throws IOException {
        return ResponseEntity.ok(cmisTest.updateDocument(file));
    }

    @PutMapping(path = "comments/{id}")
    public void updateComment(@PathVariable("id") String commentId,
                              @RequestBody CommentRequest comment) throws IOException {
        cmisTest.updateComment(comment.getVersionSeriesId(), commentId, comment.getCommentText());
    }

    @DeleteMapping(params = {"id", "allVersions"})
    public void deleteDocument(@RequestParam("id") String id, @RequestParam("allVersions") Boolean allVersions) {
        cmisTest.deleteDocument(id, allVersions);
    }

    @DeleteMapping(path = "/comments", params = {"objectId", "commentId"})
    public void deleteComment(@RequestParam("objectId") String objectId,
                              @RequestParam("commentId") String commentId) throws IOException {
        cmisTest.deleteComment(objectId, commentId);
    }

    @GetMapping(path = "/processes")
    public ResponseEntity<byte[]> getProcesses() throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getProcesses()));
    }

    @PostMapping(path = "/processes")
    public ResponseEntity<byte[]> createProcess() throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getProcesses()));
    }

    @ExceptionHandler(CmisObjectNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Throwable ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found");
    }
}
