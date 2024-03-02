package hexaround.game;

import java.util.*;

import hexaround.config.CreatureDefinition;
import hexaround.structures.*;

public class gameBoard {
    private Collection<Hex> hexBoard = new ArrayList<>();

    public gameBoard(){}//used to just make an empty in the constructor so not null

    public LinkedList<CreatureName> reds = new LinkedList<>();
    public LinkedList<CreatureName> blues = new LinkedList<>();



    public void placePiece(PlayerName turn,CreatureName creature,Hex coord){
        hexBoard.add(new Hex(turn,creature,coord));//specific addition for the placing creature
    }
    public void placePiece(CreatureName creature,Hex coord) {
        Hex hex = new Hex(PlayerName.BLUE,creature,coord);//used only in the cases of the first thing placed which would be a blue
        hexBoard.add(hex);
    }

    public void addBoard(Hex hex){
        hexBoard.add(hex);//making place without creature
    }


    public boolean isOccupied(int x, int y){
        Hex checkCoord = new Hex(x,y);
        for(Hex coord: this.hexBoard)//checks if the distance between what is being checked
            if(coord.getDistance(checkCoord)==0&&getHex(x,y).getCreature()!=null)
                return true;
        return false;
    }

    public Hex getHex(int x, int y){
        Hex spot = new Hex(x,y);
        for(Hex coords: this.hexBoard){//returns specific spot if distance checked is zero
            if (coords.getDistance(spot)==0)
                return coords;
        }
        return null;
    }

    public boolean reachable(Hex from, Hex to, int maxDist){
        int dist = from.getDistance(to);//used for the can reach in gameManager
        return maxDist>=dist;
    }

    /**
     * takes in the list of properties of one creature and determines if a desired property is among them
     * @param def
     * @param prop
     * @return
     */
    public boolean propertyExist(CreatureDefinition def, CreatureProperty prop){
        for(CreatureProperty p: def.properties()){
            if(p.toString().equals(prop.toString()))
                return true;
        }
        return false;
    }

    /**
     * updates the location attributes of the hex and leaves behind a new hex with a null creature
     * @param fromx
     * @param fromy
     * @param tox
     * @param toy
     */
    public void updateLocation(int fromx, int fromy, int tox, int toy){
        Hex currentSpot = new Hex(fromx,fromy);
        for(Hex coord: hexBoard){
            if(coord.getDistance(currentSpot)==0){
                coord.changeLoc(tox,toy);
            }
        }
        addBoard(currentSpot);
    }

    public void setReds(LinkedList<CreatureName> reds) {
        this.reds = reds;//the list of red creatures currently on the board
    }

    public void setBlues(LinkedList<CreatureName> blues) {
        this.blues = blues;//the list of blue creatures currently on the board
    }

    /**
     *
     * @param def used to get list of properties
     * @param creature
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return if a path can be made based on deterministic movement properties
     */
    public boolean viablePath(CreatureDefinition def, CreatureName creature, int fromX, int fromY, int toX, int toY){
        Stack<CreatureProperty> properties = new Stack<>();
        for(CreatureProperty property: def.properties()){
            properties.push(property);//make a stack so you can configure the order
        }
        switch(properties.pop()){
            case WALKING :
                if(walkPath(fromX,fromY,toX,toY)==null){
                    updateLocation(fromX,fromY,toX,toY);
                    if(!BFSColonyConnectivity(new Hex(toX,toY))) {//is end result connected?
                        updateLocation(toX,toY,fromX,fromY);
                        return false;
                    }
                    updateLocation(toX,toY,fromX,fromY);
                }
                break;
                case RUNNING:
                    if (runPath(def.maxDistance(),fromX,fromY,toX,toY)==null) {
                        updateLocation(fromX,fromY,toX,toY);
                        if(!BFSColonyConnectivity(new Hex(toX,toY))) {
                            updateLocation(toX,toY,fromX,fromY);
                            return false;
                        }
                        updateLocation(toX,toY,fromX,fromY);
                    }
                    break;
                case JUMPING:
                    if(!jumpPath(fromX,fromY,toX,toY))
                        return false;
                    break;
                case FLYING:
                    if (!flyPath(fromX,fromY,toX,toY))
                        return false;
                    break;
                case SWAPPING:
                    if(!swapPath(fromX, fromY,toX,toY))
                        return false;
                    break;
                default:
                    break;
            }

        return true;
    }

    private boolean swapPath(int fromX, int fromY, int toX, int toY) {
        for(Hex hex: hexBoard){
            if(hex.getX()==toX&&hex.getY()==toY&&hex.getCreature()!=null){//checks to make sure only swapping with occupied
                int placeX = toX;//placeholders for the swap
                int placeY = toY;
                toX = fromX;
                fromX = placeX;
                toY=fromY;
                fromY=placeY;
                return true;
            }
        }
        return false;
    }

    /**
     * lower the list of neighbors for a specific coordinate to only the occupied ones
     * @param coord
     * @return
     */
    Collection<Hex> activeNeighbors(Hex coord){
        Collection<Hex> currentNeighbors = coord.getNeighbors();
        Collection<Hex> actives = new ArrayList<>();
        for(Hex hex: hexBoard){
            if(hex.getCreature()!=null){
                for(Hex iterator: currentNeighbors){
                    if(hex.equals(iterator)&&isOccupied(hex.getX(), hex.getY()))
                        actives.add(hex);
                }
            }
        }
        return actives;
    }

    /**
     * checks if a jump path is linear
     * @param from
     * @param to
     * @return
     */
    boolean lineararity(Hex from,Hex to){
        int fromZ = -from.getX()- from.getY();
        int toZ = -to.getX()- to.getY();
        return (from.getX()==to.getX()||from.getY()==to.getY()||toZ==fromZ);
    }

    boolean flyPath(int fromX, int fromY, int toX, int toY){
        Hex from = new Hex(fromX,fromY);
        Hex to = new Hex(toX,toY);
        if(activeNeighbors(from).size()==6)//cannot take off if surrounded
            return false;
        if(activeNeighbors(to).isEmpty())//cannot move to where no neighbors
            return false;
        updateLocation(fromX,fromY,toX,toY);//temporarily change to simulate what board is if changed
        if(!BFSColonyConnectivity(getHex(toX,toY))){//determine if change breaks connectivity
            updateLocation(toX,toY,fromX,fromY);//change back
            return false;
        }
        updateLocation(toX,toY,fromX,fromY);
        return true;
    }

    //jump path must move in a straight line, same logic as fly for movement
    boolean jumpPath(int fromX, int fromY, int toX, int toY){
        Hex to = new Hex(toX,toY);
        if(!lineararity(new Hex(fromX,fromY),new Hex(toX,toY)))
            return false;
        if(activeNeighbors(to).isEmpty())
            return false;
        updateLocation(fromX,fromY,toX,toY);
        if(!BFSColonyConnectivity(getHex(toX,toY))){
            updateLocation(toX,toY,fromX,fromY);
            return false;
        }
        updateLocation(toX,toY,fromX,fromY);
        return true;
    }

    /**
     * determine if can move by index of one and is still connected to the ongoing path
     * @param hex
     * @param excluded
     * @return
     */
    private Hex canWalk(Hex hex, Set<Hex> excluded){
        for(int i = hex.getX()-1;i<hex.getX()+1;i++){
            for(int j = hex.getY()-1;j<hex.getY()+1;j++){
                if(!(i==hex.getX()+1&&j== hex.getY()+1)&&!(i== hex.getX()-1&&j==hex.getY()-1)){
                    boolean valid = !excluded.contains(getHex(i,j))&&!isOccupied(i,j);
                    if(valid){
                        return  getHex(i,j);
                    }
                }
            }
        }
        return null;
    }
    //similar logic to walk, but always goes by max distance
    private Hex canRun(int dist,Hex hex, Set<Hex> excluded){//dist is max distance
        for(int i = hex.getX()-dist;i<hex.getX()+dist;i++){
            for(int j = hex.getY()-dist;j<hex.getY()+dist;j++){
                if(!(i==hex.getX()+dist&&j== hex.getY()+dist)&&!(i== hex.getX()-dist&&j==hex.getY()-dist)){
                    boolean valid = !excluded.contains(getHex(i,j))&&!isOccupied(i,j);
                    if(valid){
                        return  getHex(i,j);
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return list of all hexes in the path, if its null then theres no available path to traverse
     */
    public List<Hex> walkPath(int fromX, int fromY, int toX, int toY){
        Hex start = getHex(fromX,fromY);
        Hex end = getHex(toX,toY);
        boolean path = false;
        Queue<Hex> queue = new LinkedList<>();
        Map<Hex,Hex> hexes = new HashMap<>();
        Set<Hex> visited = new HashSet<>();
        queue.offer(start);

        while(!queue.isEmpty()){
            Hex next = queue.peek();
            path = (start==end) ? true : false;
            if(path)
                break;
            visited.add(next);
            Hex notPassed = canWalk(next, visited);

            if(notPassed!=null){
                queue.offer(notPassed);
                visited.add(notPassed);
                hexes.put(notPassed,next);
                path = (notPassed.equals(end)) ? true : false;
                if(path)
                    break;

            }else{
                queue.poll();
            }
        }
        if(path){
            List<Hex> sPath = new ArrayList<>();//from the possible visited hexes, develop the shortest path
            Hex endHex = end;
            while(endHex!=null){
                sPath.add(endHex);
                endHex=hexes.get(endHex);
            }
            Collections.reverse(sPath);
            return sPath;
        }
        return null;
    }


    public List<Hex> runPath(int dist, int fromX, int fromY, int toX, int toY){//similar logic to the walk path, but using run criteria
        Hex start = getHex(fromX,fromY);
        Hex end = getHex(toX,toY);
        boolean path = false;
        Queue<Hex> queue = new LinkedList<>();
        Map<Hex,Hex> hexes = new HashMap<>();
        Set<Hex> visited = new HashSet<>();
        queue.offer(start);

        while(!queue.isEmpty()){
            Hex next = queue.peek();
            path = (start==end) ? true : false;
            if(path)
                break;
            visited.add(next);
            Hex notPassed = canRun(dist,next, visited);

            if(notPassed!=null){
                queue.offer(notPassed);
                visited.add(notPassed);
                hexes.put(notPassed,next);
                path = (notPassed.equals(end)) ? true : false;
                if(path)
                    break;

            }else{
                queue.poll();
            }
        }
        if(path){
            List<Hex> sPath = new ArrayList<>();
            Hex endHex = end;
            while(endHex!=null){
                sPath.add(endHex);
                endHex=hexes.get(endHex);
            }
            Collections.reverse(sPath);
            return sPath;
        }
        return null;
    }

    public MoveResponse checkEndGame(){
        boolean redWin = false;
        boolean blueWin = false;
        for(Hex coord: hexBoard){
            if(coord.getCreature()!=null&&coord.getCreature().equals(CreatureName.BUTTERFLY)){
                if(activeNeighbors(coord).size()==6){
                    if(coord.getPlayerPiece()==PlayerName.BLUE)
                        redWin=true;//because the blue butterfly has 6 active neighbors
                    if(coord.getPlayerPiece()==PlayerName.RED)
                        blueWin=true;
                }
            }
        }
        if(blueWin&&redWin){//note that if they draw
            return new MoveResponse(MoveResult.DRAW);
        } else if (blueWin) {
            return new MoveResponse(MoveResult.BLUE_WON);
        } else if (redWin) {
            return new MoveResponse(MoveResult.RED_WON);
        }
        return new MoveResponse(MoveResult.OK);
    }

    /**
     *
     * @param tox
     * @param toy
     * @param board
     * @return if the placement of the new piece does disconnect the board
     */
    public boolean isDisconnected(int tox, int toy, gameBoard board) {
        Hex spot = new Hex(tox,toy);
        Collection<Hex> neighbors = spot.getNeighbors();
        for(Hex neighboring: neighbors){
            if(board.isOccupied(neighboring.getX(), neighboring.getY()))
                return false;
        }
        return true;
    }

    /**
     * does BFS so that one spot can find a path to every possible hex
     * @param to
     * @return
     */
    public boolean BFSColonyConnectivity(Hex to){
        if((reds.size()+blues.size())==0||(reds.size()+blues.size())==1)//either theres no pieces or one piece which is connected
            return true;
        Set<Hex> visited = new HashSet<>();
        Queue<Hex> queue = new LinkedList<>();
        if(to.getCreature()==null){
            to=hexBoard.iterator().next();//will grab the first available space to check connectivity using bfs
        }
        queue.offer(to);
        visited.add(to);

        while(!queue.isEmpty()){
            Hex current = queue.poll();
            for(Hex neighbor: activeNeighbors(current)){
                if(!visited.contains(neighbor)&&neighbor.getCreature()!=null){
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        Set<Hex> filteredVisited = new HashSet<>();
        for(Hex curHex:visited){
            if(curHex.getCreature()!=null)
                filteredVisited.add(curHex);//filter out any hexes that may have been added if the creature was null
        }
        return filteredVisited.size()==(reds.size()+blues.size());
    }
}
