/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lvxc.shardingsphere.algorithm;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.SM3;
import org.apache.shardingsphere.encrypt.api.context.EncryptContext;
import org.apache.shardingsphere.encrypt.api.encrypt.assisted.AssistedEncryptAlgorithm;

import java.util.Properties;

/**
 * MD5 assisted encrypt algorithm.
 */
public final class SM3AssistedEncryptAlgorithm implements AssistedEncryptAlgorithm<Object, String> {

    private static final String SALT_KEY = "salt";

    private String salt;

    private static SM3 sm3;

    @Override
    public void init(final Properties props) {
        this.salt = props.getProperty(SALT_KEY, "");
        this.sm3 = StrUtil.isBlank(this.salt) ? SmUtil.sm3() : SmUtil.sm3WithSalt(salt.getBytes());
    }

    @Override
    public String encrypt(final Object plainValue, final EncryptContext encryptContext) {
        return null == plainValue ? null : sm3.digestHex(plainValue.toString());
    }

    @Override
    public String getType() {
        return "SM3";
    }
}
