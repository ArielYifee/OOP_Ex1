import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class Ex1_Test {
    private long start;
    private long end;

@BeforeEach
public void start() {
    start = new Date().getTime();
}

    @Test
    @DisplayName("check empty graph")
    void test0() throws Exception{
        weighted_graph g = new WGraph_DS();
        weighted_graph_algorithms g1 = new WGraph_Algo();
        g1.init(g);
        int n = g.nodeSize();
        int e = g.edgeSize();
        boolean c = g1.isConnected();
        assertEquals(0,n,"empty graph node size");
        assertEquals(0,e,"empty graph edge size");
        assertEquals(true,c,"empty graph connection");

    }

    @Test
    @DisplayName("check one graph")
    void test1() throws Exception {
        weighted_graph g = new WGraph_DS();
        g.addNode(1);
        weighted_graph_algorithms g1 = new WGraph_Algo();
        g1.init(g);
        int n = g.nodeSize();
        int e = g.edgeSize();
        boolean c = g1.isConnected();
        boolean h = g.hasEdge(1, 1);
        double d = g1.shortestPathDist(1, 1);
        g.removeNode(2);
        assertEquals(1,n,"one node graph node size");
        assertEquals(0,e,"one node graph edge size");
        assertEquals(true,c,"one node graph connection");
        assertEquals(false,h,"one node graph has edge");
        assertEquals(0,d,"one node graph edge weight");
    }

    @Test
    @DisplayName("check disconnected graph")
    void test2() throws Exception {
        weighted_graph g = new WGraph_DS();
        g.addNode(1);
        g.addNode(2);
        g.connect(1, 3, 5);
        weighted_graph_algorithms g1 = new WGraph_Algo();
        g1.init(g);
        int e = g.edgeSize();
        boolean c = g1.isConnected();
        boolean h = g.hasEdge(1, 2);
        double d = g1.shortestPathDist(1, 2);
        assertEquals(0,e,"disconnected graph edge size");
        assertEquals(-1,d,"disconnected graph edge weight");
        assertEquals(false,c,"disconnected graph connection");
        assertEquals(false,h,"disconnected graph has edge");
    }

    @Test
    @DisplayName("check 2 node with one edge")
    void test3() throws Exception {
        weighted_graph g = new WGraph_DS();
        g.addNode(1);
        g.addNode(2);
        g.connect(1, 2, 3.5);
        weighted_graph_algorithms g1 = new WGraph_Algo();
        g1.init(g);
        int n = g.nodeSize();
        int e = g.edgeSize();
        boolean c = g1.isConnected();
        boolean h = g.hasEdge(1, 2);
        double d = g1.shortestPathDist(1, 2);
        assertEquals(2,n,"two node graph node size");
        assertEquals(1,e,"two node graph edge size");
        assertEquals(3.5,d,"two node graph edge weight");
        assertEquals(true, c,"two node graph connection");
        assertEquals(true,h,"two node graph has edge");
    }

    @Test
    @DisplayName("check remove edge and node function, path and path distance")
    void test4() throws Exception {
        weighted_graph g = graph1();
        weighted_graph_algorithms g1 = new WGraph_Algo();
        g1.init(g);
        int n1 = g.nodeSize();
        int e0 = g.edgeSize();
        boolean c = g1.isConnected();
        boolean h = g.hasEdge(1, 2);
        double d = g1.shortestPathDist(0, 7);
        int m = g.getMC();
        List<node_info> L = g1.shortestPath(0, 7);
        int Ls = L.size();
        assertEquals(13,n1,"graph1 node size");
        assertEquals(18,e0,"graph1 edge size");
        assertEquals(5,d,"graph1 distance");
        assertEquals(4,Ls,"graph1 node list");
        assertEquals(31,m,"graph1 mode count");
        assertEquals(true,c,"graph1 connection");
        assertEquals(true,h,"graph1 has edge");
        g.removeEdge(1, 2);
        int e1 = g.edgeSize();
        double h1 = g.getEdge(1, 2);
        assertEquals(17,e1,"graph1 edge size 2");
        assertEquals(-1,h1,"graph1 has edge 2");
        g.removeNode(4);
        int n2 = g.nodeSize();
        int e2 = g.edgeSize();
        boolean c1 = g1.isConnected();
        assertEquals(14,e2,"graph1 edge size 3");
        assertEquals(12,n2,"graph1 node size 2");
        assertEquals(true,c1,"graph1 connection 2");
        g.removeEdge(0, 7);
        boolean c2 = g1.isConnected();
        assertEquals(true,c2,"graph1 connection 3");
        g.removeEdge(6, 5);
        boolean c3 = g1.isConnected();
        double d2 = g1.shortestPathDist(0, 6);
        assertEquals(false,c3,"graph1 connection 4");
        assertEquals(-1,d2,"graph1 distance 2");
    }

    @Test
    @DisplayName("check deep copy")
    void test5() throws Exception {
        weighted_graph g = graph1();
        weighted_graph_algorithms g1 = new WGraph_Algo();
        g1.init(g);
        weighted_graph g2 = g1.copy();
        g2.removeNode(10);
        boolean b;
        Collection<node_info> n = g.getV(10);
        if (n == null) {
            b = false;
        } else {
            b = true;
        }
        assertEquals(true,b,"deep copy test 1");
        g.addNode(13);
        node_info n2 = g2.getNode(13);
        boolean b2;
        if (n2 == null) {
            b2 = true;
        } else {
            b2 = false;
        }
        assertEquals(true,b2,"deep copy test 2");
    }

    @Test
    @DisplayName("check save and load")
    void test6() throws Exception {// check save and load
        weighted_graph g = graph1();
        weighted_graph_algorithms g1 = new WGraph_Algo();
        g1.init(g);
        boolean b1 = g1.save("D:\\ex1\\graph.txt"); // you need to put your own path
        assertEquals(true,b1,"save graph test");
        weighted_graph_algorithms g2 = new WGraph_Algo();
        boolean b2 = g2.load("D:\\ex1\\graph.txt");
        assertEquals(true,b2,"load graph test");
        boolean c1 = g1.isConnected();
        boolean c2 = g2.isConnected();
        double d1 = g1.shortestPathDist(0, 7);
        double d2 = g2.shortestPathDist(0, 7);
        List<node_info> L1 = g1.shortestPath(0, 7);
        List<node_info> L2 = g2.shortestPath(0, 7);
        int Ls1 = L1.size();
        int Ls2 = L2.size();
        assertEquals(d2,d1,"load graph distance");
        assertEquals(Ls2,Ls1,"load graph node list");
        assertEquals(c2,c1,"load graph connection");
    }

    @Test
    @DisplayName("graph with million vertices and 5 million edges")
    void test7() throws Exception {
        weighted_graph g = new WGraph_DS();
        weighted_graph_algorithms g1 = new WGraph_Algo();
        g1.init(g);
        for (int i = 0; i < 1000000; i++) {
            g.addNode(i);
        }
        for (int i = 0; i < 500000; i++) {
            int k = (i + 1) % 999991;
            for (int j = 0; j < 10; j++) {
                k++;
                g.connect(i, k, (Math.random() * (100 - 1)) + 1);
            }
        }
        int n = g.nodeSize();
        int e = g.edgeSize();
        assertEquals(1000000,n,"million vertices graph node size");
        assertEquals(5000000,e,"million vertices graph edge size");
    }

    @AfterEach
    public void runtime() {
    end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        boolean t = dt < 4;
//        System.out.println(end - start);
        assertEquals(true, t, "runtime test");
    }

    public weighted_graph graph1() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);
        g.addNode(6);
        g.addNode(7);
        g.addNode(8);
        g.addNode(9);
        g.addNode(10);
        g.addNode(11);
        g.addNode(12);
        g.connect(0, 1, 7);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.connect(1, 2, 3);
        g.connect(1, 4, 4);
        g.connect(2, 4, 4);
        g.connect(2, 5, 1);
        g.connect(3, 12, 2);
        g.connect(4, 6, 5);
        g.connect(5, 7, 2);
        g.connect(5, 6, 3);
        g.connect(7, 8, 2);
        g.connect(8, 9, 5);
        g.connect(9, 10, 4);
        g.connect(9, 11, 4);
        g.connect(10, 11, 6);
        g.connect(10, 12, 4);
        g.connect(11, 12, 4);
        return g;
    }
}