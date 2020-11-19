import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * made by Ariel Yifee
 */

public class WGraph_DS implements weighted_graph {
    private HashMap<Integer, HashMap<Integer, node_info>> V; // nodes
    private HashMap<Integer, HashMap<Integer, Double>> E; // edges wight
    private HashMap<Integer,node_info> NL; // node list
    private static int edgeS, MC;

    public WGraph_DS() { // constructor
        V = new HashMap<>();
//        E = new Edges();
        NL = new HashMap<>();
        E = new HashMap<>();
        this.edgeS = 0;
        this.MC = 0;

    }

    @Override
    public node_info getNode(int key) {
        if (V.containsKey(key)) { // check if the node is in the graph by it key and if it is it will return the node itself
            return V.get(key).get(key);
        }
        return null;
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (!V.containsKey(node1) || !V.containsKey(node2)) { // if one of the node aren't in the graph return false
            return false;
        }
        if (node1 == node2) {
            return false;
        }
        boolean n1 = V.get(node1).containsKey(node2);
        boolean n2 = V.get(node2).containsKey(node1);
        return n1 && n2; // if there is edge between them return true
    }
    @Override
    public double getEdge(int node1, int node2) {
        if(node1 == node2){
            return 0;
        }
        if(!hasEdge(node1,node2)){
            return -1;
        }
        return E.get(node1).get(node2);
//        return E.EdgeWeight(node1,node2);
    }

    @Override
    public void addNode(int key) {
        if(V.containsKey(key)){
            return;
        }
        node_info n = new NodeInfo(key);
        HashMap<Integer,node_info> node = new HashMap<>();
        HashMap<Integer,Double> edge = new HashMap<>();
        node.put(key,n);
        edge.put(key,0.0);
        V.put(key,node);
        E.put(key,edge);
//        E.newNode(key);
        NL.put(key,n);
        MC++;
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if (node1 == node2){
            return;
        }
        if (hasEdge(node1, node2)) { // if there is edge between them do nothing
            E.get(node1).put(node2, w);
            E.get(node2).put(node1, w);
//            E.NewEdge(node1, node2, w);
            return;
        }
        if (!V.containsKey(node1) || !V.containsKey(node2)) { // if one of them aren't in the graph do nothing
            return;
        }
        node_info Node1 = V.get(node1).get(node1);
        node_info Node2 = V.get(node2).get(node2);
        V.get(node1).put(node2,Node2);
        V.get(node2).put(node1,Node1); // add to each node the other node as neighbor
//        E.NewEdge(node1, node2, w);
        E.get(node1).put(node2, w);
        E.get(node2).put(node1, w); //add to each node the wight to the other node
        edgeS++;
        MC++;
    }

    @Override
    public Collection<node_info> getV() {
        return NL.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        if(!NL.containsKey(node_id)){
            Collection<node_info> n = new LinkedList<>();
            return n;
        }
        return V.get(node_id).values();
    }

    @Override
    public node_info removeNode(int key) {
        if (NL.containsKey(key)) { // if the node is in the graph go through all it's neighbors and delete the node itself
            int Esize = V.get(key).size() - 1;
            node_info n = NL.get(key);
            V.remove(key); //remove the node from the graph
            E.remove(key); //remove the node frome the edges
            for (HashMap nodes : V.values()) {
                if (nodes.containsKey(key)){
                    nodes.remove(key);
                }
            }
            for (HashMap nodes : E.values()) { //remove the node from the edges
                if (nodes.containsKey(key)){
                    nodes.remove(key);
                }
            }
            MC++;
            edgeS = edgeS - Esize;
            NL.remove(key);
            return n;
        }
        return null;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if (!NL.containsKey(node1) || !NL.containsKey(node2)) { // if one of them aren't in the graph do nothing
            return;
        }
        if(hasEdge(node1,node2)) { // if there is edge between them remove each other from the neighbor list
            V.get(node1).remove(node2);
            V.get(node2).remove(node1);
            E.get(node1).remove(node2);
            E.get(node2).remove(node1);
//            E.removeEd(node1, node2);
            edgeS--;
            MC++;
        }
        return;
    }

    @Override
    public int nodeSize() {
        return NL.size();
    }

    @Override
    public int edgeSize() {
        return edgeS;
    }

    @Override
    public int getMC() {
        return MC;
    }

    static class NodeInfo implements node_info {
        private String Info;
        private int key;
        private double Tag;
//        private HashMap<Integer, node_info> Ni;
        private static int index;

        //constructor
        public NodeInfo() {
            this.key = index++;
//            Ni = new HashMap<>();
            this.Tag = Double.MAX_VALUE;
            this.Info = null;
        }

        //constructor with key
        public NodeInfo(int key) {
            this.key = key;
//            Ni = new HashMap<>();
            this.Tag = Double.MAX_VALUE;
            this.Info = null;
        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return this.Info;
        }

        @Override
        public void setInfo(String s) {
            this.Info = s;
        }

        @Override
        public double getTag() {
            return this.Tag;
        }

        @Override
        public void setTag(double t) {
            this.Tag = t;
        }
    }
}
