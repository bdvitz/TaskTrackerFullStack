import java.io.File;

public class DirectoryTreePrinter {

    public static void main(String[] args) {
        File currentDir = new File("."); // current directory
        printDirectoryTree(currentDir, 0);
    }

    public static void printDirectoryTree(File folder, int indent) {
        if (!folder.isDirectory()) return;

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            for (int i = 0; i < indent; i++) {
                System.out.print("    "); // 4 spaces per indent level
            }

            if (file.isDirectory()) {
                System.out.println("📁 " + file.getName());
                printDirectoryTree(file, indent + 1); // recurse into subdirectory
            } else {
                System.out.println("📄 " + file.getName());
            }
        }
    }
}
