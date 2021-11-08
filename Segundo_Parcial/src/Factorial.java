import javax.swing.*;
import java.math.BigInteger;

public class Factorial {
    public static void main(String[] args) {
        int numero= Integer.parseInt(JOptionPane.showInputDialog("Ingrese un numero: "));
        String[] memorizacion = (factorial(numero));
        System.out.println(memorizacion[memorizacion.length-1]);

    }

    public static String[] factorial(int numero)
    {
        String[] memorizacion = new String[numero+1];
        memorizacion[0]=1+"";
        for(int i=1; i<=numero ; i++)
        {
            BigInteger entero = new BigInteger(i + "");
            memorizacion[i] = entero.multiply(new BigInteger(memorizacion[i-1])) + "";
        }
        return memorizacion;
    }
}