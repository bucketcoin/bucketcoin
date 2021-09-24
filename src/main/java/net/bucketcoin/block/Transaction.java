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

package net.bucketcoin.block;

import lombok.Getter;

import com.google.gson.GsonBuilder;
import net.bucketcoin.message.Message;
import net.bucketcoin.wallet.Wallet;

import java.io.Serializable;

public class Transaction implements Serializable, Blockable {

    @Getter
    private final double bckt;
    @Getter
    private final String sender;
    @Getter
    private final String recipient;
    @Getter
    private final double GasFee;

    /**
     * @see Message#Message(Wallet, Wallet, double, double)
     */
    public Transaction(double bckt, String sender, String recipient, double gas_fee) {
        this.bckt = bckt;
        this.sender = sender;
        this.recipient = recipient;
        this.GasFee = gas_fee;
    }

    /**
     * Returns a JSON representation of this {@linkplain Transaction}
     */
    @Override
    public String toString() {
        var g = new GsonBuilder().create();
        return g.toJson(this, Transaction.class);
    }
}
