package com.example.demo.controller;

import com.example.demo.CommentRequest;
import com.example.demo.ItemRequest;
import com.example.demo.alfresco.CmisTest;
import com.example.demo.model.People;
import com.example.demo.model.PeopleList;
import com.example.demo.model.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api("Alfresco Controller")
public class TestAlfrescoController {

    private final CmisTest cmisTest;

    @GetMapping(params = {"id"},
            produces = "application/msword")
    @ApiOperation("Скачать документ")
    public ResponseEntity<byte[]> getDocument(
            @ApiParam("id документа") @RequestParam("id") String id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.downloadDocument(id)));
    }

    @GetMapping(path = "/properties",
            params = {"id"})
    @ApiOperation("Получить свойства документа")
    public ResponseEntity<List<PropertyData<?>>> getDocumentProperties(
            @ApiParam("id документа") @RequestParam("id") String id) {
        return ResponseEntity.ok(cmisTest.getDocumentProperties(id));
    }

    @GetMapping(path = "/comments",
            params = {"id"},
            produces = "application/json")
    @ApiOperation("Получить комментарии к документу")
    public ResponseEntity<byte[]> getDocumentComments(
            @ApiParam("id документа") @RequestParam("id") String id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getComments(id)));
    }

    @GetMapping
    @ApiOperation("Получить список всех документов")
    public ResponseEntity<List<List<PropertyData<?>>>> getDocuments() {
        List<List<PropertyData<?>>> list = new ArrayList<>();
        for (QueryResult result : cmisTest.getDocuments()) {
            list.add(result.getProperties());
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping(path = "/comments")
    @ApiOperation("Добавить комментарий к документу")
    public void addComment(@RequestBody CommentRequest request) throws IOException {
        cmisTest.comment(request.getId(), request.getCommentText());
    }

    @PostMapping
    @ApiOperation("Загрузить новый документ")
    public ResponseEntity<String> addDocument(
            @ApiParam("Файл") @RequestParam("doc") MultipartFile file) throws IOException {
        return ResponseEntity.ok(cmisTest.addDocument(file));
    }

    @PutMapping
    @ApiOperation("Загрузить новую версию документа")
    public ResponseEntity<String> updateDocument(
            @ApiParam("Файл") @RequestParam("doc") MultipartFile file) throws IOException {
        return ResponseEntity.ok(cmisTest.updateDocument(file));
    }

    @PutMapping(path = "comments", params = {"id"})
    @ApiOperation("Обновить комментарий")
    public void updateComment(@ApiParam("id комментария") @RequestParam("id") String commentId,
                              @RequestBody CommentRequest comment) throws IOException {
        cmisTest.updateComments(comment.getId(), commentId, comment.getCommentText());
    }

    @DeleteMapping(params = {"id", "allVersions"})
    @ApiOperation("Удалить документ")
    public void deleteDocument(
            @ApiParam("id документа") @RequestParam("id") String id,
            @ApiParam("Удаление всех версий") @RequestParam("allVersions") Boolean allVersions) {
        cmisTest.deleteDocument(id, allVersions);
    }

    @DeleteMapping(path = "/comments", params = {"objectId", "commentId"})
    @ApiOperation("Удалить комментарий")
    public void deleteComment(@ApiParam("id документа") @RequestParam("objectId") String objectId,
                              @ApiParam("id комментария") @RequestParam("commentId") String commentId) throws IOException {
        cmisTest.deleteComments(objectId, commentId);
    }

    @GetMapping(path = "/processes", produces = "application/json")
    @ApiOperation("Получить все существующие процессы (workflow)")
    public ResponseEntity<byte[]> getProcesses() throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getProcesses()));
    }

    @GetMapping(path = "/processes/{id}", produces = "application/json")
    @ApiOperation("Получить определенный процесс (workflow)")
    public ResponseEntity<byte[]> getProcess(@ApiParam("workflow id") @PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getProcess(id)));
    }

    @GetMapping(path = "/processes/{id}/items", produces = "application/json")
    @ApiOperation("Получить все документы, прикрепленные к процессу")
    public ResponseEntity<byte[]> getProcessItems(
            @ApiParam("workflow id") @PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getProcessItems(id)));
    }

    @PostMapping(path = "/processes/{id}/items", produces = "application/json")
    @ApiOperation("Прикрепить документ к процессу")
    public ResponseEntity<byte[]> getProcessItems(@ApiParam("workflow id") @PathVariable("id") Long id,
                                                  @RequestBody ItemRequest request) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.createProcessItem(id, request)));
    }

    @DeleteMapping(path = "/processes/{processId}/items/{itemId}")
    @ApiOperation("Отвязать документ от процесса")
    public void deleteProcessItem(@ApiParam("workflow id") @PathVariable("processId") Long processId,
                                  @ApiParam("id документа") @PathVariable("itemId") Long itemId) throws IOException {
        cmisTest.deleteProcessItem(processId, itemId);
    }

    @GetMapping(path = "/processes/{id}/tasks", produces = "application/json")
    @ApiOperation("Получить задания внутри процесса")
    public ResponseEntity<byte[]> getTasksForProcess(@ApiParam("workflow id") @PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.getTasks(id)));
    }

    @PutMapping(path = "/tasks/{taskId}",
            produces = "application/json")
    @ApiOperation("Обновить статус задачи внутри процесса")
    public ResponseEntity<byte[]> updateTasksForProcess(@ApiParam("id задачи") @PathVariable("taskId") Long taskId,
                                                        @ApiParam(value = "Состояние, в которое переводится задача",
                                                        allowableValues = "claimed, " +
                                                                "delegated, " +
                                                                "unclaimed, resolved, completed")
                                                        @RequestParam("state") String state,
                                                        @ApiParam(value = "id человека, принимающего задачу")
                                                            @RequestParam(value = "assignee", required = false)
                                                                    String assignee) throws IOException {
        if (assignee == null) {
            return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.updateTask(taskId, state)));
        }
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.updateTask(taskId, state, assignee)));
    }

    @PostMapping(path = "/processes", produces = "application/json")
    @ApiOperation("Создать новый процесс review (workflow) (захардкожено на имя админа)")
    public ResponseEntity<byte[]> createProcess() throws IOException {
        return ResponseEntity.ok(IOUtils.toByteArray(cmisTest.createProcess()));
    }

    @DeleteMapping(path = "/processes/{id}")
    @ApiOperation("Удалить процесс (workflow)")
    public void deleteProcess(@ApiParam("workflow id") @PathVariable("id") Long id) throws IOException {
        cmisTest.deleteProcess(id);
    }


    @GetMapping(path = "/people")
    @ApiOperation("Получить список пользователей")
    public ResponseEntity<PeopleList> getPeople() throws IOException {
        return ResponseEntity.ok(cmisTest.getPeople());
    }

    @PostMapping(path = "/people")
    @ApiOperation("Создать нового пользователя")
    public ResponseEntity<People> createPerson(@RequestBody Person person) throws IOException {
        return ResponseEntity.ok(cmisTest.createPerson(person));
    }

    @GetMapping(path = "/people/{id}")
    @ApiOperation("Получить определенного пользователя")
    public ResponseEntity<People> getPerson(@ApiParam("id пользователя") @PathVariable("id") String id) throws IOException {
        return ResponseEntity.ok(cmisTest.getPerson(id));
    }

    @ExceptionHandler(CmisObjectNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Throwable ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found");
    }
}
