package skynet;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Crypt {

    private String seed = "sKyNNNet1337";
    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public Crypt(){
        encryptor.setPassword(seed);
    }

    public String encrypt(String text){
        return encryptor.encrypt(text);
    }

    public String decrypt(String text){
        return encryptor.decrypt(text);
    }
}