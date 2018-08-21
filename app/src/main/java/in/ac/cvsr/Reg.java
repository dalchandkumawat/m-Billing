package in.ac.cvsr.newapplication;

public class Reg {
    private String phn, loc, nameoforg, pass;

    public Reg() {
    }

    public Reg(String phn, String loc, String nameoforg, String pass) {
        this.phn = phn;
        this.loc = loc;
        this.nameoforg = nameoforg;
        this.pass = pass;
    }

    public String getLoc() {
        return loc;
    }

    public String getNameoforg() {
        return nameoforg;
    }

    public String getPhn() {
        return phn;
    }
    public String getPass(){
        return pass;
    }
}
