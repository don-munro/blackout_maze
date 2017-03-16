package blackoutmaze;

/**
 * Copyright 2017 Don Munro don.ian.scott.munro@gmail.com
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */

public class IllegalMoveException extends Exception {

    public IllegalMoveException(String message)
    {
        super(message);
    }

}
