import java.io.Serializable;

public enum Animals implements Serializable {
    RAT(0, 6, 6),
    CAT(1, 7, 1),
    DOG(2, 7, 5),
    WOLF(3, 6, 2),
    LEOPARD(4, 6, 4),
    TIGER(5, 8, 0),
    LION(6, 8, 6),
    ELEPHANT(7, 6, 0);

    private int val;
    private int p1X;
    private int p1Y;

    private Animals(int val, int p1X, int p1Y){this.val = val; this.p1X = p1X; this.p1Y = p1Y;}

    public int getRank(){return this.val;}

    public int getP1X(){return this.p1X;}

    public int getP1Y(){return this.p1Y;}
}
