import java.util.*;

public class Main {

    static class FlowEdge {
        final int from;
        final int to;
        int capacity;
        int flow;

        public FlowEdge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }

        public int remainingCapacity() {
            return capacity - flow;
        }

        public void addFlow(int flow) {
            this.flow += flow;
        }

        @Override
        public String toString() {
            return "(" +
                     from +
                    "," + to +
                    ")" + flow +
                    "/" + capacity;
        }
    }

    static class FlowNetwork {
        private final int numVertices;
        private final List<List<FlowEdge>> adjacencyList;

        public FlowNetwork(int numVertices) {
            this.numVertices = numVertices;
            adjacencyList = new ArrayList<>(numVertices);
            for (int i = 0; i < numVertices; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        public void addEdge(int from, int to, int capacity) {
            FlowEdge edge = new FlowEdge(from, to, capacity);
            FlowEdge reverseEdge = new FlowEdge(to, from, 0);
            edge.flow = reverseEdge.flow = 0;
            //edge.flow=0;
            adjacencyList.get(from).add(edge);
            adjacencyList.get(to).add(reverseEdge);
        }

        public List<FlowEdge> getEdges(int vertex) {
            return adjacencyList.get(vertex);
        }
        public void addEdge(FlowEdge f){
            FlowEdge reverseEdge = new FlowEdge(f.to, f.from, 0);
            f.flow = reverseEdge.flow = 0;
            //edge.flow=0;
            adjacencyList.get(f.from).add(f);
            adjacencyList.get(f.to).add(reverseEdge);
        }
        @Override
        public String toString() {
            String print="FlowNetwork{\n" +
                    " numVertices=" + numVertices+",\n";
            for (List l:adjacencyList) {
                print+="\n List {\n";
                for(Object f:l){
                    print+="  Edge: "+f+"\n";
                }
                print+="}\n";
            }
            return print+"\n}";

        }
    }


        /*static class Dinic {
        private final FlowNetwork network;
        private final int source;
        private final int sink;
        private final int[] distTo;
        private final int[] edgeTo;
        private final int[] iter;

        public Dinic(FlowNetwork network, int source, int sink) {
            this.network = network;
            this.source = source;
            this.sink = sink;
            distTo = new int[network.numVertices];
            edgeTo = new int[network.numVertices];
            iter = new int[network.numVertices];
        }

        private boolean bfs() {
            Arrays.fill(distTo, -1);
            distTo[source] = 0;
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            queue.add(source);
            while (!queue.isEmpty()) {
                int v = queue.poll();
                for (FlowEdge edge : network.getEdges(v)) {
                    int w = edge.to;
                    if (distTo[w] == -1 && edge.remainingCapacity() > 0) {
                        distTo[w] = distTo[v] + 1;
                        edgeTo[w] = v;
                        queue.add(w);
                    }
                }
            }
            return distTo[sink] != -1;
        }


        private int dfs(int v, int sink, int flow) {
            // Check if the current vertex is within the bounds of the flow network
            if (v < 0 || v >= network.numVertices) {
                return 0;
            }

            // If we have reached the sink vertex, return the flow
            if (v == sink) {
                return flow;
            }

            // Iterate through the edges incident to the current vertex
            for (int i = iter[v]; i < network.getEdges(v).size(); i++, iter[v]++) {
                FlowEdge edge = network.getEdges(v).get(i);
                int w = edge.to;
                // Check if the edge has remaining capacity and if the destination vertex is at a higher level in the BFS tree
                if (distTo[w] == distTo[v] + 1 && edge.remainingCapacity() > 0) {
                    int f = dfs(w, sink, Math.min(flow, edge.remainingCapacity()));
                    if (f > 0) {
                        edge.addFlow(f);
                        network.getEdges(w).get(i).addFlow(-f);
                        return f;
                    }
                }
            }
            return 0;
        }





    }*/
        static class Dinic {
            private final FlowNetwork network;
            private final int source;
            private final int sink;
            private final int[] distTo;
            private final int[] edgeTo;
            private final int[] iter;

            public Dinic(FlowNetwork network, int source, int sink) {
                this.network = network;
                this.source = source;
                this.sink = sink;
                distTo = new int[network.numVertices];
                edgeTo = new int[network.numVertices];
                iter = new int[network.numVertices];
            }

            private boolean bfs() {
                Arrays.fill(distTo, Integer.MAX_VALUE);
                distTo[source] = 0;
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(source);

                while (!queue.isEmpty()) {
                    int v = queue.poll();
                    for (FlowEdge edge : network.getEdges(v)) {
                        int w = edge.to;
                        if (edge.remainingCapacity() > 0 && distTo[w] == Integer.MAX_VALUE) {
                            distTo[w] = distTo[v] + 1;
                            edgeTo[w] = v;
                            queue.offer(w);
                        }
                    }
                }
                return distTo[sink] != Integer.MAX_VALUE;
            }

            public int maxFlow() {
                int maxFlow = 0;
                while (bfs()) {
                    Arrays.fill(iter, 0);
                    maxFlow += dfs(source, sink, Integer.MAX_VALUE);
                }
                return maxFlow;
            }

            private int dfs(int v, int sink, int flow) {
                // Check if the current vertex is within the bounds of the flow network
                if (v < 0 || v >= network.numVertices) {
                    return 0;
                }

                // If we have reached the sink vertex, return the flow
                if (v == sink) {
                    return flow;
                }

                // Iterate through the edges incident to the current vertex
                for (int i = iter[v]; i < network.getEdges(v).size(); i++, iter[v]++) {
                    FlowEdge edge = network.getEdges(v).get(i);
                    int w = edge.to;
                    // Check if the edge has remaining capacity and if the destination vertex is at a higher level in the BFS tree
                    if (distTo[w] == distTo[v] + 1 && edge.remainingCapacity() > 0) {
                        int f = dfs(w, sink, Math.min(flow, edge.remainingCapacity()));
                        if (f > 0) {
                            edge.addFlow(f);
                            network.getEdges(w).get(1).addFlow(-f);
                            return f;
                        }
                    }
                }
                return 0;
            }


        }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        String[] names = new String[n];
        int[] outcomes = new int[n];

        for (int i = 0; i < n; i++) {
            names[i] = sc.next();
        }
        for (int i = 0; i < n; i++) {
            outcomes[i] = sc.nextInt();
        }

        // Create flow network with 2 * n + 2 vertices (source, sink, and one for each job)
        FlowNetwork flowNetwork = new FlowNetwork(2 * n + 2);
        int source = 2 * n;
        int sink = 2 * n + 1;

        // Add edges from source to each job vertex
        for (int i = 0; i < n; i++) {
            flowNetwork.addEdge(source, i, 1);
        }
        // Add edges from each job vertex to sink
        for (int i = 0; i < n; i++) {
            flowNetwork.addEdge(i + n, sink, 1);
        }
        //System.out.println(flowNetwork);
        // Add edges between job vertices and their corresponding "fake" vertices
        for (int i = 0; i < n; i++) {
            FlowEdge newEdge=new FlowEdge(i,i+n,outcomes[i]);
            flowNetwork.addEdge(newEdge);
            // Set capacity of fake vertex to job's value
            flowNetwork.getEdges(n+i).get(1).capacity = outcomes[i];
        }
        // Read constraints until "Decide" line is encountered
        //sc.nextLine();
        while (sc.hasNextLine()) {
            String line = sc.next();
            //System.out.println("Line: "+line);
            if (line.equals("Decide")) {
                break;
            }

            // Split constraint into "from" and "to" job names
            String[] parts = line.substring(1, line.length() - 1).split(",");
            int from = -1, to = -1;
            // Find indices of "from" and "to" job names
            for (int i = 0; i < n; i++) {
                if (names[i].equals(parts[0])) {
                    from = i;
                }
                if (names[i].equals(parts[1])) {
                    to = i;
                }
            }
            // Add edge from "from" job's fake vertex to "to" job
            if (from >= 0 && to >= 0) {
                flowNetwork.addEdge(from + n, to, Integer.MAX_VALUE);
            }
        }

        // Create Dinic solver and compute maximum flow
        Dinic dinic = new Dinic(flowNetwork, source, sink);
        int maxFlow = dinic.maxFlow();
        //System.out.println(flowNetwork);
        // Print maximum profit and selected venture projects
        System.out.println("Venture projects: ");
        for (int i = 0; i < n; i++) {
            //System.out.print("-- "+flowNetwork.getEdges(i).get(0).flow+" -- * ");
            if (flowNetwork.getEdges(i).get(1).flow == 1) {
                System.out.println(names[i]);
            }
        }
        System.out.println("\n"+"Maximum profit: " + maxFlow);
    }




}