module net.bucketcoin {

    exports net.bucketcoin.node;
    exports net.bucketcoin.block;
    exports net.bucketcoin.message;
    exports net.bucketcoin.wallet;
    exports net.bucketcoin.contract.interfaces;

    requires lombok;
    requires org.jetbrains.annotations;
    requires peerbase;
    requires java.sql;
    requires com.google.gson;
    requires commons.codec;
    requires jcuda;
    requires jcurand;
    requires com.sun.jna;
    requires java.compiler;
    requires ecj;

}