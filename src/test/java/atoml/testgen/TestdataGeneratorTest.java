package atoml.testgen;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class TestdataGeneratorTest {

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void generateTestdata() throws IOException {
        TestdataGenerator generator = new TestdataGenerator(TestCatalog.SMOKETESTS, new LinkedList<>(), 10, 100, 1);
        File file = folder.newFolder();
        generator.generateTestdata(file.getAbsolutePath().toString() + "/");
    }
}
