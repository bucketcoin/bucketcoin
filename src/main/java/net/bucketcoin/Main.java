package net.bucketcoin;

import lombok.SneakyThrows;
import net.bucketcoin.message.SendBCKT;
import net.bucketcoin.wallet.Wallet;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        // CUDA.cudaMine(6942); // FIXME
        //for(Object x : System.getProperties().entrySet().toArray()) {
        //    System.out.println(x.toString());
        //}

        var a = new Wallet();
        var b = new Wallet();
        var c = new Wallet();
        new SendBCKT(a, b, 60, 1);
    }

}
