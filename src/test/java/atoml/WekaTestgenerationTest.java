package atoml;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

public class WekaTestgenerationTest {

	@Test
	public void test() throws IOException {
		// clear folder testres/scikittests
		File outdir = new File("testres/weka");
		if( outdir.exists() ) {
			deleteFileOrFolder(outdir.toPath());
		}
		outdir.mkdirs();
		String[] args = "-f testdata/wekaclassifiers.txt -g -i 5 --testpath testres/weka/src/test/java/ --resourcepath testres/weka/src/test/resources/".split(" ");
		Runner.main(args);
	}
	
	private static void deleteFileOrFolder(final Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(final Path file, final IOException e) {
				return handleException(e);
			}

			private FileVisitResult handleException(final IOException e) {
				e.printStackTrace(); // replace with more robust error handling
				return FileVisitResult.TERMINATE;
			}

			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException e) throws IOException {
				if (e != null)
					return handleException(e);
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	};
}
