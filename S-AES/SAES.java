import java.util.Objects;
import java.util.Random;

public class SAES{

    public static String xor(String a, String b) {//异或
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i)) {
                sb.append("0");
            } else {
                sb.append("1");
            }
        }
        return new String(sb);
    }

    public static String toBinary(int num, int n) {//转n位二进制字符串
        String binary = Integer.toBinaryString(num);
        return String.format("%" + n + "s", binary).replace(' ', '0');
    }

    public static int[][] S_box = new int[][] {
            {9, 4, 10, 11},
            {13, 1, 8, 5},
            {6, 2, 0, 3},
            {12, 14, 15, 7}};

    public static int[][] IS_box = new int[][]{
            {10, 5, 9, 11},
            {1, 7, 8, 15},
            {6, 0, 2, 3},
            {12, 4, 13, 14}};

    public static String[] NS(String[] m){//半字节代替
        String[] ns =new String[4];
        for(int i=0;i<4;i++){
            int a = 2*(m[i].charAt(0)-'0')+(m[i].charAt(1)-'0');
            int b = 2*(m[i].charAt(2)-'0')+(m[i].charAt(3)-'0');
            ns[i]=toBinary(S_box[a][b],4);
        }
        return ns;
    }

    public static String[] INS(String[] m){//逆半字节代替
        String[] ins =new String[4];
        for(int i=0;i<4;i++){
            int a = 2*(m[i].charAt(0)-'0')+(m[i].charAt(1)-'0');
            int b = 2*(m[i].charAt(2)-'0')+(m[i].charAt(3)-'0');
            ins[i]=toBinary(IS_box[a][b],4);
        }
        return ins;
    }

    public static String[] SR(String[] m){//行移位（逆行移位）
        String[] sr =new String[4];
        sr[0]=m[0];
        sr[1]=m[1];
        sr[2]=m[3];
        sr[3]=m[2];
        return sr;
    }

    public static int[] box4 = new int[] {0,4,8,12,3,7,11,15,6,2,14,10,5,1,13,9};
    public static int[] box2 = new int[] {0,2,4,6,8,10,12,14,3,1,7,5,11,9,15,13};
    public static int[] box9 = new int[] {0,9,1,8,2,11,3,10,4,13,5,12,6,15,7,14};

    public static String[] MC(String[] m){//列混淆
        String[] mc = new String[4];
        mc[0]=xor(m[0],toBinary(box4[Integer.parseInt(m[2],2)],4));
        mc[1]=xor(m[1],toBinary(box4[Integer.parseInt(m[3],2)],4));
        mc[2]=xor(m[2],toBinary(box4[Integer.parseInt(m[0],2)],4));
        mc[3]=xor(m[3],toBinary(box4[Integer.parseInt(m[1],2)],4));
        return mc;
    }

    public static String[] IMC(String[] m){//逆列混淆
        String[] imc = new String[4];
        imc[0]=xor(toBinary(box9[Integer.parseInt(m[0],2)],4),toBinary(box2[Integer.parseInt(m[2],2)],4));
        imc[1]=xor(toBinary(box9[Integer.parseInt(m[1],2)],4),toBinary(box2[Integer.parseInt(m[3],2)],4));
        imc[2]=xor(toBinary(box9[Integer.parseInt(m[2],2)],4),toBinary(box2[Integer.parseInt(m[0],2)],4));
        imc[3]=xor(toBinary(box9[Integer.parseInt(m[3],2)],4),toBinary(box2[Integer.parseInt(m[1],2)],4));
        return imc;
    }

    public static String[] RCON = new String[] {"10000000","00110000"};

    public static String g(String w, int n){//g函数
        String N0 = w.substring(0,4);
        String N1 = w.substring(4,8);
        int a = 2*(N0.charAt(0)-'0')+(N0.charAt(1)-'0');
        int b = 2*(N0.charAt(2)-'0')+(N0.charAt(3)-'0');
        int x = 2*(N1.charAt(0)-'0')+(N1.charAt(1)-'0');
        int y = 2*(N1.charAt(2)-'0')+(N1.charAt(3)-'0');
        String g=toBinary(S_box[x][y],4)+toBinary(S_box[a][b],4);
        return xor(g,RCON[n-1]);
    }

    public static String encrypt(String plaintext,String key) {//加密
        String w0 = key.substring(0,8);
        String w1 = key.substring(8,16);
        String w2 = xor(w0,g(w1,1));
        String w3 = xor(w2,w1);
        String w4 = xor(w2,g(w3,2));
        String w5 = xor(w4,w3);
        String ciphertext1 = xor(plaintext,w0+w1);
        String[] m1 = new String[] {ciphertext1.substring(0,4),ciphertext1.substring(8,12),ciphertext1.substring(4,8),ciphertext1.substring(12,16)};
        String[] m2 = MC(SR(NS(m1)));
        String ciphertext2 = m2[0]+m2[2]+m2[1]+m2[3];
        ciphertext2 = xor(ciphertext2,w2+w3);
        String[] m3 = new String[] {ciphertext2.substring(0,4),ciphertext2.substring(8,12),ciphertext2.substring(4,8),ciphertext2.substring(12,16)};
        String[] m4 = SR(NS(m3));
        String ciphertext3 = m4[0]+m4[2]+m4[1]+m4[3];
        ciphertext3 = xor(ciphertext3,w4+w5);
        return ciphertext3;
    }

    public static String decrypt(String ciphertext,String key) {//解密
        String w0 = key.substring(0,8);
        String w1 = key.substring(8,16);
        String w2 = xor(w0,g(w1,1));
        String w3 = xor(w2,w1);
        String w4 = xor(w2,g(w3,2));
        String w5 = xor(w4,w3);
        String plaintext1 = xor(ciphertext,w4+w5);
        String[] m1 = new String[] {plaintext1.substring(0,4), plaintext1.substring(8,12), plaintext1.substring(4,8), plaintext1.substring(12,16)};
        String[] m2 = INS(SR(m1));
        String plaintext2 = m2[0]+m2[2]+m2[1]+m2[3];
        plaintext2 = xor(plaintext2,w2+w3);
        String[] m3 = new String[] {plaintext2.substring(0,4), plaintext2.substring(8,12), plaintext2.substring(4,8), plaintext2.substring(12,16)};
        String[] m4 = INS(SR(IMC(m3)));
        String plaintext3 = m4[0]+m4[2]+m4[1]+m4[3];
        plaintext3 = xor(plaintext3,w0+w1);
        return plaintext3;
    }

    public static String ASCII_encrypt(String plaintext,String key){//ASCII字符加密
        if(plaintext.length() % 2 != 0 ){
            return "明文格式不符合要求，要求字符数为大于2的偶数。";
        }
        String[] m = new String[plaintext.length()/2];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plaintext.length()/2; i++) {
            m[i] = toBinary(plaintext.charAt(2 * i), 8) + toBinary(plaintext.charAt(2 * i + 1), 8);
            System.out.println(m[i]);
            m[i] = encrypt(m[i],key);
            sb.append((char) Integer.parseInt(m[i].substring(0, 8), 2));
            sb.append((char) Integer.parseInt(m[i].substring(8, 16), 2));
        }
        return new String(sb);
    }

    public static String ASCII_decrypt(String ciphertext, String key){//ASCII字符解密
        if(ciphertext.length() % 2 != 0 ){
            return "密文格式不符合要求，要求字符数为大于2的偶数。";
        }
        String[] m = new String[ciphertext.length()/2];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ciphertext.length()/2; i++) {
            m[i] = toBinary(ciphertext.charAt(2 * i), 8) + toBinary(ciphertext.charAt(2 * i + 1), 8);
            m[i] = decrypt(m[i],key);
            sb.append((char) Integer.parseInt(m[i].substring(0, 8), 2));
            sb.append((char) Integer.parseInt(m[i].substring(8, 16), 2));
        }
        return new String(sb);
    }

    public static String Double_encrypt(String plaintext,String key){//双重加密
        if(key.length() != 32){
            return "密钥格式不符合要求，要求位数为32。";
        }
        String key1 = key.substring(0,16);
        String key2 = key.substring(16,32);
        String ciphertext = encrypt(plaintext,key1);
        ciphertext = encrypt(ciphertext,key2);
        return ciphertext;
    }

    public static String Double_decrypt(String ciphertext,String key){//双重解密
        if(key.length() != 32){
            return "密钥格式不符合要求，要求位数为32。";
        }
        String key1 = key.substring(0,16);
        String key2 = key.substring(16,32);
        String plaintext = decrypt(ciphertext,key2);
        plaintext = decrypt(plaintext,key1);
        return plaintext;
    }

    public static String Attack(String plaintext,String ciphertext){//中间相遇攻击
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 65536; i++){
            String key = toBinary(i,16);
            if(Objects.equals(plaintext, decrypt(ciphertext, key))){
                sb.append(key).append(System.lineSeparator());
            }
        }
        return new String(sb);
    }

    public static String Triple_encrypt(String plaintext,String key){//三重加密
        if(key.length() != 48){
            return "密钥格式不符合要求，要求位数为48。";
        }
        String key1 = key.substring(0,16);
        String key2 = key.substring(16,32);
        String key3 = key.substring(32,48);
        String ciphertext = encrypt(plaintext,key1);
        ciphertext = encrypt(ciphertext,key2);
        ciphertext = encrypt(ciphertext,key3);
        return ciphertext;
    }

    public static String Triple_decrypt(String ciphertext, String key){//三重解密
        if(key.length() != 48){
            return "密钥格式不符合要求，要求位数为48。";
        }
        String key1 = key.substring(0,16);
        String key2 = key.substring(16,32);
        String key3 = key.substring(32,48);
        String plaintext = decrypt(ciphertext,key3);
        plaintext = decrypt(plaintext,key2);
        plaintext = decrypt(plaintext,key1);
        return plaintext;
    }

    public static String CBC_encrypt(String plaintext,String key){
        if(plaintext.length() % 16 !=0){
            return "明文格式不符合要求，要求位数为16的倍数。";
        }
        StringBuilder ciphertext = new StringBuilder();
        Random random = new Random();
        int randomNumber = random.nextInt(65536);
        String ov = toBinary(randomNumber,16);
        String[] ptext = new String[plaintext.length()/16];
        for(int i = 0; i < plaintext.length()/16; i++){
            ptext[i] = plaintext.substring(16*i,16*(i+1));
        }
        String[] ctext = new String[plaintext.length()/16];
        ctext[0] = xor(ptext[0],ov);
        ctext[0] = encrypt(ctext[0],key);
        ciphertext.append(ctext[0]);
        for(int i = 1; i < plaintext.length()/16; i++){
            ctext[i] = xor(ptext[i],ctext[i-1]);
            ctext[i] = encrypt(ctext[i],key);
            ciphertext.append(ctext[i]);
        }
        return "初始向量为"+ov+"  密文为"+ new String(ciphertext);
    }

    public static String CBC_decrypt(String ciphertext,String key,String ov){
        if(ciphertext.length() % 16 !=0){
            return "密文格式不符合要求，要求位数为16的倍数。";
        }
        StringBuilder plaintext = new StringBuilder();
        String[] ctext = new String[ciphertext.length()/16];
        for(int i = 0; i < ciphertext.length()/16; i++){
            ctext[i] = ciphertext.substring(16*i,16*(i+1));
        }
        String[] ptext = new String[ciphertext.length()/16];
        ptext[0] = decrypt(ctext[0],key);
        ptext[0] = xor(ptext[0],ov);
        plaintext.append(ptext[0]);
        for(int i = 1; i < ciphertext.length()/16; i++){
            ptext[i] = decrypt(ctext[i],key);
            ptext[i] = xor(ptext[i],ctext[i-1]);
            plaintext.append(ptext[i]);
        }
        return new String(plaintext);
    }
}