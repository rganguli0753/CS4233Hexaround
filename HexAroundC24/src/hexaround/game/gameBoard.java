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
        int dist = from.getDistance(to);
        return maxDist>=dist;
    }

    public boolean propertyExist(CreatureDefinition def, CreatureProperty prop){
        for(CreatureProperty p: def.properties()){
            if(p.toString().equals(prop.toString()))
                return true;
        }
        return false;
    }

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
        this.reds = reds;
    }

    public void setBlues(LinkedList<CreatureName> blues) {
        this.blues = blues;
    }

    public boolean viablePath(CreatureDefinition def, CreatureName creature, int fromX, int fromY, int toX, int toY){
        Stack<CreatureProperty> properties = new Stack<>();
        for(CreatureProperty property: def.properties()){
            properties.push(property);
        }
        switch(properties.pop()){
            case WALKING :
                if(walkPath(fromX,fromY,toX,toY)==null){
                    updateLocation(fromX,fromY,toX,toY);
                    if(!BFSColonyConnectivity(new Hex(toX,toY))) {
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

    boolean lineararity(Hex from,Hex to){
        int fromZ = -from.getX()- from.getY();
        int toZ = -to.getX()- to.getY();
        return (from.getX()==to.getX()||from.getY()==to.getY()||toZ==fromZ);
    }

    boolean flyPath(int fromX, int fromY, int toX, int toY){
        Hex from = new Hex(fromX,fromY);
        Hex to = new Hex(toX,toY);
        if(activeNeighbors(from).size()==6)
            return false;
        if(activeNeighbors(to).isEmpty())
            return false;
        updateLocation(fromX,fromY,toX,toY);
        if(!BFSColonyConnectivity(getHex(toX,toY))){
            updateLocation(toX,toY,fromX,fromY);
            return false;
        }
        return true;
    }

    //jump path must move in a straight line
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
        return true;
    }

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
    private Hex canRun(int dist,Hex hex, Set<Hex> excluded){//dist is max distance
        for(int i = hex.getX()-dist;i<hex.getX()+dist;i++){
            for(int j = hex.getY()-dist;j<hex.getY()+dist;j++){
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


    public List<Hex> runPath(int dist, int fromX, int fromY, int toX, int toY){
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
                        redWin=true;
                    if(coord.getPlayerPiece()==PlayerName.RED)
                        blueWin=true;
                }
            }
        }
        if(blueWin&&redWin){
            return new MoveResponse(MoveResult.DRAW);
        } else if (blueWin) {
            return new MoveResponse(MoveResult.BLUE_WON);
        } else if (redWin) {
            return new MoveResponse(MoveResult.RED_WON);
        }
        return new MoveResponse(MoveResult.OK);
    }

    public boolean isDisconnected(int tox, int toy, gameBoard board) {
        Hex spot = new Hex(tox,toy);
        Collection<Hex> neighbors = spot.getNeighbors();
        for(Hex neighboring: neighbors){
            if(board.isOccupied(neighboring.getX(), neighboring.getY()))
                return false;
        }
        return true;
    }

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
                filteredVisited.add(curHex);
        }
        return filteredVisited.size()==(reds.size()+blues.size());
    }
}
