import java.io.*;
import java.util.*;

/**
 * made by Ariel Yifee
 */

public class WGraph_Algo implements weighted_graph_algorithms {
    private static weighted_graph g;

    public WGraph_Algo(weighted_graph g0) {//constructor
        this.g = g0;
    }

    public WGraph_Algo() {//constructor
        this.g = null;
    }

    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.g;
    }

    @Override
    public weighted_graph copy() {
        if (g.getV() == null) {
            return null;
        }
        weighted_graph g2 = new WGraph_DS();
        for (node_info n : g.getV()) { // first copy all the nodes from the graph to the new graph.
//            node_info Nnode = new WGraph_DS.NodeInfo(n.getKey());
            g2.addNode(n.getKey());
            g2.getNode(n.getKey()).setInfo(n.getInfo());
            g2.getNode(n.getKey()).setTag(n.getTag());
        }
        for (node_info n : g.getV()) { //make all the connection that is in the graph to the new graph.
            for (node_info ng : g.getV(n.getKey())) {
                double w = g.getEdge(n.getKey(), ng.getKey());
                g2.connect(n.getKey(), ng.getKey(), w);
            }
        }
        return g2;
    }

    @Override
    public boolean isConnected() {
        if (g.nodeSize() == 0) { // if the graph is null return true
            return true;
        }
        for (node_info n : g.getV()) { // reset the tag value to 0
            n.setTag(Double.MAX_VALUE);
        }
        boolean[] arr = new boolean[g.getV().size()]; // make array to check the connection
        Arrays.fill(arr, false);
        Object[] obj = g.getV().toArray(); // to pick the first object
        WGraph_DS.NodeInfo first = (WGraph_DS.NodeInfo) obj[0];
        int i = 0;
        arr[i] = true;
        i++;
        Queue<node_info> queue = new LinkedList<>(); //for the bfs
        queue.add(first);
        first.setTag(1);
        while (queue.size() != 0) { // do the bfs
            node_info node = new WGraph_DS.NodeInfo();
            node = queue.poll();
            for (node_info n : g.getV(node.getKey())) {
                if (n.getTag() == Double.MAX_VALUE) {
                    queue.add(n);
                    n.setTag(1);
                    arr[i++] = true;
                }
            }
        }
        for (int j = 0; j < arr.length; j++) { // if one of the index of the array is false that's mean that not all the nodes are connected
            if (arr[j] == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if (src == dest) { // if its the same node the path is 0
            return 0;
        }
        if (g.getNode(src) == null || g.getNode(dest) == null) {// if one of the nodes are null return -1
            return -1;
        }
        for (node_info n : g.getV()) { // reset the tag value
            n.setTag(Double.MAX_VALUE);
        }
        double path = 0;
        node_info src1 = g.getNode(src);
        node_info dest1 = g.getNode(dest);
        PriorityQueue<node_info> queue = new PriorityQueue<node_info>(new Comparator<node_info>() {
            @Override
            public int compare(node_info o1, node_info o2) {
                if (o1.getTag() < o2.getTag()) {
                    return -1;
                } else if (o2.getTag() < o1.getTag()) {
                    return 1;
                }
                return 0;
            }
        });
        queue.add(src1);
        src1.setTag(g.getEdge(src, src));
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size > 0) {
                node_info node = queue.poll();
                if(node == null){
                    return -1;
                }
                if (node == dest1) {
                    path = node.getTag();
                    return path;
                }
                for (node_info n : g.getV(node.getKey())) {
                    if (n.getTag() > node.getTag() + g.getEdge(n.getKey(), node.getKey())) {
                        n.setTag(node.getTag() + g.getEdge(n.getKey(), node.getKey()));
                        queue.add(n);
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        LinkedList<node_info> path = new LinkedList<node_info>(); // contains the nodes
        double dis = shortestPathDist(src, dest);
        if (src == dest) { // if its the same node the path is 0
            node_info src1 = g.getNode(src);
            path.addLast(src1);
            return path;
        }
        if (g.getNode(src) == null || g.getNode(dest) == null) {// if one of the nodes are null return -1
            return path;
        }
        if (dis == -1) { // if there is not path return -1
            return path;
        }
        node_info src1 = g.getNode(src);
        node_info dest1 = g.getNode(dest);
        PriorityQueue<node_info> queue = new PriorityQueue<node_info>(new Comparator<node_info>() {
            @Override
            public int compare(node_info o1, node_info o2) {
                if (o1.getTag() < o2.getTag()) {
                    return -1;
                } else if (o2.getTag() < o1.getTag()) {
                    return 1;
                }
                return 0;
            }
        }); // we'll run with bfs to check the shortest path
        for (node_info n : g.getV()) { // reset the tag value
            n.setTag(Double.MAX_VALUE);
        }
        queue.add(src1);
        src1.setTag(g.getEdge(src, src));
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size > 0) {
                node_info node = queue.poll();
                if (node == dest1) {
                    path.addLast(node);
                    while(node.getTag() > 0){
                        for (node_info ni: g.getV(node.getKey())){
                            if(ni.getTag() + g.getEdge(node.getKey(),ni.getKey()) == node.getTag()){
                                path.addFirst(ni);
                                node = ni;
                            }
                        }
                    }
                    return path;
                }
                for (node_info n : g.getV(node.getKey())) {
                    if (n.getTag() > node.getTag() + g.getEdge(n.getKey(), node.getKey())) {
                        n.setTag(node.getTag() + g.getEdge(n.getKey(), node.getKey()));
                        queue.add(n);
                    }
                }
            }
            for (node_info n : g.getV()) { // reset the tag value
                n.setTag(Double.MAX_VALUE);
            }
        }
        return path;
    }

    @Override
    public boolean save(String file) {
        try {
            File graph = new File(file); //create new file
            FileWriter writer = new  FileWriter(file, true); //if the file is already exist overwrite it.
            if (!(new File(graph.getParent()).exists())) { // check if the direction is exist
                System.out.println("Parent folders did not exist, creating them now..");
                File dirs = new File(graph.getParent()); // try to make parent folder
                if (!dirs.mkdirs()) {
                    System.err.println("Could not create parent folders.");
                    return false;
                }
            }
            if (graph.createNewFile()) {// if we create new file
                System.out.println("File created: " + graph.getName());
                write2File(file);
                return true;
            } else { // if the file is already exist
                System.out.println("File is update.");
                write2File(file);
                return true;
            }
        } catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    public static void write2File(String file_path) {
        File file = new File(file_path);
        BufferedWriter bf = null;
        ;
        try {
            //create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));
            int q = 0;
            bf.write("the graph nodes are:\n");
            for (node_info n: g.getV()){
                if(q != 0) {
                    bf.write("," + n.getKey());
                }
                else {
                    bf.write(""+ n.getKey());
                    q++;
                }
            }
            for (node_info node : g.getV()) {
                q=0;
                bf.write("\n\n" + node.getInfo() + "," + node.getKey() +"," + node.getTag()+ "\nneighbors:\n");
                for (node_info n : g.getV(node.getKey())) {
                    if(n.getKey() != node.getKey()) {
                        if (q!=0) {
                            bf.write("," + n.getKey());
                        }
                        else {
                            bf.write("" + n.getKey());
                            q++;
                        }
                    }
                }
                bf.newLine();
                bf.write("edges:\n");
                q=0;
                for (node_info n : g.getV(node.getKey())) {
                    if(n.getKey() != node.getKey()) {
                        if(q!=0) {
                            bf.write("," + g.getEdge(node.getKey(), n.getKey()));
                        }
                        else {
                            bf.write(""+g.getEdge(node.getKey(), n.getKey()));
                            q++;
                        }
                    }
                }
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //always close the writer
                bf.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public boolean load(String file) {
        weighted_graph g5 = new WGraph_DS();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(file));
            String contentLine = br.readLine();
            contentLine = br.readLine();
            String[] arr1 = contentLine.split(",");
//            System.out.println(Arrays.toString(arr1));
            for (int i = 0; i < arr1.length; i++) {
                int x = Integer.parseInt(arr1[i]);
                g5.addNode(x);
            }
            contentLine = br.readLine(); // line3
            contentLine = br.readLine(); // line4
            while (contentLine != null){
                String[] arr2 = contentLine.split(",");
                int id = Integer.parseInt(arr2[1]);
                double tag1 = Double.parseDouble(arr2[2]);
                g5.getNode(id).setInfo(arr2[0]);
                g5.getNode(id).setTag(tag1);
                contentLine = br.readLine(); // next line
                contentLine = br.readLine(); // next line
                String[] arr3 = contentLine.split(",");
                contentLine = br.readLine(); // next line
                contentLine = br.readLine(); // next line 8
                String[] arr4 = contentLine.split(",");
                for (int i = 0; i < arr3.length; i++) {
                    int x = Integer.parseInt(arr3[i]);
                    double w = Double.parseDouble(arr4[i]);
                    g5.connect(id,x,w);
                }
                contentLine = br.readLine(); // next line
                contentLine = br.readLine(); // next line
            }
            this.g = g5;
            return true;
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return false;
        }
        finally
        {
            try {
                if (br != null)
                    br.close();
            }
            catch (IOException ioe)
            {
                System.out.println("Error in closing the BufferedReader");
                return false;
            }
        }
    }
}