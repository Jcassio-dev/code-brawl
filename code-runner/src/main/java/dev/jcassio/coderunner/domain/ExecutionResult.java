package dev.jcassio.coderunner.domain;

public record ExecutionResult(
                String stdout,
                String stderr,
                int exitCode,
                boolean timedOut,
                long execTimeMs) {
}
