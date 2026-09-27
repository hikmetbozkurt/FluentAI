### Home Page’de bulunması gereken her şey

1. **Genel ekran yapısı**
   - Açık pembe ana arka plan.
   - Sol tarafta sabit **Side Navigation**.
   - Sağ tarafta scroll edilebilir Dashboard.
   - Dashboard içeriği responsive bir grid içerisinde.
   - Tüm card'larda aynı:
     - corner radius
     - border
     - shadow
     - padding
     - background
     - spacing sistemi.
   - Dark mode olmayacak.
2. **Side Navigation**
   - Uygulama logosu.
   - Uygulama adı.
   - Küçük slogan/subtitle.
   - Menü:
     - Home
     - Learn
     - Speak
     - Progress
     - Profile
     - Settings
   - Aktif sayfa için pembe rounded background.
   - Menü ikonları.
   - Menü textleri.
   - Alt bölümde kadın karakter illüstrasyonu.
   - Küçük pembe heart dekorasyonları.
   - Altında motivasyon sözü.
   - SideBar ekran küçülürse kontrollü biçimde daralmalı; içerik kaymamalı.
3. **Top Bar**
   - Solda Search Bar.
   - Search icon.
   - Placeholder:
     - `Search lessons, topics or anything...`
   - Sağda notification bell.
   - Notification badge/dot.
   - Kullanıcı avatarı.
   - Kullanıcı adı.
   - Dropdown arrow.
   - Search Bar, avatar ve notification farklı ekranlarda birbirine yaklaşmamalı veya taşmamalı.
4. **Welcome Hero Card**
   - `Welcome back, Hikmet 👋`
   - `Ready to practice today?`
   - Motivasyon metni.
   - Sağ bölümde dekoratif görsel:
     - pembe kitaplar
     - beyaz/pembe kupa
     - küçük bitki
   - Görsel card'ın sağ tarafına bağlı olmalı.
   - Text sol tarafta.
   - Görsel ekran küçülürse text üzerine binmemeli.
5. **Current Level Card**
   - Section icon.
   - `Current Level`
   - `View details →`
   - Crown icon.
   - Level:
     - B1
   - Level title:
     - Intermediate
   - `Overall Progress`
   - Büyük yüzde:
     - örn. `68%`
   - Progress bar.
   - `32% to reach B2`
   - Bu değerler daha sonra gerçek kullanıcı verisine bağlanacak.
6. **Continue Learning Card**
   - Başlık.
   - Subtitle:
     - `Pick up where you left off`
   - Son ders için thumbnail.
   - `Last lesson` badge.
   - Ders adı.
   - Ders tipi.
   - Ders süresi.
   - Büyük primary pink button.
   - Play icon.
   - `Continue Learning`.
   - Arrow icon.
7. **Daily Practice Card**
   - Calendar icon.
   - `Daily Practice`.
   - `Today's goal: 20 minutes`.
   - Circular progress indicator.
   - Ortada:
     - `12 min`
   - Streak alanı.
   - Flame icon.
   - `5 day streak`.
   - Alt bilgi alanı:
     - `You're doing great!`
     - `8 minutes to complete today's goal.`
8. **Speaking Practice Card**
   - Microphone icon.
   - `Speaking Practice`.
   - Subtitle.
   - AI konuşma karakteri olarak kadın illüstrasyonu.
   - Pembe headphones.
   - Küçük speech bubble'lar:
     - `Hi! 👋`
     - `Let's talk!`
     - `Practice real English!`
   - Ana CTA:
     - microphone icon
     - `Start Conversation`
     - arrow.
   - Bu card görsel olarak ana Dashboard CTA'larından biri olacak.
9. **Today's Focus Card**
   - Target icon.
   - `Today's Focus`.
   - Subtitle.
   - Beş mini skill card:
     - Vocabulary
     - Grammar
     - Pronunciation
     - Listening
     - Speaking
   - Her biri kendi iconuna sahip.
   - Hafif farklı pastel background kullanılabilir:
     - pink
     - purple
     - peach
     - mint
     - blue.
   - Ancak genel Barbie/pink tema korunmalı.
10. **Weak Areas / Needs Practice Card**
    - Warning icon.
    - Başlık.
    - Subtitle.
    - `See all →`.
    - Liste:
      - Past Tense
      - Prepositions
      - Pronunciation: TH
    - Her row içerisinde:
      - icon
      - title
      - açıklama
      - sağ arrow.
    - Row'lar hafif pembe background ile ayrılmalı.
11. **Progress Overview Card**
    - Progress icon.
    - `Progress Overview`.
    - Subtitle.
    - `See details →`.
    - Skill'ler:
      - Speaking
      - Vocabulary
      - Grammar
      - Listening
      - Pronunciation.
    - Her biri circular progress olarak gösterilecek.
    - Ortasında yüzdelik değer.
    - Altında skill adı.
    - Gerçek ilerleme datasına bağlanabilecek reusable component olmalı.
12. **Recent Activity Card**
    - Clock icon.
    - `Recent Activity`.
    - Subtitle.
    - `See all →`.
    - Activity satırları:
      - AI Conversation
      - Learned Words
      - Corrected Mistakes.
    - Her satır:
      - icon
      - activity type
      - kısa açıklama
      - tarih/süre.
    - Divider veya spacing ile ayrılmalı.
13. **Kadın karakter görselleri**
    - İki ayrı kullanım var:
      - Sidebar karakteri.
      - Speaking Practice karakteri.
    - Aynı karakter kimliği kullanılmalı.
    - Aynı:
      - saç
      - yüz
      - kıyafet
      - renk paleti
      - illüstrasyon stili.
    - Speaking versiyonunda headphones bulunabilir.
    - Görseller transparent PNG/WebP asset olmalı.
    - UI içine doğrudan background fotoğraf olarak gömülmemeli.
14. **Diğer dekoratif görseller**
    - Hero card:
      - books
      - mug
      - plant.
    - Lesson thumbnail.
    - Avatar.
    - Heart/decorative shapes.
    - Bunların hepsi `/assets/images/` gibi merkezi bir yapı içerisinde tutulmalı.
15. **Icon sistemi**
    - Home.
    - Book/Learn.
    - Microphone.
    - Progress chart.
    - Profile.
    - Settings.
    - Search.
    - Bell.
    - Chevron.
    - Calendar.
    - Crown.
    - Flame.
    - Target.
    - Vocabulary/book.
    - Grammar/document.
    - Speaker.
    - Headphones.
    - Warning.
    - Clock.
    - Icons mümkünse tek icon library üzerinden gelmeli.
    - Rastgele emoji/icon karışımı yapılmamalı.
16. **Typography**
    - Tek typography sistemi tanımlanmalı.
    - Örneğin:
      - Display
      - Page title
      - Card title
      - Body
      - Caption
      - Button
      - Numeric/stat.
    - Font size'lar her card içerisinde elle tekrar tanımlanmamalı.
    - `sp` kullanılmalı.
    - Uzun text:
      - maxLines
      - ellipsis
      - responsive wrapping.
    - Başlıklar ve body text aynı baseline sisteminde olmalı.
17. **Color Tokens**
    - `BackgroundPrimary`
    - `BackgroundSecondary`
    - `Surface`
    - `SurfacePink`
    - `PrimaryPink`
    - `PrimaryPinkDark`
    - `PrimaryPinkLight`
    - `TextPrimary`
    - `TextSecondary`
    - `BorderSoft`
    - `ProgressTrack`
    - Accent pastel renkleri.
    - Hiçbir component içinde rastgele hex kullanılmamalı.
18. **Spacing sistemi**
    - Temel spacing tokenları:
      - 4dp
      - 8dp
      - 12dp
      - 16dp
      - 20dp
      - 24dp
      - 32dp.
    - Dashboard dış margin örneğin 24dp.
    - Card arası gap yaklaşık 16dp.
    - Card iç padding yaklaşık 16–20dp.
    - Böylece elementler farklı cihazlarda kaymaz.
19. **Card sistemi**
    - Ortak reusable `DashboardCard`.
    - Yaklaşık:
      - radius: 18–24dp
      - white/light pink surface
      - subtle border
      - very soft shadow.
    - Her card kendi custom container'ını yaratmamalı.
    - Header yapısı da ortak olabilir:
      - icon
      - title
      - subtitle
      - optional action.
20. **Button sistemi**
    - Primary Button.
    - Secondary Button.
    - Icon Button.
    - Text Button.
    - Primary CTA:
      - canlı Barbie pink.
      - white text.
      - rounded corners.
      - icon + text + optional arrow.
    - Aynı height ve padding standardı kullanılmalı.
21. **Responsive Dashboard Grid**
    - Sabit x/y positioning kullanılmamalı.
    - Dashboard width'e göre layout değişmeli.
    - Büyük ekran:
      - 2–3 column.
    - Orta ekran:
      - 2 column.
    - Küçük ekran:
      - 1 column.
    - Hero ve bazı büyük card'lar birden fazla column span edebilir.
    - Grid parent kullanılmalı.
    - Card içinde Row/Column kullanılmalı.
22. **Dashboard yaklaşık yerleşim sırası**
    - TopBar
    - Row:
      - Welcome Hero geniş
      - Current Level küçük
    - Row:
      - Continue Learning
      - Daily Practice
      - Speaking Practice
    - Row:
      - Today's Focus geniş
      - Weak Areas geniş
    - Row:
      - Progress Overview
      - Recent Activity.
    - Bu sıralama referans görüntünün ana kompozisyonunu korur.

---

# UI mimarisi

Android/Compose tarafında ana yapı kabaca:

`Scaffold`

→ `Row`

→ `SideNavigation`

→ `MainDashboard`

Dashboard tarafı:

`Column`

→ `TopBar`

→ `LazyVerticalGrid / adaptive grid`

→ `DashboardCard`

→ card içerisinde `Row / Column / Box`.

Önemli olan şu:

**Hiçbir element `absolute x=340 y=150` gibi konumlandırılmamalı.**

UI parent container üzerinden pozisyonlanmalı.

Örneğin:

`Card → Column → Header Row → Content → Button`

şeklinde ilerlemeli.

---

# Responsive sistem

Tablet/desktop genişliği düşünülerek belirli breakpoint'ler tanımlanmalı.

Örneğin:

**Large**\
`>= 1200dp`

SideBar açık.

Dashboard maksimum 3 column.

**Medium**\
`700–1199dp`

2 column.

SideBar biraz daraltılabilir.

**Small**\
`<700dp`

1 column.

Gerekirse SideBar drawer yapısına dönüşebilir.

Bu sayede UI herhangi bir çözünürlükte bozulmaz.

---

# Görselleri bire bir yaklaştırma

Burada önemli bir nokta var.

Ürettiğimiz mockup'taki:

- kadın karakter
- books
- mug
- plant
- heart illustrations

gibi görseller UI koduyla çizilmemeli.

Bunları ayrı **transparent asset** olarak üretmemiz gerekir.

Örneğin:

```
assets/
  images/
    assistant_sidebar.webp
    assistant_speaking.webp
    dashboard_hero_books.webp
    dashboard_hero_mug.webp
    lesson_cafe.webp
    user_avatar.webp

  icons/
    ...
```

Kadın karakter için de tek bir **character design reference** belirleyip sonraki tüm görüntüleri aynı karakter üzerinden üretmek önemli.

Aksi halde her image generation sonucunda farklı bir kadın oluşur.

---

# Refactor yaklaşımı

Bu işi doğrudan HomeScreen dosyasına girip bütün kodu değiştirmek şeklinde yapmamak lazım.

Önce şu temel sistem kurulmalı:

```
theme/
    Colors
    Typography
    Spacing
    Dimensions
    Shapes

components/
    DashboardCard
    DashboardHeader
    PrimaryButton
    ProgressRing
    SkillCard
    ActivityRow
    SideNavItem
    SearchBar
```

Daha sonra:

```
screens/
    HomeScreen
```

HomeScreen sadece bunları compose etmeli.

Örneğin:

```
HomeScreen

TopBar

DashboardGrid
    WelcomeCard
    CurrentLevelCard

    ContinueLearningCard
    DailyPracticeCard
    SpeakingPracticeCard

    TodaysFocusCard
    WeakAreasCard

    ProgressOverviewCard
    RecentActivityCard
```

Böylece layout kontrolü çok daha kolay olur.

---

# Refactor'ın uygulanma sırası

İlk önce **tasarım tokenları** oluşturulmalı.

Ardından ortak:

`Card / Button / Typography / Icon / Spacing`

component'leri hazırlanmalı.

Sonra Side Navigation ve TopBar yapılmalı.

Ardından yukarıdan aşağı Dashboard card'ları eklenmeli.

En son:

- gerçek görseller
- animasyonlar
- gerçek data
- navigation action'ları
- click behavior
- hover/pressed states

bağlanmalı.

Özellikle ilk aşamada **backend veya gerçek data mantığına dokunmadan yalnızca UI refactor yapmak** daha doğru olur.

---

# En kritik kural

Bu Home Page'i bire bir yaklaştırmak için üç şeyi birbirinden ayırmamız gerekiyor:

**1. Design system**

Renk, font, radius, spacing, button, card.

**2. Layout system**

SideBar + Responsive Grid + Row/Column.

**3. Assets**

Kadın karakter, mug, books, thumbnail, avatar ve dekorasyonlar.