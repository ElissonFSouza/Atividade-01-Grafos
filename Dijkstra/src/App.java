// ATENÇÃO: Execute o código para obter instruções.

import java.util.ArrayList;
import java.util.Scanner;

public class App {
    static Scanner entrada = new Scanner(System.in);
    static int INFINITY = 9999, qtd_vertices = 100, NILL = -1, origem = -1, destino = -1;
    static ArrayList<Integer> caminho = new ArrayList<>();

    public static void main(String[] args) {
                            // 0  1  2  3  4  5  6  7  8  9
        int[][] matrizMapa = {{1, 1, 1, 1, 0, 0, 1, 1, 1, 1},  // 0    (0 = obstáculo / maior ou igual a 1 = caminho)
                              {1, 0, 0, 1, 0, 0, 1, 0, 0, 1},  // 1    (para simular um terreno acidentado (peso nas arestas), escolher números inteiros entre 1 e 999)
                              {1, 1, 1, 1, 1, 1, 1, 0, 0, 1},  // 2
                              {1, 0, 0, 0, 0, 1, 0, 0, 1, 1},  // 3
                              {1, 1, 0, 0, 0, 1, 1, 1, 1, 0},  // 4
                              {0, 1, 1, 1, 0, 1, 0, 0, 1, 0},  // 5
                              {1, 1, 0, 1, 1, 1, 1, 1, 1, 0},  // 6
                              {1, 0, 0, 1, 0, 0, 1, 0, 0, 0},  // 7
                              {1, 0, 0, 1, 0, 0, 1, 0, 0, 1},  // 8
                              {1, 0, 0, 1, 1, 1, 1, 1, 1, 1}}; // 9

        System.out.print("\nEste é o mapa atual (para alterá-lo, busque a \"matrizMapa\" no método main do código fonte (Dijkstra > src > App.java):");

        imprimirMatrizMapa(matrizMapa);

        System.out.println("\n(X representa um caminho livre)." +
                "\n(Cada vértice pode ser representado por um número de 00 a 99).\n" +
                "(Exemplo: para escolher as coordenadas [4,7], deve ser digitado \"47\", onde 4 representa a linha, e 7 a coluna).");
        System.out.print("\nEscolha a coordenada de origem: ");
        origem = entrada.nextInt();
        System.out.print("Escolha a coordenada de destino: ");
        destino = entrada.nextInt();

        System.out.println("\nResultado:");

        transformarMatriz(matrizMapa);
    }

    static void transformarMatriz(int[][] matrizMapa) {
        int[][] matrizAdjacencia = new int[100][100];

        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                if (matrizMapa[i][j] > 0) {
                    if (i != 0 && j != 0) {
                        if (matrizMapa[i-1][j] > 0) {
                            matrizAdjacencia [(i*10)+j] [((i-1)*10)+j] = matrizMapa[i][j];
                            matrizAdjacencia [((i-1)*10)+j] [(i*10)+j] = matrizMapa[i][j];
                        }
                        if (matrizMapa[i][j-1] > 0) {
                            matrizAdjacencia [(i*10)+j] [(i*10)+(j-1)] = matrizMapa[i][j];
                            matrizAdjacencia [(i*10)+(j-1)] [(i*10)+j] = matrizMapa[i][j];
                        }
                    }

                    if (i != 9 && j!= 9) {
                        if (matrizMapa[i+1][j] > 0) {
                            matrizAdjacencia [(i*10)+j] [((i+1)*10)+j] = matrizMapa[i][j];
                            matrizAdjacencia [((i+1)*10)+j] [(i*10)+j] = matrizMapa[i][j];
                        }
                        if (matrizMapa[i][j+1] > 0) {
                            matrizAdjacencia [(i*10)+j] [(i*10)+(j+1)] = matrizMapa[i][j];
                            matrizAdjacencia [(i*10)+(j+1)] [(i*10)+j] = matrizMapa[i][j];
                        }
                    }
                }
            }
        }

        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                if (matrizAdjacencia[i][j] == 0 && i != j) {
                    matrizAdjacencia[i][j] = INFINITY;
                }
            }
        }

        dijkstra(matrizAdjacencia, matrizMapa, origem, destino);
    }

    static void dijkstra(int[][] Graph, int[][] matrizMapa, int orig, int dest) {
        int i, u, v, count;
        int[] dist = new int[qtd_vertices];
        int[] Blackened = new int[qtd_vertices];
        int[] pathlength = new int[qtd_vertices];
        int[] parent = new int[qtd_vertices];

        // The parent Of the source vertex is always equal to nill
        parent[orig] = NILL;

        // first, we initialize all distances to infinity.
        for (i = 0; i < qtd_vertices; i++)
            dist[i] = INFINITY;

        dist[orig] = 0;
        for (count = 0; count < qtd_vertices - 1; count++) {
            u = minDistance(dist, Blackened);

            // if MinDistance() returns INFINITY, then the graph is not
            // connected and we have traversed all of the vertices in the
            // connected component of the source vertex, so it can reduce
            // the time complexity sometimes
            // In a directed graph, it means that the source vertex
            // is not a root
            if (u == INFINITY)
                break;
            else {
                // Mark the vertex as Blackened
                Blackened[u] = 1;
                for (v = 0; v < qtd_vertices; v++)  {
                    if (Blackened[v] == 0 && Graph[u][v] != 0
                            && dist[u] + Graph[u][v] < dist[v]) {
                        parent[v] = u;
                        pathlength[v] = pathlength[parent[v]] + 1;
                        dist[v] = dist[u] + Graph[u][v];
                    }
                    else if (Blackened[v] == 0 && Graph[u][v] != 0
                            && dist[u] + Graph[u][v] == dist[v]
                            && pathlength[u] + 1 < pathlength[v]) {
                        parent[v] = u;
                        pathlength[v] = pathlength[u] + 1;
                    }
                }
            }
        }

        // Printing the path
        if (dist[dest] != INFINITY) {
            printPath(parent, dest);
            imprimirMatrizMapa(matrizMapa);
        }

        else
            System.out.println("Não há um caminho do vértice " + orig + " para o vértice " + dest + ".");
    }

    static int minDistance(int[] dist, int[] Blackened) {
        int min = INFINITY, min_index = -1, v;
        for (v = 0; v < qtd_vertices; v++)
            if (Blackened[v] == 0 && dist[v] < min) {
                min = dist[v];
                min_index = v;
            }
        return min == INFINITY ? INFINITY : min_index;
    }

    static void printPath(int[] parent, int _d) { // Function to print the path
        if (parent[_d] == NILL) {
            System.out.print(_d);
            caminho.add(_d);
            return;
        }
        printPath(parent, parent[_d]);
        System.out.print("->" + _d);
        caminho.add(_d);
    }

    static void imprimirMatrizMapa(int[][] matrizMapa) {
        for(Integer vertice : caminho) {
            matrizMapa[vertice/10][vertice%10] = -1;
        }

        System.out.println("\n\n     0 1 2 3 4 5 6 7 8 9\n");
        for (int i=0; i<10; i++) {
            System.out.print(i + "    ");
            for (int j=0; j<10; j++) {
                if (matrizMapa[i][j] == 0) {
                    System.out.print("  ");
                }
                else if (matrizMapa[i][j] == -1) {
                    if ((i*10) + j == origem || (i*10) + j == destino) {
                        System.out.print("ª ");
                    }
                    else {
                        System.out.print("º ");
                    }
                }
                else {
                    System.out.print("X ");
                }
            }
            System.out.println(" ");
        }
    }
}
