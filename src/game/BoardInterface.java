package game;

public interface BoardInterface {
    public int getBoardValue(int i, int j);

    public void setBoardValue(int i, int j, int value);

    public void handleClick(int i, int j) throws InterruptedException;
}
