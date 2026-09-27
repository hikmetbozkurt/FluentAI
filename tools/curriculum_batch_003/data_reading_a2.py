#!/usr/bin/env python3
"""
Reading Batch 003: A2 Articles (5 articles).
Lengths: 250-500 words each, 4 paragraphs, 5 questions each, verified vocabulary annotations.
"""

from typing import List, Dict, Any
from reading_builder import build_article

READING_A2: List[Dict[str, Any]] = [
    # Article 1: Morning Habits and Energy (daily-life)
    build_article(
        article_id="reading.a2.morning-habits-and-energy",
        title="Simple Morning Habits for Sustained Daily Energy",
        cefr="A2",
        category="workplace_communication",
        summary_en="An explanatory article describing practical morning habits that help working professionals maintain energy throughout the day.",
        summary_tr="Çalışan profesyonellerin gün boyunca enerjilerini korumalarına yardımcı olan pratik sabah alışkanlıklarını anlatan açıklayıcı bir makale.",
        topic_tags=["daily-life"],
        paragraphs=[
            {
                "paragraph_index": 1,
                "title": "Starting the Morning Well",
                "content_en": "Many working adults feel tired when their morning alarm rings early. However, starting the day with calm habits can transform your personal productivity and mental focus. Instead of immediately checking phone notifications in bed, drinking a tall glass of fresh water helps wake up your digestive system and body. Opening the curtains allows natural sunlight to enter your bedroom, which signals to your brain that it is time to be active and positive.",
                "content_tr": "Birçok çalışan yetişkin, sabah alarmları erken çaldığında kendini yorgun hisseder. Ancak güne sakin alışkanlıklarla başlamak kişisel verimliliğinizi ve zihinsel odağınızı dönüştürebilir. Hemen yatakta telefon bildirimlerine bakmak yerine, uzun bir bardak taze su içmek sindirim sisteminizin ve vücudunuzun uyanmasına yardımcı olur. Perdeleri açmak, yatak odanıza doğal güneş ışığının girmesini sağlar ve bu da beyninize aktif ve pozitif olma vaktinin geldiği sinyalini verir."
            },
            {
                "paragraph_index": 2,
                "title": "A Nutritious Morning Meal",
                "content_en": "Eating a balanced breakfast provides continuous physical stamina during long working hours. Nutrition experts suggest combining whole grain bread with boiled eggs, seasonal fruit, and a cup of warm tea. Skipping breakfast often leads to a sudden headache or low concentration before noon arrives. A healthy morning meal stabilizes blood sugar and reduces the craving for sweet snacks later in the busy office environment.",
                "content_tr": "Dengeli bir kahvaltı yapmak, uzun çalışma saatleri boyunca sürekli fiziksel dayanıklılık sağlar. Beslenme uzmanları, tam tahıllı ekmeği haşlanmış yumurta, mevsim meyveleri ve bir fincan sıcak çay ile birleştirmeyi önermektedir. Kahvaltıyı atlamak genellikle öğle vakti gelmeden önce ani bir baş ağrısına veya düşük konsantrasyona yol açar. Sağlıklı bir sabah öğünü kan şekerini dengeler ve yoğun ofis ortamında daha sonra tatlı atıştırmalıklar yeme isteğini azaltır."
            },
            {
                "paragraph_index": 3,
                "title": "Gentle Physical Movement",
                "content_en": "Light exercise before leaving home prepares your muscles and joints for the demanding workday. Ten minutes of gentle stretching or a brisk walk to the nearby bus stop improves cardiovascular blood circulation. When people move their bodies in the morning, they feel noticeably more alert and less stressed. Regular morning movement also helps you sleep more peacefully and soundly when nighttime arrives.",
                "content_tr": "Evden çıkmadan önce yapılan hafif egzersiz, kaslarınızı ve eklemlerinizi zorlu iş gününe hazırlar. On dakikalık hafif esneme hareketleri veya yakındaki otobüs durağına tempolu bir yürüyüş kardiyovasküler kan dolaşımını iyileştirir. İnsanlar sabahları vücutlarını hareket ettirdiklerinde kendilerini belirgin şekilde daha dinç ve daha az stresli hissederler. Düzenli sabah hareketi ayrıca gece geldiğinde daha huzurlu ve deliksiz uyumanıza yardımcı olur."
            },
            {
                "paragraph_index": 4,
                "title": "Planning the Day Calmly",
                "content_en": "Finally, spending five quiet minutes reviewing your calendar creates clear mental clarity. Writing down three primary goals on a notepad ensures that you focus your energy on important deliverables first. By organizing your thoughts before opening your workplace inbox, you keep control over your daily schedule, reduce workplace anxiety, and approach colleagues with polite confidence.",
                "content_tr": "Son olarak, takviminizi incelemek için beş sessiz dakika harcamak net bir zihinsel berraklık yaratır. Bir not defterine üç temel hedef yazmak, enerjinizi öncelikle önemli teslimatlara odaklamanızı sağlar. İş yeri gelen kutunuzu açmadan önce düşüncelerinizi düzenleyerek, günlük programınızın kontrolünü elinizde tutar, iş yeri kaygısını azaltır ve iş arkadaşlarınıza kibar bir güvenle yaklaşırsınız."
            }
        ],
        annotations=[
            {
                "word": "alarm",
                "vocab_id": "vocab.alarm",
                "context_definition_en": "clock sound waking someone up",
                "context_meaning_tr": "çalar saat sesi"
            },
            {
                "word": "headache",
                "vocab_id": "vocab.headache",
                "context_definition_en": "a continuous pain in the head",
                "context_meaning_tr": "baş ağrısı"
            },
            {
                "word": "polite",
                "vocab_id": "vocab.polite",
                "context_definition_en": "socially correct and considerate",
                "context_meaning_tr": "kibar, saygılı"
            }
        ],
        raw_questions=[
            {
                "question_en": "What is recommended as the first action upon waking up?",
                "correct_answer": "Drinking a glass of fresh water and letting sunlight in",
                "distractors": [
                    "Checking social media notifications immediately",
                    "Taking strong headache medicine right away",
                    "Calling your manager to discuss office tasks"
                ],
                "explanation_en": "Paragraph 1 explicitly suggests drinking fresh water and opening curtains instead of checking phone notifications.",
                "explanation_tr": "1. paragraf telefon bildirimlerine bakmak yerine su içmeyi ve perdeleri açmayı önerir."
            },
            {
                "question_en": "According to paragraph 2, what problem can skipping breakfast cause?",
                "correct_answer": "A sudden headache and poor concentration before noon",
                "distractors": [
                    "Losing all access to your workplace calendar",
                    "Arriving late for every morning bus journey",
                    "Permanently damaging leg muscles during exercise"
                ],
                "explanation_en": "Paragraph 2 notes that skipping breakfast often leads to a sudden headache or low concentration.",
                "explanation_tr": "2. paragraf kahvaltıyı atlamanın baş ağrısına ve konsantrasyon kaybına yol açabileceğini belirtir."
            },
            {
                "question_en": "How does morning physical movement affect people?",
                "correct_answer": "It makes them feel more alert and less stressed",
                "distractors": [
                    "It causes severe muscle soreness throughout the week",
                    "It forces employees to sleep during afternoon meetings",
                    "It eliminates the need to drink water or eat food"
                ],
                "explanation_en": "Paragraph 3 explains that moving in the morning helps people feel more alert and less stressed.",
                "explanation_tr": "3. paragraf sabah hareketinin insanları daha dinç ve daha az stresli hissettirdiğini açıklar."
            },
            {
                "question_en": "What should professionals write down on a notepad before opening email?",
                "correct_answer": "Three primary goals for the day",
                "distractors": [
                    "A list of complaints about their colleagues",
                    "Detailed recipes for weekend dinners",
                    "Every train schedule in the capital city"
                ],
                "explanation_en": "Paragraph 4 advises writing down three primary goals to ensure focus on important deliverables.",
                "explanation_tr": "4. paragraf gün için üç temel hedef yazmayı önerir."
            },
            {
                "question_en": "What is the overall purpose of this article?",
                "correct_answer": "To explain practical morning habits that improve daytime energy",
                "distractors": [
                    "To criticize employees who commute on city buses",
                    "To advertise an expensive electronic alarm clock",
                    "To convince readers to avoid eating any breakfast"
                ],
                "explanation_en": "The article provides an informative overview of healthy morning habits to sustain energy.",
                "explanation_tr": "Makale gün boyu enerjiyi sürdüren sağlıklı sabah alışkanlıklarını açıklar."
            }
        ]
    ),

    # Article 2: Neighborhood Farmers Market (food-shopping)
    build_article(
        article_id="reading.a2.neighborhood-farmer-market",
        title="Shopping for Fresh Food at the Weekend Market",
        cefr="A2",
        category="workplace_communication",
        summary_en="A practical guide to visiting community markets for fresh seasonal ingredients, supporting local farmers and budgeting wisely.",
        summary_tr="Mevsimlik taze malzemeler bulmak, yerel çiftçileri desteklemek ve akıllıca bütçe yapmak için semt pazarlarını ziyaret etme rehberi.",
        topic_tags=["food-shopping"],
        paragraphs=[
            {
                "paragraph_index": 1,
                "title": "The Atmosphere of the Market",
                "content_en": "Every Saturday morning, local residents visit the neighborhood open-air market to buy groceries and household essentials. Unlike large modern supermarkets with artificial fluorescent lights, the market square is filled with colorful wooden stalls and friendly welcoming voices. Farmers from surrounding agricultural villages bring wooden crates of crisp fresh apples, green vegetables, and jars of organic honey. Shopping in this open space allows urban families to connect directly with the hardworking people who grow their food.",
                "content_tr": "Her cumartesi sabahı mahalle sakinleri bakkaliye ve temel ev ihtiyaçlarını satın almak için açık hava semt pazarını ziyaret eder. Yapay floresan ışıkları olan büyük modern süpermarketlerin aksine, pazar meydanı renkli ahşap tezgahlar ve samimi, sıcak seslerle doludur. Çevre tarım köylerinden gelen çiftçiler tahta kasalar dolusu gevrek taze elma, yeşil sebze ve organik bal kavanozları getirir. Bu açık alanda alışveriş yapmak, şehirli ailelerin yiyeceklerini yetiştiren çalışkan insanlarla doğrudan bağlantı kurmasını sağlar."
            },
            {
                "paragraph_index": 2,
                "title": "Choosing Quality Produce",
                "content_en": "To select the best ingredients, market visitors learn to observe natural colors and scents carefully. Fresh ripe tomatoes have firm skins and an earthy garden fragrance, while crisp lettuce leaves show vibrant green color without brown wilted edges. Vendors are always happy to answer detailed questions about when vegetables were harvested from the fields. Customers often taste small sweet slices of fresh melon before deciding to order a full wooden basket for their family.",
                "content_tr": "En iyi malzemeleri seçmek için pazar ziyaretçileri doğal renkleri ve kokuları dikkatlice gözlemlemeyi öğrenir. Taze olgun domatesler sert kabuklara ve topraksı bir bahçe kokusuna sahipken, gevrek marul yaprakları kahverengi solmuş kenarlar olmaksızın canlı yeşil renk gösterir. Satıcılar, sebzelerin tarlalardan ne zaman hasat edildiğine ilişkin ayrıntılı soruları yanıtlamaktan her zaman mutluluk duyar. Müşteriler genellikle aileleri için dolu bir tahta sepet sipariş etmeye karar vermeden önce taze kavunun küçük tatlı dilimlerini tadarlar."
            },
            {
                "paragraph_index": 3,
                "title": "Smart Budgeting and Payment",
                "content_en": "Visiting the local market helps households save considerable money on their weekly food expenses. Seasonal farm produce is often cheap compared to packaged goods in commercial shopping malls. Many small independent stalls now accept modern contactless payment, allowing customers to pay by card conveniently without carrying large heavy bags of coins. However, bringing some paper cash is still helpful when buying small bundles of fresh herbs from elderly village sellers.",
                "content_tr": "Yerel pazarı ziyaret etmek, hanelerin haftalık gıda harcamalarında önemli ölçüde tasarruf etmelerine yardımcı olur. Mevsimlik çiftlik ürünleri, ticari alışveriş merkezlerindeki paketlenmiş ürünlere kıyasla genellikle ucuzdur. Artık birçok küçük bağımsız tezgah modern temassız ödemeyi kabul ederek müşterilerin büyük ağır madeni para çantaları taşımadan kartla rahatça ödeme yapmasına olanak tanır. Ancak yaşlı köylü satıcılardan küçük taze ot demetleri alırken bir miktar kağıt nakit getirmek hala faydalıdır."
            },
            {
                "paragraph_index": 4,
                "title": "Environmental Benefits",
                "content_en": "Finally, buying local food reduces plastic packaging waste and environmental pollution across regional cities. Supermarket goods travel hundreds of kilometers in refrigerated diesel trucks, generating significant carbon exhaust emissions. At the neighborhood market, conscious shoppers bring reusable cotton tote bags and glass storage containers. Supporting community agriculture keeps nearby farmland productive and ensures that city dwellers enjoy nutritious meals every week.",
                "content_tr": "Son olarak, yerel gıda satın almak bölgesel şehirlerde plastik ambalaj atıklarını ve çevre kirliliğini azaltır. Süpermarket ürünleri, soğutmalı dizel kamyonlarda yüzlerce kilometre yol kat ederek önemli karbon egzoz emisyonları üretir. Semt pazarında bilinçli alışveriş yapanlar yeniden kullanılabilir pamuklu bez çantalar ve cam saklama kapları getirir. Topluluk tarımını desteklemek yakındaki tarım arazilerini verimli tutar ve kent sakinlerinin her hafta besleyici yemeklerin tadını çıkarmasını sağlar."
            }
        ],
        annotations=[
            {
                "word": "fresh",
                "vocab_id": "vocab.fresh",
                "context_definition_en": "recently produced or picked food",
                "context_meaning_tr": "taze"
            },
            {
                "word": "cheap",
                "vocab_id": "vocab.cheap",
                "context_definition_en": "low in price",
                "context_meaning_tr": "ucuz"
            },
            {
                "word": "pay by card",
                "vocab_id": "vocab.pay-by-card",
                "context_definition_en": "pay using a payment card",
                "context_meaning_tr": "kartla ödemek"
            }
        ],
        raw_questions=[
            {
                "question_en": "What do farmers bring to the neighborhood Saturday market?",
                "correct_answer": "Crates of fresh fruit, green vegetables, and organic honey",
                "distractors": [
                    "Factory-made industrial plastics and electronics",
                    "Diesel fuel engines for refrigerated transport trucks",
                    "Expensive imported furniture from foreign capitals"
                ],
                "explanation_en": "Paragraph 1 mentions farmers bringing crates of fresh apples, green vegetables, and organic honey.",
                "explanation_tr": "1. paragraf çiftçilerin taze elma, yeşil sebze ve organik bal getirdiğini belirtir."
            },
            {
                "question_en": "How can customers verify the quality of fresh tomatoes at market stalls?",
                "correct_answer": "By checking for firm skins and an earthy fragrance",
                "distractors": [
                    "By scanning barcodes with special smartphone sensors",
                    "By asking bank managers for a credit verification code",
                    "By ensuring the skin is completely brown and dry"
                ],
                "explanation_en": "Paragraph 2 states that fresh tomatoes have firm skins and an earthy fragrance.",
                "explanation_tr": "2. paragraf taze domateslerin sert kabuklu ve topraksı kokulu olduğunu belirtir."
            },
            {
                "question_en": "Why do shoppers save money at the open-air market?",
                "correct_answer": "Because seasonal produce is often cheap compared to supermarket goods",
                "distractors": [
                    "Because market sellers give all vegetables away for free",
                    "Because banks pay people to buy seasonal tomatoes",
                    "Because vendors only accept gold coins as payment"
                ],
                "explanation_en": "Paragraph 3 notes that seasonal produce is often cheap compared to packaged goods in malls.",
                "explanation_tr": "3. paragraf mevsimlik ürünlerin süpermarkete göre genellikle ucuz olduğunu açıklar."
            },
            {
                "question_en": "How does buying food locally help protect the environment?",
                "correct_answer": "It reduces long-distance truck transport and plastic packaging waste",
                "distractors": [
                    "It forces all city residents to stop eating fresh vegetables",
                    "It replaces farmland with large diesel fuel refineries",
                    "It increases refrigerated freight shipping across oceans"
                ],
                "explanation_en": "Paragraph 4 explains that local shopping cuts down truck transport emissions and plastic packaging waste.",
                "explanation_tr": "4. paragraf yerel alışverişin kamyon taşımacılığı emisyonlarını ve plastik atıkları azalttığını anlatır."
            },
            {
                "question_en": "Which item do shoppers commonly bring to avoid single-use plastic bags?",
                "correct_answer": "Reusable cotton tote bags and glass containers",
                "distractors": [
                    "Heavy wooden shipping crates from cargo ships",
                    "Plastic disposable cutlery from office cafeterias",
                    "Gasoline containers for long vehicle journeys"
                ],
                "explanation_en": "Paragraph 4 mentions shoppers bringing reusable cotton tote bags and glass containers.",
                "explanation_tr": "4. paragraf alışveriş yapanların bez çanta ve cam kaplar getirdiğini söyler."
            }
        ]
    ),

    # Article 3: Weekend City Break Guide (travel)
    build_article(
        article_id="reading.a2.weekend-city-break-guide",
        title="Planning an Affordable Weekend City Break",
        cefr="A2",
        category="workplace_communication",
        summary_en="An instructional travel guide offering simple strategies to plan relaxing and budget-friendly weekend trips by train.",
        summary_tr="Trenle dinlendirici ve bütçe dostu hafta sonu seyahatleri planlamak için basit stratejiler sunan bir seyahat rehberi.",
        topic_tags=["travel"],
        paragraphs=[
            {
                "paragraph_index": 1,
                "title": "The Joy of Short Trips",
                "content_en": "Taking a short city break over the weekend provides welcome emotional relief from workplace stress and screen fatigue. You do not need to take two weeks of formal annual leave to discover historic architecture and taste regional culinary cuisine. Booking an affordable train ticket to a quiet riverside town two hours away offers a refreshing change of scenery for tired professionals. Traveling by modern rail is comfortable, highly scenic, and avoids long stressful airport security queues.",
                "content_tr": "Hafta sonu kısa bir şehir kaçamağı yapmak, iş yeri stresinden ve ekran yorgunluğundan hoş bir duygusal rahatlama sağlar. Tarihi mimariyi keşfetmek ve yöresel mutfak lezzetlerini tatmak için iki haftalık resmi yıllık izin almanıza gerek yoktur. İki saat uzaklıktaki nehir kenarındaki sakin bir kasabaya uygun fiyatlı bir tren bileti ayırtmak, yorgun profesyoneller için ferahlatıcı bir manzara değişikliği sunar. Modern demiryolu ile seyahat etmek konforludur, son derece manzaralıdır ve uzun stresli havaalanı güvenlik kuyruklarından kaçınır."
            },
            {
                "paragraph_index": 2,
                "title": "Arriving at the Station",
                "content_en": "When your journey begins on Friday evening, arrive at the railway platform twenty minutes before scheduled departure time. High-speed intercity trains depart punctually, so checking digital departure boards guarantees that you board the correct carriage without rushing. Keeping a compact backpack with your electronic charger, warm jacket, and booking confirmation ticket makes boarding smooth. Passenger trains offer electrical charging outlets and quiet wagons where you can read peacefully.",
                "content_tr": "Yolculuğunuz cuma akşamı başladığında, planlanan kalkış saatinden yirmi dakika önce tren peronuna varın. Şehirlerarası hızlı trenler vaktinde kalkar, bu nedenle dijital kalkış panolarını kontrol etmek acele etmeden doğru vagona binmenizi garanti eder. Elektronik şarj cihazınızı, sıcak tutan ceketinizi ve rezervasyon onay biletinizi içeren kompakt bir sırt çantası taşımak binişi sorunsuz hale getirir. Yolcu trenleri, huzur içinde okuyabileceğiniz elektrikli şarj prizleri ve sessiz vagonlar sunar."
            },
            {
                "paragraph_index": 3,
                "title": "Exploring on Foot",
                "content_en": "Once you arrive at your destination, walking on foot is the most authentic way to explore ancient cobblestone streets. Many historic provincial towns feature large pedestrian squares where motor vehicles are strictly prohibited. You can admire ancient stone clock towers, browse cozy independent bookshops, and greet friendly local cafe owners. Walking leisurely between major sights saves expensive taxi fares and lets you uncover charming historic alleys that guidebooks often overlook.",
                "content_tr": "Varış noktanıza ulaştığınızda, yürümek antik arnavut kaldırımlı sokakları keşfetmenin en otantik yoludur. Birçok tarihi taşra kasabasında motorlu araçların kesinlikle yasak olduğu büyük yaya meydanları bulunur. Eski taş saat kulelerine hayran kalabilir, samimi bağımsız kitapçıları gezebilir ve dost canlısı yerel kafe sahiplerini selamlayabilirsiniz. Önemli yerler arasında yavaşça yürümek pahalı taksi ücretlerinden tasarruf sağlar ve rehber kitapların genellikle gözden kaçırdığı büyüleyici tarihi ara sokakları keşfetmenize olanak tanır."
            },
            {
                "paragraph_index": 4,
                "title": "Dining and Memories",
                "content_en": "Sampling regional food at neighborhood bistros creates lasting travel memories with friends and family. Ask local residents where they personally prefer to dine rather than choosing expensive restaurants directly beside crowded tourist monuments. Traditional daily set menus offer hearty warm soups and freshly baked bread at very reasonable prices. After a restful weekend exploring new streets, you return home on Sunday evening feeling refreshed, calm, and motivated for the coming week.",
                "content_tr": "Semt bistrolarında yöresel yemekleri tatmak, arkadaşlar ve aile ile kalıcı seyahat anıları yaratır. Kalabalık turistik anıtların hemen yanındaki pahalı restoranları seçmek yerine, yerel halka kişisel olarak nerede yemek yemeyi tercih ettiklerini sorun. Geleneksel günlük menüler, çok makul fiyatlarla doyurucu sıcak çorbalar ve taze pişmiş ekmek sunar. Yeni sokakları keşfettiğiniz dinlendirici bir hafta sonunun ardından, pazar akşamı kendinizi yenilenmiş, sakin ve gelecek hafta için motive olmuş hissederek eve dönersiniz."
            }
        ],
        annotations=[
            {
                "word": "ticket",
                "vocab_id": "vocab.ticket",
                "context_definition_en": "pass for travel or entry",
                "context_meaning_tr": "bilet"
            },
            {
                "word": "platform",
                "vocab_id": "vocab.platform",
                "context_definition_en": "raised area at train station",
                "context_meaning_tr": "peron"
            },
            {
                "word": "charger",
                "vocab_id": "vocab.charger",
                "context_definition_en": "device putting electricity into a battery",
                "context_meaning_tr": "şarj cihazı"
            }
        ],
        raw_questions=[
            {
                "question_en": "Why does the author recommend traveling by train for short weekend trips?",
                "correct_answer": "It is comfortable, scenic, and avoids long airport security lines",
                "distractors": [
                    "It requires tourists to purchase private luxury locomotives",
                    "It takes two full weeks of continuous travel to reach nearby towns",
                    "It completely prohibits passengers from carrying jackets or bags"
                ],
                "explanation_en": "Paragraph 1 explains that rail travel is comfortable, scenic, and avoids long airport security queues.",
                "explanation_tr": "1. paragraf tren yolculuğunun konforlu, manzaralı olduğunu ve uzun havaalanı kuyruklarını önlediğini belirtir."
            },
            {
                "question_en": "What advice is given regarding arrival at the railway station?",
                "correct_answer": "Arrive at the platform twenty minutes before scheduled departure",
                "distractors": [
                    "Run onto the tracks after the train has already started moving",
                    "Wait outside the city center until Sunday morning arrives",
                    "Leave all luggage and electronic chargers at your office desk"
                ],
                "explanation_en": "Paragraph 2 advises arriving at the platform twenty minutes before departure.",
                "explanation_tr": "2. paragraf kalkıştan yirmi dakika önce perona varılmasını tavsiye eder."
            },
            {
                "question_en": "Why is walking considered the best way to explore a new destination?",
                "correct_answer": "It saves money on taxis and reveals charming alleys missed by guidebooks",
                "distractors": [
                    "It allows travelers to drive heavy diesel trucks into museums",
                    "It prevents visitors from speaking to any local cafe owners",
                    "It guarantees that travelers will miss their return train ticket"
                ],
                "explanation_en": "Paragraph 3 notes walking saves taxi fares and uncovers charming alleys guidebooks overlook.",
                "explanation_tr": "3. paragraf yürümenin taksi masrafını önlediğini ve gizli sokakları keşfettirdiğini söyler."
            },
            {
                "question_en": "How should travelers find good regional food, according to paragraph 4?",
                "correct_answer": "By asking local residents where they personally prefer to dine",
                "distractors": [
                    "By dining exclusively at expensive airport departure gates",
                    "By selecting restaurants right beside crowded tourist monuments",
                    "By ordering fast food from multinational chain franchises"
                ],
                "explanation_en": "Paragraph 4 recommends asking local residents where they prefer to dine.",
                "explanation_tr": "4. paragraf yerel halka nerede yemek yemeyi tercih ettiklerini sormayı önerir."
            },
            {
                "question_en": "How do travelers feel when returning on Sunday evening after a city break?",
                "correct_answer": "Refreshed and motivated for the upcoming workweek",
                "distractors": [
                    "Exhausted and unable to remember their office address",
                    "Angry about having visited historic stone buildings",
                    "Desperate to cancel all future weekend travel plans"
                ],
                "explanation_en": "Paragraph 4 concludes that travelers return feeling refreshed and motivated for the coming week.",
                "explanation_tr": "4. paragraf gezginlerin yenilenmiş ve motive olmuş hissettiklerini belirtir."
            }
        ]
    ),

    # Article 4: Staying Connected with Friends (relationships)
    build_article(
        article_id="reading.a2.staying-connected-with-friends",
        title="Maintaining Meaningful Friendships in a Busy World",
        cefr="A2",
        category="workplace_communication",
        summary_en="An insightful article on nurturing close personal relationships despite demanding professional work schedules.",
        summary_tr="Yoğun profesyonel iş programlarına rağmen yakın kişisel ilişkileri besleme ve sürdürme üzerine anlamlı bir makale.",
        topic_tags=["relationships"],
        paragraphs=[
            {
                "paragraph_index": 1,
                "title": "The Challenge of Busy Schedules",
                "content_en": "In modern corporate life, maintaining close friendships requires deliberate and intentional effort. Between strict project deadlines, family commitments, and daily household routines, weeks can easily pass without speaking to close companions. People often feel guilty when they postpone dinner plans or forget to call back an old classmate after a tiring day. However, strong lasting friendships do not require daily contact; they rely on sincere emotional care and reliable communication.",
                "content_tr": "Modern kurumsal hayatta yakın dostlukları sürdürmek bilinçli ve kararlı bir çaba gerektirir. Katı proje teslim tarihleri, aile sorumlulukları ve günlük ev rutinleri arasında, yakın dostlarla konuşmadan haftalar kolayca geçebilir. İnsanlar yorucu bir günün ardından akşam yemeği planlarını ertelediklerinde veya eski bir sınıf arkadaşını geri aramayı unuttuklarında sıklıkla suçluluk hissederler. Ancak güçlü ve kalıcı arkadaşlıklar günlük temas gerektirmez; samimi duygusal ilgiye ve güvenilir iletişime dayanır."
            },
            {
                "paragraph_index": 2,
                "title": "Small Gestures that Matter",
                "content_en": "Simple daily communication habits can keep friendships vibrant and strong across busy months. Sending a short text message to ask how a friend's important presentation went demonstrates genuine interest and warmth. Sharing an amusing photo or remembering a birthday shows that you keep them actively in mind. When friends understand that both people have busy professional careers, they appreciate thoughtful messages without expecting an immediate formal reply.",
                "content_tr": "Basit günlük iletişim alışkanlıkları, yoğun aylar boyunca arkadaşlıkları canlı ve güçlü tutabilir. Bir arkadaşınızın önemli sunumunun nasıl geçtiğini sormak için kısa bir kısa mesaj göndermek samimi bir ilgi ve sıcaklık gösterir. Eğlenceli bir fotoğraf paylaşmak veya bir doğum gününü hatırlamak, onları aktif olarak aklınızda tuttuğunuzu gösterir. Arkadaşlar her iki kişinin de yoğun profesyonel kariyerleri olduğunu anladığında, hemen resmi bir yanıt beklemeden düşünceli mesajların değerini bilirler."
            },
            {
                "paragraph_index": 3,
                "title": "Quality Time Together",
                "content_en": "When personal schedules finally align, scheduling dedicated face-to-face time significantly strengthens emotional bonds. Meeting for a casual weekend breakfast or going on an outdoor walk in the quiet park allows for uninterrupted conversation. Putting personal smartphones away during these catch-up sessions ensures active and supportive listening. Listening with polite attention to a companion's joys and worries deepens mutual trust and understanding.",
                "content_tr": "Kişisel programlar nihayet uyum sağladığında, yüz yüze özel vakit planlamak duygusal bağları önemli ölçüde güçlendirir. Hafta sonu samimi bir kahvaltı için buluşmak veya sakin parkta açık hava yürüyüşüne çıkmak, kesintisiz sohbet imkanı sağlar. Bu buluşmalar sırasında kişisel akıllı telefonları bir kenara koymak aktif ve destekleyici dinlemeyi sağlar. Bir dostun sevinçlerini ve endişelerini kibar bir dikkatle dinlemek karşılıklı güveni ve anlayışı derinleştirir."
            },
            {
                "paragraph_index": 4,
                "title": "Mutual Support in Difficult Times",
                "content_en": "True loyal companions stand reliably by each other during life transitions and difficult emotional challenges. Whether someone is navigating a challenging job interview, recovering from an injury, or moving to a new apartment, practical help is invaluable. Helping a friend carry furniture or offering a comforting word creates lasting personal loyalty. In the end, lasting relationships enrich our emotional wellbeing and make daily life deeply rewarding.",
                "content_tr": "Gerçek ve sadık dostlar, hayat geçişlerinde ve zorlu duygusal engellerde güvenilir bir şekilde birbirlerinin yanında dururlar. Biri ister zorlu bir iş mülakatına hazırlanıyor olsun, ister bir sakatlıktan kurtuluyor veya yeni bir daireye taşınıyor olsun, pratik yardım paha biçilemezdir. Bir arkadaşın mobilya taşımasına yardım etmek veya teselli edici bir söz söylemek kalıcı bir kişisel sadakat yaratır. Sonuçta, kalıcı ilişkiler duygusal sağlığımızı zenginleştirir ve günlük hayatı derinden ödüllendirici kılar."
            }
        ],
        annotations=[
            {
                "word": "call back",
                "vocab_id": "vocab.call-back",
                "context_definition_en": "phone someone again in return",
                "context_meaning_tr": "geri aramak"
            },
            {
                "word": "polite",
                "vocab_id": "vocab.polite",
                "context_definition_en": "socially correct and considerate",
                "context_meaning_tr": "kibar, saygılı"
            },
            {
                "word": "routine",
                "vocab_id": "vocab.routine",
                "context_definition_en": "usual series of activities done regularly",
                "context_meaning_tr": "rutin"
            }
        ],
        raw_questions=[
            {
                "question_en": "Why can weeks pass without speaking to close friends in corporate life?",
                "correct_answer": "Because of demanding deadlines, family commitments, and household chores",
                "distractors": [
                    "Because modern employers legally prohibit personal friendships",
                    "Because smartphones cannot transmit text messages across cities",
                    "Because people permanently lose the ability to speak English"
                ],
                "explanation_en": "Paragraph 1 highlights deadlines, family commitments, and household chores as reasons.",
                "explanation_tr": "1. paragraf iş teslim tarihleri, aile ve ev işlerinin buna sebep olduğunu belirtir."
            },
            {
                "question_en": "What simple action shows genuine care according to paragraph 2?",
                "correct_answer": "Sending a short text to ask about a presentation or remembering a birthday",
                "distractors": [
                    "Buying an expensive luxury sports car for every acquaintance",
                    "Calling someone fifty times continuously during their workday",
                    "Deleting all contact numbers from your electronic mobile phone"
                ],
                "explanation_en": "Paragraph 2 notes that sending a brief text or remembering a birthday demonstrates care.",
                "explanation_tr": "2. paragraf kısa bir mesaj atmanın veya doğum gününü hatırlamanın ilgi gösterdiğini söyler."
            },
            {
                "question_en": "What practice is encouraged during face-to-face meetups with friends?",
                "correct_answer": "Putting smartphones away to enable active, uninterrupted listening",
                "distractors": [
                    "Checking work email spreadsheets every two minutes",
                    "Wearing dark sunglasses and refusing to speak aloud",
                    "Leaving the cafe immediately after greeting the waiter"
                ],
                "explanation_en": "Paragraph 3 encourages putting smartphones away to ensure active listening.",
                "explanation_tr": "3. paragraf aktif dinleme için telefonların kaldırılmasını önerir."
            },
            {
                "question_en": "How do loyal companions support each other during challenges?",
                "correct_answer": "By offering comforting words and practical help like moving furniture",
                "distractors": [
                    "By charging hourly consulting fees for listening to problems",
                    "By ignoring friends whenever they face illness or career change",
                    "By reporting personal discussions to corporate HR departments"
                ],
                "explanation_en": "Paragraph 4 describes practical assistance and comforting words as hallmarks of true friendship.",
                "explanation_tr": "4. paragraf pratik yardım ve tesellinin gerçek dostluğun göstergesi olduğunu açıklar."
            },
            {
                "question_en": "What is the primary message of this article?",
                "correct_answer": "Meaningful friendships require thoughtful attention and mutual support, not constant daily contact",
                "distractors": [
                    "Adults should abandon all personal friendships to focus exclusively on career status",
                    "Only childhood friends who live in the same apartment building deserve attention",
                    "Modern technology makes it entirely impossible to maintain emotional connections"
                ],
                "explanation_en": "The article emphasizes that sincere gestures and quality time sustain meaningful friendships.",
                "explanation_tr": "Makale, samimi jestler ve kaliteli vaktin dostlukları sürdürdüğünü vurgular."
            }
        ]
    ),

    # Article 5: Daily Exercise Routine (health-lifestyle)
    build_article(
        article_id="reading.a2.daily-exercise-routine",
        title="Building a Manageable Daily Fitness Routine",
        cefr="A2",
        category="workplace_communication",
        summary_en="An accessible health guide outlining how busy white-collar workers can integrate regular physical activity into sedentary lifestyles.",
        summary_tr="Yoğun beyaz yakalı çalışanların hareketsiz yaşam tarzlarına düzenli fiziksel aktiviteyi nasıl entegre edebileceklerini anlatan erişilebilir bir sağlık rehberi.",
        topic_tags=["health-lifestyle"],
        paragraphs=[
            {
                "paragraph_index": 1,
                "title": "The Sedentary Desk Problem",
                "content_en": "Sitting in front of computer screens for eight hours every day takes a toll on human posture. Many office workers develop tight neck muscles, sore shoulders, and lower back stiffness before the workweek ends. When you remain seated in office chairs without moving, blood flow slows down and mental tiredness increases. Recognizing that our bodies require regular movement is the first step toward long-term physical wellbeing.",
                "content_tr": "Her gün sekiz saat boyunca bilgisayar ekranlarının karşısında oturmak insan duruşunu olumsuz etkiler. Birçok ofis çalışanı, çalışma haftası bitmeden önce gergin boyun kasları, ağrıyan omuzlar ve bel tutulması yaşar. Ofis koltuklarında hareket etmeden oturduğunuzda, kan akışı yavaşlar ve zihinsel yorgunluk artar. Bedenlerimizin düzenli harekete ihtiyaç duyduğunu fark etmek, uzun vadeli fiziksel sağlığa doğru ilk adımdır."
            },
            {
                "paragraph_index": 2,
                "title": "Starting with Manageable Goals",
                "content_en": "The biggest obstacle to fitness is attempting too much too quickly. Beginners often buy expensive gym memberships, exercise intensely for three days, and quit because their bodies feel exhausted and painful. A more sustainable strategy is setting small, realistic goals. Committing to a fifteen-minute brisk walk during lunch or doing simple bodyweight squats at home builds consistency without causing injury.",
                "content_tr": "Zindeliğin önündeki en büyük engel, çok hızlı bir şekilde çok fazlasını denemektir. Yeni başlayanlar genellikle pahalı spor salonu üyelikleri satın alır, üç gün yoğun egzersiz yapar ve vücutları bitkin ve ağrılı hissettiği için bırakırlar. Daha sürdürülebilir bir strateji, küçük ve gerçekçi hedefler belirlemektir. Öğle yemeğinde on beş dakikalık tempolu bir yürüyüş yapmayı taahhüt etmek veya evde basit çömelme hareketleri yapmak, sakatlığa yol açmadan süreklilik oluşturur."
            },
            {
                "paragraph_index": 3,
                "title": "Integrating Movement into Work",
                "content_en": "You can weave physical activity into your office routine without disrupting professional responsibilities. Taking a short break to walk down the hallway or using the stairs instead of the elevator strengthens leg muscles and burns calories naturally. Standing up every forty-five minutes to stretch your arms and roll your shoulders prevents chronic muscle tension. Some professionals hold walking meetings with colleagues when discussing project updates, combining problem-solving with fresh air.",
                "content_tr": "Profesyonel sorumlulukları aksatmadan fiziksel aktiviteyi ofis rutininize katabilirsiniz. Asansör yerine merdivenleri kullanmak bacak kaslarını güçlendirir ve doğal olarak kalori yakar. Kollarınızı esnetmek ve omuzlarınızı döndürmek için her kırk beş dakikada bir ayağa kalkmak kronik kas gerginliğini önler. Bazı profesyoneller, proje güncellemelerini tartışırken iş arkadaşlarıyla yürüyüş toplantıları düzenleyerek sorun çözmeyi temiz hava ile birleştirir."
            },
            {
                "paragraph_index": 4,
                "title": "Celebrating Small Improvements",
                "content_en": "Consistent physical activity produces visible benefits within a few weeks. People report higher daily energy levels, improved mood, and deeper sleep at night. You do not need to train like an Olympic athlete to enjoy these rewards; regular moderate movement is completely sufficient. By prioritizing your physical health today, you protect your body and enhance your professional productivity for the future.",
                "content_tr": "Düzenli fiziksel aktivite, birkaç hafta içinde gözle görülür faydalar sağlar. İnsanlar daha yüksek günlük enerji seviyeleri, gelişmiş bir ruh hali ve geceleri daha derin bir uyku bildirmektedir. Bu kazanımların tadını çıkarmak için olimpik bir atlet gibi antrenman yapmanıza gerek yoktur; düzenli orta düzeyde hareket tamamen yeterlidir. Bugün fiziksel sağlığınıza öncelik vererek vücudunuzu korur ve gelecek için profesyonel üretkenliğinizi artırırsınız."
            }
        ],
        annotations=[
            {
                "word": "tired",
                "vocab_id": "vocab.tired",
                "context_definition_en": "needing sleep or rest",
                "context_meaning_tr": "yorgun"
            },
            {
                "word": "break",
                "vocab_id": "vocab.break-n",
                "context_definition_en": "short period of rest from work",
                "context_meaning_tr": "mola"
            },
            {
                "word": "routine",
                "vocab_id": "vocab.routine",
                "context_definition_en": "usual series of activities done regularly",
                "context_meaning_tr": "rutin"
            }
        ],
        raw_questions=[
            {
                "question_en": "What physical issues often affect employees who sit at computers all day?",
                "correct_answer": "Tight neck muscles, sore shoulders, and lower back stiffness",
                "distractors": [
                    "Instant loss of vision within the first thirty minutes",
                    "A total inability to type letters on computer keyboards",
                    "Permanent muscle expansion in their hands and fingers"
                ],
                "explanation_en": "Paragraph 1 mentions neck tension, sore shoulders, and lower back stiffness from prolonged sitting.",
                "explanation_tr": "1. paragraf uzun süre oturmaktan boyun, omuz ve bel tutulmalarının oluştuğunu belirtir."
            },
            {
                "question_en": "Why do many fitness beginners quit exercising soon after starting?",
                "correct_answer": "They attempt overly intense routines too quickly and feel exhausted",
                "distractors": [
                    "They are legally forbidden from exercising more than three days",
                    "Their doctors tell them that walking outside causes heart failure",
                    "All gyms close down permanently after three days of training"
                ],
                "explanation_en": "Paragraph 2 explains that attempting too much too quickly leads to exhaustion and quitting.",
                "explanation_tr": "2. paragraf çok hızlı aşırı yüklenmenin bitkinlik ve pes etmeye yol açtığını belirtir."
            },
            {
                "question_en": "What is one simple way to add movement to the workday?",
                "correct_answer": "Using the stairs instead of the elevator and standing up every forty-five minutes",
                "distractors": [
                    "Running a complete marathon around the boardroom table",
                    "Refusing to sit down in any chair for twelve consecutive hours",
                    "Carrying heavy desks up to the office rooftop every morning"
                ],
                "explanation_en": "Paragraph 3 suggests taking the stairs and standing up every forty-five minutes.",
                "explanation_tr": "3. paragraf merdiven kullanmayı ve 45 dakikada bir ayağa kalkmayı önerir."
            },
            {
                "question_en": "What are 'walking meetings' mentioned in paragraph 3?",
                "correct_answer": "Conversations where coworkers discuss updates while walking outdoors",
                "distractors": [
                    "Speed-walking races where winners receive executive promotions",
                    "Silent formal exams conducted while marching in formation",
                    "Emergency evacuations triggered by building security alarms"
                ],
                "explanation_en": "Paragraph 3 describes colleagues discussing updates on a walk, combining problem-solving with fresh air.",
                "explanation_tr": "3. paragraf meslektaşların yürüyerek proje güncellemelerini tartıştığı toplantıları açıklar."
            },
            {
                "question_en": "What benefits do people typically notice within a few weeks of consistent movement?",
                "correct_answer": "Higher energy, improved mood, and deeper sleep",
                "distractors": [
                    "An immediate need to quit their corporate careers",
                    "Complete disappearance of all human emotional feelings",
                    "Severe weight gain caused by increased water intake"
                ],
                "explanation_en": "Paragraph 4 reports higher energy levels, better mood, and deeper sleep.",
                "explanation_tr": "4. paragraf daha yüksek enerji, iyi ruh hali ve derin uykuyu sıralar."
            }
        ]
    )
]

if __name__ == "__main__":
    print(f"Total A2 Reading Articles: {len(READING_A2)}")
    for a in READING_A2:
        print(f"  [{a['id']}] {a['title']} - {a['word_count']} words, {len(a['comprehension_questions'])} questions")
