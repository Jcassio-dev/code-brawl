package dev.jcassio.coderunner.domain;

public interface ExecutionUseCase {
    ExecutionResult executeCode(String code, String lang) ;
}
