package dev.jcassio.coderunner.infra;

import dev.jcassio.coderunner.domain.ExecutionResult;
import dev.jcassio.coderunner.domain.ExecutionUseCase;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DockerExecutionService implements ExecutionUseCase {

    private final DockerCommandFactory commandFactory;

    public DockerExecutionService() {
        this.commandFactory = new DockerCommandFactory();
    }

    @Override
    public ExecutionResult executeCode(String code, String language) {

        String[] command;
        try {
            command = commandFactory.buildCommand(language);
        } catch (IllegalArgumentException e) {
            return new ExecutionResult("", e.getMessage(), -1, false, 0);
        }

        long startTime = System.nanoTime();
        long durationMs = 0;

        try {
            ProcessBuilder build = new ProcessBuilder(command);
            Process process = build.start();

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(code);
            }

            CompletableFuture<String> stdoutFuture = readStream(process.getInputStream());
            CompletableFuture<String> stderrFuture = readStream(process.getErrorStream());

            boolean finished = process.waitFor(10, TimeUnit.SECONDS);

            long endTime = System.nanoTime();
            durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

            if (!finished) {
                process.destroyForcibly();
                return new ExecutionResult("", "Erro: Timeout!", -1, true, 10);
            }

            String stdout = stdoutFuture.get(1, TimeUnit.SECONDS);
            String stderr = stderrFuture.get(1, TimeUnit.SECONDS);

            return new ExecutionResult(stdout, stderr, process.exitValue(), false, durationMs);

        } catch (Exception e) {
            long endTime = System.nanoTime();
            durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            return new ExecutionResult("", "Erro: " + e.getMessage(), -1, false, durationMs);
        }
    }

    private CompletableFuture<String> readStream(java.io.InputStream is) {
        return CompletableFuture.supplyAsync(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(Collectors.joining("\n"));
            } catch (java.io.IOException e) {
                return "Erro ao ler stream: " + e.getMessage();
            }
        });
    }
}