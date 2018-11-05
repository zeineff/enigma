package enigma_machine;

public class Enigma{
    Rotor[] rotors;
    int[] plugboard = {1, 0, 3, 2, 5, 4, 7, 6, 9, 8, 11, 10, 13, 12, 15, 14, 17, 16, 19, 18, 20, 21, 22, 23, 24, 25};
    int[] reflector = Rotor.B;
    final int rotorSize;
    
    public Enigma(Rotor...rotors){
        if (rotors.length > 0){
            int size = rotors[0].wiring.length;
            
            for (int i = 1; i < rotors.length; i++)
                if (size != rotors[i].wiring.length)
                    System.out.println("Error: Rotor(s) differ in size");
        }
        
        this.rotors = rotors;
        rotorSize = rotors[0].wiring.length;
    }
    
    public void setRotors(int...positions){
        if (positions.length != rotors.length)
            System.out.println("Number of positions given differs from number of rotors");
        else
            for (int i = 0; i < rotors.length; i++)
                rotors[i].offset = positions[i];
    }
    
    public void rotate(){
        rotate(rotors.length - 1);
    }
    
    public void rotate(int rotor){
        Rotor r = rotors[rotor];
        
        if (r.rotate() && rotor != 0)
            rotate(rotor - 1);
    }
    
    public String encrypt(String msg){
        System.out.printf("\nStart encryption/decryption\n---------------------\n");
        System.out.printf("Message: %s\n", msg);
        
        msg = msg.toUpperCase().replaceAll(" ", "");
        
        System.out.printf("Reduced: %s\n\n", msg);
        
        int[] nums = new int[msg.length()];
        
        for (int i = 0; i < nums.length; i++)
            nums[i] = cipher(msg.charAt(i));
        
        char[] c = new char[nums.length + (nums.length - 1) / 4];
        
        for (int i = 0; i < (nums.length + 3) / 4; i++){
            for (int j = 0; j < 4; j++){
                int k = 5 * i + j;
                
                if (k < nums.length)
                    c[k] = (char) ('A' + nums[4 * i + j]);
                else
                    break;
            }
        }
        
        for (int i = 4; i < c.length; i += 5)
            c[i] = ' ';
        
        return new String(c);
    }
    
    public int cipher(char c){
        int n = plugboard[Main.mod(c - 'A', rotorSize)];
        
        System.out.printf("Plugboard: %c -> %c\n", c, (char) ('A' + n));
        System.out.printf("Rotors:    %c", (char) ('A' + n));
        
        for (int i = rotors.length - 1; i >= 0; i--){
            Rotor r = rotors[i];
            
            n = r.wiring[(n + r.offset) % rotorSize];
            
            System.out.printf(" -> %c", (char) ('A' + n));
        }
        
        System.out.print("\n");
        
        n = Main.mod(n - rotors[0].offset, rotorSize);
        
        System.out.printf("Reflector: %c -> ", (char) ('A' + n));
        n = reflector[n];
        System.out.printf("%c\n", (char) ('A' + n));
        
        System.out.printf("Reverse:   %c", (char) ('A' + n));
        
        for (Rotor r:rotors){
            n = Main.mod(r.inverse[n] - r.offset, rotorSize);
            
            System.out.printf(" -> %c", (char) ('A' + n));
        }
        
        rotate();
        
        System.out.printf("\nPlugboard: %c -> ", (char) ('A' + n));
        n = plugboard[n];
        System.out.printf("%c\n\n", (char) ('A' + n));
        
        return n;
    }
    
    public void print(){
        int rotorSize = rotors[0].wiring.length;
        
        for (int i = 0; i < rotors.length - 1; i++)
            System.out.printf(" %c     ", (char) ('A' + rotors[i].offset));
        
        System.out.printf(" %c\n", (char) ('A' + rotors[rotors.length - 1].offset));
        
        for (int i = 0; i < rotors[0].wiring.length; i++){
            Rotor r = rotors[0];
            
            System.out.printf("[%c", (char) ('A' + (r.wiring[(i + r.offset) % rotorSize])));
            
            for (int j = 1; j < rotors.length; j++){
                r = rotors[j];
                
                char letter = (char) ('A' + (r.wiring[(i + r.offset) % rotorSize]));
                
                System.out.printf("] <- [%c", letter);
            }
            
            System.out.printf("] < %c\n", (char) ('A' + i));
        }
    }
}
