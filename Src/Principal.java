//import java.io.File;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
//import java.lang.NumberFormatException;
public class Principal{
    public static void main(String[] args)throws Exception{
        FileOutputStream arquivo = new FileOutputStream("Dados.db");
        DataOutputStream escritor = new DataOutputStream(arquivo);
        int dado = 10;
        escritor.writeInt(dado);
        escritor.writeBytes("Algo existente");
        //RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
        //String algo = "Algo";
        //byte[] binario = algo.getBytes(StandardCharsets.UTF_8);
        //raf.close();
        escritor.close();
        arquivo.close();
        Scanner scan = new Scanner(System.in);
        scan.close();
    }
}
