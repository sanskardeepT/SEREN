package com.seren.app.data.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object SecurityHelper {
    private const val KEY_ALIAS = "seren_db_key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val PREFS_NAME = "seren_sec_prefs"
    private const val ENCRYPTED_PASSPHRASE_KEY = "enc_db_passphrase"
    private const val IV_KEY = "db_passphrase_iv"
    private const val AES_MODE = "AES/GCM/NoPadding"

    @Synchronized
    fun getOrCreateDatabasePassphrase(context: Context): ByteArray {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val encryptedBase64 = prefs.getString(ENCRYPTED_PASSPHRASE_KEY, null)
        val ivBase64 = prefs.getString(IV_KEY, null)

        if (encryptedBase64 != null && ivBase64 != null) {
            try {
                val encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT)
                val iv = Base64.decode(ivBase64, Base64.DEFAULT)
                return decryptPassphrase(encryptedBytes, iv)
            } catch (e: Exception) {
                // If decryption fails (e.g. key invalidated), fallback to generating a new key
                Log.e("SecurityHelper", "Decryption of DB passphrase failed", e)
            }
        }

        // Generate a new random 32-byte (256-bit) passphrase
        val rawPassphrase = ByteArray(32)
        SecureRandom().nextBytes(rawPassphrase)

        try {
            val encryptionResult = encryptPassphrase(rawPassphrase)
            prefs.edit()
                .putString(ENCRYPTED_PASSPHRASE_KEY, Base64.encodeToString(encryptionResult.encryptedData, Base64.DEFAULT))
                .putString(IV_KEY, Base64.encodeToString(encryptionResult.iv, Base64.DEFAULT))
                .apply()
        } catch (e: Exception) {
            Log.e("SecurityHelper", "Encryption of new DB passphrase failed", e)
            // In case keystore encryption fails, return a secure random fallback (in-memory only for this session)
            return rawPassphrase
        }

        return rawPassphrase
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (secretKey != null) {
            return secretKey
        }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false) // Dynamic background operations allowed
            .build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    private fun encryptPassphrase(rawPassphrase: ByteArray): EncryptionResult {
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val encryptedData = cipher.doFinal(rawPassphrase)
        return EncryptionResult(encryptedData, cipher.iv)
    }

    private fun decryptPassphrase(encryptedData: ByteArray, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        return cipher.doFinal(encryptedData)
    }

    private class EncryptionResult(val encryptedData: ByteArray, val iv: ByteArray)
}
