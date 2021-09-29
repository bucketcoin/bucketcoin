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

import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.bucketcoin.message.Message;
import net.bucketcoin.wallet.Wallet;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class Trade implements Serializable, Blockable {

    @Getter
    private final List<?> toSend, toGet;
    @Getter
    private final String sender;
    @Getter
    private final String recipient;
    @Getter
    private final double GasFee;

    /**
     * @see Message#Message(Wallet, List, List, double)
     */
    public Trade(@NotNull List<?> toSend, @NotNull List<?> toGet, String sender, String recipient, double gas_fee) {
        this.toSend = toSend;
        this.toGet = toGet;
        this.sender = sender;
        this.recipient = recipient;
        this.GasFee = gas_fee;
    }


    @Override
    public String toString() {
        var g = new GsonBuilder().create();
        return g.toJson(this, Transaction.class);
    }

}
