
import requests

DIRECTIONS = ('north', 'south', 'east', 'west')


class Stack:
    def __init__(self):
        self.items = []

    def isEmpty(self):
        return self.items == []

    def push(self, item):
        self.items.append(item)

    def pop(self):
        return self.items.pop()

    def peek(self):
        return self.items[len(self.items) - 1]

    def size(self):
        return len(self.items)

EPATH_BASE_URL = "http://www.epdeveloperchallenge.com/api/%s"


class DarkMaze(object):


    def __init__(self):
        self.split_points = Stack()
        self.maze = requests.post(
            "http://www.epdeveloperchallenge.com/api/init").json()
        self.start_cell = self.maze.get('currentCell')
        self.maze_guid = self.start_cell.get('mazeGuid')


    def move_one(self, direction):

        mv_url = "http://www.epdeveloperchallenge.com/api/move?mazeGuid=%s&direction=%s" % \
                 (self.maze_guid, direction.upper())
        try:
            response = requests.post(mv_url).json()
        except Exception as exc:
            print exc

        return response.get('currentCell')

    def jump_to(self, x, y):

        jmp_url = "http://www.epdeveloperchallenge.com/api/jump?mazeGuid=%s&x=%i&y=%i" % \
                 (self.maze_guid, x, y)
        current_url = "http://www.epdeveloperchallenge.com/api/currentCell?%s" % (self.maze_guid)
        try:
            response = requests.post(jmp_url).json()
        except Exception as exc:
            print exc

        return response.get('currentCell')

    def explore2(self):

        start_cell = self.start_cell
        try:
            found = False
            current_cell = start_cell
            while not found:
                unexplored = [dir for dir in DIRECTIONS
                              if current_cell.get(dir) == 'UNEXPLORED']
                if len(unexplored) > 1:
                    print("Split Point %s, %s" % (current_cell['x'],
                                                  current_cell['y']))
                    self.split_points.push((current_cell['x'],
                                            current_cell['y']))
                if not unexplored:
                    # We've reached a dead end. Pop the last split point off
                    # the stack and jump back to that spot. The 'jump_to' call
                    # results will be upto date with regards to 'explored' status.
                    x, y = self.split_points.pop()
                    current_cell = self.jump_to(x, y)
                    print("Jumping back to %s, %s" %(x, y))
                else:
                    # Just take the 1st option ... if it ends up in a dead end
                    # the 'jump-to' above will reset the 'unexplored'
                    direction = unexplored[0]
                    current_cell = self.move_one(direction)
                    print ("Moved %s to %i, %i" % (direction, current_cell['x'],
                                                   current_cell['y']))
                    found = current_cell.get('atEnd')

            return current_cell
        except Exception as exc:
            print exc
            print current_cell


def main():

    maze = DarkMaze()
    node = maze.explore2()
    if node:
        print node['note']
    else:
        print "Ouch - failed to find your way out"
    # directions = ('north', 'south', 'east', 'west')
    #
    # try:
    #     while not current_cell.get('atEnd'):
    #         unexplored = [dir for dir in directions
    #                       if current_cell.get(dir) == 'UNEXPLORED']
    #         if unexplored.count() > 1:
    #             jump_point = current_cell
    #
    #         for direction in unexplored:
    #             print direction
    #             current_cell = move_one(maze_guid, direction)
    # except Exception as exc:
    #     exc = exc
    #
    #     print current_cell

if __name__ == '__main__':
    main()