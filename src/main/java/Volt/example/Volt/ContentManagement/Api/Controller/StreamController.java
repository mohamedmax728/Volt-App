package Volt.example.Volt.ContentManagement.Api.Controller;

import Volt.example.Volt.ContentManagement.Application.Interfaces.StreamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stream")
public class StreamController {
    @Autowired
    private StreamService streamService;

    @PostMapping("/createStream")
    public String createStream() {
        return streamService.createStream();

    }
}
