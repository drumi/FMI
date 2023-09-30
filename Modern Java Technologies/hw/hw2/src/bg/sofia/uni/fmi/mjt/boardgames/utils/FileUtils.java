package bg.sofia.uni.fmi.mjt.boardgames.utils;

public class FileUtils {

    private FileUtils() {
    }

    public static final String BOM = "\uFEFF";

    public static String stripBOM(String s) {
        return s.startsWith(BOM) ? s.substring(BOM.length()) : s;
    }

}
