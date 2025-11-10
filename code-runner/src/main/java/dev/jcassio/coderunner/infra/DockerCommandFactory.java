package dev.jcassio.coderunner.infra;


import java.util.HashMap;
import java.util.Map;

public class DockerCommandFactory {

    private record LanguageConfig(String image, String... command) {}

    private static final Map<String, LanguageConfig> LANGUAGE_MAP;

    static {
        LANGUAGE_MAP = new HashMap<>();

        LANGUAGE_MAP.put("js", new LanguageConfig(
                "node:18-alpine",
                "node"
        ));

        LANGUAGE_MAP.put("javascript", LANGUAGE_MAP.get("js")); // Alias

        LANGUAGE_MAP.put("py", new LanguageConfig(
                "python:3.11-alpine",
                "python"
        ));

        LANGUAGE_MAP.put("python", LANGUAGE_MAP.get("py")); // Alias

        LANGUAGE_MAP.put("c", new LanguageConfig(
                "alpine:latest",
                "sh", "-c", "apk add --no-cache gcc musl-dev && cat > main.c && gcc -O2 -static -s main.c -o a.out && ./a.out"
        ));

        LANGUAGE_MAP.put("java", new LanguageConfig(
                "eclipse-temurin:21-jdk-alpine",
                "sh", "-c", "cat > Main.java && javac Main.java && java Main"
        ));

        LANGUAGE_MAP.put("rb", new LanguageConfig(
                "ruby:3.2-alpine",
                "ruby"
        ));
        LANGUAGE_MAP.put("ruby", LANGUAGE_MAP.get("rb"));
    }
    public String [] buildCommand(String language) {
        String [] baseCommand = {
                "docker", "run", "--rm",
                "-i",
                "--network=none",
                "-m=100m",
                "--cpus=0.5"
        };

        LanguageConfig config = LANGUAGE_MAP.get(language.toLowerCase());

        if(config == null) {
            throw new IllegalArgumentException(String.format("Language '%s' is not supported", language));
        }

        String[] imageArg = new String[] {config.image()};

        String[] commandArgs = config.command();

        return concatArrays(baseCommand, concatArrays(imageArg, commandArgs));
    }

    private String[] concatArrays(String[] a, String[] b) {
        String[] result = new String[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
