public class CipherMethod {

    private IEncryptable callback;

    public CipherMethod(IEncryptable callback) {
        this.callback = callback;
    }

    public String encryptText(String plainText, String cipher) {
        Encryption encryption = new Encryption();
        return encryption.getMonoCipherMethod().encrypt(plainText, cipher);
    }

    public String decryptText(String cipherText, String cipher) {
        Encryption decryption = new Encryption();
        return decryption.getMonoCipherMethod().encrypt(cipherText, cipher);
    }
}
