/*
 *    Copyright 2021 The Bucketcoin Authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.bucketcoin.wallet;

import lombok.SneakyThrows;
import net.bucketcoin.util.CryptoResources;
import net.bucketcoin.crypto.state.StateTrie;
import net.bucketcoin.message.Message;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.*;

public final class Wallet {

    public final PublicKey publicKey;
    private final PrivateKey privateKey;

    @SneakyThrows
    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator k = CryptoResources.getStandardKeyPairGenerator();
        KeyPair keyPair = k.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    @Contract(" -> new")
    public @NotNull String getAddress() {
        return DigestUtils.shaHex(publicKey.toString());
    }

    public Key getPrivateKey() {
        return privateKey;
    }

    @SneakyThrows
    public void sendMessage(@NotNull Message message) {
        message.send();
    }

    public byte[] asBytes() {
        return getAddress().getBytes(StandardCharsets.ISO_8859_1);
    }

    public boolean hasEnough(double price) {
        var a = StateTrie.queryAddress(getAddress().getBytes(StandardCharsets.ISO_8859_1));
        return price <= a.balance();
    }

}
