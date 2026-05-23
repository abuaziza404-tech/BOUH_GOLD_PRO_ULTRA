# BOUH Map GPS Android

تطبيق Android جاهز للبناء كـ APK لنظام **بوح التضاريس | منظومة أبوعزيزة**.

## الوظائف
- خريطة GPS مباشرة.
- طبقات جيولوجية/استشعار بصيغة GeoJSON.
- نقاط ودوائر 30 متر بصيغة CSV.
- Radar Overlay شفاف فوق الخريطة.
- دعم خرائط Online Tiles، ويمكن تحويله إلى Offline Tiles بوضع tiles داخل `assets/tiles/z/x/y.png`.
- واجهة عربية ميدانية مناسبة لـ GPZ field navigation.

## الملفات المهمة
- `app/src/main/assets/map.html` الواجهة والخريطة والرادار.
- `app/src/main/assets/layers.geojson` طبقات جيولوجية تجريبية.
- `app/src/main/assets/targets.csv` نقاط ودوائر تجريبية.
- `.github/workflows/build-apk.yml` يبني APK تلقائياً عند رفع المشروع إلى GitHub.

## طريقة بناء APK من GitHub
1. ارفع محتوى هذا المشروع إلى مستودع GitHub.
2. افتح تبويب Actions.
3. شغّل workflow باسم `Build Android APK`.
4. بعد انتهاء البناء، افتح Artifacts وحمّل `BOUH-Map-GPS-APK`.

## ملاحظة مهمة
النقاط والطبقات الموجودة في الحزمة أمثلة توضيحية فقط. استبدلها بملفاتك الحقيقية:
- GeoJSON للطبقات: `layers.geojson`
- CSV للنقاط: `targets.csv`

لا تستخدم صور Google Earth أو PNG/JPG/KMZ كدليل طيفي حسابي. استخدمها كدعم بصري فقط.
