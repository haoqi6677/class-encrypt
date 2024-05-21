package fuck.world.custom;

import java.io.ByteArrayOutputStream;

public class CfOutputStream extends ByteArrayOutputStream {
    public void u1(byte b) {
        write((int) b);
    }

    public void u2(short s) {
        write((s >> 8) & 0xff);
        write(s & 0xff);
    }

    public void u2(int s) {
        write((s >> 8) & 0xff);
        write(s & 0xff);
    }

    public void u4(int i) {
        write((i >> 24) & 0xff);
        write((i >> 16) & 0xff);
        write((i >> 8) & 0xff);
        write(i & 0xff);
    }

    public void array(byte[] a) {
        write(a, 0, a.length);
    }

    public byte[] toByteArray() {
        return super.toByteArray();
    }
}
