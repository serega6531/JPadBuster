package ru.serega6531.oracle;

public interface Oracle {

    /**
     * @param encrypted text to encrypt
     * @return true on decryption without padding error, false otherwise
     */
    boolean tryDecrypt(byte[] encrypted);

}
