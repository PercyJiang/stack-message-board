package com.msg.api;

import static com.msg.Constants.API_BASE_PATH;

import com.msg.MessageService;
import com.msg.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = API_BASE_PATH)
@CrossOrigin
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Message>> getAll() {
        List<Message> response = messageService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(
            value = "/post",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Integer> create(@RequestBody Message message) {
        int response = messageService.create(message.getSource(), message.getContent());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/put/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody Message message) {
        int result = messageService.update(id, message.getContent());
        return result == 1 ? new ResponseEntity<>(HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        int result = messageService.delete(id);
        return result == 1 ? new ResponseEntity<>(HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
