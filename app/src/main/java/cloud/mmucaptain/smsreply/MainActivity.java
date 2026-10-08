package cloud.mmucaptain.smsreply;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private LinearLayout list;
    private TextView status;
    private EditText numberInput;
    private EditText templateInput;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Ui.PARCHMENT);
        scroll.setFillViewport(true);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = Ui.dp(this, 20);
        root.setPadding(pad, Ui.dp(this, 48), pad, Ui.dp(this, 48));
        scroll.addView(root);

        root.addView(Ui.title(this, "문자 답장"));
        root.addView(Ui.text(this, "지정한 번호에서 문자가 오면 답장 문구를 바로 띄웁니다.", 17, false, Ui.MUTED),
                Ui.lp(-1, -2, 8, this));

        status = Ui.text(this, "", 14, true, Ui.INK);
        root.addView(status, Ui.lp(-1, -2, 20, this));

        // 지정 번호 카드
        LinearLayout c1 = Ui.card(this);
        c1.addView(Ui.text(this, "지정 번호", 17, true, Ui.INK));

        LinearLayout addRow = new LinearLayout(this);
        addRow.setOrientation(LinearLayout.HORIZONTAL);
        addRow.setGravity(Gravity.CENTER_VERTICAL);
        numberInput = Ui.field(this, "010-0000-0000", false);
        numberInput.setInputType(InputType.TYPE_CLASS_PHONE);
        addRow.addView(numberInput, new LinearLayout.LayoutParams(0, -2, 1f));
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-2, -2);
        ap.leftMargin = Ui.dp(this, 8);
        addRow.addView(Ui.pill(this, "추가", true, v -> {
            if (Prefs.add(this, numberInput.getText().toString())) {
                numberInput.setText("");
                refresh();
            } else {
                Toast.makeText(this, "번호를 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
        }), ap);
        c1.addView(addRow, Ui.lp(-1, -2, 12, this));

        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        c1.addView(list, Ui.lp(-1, -2, 8, this));
        root.addView(c1, Ui.lp(-1, -2, 16, this));

        // 답장 문구 카드
        LinearLayout c2 = Ui.card(this);
        c2.addView(Ui.text(this, "답장 문구", 17, true, Ui.INK));
        c2.addView(Ui.text(this, Prefs.TOKEN + " 자리에 받은 문자가 들어갑니다.", 14, false, Ui.MUTED),
                Ui.lp(-1, -2, 4, this));
        templateInput = Ui.field(this, Prefs.DEFAULT_TEMPLATE, true);
        templateInput.setText(Prefs.template(this));
        c2.addView(templateInput, Ui.lp(-1, -2, 12, this));
        LinearLayout.LayoutParams sp = Ui.lp(-2, -2, 12, this);
        sp.gravity = Gravity.END;
        c2.addView(Ui.pill(this, "저장", true, v -> {
            String t = templateInput.getText().toString().trim();
            if (t.isEmpty()) t = Prefs.DEFAULT_TEMPLATE;
            Prefs.setTemplate(this, t);
            Toast.makeText(this, "저장했습니다", Toast.LENGTH_SHORT).show();
        }), sp);
        root.addView(c2, Ui.lp(-1, -2, 16, this));

        setContentView(scroll);
        askPermissions();
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        list.removeAllViews();
        List<String> nums = Prefs.numbers(this);
        if (nums.isEmpty()) {
            list.addView(Ui.text(this, "등록된 번호가 없습니다.", 14, false, Ui.MUTED));
        }
        for (String n : nums) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.addView(Ui.text(this, n, 17, false, Ui.INK), new LinearLayout.LayoutParams(0, -2, 1f));
            row.addView(Ui.link(this, "삭제", v -> {
                Prefs.remove(this, n);
                refresh();
            }));
            list.addView(row);
        }
        boolean sms = checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean noti = Build.VERSION.SDK_INT < 33
                || checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        status.setText(sms && noti ? "사용 중" : "권한이 필요합니다. 문자와 알림을 허용해 주세요.");
        status.setTextColor(sms && noti ? Ui.INK : Ui.PRIMARY);
    }

    private void askPermissions() {
        List<String> need = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
            need.add(Manifest.permission.RECEIVE_SMS);
        if (Build.VERSION.SDK_INT >= 33
                && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            need.add(Manifest.permission.POST_NOTIFICATIONS);
        if (!need.isEmpty()) requestPermissions(need.toArray(new String[0]), 1);
    }

    @Override
    public void onRequestPermissionsResult(int code, String[] perms, int[] results) {
        super.onRequestPermissionsResult(code, perms, results);
        refresh();
    }
}
