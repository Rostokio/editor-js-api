package com.example.demo.controller;

import com.example.demo.CommentRequest;
import com.example.demo.ItemRequest;
import com.example.demo.alfresco.CmisTest;
import com.example.demo.model.People;
import com.example.demo.model.PeopleList;
import com.example.demo.model.Person;
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
            params = {"id"},
            produces = "application/json")
    public ResponseEntity<byte[]> getDocumentComments(@RequestParam("id") String id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getComments(id)));
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
        cmisTest.comment(request.getVersionSeriesId(), request.getCommentText());
    }

    @PostMapping
    public ResponseEntity<String> addDocument(@RequestParam("doc") MultipartFile file) throws IOException {
        return ResponseEntity.ok(cmisTest.addDocument(file));
    }

    @PutMapping
    public ResponseEntity<String> updateDocument(@RequestParam("doc") MultipartFile file) throws IOException {
        return ResponseEntity.ok(cmisTest.updateDocument(file));
    }

    @PutMapping(path = "comments", params = {"id"})
    public void updateComment(@RequestParam("id") String commentId,
                              @RequestBody CommentRequest comment) throws IOException {
        cmisTest.updateComments(comment.getVersionSeriesId(), commentId, comment.getCommentText());
    }

    @DeleteMapping(params = {"id", "allVersions"})
    public void deleteDocument(@RequestParam("id") String id, @RequestParam("allVersions") Boolean allVersions) {
        cmisTest.deleteDocument(id, allVersions);
    }

    @DeleteMapping(path = "/comments", params = {"objectId", "commentId"})
    public void deleteComment(@RequestParam("objectId") String objectId,
                              @RequestParam("commentId") String commentId) throws IOException {
        cmisTest.deleteComments(objectId, commentId);
    }

    @GetMapping(path = "/processes", produces = "application/json")
    public ResponseEntity<byte[]> getProcesses() throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getProcesses()));
    }

    @GetMapping(path = "/processes/{id}", produces = "application/json")
    public ResponseEntity<byte[]> getProcess(@PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getProcess(id)));
    }

    @GetMapping(path = "/processes/{id}/items", produces = "application/json")
    public ResponseEntity<byte[]> getProcessItems(@PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getProcessItems(id)));
    }

    @PostMapping(path = "/processes/{id}/items", produces = "application/json")
    public ResponseEntity<byte[]> getProcessItems(@PathVariable("id") Long id,
                                                  @RequestBody ItemRequest request) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.createProcessItem(id, request)));
    }

    @DeleteMapping(path = "/processes/{processId}/items/{itemId}")
    public void deleteProcessItem(@PathVariable("processId") Long processId,
                                                  @PathVariable("itemId") Long itemId) throws IOException {
        cmisTest.deleteProcessItem(processId, itemId);
    }

    @GetMapping(path = "/processes/{id}/tasks", produces = "application/json")
    public ResponseEntity<byte[]> getTasksForProcess(@PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getTasks(id)));
    }

    @PutMapping(path = "/tasks/{taskId}",
            params={"state, assignee"},
            produces = "application/json")
    public ResponseEntity<byte[]> updateTasksForProcess(@PathVariable("taskId") Long taskId,
                                                        @RequestParam("state") String state,
                                                        @RequestParam("assignee") String assignee) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.updateTask(taskId, state, assignee)));
    }

    @PutMapping(path = "/tasks/{taskId}",
            params={"state"},
            produces = "application/json")
    public ResponseEntity<byte[]> updateTasksForProcess(@PathVariable("taskId") Long taskId,
                                                        @RequestParam("state") String state) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.updateTask(taskId, state)));
    }

    @PostMapping(path = "/processes", produces = "application/json")
    public ResponseEntity<byte[]> createProcess() throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.createProcess()));
    }

    @DeleteMapping(path = "/processes/{id}")
    public void deleteProcess(@PathVariable("id") Long id) throws IOException {
        cmisTest.deleteProcess(id);
    }


    @GetMapping(path = "/people")
    public ResponseEntity<PeopleList> getPeople() throws IOException {
        return ResponseEntity.ok(cmisTest.getPeople());
    }

    @PostMapping(path = "/people")
    public ResponseEntity<People> createPerson(@RequestBody Person person) throws IOException {
        return ResponseEntity.ok(cmisTest.createPerson(person));
    }

    @GetMapping(path = "/people/{id}")
    public ResponseEntity<People> getPerson(@PathVariable("id") String id) throws IOException {
        return ResponseEntity.ok(cmisTest.getPerson(id));
    }

    @ExceptionHandler(CmisObjectNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Throwable ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found");
    }
}
