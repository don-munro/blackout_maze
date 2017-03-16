package blackoutmaze;

import java.util.*;

/**
 * Created by donmunro on 2017-03-15.
 */
public class MazeCell
{
    // ElasticPath API requires the use of upper case
    private final String NORTH = "NORTH";
    private final String SOUTH = "SOUTH";
    private final String EAST = "EAST";
    private final String WEST = "WEST";

    boolean previouslyVisited;
    String north;
    String east;
    String south;
    String west;
    String  mazeGuid;
    boolean atEnd;
    String note;
    Map responseMap;

    public MazeCell()
    {
        int huh = 0; // debug line
    }



    // {"MazeCell": {
    //      "previouslyVisited":false,
    //      "north":"BLOCKED",
    //      "east":"UNEXPLORED",
    //      "south":"BLOCKED",
    //      "west":"BLOCKED",
    //      "mazeGuid":"e62ee51f-1fb6-4b11-b3e2-68920a25aa54",
    //      "atEnd":false,
    //      "note":"Welcome to the Elastic Path blackout maze challenge! Do you have what it takes to find the end of the maze? Complete it and follow the given instructions to find out more about Elastic Path!","y":0,"x":0}}

    public void setCurrentCell(Map MazeCell)
    {
        responseMap = MazeCell;
    }

    public Map getCurrentCell()
    {
        return responseMap;
    }

    public boolean isPreviouslyVisited() {
        return (boolean)responseMap.get("previouslyVisited");
    }

    public void setPreviouslyVisited(boolean previouslyVisited) {
        // do nothing
    }

    public String getNorth() {
        return (String)responseMap.get("north");
    }

    public String getEast() {
        return (String)responseMap.get("east");
    }

    public String getSouth() {
        return (String)responseMap.get("south");
    }

    public String getWest() {
        return(String)responseMap.get("west");
    }

    public List<String> getUnexploredDirections()
    {
        // @TODO - need a pythin-like list comprehension approach here to
        //         make this a more efficient operation.
        ArrayList unexplored = new ArrayList<String>();
        if (getNorth().equals("UNEXPLORED"))
            unexplored.add(NORTH);
        if (getEast().equals("UNEXPLORED"))
            unexplored.add(EAST);
        if (getSouth().equals("UNEXPLORED"))
            unexplored.add(SOUTH);
        if (getWest().equals("UNEXPLORED"))
            unexplored.add(WEST);

        return unexplored;
    }

    public String getMazeGuid() {
        return (String)responseMap.get("mazeGuid");
    }

    public void setMazeGuid(String mazeGuid) {
        this.mazeGuid = mazeGuid;
    }

    public boolean isAtEnd() {
        return (boolean)responseMap.get("atEnd");
    }

    public void setAtEnd(boolean atEnd) {
        this.atEnd = atEnd;
    }

    public String getNote() {
        return (String)responseMap.get("note");
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getX()
    {
        return (int)responseMap.get("x");
    }

    public int getY()
    {
        return (int)responseMap.get("y");
    }
}
