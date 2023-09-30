public class PathUtils {

    public static String getCanonicalPath(String path) {
        String[] nodes = path.split("/");
        String[] directories = new String[nodes.length];
        int directoriesIndex = 0;

        for (String node : nodes) {
            if (node.isBlank() || node.equals(".")) {
                continue;
            }

            if (node.equals("..")) {
                directoriesIndex--;
                continue;
            }

            directories[directoriesIndex] = node;
            directoriesIndex++;
        }

        var sb = new StringBuilder();
        for (int i = 0; i < directoriesIndex; i++) {
            sb.append("/")
              .append(directories[i]);
        }

        return sb.isEmpty() ? "/" : sb.toString();
    }

}