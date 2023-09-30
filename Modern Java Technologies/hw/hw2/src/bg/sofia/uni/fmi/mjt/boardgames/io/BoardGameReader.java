package bg.sofia.uni.fmi.mjt.boardgames.io;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.io.Closeable;
import java.util.List;

public interface BoardGameReader extends Closeable {

    BoardGame read();
    List<BoardGame> readAll();

}
