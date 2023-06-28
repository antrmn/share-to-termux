package com.antrmn.sharetotermux;

import static android.content.Intent.getIntent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class ShareActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        // Get intent, action and MIME type
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (intent.ACTION_PROCESS_TEXT.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleProcessText(intent);
            }
        }
    }

    void handleSendImage(Intent intent1) {
        Uri imageUri = (Uri) intent1.getParcelableExtra(Intent.EXTRA_STREAM);
        Toast toast=Toast. makeText(getApplicationContext(),imageUri.toString(),Toast. LENGTH_SHORT);
        toast. setMargin(50,50);
        toast. show();
        if (imageUri != null) {
            Intent intent = new Intent();
            intent.setClassName("org.gnu.emacs", "org.gnu.emacs.EmacsOpenActivity");
            intent.setAction("android.intent.action.VIEW");
            intent.setData(imageUri);
            startActivity(intent);
        }
    }


    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            callTermux(sharedText);
        }
    }

    private void handleProcessText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
        if (sharedText != null) {
            callTermux(sharedText);
        }
    }

    private void callTermux(String param) {
        Intent intent = new Intent();
        intent.setClassName("com.termux", "com.termux.app.RunCommandService");
        intent.setAction("com.termux.RUN_COMMAND");
        intent.putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/termux-gui-dialog");
        intent.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", new String[]{"label", param});
        intent.putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home");
        intent.putExtra("com.termux.RUN_COMMAND_BACKGROUND", true);
        intent.putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0");
        startService(intent);
    }
}