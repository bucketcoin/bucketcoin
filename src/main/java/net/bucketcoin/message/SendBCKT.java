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

package net.bucketcoin.message;

import net.bucketcoin.block.Transaction;
import net.bucketcoin.util.CryptoResources;
import net.bucketcoin.exception.InsufficientBalanceException;
import net.bucketcoin.p2p.Broadcast;
import net.bucketcoin.wallet.Wallet;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SendBCKT extends Message {

    private final Wallet recipient;
    private final double bckt;
    private final double gas_fee;

    public SendBCKT(@NotNull Wallet recipient, double gas_fee, double bckt) {

        super(recipient, gas_fee, bckt);
        this.recipient = recipient;
        this.bckt = bckt;
        this.gas_fee = gas_fee;
    }

    @Override
    public void send(@NotNull Wallet sender) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InsufficientBalanceException {
        Transaction transaction = new Transaction(bckt, sender.getPublicKey().toString(), recipient.getPublicKey().toString(), gas_fee);
        String transaction_sha256 = DigestUtils.shaHex(transaction.toString());
        var c = CryptoResources.getStandardCipher();
        c.init(Cipher.ENCRYPT_MODE, sender.getPrivateKey());
        var signature = c.doFinal(transaction_sha256.getBytes(StandardCharsets.ISO_8859_1));
        if(!sender.hasEnough(bckt + (gas_fee * 0.000001))) {
            throw new InsufficientBalanceException();
        }
        Broadcast.transaction(transaction, sender.getPublicKey(), signature); // network effect
    }
}
