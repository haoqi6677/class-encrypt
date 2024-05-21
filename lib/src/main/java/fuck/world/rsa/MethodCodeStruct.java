package fuck.world.rsa;

public class MethodCodeStruct {

    //u1-被加密flag
    public static int ENCRYPT_FLAG = 0xfd;

    private int count;
    //[{
    // u2 length
    // info
    // }]
    private byte[] code;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    public byte[] toByte() {
        byte[] b = new byte[3 + code.length];
        b[0] = (byte) ENCRYPT_FLAG;
        b[1] = (byte) (count >> 8);
        b[2] = (byte) (count & 0xff);
        int i = 3;
        for (byte aByte : code) {
            b[i] = aByte;
            i++;
        }
        return b;
    }
}
