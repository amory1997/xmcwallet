/*
 * Copyright (c) 2020 m2049r
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.m2049r.xmrwallet.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Nodes stolen from https://moneroworld.com/#nodes

@AllArgsConstructor
public enum DefaultNodes {
    XMC1("node1.monero-classic.org:18081"),
    XMC2("node2.monero-classic.org:18081"),
    XMC3("node3.monero-classic.org:18081"),
    XMC4("node1.xmc-seed.com:18081"),
    XMC5("node2.xmc-seed.com:18081"),
    XMC6("node3.xmc-seed.com:18081"),
    XMC7("node1.xmc-seed.org:18081"),
    XMC8("node2.xmc-seed.org:18081"),
    XMC9("node3.xmc-seed.org:18081");

    @Getter
    private final String uri;
}
