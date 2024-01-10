package com.moddy.server.controller.designer;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designer")
@Tag(name = "DesignerController")
@RequiredArgsConstructor
public class DesignerController {
}
