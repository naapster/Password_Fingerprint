package com.example.password_fingerprint;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ShowNote extends AppCompatActivity {

    EditText editNew;
    TextView textNote;
    TextView textNote2;
    Button buttonchange2;
    String decoded;
    String tekst;
    private byte[] szyfr;
    byte [] decodedData;
    String unencryptedString;
    String secret;
    String loadFromPref;
    String loadFromPref2;
    String ivString;
    byte [] iv;
    byte [] iv2;
    byte[] encryptedInfo;
    static final String KEY_NAME = "yourKey";
    static Cipher cipher;
    public static KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher ciper;
    private Executor executor;
    static SharedPreferences utilsNote;
    static SharedPreferences utilsIv;
    private SecretKey key;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        tekst = "lol";
        editNew = findViewById(R.id.editText);
        textNote = findViewById(R.id.textView3);
        textNote2 = findViewById(R.id.textView4);
        buttonchange2 = findViewById(R.id.button);

        utilsIv = getSharedPreferences("IV", MODE_PRIVATE);
        utilsNote = getSharedPreferences("Note", MODE_PRIVATE);

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {

            if (!keyStore.containsAlias(KEY_NAME)) {
                textNote.setText("nie bylo");
                keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                keyGenerator.init(new
                        KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setUserAuthenticationRequired(false)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build());
                keyGenerator.generateKey();
                cipher = Cipher.getInstance(
                        KeyProperties.KEY_ALGORITHM_AES + "/"
                                + KeyProperties.BLOCK_MODE_GCM + "/"
                                + KeyProperties.ENCRYPTION_PADDING_NONE);
                SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                        null);
                cipher.init(Cipher.ENCRYPT_MODE, key);

            } else {
                textNote.setText("byl");
                cipher = Cipher.getInstance(
                        KeyProperties.KEY_ALGORITHM_AES + "/"
                                + KeyProperties.BLOCK_MODE_GCM + "/"
                                + KeyProperties.ENCRYPTION_PADDING_NONE);
                ;
                SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                        null);
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        buttonchange2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String text = editNew.getText().toString();
                try {
                    keyStore.load(null);
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    key = (SecretKey) keyStore.getKey(KEY_NAME, null);
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                }
                try {
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                iv2 = cipher.getIV ();
                ivString = Base64.getEncoder().encodeToString(iv2);
                SharedPreferences.Editor editor = utilsIv.edit();
                editor.putString("IV", ivString);
                editor.commit();

                try {
                    szyfr = cipher.doFinal(text.getBytes("UTF-8"));
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                secret = Base64.getEncoder().encodeToString(szyfr);
                SharedPreferences.Editor editor2 = utilsNote.edit();
                editor2.putString("Note", secret);
                editor2.commit();
                Toast.makeText(ShowNote.this, "You changed your note", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowNote.this, MainActivity.class);
                startActivity(intent);
            }
        });

        loadFromPref = utilsNote.getString("Note","");
        loadFromPref2 = utilsIv.getString("IV","");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            iv = Base64.getDecoder().decode(loadFromPref2);
        }
        final GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        try {
            key = (SecretKey) keyStore.getKey(KEY_NAME,null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                decodedData =cipher.doFinal(Base64.getDecoder().decode(loadFromPref));
            }
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        try {
            unencryptedString = new String(decodedData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        textNote.setText(unencryptedString);

        String loadFromPref = utilsNote.getString("Note","");
        textNote2.setText(loadFromPref);
    }
}