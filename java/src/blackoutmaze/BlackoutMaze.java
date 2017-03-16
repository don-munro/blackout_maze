package blackoutmaze;

/**
 * Copyright 2017 Don Munro don.ian.scott.munro@gmail.com
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.util.*;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;


public class BlackoutMaze {

    private static String DARK_MAZE_BASE_URI = "http://www.epdeveloperchallenge.com/api";
    private static String INIT_URI = "http://www.epdeveloperchallenge.com/api/init";
    private static String MOVE_TO_URI = DARK_MAZE_BASE_URI + "/move?mazeGuid=%s&direction=%s";
    private static String JUMP_TO_URI = DARK_MAZE_BASE_URI + "/jump?mazeGuid=%s&x=%d&y=%d";

    ResteasyClient restClient;
    String mazeGuid;
    MazeCell currentCell;
    Stack<MazeCell> split_points;

    public BlackoutMaze() {
        ResteasyClientBuilder builder = new ResteasyClientBuilder();
        restClient = builder.build();
        split_points = new Stack();
    }

    private MazeCell startMaze() throws IllegalMoveException {

        currentCell = handlePost(INIT_URI);
        mazeGuid = currentCell.getMazeGuid();
        
        return currentCell;
    }
    
    private MazeCell move(String direction) throws IllegalMoveException {

        String url = String.format(MOVE_TO_URI, mazeGuid, direction.toUpperCase());
        System.out.println(String.format("Moving %s to %d, %d", direction, 
                                         currentCell.getX(), currentCell.getY()));
        return handlePost(url);
    }

    private MazeCell jumpTo(MazeCell cell) throws IllegalMoveException {

        String url = String.format(JUMP_TO_URI, mazeGuid, cell.getX(), cell.getY());
        System.out.println(String.format("Jumping to %d, %d", currentCell.getX(),
                           currentCell.getY()));
        return handlePost(url);
    }

    private void validateResponse(Response response) throws IllegalMoveException {
        // @TODO - Dig out the details of the failure and raise an exception with a reasonable
        //         message. There doesn't seem to be a clear way to get this from the response
        //         but given that a 'curl' request will provide the details ... there is hope.
        //         If not, this may be a good excuse to call the restEasy experiment a failure.
        if (response.getStatus() != 200)
        {
            throw new IllegalMoveException("Failed with HTTP Status Code : " + response.getStatus());
        }  
    }
    private MazeCell handlePost(String url)
            throws IllegalMoveException
    {
        WebTarget target = restClient.target(url);
        Response response = target.request().post(Entity.json(null));

        validateResponse(response);
        currentCell = response.readEntity(MazeCell.class);
        response.close();

        return currentCell;
    }

    private void markSplitPoint()
    {
        // A 'splitpoint' is any cell that has more than one direction to
        // follow. The choice of one direction could lead to a deadend
        // and if that happens we want to be able to jump back to the
        // 'splitpoint' and try another direction.  Push the current
        // cell onto a stack, we'll pop it back off to return.
        System.out.println(
                String.format("Split Point %s, %s", currentCell.getX(),
                        currentCell.getY())
        );
        split_points.push(currentCell);
    }

    public void acceptChallenge() {
        long startTime = System.currentTimeMillis();
        String direction;
        List<String> unexploredDirections;
        try {
            startMaze();
            boolean found = false;
            do {
                unexploredDirections = currentCell.getUnexploredDirections();
                if (unexploredDirections.size() > 0) {
                    if (unexploredDirections.size() > 1) {
                        markSplitPoint();
                    }
                    direction = unexploredDirections.get(0);
                    move(direction);
                    found = currentCell.isAtEnd();
                } else {
                    // We've reached a dead end. Pop the last split point off 
                    // the stack and jump back to that spot. The 'jump_to' call
                    // results will be upto date with regards to 'explored' status.
                    MazeCell jumpToCell = split_points.pop();
                    jumpTo(jumpToCell);
                    
                }
            } while (!found);
        } catch (IllegalMoveException exc) {
            System.out.println("Failed to exit maze : " + exc.getMessage());
        }

        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(currentCell.getNote());
    }

    public static void main(String[] args) {
        BlackoutMaze maze = new BlackoutMaze();
        maze.acceptChallenge();
    }

}

