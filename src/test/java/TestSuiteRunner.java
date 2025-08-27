import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class TestSuiteRunner {

    private static int passed = 0;
    private static int failed = 0;
    private static int aborted = 0;

    // Map class names to file paths
    private static Map<String, String> classToFileMap = new HashMap<>();
    private static Set<String> failedTestFiles = new HashSet<>();

    public static void main(String[] args) throws Exception {
        SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
        Launcher launcher = LauncherFactory.create();

        // Discover all test classes
        try (Stream<Path> paths = Files.walk(Paths.get("src/test/java"))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith("Test.java") || p.getFileName().toString().startsWith("Test"))
                    .forEach(path -> {
                        Path basePath = Paths.get("src/test/java");
                        String className = basePath.relativize(path)
                                .toString()
                                .replace(".java", "")
                                .replace(File.separatorChar, '.');

                        classToFileMap.put(className, path.toString());
                    });
        }

        // Listener to track test results
        TestExecutionListener listener = new TestExecutionListener() {
            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult result) {
                if (testIdentifier.isTest()) {
                    String displayName = testIdentifier.getDisplayName();
                    String className = testIdentifier.getParentId()
                            .flatMap(parentId -> Optional.ofNullable(testIdentifier.getSource()
                                    .filter(source -> source instanceof org.junit.platform.engine.support.descriptor.ClassSource)
                                    .map(source -> ((org.junit.platform.engine.support.descriptor.ClassSource) source).getClassName())
                                    .orElse(null)))
                            .orElse("UnknownClass");

                    switch (result.getStatus()) {
                        case SUCCESSFUL -> {
                            passed++;
                            System.out.println("✅ PASSED: " + displayName);
                        }
                        case FAILED -> {
                            failed++;
                            System.out.println("❌ FAILED: " + displayName + " - " +
                                    result.getThrowable().map(Throwable::getMessage).orElse("No error message"));
                            failedTestFiles.add(classToFileMap.getOrDefault(className, "UnknownClass"));
                        }
                        case ABORTED -> {
                            aborted++;
                            System.out.println("⚠️ ABORTED: " + displayName);
                        }
                    }
                }
            }
        };

        launcher.registerTestExecutionListeners(summaryListener, listener);

        // Execute each test class sequentially
        for (String className : classToFileMap.keySet()) {
            System.out.println("\n▶️ Running test class: " + className);
            LauncherDiscoveryRequest classRequest = LauncherDiscoveryRequestBuilder.request()
                    .selectors(DiscoverySelectors.selectClass(className))
                    .build();
            launcher.execute(classRequest);
        }

        // Summary
        var summary = summaryListener.getSummary();
        System.out.println("\n📊 Test Summary:");
        summary.printTo(new PrintWriter(System.out));

        System.out.println("\n📌 Totals:");
        System.out.println("   ✅ Passed : " + passed);
        System.out.println("   ❌ Failed : " + failed);
        System.out.println("   ⚠️ Aborted: " + aborted);
        System.out.println("   🧪 Total  : " + (passed + failed + aborted));

        if (!failedTestFiles.isEmpty()) {
            System.out.println("\n❌ Failed Test Files:");
            failedTestFiles.forEach(System.out::println);
        }
    }
}
