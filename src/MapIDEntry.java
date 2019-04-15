import java.util.List;

public class MapIDEntry {

    public List<Integer> rgbList;
    public String blockName;
    public String blockID;

    public MapIDEntry(List<Integer> rgbList, String blockName, String blockID){
        this.rgbList = rgbList;
        this.blockName = blockName;
        this.blockID = blockID;
    }
}
