package bg.sofia.uni.fmi.mjt.boardgames.io;

import java.io.Closeable;
import java.util.List;
import java.util.Map;

public interface CSVReader extends Closeable {

    Map<String, String> read();
    List<Map<String, String>> readAll();
    List<String> getColumnNames();

}
