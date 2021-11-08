import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Carga {
    public static class Objeto implements Comparable<Objeto>{
        String nombre;
        int peso;

        public Objeto(String nombre,int peso) {
            this.nombre = nombre;
            this.peso =peso;
        }

        @Override
        public int compareTo(Objeto o) {
            return Integer.compare(this.peso,o.peso);
        }
    }

    public static void main(String[] args) {
        int carguero = 700;
        int cantidad = 10;
        int[] peso_contenedores = new int[cantidad];
        String[] nombre_contenedores = new String[cantidad];
        Scanner teclado = new Scanner(System.in);
        List<Objeto> objetos = new ArrayList<>();
        for(int i=0; i<cantidad; i++)
        {
            System.out.println("Digite nombre del contenedor "+(i+1));
            String nombre = teclado.nextLine();
            nombre_contenedores[i] = nombre;
            System.out.println("Digite peso del contenedor"+(i+1));
            int peso = Integer.parseInt(teclado.nextLine());
            peso_contenedores[i] = peso;
            Objeto contendedor = new Objeto(nombre,peso);
            objetos.add(contendedor);
        }

        for (Objeto value : objetos) {
            System.out.println(value.nombre + " " + value.peso);
        }

        Collections.sort(objetos);
        ArrayList<Objeto>contenedores=new ArrayList<>();
        greedyCarga(objetos,carguero,contenedores);

        System.out.println("Estos son los contenedores cogidos");
        for (Objeto objeto : contenedores) {
            System.out.println(objeto.nombre + " " + objeto.peso);
        }
    }

    private static void greedyCarga(List<Objeto> objetos, int carguero, ArrayList<Objeto> contenedores) {
        int auxPeso=0;
        while ((auxPeso<carguero)&&(!objetos.isEmpty())){
            for (int i = 0; i <objetos.size() ; i++) {
                if(auxPeso+ objetos.get(i).peso<=carguero){
                    System.out.println(" Añado contenedor: " + objetos.get(i).nombre);
                    contenedores.add(objetos.get(i));
                    auxPeso=auxPeso+ objetos.get(i).peso;
                }
                else{
                    System.out.println(" Elimino contenedor: " + objetos.get(i).nombre);
                }
                objetos.remove(i);
            }
        }
    }
}