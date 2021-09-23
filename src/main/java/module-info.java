module net.bucketcoin {

    exports net.bucketcoin.node;
    exports net.bucketcoin.block;
    exports net.bucketcoin.message;
    exports net.bucketcoin.wallet;
    exports net.bucketcoin.contract;
    exports net.bucketcoin.exception;
    exports net.bucketcoin.runtime.exception;
    exports net.bucketcoin.runtime.event;
    exports net.bucketcoin.collections;
    exports net.bucketcoin.util;

    opens net.bucketcoin.block to com.google.gson;
    opens net.bucketcoin.message to com.google.gson;

    requires lombok;
    requires org.jetbrains.annotations;
    requires peerbase;
    requires java.sql;
    requires com.google.gson;
    requires commons.codec;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires com.sun.jna;
    requires java.compiler;
    requires ecj;
    requires junit;
    requires com.thoughtworks.qdox;
    requires leveldb;
    requires leveldb.api;
    requires commons.io;
    requires com.google.common;
    requires org.bouncycastle.provider;

}