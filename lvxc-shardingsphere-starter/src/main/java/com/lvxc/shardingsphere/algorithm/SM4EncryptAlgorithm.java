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

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.google.common.base.Strings;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.shardingsphere.encrypt.api.context.EncryptContext;
import org.apache.shardingsphere.encrypt.api.encrypt.standard.StandardEncryptAlgorithm;
import org.apache.shardingsphere.encrypt.exception.algorithm.EncryptAlgorithmInitializationException;
import org.apache.shardingsphere.infra.util.exception.ShardingSpherePreconditions;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

/**
 * SM4 encrypt algorithm.
 */
public final class SM4EncryptAlgorithm implements StandardEncryptAlgorithm<Object, String> {
    public static final String SM4_KEY = "sm4-key-value";

    private static final String DIGEST_ALGORITHM_NAME = "digest-algorithm-name";

    public static SymmetricCrypto crypto;

    @Override
    public void init(final Properties props) {
        byte[] secretKey = createSecretKey(props);
        crypto = new SymmetricCrypto("SM4/ECB/PKCS5Padding", secretKey);
    }

    private byte[] createSecretKey(final Properties props) {
        String sm4Key = props.getProperty(SM4_KEY);
        ShardingSpherePreconditions.checkState(!Strings.isNullOrEmpty(sm4Key),
                () -> new EncryptAlgorithmInitializationException(getType(), String.format("%s can not be null or empty", SM4_KEY)));
        String digestAlgorithm = props.getProperty(DIGEST_ALGORITHM_NAME, MessageDigestAlgorithms.SHA_1);
        return Arrays.copyOf(DigestUtils.getDigest(digestAlgorithm.toUpperCase()).digest(sm4Key.getBytes(StandardCharsets.UTF_8)), 16);
    }

    @Override
    public String encrypt(final Object plainValue, final EncryptContext encryptContext) {
        if (null == plainValue) {
            return null;
        }
        return crypto.encryptHex(String.valueOf(plainValue), StandardCharsets.UTF_8);
    }

    @Override
    public Object decrypt(final String cipherValue, final EncryptContext encryptContext) {
        if (null == cipherValue) {
            return null;
        }
        return crypto.decryptStr(cipherValue);
    }

    @Override
    public String getType() {
        return "SM4";
    }
}
