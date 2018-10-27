package springmvc.demo.models.Room;

public class Beds {

    private int couple;

    private int single;

    Beds(int couple, int single) {
        this.couple = couple;
        this.single = single;
    }

    public int getCouple() {
        return couple;
    }

    public void setCouple(int couple) {
        this.couple = couple;
    }

    public int getSingle() {
        return single;
    }

    public void setSingle(int single) {
        this.single = single;
    }
}
