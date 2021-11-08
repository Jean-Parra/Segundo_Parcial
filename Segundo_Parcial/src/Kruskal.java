import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Kruskal {
    static final int MAX = 100;
    static int[] padre = new int[MAX];
    static void Inicializacion(int n) {
        for (int i = 1; i <= n; ++i) padre[i] = i;
    }

    static int Encontrar(int x) { //Método para encontrar la raiz del vértice
        return (x == padre[x]) ? x : (padre[x] = Encontrar(padre[x]));
    }

    static void Union(int x, int y) { //unir 2 componentes
        padre[Encontrar(x)] = Encontrar(y);
    }

    static boolean iguales(int x, int y) {
        return Encontrar(x) == Encontrar(y);
    }

    static int V, A;

    static class Arista implements Comparator<Arista> {
        int origen;
        int destino;
        int peso;
        Arista() {
        }
        @Override
        public int compare(Arista N1, Arista N2) { //Comparar el peso para organizar
            return N1.peso - N2.peso;
        }
    }

    static Arista[] arista = new Arista[MAX];
    static Arista[] Minima = new Arista[MAX];
    static void ExpansionMinima() {
        int origen, destino, peso;
        int total = 0;
        int numAristas = 0;
        Inicializacion(V);
        Arrays.sort(arista, 0, A, new Arista());
        for (int i = 0; i < A; ++i) {
            origen = arista[i].origen;
            destino = arista[i].destino;
            peso = arista[i].peso;
            if (!iguales(origen, destino)) {
                total += peso;
                Minima[numAristas++] = arista[i];
                Union(origen, destino);
            }
        }
        if (V - 1 != numAristas) {
            System.out.println("No es valido");
            return;
        }
        for (int i = 0; i < numAristas; ++i)
            System.out.printf("( %d , %d ) : %d\n", Minima[i].origen, Minima[i].destino, Minima[i].peso);
        System.out.printf("El costo minimo es : %d\n", total);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner( System.in );
        V = sc.nextInt(); A = sc.nextInt();
        for( int i = 0 ; i < A ; ++i ){
            arista[i] = new Arista();
            arista[i].origen = sc.nextInt();
            arista[i].destino = sc.nextInt();
            arista[i].peso = sc.nextInt();
        }
        ExpansionMinima();
    }
}