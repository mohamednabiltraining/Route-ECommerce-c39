package com.route.domain

import android.os.Environment
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class TokenEncryptionManager {
    private val keyStore =
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

    private val encryptCipher =
        Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry("secret", null) as? SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    "secret",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build(),
            )
        }.generateKey()
    }

    private fun encrypt(
        bytes: ByteArray,
        outputStream: OutputStream,
    ): ByteArray {
        val encryptedBytes = encryptCipher.doFinal(bytes)
        outputStream.use {
            it.write(encryptCipher.iv.size)
            it.write(encryptCipher.iv)
            it.write(encryptedBytes.size)
            it.write(encryptedBytes)
        }
        return encryptedBytes
    }

    private fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val ivSize = it.read()
            val iv = ByteArray(ivSize)
            it.read(iv)
            val encryptedByteSize = it.read()
            val encryptedByte = ByteArray(encryptedByteSize)
            it.read(encryptedByte)

            getDecryptCipherForIv(iv).doFinal(encryptedByte)
        }
    }

    fun encryptToken(token: String?) {
        if (token == null) return
        val encryptManager = TokenEncryptionManager()
        val tokenInByte = token.encodeToByteArray()

        val file =
            File(Environment.getExternalStorageDirectory(), "/" + File.separator + "secret.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        val fos = FileOutputStream(file)
        encryptManager.encrypt(
            bytes = tokenInByte,
            outputStream = fos,
        )
    }

    fun decryptToken(): String {
        val file =
            File(Environment.getExternalStorageDirectory(), "/" + File.separator + "secret.txt")
        return decrypt(
            FileInputStream(file),
        ).decodeToString()
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}
