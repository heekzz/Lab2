package freha_tddd13.lab2;

import java.util.ArrayList;

public class Lightness {

    private String name;
    private ArrayList<Color> colorList = new ArrayList<Color>();

    public Lightness(String name, ArrayList<Color> colorList) {
        super();
        this.name = name;
        this.colorList = colorList;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Color> getColorList() {
        return colorList;
    }
    public void setColorList(ArrayList<Color> colorList) {
        this.colorList = colorList;
    };


}