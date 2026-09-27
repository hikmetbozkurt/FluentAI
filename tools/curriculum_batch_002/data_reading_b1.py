#!/usr/bin/env python3
"""
Reading Batch 002: B1 Data (8 articles).
Each article: 400-700 words, 5 paragraphs, verified annotations, 5 questions.
"""

from typing import List, Dict, Any
from mcq_shuffler import DeterministicMcqShuffler

shuffler = DeterministicMcqShuffler(20260927)

def build_article(
    article_id: str,
    title: str,
    cefr: str,
    category: str,
    summary_en: str,
    summary_tr: str,
    topic_tags: List[str],
    paragraphs: List[Dict[str, Any]],
    annotations: List[Dict[str, str]],
    raw_questions: List[Dict[str, Any]],
) -> Dict[str, Any]:
    total_words = sum(len(p["content_en"].split()) for p in paragraphs)
    est_minutes = max(1, round(total_words / 160))

    full_text = " ".join(p["content_en"].lower() for p in paragraphs)
    for ann in annotations:
        word = ann["word"].lower()
        assert word in full_text, f"[{article_id}] Annotation word '{word}' not found in article text!"

    questions = []
    for q_idx, q in enumerate(raw_questions):
        qid = f"q_{article_id.replace('.', '_')}_{q_idx+1:02d}"
        shuffled = shuffler.shuffle_question(
            qid,
            q["correct_answer"],
            q["distractors"],
        )
        questions.append({
            "id": qid,
            "question_en": q["question_en"],
            "question_tr_hint": q["question_tr_hint"],
            "options": shuffled["options"],
            "correct_answer": shuffled["correct_answer"],
            "explanation_en": q["explanation_en"],
            "explanation_tr": q["explanation_tr"],
        })

    assert len(questions) == 5, f"[{article_id}] Must have exactly 5 questions, got {len(questions)}"

    return {
        "id": article_id,
        "title": title,
        "cefr_level": cefr,
        "category": category,
        "summary_en": summary_en,
        "summary_tr": summary_tr,
        "word_count": total_words,
        "estimated_reading_minutes": est_minutes,
        "paragraphs": paragraphs,
        "vocabulary_annotations": annotations,
        "comprehension_questions": questions,
        "topic_tags": topic_tags,
        "status": "APPROVED",
        "version": 1,
    }

READING_B1: List[Dict[str, Any]] = [
    # 1. health-lifestyle / workplace (B1, ~460w)
    build_article(
        "reading.b1.workplace-ergonomics-remote",
        "Practical Ergonomics for Home Office Environments",
        "B1", "workplace_communication",
        "Guidelines on arranging computer monitors, chairs, and desk accessories to eliminate physical strain during remote work.",
        "Uzaktan çalışma sırasında fiziksel gerginliği önlemek için bilgisayar ekranları, sandalyeler ve masa aksesuarlarını düzenleme kılavuzu.",
        ["health-lifestyle", "remote-work", "ergonomics", "wellbeing"],
        [
            {
                "paragraph_index": 1,
                "title": "The Silent Strain of Informal Setups",
                "content_en": "When companies transitioned to distributed working models, thousands of knowledge workers set up makeshift workstations at kitchen tables, soft sofas, and decorative desks. Initially viewed as temporary inconveniences, these unsupportive furniture arrangements soon triggered widespread neck stiffness, lower back soreness, and repetitive wrist strain. Occupational physiotherapists emphasize that poor posture creates cumulative stress on musculoskeletal tissues. Sustained slouching compresses spinal discs and impedes proper circulation, gradually diminishing energy levels and cognitive endurance across consecutive workdays.",
                "content_tr": "Şirketler dağıtık çalışma modellerine geçtiğinde, binlerce bilgi çalışanı mutfak masalarında, yumuşak kanepelerde ve dekoratif masalarda geçici çalışma alanları kurdu. Başlangıçta geçici rahatsızlıklar olarak görülen bu desteksiz mobilya düzenlemeleri, kısa sürede yaygın boyun tutulmasına, bel ağrısına ve bilek zorlanmasına yol açtı. Mesleki fizyoterapistler, kötü duruşun kas-iskelet dokularında birikimli stres yarattığını vurgulamaktadır. Sürekli kambur durmak omurilik disklerini sıkıştırır ve doğru dolaşımı engelleyerek ardışık iş günleri boyunca enerji seviyelerini ve bilişsel dayanıklılığı kademeli olarak azaltır."
            },
            {
                "paragraph_index": 2,
                "title": "Configuring the Primary Touchpoints",
                "content_en": "Designing an ergonomic workstation does not require purchasing exorbitant specialized equipment. The fundamental principle is maintaining neutral body angles where joints rest naturally without excessive tension. Your computer monitor must be elevated so the top third of the display aligns with eye level, preventing the head from tilting forward. Office chairs should support the natural inward curve of the lower spine, while seat height must allow the feet to rest flat on the floor with knees bent at approximately ninety degrees. When typing, elbows ought to remain close to the torso with forearms parallel to the desktop surface.",
                "content_tr": "Ergonomik bir çalışma alanı tasarlamak çok pahalı özel ekipmanlar satın almayı gerektirmez. Temel ilke, eklemlerin aşırı gerginlik olmadan doğal bir şekilde dinlendiği nötr vücut açılarını korumaktır. Başın öne eğilmesini önlemek için bilgisayar monitörünüz, ekranın üst üçte birlik kısmı göz hizasıyla hizalanacak şekilde yükseltilmelidir. Ofis koltukları alt omurganın doğal içe doğru kıvrımını desteklemeli, koltuk yüksekliği ise dizler yaklaşık doksan derece bükülmüş halde ayakların yere düz basmasını sağlamalıdır. Yazı yazarken dirsekler gövdeye yakın kalmalı ve ön kollar masa yüzeyine paralel olmalıdır."
            },
            {
                "paragraph_index": 3,
                "title": "Movement as the Ultimate Countermeasure",
                "content_en": "Even the most sophisticated ergonomic chair cannot counteract the physiological hazards of prolonged stationary sitting. Static muscular loading restricts blood flow, accumulating metabolic waste products that cause discomfort. Health experts recommend following the twenty-twenty rule: every twenty minutes, look at an object twenty feet away for twenty seconds, and stand up to stretch briefly every hour. Alternating between sitting and standing, taking short walking phone calls, and performing gentle shoulder rolls restore muscular balance and sustain mental alertness throughout demanding afternoons.",
                "content_tr": "En gelişmiş ergonomik sandalye bile uzun süreli hareketsiz oturmanın fizyolojik tehlikelerini tek başına telafi edemez. Statik kas yüklenmesi kan akışını kısıtlar ve rahatsızlığa neden olan metabolik atık ürünleri biriktirir. Sağlık uzmanları yirmi-yirmi kuralını uygulamayı önermektedir: her yirmi dakikada bir, yirmi fit uzaktaki bir nesneye yirmi saniye bakın ve her saat başı kısa süre esnemek için ayağa kalkın. Oturma ve ayakta durma arasında geçiş yapmak, kısa yürüyüşlü telefon görüşmeleri yapmak ve hafif omuz hareketleri kas dengesini yeniler ve zorlu öğleden sonraları boyunca zihinsel uyanıklığı korur."
            },
            {
                "paragraph_index": 4,
                "title": "Optimizing Peripheral Accessories",
                "content_en": "In addition to core seating and monitor elevation, secondary computer accessories exert a profound influence on daily comfort. Using a compact laptop keyboard forces the shoulders inward and pinches delicate nerves in the cervical spine. Connecting an external full-sized keyboard and an ergonomically sculpted vertical mouse allows the wrists to remain in a neutral handshake posture. Simple wrist rests filled with supportive gel prevent blood vessels from being compressed against sharp desk edges during intensive data entry.",
                "content_tr": "Temel oturma düzeni ve ekran yüksekliğine ek olarak, ikincil bilgisayar aksesuarları da günlük konfor üzerinde derin bir etkiye sahiptir. Kompakt bir dizüstü bilgisayar klavyesi kullanmak omuzları içe doğru zorlar ve boyun omurgasındaki hassas sinirleri sıkıştırır. Harici bir tam boyutlu klavye ve ergonomik olarak şekillendirilmiş dikey bir fare bağlamak, bileklerin nötr bir el sıkışma pozisyonunda kalmasını sağlar. Destekleyici jel ile doldurulmuş basit bilek destekleri, yoğun veri girişi sırasında kan damarlarının keskin masa kenarlarına sıkışmasını önler."
            },
            {
                "paragraph_index": 5,
                "title": "Creating Sustainable Work Routines",
                "content_en": "Ultimately, ergonomics is not a one-time mechanical adjustment but a daily behavioural practice. Auditing your posture several times throughout the working day helps identify unconscious slouching before chronic pain develops. Taking two minutes at the end of each afternoon to tidy your desk creates an inviting environment for the following morning. By treating physical well-being as an integral component of professional productivity, remote professionals ensure sustainable career longevity without physical compromise.",
                "content_tr": "Nihayetinde ergonomi, tek seferlik mekanik bir ayarlama değil, günlük bir davranışsal uygulamadır. Çalışma günü boyunca duruşunuzu birkaç kez denetlemek, kronik ağrı gelişmeden önce bilinçsiz kamburlaşmayı belirlemeye yardımcı olur. Her öğleden sonrasının sonunda masanızı toplamak için iki dakika ayırmak, ertesi sabah için davetkar bir ortam yaratır. Uzaktan çalışan profesyoneller, fiziksel refahı mesleki üretkenliğin ayrılmaz bir bileşeni olarak görerek fiziksel taviz vermeden sürdürülebilir bir kariyer uzunluğu sağlarlar."
            }
        ],
        [
            {"word": "strain", "vocab_id": "vocab.strain", "context_definition_en": "Severe demand on physical strength or bodily tissues.", "context_meaning_tr": "zorlanma, gerginlik"},
            {"word": "slouching", "vocab_id": "vocab.slouch", "context_definition_en": "Sitting or standing in a lazy, drooping posture.", "context_meaning_tr": "kambur oturma"},
            {"word": "stationary", "vocab_id": "vocab.stationary", "context_definition_en": "Remaining in one place without moving.", "context_meaning_tr": "hareketsiz, sabit"}
        ],
        [
            {
                "question_en": "According to the passage, what is the primary risk of using informal furniture for remote work?",
                "question_tr_hint": "Metne göre uzaktan çalışmada uygunsuz mobilya kullanmanın temel riski nedir?",
                "correct_answer": "It causes cumulative musculoskeletal strain and decreases long-term energy.",
                "distractors": [
                    "It completely destroys the structural stability of the computer monitor.",
                    "It immediately forces employers to cancel all remote work policies.",
                    "It prevents workers from accessing digital files securely."
                ],
                "explanation_en": "The first paragraph states that poor posture creates cumulative stress on musculoskeletal tissues and diminishes energy levels.",
                "explanation_tr": "Birinci paragrafta kötü duruşun kas-iskelet dokularında birikimli stres yarattığı ve enerji seviyelerini düşürdüğü belirtilmektedir."
            },
            {
                "question_en": "Where should the top third of a computer monitor ideally be positioned?",
                "question_tr_hint": "Bilgisayar monitörünün üst üçte birlik kısmı ideal olarak nereye hizalanmalıdır?",
                "correct_answer": "Directly at the user's eye level to prevent forward head tilting.",
                "distractors": [
                    "Below the desk surface to minimize glare from windows.",
                    "At least thirty centimeters above the user's head.",
                    "Directly against the ceiling to improve neck arching."
                ],
                "explanation_en": "Paragraph 2 specifies that the top third of the display must align with eye level to keep the neck neutral.",
                "explanation_tr": "İkinci paragrafta ekranın üst üçte birinin göz hizasında olması gerektiği ifade edilmiştir."
            },
            {
                "question_en": "What does the author suggest about expensive ergonomic furniture?",
                "question_tr_hint": "Yazar pahalı ergonomik mobilyalar hakkında ne öne sürmektedir?",
                "correct_answer": "Ergonomics is about neutral body angles rather than buying exorbitant equipment.",
                "distractors": [
                    "Only million-dollar specialized chairs can protect workers from fatigue.",
                    "Ergonomic furniture has been medically proven to be completely useless.",
                    "Every employee must purchase professional medical massage apparatuses."
                ],
                "explanation_en": "Paragraph 2 states that good ergonomics does not require exorbitant specialized equipment, but proper alignment.",
                "explanation_tr": "İkinci paragraf ergonominin aşırı pahalı ekipmanlar değil, doğru vücut açıları gerektirdiğini söyler."
            },
            {
                "question_en": "Why is standing and moving periodically essential even with a great chair?",
                "question_tr_hint": "Harika bir sandalye olsa dahi periyodik olarak kalkıp hareket etmek neden gereklidir?",
                "correct_answer": "Because prolonged stationary sitting restricts circulation and accumulates metabolic waste.",
                "distractors": [
                    "Because modern chairs lose their mechanical warranties if sat on continuously.",
                    "Because employers track keystroke speed through movement sensors.",
                    "Because standing burns up all daily calories in under five minutes."
                ],
                "explanation_en": "Paragraph 3 explains that static muscular loading restricts blood flow and causes discomfort regardless of chair quality.",
                "explanation_tr": "Üçüncü paragraf, sandalyenin kalitesinden bağımsız olarak hareketsiz oturmanın kan akışını kısıtladığını ve metabolik atık biriktirdiğini açıklar."
            },
            {
                "question_en": "What advantage is provided by using an external keyboard and vertical mouse?",
                "question_tr_hint": "Harici klavye ve dikey fare kullanmanın sağladığı avantaj nedir?",
                "correct_answer": "They keep the wrists in a neutral position and prevent compressed nerves.",
                "distractors": [
                    "They allow computers to operate without any electricity.",
                    "They eliminate all software errors from corporate databases.",
                    "They double the typing speed of all employees automatically."
                ],
                "explanation_en": "Paragraph 4 explains that external peripherals maintain neutral wrist posture and prevent pinched nerves.",
                "explanation_tr": "4. paragraf harici donanımların bilekleri nötr pozisyonda tuttuğunu ve sinir sıkışmasını engellediğini açıklar."
            }
        ]
    ),

    # 2. travel (B1, ~450w)
    build_article(
        "reading.b1.solo-travel-safety-tips",
        "The Art and Safety of Independent Travel",
        "B1", "workplace_communication",
        "Essential preparation, navigation strategies, and situational awareness for rewarding and secure solo adventures.",
        "Güvenli ve tatmin edici tek başına seyahatler için temel hazırlık, navigasyon stratejileri ve durumsal farkındalık.",
        ["travel", "safety", "independence", "planning"],
        [
            {
                "paragraph_index": 1,
                "title": "The Growth of Independent Exploration",
                "content_en": "Embarking on a solo trip offers unparalleled freedom. Without the necessity of compromising with companions, independent travelers choose their own itineraries, dine whenever appetite dictates, and pause spontaneously at hidden urban courtyards. However, this exhilarating autonomy requires heightened self-reliance and practical preparation. Solo travelers act as their own navigators, financial managers, logistics coordinators, and personal security officers throughout the entire journey. Understanding how to mitigate potential risks transforms anxious apprehension into grounded self-confidence. Travelers who master these skills discover a profound sense of personal liberation and boundless adventure.",
                "content_tr": "Tek başına bir yolculuğa çıkmak eşsiz bir özgürlük sunar. Yol arkadaşlarıyla uzlaşma zorunluluğu olmadan, bağımsız gezginler kendi güzergahlarını seçer, iştahları ne zaman isterse o zaman yemek yer ve gizli avlularda kendiliğinden mola verir. Ancak bu heyecan verici özerklik, daha yüksek bir özgüven ve pratik hazırlık gerektirir. Yalnız gezginler kendi navigatörleri, finans yöneticileri ve güvenlik görevlileridir. Olası riskleri nasıl azaltacağını anlamak, endişeli tedirginliği sağlam bir özgüvene dönüştürür."
            },
            {
                "paragraph_index": 2,
                "title": "Digital Redundancy and Communication",
                "content_en": "A reliable safety strategy begins before boarding the first airplane or train. Savvy travelers prepare redundant copies of critical documents, including passports, entry visas, and medical insurance policies, storing encrypted digital scans in secure cloud repositories while keeping printed duplicates in separate luggage compartments. Furthermore, sharing your detailed route and daily accommodation addresses with a trusted relative establishes an essential safety net. Establishing a routine morning check-in message provides loved ones with peace of mind without curtailing your spontaneous daytime explorations. Regular contact ensures that any unexpected change in plans is noted promptly by people who care about your welfare.",
                "content_tr": "Güvenilir bir güvenlik stratejisi ilk uçağa veya trene binmeden önce başlar. Bilinçli gezginler pasaportlar, giriş vizeleri ve sağlık sigortası poliçeleri de dahil olmak üzere kritik belgelerin yedek kopyalarını hazırlar; şifrelenmiş dijital taramaları güvenli bulut depolarında saklarken basılı kopyaları ayrı bagaj bölmelerinde tutar. Ayrıca, ayrıntılı rotanızı ve günlük konaklama adreslerinizi güvenilir bir akrabanızla paylaşmak önemli bir güvenlik ağı oluşturur. Sabahları düzenli bir durum bildirimi mesajı göndermek, gün içindeki spontane keşiflerinizi kısıtlamadan sevdiklerinize huzur verir."
            },
            {
                "paragraph_index": 3,
                "title": "Situational Awareness in Unfamiliar Cities",
                "content_en": "Navigating unfamiliar foreign neighborhoods demands mindful situational awareness rather than chronic paranoia. Projecting outward confidence through upright posture and purposeful stride discourages opportunists who target visibly disoriented tourists. When studying digital transit maps, step inside a café or museum lobby instead of pausing motionless at crowded intersection corners. Additionally, splitting cash and payment cards across multiple secure pockets prevents a single pickpocketing incident from becoming an overwhelming financial disaster. Keeping a concealed emergency debit card separate from your daily wallet guarantees uninterrupted access to necessary funds.",
                "content_tr": "Yabancı mahallelerde gezinmek sürekli bir paranoyadan ziyade bilinçli bir durumsal farkındalık gerektirir. Dik bir duruş ve amaçlı adımlarla dışa doğru özgüven yansıtmak, gözle görülür şekilde yönünü şaşırmış turistleri hedef alan fırsatçıları caydırır. Dijital toplu taşıma haritalarını incelerken, kalabalık kavşak köşelerinde hareketsiz durmak yerine bir kafeye veya müze lobisine girin. Ayrıca nakit ve ödeme kartlarını birden fazla güvenli cebe bölmek, tek bir yankesicilik olayının ezici bir mali felakete dönüşmesini önler."
            },
            {
                "paragraph_index": 4,
                "title": "Choosing Safe Accommodations",
                "content_en": "Selecting suitable lodging is another pillar of peaceful independent travel. Prioritize accommodations situated in vibrant, well-lit districts close to reliable public transit stations rather than secluded bargains on city outskirts. Reading recent guest reviews from other solo travelers yields invaluable intelligence regarding neighbourhood safety, nighttime street lighting, and the helpfulness of front desk personnel. Arriving at new destinations during daytime hours provides daylight to orient yourself before darkness falls.",
                "content_tr": "Uygun konaklama yeri seçmek, huzurlu bağımsız seyahatin bir diğer sütunudur. Şehir dışındaki izole fırsatlar yerine güvenilir toplu taşıma istasyonlarına yakın, canlı ve iyi aydınlatılmış bölgelerde bulunan konaklama yerlerini tercih edin. Diğer yalnız gezginlerin güncel konuk yorumlarını okumak mahalle güvenliği, gece sokak aydınlatması ve resepsiyon personelinin yardımseverliği hakkında paha biçilmez bilgiler sağlar. Yeni varış noktalarına gündüz saatlerinde varmak, karanlık basmadan önce yönünüzü belirlemeniz için gün ışığı sağlar."
            },
            {
                "paragraph_index": 5,
                "title": "Trusting Intuition and Connecting Respectfully",
                "content_en": "Finally, the most powerful personal defence mechanism is trusting your intuitive instincts. If an unprompted conversation or unfamiliar alleyway feels subtly uncomfortable, depart calmly without hesitation. Cultivating courteous interactions with local shopkeepers, hotel staff, and museum guides provides genuine cultural insights while creating spontaneous allies. Independent travel tests your resourcefulness, leaving you with an enduring sense of personal capability.",
                "content_tr": "Son olarak, en güçlü kişisel savunma mekanizması sezgisel içgüdülerinize güvenmektir. Kendiliğinden gelişen bir konuşma veya yabancı bir ara sokak hafifçe rahatsız edici hissettiriyorsa, tereddüt etmeden sakince oradan uzaklaşın. Yerel esnaf, otel personeli ve müze rehberleriyle kibar etkileşimler kurmak, kendiliğinden müttefikler yaratırken samimi kültürel içgörüler sağlar. Bağımsız seyahat becerikliliğinizi test eder ve size kalıcı bir kişisel yetkinlik duygusu bırakır."
            }
        ],
        [
            {"word": "itineraries", "vocab_id": "vocab.itinerary", "context_definition_en": "Planned routes or schedules of travel.", "context_meaning_tr": "seyahat programları"},
            {"word": "redundant", "vocab_id": "vocab.redundant", "context_definition_en": "Providing duplicate copies as backup for security.", "context_meaning_tr": "yedekli, fazladan"},
            {"word": "disoriented", "vocab_id": "vocab.disoriented", "context_definition_en": "Confused regarding time, place, or identity.", "context_meaning_tr": "yönünü şaşırmış"}
        ],
        [
            {
                "question_en": "What is described as the primary appeal of solo travel?",
                "question_tr_hint": "Tek başına seyahatin temel cazibesi olarak ne tanımlanmaktadır?",
                "correct_answer": "Complete autonomy over schedule, routes, and spontaneous choices.",
                "distractors": [
                    "Guaranteed discounts on international airline tickets.",
                    "Avoiding all interactions with local residents.",
                    "Receiving free accommodation in every destination."
                ],
                "explanation_en": "The first paragraph emphasizes freedom and autonomy in choosing itineraries without needing to compromise.",
                "explanation_tr": "İlk paragraf, uzlaşmaya gerek kalmadan güzergah seçmedeki özgürlüğü ve özerkliği vurgular."
            },
            {
                "question_en": "How should travelers handle vital travel documents according to the passage?",
                "question_tr_hint": "Metne göre gezginler hayati seyahat belgelerini nasıl idare etmelidir?",
                "correct_answer": "Maintain encrypted cloud scans and store printed copies in separate luggage.",
                "distractors": [
                    "Carry all original papers in a single exterior jacket pocket.",
                    "Laminate all documents and display them prominently around the neck.",
                    "Surrender all personal identification to airport authorities upon arrival."
                ],
                "explanation_en": "Paragraph 2 recommends storing encrypted digital scans online and physical copies in separate bags.",
                "explanation_tr": "İkinci paragraf şifreli dijital taramaların bulutta, fiziksel kopyaların ise ayrı bölmelerde tutulmasını önerir."
            },
            {
                "question_en": "Why does the author recommend stepping inside a café to consult map applications?",
                "question_tr_hint": "Yazar harita uygulamalarını incelemek için neden bir kafeye girmeyi önermektedir?",
                "correct_answer": "It avoids looking disoriented and vulnerable to pickpockets at busy street corners.",
                "distractors": [
                    "Because mobile internet only functions inside commercial restaurant kitchens.",
                    "To ensure that coffee shop owners inspect the tourist's itinerary.",
                    "To hide from local municipal transportation authorities."
                ],
                "explanation_en": "Paragraph 3 notes that consulting maps indoors prevents standing visibly disoriented at busy street corners.",
                "explanation_tr": "Üçüncü paragraf, haritalara kapalı mekanlarda bakmanın sokak köşelerinde savunmasız ve şaşkın görünmeyi engellediğini belirtir."
            },
            {
                "question_en": "Why is arriving at new destinations during daytime recommended?",
                "question_tr_hint": "Yeni şehirlere gündüz saatlerinde varmak neden tavsiye edilir?",
                "correct_answer": "It allows travelers to orient themselves and find lodging before dark.",
                "distractors": [
                    "Because hotel doors are legally locked between sunrise and noon.",
                    "Because foreign public transportation stops running during the day.",
                    "Because taxis charge ten times higher fares before evening."
                ],
                "explanation_en": "Paragraph 4 explains that daytime arrivals provide light to navigate and locate accommodations safely.",
                "explanation_tr": "4. paragraf gündüz varışın yön bulma ve otele yerleşme açısından gün ışığı sağladığını belirtir."
            },
            {
                "question_en": "Which statement best captures the author's general attitude toward solo travel?",
                "question_tr_hint": "Hangi ifade yazarın tek başına seyahate yönelik genel tutumunu en iyi yansıtır?",
                "correct_answer": "It is empowering and rewarding when combined with sensible safety measures.",
                "distractors": [
                    "It is overwhelmingly perilous and should be avoided by sensible people.",
                    "It is purely for professional travel vloggers seeking sponsorships.",
                    "It requires military-level physical defense training to survive."
                ],
                "explanation_en": "The passage portrays solo travel as an enriching adventure that flourishes through practical prudence.",
                "explanation_tr": "Metin, tek başına seyahati pratik önlemlerle zenginleşen güçlendirici bir deneyim olarak sunar."
            }
        ]
    ),

    # 3. relationships / leadership (B1, ~470w)
    build_article(
        "reading.b1.cross-generational-mentorship",
        "Bridging Generational Gaps Through Mutual Mentorship",
        "B1", "leadership_and_management",
        "How reciprocal learning partnerships between experienced senior leaders and younger junior professionals create thriving organizational cultures.",
        "Kıdemli yöneticiler ile genç profesyoneller arasındaki karşılıklı öğrenme ortaklıklarının gelişen kurum kültürleri yaratması.",
        ["relationships", "mentorship", "workplace", "career"],
        [
            {
                "paragraph_index": 1,
                "title": "Rethinking the Traditional Hierarchy",
                "content_en": "For decades, professional mentorship operated along a unidirectional path. Senior executives dispensed wisdom, institutional history, and tactical guidance to receptive younger colleagues, while juniors listened respectfully and absorbed industry conventions. However, the accelerating velocity of technological disruption and shifting cultural expectations have rendered this one-way dynamic obsolete. Forward-thinking global enterprises now actively cultivate dynamic mutual mentorship programs where valuable learning flows seamlessly in both directions across diverse organizational cohorts and functional departments. In these progressive workplaces, collective intelligence supersedes traditional tenure as the primary driver of corporate innovation.",
                "content_tr": "On yıllar boyunca mesleki mentorluk tek yönlü bir yolda ilerledi. Kıdemli yöneticiler alıcı genç meslektaşlarına bilgelik, kurumsal tarih ve taktiksel rehberlik dağıtırken, gençler saygıyla dinleyip sektör kurallarını benimsedi. Ancak teknolojik dönüşümün hızlanması ve değişen kültürel beklentiler, bu tek yönlü dinamiği geçersiz kıldı. İleri görüşlü şirketler artık öğrenmenin çalışan grupları arasında her iki yönde de sorunsuz aktığı karşılıklı mentorluk programları geliştirmektedir."
            },
            {
                "paragraph_index": 2,
                "title": "Reciprocal Exchanges of Knowledge",
                "content_en": "In a well-designed, highly structured mutual partnership, each enthusiastic participant brings distinct professional strengths and unique viewpoints to the collaborative table. Senior professionals share seasoned judgment regarding political nuance, stakeholder management, and resilience through economic downturns. Concurrently, early-career colleagues illuminate emerging consumer behaviors, algorithmic social media trends, and collaborative digital platforms. This reciprocal exchange eliminates feelings of condescension. Junior employees gain unprecedented access to high-level strategic thinking, while senior directors stay intimately connected to fast-moving ground-level realities. This balanced alliance bridges institutional knowledge with contemporary agility, accelerating collaborative problem-solving.",
                "content_tr": "İyi tasarlanmış bir karşılıklı ortaklıkta, her katılımcı işbirliği masasına farklı güçler getirir. Kıdemli profesyoneller politik nüanslar, paydaş yönetimi ve ekonomik krizlere karşı dayanıklılık konusundaki deneyimli yargılarını paylaşır. Eş zamanlı olarak, kariyerinin başındaki meslektaşlar yükselen tüketici davranışlarını, algoritmik sosyal medya trendlerini ve işbirlikçi dijital platformları aydınlatır. Bu karşılıklı alışveriş üstten bakma duygusunu ortadan kaldırır. Genç çalışanlar stratejik düşünceye eşsiz bir erişim sağlarken, kıdemli yöneticiler sahadaki gerçeklerle bağlantıda kalır."
            },
            {
                "paragraph_index": 3,
                "title": "Overcoming Cultural Inhibitions",
                "content_en": "Establishing productive cross-generational dialogues requires vulnerability and mutual psychological safety. Junior associates frequently hesitate to offer frank perspectives, fearing their insights might be perceived as presumptuous or disrespectful. Conversely, experienced executives might dread admitting ignorance regarding modern software tools. Organizations succeed when they establish explicit parameters that celebrate inquiry over infallibility. When generational differences transform from points of friction into reservoirs of shared curiosity, workplace cohesion deepens measurably. Colleagues discover that diverse experiences enrich creative debates, transforming potential generational conflict into harmonious progress.",
                "content_tr": "Üretken nesiller arası diyaloglar kurmak savunmasızlık ve karşılıklı psikolojik güvenlik gerektirir. Genç çalışanlar içgörülerinin saygısız veya hadsiz algılanabileceğinden korkarak dürüst bakış açıları sunmakta sıklıkla tereddüt ederler. Tersine, deneyimli yöneticiler modern yazılım araçları konusundaki bilgisizliklerini kabul etmekten çekinebilirler. Kuruluşlar, kusursuzluk yerine sorgulamayı kutlayan açık kurallar belirlediklerinde başarılı olurlar. Kuşak farklılıkları sürtünme noktalarından ortak merak kaynaklarına dönüştüğünde, işyeri uyumu ölçülebilir şekilde derinleşir."
            },
            {
                "paragraph_index": 4,
                "title": "Practical Frameworks for Implementation",
                "content_en": "To prevent these partnerships from dissolving into aimless social chatter, human resource leaders establish structured frameworks. Pairs agree upon monthly focus themes, such as exploring artificial intelligence productivity tools or examining client presentation tactics. Alternating meeting locations between quiet conference rooms and informal campus coffee spots keeps interactions energizing. Both partners maintain shared learning journals to record actionable takeaways after each dialogue session.",
                "content_tr": "Bu ortaklıkların amaçsız sosyal sohbetlere dönüşmesini önlemek için insan kaynakları liderleri yapılandırılmış çerçeveler oluşturur. Çiftler yapay zeka üretkenlik araçlarını keşfetmek veya müşteri sunum taktiklerini incelemek gibi aylık odak temaları üzerinde anlaşırlar. Toplantı yerlerini sessiz konferans salonları ile gayriresmi kampüs kafeleri arasında dönüştürmek etkileşimleri canlı tutar. Her iki ortak da her diyalog oturumundan sonra uygulanabilir çıkarımları kaydetmek için ortak öğrenme günlükleri tutar."
            },
            {
                "paragraph_index": 5,
                "title": "Long-Term Cultural Dividends",
                "content_en": "Over time, cross-generational mentorship yields profound cultural dividends for the entire organization. Junior turnover decreases markedly because emerging talent feels respected and heard by upper management. Meanwhile, senior leaders report feeling revitalized by the enthusiasm and fresh paradigms of their junior counterparts. By transforming age diversity into a strategic asset, forward-looking companies build inclusive, adaptive communities capable of navigating ongoing market disruption. Fostering cross-age empathy creates an enduring competitive advantage that attracts ambitious candidates from every walk of life.",
                "content_tr": "Zamanla, nesiller arası mentorluk tüm organizasyon için derin kültürel getiriler sağlar. Gelişen yetenekler üst yönetim tarafından saygı duyulduğunu ve dinlendiğini hissettiğinden genç çalışan devri belirgin şekilde azalır. Bu arada kıdemli liderler, genç meslektaşlarının coşkusu ve taze paradigmalarıyla yeniden canlandıklarını bildirirler. Yaş çeşitliliğini stratejik bir avantaja dönüştüren ileri görüşlü şirketler, süregelen pazar dalgalanmalarını yönetebilecek kapsayıcı ve uyarlanabilir topluluklar inşa eder."
            }
        ],
        [
            {"word": "velocity", "vocab_id": "vocab.velocity", "context_definition_en": "Speed of motion, action, or operation.", "context_meaning_tr": "hız, sürat"},
            {"word": "reciprocal", "vocab_id": "vocab.reciprocal", "context_definition_en": "Given, felt, or done in return.", "context_meaning_tr": "karşılıklı"},
            {"word": "presumptuous", "vocab_id": "vocab.presumptuous", "context_definition_en": "Failing to observe the limits of what is permitted or appropriate.", "context_meaning_tr": "hadsiz, küstah"}
        ],
        [
            {
                "question_en": "How did traditional professional mentorship primarily function in the past?",
                "question_tr_hint": "Geleneksel mesleki mentorluk geçmişte öncelikle nasıl işliyordu?",
                "correct_answer": "As a one-way transfer where senior managers taught and juniors observed.",
                "distractors": [
                    "As an anonymous automated survey completed once a year.",
                    "As an exchange where junior staff managed senior employee payrolls.",
                    "As a competition where the lowest-rated participant was dismissed."
                ],
                "explanation_en": "Paragraph 1 states that mentorship traditionally operated unidirectionally from senior leaders to juniors.",
                "explanation_tr": "İlk paragraf mentorluğun geleneksel olarak kıdemlilerden gençlere tek yönlü aktığını belirtir."
            },
            {
                "question_en": "What unique perspective do early-career professionals often contribute in modern mutual partnerships?",
                "question_tr_hint": "Kariyerinin başındaki çalışanlar modern karşılıklı ortaklıklara genellikle hangi benzersiz bakış açısını katar?",
                "correct_answer": "Insights into emerging consumer behaviors and novel digital technologies.",
                "distractors": [
                    "Decades of corporate legal expertise and tax compliance history.",
                    "Retirement portfolio strategies and commercial banking agreements.",
                    "Direct authority to approve corporate mergers without board review."
                ],
                "explanation_en": "Paragraph 2 notes that younger colleagues provide insights on consumer behavior, social media trends, and digital tools.",
                "explanation_tr": "İkinci paragraf gençlerin tüketici davranışları ve yeni dijital araçlar hakkında bilgi sunduğunu belirtir."
            },
            {
                "question_en": "What psychological barrier might prevent senior managers from fully engaging in reciprocal learning?",
                "question_tr_hint": "Kıdemli yöneticilerin karşılıklı öğrenmeye tam olarak katılmasını hangi psikolojik engel önleyebilir?",
                "correct_answer": "The discomfort of admitting lack of familiarity with contemporary software tools.",
                "distractors": [
                    "A mandatory company policy forbidding conversations with trainees.",
                    "An inability to read emails written in standard English fonts.",
                    "Severe contractual fines imposed by labor unions for mentoring."
                ],
                "explanation_en": "Paragraph 3 explains that executives might dread admitting ignorance regarding modern software.",
                "explanation_tr": "Üçüncü paragraf yöneticilerin yeni yazılım araçları konusundaki bilgisizliklerini kabul etmekten çekinebileceğini ifade eder."
            },
            {
                "question_en": "How do structured monthly focus themes help mentorship pairs?",
                "question_tr_hint": "Yapılandırılmış aylık odak temaları mentorluk çiftlerine nasıl yardımcı olur?",
                "correct_answer": "They keep discussions actionable and prevent meetings from devolving into aimless chatter.",
                "distractors": [
                    "They allow companies to cancel all annual performance reviews.",
                    "They force partners to complete three-hour written examinations weekly.",
                    "They prohibit partners from speaking during working hours."
                ],
                "explanation_en": "Paragraph 4 explains that monthly focus themes keep partnerships structured and prevent aimless chatter.",
                "explanation_tr": "4. paragraf aylık temaların amaçsız sohbeti önleyerek ortaklığı yapılandırılmış tuttuğunu açıklar."
            },
            {
                "question_en": "What organizational benefit results from effective mutual mentorship?",
                "question_tr_hint": "Etkili karşılıklı mentorluktan hangi kurumsal fayda doğar?",
                "correct_answer": "Lower turnover among junior talent and revitalized senior leadership enthusiasm.",
                "distractors": [
                    "The immediate elimination of all corporate tax responsibilities.",
                    "A total ban on hiring any workers over forty years old.",
                    "Automated stock market valuation increases guaranteed by law."
                ],
                "explanation_en": "Paragraph 5 highlights reduced junior turnover and revitalized senior leadership.",
                "explanation_tr": "5. paragraf azalan genç personel devrini ve yenilenen kıdemli liderlik coşkusunu vurgular."
            }
        ]
    ),

    # 4. education / habits (B1, ~460w)
    build_article(
        "reading.b1.microlearning-daily-habits",
        "The Power of Microlearning in Adult Education",
        "B1", "leadership_and_management",
        "How brief, daily educational increments produce superior knowledge retention and habit consistency compared to marathon weekend study sessions.",
        "Kısa ve günlük eğitim adımlarının, maraton hafta sonu çalışmalarına kıyasla nasıl daha üstün bilgi kalıcılığı ve alışkanlık sağladığı.",
        ["education", "learning", "habits", "productivity"],
        [
            {
                "paragraph_index": 1,
                "title": "The Fallacy of the Weekend Cram",
                "content_en": "Adult professionals eager to master a foreign language or acquire data analysis skills routinely fall into a familiar cognitive trap. Constrained by exhausting weekday work schedules, they postpone study activities until Saturday morning, intending to dedicate five uninterrupted hours to intensive practice. Inevitably, fatigue, domestic obligations, and waning willpower intervene. Even when learners complete these marathons, cognitive research demonstrates that excessive cramming yields rapid forgetting, as overburdened short-term memory fails to consolidate knowledge into durable neural architectures. Cognitive fatigue quickly sets in during long marathons, preventing the mind from synthesizing complex grammatical patterns effectively.",
                "content_tr": "Yabancı bir dilde ustalaşmaya veya veri analizi becerileri kazanmaya hevesli yetişkin profesyoneller, rutin olarak tanıdık bir bilişsel tuzağa düşerler. Hafta içi yorucu çalışma programlarıyla kısıtlanan bu kişiler, yoğun pratiğe beş saat kesintisiz zaman ayırma niyetiyle çalışma faaliyetlerini cumartesi sabahına ertelerler. Kaçınılmaz olarak yorgunluk, ev sorumlulukları ve azalan irade devreye girer. Öğrenenler bu maratonları tamamlasalar bile, aşırı yüklenen kısa süreli bellek bilgiyi dayanıklı sinirsel yapılara aktaramadığı için hızlı bir unutmaya yol açar."
            },
            {
                "paragraph_index": 2,
                "title": "The Mechanics of Spaced Micro-Sessions",
                "content_en": "Microlearning offers an empirically validated alternative tailored to busy adult schedules. Rather than consuming massive modules sporadically, learners engage with tightly focused concepts during concise ten-to-fifteen-minute intervals every day. This methodology leverages the spacing effect—a psychological principle establishing that information reviewed across spaced intervals is retained with dramatically higher fidelity. By revisiting a grammatical contrast or vocabulary cluster repeatedly across multiple days, the brain interprets the stimuli as essential survival data, strengthening synaptic pathways. Each retrieval event reactivates relevant memory traces, gradually moving grammatical concepts into effortless automatic recall.",
                "content_tr": "Mikro öğrenme, yoğun yetişkin programlarına göre uyarlanmış, deneysel olarak doğrulanmış bir alternatif sunar. Öğrenenler büyük modülleri ara sıra tüketmek yerine, her gün on ila on beş dakikalık kısa aralıklarla odaklanmış kavramlarla etkileşime girerler. Bu metodoloji, aralıklı periyotlarla gözden geçirilen bilgilerin çok daha yüksek bir doğrulukla akılda tutulduğunu kanıtlayan aralıklı tekrar etkisinden yararlanır. Bir dilbilgisi ayrımını veya kelime grubunu birden çok gün boyunca tekrar ziyaret ederek beyin bu uyarıcıları temel bilgi olarak yorumlar ve sinapsları güçlendirir."
            },
            {
                "paragraph_index": 3,
                "title": "Frictionless Habit Anchoring",
                "content_en": "The greatest advantage of microlearning lies in behavioral compliance. Initiating a ten-minute vocabulary drill presents minimal psychological resistance, allowing learners to anchor practice sessions directly to existing daily rituals, such as sipping morning espresso or riding public transit. Because the time commitment feels inconsequential, users rarely abandon the habit during stressful work deadlines. Over twelve months, these modest daily increments accumulate into dozens of hours of high-concentration engagement, outperforming sporadic study marathons in fluency, comprehension, and practical application. Consistency triumphs over erratic effort, creating steady and predictable mastery across months of steady dedication.",
                "content_tr": "Mikro öğrenmenin en büyük avantajı davranışsal sürdürülebilirlikte yatar. On dakikalık bir kelime çalışmasını başlatmak minimum psikolojik direnç oluşturur ve öğrenenlerin pratik seanslarını sabah kahvesi yudumlamak veya toplu taşımaya binmek gibi mevcut günlük ritüellere doğrudan bağlamasını sağlar. Zaman taahhüdü önemsiz hissettirdiğinden, kullanıcılar stresli iş teslim tarihlerinde bile alışkanlığı nadiren terk ederler. On iki ay boyunca bu mütevazı günlük artışlar, akıcılık, kavrama ve uygulamada aralıklı çalışma maratonlarını geride bırakarak düzinelerce saatlik yüksek odaklı öğrenmeye dönüşür."
            },
            {
                "paragraph_index": 4,
                "title": "Optimizing Content Design for Retention",
                "content_en": "Effective microlearning experiences are meticulously structured to avoid cognitive overload. Each short lesson addresses a single learning outcome, pairing brief theoretical instruction with an active retrieval exercise. Instead of passively reading through grammar rules, learners immediately manipulate sentence components or identify usage errors. Immediate diagnostic feedback solidifies conceptual clarity before mental fatigue sets in, maximizing the return on educational time invested.",
                "content_tr": "Etkili mikro öğrenme deneyimleri, bilişsel aşırı yüklenmeyi önlemek için titizlikle yapılandırılmıştır. Her kısa ders tek bir öğrenme çıktısını ele alır ve kısa teorik talimatı aktif bir hatırlama alıştırmasıyla eşleştirir. Öğrenenler dilbilgisi kurallarını pasif bir şekilde okumak yerine, cümle bileşenlerini anında manipüle eder veya kullanım hatalarını belirlerler. Anında geri bildirim, zihinsel yorgunluk başlamadan önce kavramsal netliği pekiştirir ve harcanan eğitim süresinin getirisini en üst düzeye çıkarır."
            },
            {
                "paragraph_index": 5,
                "title": "The Compounding Effect of Daily Progress",
                "content_en": "Just as financial investments compound interest silently over decades, microlearning compounds cognitive capability over months. Learners who practice fifteen minutes each morning complete over ninety hours of active language study annually. More importantly, because the knowledge is reinforced daily, vocabulary recall becomes spontaneous and fluid. Small daily habits quietly construct the foundation of lifelong mastery. By embracing the power of modest daily commitments, adult learners achieve linguistic fluency without sacrificing their demanding careers.",
                "content_tr": "Tıpkı finansal yatırımların on yıllar boyunca sessizce bileşik faiz getirmesi gibi, mikro öğrenme de aylar boyunca bilişsel yeteneği katlar. Her sabah on beş dakika pratik yapan öğrenciler, yılda doksan saatin üzerinde aktif dil çalışmasını tamamlar. Daha da önemlisi, bilgi günlük olarak pekiştirildiği için kelime hatırlama kendiliğinden ve akıcı hale gelir. Küçük günlük alışkanlıklar, ömür boyu ustalığın temelini sessizce inşa eder."
            }
        ],
        [
            {"word": "consolidate", "vocab_id": "vocab.consolidate", "context_definition_en": "Combine into a single more effective or coherent whole.", "context_meaning_tr": "pekiştirmek, sağlamlaştırmak"},
            {"word": "sporadically", "vocab_id": "vocab.sporadic", "context_definition_en": "Occurring occasionally, singly, or in irregular instances.", "context_meaning_tr": "düzensizce, ara sıra"},
            {"word": "compliance", "vocab_id": "vocab.compliance", "context_definition_en": "The act of conforming or adhering to a rule or habit.", "context_meaning_tr": "uyum, devamlılık"}
        ],
        [
            {
                "question_en": "Why do lengthy weekend cramming sessions frequently prove ineffective for adult learners?",
                "question_tr_hint": "Hafta sonu yapılan uzun çalışma maratonları yetişkin öğrenenler için neden sıklıkla etkisiz kalır?",
                "correct_answer": "Overburdened short-term memory fails to transfer information into durable long-term retention.",
                "distractors": [
                    "Weekend study sessions are legally prohibited in most international universities.",
                    "Learners completely lose the physical ability to read after working on weekdays.",
                    "Studying on weekends causes computer batteries to degrade twice as fast."
                ],
                "explanation_en": "Paragraph 1 explains that cramming overburdens short-term memory, leading to rapid forgetting.",
                "explanation_tr": "İlk paragraf aşırı yüklemenin kısa süreli belleği zorladığını ve hızlı unutmaya yol açtığını açıklar."
            },
            {
                "question_en": "What cognitive phenomenon underpins the success of microlearning?",
                "question_tr_hint": "Mikro öğrenmenin başarısının temelinde hangi bilişsel olgu yatar?",
                "correct_answer": "The spacing effect, which strengthens retention through distributed intervals.",
                "distractors": [
                    "Subliminal audio absorption during deep sleep cycles.",
                    "Hypnotic suggestion induced through continuous video animation.",
                    "Total immersion without ever reviewing previous mistakes."
                ],
                "explanation_en": "Paragraph 2 explicitly attributes microlearning's efficacy to the spacing effect across intervals.",
                "explanation_tr": "İkinci paragraf, mikro öğrenmenin etkinliğini aralıklı tekrar etkisine dayandırır."
            },
            {
                "question_en": "How does microlearning reduce psychological friction for busy professionals?",
                "question_tr_hint": "Mikro öğrenme yoğun çalışanlar için psikolojik direnci nasıl azaltır?",
                "correct_answer": "Short ten-minute commitments feel effortless and attach easily to daily routines.",
                "distractors": [
                    "It replaces all mental effort with automated artificial intelligence answers.",
                    "It guarantees an instant corporate salary raise upon finishing each drill.",
                    "It requires learners to abandon their employment to focus entirely on study."
                ],
                "explanation_en": "Paragraph 3 notes that brief sessions create minimal resistance and anchor smoothly into existing habits.",
                "explanation_tr": "Üçüncü paragraf, kısa seansların az direnç oluşturduğunu ve günlük ritüellere kolayca bağlandığını belirtir."
            },
            {
                "question_en": "How does pairing brief instruction with active retrieval improve learning?",
                "question_tr_hint": "Kısa talimatı aktif hatırlama ile eşleştirmek öğrenmeyi nasıl geliştirir?",
                "correct_answer": "It provides immediate diagnostic feedback and solidifies concepts before fatigue sets in.",
                "distractors": [
                    "It allows learners to skip all subsequent vocabulary exercises.",
                    "It guarantees a perfect native accent within forty-eight hours.",
                    "It eliminates the need to review any future study materials."
                ],
                "explanation_en": "Paragraph 4 explains that immediate retrieval exercises solidify clarity before fatigue arises.",
                "explanation_tr": "4. paragraf anında hatırlama alıştırmalarının yorgunluk başlamadan netliği pekiştirdiğini açıklar."
            },
            {
                "question_en": "Which learning routine aligns best with the author's advice?",
                "question_tr_hint": "Yazarın tavsiyesine en uygun öğrenme rutini hangisidir?",
                "correct_answer": "Practicing fifteen minutes every morning while drinking coffee.",
                "distractors": [
                    "Completing six continuous hours of flashcards only on Sunday evening.",
                    "Reading grammar textbooks for thirty hours consecutively once a month.",
                    "Avoiding all language exercises until the night before an official exam."
                ],
                "explanation_en": "The author advocates for short, daily sessions integrated into everyday rituals.",
                "explanation_tr": "Yazar günlük ritüellere entegre edilmiş kısa ve düzenli seansları savunmaktadır."
            }
        ]
    ),

    # 5. society / environment (B1, ~450w)
    build_article(
        "reading.b1.urban-community-gardens",
        "The Resurgence of Urban Community Gardens",
        "B1", "engineering_culture",
        "How repurposing neglected municipal parcels into collective green plots fosters ecological awareness, neighborhood solidarity, and fresh nutrition.",
        "İhmal edilmiş kentsel arsaların ortak bahçelere dönüştürülmesinin ekolojik farkındalık, mahalle dayanışması ve taze beslenmeyi teşvik etmesi.",
        ["society", "environment", "community", "sustainability"],
        [
            {
                "paragraph_index": 1,
                "title": "Transforming Concrete Deserts",
                "content_en": "Across densely populated metropolitan landscapes, vacant industrial lots and abandoned parking spaces frequently sit neglected behind chain-link fences. In recent years, spirited neighborhood coalitions have begun reclaiming these derelict parcels, converting barren asphalt into vibrant community gardens. Raised timber beds, rainwater harvesting barrels, and flourishing tomato trellises now substitute for litter and broken concrete. This grassroots ecological movement revitalizes urban aesthetics while providing residents with accessible sanctuaries for physical activity and open-air reflection. These transformed spaces offer an oasis of calm amid the constant rush and concrete expanse of modern urban life.",
                "content_tr": "Yoğun nüfuslu metropollerde, boş sanayi arsaları ve terk edilmiş otoparklar sıklıkla tel örgülerin ardında ihmal edilmiş durumda kalır. Son yıllarda, enerjik mahalle dayanışmaları bu sahipsiz parselleri geri kazanmaya başladı; çorak asfaltı canlı topluluk bahçelerine dönüştürdü. Yükseltilmiş ahşap tarhlar, yağmur suyu toplama varilleri ve gür domates kafesleri artık çöplerin ve kırık betonun yerini alıyor. Bu tabandan gelen ekolojik hareket, kentsel estetiği canlandırırken sakinlere fiziksel aktivite ve açık havada düşünme için erişilebilir sığınaklar sunuyor."
            },
            {
                "paragraph_index": 2,
                "title": "Cultivating Fresh Nutrition and Ecological Literacy",
                "content_en": "The practical benefits of neighborhood gardening extend far beyond ornamental beautification. In low-income neighborhoods characterized as food deserts—where convenience stores outnumber grocery outlets selling fresh vegetables—collective gardens furnish affordable organic produce, including kale, legumes, and aromatic herbs. Furthermore, tending communal plots functions as an experiential classroom. Children observe the delicate pollination cycles of honeybees, master composting fundamentals, and witness how culinary food originates from living soil rather than plastic supermarket packaging. Developing this fundamental appreciation for nature builds lifelong respect for seasonal agriculture and environmental conservation.",
                "content_tr": "Mahalle bahçeciliğinin pratik faydaları süs amaçlı güzelleştirmenin çok ötesine geçer. Taze sebze satan marketlerden çok bakkalların bulunduğu ve gıda çölü olarak nitelendirilen düşük gelirli mahallelerde, ortak bahçeler lahana, baklagiller ve aromatik otlar da dahil olmak üzere uygun fiyatlı organik ürünler sağlar. Ayrıca ortak tarhlarla ilgilenmek deneyimsel bir sınıf işlevi görür. Çocuklar bal arılarının hassas tozlaşma döngülerini gözlemler, kompost yapmanın temellerini öğrenir ve mutfak yiyeceklerinin plastik market ambalajlarından değil yaşayan topraktan nasıl geldiğine tanık olurlar."
            },
            {
                "paragraph_index": 3,
                "title": "Rebuilding Fractured Social Fabric",
                "content_en": "Perhaps the most enduring transformation sparked by community gardens is social rather than botanical. Modern apartment buildings frequently promote anonymity, where neighbors reside side by side for years without exchanging warm introductions. Working shoulder to shoulder to weed carrot beds or repair drip irrigation hoses dissolves demographic barriers. Retirees share generational farming techniques with tech-industry newcomers, fostering authentic mutual empathy. These cultivated green commons transform disconnected city blocks into interdependent, caring communities. Working together toward a common harvest restores a sense of shared purpose that is often lacking in fast-paced modern cities.",
                "content_tr": "Belki de topluluk bahçelerinin yol açtığı en kalıcı dönüşüm botanikten ziyade sosyaldir. Modern apartman binaları sıklıkla sakinlerin sıcak bir selam bile vermeden yıllarca yan yana yaşadığı bir yabancılaşmayı teşvik eder. Havuç tarhlarını yabani otlardan arındırmak veya damla sulama hortumlarını onarmak için omuz omuza çalışmak demografik engelleri eritir. Emekliler nesiller arası tarım tekniklerini teknoloji sektörü çalışanlarıyla paylaşarak samimi bir empati oluşturur. Bu ekilen ortak yeşil alanlar, birbirinden kopuk şehir bloklarını birbirine bağlı, duyarlı topluluklara dönüştürür."
            },
            {
                "paragraph_index": 4,
                "title": "Ecological Buffers Against Urban Heat",
                "content_en": "Beyond direct nutritional and communal gains, community gardens serve vital environmental functions within urban ecosystems. Densely built urban centers suffer from the urban heat island effect, where dark masonry and asphalt absorb solar radiation, raising local temperatures. Vegetated garden parcels absorb rainwater runoff, replenish groundwater reserves, and cool surrounding city air through evapotranspiration. They also provide vital corridors of biodiversity for urban songbirds and beneficial insect pollinators.",
                "content_tr": "Topluluk bahçeleri doğrudan beslenme ve toplumsal kazanımların ötesinde, kentsel ekosistemlerde hayati çevresel işlevler görür. Yoğun yapılaşmış şehir merkezleri, koyu taş ve asfaltın güneş radyasyonunu emerek yerel sıcaklıkları yükselttiği kentsel ısı adası etkisinden muzdariptir. Bitki örtülü bahçe parselleri yağmur suyu akışını emer, yeraltı suyu rezervlerini yeniler ve terleme yoluyla çevredeki şehir havasını soğutur. Ayrıca ötücü kuşlar ve faydalı böcek tozlaştırıcıları için hayati biyolojik çeşitlilik koridorları sağlarlar."
            },
            {
                "paragraph_index": 5,
                "title": "Advocating for Permanent Civic Support",
                "content_en": "Despite their evident advantages, urban gardens frequently face existential threats from commercial real estate speculation. To ensure their long-term survival, proactive community organizers partner with municipal governments to secure permanent zoning protections and water access rights. Preserving green commons is increasingly recognized as a fundamental element of equitable, sustainable city planning that champions human well-being over unconstrained commercial expansion. Integrating nature into municipal design creates healthier, happier, and more resilient urban populations for generations to come.",
                "content_tr": "Belirgin avantajlarına rağmen, şehir bahçeleri ticari gayrimenkul spekülasyonlarından kaynaklanan varoluşsal tehditlerle sık sık karşı karşıya kalır. Uzun vadeli hayatta kalmalarını sağlamak için proaktif topluluk organizatörleri, kalıcı imar korumaları ve su erişim hakları elde etmek üzere belediye yönetimleriyle ortaklık kurar. Ortak yeşil alanların korunması, kısıtlamasız ticari genişleme yerine insan refahını savunan eşitlikçi ve sürdürülebilir şehir planlamasının temel bir unsuru olarak giderek daha fazla kabul görmektedir."
            }
        ],
        [
            {"word": "derelict", "vocab_id": "vocab.derelict", "context_definition_en": "In a very poor condition as a result of disuse and neglect.", "context_meaning_tr": "terk edilmiş, sahipsiz"},
            {"word": "pollination", "vocab_id": "vocab.pollination", "context_definition_en": "The transfer of pollen to enable plant fertilization.", "context_meaning_tr": "tozlaşma"},
            {"word": "anonymity", "vocab_id": "vocab.anonymity", "context_definition_en": "The condition of being unknown or unacknowledged.", "context_meaning_tr": "yabancılaşma, isimsizlik"}
        ],
        [
            {
                "question_en": "What types of urban spaces are typically transformed into community gardens?",
                "question_tr_hint": "Genellikle ne tür kentsel alanlar topluluk bahçelerine dönüştürülmektedir?",
                "correct_answer": "Vacant industrial lots and neglected parking areas.",
                "distractors": [
                    "Active commercial airport runways.",
                    "Underground subway tunnels in continuous operation.",
                    "Historic government legislative chambers."
                ],
                "explanation_en": "Paragraph 1 mentions that neglected industrial parcels and abandoned parking lots are reclaimed.",
                "explanation_tr": "İlk paragraf ihmal edilmiş sanayi arsaları ve terk edilmiş otoparkların dönüştürüldüğünü belirtir."
            },
            {
                "question_en": "What educational value do children gain from participating in communal gardens?",
                "question_tr_hint": "Çocuklar topluluk bahçelerine katılarak hangi eğitici değeri kazanırlar?",
                "correct_answer": "They witness natural ecological cycles and learn where fresh food originates.",
                "distractors": [
                    "They learn how to operate complex industrial harvesting combines.",
                    "They memorize international commodity futures trading codes.",
                    "They obtain certified university degrees in corporate management."
                ],
                "explanation_en": "Paragraph 2 explains that children observe pollination, composting, and understand food origins.",
                "explanation_tr": "İkinci paragraf çocukların tozlaşmayı, kompostu ve gıdanın kökenini öğrendiklerini açıklar."
            },
            {
                "question_en": "What social benefit is highlighted as the most significant outcome of these initiatives?",
                "question_tr_hint": "Bu girişimlerin en önemli sonucu olarak hangi sosyal fayda vurgulanmaktadır?",
                "correct_answer": "Connecting diverse neighbors and dismantling urban social isolation.",
                "distractors": [
                    "Completely eliminating all municipal property taxes across the city.",
                    "Banning all personal automobile ownership within city limits.",
                    "Replacing all local grocery stores with mandatory barter markets."
                ],
                "explanation_en": "Paragraph 3 discusses how gardening brings neighbors together across generations and dissolves anonymity.",
                "explanation_tr": "Üçüncü paragraf bahçeciliğin komşuları bir araya getirdiğini ve kentsel yalnızlığı ortadan kaldırdığını belirtir."
            },
            {
                "question_en": "How do urban gardens help mitigate the urban heat island effect?",
                "question_tr_hint": "Şehir bahçeleri kentsel ısı adası etkisini azaltmaya nasıl yardımcı olur?",
                "correct_answer": "By absorbing rainwater and cooling surrounding air through plant evapotranspiration.",
                "distractors": [
                    "By reflecting all sunlight back into outer space like mirrors.",
                    "By blowing artificial air-conditioning across municipal avenues.",
                    "By covering city sidewalks with thick layers of plastic sheeting."
                ],
                "explanation_en": "Paragraph 4 explains that vegetation absorbs runoff and cools city air via evapotranspiration.",
                "explanation_tr": "4. paragraf bitki örtüsünün suyu emip terleme yoluyla şehir havasını soğuttuğunu açıklar."
            },
            {
                "question_en": "What primary threat do community gardens face according to the text?",
                "question_tr_hint": "Metne göre topluluk bahçeleri hangi temel tehditle karşı karşıyadır?",
                "correct_answer": "Commercial real estate speculation threatening their land use.",
                "distractors": [
                    "Severe plagues of locusts destroying all plant leaves.",
                    "A total lack of interest from neighborhood volunteers.",
                    "Mandatory international treaties prohibiting vegetables."
                ],
                "explanation_en": "Paragraph 5 mentions that gardens often face existential threats from real estate speculation.",
                "explanation_tr": "5. paragraf bahçelerin sıklıkla gayrimenkul spekülasyonu tehdidiyle karşılaştığını belirtir."
            }
        ]
    ),

    # 6. culture / technology (B1, ~460w)
    build_article(
        "reading.b1.museums-digital-preservation",
        "The Digital Reimagining of Cultural Heritage",
        "B1", "technology",
        "How high-resolution 3D scanning, virtual reality, and open-access databases democratize museum collections worldwide.",
        "Yüksek çözünürlüklü 3D tarama, sanal gerçeklik ve açık erişimli veri tabanlarının dünya çapındaki müze koleksiyonlarını nasıl demokratikleştirdiği.",
        ["culture", "technology", "history", "preservation"],
        [
            {
                "paragraph_index": 1,
                "title": "Opening the Vaults of History",
                "content_en": "For centuries, prestigious cultural institutions operated as guarded sanctuaries of rare human heritage. Physical collections of ancient pottery, Renaissance canvases, and delicate historical manuscripts were locked within climate-controlled display galleries accessible exclusively to visitors possessing the financial means to travel to major global capitals. Even within world-renowned museums, curators typically exhibit less than ten percent of their archived holdings due to physical space constraints. Today, digital preservation technologies are dismantling these historical barriers, unlocking rare cultural artifacts for global discovery. Transforming restricted museum vaults into decentralized digital libraries unlocks our collective human past for curious minds everywhere.",
                "content_tr": "Yüzyıllar boyunca prestijli kültür kurumları, nadir insan mirasının korunan sığınakları olarak faaliyet gösterdi. Antik çanak çömlekler, Rönesans tuvalleri ve narin tarihi el yazmalarından oluşan fiziksel koleksiyonlar, yalnızca büyük dünya başkentlerine seyahat edecek maddi güce sahip ziyaretçilerin erişebildiği iklim kontrollü galerilerde kilitli tutuldu. Dünyaca ünlü müzelerde bile küratörler, fiziksel alan kısıtlamaları nedeniyle arşivlerindeki eserlerin yalnızca yüzde onundan daha azını sergileyebilmektedir. Günümüzde dijital koruma teknolojileri bu tarihi engelleri yıkarak nadir kültürel eserleri küresel keşfe açmaktadır."
            },
            {
                "paragraph_index": 2,
                "title": "Precision Scanning and Immersive Reconstruction",
                "content_en": "The technical engine of this cultural renaissance relies on cutting-edge photogrammetry and structured-light 3D laser scanning. Conservators capture millions of data points from vulnerable antiquities, creating millimeter-accurate digital replicas that record subtle brushwork textures and microscopic surface fractures. These models serve dual imperatives. First, they establish indelible archival backups against natural disasters, armed conflicts, or accidental destruction. Second, they power immersive virtual reality environments where students in remote regions inspect intricate Egyptian amulets as though holding them in their own palms. High-fidelity rendering captures microscopic textures with astonishing realism, allowing researchers worldwide to conduct detailed academic examinations remotely.",
                "content_tr": "Bu kültürel rönesansın teknik motoru, ileri düzey fotogrametri ve yapılandırılmış ışıkla 3D lazer taramaya dayanmaktadır. Konservatörler, hassas antik eserlerden milyonlarca veri noktası toplayarak ince fırça darbelerini ve mikroskobik yüzey çatlaklarını kaydeden milimetre hassasiyetinde dijital kopyalar oluştururlar. Bu modeller ikili bir amaca hizmet eder. Birincisi, doğal afetler, silahlı çatışmalar veya kazara yıkımlara karşı silinmez arşiv yedekleri oluştururlar. İkincisi, uzak bölgelerdeki öğrencilerin Mısır muskalarını sanki kendi avuçlarında tutuyormuş gibi inceleyebilecekleri sürükleyici sanal gerçeklik ortamlarına güç verirler."
            },
            {
                "paragraph_index": 3,
                "title": "Democratizing Intellectual Ownership",
                "content_en": "Beyond captivating visuals, the philosophical impact of open-access digital heritage is profound. Progressive institutions now release high-resolution artifact scans into the public domain under unrestricted licenses, encouraging educators, game developers, and independent scholars to incorporate historical models into innovative creative projects. By relinquishing rigid institutional monopolies over visual access, museums evolve from passive repositories of antiquity into dynamic conduits of decentralized education, ensuring that humanity's shared historical treasures inspire future generations regardless of geographic borders. Open access transforms culture from a privileged commodity into a universal birthright that enriches global human understanding.",
                "content_tr": "Büyüleyici görsellerin ötesinde, açık erişimli dijital mirasın felsefi etkisi çok derindir. İlerici kurumlar artık yüksek çözünürlüklü eser taramalarını kısıtlamasız lisanslarla kamuya açarak eğitimcileri, oyun geliştiricilerini ve bağımsız araştırmacıları tarihi modelleri yaratıcı projelere dahil etmeye teşvik etmektedir. Görsel erişim üzerindeki katı kurumsal tekellerinden vazgeçen müzeler, antik çağın pasif depolarından merkeziyetsiz eğitimin dinamik kanallarına dönüşerek insanlığın ortak mirasının coğrafi sınırlardan bağımsız olarak gelecek nesillere ilham vermesini sağlamaktadır."
            },
            {
                "paragraph_index": 4,
                "title": "Restoring Context to Displaced Antiquities",
                "content_en": "Digital curation also helps resolve thorny ethical disputes surrounding looted or disputed colonial artifacts. Through virtual repatriation initiatives, museums reconstruct ancient archaeological sites digitally, bringing together dispersed treasures that currently reside in separate institutions across multiple continents. Scholars and indigenous communities can interact with complete, unified cultural assemblages in virtual reality, re-establishing historical context that was fragmented by centuries of imperial trade.",
                "content_tr": "Dijital kürasyon, yağmalanmış veya ihtilaflı sömürge eserlerini çevreleyen çetrefilli etik anlaşmazlıkların çözülmesine de yardımcı olur. Sanal iade girişimleri aracılığıyla müzeler, antik arkeolojik alanları dijital olarak yeniden inşa ederek şu anda birden fazla kıtadaki ayrı kurumlarda bulunan dağınık hazineleri bir araya getirir. Bilim insanları ve yerli topluluklar sanal gerçeklikte eksiksiz, birleşik kültürel topluluklarla etkileşime girerek yüzyıllardır süren emperyal ticaretle parçalanmış tarihi bağlamı yeniden kurabilirler."
            },
            {
                "paragraph_index": 5,
                "title": "The Future of Participatory Curation",
                "content_en": "Looking forward, digital collections will become increasingly interactive through machine learning and participatory tagging. Visitors will curate personal virtual exhibitions, annotate artifacts with regional folklore, and compare artistic styles across historical eras using algorithmic visual search. In this decentralized cultural ecosystem, preservation transcends passive safeguarding, becoming an active, worldwide collaboration in collective memory. Connecting international audiences through digital storytelling guarantees that historic treasures remain relevant, accessible, and deeply cherished across centuries.",
                "content_tr": "Geleceğe bakıldığında, dijital koleksiyonlar makine öğrenimi ve katılımcı etiketleme yoluyla giderek daha etkileşimli hale gelecektir. Ziyaretçiler kişisel sanal sergilerin küratörlüğünü yapacak, eserlere bölgesel folklor notları ekleyecek ve algoritmik görsel arama kullanarak tarihi dönemler arasındaki sanatsal stilleri karşılaştıracak. Bu merkeziyetsiz kültürel ekosistemde koruma, pasif muhafazayı aşarak kolektif hafızada dünya çapında aktif bir işbirliğine dönüşür."
            }
        ],
        [
            {"word": "manuscripts", "vocab_id": "vocab.manuscript", "context_definition_en": "Texts written by hand or ancient historical documents.", "context_meaning_tr": "el yazmaları"},
            {"word": "indelible", "vocab_id": "vocab.indelible", "context_definition_en": "Making marks that cannot be removed; permanent.", "context_meaning_tr": "silinmez, kalıcı"},
            {"word": "relinquishing", "vocab_id": "vocab.relinquish", "context_definition_en": "Voluntarily ceasing to claim or keep; giving up.", "context_meaning_tr": "feragat etme, bırakma"}
        ],
        [
            {
                "question_en": "Why are most items in traditional museum collections hidden from general view?",
                "question_tr_hint": "Geleneksel müze koleksiyonlarındaki eserlerin çoğu neden halkın gözünden uzaktır?",
                "correct_answer": "Museums lack sufficient physical display space to exhibit all their archived holdings.",
                "distractors": [
                    "International laws forbid displaying ancient art to non-specialists.",
                    "All historic canvases crumble immediately upon contact with indoor electric lighting.",
                    "Museums are legally required to sell ninety percent of their inventory every month."
                ],
                "explanation_en": "Paragraph 1 states that curators exhibit less than ten percent of their holdings due to physical space limitations.",
                "explanation_tr": "İlk paragraf küratörlerin fiziksel alan yetersizliği nedeniyle eserlerin yüzde onundan azını sergileyebildiğini belirtir."
            },
            {
                "question_en": "How does 3D laser scanning protect cultural heritage against unforeseen disasters?",
                "question_tr_hint": "3D lazer tarama kültürel mirası beklenmedik felaketlere karşı nasıl korur?",
                "correct_answer": "It provides permanent, millimeter-accurate digital replicas for archival restoration.",
                "distractors": [
                    "It makes stone statues immune to earthquakes and bomb blasts.",
                    "It physically teleports original artifacts into underground bunkers.",
                    "It automatically reconstructs destroyed buildings within twenty minutes."
                ],
                "explanation_en": "Paragraph 2 states that scanning creates indelible archival backups against conflict or destruction.",
                "explanation_tr": "İkinci paragraf taramaların yıkıma karşı silinmez arşiv yedekleri sağladığını ifade eder."
            },
            {
                "question_en": "What occurs when cultural institutions release scans into the public domain?",
                "question_tr_hint": "Kültür kurumları taramaları kamuya açık lisanslarla yayınladığında ne olur?",
                "correct_answer": "Independent creators and educators can use the models freely in creative projects.",
                "distractors": [
                    "The museum immediately goes bankrupt and closes forever.",
                    "Original historical artifacts lose all monetary value on art markets.",
                    "Governments confiscate the computers of all contributing curators."
                ],
                "explanation_en": "Paragraph 3 explains that open-access licenses allow educators and creators to integrate models into projects.",
                "explanation_tr": "Üçüncü paragraf açık erişim lisanslarının yaratıcıların bu modelleri serbestçe kullanmasına imkan tanıdığını belirtir."
            },
            {
                "question_en": "How does virtual repatriation help address controversies over displaced artifacts?",
                "question_tr_hint": "Sanal iade, yeri değiştirilmiş eserlerle ilgili tartışmaları çözmeye nasıl yardımcı olur?",
                "correct_answer": "By reuniting dispersed artifacts digitally in their original historical archaeological context.",
                "distractors": [
                    "By melting down all historical relics to mint modern gold coins.",
                    "By forcing museums to erase all historical records of colonialism.",
                    "By replacing all physical museum buildings with shopping centers."
                ],
                "explanation_en": "Paragraph 4 explains that virtual repatriation digitally unifies dispersed cultural treasures in context.",
                "explanation_tr": "4. paragraf sanal iadenin farklı yerlerdeki eserleri dijital olarak orijinal bağlamında birleştirdiğini açıklar."
            },
            {
                "question_en": "Which statement summarizes the central thesis of the article?",
                "question_tr_hint": "Hangi ifade makalenin ana tezini özetler?",
                "correct_answer": "Digital technology democratizes historical access and safeguards cultural heritage globally.",
                "distractors": [
                    "Physical museums will be completely demolished within the next decade.",
                    "Virtual reality headsets cause irreversible harm to historical comprehension.",
                    "Only trained historians should be permitted to view 3D digital scans."
                ],
                "explanation_en": "The article demonstrates how digital technologies expand cultural access and protect global heritage.",
                "explanation_tr": "Makale, dijital teknolojilerin kültürel erişimi genişlettiğini ve küresel mirası koruduğunu gösterir."
            }
        ]
    ),

    # 7. environment / business (B1, ~450w)
    build_article(
        "reading.b1.circular-economy-household",
        "The Circular Economy in Everyday Household Living",
        "B1", "business_strategy",
        "Transitioning from linear consumer waste to circular resource stewardship through repairing, repurposing, and borrowing.",
        "Tamir etme, yeniden değerlendirme ve ödünç alma yoluyla doğrusal tüketici israfından döngüsel kaynak yönetimine geçiş.",
        ["environment", "sustainability", "economy", "lifestyle"],
        [
            {
                "paragraph_index": 1,
                "title": "The Pitfalls of the Throwaway Model",
                "content_en": "For the past century, industrial economies operated according to an extractive linear paradigm: take virgin natural resources, manufacture disposable consumer goods, and discard products into landfills once damaged. This take-make-waste cycle has generated unprecedented volumes of municipal refuse and accelerated ecological degradation. Cheap manufacturing often makes replacing a malfunctioning toaster or garment seem faster and less expensive than seeking specialized repair. However, this illusion of affordability conceals immense ecological costs, including carbon-heavy transportation and toxic microplastic pollution. Throwaway consumer goods deplete precious planetary resources while burdening municipal authorities with unsustainable mountains of unmanageable waste.",
                "content_tr": "Geçtiğimiz yüzyıl boyunca sanayi ekonomileri sömürüye dayalı doğrusal bir modele göre işledi: işlenmemiş doğal kaynakları al, tek kullanımlık tüketim malları üret ve hasar gördüğünde ürünleri çöplüklere at. Bu 'al-yap-at' döngüsü benzeri görülmemiş miktarda kentsel atık üretti ve ekolojik bozulmayı hızlandırdı. Ucuz üretim, arızalı bir ekmek kızartma makinesini veya giysiyi tamir ettirmek yerine yenisiyle değiştirmeyi genellikle daha hızlı ve daha ucuz gösterir. Ancak bu ekonomiklik yanılsaması, karbon yoğun taşımacılık ve toksik mikroplastik kirliliği de dahil olmak üzere muazzam ekolojik maliyetleri gizler."
            },
            {
                "paragraph_index": 2,
                "title": "Reimagining Ownership and Durability",
                "content_en": "In contrast to linear extraction, the circular economy conceives of products as perpetual loops of utility. At the domestic level, adopting circular principles begins with rethinking private ownership. Rather than purchasing specialized tools that sit idle in garage cabinets for three hundred and sixty days a year, households participate in community tool libraries. In these lending hubs, neighbors borrow lawn aerators, high-pressure washers, and tile cutters as needed. Shared access preserves raw materials, minimizes household clutter, and frees household budgets for meaningful life experiences. Shifting from ownership to access reduces clutter in modern homes while establishing friendly connections among local neighborhood residents.",
                "content_tr": "Doğrusal sömürünün aksine, döngüsel ekonomi ürünleri sürekli bir fayda döngüsü olarak tasarlar. Ev düzeyinde döngüsel ilkeleri benimsemek, özel mülkiyeti yeniden düşünmekle başlar. Yılda üç yüz altmış gün garaj dolaplarında boş duran özel aletleri satın almak yerine, haneler topluluk alet kütüphanelerine katılır. Bu paylaşım merkezlerinde komşular ihtiyaç duydukça çim havalandırıcıları, yüksek basınçlı yıkayıcıları ve fayans kesicileri ödünç alırlar. Ortak erişim ham maddeleri korur, evdeki dağınıklığı en aza indirir ve hane bütçelerini anlamlı yaşam deneyimleri için serbest bırakır."
            },
            {
                "paragraph_index": 3,
                "title": "The Renaissance of Domestic Repair",
                "content_en": "Another cornerstone of the circular household is reviving the dignity of repair. Community repair cafés—where volunteer mechanics and seamstresses help neighbors fix broken blenders, replace frayed wiring, and mend torn jackets—are flourishing globally. In addition to diverting metric tons of functional machinery from waste dumps, these collaborative events impart practical technical competencies to younger generations. Embracing circular habits transforms consumers from passive discarders of disposable goods into conscious stewards of material culture. Developing basic mechanical competencies fosters a healthy sense of self-reliance and profound respect for human craftsmanship.",
                "content_tr": "Döngüsel evin bir diğer köşe taşı da onarımın itibarını canlandırmaktır. Gönüllü tamircilerin ve terzilerin komşularının bozuk blenderları tamir etmelerine, yıpranmış kabloları değiştirmelerine ve yırtık ceketleri onarmalarına yardımcı olduğu tamir kafeleri dünya çapında yaygınlaşmaktadır. Bu işbirlikçi etkinlikler, tonlarca işlevsel makinenin çöplüklere gitmesini engellemenin yanı sıra genç nesillere pratik teknik beceriler kazandırır. Döngüsel alışkanlıkları benimsemek, tüketicileri tek kullanımlık malların pasif atıcılarından maddi kültürün bilinçli yöneticilerine dönüştürür."
            },
            {
                "paragraph_index": 4,
                "title": "Closed-Loop Food and Organic Waste",
                "content_en": "Circular living also extends into the domestic kitchen through organic waste management. Discarded vegetable peelings and coffee grounds, which emit methane gas when rotting inside anaerobic landfills, become nutrient-rich compost when decomposed properly in backyard bins or municipal collection systems. Returning this organic matter to garden beds closes the biological loop, rejuvenating topsoil and reducing the necessity for synthetic chemical fertilizers.",
                "content_tr": "Döngüsel yaşam, organik atık yönetimi yoluyla ev mutfağına da uzanır. Havasız depolama alanlarında çürürken metan gazı yayan atık sebze kabukları ve kahve telvesi, arka bahçe kutularında veya belediye toplama sistemlerinde uygun şekilde ayrıştırıldığında besin açısından zengin kompost haline gelir. Bu organik maddeyi bahçe tarhlarına geri döndürmek biyolojik döngüyü kapatır, üst toprağı canlandırır ve sentetik kimyasal gübre ihtiyacını azaltır."
            },
            {
                "paragraph_index": 5,
                "title": "Consumer Demand Driving Policy Reform",
                "content_en": "Ultimately, individual household choices aggregate into powerful market signals that compel corporate manufacturers to adopt circular product design. Consumers increasingly demand modular electronic devices with easily replaceable batteries and publicly available schematics. Supported by legislative right-to-repair statutes in progressive jurisdictions, the circular economy is steadily evolving from an idealistic personal lifestyle into the dominant industrial standard of the twenty-first century. Embracing closed-loop economic principles protects our shared biosphere while building resilient, prosperous communities for future generations to enjoy.",
                "content_tr": "Nihayetinde bireysel hane seçimleri, kurumsal üreticileri döngüsel ürün tasarımını benimsemeye zorlayan güçlü pazar sinyallerine dönüşür. Tüketiciler, kolayca değiştirilebilir pillere ve kamuya açık şemalara sahip modüler elektronik cihazları giderek daha fazla talep etmektedir. İlerici yargı bölgelerindeki yasal tamir hakkı yasalarıyla desteklenen döngüsel ekonomi, idealist bir kişisel yaşam tarzından yirmi birinci yüzyılın baskın endüstri standardına doğru istikrarlı bir şekilde gelişmektedir."
            }
        ],
        [
            {"word": "extractive", "vocab_id": "vocab.extractive", "context_definition_en": "Relating to the withdrawal of natural resources.", "context_meaning_tr": "sömürüye dayalı, kaynak tüketen"},
            {"word": "perpetual", "vocab_id": "vocab.perpetual", "context_definition_en": "Never ending or changing; occurring repeatedly.", "context_meaning_tr": "sürekli, daimi"},
            {"word": "stewards", "vocab_id": "vocab.steward", "context_definition_en": "People who responsibly oversee and protect resources.", "context_meaning_tr": "koruyucular, yöneticiler"}
        ],
        [
            {
                "question_en": "What defines the traditional linear economic model described in the passage?",
                "question_tr_hint": "Metinde tarif edilen geleneksel doğrusal ekonomik modeli ne tanımlar?",
                "correct_answer": "Extracting virgin resources, manufacturing goods, and discarding them into landfills.",
                "distractors": [
                    "Reusing all manufactured items continuously without any raw materials.",
                    "Exchanging food grains for manual labor without currency.",
                    "Distributing all manufactured goods for free through national lotteries."
                ],
                "explanation_en": "Paragraph 1 characterizes the linear model as a 'take-make-waste' cycle resulting in landfill dumping.",
                "explanation_tr": "İlk paragraf doğrusal modeli çöplüklerle sonuçlanan bir 'al-yap-at' döngüsü olarak tanımlar."
            },
            {
                "question_en": "How do tool lending libraries support circular economy principles?",
                "question_tr_hint": "Alet kütüphaneleri döngüsel ekonomi ilkelerini nasıl destekler?",
                "correct_answer": "They enable shared access to infrequently used equipment, reducing resource consumption.",
                "distractors": [
                    "They force every citizen to purchase five sets of industrial equipment.",
                    "They require members to manufacture heavy machinery in their backyards.",
                    "They permanently confiscate all private personal possessions."
                ],
                "explanation_en": "Paragraph 2 explains that tool libraries allow shared use of tools that would otherwise sit idle.",
                "explanation_tr": "İkinci paragraf alet kütüphanelerinin nadiren kullanılan ekipmanların ortaklaşa kullanımını sağladığını belirtir."
            },
            {
                "question_en": "What occurs at community repair cafés?",
                "question_tr_hint": "Topluluk tamir kafelerinde ne gerçekleşir?",
                "correct_answer": "Volunteers help neighbors fix broken household goods and teach repair skills.",
                "distractors": [
                    "Participants purchase mass-produced disposable electronic toys.",
                    "Citizens hold auctions to sell damaged property to municipal dumps.",
                    "Technicians destroy broken appliances with heavy sledgehammers."
                ],
                "explanation_en": "Paragraph 3 describes repair cafés where volunteers mend appliances and pass on practical skills.",
                "explanation_tr": "Üçüncü paragraf tamir kafelerinde gönüllülerin ev aletlerini onardığını ve beceri aktardığını anlatır."
            },
            {
                "question_en": "Why is composting food scraps preferable to landfill disposal?",
                "question_tr_hint": "Yiyecek artıklarını kompostlamak neden çöplüklere atmaktan daha iyidir?",
                "correct_answer": "It avoids methane emissions and produces nutrient-rich organic fertilizer.",
                "distractors": [
                    "It converts kitchen waste into solid radioactive uranium bars.",
                    "It completely stops rain from falling in agricultural regions.",
                    "It eliminates the need for municipal drinking water systems."
                ],
                "explanation_en": "Paragraph 4 explains that composting prevents methane emissions from landfills and rejuvenates topsoil.",
                "explanation_tr": "4. paragraf kompost yapmanın çöplüklerdeki metan salınımını önlediğini ve toprağı yenilediğini açıklar."
            },
            {
                "question_en": "What overarching behavioral shift does the author advocate for consumers?",
                "question_tr_hint": "Yazar tüketiciler için hangi kapsamlı davranışsal değişimi savunmaktadır?",
                "correct_answer": "Becoming mindful stewards of materials rather than passive disposers.",
                "distractors": [
                    "Ceasing all consumption of modern medical pharmaceuticals.",
                    "Refusing to participate in any local neighborhood activities.",
                    "Buying double the amount of disposable plastic utensils."
                ],
                "explanation_en": "The final sentence of paragraph 3 emphasizes becoming conscious stewards of material culture. Developing basic mechanical competencies fosters a healthy sense of self-reliance and profound respect for human craftsmanship.",
                "explanation_tr": "3. paragrafın son cümlesi maddi kültürün bilinçli yöneticileri haline gelmeyi vurgulamaktadır."
            }
        ]
    ),

    # 8. science / health (B1, ~470w)
    build_article(
        "reading.b1.sleep-hygiene-productivity",
        "The Circadian Blueprint for Restorative Sleep",
        "B1", "workplace_communication",
        "How aligning daily light exposure, evening digital habits, and temperature optimizes cognitive performance and emotional stability.",
        "Günlük ışık maruziyetini, akşam dijital alışkanlıklarını ve oda sıcaklığını uyumlu hale getirmenin bilişsel performansı ve duygusal dengeyi nasıl optimize ettiği.",
        ["science", "health-lifestyle", "sleep", "productivity"],
        [
            {
                "paragraph_index": 1,
                "title": "The Chronic Epidemic of Rest Deprivation",
                "content_en": "In modern corporate cultures that glorify relentless ambition, sleep has frequently been dismissed as a negotiable luxury. Ambitious professionals boast of operating on four or five hours of fragmented rest, relying on caffeinated energy drinks to sustain alertness throughout lengthy meetings. Yet neuroscience presents an uncompromising verdict: chronic sleep deprivation severely impairs the prefrontal cortex, the cerebral control center responsible for executive function, emotional regulation, and strategic risk assessment. Deprived of restorative slumber, individuals experience diminished memory consolidation, elevated cortisol levels, and heightened emotional reactivity. Operating in a state of continuous exhaustion degrades interpersonal relationships and undermines sound decision-making at work.",
                "content_tr": "Aralıksız hırsı yücelten modern şirket kültürlerinde uyku sıklıkla feda edilebilir bir lüks olarak görülmüştür. Hırslı profesyoneller, uzun toplantılar boyunca uyanık kalmak için kafeinli enerji içeceklerine güvenerek dört ya da beş saatlik bölünmüş dinlenmeyle çalıştıklarını övünerek anlatırlar. Oysa sinirbilim tavizsiz bir karar sunar: kronik uyku yoksunluğu yürütücü işlevlerden, duygusal düzenlemeden ve stratejik risk değerlendirmesinden sorumlu beyin kontrol merkezi olan prefrontal kortekse ciddi şekilde zarar verir. Onarıcı uykudan mahrum kalan bireyler azalmış bellek pekiştirmesi, yükselen kortizol seviyeleri ve yüksek duygusal tepkisellik yaşarlar."
            },
            {
                "paragraph_index": 2,
                "title": "Light Signals and Melatonin Secretion",
                "content_en": "Our internal twenty-four-hour circadian rhythm is governed by the suprachiasmatic nucleus, an internal pacemaker synchronized by ambient environmental illumination. When natural morning sunlight hits specialized retinal ganglion cells, it triggers an immediate cortisol peak that awakens the metabolism while resetting the internal countdown to evening drowsiness. Conversely, exposure to high-intensity blue light emitted by smartphones, laptop screens, and harsh overhead light-emitting diodes after sunset tricks the brain into simulating midday. This inhibits the timely secretion of melatonin, delaying sleep onset and fragmenting rapid-eye-movement cycles. Diminished rapid-eye-movement sleep impairs emotional regulation, making individuals significantly more vulnerable to acute workplace stress.",
                "content_tr": "Yirmi dört saatlik iç sirkadiyen ritmimiz, ortam aydınlatması ile senkronize olan bir iç kalp pili niteliğindeki suprakiyazmatik çekirdek tarafından yönetilir. Doğal sabah güneş ışığı retinadaki özel gangliyon hücrelerine çarptığında, metabolizmayı uyandıran ani bir kortizol zirvesini tetiklerken akşam uykululuğuna giden iç geri sayımı sıfırlar. Tersine, gün batımından sonra akıllı telefonlar, dizüstü bilgisayar ekranları ve parlak tepe LED lambaları tarafından yayılan yüksek yoğunluklu mavi ışığa maruz kalmak beyni gün ortasını simüle etmesi için yanıltır. Bu durum melatoninin zamanında salgılanmasını engelleyerek uykuya dalmayı geciktirir ve REM döngülerini böler."
            },
            {
                "paragraph_index": 3,
                "title": "Optimizing the Physical Sleep Sanctuary",
                "content_en": "Transforming nightly rest requires deliberate environmental calibration rather than pharmaceutical sleep aids. Sleep researchers advise keeping bedroom temperatures cool—ideally between sixteen and nineteen degrees Celsius—as the human body must shed core thermal heat to initiate and sustain deep slow-wave slumber. Installing blackout curtains to eliminate nocturnal street glare and removing digital notifications from the bedside table establish a tranquil psychological boundary. Establishing a consistent wind-down ritual, such as light fiction reading or warm herbal tea, signals to the autonomic nervous system that the day's analytical demands have drawn to a peaceful close. Respecting this evening transition prepares both brain and body for a restorative night of deep healing.",
                "content_tr": "Gece dinlenmesini dönüştürmek, ilaç bazlı uyku yardımcılarından ziyade bilinçli bir çevresel düzenleme gerektirir. Uyku araştırmacıları, insan vücudunun derin yavaş dalga uykusunu başlatmak ve sürdürmek için çekirdek termal ısısını düşürmesi gerektiğinden, yatak odası sıcaklıklarının serin (ideal olarak 16 ila 19 derece) tutulmasını tavsiye eder. Gece sokak parıltısını ortadan kaldırmak için karartma perdeleri takmak ve komodinden dijital bildirimleri kaldırmak huzurlu bir psikolojik sınır oluşturur. Hafif kurgu okumak veya ılık bitki çayı içmek gibi tutarlı bir gevşeme ritüeli oluşturmak, otonom sinir sistemine günün analitik taleplerinin huzurlu bir şekilde sona erdiğini bildirir."
            },
            {
                "paragraph_index": 4,
                "title": "The Role of Nutrition and Caffeine Timing",
                "content_en": "In addition to light and ambient temperature, metabolic factors exert a strong influence over nocturnal sleep architecture. Consuming heavy, high-fat dinners close to bedtime forces the gastrointestinal tract to work actively during periods intended for cellular repair, causing frequent awakenings. Furthermore, because caffeine has an average metabolic half-life of six to eight hours, enjoying an espresso at five in the afternoon means significant stimulant molecules remain bound to adenosine receptors at midnight, preventing deep delta-wave sleep.",
                "content_tr": "Işık ve ortam sıcaklığına ek olarak metabolik faktörler de gece uyku mimarisi üzerinde güçlü bir etki yaratır. Yatmadan hemen önce ağır, yüksek yağlı akşam yemekleri tüketmek, gastrointestinal sistemi hücresel onarım için ayrılan dönemlerde aktif olarak çalışmaya zorlar ve sık sık uyanmalara neden olur. Ayrıca kafeinin ortalama altı ila sekiz saatlik bir metabolik yarılanma ömrü olduğundan, öğleden sonra beşte bir espresso içmek gece yarısı adenozin reseptörlerine bağlı önemli miktarda uyarıcı molekülün kalması anlamına gelir ve derin delta dalgası uykusunu engeller."
            },
            {
                "paragraph_index": 5,
                "title": "Sustainable Cognitive Performance",
                "content_en": "Ultimately, respecting human biology is the prerequisite for sustainable professional brilliance. When professionals prioritize seven to eight hours of consolidated sleep, their emotional resilience stabilizes, memory retention sharpens, and innovative insight flourishes naturally. Far from an unproductive waste of time, restorative sleep is the greatest cognitive enhancer available to modern knowledge workers. Protecting your nightly slumber is the most profound investment you can make in your long-term health and professional vitality.",
                "content_tr": "Nihayetinde insan biyolojisine saygı duymak, sürdürülebilir mesleki mükemmelliğin ön koşuludur. Profesyoneller yedi ila sekiz saatlik kesintisiz uykuya öncelik verdiklerinde, duygusal dayanıklılıkları dengelenir, hafıza keskinleşir ve yenilikçi içgörü kendiliğinden gelişir. Verimsiz bir zaman kaybı olmaktan çok uzak olan onarıcı uyku, modern bilgi çalışanları için mevcut olan en büyük bilişsel geliştiricidir."
            }
        ],
        [
            {"word": "deprivation", "vocab_id": "vocab.deprivation", "context_definition_en": "The damaging lack of material or physiological benefits.", "context_meaning_tr": "yoksunluk"},
            {"word": "synchronized", "vocab_id": "vocab.synchronize", "context_definition_en": "Caused to occur or operate at the same time or rate.", "context_meaning_tr": "senkronize edilmiş, uyumlu"},
            {"word": "tranquil", "vocab_id": "vocab.tranquil", "context_definition_en": "Free from disturbance; calm and serene.", "context_meaning_tr": "huzurlu, sakin"}
        ],
        [
            {
                "question_en": "Which brain region is directly compromised by chronic sleep deprivation according to the text?",
                "question_tr_hint": "Metne göre kronik uyku yoksunluğundan doğrudan hangi beyin bölgesi zarar görür?",
                "correct_answer": "The prefrontal cortex, which governs executive function and emotional balance.",
                "distractors": [
                    "The olfactory bulb that regulates culinary taste buds.",
                    "The auditory cortex that decodes high-pitch dog whistles.",
                    "The motor nerves that control reflexive blinking in bright sun."
                ],
                "explanation_en": "Paragraph 1 states that sleep deprivation impairs the prefrontal cortex, responsible for executive decisions.",
                "explanation_tr": "İlk paragraf uyku yoksunluğunun yürütücü işlevlerden sorumlu prefrontal kortekse zarar verdiğini açıklar."
            },
            {
                "question_en": "Why is exposure to natural morning sunlight recommended?",
                "question_tr_hint": "Doğal sabah güneş ışığına maruz kalmak neden önerilmektedir?",
                "correct_answer": "It stimulates a waking cortisol rise and synchronizes the circadian countdown to evening sleep.",
                "distractors": [
                    "It permanently eliminates the human need for drinking water.",
                    "It charges digital fitness trackers through ultraviolet skin contact.",
                    "It cures all bacterial respiratory infections immediately."
                ],
                "explanation_en": "Paragraph 2 explains that morning sunlight triggers an awakening cortisol peak and resets circadian timing.",
                "explanation_tr": "İkinci paragraf sabah ışığının kortizolü tetikleyerek sirkadiyen zamanlamayı sıfırladığını belirtir."
            },
            {
                "question_en": "How does nighttime screen exposure disrupt sleep architecture?",
                "question_tr_hint": "Gece ekrana maruz kalmak uyku mimarisini nasıl bozar?",
                "correct_answer": "Blue light tricks the brain into simulating daylight, suppressing melatonin secretion.",
                "distractors": [
                    "It causes the smartphone battery to overheat and ignite pillows.",
                    "It erases all vocabulary learned earlier during the day.",
                    "It converts brain waves into audible electromagnetic interference."
                ],
                "explanation_en": "Paragraph 2 notes that blue light simulates midday, preventing timely melatonin release.",
                "explanation_tr": "İkinci paragraf mavi ışığın gün ortasını taklit ederek melatonin salgısını baskıladığını açıklar."
            },
            {
                "question_en": "Why should caffeine intake be restricted late in the afternoon?",
                "question_tr_hint": "Öğleden sonra geç saatlerde kafein alımı neden sınırlandırılmalıdır?",
                "correct_answer": "Because its six-to-eight-hour half-life means stimulant molecules remain active at midnight.",
                "distractors": [
                    "Because caffeine turns poisonous when exposed to darkness.",
                    "Because international health agencies fine coffee drinkers after four o'clock.",
                    "Because caffeine causes teeth to change color during the night."
                ],
                "explanation_en": "Paragraph 4 explains that caffeine has a six-to-eight-hour half-life that keeps it bound to receptors at night.",
                "explanation_tr": "4. paragraf kafeinin 6-8 saatlik yarılanma ömrü nedeniyle gece yarısı hala reseptörlerde aktif kaldığını açıklar."
            },
            {
                "question_en": "What is the author's primary recommendation for enhancing sleep quality?",
                "question_tr_hint": "Yazarın uyku kalitesini artırmak için temel tavsiyesi nedir?",
                "correct_answer": "Calibrating light exposure, bedroom temperature, and relaxing evening rituals.",
                "distractors": [
                    "Consuming high doses of prescription stimulants before resting.",
                    "Working on analytical financial spreadsheets until falling asleep at the desk.",
                    "Keeping bright ceiling lights illuminated throughout the entire night."
                ],
                "explanation_en": "The passage outlines natural circadian alignment, light management, and cooling as key strategies.",
                "explanation_tr": "Metin, doğal sirkadiyen uyum, ışık yönetimi ve serin ortamı ana stratejiler olarak sunar."
            }
        ]
    )
]
