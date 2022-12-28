import java.util.*;
public class BERKECAN_KOCYIGIT_S021062 {

    private final int MAX_FLOW = Integer.MAX_VALUE;

    static class FlowEdge {
        int from, to, capacity, flow;
        int initalCap;
        public FlowEdge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
            initalCap=capacity;
        }

        public int residualCapacity(int vertex) {
            return  capacity -flow;
        }

        public void addFlow(int vertex, int flow) {

            this.flow+=flow;
        }

        @Override
        public String toString() {
            return "\nFlowEdge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", capacity=" + capacity +
                    ", flow=" + flow +
                    ", initalCap=" + initalCap +
                    '}';
        }
    }

    class FlowNetwork {
        int numVertices;
        List<FlowEdge>[] adjacencyList;

        public FlowNetwork(int numVertices) {
            this.numVertices = numVertices;
            adjacencyList = (List<FlowEdge>[]) new List[numVertices];
            for (int i = 0; i < numVertices; i++) {
                adjacencyList[i] = new ArrayList<>();
            }
        }

        public void addEdge(int from, int to, int capacity) {
            FlowEdge edge = new FlowEdge(from, to, capacity);
            adjacencyList[from].add(edge);
            adjacencyList[to].add(edge);
        }

        public List<FlowEdge> getEdges(int vertex) {
            return adjacencyList[vertex];
        }
        public void reverseEdge(int to,int from,int capacity,int flow){
            int firstArrayIndex=0;
            for(int i=0;i<adjacencyList[from].size();i++){
                if(adjacencyList[from].get(i).to==to){
                    firstArrayIndex=i;
                }
            }
            int secondArrayIndex=0;
            for(int i=0;i<adjacencyList[to].size();i++){
                if(adjacencyList[to].get(i).from==from){
                    secondArrayIndex=i;
                }
            }
            adjacencyList[from].remove(adjacencyList[from].get(firstArrayIndex));
            adjacencyList[to].remove(adjacencyList[to].get(secondArrayIndex));
            FlowEdge newEdge=new FlowEdge(to, from,-capacity);
            adjacencyList[from].add(newEdge);
            adjacencyList[to].add(newEdge);
        }

        @Override
        public String toString() {
            return "FlowNetwork{" +
                    "numVertices=" + numVertices +
                    ", adjacencyList=" + Arrays.toString(adjacencyList) +
                    '}';
        }
    }

    class Vertex {
        int vertex;
        int parent;

        public Vertex(int vertex, int parent) {
            this.vertex = vertex;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "vertex=" + vertex +
                    ", parent=" + parent +
                    '}';
        }
    }
    public int maxFlow(FlowNetwork flowNetwork, int source, int sink) {
        FlowNetwork newNetwork= new FlowNetwork(flowNetwork.numVertices);
        newNetwork.adjacencyList=flowNetwork.adjacencyList.clone();
        List<FlowEdge> will_changed=new ArrayList<>();
        for (int i = 0; i < flowNetwork.numVertices; i++) {
            for (FlowEdge edge : flowNetwork.getEdges(i)) {
                if (edge.capacity < 0) {
                    will_changed.add(edge);
                }
            }
        }
        List<FlowEdge> added=new ArrayList<>();
        int extract=0;
        for(FlowEdge edge:will_changed){
            if(!added.contains(edge)){
                newNetwork.reverseEdge(edge.to,edge.from,edge.capacity, edge.flow);
                added.add(edge);
            }

        }
        return fordFulkerson(newNetwork, source, sink);
    }
    public int fordFulkerson(FlowNetwork flowNetwork, int source, int sink) {
        int flow = 0;
        while (true) {
            Queue<Vertex> queue = new LinkedList<>();
            queue.add(new Vertex(source, -1));
            int[] parent = new int[flowNetwork.numVertices];
            Arrays.fill(parent, -1);
            parent[source] = source;
            while (!queue.isEmpty()) {
                Vertex vertex = queue.poll();
                int vertexNum = vertex.vertex;
                for (FlowEdge edge : flowNetwork.getEdges(vertexNum)) {
                    int to = edge.to;
                    if (edge.residualCapacity(vertexNum) > 0 && parent[to] == -1) {
                        queue.add(new Vertex(to, vertexNum));
                        parent[to] = vertexNum;
                    }
                }
            }
            if (parent[sink]==-1) {
                return flow;
            }
            int minFlow = MAX_FLOW;
            for (int vertex = sink; vertex != source; vertex = parent[vertex]) {
                FlowEdge edge = findEdge(parent[vertex], vertex, flowNetwork);
                minFlow = Math.min(minFlow, edge.residualCapacity(parent[vertex]));
            }
            for (int vertex = sink; vertex != source; vertex = parent[vertex]) {
                FlowEdge edge = findEdge(parent[vertex], vertex, flowNetwork);
                edge.addFlow(parent[vertex], minFlow);
            }
            flow += minFlow;
        }

    }

    private FlowEdge findEdge(int from, int to, FlowNetwork flowNetwork) {
        for (FlowEdge edge : flowNetwork.getEdges(from)) {
            if (edge.to == to &&edge.from==from) {
                return edge;
            }
        }
        throw new RuntimeException("Edge not found");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numProjects = scanner.nextInt();

        String[] projects = new String[numProjects];
        for (int i = 0; i < numProjects; i++) {
            projects[i] = scanner.next();
        }

        int[] capacities = new int[numProjects];
        for (int i = 0; i < numProjects; i++) {
            capacities[i] = scanner.nextInt();
        }

        BERKECAN_KOCYIGIT_S021062.FlowNetwork flowNetwork = new BERKECAN_KOCYIGIT_S021062().new FlowNetwork(numProjects + 2);

        int source = numProjects;

        int sink = numProjects + 1;

        List<FlowEdge> edges=new ArrayList<>();
        while (scanner.hasNext()) {
            String[] edge = scanner.next().split(",");
            if (edge[0].equals("Decide")) {
                break;
            }

            int from = -1;
            int to = -1;
            edge[0]=edge[0].substring(1);
            edge[1]=edge[1].substring(0,1);
            for (int i = 0; i < numProjects; i++) {
                if (projects[i].equals(edge[0])) {
                    to = i;
                }
                if (projects[i].equals(edge[1])) {
                    from = i;
                }
            }
            if (from != -1 && to != -1) {

                flowNetwork.addEdge(from, to, Integer.MAX_VALUE);
                edges.add(new FlowEdge(from,to,0));
            }
        }

        for(FlowEdge edge:edges){
            flowNetwork.addEdge(edge.to,sink,capacities[edge.to]);
            flowNetwork.addEdge(source,edge.from,capacities[edge.from]);
        }

        BERKECAN_KOCYIGIT_S021062 fordFulkerson = new BERKECAN_KOCYIGIT_S021062();

        int maxFlow = fordFulkerson.maxFlow(flowNetwork, source, sink);

        System.out.println("Max profit: "+maxFlow);
    }

}

