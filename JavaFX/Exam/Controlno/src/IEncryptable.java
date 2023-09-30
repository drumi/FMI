public interface IEncryptable {

    String encrypt(String plainText, String cipher);
    String decrypt(String cipherText, String cipher);
}
