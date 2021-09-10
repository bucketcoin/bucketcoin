module net.bucketcoin {

    exports net.bucketcoin.node;
    exports net.bucketcoin.block;
    exports net.bucketcoin.message;
    exports net.bucketcoin.wallet;
    exports net.bucketcoin.contract.interfaces;

    opens net.bucketcoin.block to com.google.gson;
    opens net.bucketcoin.message to com.google.gson;

    requires lombok;
    requires org.jetbrains.annotations;
    requires peerbase;
    requires java.sql;
    requires com.google.gson;
    requires commons.codec;
    requires com.sun.jna;
    requires java.compiler;
    requires ecj;
    requires junit;
    requires com.thoughtworks.qdox;
    requires leveldb;
    requires leveldb.api;

}