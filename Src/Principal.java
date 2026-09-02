import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
//import java.lang.NumberFormatException;
public class Principal{
    public static void main(String[] args)throws Exception{
        File arquivo = new File("Dados.bin");
        if(!arquivo.exists()){
            arquivo.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
        String algo = "Algo";
        byte[] binario = algo.getBytes(StandardCharsets.UTF_8);
        //String resul = raf.readLine();
        /*if(resul != null && resul.compareTo(algo) == 0){
            System.out.println("Acesso negado");
        }*/
        //else{
            //raf.writeBytes(algo);
            //raf.writeBytes(System.lineSeparator());
            raf.writeBytes(Integer.toBinaryString(binario[0] & 0xFF) /*+ " "*/);
            raf.writeBytes(System.lineSeparator());
            raf.writeBytes(Integer.toBinaryString(binario[1] & 0xFF) /*+ " "*/);
            raf.writeBytes(System.lineSeparator());
            raf.writeBytes(Integer.toBinaryString(binario[2] & 0xFF) /*+ " "*/);
            raf.writeBytes(System.lineSeparator());
            raf.writeBytes(Integer.toBinaryString(binario[3] & 0xFF) /*+ " "*/);
            raf.writeBytes(System.lineSeparator());

            raf.seek(0);
            System.out.print((char)Integer.parseInt(raf.readLine(), 2) /*+ " "*/);
            System.out.print((char)Integer.parseInt(raf.readLine(), 2) /*+ " "*/);
            System.out.print((char)Integer.parseInt(raf.readLine(), 2) /*+ " "*/);
            System.out.println((char)Integer.parseInt(raf.readLine(), 2) /*+ " "*/);
            /*raf.seek(1);
            System.out.print(raf.read() + " ");
            raf.seek(2);
            System.out.print(raf.read() + " ");
            raf.seek(3);
            System.out.print(raf.read() + " ");
            raf.seek(4);
            System.out.print(raf.read() + " ");
            raf.seek(5);
            System.out.print(raf.read() + " ");
            raf.seek(6);
            System.out.print(raf.read() + " ");*/
            /*raf.seek(7);
            System.out.println(raf.read() + " ");*/
            //}
        raf.close();
        Scanner scan = new Scanner(System.in);
        scan.close();
    }
}
