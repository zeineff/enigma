package enigma_machine;

import java.util.Random;

public class Main{
    public static void main(String[] args){
        Enigma e = new Enigma(Rotor.I, Rotor.II, Rotor.III);
        e.print();
        
        String msg = "The quick";
        
        msg = e.encrypt(msg);
        System.out.printf("Output: %s\n", msg);
        
        e.setRotors(0, 0, 0);
        msg = e.encrypt(msg);
        System.out.println(msg);
    }
    
    public static int mod(int a, int b){
        if (a < 0)
            a += (-a / b + 1) * b;
        
        return a % b;
    }
    
    public static Rotor randRotor(int size, boolean preventSame){
        Random rand = new Random();
        int[] wiring = new int[size];
        
        for (int i = 0; i < size; i++)
            wiring[i] = i;
        
        int x = size + (preventSame ? -1 : 0);
        
        for (int i = 0; i < size; i++){
            int a = wiring[i];
            int b = rand.nextInt(x);
            
            if (preventSame)
                while (wiring[b] == i || wiring[i] == b)
                    b++;
            
            wiring[i] = wiring[b];
            wiring[b] = a;
        }
        
        return new Rotor(wiring);
    }
    
    public static int[] makeReflector(int size){
        int[] reflector = new int[size];
        
        for (int i = 0; i < size; i++)
            reflector[i] = size - 1 - i;
        
        return reflector;
    }
    
    public static void print(String s){
        char[] a = s.toCharArray();
        
        int[] nums = new int[a.length];
        
        for (int i = 0; i < a.length; i++)
            nums[i] = a[i] - 'A';
        
        System.out.printf("%d", nums[0]);
        
        for (int i = 1; i < a.length; i++)
            System.out.printf(", %d", nums[i]);
        
        System.out.printf("\n");
    }
}
