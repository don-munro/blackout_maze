package blackoutmaze;

import java.util.*;

/**
 * Copyright 2017 Don Munro don.ian.scott.munro@gmail.com
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */

public class MazeCell
{
    // Provides an immutable representation of a cell in a maze.  Primary
    // use case sees MazeCell instances created via RestEasy's processing
    // of a REST response with the following format:
    //     {"currentCell": {
    //          "previouslyVisited":false,
    //          "north":"BLOCKED",
    //          "east":"UNEXPLORED",
    //          "south":"BLOCKED",
    //          "west":"BLOCKED",
    //          "mazeGuid":"e62ee51f-1fb6-4b11-b3e2-68920a25aa54",
    //          "atEnd":false,
    //          "note":"Welcome to the Elastic Path blackout maze challenge! Do you have what it takes to find
    //                  the end of the maze? Complete it and follow the given instructions to find out more
    //                  about Elastic Path!",
    //           "y":0,
    //           "x":0}
    //     }
    //
    // To avoid having to define object mappers, we rely on the fact that Jackson will
    // call 'setCurrentCell' and provide the map that really defines the cell.  We'll
    // cheat a bit as well and use that map directly on all 'get' functions. This
    // creates a bit of a facade (hiding the map) but can be revisited later.
    // @TODO - look at defining a mapper or ??? to see how else we can do this.

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

    }

    public void setCurrentCell(Map cellMap) {
        responseMap = cellMap;
    }

    public boolean isPreviouslyVisited() {

        return (boolean)responseMap.get("previouslyVisited");
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
        // @TODO - need a python-like list comprehension approach here to
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

    public boolean isAtEnd() {

        return (boolean)responseMap.get("atEnd");
    }

    public String getNote() {
        return (String)responseMap.get("note");
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getX() {
        return (int)responseMap.get("x");
    }

    public int getY() {
        return (int)responseMap.get("y");
    }
}
