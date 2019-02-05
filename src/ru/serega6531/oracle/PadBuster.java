package ru.serega6531.oracle;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class PadBuster {
    
    private int blockSize;
    private Oracle oracle;
    private byte[] iv = null;
    private int errorCorrectionLevel = 0;

    public PadBuster(int blockSize, Oracle oracle) {
        this.blockSize = blockSize;
        this.oracle = oracle;
    }

    public PadBuster(int blockSize, Oracle oracle, byte[] iv) {
        this.blockSize = blockSize;
        this.oracle = oracle;
        this.iv = iv;
    }

    public byte[] attack(byte[] enc) {
        enc = ArrayUtils.addAll(iv, enc);
        byte[] decoded = new byte[enc.length];
        Arrays.fill(decoded, (byte) '-');

        for (int i = (int) Math.floor((double) enc.length / blockSize); i > 1; i--) {
            byte[] copy = Arrays.copyOf(enc, i * blockSize);
            try {
                byte[] block = bustPadding(i - 1, copy);
                System.arraycopy(block, 0, decoded, 16 * (i - 1), blockSize);
            } catch (IllegalStateException e) {
                if(errorCorrectionLevel == 2) {
                    throw e;
                }

                System.out.println("Resetting");
                errorCorrectionLevel++;
                i++;
            }
        }

        byte[] result = removePadding(decoded);
        if(iv != null) {
            result = Arrays.copyOfRange(result, 16, result.length);
        }
        return result;
    }

    private byte[] removePadding(byte[] dec) {
        int count = dec[dec.length - 1];
        return Arrays.copyOf(dec, dec.length - count);
    }

    private byte[] bustPadding(int blockNum, byte[] enc) {
        byte[] i2 = new byte[blockSize];
        byte[] p2 = new byte[blockSize];

        for (int i = 0; i < blockSize; i++) {
            for (int j = enc.length - i; j < enc.length; j++) {
                enc[j - blockSize] = (byte) ((i + 1) ^ i2[j % blockSize]);
            }

            byte c1 = enc[enc.length - 1 - i - blockSize];
            byte p2d = (byte) (i + 1);
            byte c1d = guessByte(enc, blockNum, enc.length - 1 - i - blockSize);
            errorCorrectionLevel = 0;

            i2[blockSize - i - 1] = (byte) (c1d ^ p2d);
            p2[blockSize - i - 1] = (byte) (c1 ^ i2[blockSize - i - 1]);
            for (int j = (iv != null ? blockSize : 0); j < enc.length - i - 1; j++) {
                System.out.print('-');
            }

            for (int j = blockSize - i - 1; j < blockSize; j++) {
                System.out.print((char) p2[j]);
            }
            System.out.println();
        }

        return p2;
    }

    private byte guessByte(byte[] enc, int blockNum, int place) {
        byte[] copy = Arrays.copyOf(enc, enc.length);
        int errorCorrection = errorCorrectionLevel;

        for (int i = blockSize * (blockNum - 1); i < place; i++) {
            copy[i] = 0;
        }

        for (int i = 0; i < 256; i++) {
            copy[place] = (byte) i;

            if(oracle.tryDecrypt(copy)) {
                if(errorCorrection == 0) {
                    return (byte) i;
                } else {
                    errorCorrection--;
                }
            }
        }

        throw new IllegalStateException("Could not guess byte");
    }

}
