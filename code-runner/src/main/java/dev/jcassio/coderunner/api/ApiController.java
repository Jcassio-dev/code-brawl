package dev.jcassio.coderunner.api;

import dev.jcassio.coderunner.domain.ExecutionUseCase;
import dev.jcassio.coderunner.domain.ExecutionResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ApiController {

    private final ExecutionUseCase executionUseCase;

    public ApiController(ExecutionUseCase executionUseCase) {
        this.executionUseCase = executionUseCase;
    }

    public record SubmissionRequest(String code, String language) {
    }

    public record Problem(int id, String title, String description, String boilerplate) {
    }

    @GetMapping("/api/problem")
    public Problem getProblem() {
        String boilerplate = "function soma(a, b) {\n // seu código aqui\n}";
        return new Problem(
                1,
                "Soma Simples",
                "Crie uma função que some dois números.",
                boilerplate);
    }

    @PostMapping("/api/submit")
    public ExecutionResult handleSubmit(@RequestBody SubmissionRequest request) {
        return executionUseCase.executeCode(request.code(), request.language());
    }
}