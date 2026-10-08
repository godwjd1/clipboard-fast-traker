package cloud.mmucaptain.smsreply;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

final class Prefs {
    static final String DEFAULT_TEMPLATE = "{문자} 감사합니다";
    static final String TOKEN = "{문자}";

    private static final String FILE = "smsreply";
    private static final String K_NUMBERS = "numbers";
    private static final String K_TEMPLATE = "template";

    private Prefs() {}

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    /** 숫자만 남기고 +82 / 82 로 시작하면 0으로 바꾼다. */
    static String normalize(String raw) {
        if (raw == null) return "";
        String d = raw.replaceAll("[^0-9]", "");
        if (d.startsWith("82") && d.length() > 9) d = "0" + d.substring(2);
        return d;
    }

    static List<String> numbers(Context c) {
        List<String> out = new ArrayList<>();
        String s = sp(c).getString(K_NUMBERS, "");
        for (String n : s.split("\n")) if (!n.isEmpty()) out.add(n);
        return out;
    }

    static boolean add(Context c, String raw) {
        String n = normalize(raw);
        if (n.length() < 4) return false;
        List<String> list = numbers(c);
        if (list.contains(n)) return false;
        list.add(n);
        save(c, list);
        return true;
    }

    static void remove(Context c, String n) {
        List<String> list = numbers(c);
        list.remove(n);
        save(c, list);
    }

    private static void save(Context c, List<String> list) {
        sp(c).edit().putString(K_NUMBERS, String.join("\n", list)).apply();
    }

    static boolean isRegistered(Context c, String sender) {
        String n = normalize(sender);
        return !n.isEmpty() && numbers(c).contains(n);
    }

    static String template(Context c) {
        return sp(c).getString(K_TEMPLATE, DEFAULT_TEMPLATE);
    }

    static void setTemplate(Context c, String t) {
        sp(c).edit().putString(K_TEMPLATE, t).apply();
    }

    static String compose(Context c, String body) {
        String t = template(c);
        return t.contains(TOKEN) ? t.replace(TOKEN, body) : body + " " + t;
    }
}
