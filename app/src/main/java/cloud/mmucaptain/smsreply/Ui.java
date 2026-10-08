package cloud.mmucaptain.smsreply;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Apple 디자인 토큰을 코드로 옮긴 헬퍼. 색은 파란색 하나만 쓴다. */
final class Ui {
    static final int PRIMARY = Color.parseColor("#0066cc");
    static final int INK = Color.parseColor("#1d1d1f");
    static final int MUTED = Color.parseColor("#7a7a7a");
    static final int PARCHMENT = Color.parseColor("#f5f5f7");
    static final int PEARL = Color.parseColor("#fafafc");
    static final int HAIRLINE = Color.parseColor("#e0e0e0");
    static final int WHITE = Color.WHITE;

    private Ui() {}

    static int dp(Context c, float v) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, v, c.getResources().getDisplayMetrics()));
    }

    static GradientDrawable rect(Context c, int fill, float radiusDp, int strokeColor) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(fill);
        d.setCornerRadius(dp(c, radiusDp));
        if (strokeColor != 0) d.setStroke(dp(c, 1), strokeColor);
        return d;
    }

    static TextView text(Context c, String s, float sp, boolean bold, int color) {
        TextView t = new TextView(c);
        t.setText(s);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        t.setTextColor(color);
        t.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        t.setLetterSpacing(sp >= 17 ? -0.02f : -0.01f);
        return t;
    }

    static TextView title(Context c, String s) {
        TextView t = text(c, s, 34, true, INK);
        t.setLineSpacing(0, 1.1f);
        return t;
    }

    /** 흰 카드. 18dp 라운드, 1dp 헤어라인, 그림자 없음. */
    static LinearLayout card(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        int p = dp(c, 24);
        l.setPadding(p, p, p, p);
        l.setBackground(rect(c, WHITE, 18, HAIRLINE));
        return l;
    }

    /** 알약 버튼. filled=true면 Action Blue, 아니면 고스트 알약. 누르면 0.95배. */
    static TextView pill(Context c, String label, boolean filled, View.OnClickListener l) {
        TextView t = text(c, label, 17, false, filled ? WHITE : PRIMARY);
        t.setGravity(Gravity.CENTER);
        t.setPadding(dp(c, 22), dp(c, 11), dp(c, 22), dp(c, 11));
        t.setMinHeight(dp(c, 44));
        t.setBackground(filled ? rect(c, PRIMARY, 999, 0) : rect(c, WHITE, 999, PRIMARY));
        t.setOnClickListener(l);
        t.setOnTouchListener((v, e) -> {
            int a = e.getActionMasked();
            if (a == MotionEvent.ACTION_DOWN) v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).start();
            else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_CANCEL)
                v.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
            return false;
        });
        return t;
    }

    static TextView link(Context c, String label, View.OnClickListener l) {
        TextView t = text(c, label, 17, false, PRIMARY);
        t.setPadding(dp(c, 12), dp(c, 10), 0, dp(c, 10));
        t.setOnClickListener(l);
        return t;
    }

    static EditText field(Context c, String hint, boolean multiline) {
        EditText e = new EditText(c);
        e.setHint(hint);
        e.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        e.setTextColor(INK);
        e.setHintTextColor(MUTED);
        e.setBackground(rect(c, PEARL, 11, HAIRLINE));
        e.setPadding(dp(c, 16), dp(c, 12), dp(c, 16), dp(c, 12));
        if (multiline) {
            e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            e.setMinLines(3);
            e.setGravity(Gravity.TOP | Gravity.START);
        }
        return e;
    }

    static LinearLayout.LayoutParams lp(int w, int h, int topDp, Context c) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(w, h);
        p.topMargin = dp(c, topDp);
        return p;
    }
}
