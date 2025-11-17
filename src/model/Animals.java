package model;

import java.io.Serializable;

public enum Animals implements Serializable {
    //p1X and p1Y represents the position of the chess on the board, val represents the ranking of the animal
    //r = row# and c=column#
    RAT(0, 6, 6),
    CAT(1, 7, 1),
    DOG(2, 7, 5),
    WOLF(3, 6, 2),
    LEOPARD(4, 6, 4),
    TIGER(5, 8, 0),
    LION(6, 8, 6),
    ELEPHANT(7, 6, 0);

    private int rank;
    private int p1R;
    private int p1C;

    private Animals(int rank, int p1R, int p1C){this.rank = rank; this.p1R = p1R; this.p1C = p1C;}

    public int getRank(){return this.rank;}

    public int getP1R(){return this.p1R;}

    public int getP1C(){return this.p1C;}
}
