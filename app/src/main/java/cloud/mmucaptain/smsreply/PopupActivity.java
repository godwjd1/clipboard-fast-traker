package cloud.mmucaptain.smsreply;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PopupActivity extends Activity {
    static final String EXTRA_SENDER = "sender";
    static final String EXTRA_BODY = "body";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setShowWhenLocked(true);
        setTurnScreenOn(true);

        String sender = getIntent().getStringExtra(EXTRA_SENDER);
        String body = getIntent().getStringExtra(EXTRA_BODY);
        if (sender == null) sender = "";
        if (body == null) body = "";
        final String to = sender;

        LinearLayout sheet = Ui.card(this);
        sheet.setBackground(Ui.rect(this, Ui.WHITE, 18, 0));

        sheet.addView(Ui.text(this, to, 14, true, Ui.MUTED));

        TextView original = Ui.text(this, body, 17, false, Ui.INK);
        original.setMaxLines(4);
        original.setLineSpacing(0, 1.2f);
        sheet.addView(original, Ui.lp(-1, -2, 6, this));

        final EditText reply = Ui.field(this, "답장", true);
        reply.setText(Prefs.compose(this, body));
        sheet.addView(reply, Ui.lp(-1, -2, 16, this));

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.END);

        TextView close = Ui.pill(this, "닫기", false, v -> finish());
        TextView copy = Ui.pill(this, "복사", false, v -> {
            ClipboardManager cm = getSystemService(ClipboardManager.class);
            cm.setPrimaryClip(ClipData.newPlainText("reply", reply.getText().toString()));
            Toast.makeText(this, "복사했습니다", Toast.LENGTH_SHORT).show();
        });
        TextView send = Ui.pill(this, "보내기", true, v -> {
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode(to)));
            i.putExtra("sms_body", reply.getText().toString());
            startActivity(i);
            finish();
        });

        row.addView(close);
        LinearLayout.LayoutParams gap = new LinearLayout.LayoutParams(-2, -2);
        gap.leftMargin = Ui.dp(this, 8);
        row.addView(copy, gap);
        LinearLayout.LayoutParams gap2 = new LinearLayout.LayoutParams(-2, -2);
        gap2.leftMargin = Ui.dp(this, 8);
        row.addView(send, gap2);
        sheet.addView(row, Ui.lp(-1, -2, 16, this));

        setContentView(sheet);

        Window w = getWindow();
        w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        w.setGravity(Gravity.BOTTOM);
    }
}
