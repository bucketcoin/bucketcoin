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

import lombok.Getter;
import net.bucketcoin.exception.InsufficientBalanceException;
import net.bucketcoin.wallet.Wallet;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

public abstract class Message {

    @Getter
    private final Wallet sender;
    @Getter
    private final Wallet recipient;
    private List<?> toSend;
    private List<?> toGet;

    private final double gas_fee;
    @Getter
    private double bckt;

    /**
     * A message to send to the recipient.
     * @param sender The sender of the message
     * @param recipient The message recipient to deliver the message to.
     * @param gas_fee The gas fee (in 0.00000??? BCKT) paid to validate the message. The higher it is, the higher priority your {@code Message} gets.
     */
    public Message(@NotNull Wallet sender, @NotNull Wallet recipient, double gas_fee) {
        this.sender = sender;
        this.recipient = recipient;
        this.gas_fee = gas_fee;
    }

    /**
     * A message to send to the recipient.
     * @param sender The sender of the message
     * @param recipient The message recipient to deliver the message to.
     * @param gas_fee The gas fee (in 0.00000??? BCKT) paid to validate the message. The higher it is, the higher priority your {@code Message} gets.
     * @param bckt The BCKT that will be transferred to the recipient.
     */
    public Message(@NotNull Wallet sender, @NotNull Wallet recipient, double gas_fee, double bckt) {
        this.sender = sender;
        this.recipient = recipient;
        this.gas_fee = gas_fee;
        this.bckt = bckt;
    }

    /**
     * A message to send to the recipient.
     * @param toGet The {@link Object}s the request sender wishes to take.
     * @param toSend The {@link Object}s the request sender wishes to give.
     * @param sender The sender of the message
     * @param recipient The message recipient to deliver the message to.
     * @param gas_fee The gas fee (in 0.00000??? BCKT) paid to validate the message. The higher it is, the higher priority your {@code Message} gets.
     */
    public Message(@NotNull Wallet sender, @NotNull Wallet recipient, List<?> toSend, List<?> toGet, double gas_fee) {
        this.sender = sender;
        this.recipient = recipient;
        this.toSend = toSend;
        this.toGet = toGet;
        this.gas_fee = gas_fee;
    }

    /**
     * Sends the Message to the network.
     */
    public abstract void send() throws NoSuchPaddingException,
                                       NoSuchAlgorithmException,
                                       InvalidKeyException,
                                       IllegalBlockSizeException,
                                       BadPaddingException,
                                       NoSuchProviderException,
                                       InsufficientBalanceException;

}
