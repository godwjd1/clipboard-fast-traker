문자 답장 앱 (안드로이드)

기능
지정한 번호에서 문자가 오면 알림과 팝업이 뜹니다.
팝업에는 "[받은 문자] 감사합니다" 형태의 답장이 미리 채워져 있고, 고쳐서 복사하거나 문자 앱으로 보낼 수 있습니다.
번호와 문구는 앱 첫 화면에서 바꿉니다. 문구의 {문자} 자리에 받은 문자가 들어갑니다.

APK 만드는 방법 A: Android Studio
1. 이 폴더를 Android Studio로 연다.
2. Build > Build Bundle(s) / APK(s) > Build APK(s).
3. app/build/outputs/apk/debug/app-debug.apk 를 폰으로 옮겨 설치한다.

APK 만드는 방법 B: GitHub (PC에 아무것도 설치하지 않는 방법)
1. GitHub에 새 저장소를 만들고 이 폴더 내용을 그대로 올린다.
2. Actions 탭에서 Build APK 가 끝나길 기다린다. (2~4분)
3. 실행 결과 화면 아래 Artifacts에서 smsreply-apk 를 받는다. zip 안에 app-debug.apk 가 있다.

설치할 때
출처를 알 수 없는 앱 설치를 허용해야 합니다.
Play 프로텍트가 한 번 경고할 수 있습니다. "상세 정보 > 무시하고 설치"를 누르면 됩니다.
Android 13 이상에서 설치 직후 문자 권한이 회색이면, 설정 > 앱 > 문자 답장 > 점3개 > "제한된 설정 허용"을 켠 뒤 다시 권한을 허용하세요.
