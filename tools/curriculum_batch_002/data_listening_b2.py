#!/usr/bin/env python3
"""
Listening Batch 002: B2 Scenarios (12 scenarios).
"""

from listening_builder import build_scenario

SCENARIOS_B2 = [
    # 1. electric-vehicle-charging (daily-life)
    build_scenario(
        "listening.b2.electric-vehicle-charging",
        "Consulting an EV Home Charging Specialist",
        "B2", "product_discovery",
        "Tarik consults an electrical infrastructure specialist, Fiona, regarding installing a Level 2 smart home charger and optimizing time-of-use tariffs.",
        [
            {"id": "fiona", "name": "Fiona", "role": "Electrical Infrastructure Engineer", "accent": "British"},
            {"id": "tarik", "name": "Tarik", "role": "EV Homeowner", "accent": "Turkish"},
        ],
        "b2_electric_vehicle_charging.mp3",
        [
            {"speaker_id": "fiona", "text_en": "Good morning Tarik. I reviewed your home panel photos. Your current consumer unit has a sixty-amp main service fuse.", "text_tr": "Günaydın Tarık. Evinizin elektrik panosu fotoğraflarını inceledim. Mevcut sigorta kutunuzda altmış amperlik bir ana servis sigortası var."},
            {"speaker_id": "tarik", "text_en": "Thanks Fiona. Would that sixty-amp capacity support a standard seven-kilowatt Level 2 charging station alongside heat pumps?", "text_tr": "Teşekkürler Fiona. Bu altmış amperlik kapasite, ısı pompalarının yanı sıra standart yedi kilovatlık bir Seviye 2 şarj istasyonunu destekler mi?"},
            {"speaker_id": "fiona", "text_en": "It is borderline during peak winter evenings. If your induction cooktop, heat pump, and EV charger run simultaneously, you risk tripping the main breaker.", "text_tr": "Kış akşamları zirve saatlerde sınırda kalır. İndüksiyonlu ocağınız, ısı pompanız ve elektrikli araç şarj cihazınız aynı anda çalışırsa ana şalteri attırma riskiyle karşılaşırsınız."},
            {"speaker_id": "tarik", "text_en": "What solutions exist to prevent that without waiting months for the utility to upgrade the street cable to one hundred amps?", "text_tr": "Elektrik dağıtım şirketinin sokak kablosunu yüz ampere yükseltmesini aylarca beklemeden bunu önleyecek hangi çözümler var?"},
            {"speaker_id": "fiona", "text_en": "We can install a smart dynamic load-balancing charger. It clamps a current transformer sensor onto your main incoming cable and modulates charging current in real time.", "text_tr": "Akıllı dinamik yük dengeleyici bir şarj cihazı kurabiliriz. Ana giriş kablonuza bir akım trafosu sensörü kelepçeler ve şarj akımını gerçek zamanlı olarak ayarlar."},
            {"speaker_id": "tarik", "text_en": "So when domestic household demand spikes, the charger automatically throttles down the charging rate?", "text_tr": "Yani evdeki elektrik talebi aniden yükseldiğinde şarj cihazı şarj hızını otomatik olarak kısıyor mu?"},
            {"speaker_id": "fiona", "text_en": "Exactly. Furthermore, it integrates with time-of-use tariffs, scheduling your charging window between midnight and five in the morning when kilowatt rates drop by seventy percent.", "text_tr": "Kesinlikle. Dahası çok zamanlı tarifelerle entegre olarak şarj pencerenizi kilovatsaat tarifelerinin yüzde yetmiş düştüğü gece yarısı ile sabah beş arasına planlar."},
            {"speaker_id": "tarik", "text_en": "That dynamic load-balancing unit sounds like the ideal compromise. Let's proceed with that quotation.", "text_tr": "Bu dinamik yük dengeleme ünitesi ideal bir uzlaşma gibi görünüyor. Bu teklifle ilerleyelim."},
        ],
        [
            {
                "question_en": "What capacity limitation does Fiona identify on Tarik's home electrical panel?",
                "question_tr_hint": "Fiona Tarık'ın ev elektrik panosunda hangi kapasite kısıtlamasını tespit ediyor?",
                "correct_answer": "A sixty-amp main service fuse that could trip during peak household demand.",
                "distractors": [
                    "A completely severed copper ground cable.",
                    "A dangerous high-voltage electrical fire hazard.",
                    "Missing circuit breakers for all second-floor rooms."
                ],
                "explanation_en": "Fiona notes his main service fuse is 60 amps, which could trip during simultaneous usage.",
                "explanation_tr": "Fiona ana sigortanın 60 amper olduğunu ve eşzamanlı kullanımda atabileceğini belirtir."
            },
            {
                "question_en": "How does a dynamic load-balancing charger resolve the capacity bottleneck?",
                "question_tr_hint": "Dinamik yük dengeleyici bir şarj cihazı kapasite darboğazını nasıl çözer?",
                "correct_answer": "It monitors overall household current and throttles charging speed dynamically in real time.",
                "distractors": [
                    "It converts alternating current into solar battery power.",
                    "It turns off the household refrigerator whenever the car charges.",
                    "It doubles the street grid voltage automatically."
                ],
                "explanation_en": "Fiona explains it monitors current and modulates charging in real time.",
                "explanation_tr": "Fiona akımı izleyip şarjı gerçek zamanlı ayarladığını açıklar."
            },
            {
                "question_en": "What financial benefit does integration with time-of-use tariffs offer?",
                "question_tr_hint": "Çok zamanlı tarifelerle entegrasyon nasıl bir finansal fayda sağlar?",
                "correct_answer": "It schedules charging between midnight and 5:00 AM when electricity rates drop by seventy percent.",
                "distractors": [
                    "The power company provides free electricity on weekends.",
                    "The car manufacturer pays for all household heating bills.",
                    "Government rebates cover the entire cost of the vehicle."
                ],
                "explanation_en": "Fiona notes charging between midnight and 5:00 AM saves 70% on rates.",
                "explanation_tr": "Fiona gece yarısı ile 5:00 arasında şarjın tarifelerde %70 tasarruf sağladığını belirtir."
            },
            {
                "question_en": "Why does Tarik prefer dynamic load balancing over upgrading the street cable?",
                "question_tr_hint": "Tarık sokak kablosunu yükseltmek yerine neden dinamik yük dengelemeyi tercih ediyor?",
                "correct_answer": "Upgrading the street cable would require waiting months for the utility company.",
                "distractors": [
                    "Street cable upgrades are illegal under local building regulations.",
                    "He is planning to move out of the country next week.",
                    "Dynamic chargers require no electrical permits whatsoever."
                ],
                "explanation_en": "Tarik mentions avoiding waiting months for the utility to upgrade the street cable.",
                "explanation_tr": "Tarık dağıtım şirketinin sokak kablosunu yükseltmesini aylarca beklemekten kaçınmak istediğini belirtir."
            },
            {
                "question_en": "What decision does Tarik make at the end of the consultation?",
                "question_tr_hint": "Danışmanlığın sonunda Tarık nasıl bir karar alıyor?",
                "correct_answer": "Proceed with the quotation for the smart dynamic load-balancing charger.",
                "distractors": [
                    "Cancel the electric vehicle purchase entirely.",
                    "Install a gas-powered generator in his backyard.",
                    "Rely exclusively on public commercial fast chargers."
                ],
                "explanation_en": "Tarik approves moving forward with the dynamic load-balancing quotation.",
                "explanation_tr": "Tarık dinamik yük dengeleme teklifiyle ilerlemeyi onaylar."
            }
        ],
        ["daily-life", "ev", "sustainability", "energy", "technology"]
    ),

    # 2. high-speed-rail-delay (travel)
    build_scenario(
        "listening.b2.high-speed-rail-delay",
        "Managing a Cross-Border Rail Disruption at Munich Central",
        "B2", "incident_response",
        "Ceyda coordinates alternative connecting travel arrangements with train station controller Hans following an overhead catenary wire failure.",
        [
            {"id": "hans", "name": "Hans", "role": "Station Operations Controller", "accent": "German"},
            {"id": "ceyda", "name": "Ceyda", "role": "Business Traveler", "accent": "Turkish"},
        ],
        "b2_high_speed_rail_delay.mp3",
        [
            {"speaker_id": "hans", "text_en": "Guten Tag, Deutsche Bahn Customer Information. How may I direct your travel route?", "text_tr": "İyi günler, Deutsche Bahn Müşteri Danışma. Seyahat rotanızı nasıl yönlendirebilirim?"},
            {"speaker_id": "ceyda", "text_en": "Hello Hans. The information board just announced that the ICE train to Vienna has been canceled due to overhead wire damage near Salzburg.", "text_tr": "Merhaba Hans. Bilgi panosu az önce Salzburg yakınlarındaki havai hat hasarı nedeniyle Viyana'ya giden ICE treninin iptal edildiğini duyurdu."},
            {"speaker_id": "hans", "text_en": "Unfortunately yes. High winds brought down a major catenary section, blocking both primary tracks through the mountain corridor.", "text_tr": "Maalesef evet. Şiddetli rüzgar ana bir kataner hattını devirerek dağ koridorundaki her iki ana rayı da kapattı."},
            {"speaker_id": "ceyda", "text_en": "I have an essential medical conference keynote presentation in Vienna tomorrow morning at nine o'clock. What alternative transit options exist?", "text_tr": "Yarın sabah saat dokuzda Viyana'da önemli bir tıp konferansı açılış konuşmam var. Hangi alternatif ulaşım seçenekleri mevcut?"},
            {"speaker_id": "hans", "text_en": "We are operating emergency diesel shuttle services around the damaged track section to Linz, connecting with an Austrian RailJet directly to Vienna Hauptbahnhof.", "text_tr": "Hasarlı hat bölümünün etrafından Linz'e acil dizel servis trenleri çalıştırıyoruz, oradan doğrudan Viyana Hauptbahnhof'a giden Avusturya RailJet'e bağlanıyor."},
            {"speaker_id": "ceyda", "text_en": "How much additional journey time would that detour entail compared to the direct high-speed line?", "text_tr": "Bu aktarma doğrudan yüksek hızlı hatta kıyasla ne kadar ek yolculuk süresi gerektirir?"},
            {"speaker_id": "hans", "text_en": "It adds roughly eighty minutes, which puts your estimated arrival in Vienna at eleven thirty tonight. Your existing first-class ticket is fully valid on all replacement services.", "text_tr": "Yaklaşık seksen dakika ekler, bu da Viyana'ya tahmini varışınızı bu gece saat on bir buçuğa denk getirir. Mevcut birinci sınıf biletiniz tüm alternatif seferlerde tamamen geçerlidir."},
            {"speaker_id": "ceyda", "text_en": "Eleven thirty tonight still gives me adequate rest before my morning presentation. Could you stamp my ticket for the Austrian transfer conductor?", "text_tr": "Bu gece on bir buçuk yine de sabah sunumumdan önce yeterli dinlenme sağlar. Avusturyalı aktarma kondüktörü için biletime kaşe basabilir misiniz?"},
        ],
        [
            {
                "question_en": "What infrastructure failure caused the cancellation of the ICE train to Vienna?",
                "question_tr_hint": "Viyana'ya giden ICE treninin iptal edilmesine hangi altyapı arızası neden oldu?",
                "correct_answer": "Overhead catenary wires were downed by high winds near Salzburg.",
                "distractors": [
                    "A massive locomotive engine explosion inside Munich station.",
                    "A widespread computer cyberattack shutting down ticketing.",
                    "A national railroad strike by train drivers."
                ],
                "explanation_en": "Hans explains high winds brought down a catenary section blocking tracks.",
                "explanation_tr": "Hans şiddetli rüzgarın kataner hattını devirerek rayları kapattığını açıklar."
            },
            {
                "question_en": "Why is Ceyda's arrival in Vienna time-critical?",
                "question_tr_hint": "Ceyda'nın Viyana'ya varışı neden zaman açısından kritik?",
                "correct_answer": "She is delivering an essential medical conference keynote presentation tomorrow at 9:00 AM.",
                "distractors": [
                    "She is attending an international soccer championship match.",
                    "She must catch an intercontinental cruise ship departure.",
                    "She is attending a close family wedding ceremony at dawn."
                ],
                "explanation_en": "Ceyda explains she has a keynote presentation tomorrow at 9:00 AM.",
                "explanation_tr": "Ceyda yarın sabah saat 9:00'da açılış konuşması olduğunu belirtir."
            },
            {
                "question_en": "What replacement transit route does Hans recommend?",
                "question_tr_hint": "Hans hangi alternatif ulaşım güzergahını öneriyor?",
                "correct_answer": "An emergency diesel shuttle to Linz connecting with an Austrian RailJet to Vienna.",
                "distractors": [
                    "A sixteen-hour commercial bus through Switzerland.",
                    "Renting a luxury limousine to drive across the Alps.",
                    "Booking a domestic flight from Munich airport."
                ],
                "explanation_en": "Hans explains emergency diesel shuttles connect to Linz for an Austrian RailJet.",
                "explanation_tr": "Hans acil dizel servislerin Avusturya RailJet için Linz'e bağlandığını açıklar."
            },
            {
                "question_en": "How much additional transit time does the detour add?",
                "question_tr_hint": "Bu aktarma yolculuğa yaklaşık ne kadar ek süre ekliyor?",
                "correct_answer": "Roughly eighty minutes, arriving at 11:30 PM.",
                "distractors": [
                    "Ten additional minutes.",
                    "Over six hours of extra travel.",
                    "Two full calendar days."
                ],
                "explanation_en": "Hans notes it adds roughly 80 minutes, arriving at 11:30 PM.",
                "explanation_tr": "Hans yaklaşık 80 dakika eklediğini ve 23:30'da varacağını belirtir."
            },
            {
                "question_en": "What status does Ceyda's ticket hold on the replacement train?",
                "question_tr_hint": "Ceyda'nın biletinin alternatif trendeki durumu nedir?",
                "correct_answer": "Her first-class ticket is fully valid without any additional surcharge.",
                "distractors": [
                    "She must purchase an entirely new ticket in cash.",
                    "Her ticket is downgraded to economy freight seating.",
                    "She is only allowed to board if space permits."
                ],
                "explanation_en": "Hans confirms her existing first-class ticket is fully valid.",
                "explanation_tr": "Hans mevcut birinci sınıf biletinin tamamen geçerli olduğunu onaylar."
            }
        ],
        ["travel", "rail", "transit", "incident-management", "germany"]
    ),

    # 3. preventative-health-screening (health-lifestyle)
    build_scenario(
        "listening.b2.preventative-health-screening",
        "Interpreting Cardiovascular and Metabolic Biomarkers",
        "B2", "product_discovery",
        "Kemal discusses his comprehensive annual executive health screening panel, focusing on lipid profiles and glycemic control, with physician Dr. Aris.",
        [
            {"id": "dr_aris", "name": "Dr. Aris", "role": "Preventative Health Physician", "accent": "American"},
            {"id": "kemal", "name": "Kemal", "role": "Executive Patient", "accent": "Turkish"},
        ],
        "b2_preventative_health_screening.mp3",
        [
            {"speaker_id": "dr_aris", "text_en": "Good afternoon Kemal. I have compiled the results from your comprehensive annual executive metabolic screening.", "text_tr": "Tünaydın Kemal. Kapsamlı yıllık yönetici metabolik taramanızın sonuçlarını derledim."},
            {"speaker_id": "kemal", "text_en": "Thank you Dr. Aris. I have been feeling slightly fatigued during late afternoons. How do my lipid and glucose panels look?", "text_tr": "Teşekkürler Dr. Aris. Öğleden sonraları kendimi biraz yorgun hissediyordum. Lipid ve glukoz panellerim nasıl görünüyor?"},
            {"speaker_id": "dr_aris", "text_en": "Your fasting glucose is ninety-two, which is normal, but your hemoglobin A1c is five point seven percent, placing you right at the pre-diabetes threshold.", "text_tr": "Açlık glukozunuz doksan iki, bu normal; ancak hemoglobin A1c'niz yüzde beş nokta yedi, bu da sizi tam prediyabet eşiğine koyuyor."},
            {"speaker_id": "kemal", "text_en": "That is concerning given my family history of Type 2 diabetes. What about my cardiovascular markers?", "text_tr": "Ailemdeki Tip 2 diyabet geçmişi göz önüne alındığında bu endişe verici. Peki ya kardiyovasküler belirteçlerim?"},
            {"speaker_id": "dr_aris", "text_en": "Your total cholesterol is slightly elevated, but the more critical marker is ApoB and small dense LDL particles, which indicate atherogenic particle burden.", "text_tr": "Toplam kolesterolünüz biraz yüksek ancak daha kritik belirteç aterojenik parçacık yükünü gösteren ApoB ve küçük yoğun LDL parçacıklarıdır."},
            {"speaker_id": "kemal", "text_en": "Do I need to initiate prescription statin therapy immediately, or can we attempt lifestyle intervention first?", "text_tr": "Hemen reçeteli statin tedavisine başlamam gerekiyor mu, yoksa önce yaşam tarzı müdahalesini deneyebilir miyiz?"},
            {"speaker_id": "dr_aris", "text_en": "Given that your coronary artery calcium score is zero, we have a safe twelve-week window to trial structured nutritional and exercise interventions.", "text_tr": "Koroner arter kalsiyum skorunuzun sıfır olduğu göz önüne alındığında yapılandırılmış beslenme ve egzersiz müdahalelerini denemek için güvenli bir on iki haftalık penceremiz var."},
            {"speaker_id": "kemal", "text_en": "What specific dietary modifications will yield the most impactful glycemic improvements?", "text_tr": "Hangi özel beslenme değişiklikleri en etkili glisemik iyileşmeleri sağlayacaktır?"},
            {"speaker_id": "dr_aris", "text_en": "Eliminate refined carbohydrates and ultra-processed seed oils, increase viscous soluble fiber to forty grams daily, and incorporate zone-two aerobic training.", "text_tr": "Rafine karbonhidratları ve aşırı işlenmiş tohum yağlarını hayatınızdan çıkarın, yapışkan çözünür lifi günde kırk grama çıkarın ve bölge-iki aerobik antrenmanını dahil edin."},
        ],
        [
            {
                "question_en": "What specific biomarker indicates that Kemal is at the pre-diabetes threshold?",
                "question_tr_hint": "Kemal'in prediyabet eşiğinde olduğunu hangi belirteç göstermektedir?",
                "correct_answer": "Hemoglobin A1c of 5.7 percent.",
                "distractors": [
                    "A fasting blood glucose of four hundred.",
                    "An elevated white blood cell count.",
                    "Extremely low platelet density."
                ],
                "explanation_en": "Dr. Aris explains HbA1c is 5.7%, which sits right at the pre-diabetes threshold.",
                "explanation_tr": "Dr. Aris HbA1c'nin %5.7 olup prediyabet eşiğinde olduğunu açıklar."
            },
            {
                "question_en": "Why does Dr. Aris emphasize ApoB and small dense LDL over total cholesterol?",
                "question_tr_hint": "Dr. Aris toplam kolesterol yerine neden ApoB ve küçük yoğun LDL'yi vurguluyor?",
                "correct_answer": "They reflect atherogenic particle burden and cardiovascular plaque risk more accurately.",
                "distractors": [
                    "Total cholesterol tests are universally illegal.",
                    "ApoB measures bone density in the legs.",
                    "Small dense LDL indicates liver hepatitis."
                ],
                "explanation_en": "Dr. Aris notes ApoB indicates true atherogenic particle burden.",
                "explanation_tr": "Dr. Aris ApoB'nin gerçek aterojenik parçacık yükünü yansıttığını belirtir."
            },
            {
                "question_en": "What crucial diagnostic test allows Kemal to trial lifestyle changes before medication?",
                "question_tr_hint": "Hangi kritik tanı testi Kemal'in ilaçtan önce yaşam tarzı değişikliklerini denemesine izin veriyor?",
                "correct_answer": "A coronary artery calcium (CAC) score of zero.",
                "distractors": [
                    "A normal resting electrocardiogram.",
                    "A negative chest X-ray examination.",
                    "Normal psychological cognitive screening."
                ],
                "explanation_en": "Dr. Aris points out his coronary artery calcium score is zero, allowing a 12-week trial.",
                "explanation_tr": "Dr. Aris koroner kalsiyum skorunun sıfır olduğunu ve 12 haftalık denemeye izin verdiğini belirtir."
            },
            {
                "question_en": "How long is the structured trial window before re-testing biomarkers?",
                "question_tr_hint": "Belirteçlerin yeniden test edilmesinden önceki yapılandırılmış deneme penceresi ne kadardır?",
                "correct_answer": "Twelve weeks.",
                "distractors": [
                    "Two days.",
                    "Five years.",
                    "One single week."
                ],
                "explanation_en": "Dr. Aris specifies a safe twelve-week window.",
                "explanation_tr": "Dr. Aris güvenli bir 12 haftalık pencere belirtir."
            },
            {
                "question_en": "What dietary and training changes does Dr. Aris recommend?",
                "question_tr_hint": "Dr. Aris hangi beslenme ve antrenman değişikliklerini öneriyor?",
                "correct_answer": "Eliminating refined carbohydrates, increasing soluble fiber to 40g, and zone-two aerobic training.",
                "distractors": [
                    "Consuming sugar before every meal and sleeping four hours.",
                    "Eating only red meat and avoiding all physical activity.",
                    "Drinking three liters of fruit juice every morning."
                ],
                "explanation_en": "Dr. Aris recommends cutting refined carbs, hitting 40g fiber, and zone-two training.",
                "explanation_tr": "Dr. Aris rafine karbonhidratları kesmeyi, 40 gr lif almayı ve bölge-2 antrenmanı önerir."
            }
        ],
        ["health-lifestyle", "medicine", "biomarkers", "preventative-care"]
    ),

    # 4. artisanal-coffee-sourcing (food-shopping)
    build_scenario(
        "listening.b2.artisanal-coffee-sourcing",
        "Negotiating Specialty Coffee Direct-Trade Contracts",
        "B2", "negotiation",
        "Nilay negotiates direct-trade specialty green coffee procurement contracts with Colombian cooperative export director Mateo, balancing bean quality against shipping volatility.",
        [
            {"id": "mateo", "name": "Mateo", "role": "Coffee Cooperative Export Director", "accent": "Spanish"},
            {"id": "nilay", "name": "Nilay", "role": "Specialty Coffee Roaster", "accent": "Turkish"},
        ],
        "b2_artisanal_coffee_sourcing.mp3",
        [
            {"speaker_id": "mateo", "text_en": "Welcome to our cupping lab Nilay. You evaluated the anaerobic natural Geisha and washed Pink Bourbon lots this morning. What were your cupping scores?", "text_tr": "Tadım laboratuvarımıza hoş geldin Nilay. Bu sabah anaerobik doğal Geisha ve yıkanmış Pink Bourbon partilerini değerlendirdin. Tadım puanların neydi?"},
            {"speaker_id": "nilay", "text_en": "Hello Mateo. The Pink Bourbon lot was extraordinary. It scored an eighty-eight point five with distinct jasmine florals, peach acidity, and clean silky mouthfeel.", "text_tr": "Merhaba Mateo. Pink Bourbon partisi olağanüstüydü. Belirgin yasemin çiçekleri, şeftali asiditesi ve temiz ipeksi gövdesiyle seksen sekiz nokta beş puan aldı."},
            {"speaker_id": "mateo", "text_en": "That lot represents our highest-altitude harvest at nineteen hundred meters in Huila. Because yield was limited by unseasonal rains, we are pricing it at five dollars twenty per pound FOB.", "text_tr": "Bu parti Huila'da bin dokuz yüz metredeki en yüksek irtifa hasadımızı temsil ediyor. Mevsim dışı yağmurlar verimi sınırladığından, FOB bazında libresi beş dolar yirmi sent olarak fiyatlandırıyoruz."},
            {"speaker_id": "nilay", "text_en": "Five twenty is above our projected benchmark for specialty microlots. Could we negotiate four dollars eighty if we contract for sixty GrainPro bags upfront?", "text_tr": "Beş yirmi, özel mikro partiler için öngördüğümüz kıstasın üzerinde. Peşin olarak altmış GrainPro çuval için sözleşme yaparsak dört dolar seksene anlaşabilir miyiz?"},
            {"speaker_id": "mateo", "text_en": "If you commit to sixty bags, we can meet at five dollars flat, provided you cover the temperature-controlled reefer container surcharge for maritime shipping.", "text_tr": "Altmış çuvala taahhüt verirseniz, deniz taşımacılığı için sıcaklık kontrollü reefer konteyner ek ücretini karşılamanız şartıyla düz beş dolarda buluşabiliriz."},
            {"speaker_id": "nilay", "text_en": "Preserving moisture stability through maritime transit is essential to prevent baggy cardboard defects. I accept the reefer surcharge.", "text_tr": "Deniz taşımacılığı sırasında nem dengesini korumak çuval karton kusurlarını önlemek için esastır. Reefer ek ücretini kabul ediyorum."},
            {"speaker_id": "mateo", "text_en": "Excellent. We will mill the parchment and pack the green coffee in multi-layer hermetic GrainPro bags next week. Export documentation will follow shortly.", "text_tr": "Mükemmel. Parşömeni ayıklayıp yeşil kahveyi gelecek hafta çok katmanlı hermetik GrainPro torbalarda paketleyeceğiz. İhracat belgeleri kısa süre içinde takip edecek."},
            {"speaker_id": "nilay", "text_en": "Thank you Mateo. This direct partnership guarantees exceptional quality for our roastery while ensuring sustainable premium prices for your farming community.", "text_tr": "Teşekkürler Mateo. Bu doğrudan ortaklık kavurmahanemiz için olağanüstü kaliteyi garanti ederken çiftçi topluluğunuz için sürdürülebilir prim fiyatları sağlar."},
        ],
        [
            {
                "question_en": "What sensory cupping score did Nilay award the washed Pink Bourbon lot?",
                "question_tr_hint": "Nilay yıkanmış Pink Bourbon partisine kaç tadım puanı verdi?",
                "correct_answer": "88.5 points.",
                "distractors": [
                    "70.0 points.",
                    "95.0 points.",
                    "65.5 points."
                ],
                "explanation_en": "Nilay states the Pink Bourbon scored 88.5 points.",
                "explanation_tr": "Nilay Pink Bourbon'un 88.5 puan aldığını belirtir."
            },
            {
                "question_en": "What environmental factor limited coffee yields at the Huila farm?",
                "question_tr_hint": "Huila çiftliğinde kahve verimini hangi çevresel faktör sınırladı?",
                "correct_answer": "Unseasonal rains at high altitude.",
                "distractors": [
                    "A severe volcanic ash eruption.",
                    "A destructive insect infestation.",
                    "Extreme heatwave drought drying up rivers."
                ],
                "explanation_en": "Mateo explains yield was limited by unseasonal rains at 1,900 meters.",
                "explanation_tr": "Mateo 1900 metredeki mevsim dışı yağmurların verimi kısıtladığını açıklar."
            },
            {
                "question_en": "What compromised price per pound did the negotiators agree upon?",
                "question_tr_hint": "Müzakereciler libre başına hangi uzlaşılan fiyatta anlaştılar?",
                "correct_answer": "Five dollars flat per pound.",
                "distractors": [
                    "Four dollars eighty cents.",
                    "Five dollars twenty cents.",
                    "Three dollars fifty cents."
                ],
                "explanation_en": "Mateo proposes meeting at five dollars flat for 60 bags, which Nilay accepts.",
                "explanation_tr": "Mateo 60 çuval için düz 5 dolarda buluşmayı önerir ve Nilay kabul eder."
            },
            {
                "question_en": "Why does Nilay accept the temperature-controlled reefer container surcharge?",
                "question_tr_hint": "Nilay sıcaklık kontrollü reefer konteyner ek ücretini neden kabul ediyor?",
                "correct_answer": "To preserve moisture stability during maritime shipping and prevent defects.",
                "distractors": [
                    "Because maritime international law mandates reefer containers for all cargo.",
                    "To speed up the ocean cargo ship transit by three weeks.",
                    "Because regular dry containers are completely out of stock."
                ],
                "explanation_en": "Nilay notes moisture stability prevents baggy cardboard defects.",
                "explanation_tr": "Nilay nem dengesini korumanın çuval kusurlarını önlediğini belirtir."
            },
            {
                "question_en": "In what specialized packaging will the green coffee be shipped?",
                "question_tr_hint": "Yeşil kahve hangi özel ambalajda sevk edilecek?",
                "correct_answer": "Multi-layer hermetic GrainPro bags.",
                "distractors": [
                    "Open burlap cotton sacks without liners.",
                    "Discarded wooden fruit crates.",
                    "Plastic water barrels."
                ],
                "explanation_en": "Mateo specifies packing in multi-layer hermetic GrainPro bags.",
                "explanation_tr": "Mateo çok katmanlı hermetik GrainPro torbalarda paketleneceğini belirtir."
            }
        ],
        ["food-shopping", "coffee", "trade", "sourcing", "negotiation"]
    ),

    # 5. mentorship-career-pivot (relationships)
    build_scenario(
        "listening.b2.mentorship-career-pivot",
        "Navigating a Career Pivot to Data Platform Engineering",
        "B2", "stakeholder_alignment",
        "Onur consults his staff engineering mentor Elena on transitioning from legacy monolith backend development to distributed data infrastructure engineering.",
        [
            {"id": "elena", "name": "Elena", "role": "Staff Engineering Mentor", "accent": "American"},
            {"id": "onur", "name": "Onur", "role": "Senior Backend Developer", "accent": "Turkish"},
        ],
        "b2_mentorship_career_pivot.mp3",
        [
            {"speaker_id": "elena", "text_en": "Hi Onur. Thanks for booking this quarterly mentorship session. You mentioned in your prep notes that you are contemplating a lateral career transition?", "text_tr": "Selam Onur. Bu üç aylık mentorluk seansını ayırttığın için teşekkürler. Hazırlık notlarında yatay bir kariyer geçişi düşündüğünü belirtmiştin?"},
            {"speaker_id": "onur", "text_en": "Hi Elena. Yes, after five years building CRUD microservices and REST APIs, I feel my intellectual growth has plateaued. I want to transition into distributed data platform engineering.", "text_tr": "Selam Elena. Evet, beş yıl CRUD mikroservisleri ve REST API'leri geliştirdikten sonra entelektüel gelişimimin duraksadığını hissediyorum. Dağıtık veri platformu mühendisliğine geçmek istiyorum."},
            {"speaker_id": "elena", "text_en": "That is an exceptionally high-leverage domain. What foundational architectural paradigms do you need to bridge between standard backend and streaming systems?", "text_tr": "Bu son derece yüksek etkili bir alan. Standart arka uç ile akış sistemleri arasında hangi temel mimari paradigmalar arasında köprü kurman gerekiyor?"},
            {"speaker_id": "onur", "text_en": "I understand relational SQL indexing well, but I need deep hands-on mastery of distributed event logs like Apache Kafka, partition rebalancing, and stream processing with Flink.", "text_tr": "İlişkisel SQL indekslemeyi iyi anlıyorum ancak Apache Kafka gibi dağıtık olay günlükleri, bölüm yeniden dengeleme ve Flink ile akış işleme konularında derin pratik uzmanlığa ihtiyacım var."},
            {"speaker_id": "elena", "text_en": "Exactly. You must shift mental models from synchronous request-response guarantees to asynchronous eventual consistency, watermarking, and backpressure handling.", "text_tr": "Kesinlikle. Zihinsel modellerini senkronize istek-yanıt güvencelerinden eşzamansız nihai tutarlılık, filigranlama (watermarking) ve geri basınç yönetimine kaydırmalısın."},
            {"speaker_id": "onur", "text_en": "How can I demonstrate credible competency internally without taking a demotion to a junior data role?", "text_tr": "Junior bir veri rolüne düşürülmeden şirket içinde güvenilir yetkinliği nasıl sergileyebilirim?"},
            {"speaker_id": "elena", "text_en": "Partner with our data platform squad on their current CDC migration. Volunteer to write the Kafka Connect sink connectors that ingest our product databases into Snowflake.", "text_tr": "Mevcut CDC geçişlerinde veri platformu ekibimizle ortaklık kur. Ürün veritabanlarımızı Snowflake'e aktaran Kafka Connect alıcı bağlayıcılarını yazmaya gönüllü ol."},
            {"speaker_id": "onur", "text_en": "That gives me production-grade distributed systems experience directly tied to business value. I will reach out to their tech lead today.", "text_tr": "Bu bana doğrudan ticari değerle bağlantılı üretim kalitesinde dağıtık sistemler deneyimi kazandırır. Bugün onların teknik liderine ulaşacağım."},
        ],
        [
            {
                "question_en": "Why does Onur want to transition away from traditional backend API development?",
                "question_tr_hint": "Onur geleneksel arka uç API geliştirmeden neden ayrılmak istiyor?",
                "correct_answer": "After five years of building CRUD APIs, he feels his intellectual growth has plateaued.",
                "distractors": [
                    "His company announced bankruptcy and cancelled all salaries.",
                    "He lost all interest in writing any computer software code.",
                    "His manager ordered him to transfer immediately or face dismissal."
                ],
                "explanation_en": "Onur states he feels his intellectual growth has plateaued after five years.",
                "explanation_tr": "Onur beş yılın ardından entelektüel gelişiminin duraksadığını hissettiğini belirtir."
            },
            {
                "question_en": "What distributed systems concepts does Elena identify as critical for data engineering?",
                "question_tr_hint": "Elena veri mühendisliği için hangi dağıtık sistem kavramlarını kritik olarak tanımlıyor?",
                "correct_answer": "Eventual consistency, watermarking, and backpressure handling in event streams.",
                "distractors": [
                    "Manual paper filing and analog telephone switchboards.",
                    "Basic HTML web formatting and simple spreadsheet entry.",
                    "Fixing physical hardware power cords in the server closet."
                ],
                "explanation_en": "Elena emphasizes shifting mental models to eventual consistency, watermarking, and backpressure.",
                "explanation_tr": "Elena zihinsel modelleri nihai tutarlılık, filigranlama ve geri basınca kaydırmayı vurgular."
            },
            {
                "question_en": "What concern does Onur express regarding his career transition?",
                "question_tr_hint": "Onur kariyer geçişiyle ilgili hangi endişeyi dile getiriyor?",
                "correct_answer": "He worries about taking a demotion to a junior role during the transition.",
                "distractors": [
                    "He is afraid of having to move to another country.",
                    "He cannot understand English technical documentation.",
                    "He has forgotten all high school mathematics theorems."
                ],
                "explanation_en": "Onur asks how to demonstrate competency without taking a demotion to junior.",
                "explanation_tr": "Onur junior seviyesine düşürülmeden yetkinliği nasıl kanıtlayacağını sorar."
            },
            {
                "question_en": "What strategic project does Elena recommend Onur volunteer for?",
                "question_tr_hint": "Elena Onur'un hangi stratejik projeye gönüllü olmasını öneriyor?",
                "correct_answer": "Writing Kafka Connect sink connectors for the data squad's CDC migration into Snowflake.",
                "distractors": [
                    "Organizing the annual holiday office party catering.",
                    "Redesigning the company graphic logo in Photoshop.",
                    "Answering incoming customer billing complaints."
                ],
                "explanation_en": "Elena suggests writing Kafka Connect sink connectors for the CDC migration.",
                "explanation_tr": "Elena CDC geçişi için Kafka Connect bağlayıcıları yazmasını önerir."
            },
            {
                "question_en": "What next step does Onur decide to take immediately?",
                "question_tr_hint": "Onur hemen hangi sonraki adımı atmaya karar veriyor?",
                "correct_answer": "Reach out to the data platform squad's tech lead today.",
                "distractors": [
                    "Submit his formal resignation to human resources.",
                    "Enroll in a full-time four-year college degree.",
                    "Delete all his existing software code repositories."
                ],
                "explanation_en": "Onur says he will reach out to their tech lead today.",
                "explanation_tr": "Onur bugün teknik lidere ulaşacağını söyler."
            }
        ],
        ["relationships", "career", "mentorship", "software-engineering", "data"]
    ),

    # 6. postgraduate-thesis-proposal (education)
    build_scenario(
        "listening.b2.postgraduate-thesis-proposal",
        "Defending an Algorithmic Fairness Master's Thesis Proposal",
        "B2", "stakeholder_alignment",
        "Seda defends her graduate research methodology on auditing demographic disparity in credit scoring machine learning models before advisor Professor Wallace.",
        [
            {"id": "prof_wallace", "name": "Professor Wallace", "role": "Faculty Research Advisor", "accent": "British"},
            {"id": "seda", "name": "Seda", "role": "Master's Candidate", "accent": "Turkish"},
        ],
        "b2_postgraduate_thesis_proposal.mp3",
        [
            {"speaker_id": "prof_wallace", "text_en": "Welcome Seda. I read your thesis prospectus on algorithmic fairness in automated credit underwriting. You propose evaluating demographic parity against equalized odds.", "text_tr": "Hoş geldin Seda. Otomatik kredi değerlendirmesinde algoritmik adillik üzerine tez taslağını okudum. Demografik eşitliği fırsat eşitliğine (equalized odds) karşı değerlendirmeyi öneriyorsun."},
            {"speaker_id": "seda", "text_en": "Thank you Professor Wallace. Yes, demographic parity enforces equal acceptance rates across protected demographic groups, regardless of underlying credit risk distributions.", "text_tr": "Teşekkürler Profesör Wallace. Evet, demografik eşitlik temel kredi riski dağılımlarına bakılmaksızın korunan demografik gruplar genelinde eşit kabul oranlarını zorunlu kılar."},
            {"speaker_id": "prof_wallace", "text_en": "And what mathematical trade-off arises when lenders attempt to enforce demographic parity simultaneously with equalized odds?", "text_tr": "Peki kredi kuruluşları fırsat eşitliği ile eşzamanlı olarak demografik eşitliği uygulamaya çalıştığında hangi matematiksel ödünleşim ortaya çıkar?"},
            {"speaker_id": "seda", "text_en": "Kleinberg's impossibility theorem demonstrates that unless base rates of default are identical across populations, these two fairness criteria are mathematically incompatible.", "text_tr": "Kleinberg'in imkansızlık teoremi temerrüt taban oranları popülasyonlar genelinde özdeş olmadıkça bu iki adillik kriterinin matematiksel olarak uyumsuz olduğunu göstermektedir."},
            {"speaker_id": "prof_wallace", "text_en": "Precisely formulated. Now, how will your empirical methodology validate model fairness without access to protected attributes like race or gender in commercial loan datasets?", "text_tr": "Kesinlikle doğru formüle edilmiş. Peki ticari kredi veri setlerinde ırk veya cinsiyet gibi korunan özelliklere erişim olmadan ampirik metodolojiniz model adilliğini nasıl doğrulayacak?"},
            {"speaker_id": "seda", "text_en": "I will employ Bayesian Improved First Name Surname Geocoding (BIFSG) to infer probabilistic demographic distributions while evaluating proxy variable leakage.", "text_tr": "Vekil değişken sızıntısını değerlendirirken olasılıksal demografik dağılımları çıkarmak için Bayesçi Geliştirilmiş İsim Soyisim Coğrafi Kodlaması (BIFSG) kullanacağım."},
            {"speaker_id": "prof_wallace", "text_en": "BIFSG introduces imputation noise. You must incorporate sensitivity analysis to quantify how imputation error bounds impact your fairness metrics.", "text_tr": "BIFSG atama gürültüsü getirir. Atama hata sınırlarının adillik metriklerinizi nasıl etkilediğini ölçmek için duyarlılık analizini dahil etmelisiniz."},
            {"speaker_id": "seda", "text_en": "I will add a formal Monte Carlo perturbation chapter to measure the variance of fairness metrics under synthetic imputation noise.", "text_tr": "Sentetik atama gürültüsü altında adillik metriklerinin varyansını ölçmek için resmi bir Monte Carlo pertürbasyon bölümü ekleyeceğim."},
        ],
        [
            {
                "question_en": "What is the core subject of Seda's master's thesis proposal?",
                "question_tr_hint": "Seda'nın yüksek lisans tez önerisinin ana konusu nedir?",
                "correct_answer": "Algorithmic fairness criteria and demographic disparities in automated credit underwriting.",
                "distractors": [
                    "Designing high-speed quantum processors for space exploration.",
                    "The history of ancient Egyptian architectural excavation techniques.",
                    "Developing biological vaccines against plant agricultural viruses."
                ],
                "explanation_en": "Wallace notes the prospectus is on algorithmic fairness in automated credit underwriting.",
                "explanation_tr": "Wallace taslağın otomatik kredi değerlendirmesinde algoritmik adillik üzerine olduğunu belirtir."
            },
            {
                "question_en": "What mathematical principle does Seda cite regarding conflicting fairness definitions?",
                "question_tr_hint": "Seda çelişen adillik tanımlarıyla ilgili hangi matematiksel ilkeye atıfta bulunuyor?",
                "correct_answer": "Kleinberg's impossibility theorem proving demographic parity and equalized odds are incompatible.",
                "distractors": [
                    "Pythagoras's geometric triangle theorem.",
                    "Newton's universal law of gravitation.",
                    "Moore's law of microprocessor transistor scaling."
                ],
                "explanation_en": "Seda cites Kleinberg's theorem on incompatible fairness criteria when base rates differ.",
                "explanation_tr": "Seda taban oranlar farklıyken adillik kriterlerinin uyumsuzluğunu gösteren Kleinberg teoremini anar."
            },
            {
                "question_en": "How does Seda plan to handle the absence of protected demographic labels in commercial data?",
                "question_tr_hint": "Seda ticari verilerde korunan demografik etiketlerin bulunmamasını nasıl ele almayı planlıyor?",
                "correct_answer": "Using Bayesian Improved First Name Surname Geocoding (BIFSG) to infer probabilistic distributions.",
                "distractors": [
                    "Guessing demographic attributes randomly with coin flips.",
                    "Calling every credit applicant on their personal telephone.",
                    "Ignoring fairness completely and assuming zero discrimination."
                ],
                "explanation_en": "Seda proposes using BIFSG to infer probabilistic demographics.",
                "explanation_tr": "Seda olasılıksal demografiyi çıkarmak için BIFSG kullanmayı önerir."
            },
            {
                "question_en": "What methodological limitation does Professor Wallace point out regarding BIFSG?",
                "question_tr_hint": "Profesör Wallace BIFSG hakkında hangi metodolojik sınırlamaya işaret ediyor?",
                "correct_answer": "It introduces imputation noise that requires sensitivity analysis.",
                "distractors": [
                    "It is completely illegal under international copyright law.",
                    "It requires supercomputers that do not exist on Earth.",
                    "It only works on ancient dead languages."
                ],
                "explanation_en": "Wallace notes BIFSG introduces imputation noise needing sensitivity analysis.",
                "explanation_tr": "Wallace BIFSG'nin duyarlılık analizi gerektiren atama gürültüsü getirdiğini belirtir."
            },
            {
                "question_en": "What analytical technique does Seda promise to add to quantify imputation variance?",
                "question_tr_hint": "Seda atama varyansını ölçmek için hangi analitik tekniği eklemeyi vaat ediyor?",
                "correct_answer": "A formal Monte Carlo perturbation analysis.",
                "distractors": [
                    "A manual physical coin-toss tournament.",
                    "A public survey on social media networks.",
                    "A written essay describing personal opinions."
                ],
                "explanation_en": "Seda promises to add a formal Monte Carlo perturbation chapter.",
                "explanation_tr": "Seda resmi bir Monte Carlo pertürbasyon bölümü ekleyeceğini söyler."
            }
        ],
        ["education", "academic", "thesis", "machine-learning", "ethics"]
    ),

    # 7. cross-cultural-client-pitch (communication)
    build_scenario(
        "listening.b2.cross-cultural-client-pitch",
        "Debriefing a Japanese Enterprise Client Presentation",
        "B2", "stakeholder_alignment",
        "Berk and marketing partner Chloe debrief a high-stakes enterprise software sales pitch to a Tokyo conglomerate, decoding subtle non-verbal cues and indirect consensus dynamics.",
        [
            {"id": "chloe", "name": "Chloe", "role": "Global Account Director", "accent": "American"},
            {"id": "berk", "name": "Berk", "role": "Technical Sales Engineer", "accent": "Turkish"},
        ],
        "b2_cross_cultural_client_pitch.mp3",
        [
            {"speaker_id": "chloe", "text_en": "Great presentation Berk! The live microservice latency benchmark demo was flawless. How do you feel about the executive committee's reaction?", "text_tr": "Harika sunumdu Berk! Canlı mikroservis gecikme kıyaslama demosu kusursuzdu. Yönetim kurulunun tepkisi hakkında ne hissediyorsun?"},
            {"speaker_id": "berk", "text_en": "Thanks Chloe. The technical directors nodded continuously throughout our slides, but when I asked if they were ready to sign the pilot agreement, the room fell completely silent.", "text_tr": "Teşekkürler Chloe. Teknik direktörler slaytlarımız boyunca sürekli başlarını salladılar ancak pilot sözleşmeyi imzalamaya hazır olup olmadıklarını sorduğumda oda tamamen sessizliğe gömüldü."},
            {"speaker_id": "chloe", "text_en": "Ah, that is a classic cross-cultural misinterpretation. In Japanese business culture, nodding indicates 'I hear and understand you', not necessarily 'I agree with your proposal'.", "text_tr": "Ah, bu klasik bir kültürlerarası yanlış yorumlama. Japon iş kültüründe baş sallamak 'Sizi duyuyor ve anlıyorum' anlamına gelir; ille de 'Teklifinize katılıyorum' demek değildir."},
            {"speaker_id": "berk", "text_en": "Did I commit a faux pas by asking for an immediate commercial commitment at the end of the meeting?", "text_tr": "Toplantının sonunda hemen ticari bir taahhüt isteyerek bir pot mu kırdım?"},
            {"speaker_id": "chloe", "text_en": "A slight one. Enterprise procurement in Japan relies on 'ringisho'—a meticulous bottom-up consensus building process. Asking executives to commit publicly on the spot causes loss of face.", "text_tr": "Ufak bir pot. Japonya'da kurumsal satın alma 'ringisho'ya—aşağıdan yukarıya titiz bir fikir birliği oluşturma sürecine—dayanır. Yöneticilerden anında kamuya açık taahhüt istemek itibar kaybına neden olur."},
            {"speaker_id": "berk", "text_en": "I understand now. The silence was not rejection; they needed internal space to deliberate without confrontational pressure.", "text_tr": "Şimdi anlıyorum. Sessizlik ret değildi; çatışmacı baskı olmadan müzakere etmek için iç alana ihtiyaçları vardı."},
            {"speaker_id": "chloe", "text_en": "Precisely. The managing director did mention that our technical compliance documentation was very thorough. That is an encouraging signal.", "text_tr": "Kesinlikle. Genel müdür teknik uyumluluk belgelerimizin çok kapsamlı olduğunu belirtti. Bu cesaret verici bir sinyal."},
            {"speaker_id": "berk", "text_en": "I will send a polite, formal follow-up letter expressing gratitude for their hospitality and offering supplementary architectural schematics.", "text_tr": "Misafirperverlikleri için minnettarlığımı ifade eden ve ek mimari şemalar sunan kibar, resmi bir takip mektubu göndereceğim."},
        ],
        [
            {
                "question_en": "How did Berk misinterpret the Japanese executives' nodding during the presentation?",
                "question_tr_hint": "Berk sunum sırasında Japon yöneticilerin baş sallamasını nasıl yanlış yorumladı?",
                "correct_answer": "He assumed nodding meant agreement, whereas it only signaled attentive listening.",
                "distractors": [
                    "He thought they were falling asleep from boredom.",
                    "He believed they were rejecting his software completely.",
                    "He thought they did not speak any English."
                ],
                "explanation_en": "Chloe explains nodding signifies comprehension and listening, not agreement.",
                "explanation_tr": "Chloe baş sallamanın onay değil anlama ve dinleme anlamına geldiğini açıklar."
            },
            {
                "question_en": "Why was asking for an immediate contract signing considered a cultural misstep?",
                "question_tr_hint": "Hemen sözleşme imzalanmasını istemek neden kültürel bir hata olarak görüldü?",
                "correct_answer": "Decisions rely on consensus building (ringisho), and public on-the-spot pressure causes loss of face.",
                "distractors": [
                    "Signatures are completely illegal under Japanese corporate law.",
                    "Foreign vendors are forbidden from entering corporate office towers.",
                    "Contracts must always be carved into physical wooden tablets."
                ],
                "explanation_en": "Chloe explains decisions require bottom-up consensus and spot pressure causes loss of face.",
                "explanation_tr": "Chloe kararların konsensüs gerektirdiğini ve anlık baskının itibar kaybına yol açtığını açıklar."
            },
            {
                "question_en": "What positive signal did the managing director communicate before departing?",
                "question_tr_hint": "Genel müdür ayrılmadan önce hangi olumlu sinyali verdi?",
                "correct_answer": "Commended the technical compliance documentation as being very thorough.",
                "distractors": [
                    "Offered to buy the entire software startup company.",
                    "Invited Berk to a private karaoke party that night.",
                    "Handed over a cashier's check for one million dollars."
                ],
                "explanation_en": "Chloe notes the managing director praised the thoroughness of technical documentation.",
                "explanation_tr": "Chloe genel müdürün teknik belgelerin kapsamlılığını övdüğünü belirtir."
            },
            {
                "question_en": "What did the prolonged silence following Berk's closing question truly indicate?",
                "question_tr_hint": "Berk'in kapanış sorusunun ardından gelen uzun sessizlik gerçekte neyi gösteriyordu?",
                "correct_answer": "A need for internal deliberation space without confrontational sales pressure.",
                "distractors": [
                    "A sudden medical emergency among the executives.",
                    "The translation headphones ran out of battery power.",
                    "An intentional insult intended to end the business relationship."
                ],
                "explanation_en": "Berk realizes silence was not rejection, but need for deliberation space.",
                "explanation_tr": "Berk sessizliğin ret değil müzakere alanı ihtiyacı olduğunu fark eder."
            },
            {
                "question_en": "What appropriate follow-up action does Berk propose?",
                "question_tr_hint": "Berk nasıl uygun bir takip eylemi öneriyor?",
                "correct_answer": "Send a formal thank-you letter offering supplementary architectural schematics.",
                "distractors": [
                    "Call the managing director's home telephone repeatedly.",
                    "Show up unannounced at the client's corporate headquarters tomorrow.",
                    "Threaten to sell the technology to their biggest competitor."
                ],
                "explanation_en": "Berk decides to send a polite formal thank-you letter with supplementary schematics.",
                "explanation_tr": "Berk ek şemalar içeren kibar resmi bir teşekkür mektubu göndermeye karar verir."
            }
        ],
        ["communication", "cross-cultural", "sales", "japan", "negotiation"]
    ),

    # 8. incident-postmortem-database (communication)
    build_scenario(
        "listening.b2.incident-postmortem-database",
        "Conducting a Blameless Database Outage Postmortem",
        "B2", "incident_response",
        "Engineering lead Ziya convenes a blameless post-incident review with site reliability engineer Maya to analyze a major production database connection pool starvation outage.",
        [
            {"id": "maya", "name": "Maya", "role": "Site Reliability Engineer", "accent": "British"},
            {"id": "ziya", "name": "Ziya", "role": "Principal Engineering Lead", "accent": "Turkish"},
        ],
        "b2_incident_postmortem_database.mp3",
        [
            {"speaker_id": "ziya", "text_en": "Welcome everyone to the incident postmortem for yesterday's Severity-1 outage. As always, this is a blameless retrospective focused on systemic resilience.", "text_tr": "Dünkü 1. Seviye kesintinin olay sonrası incelemesine hoş geldiniz. Her zaman olduğu gibi bu sistemik dayanıklılığa odaklanan suçlamasız bir retrospektiftir."},
            {"speaker_id": "maya", "text_en": "Thanks Ziya. The outage began at fourteen twenty UTC when API gateway latency breached five seconds, followed by fifty-four percent of checkout requests failing with HTTP 504 gateway timeouts.", "text_tr": "Teşekkürler Ziya. Kesinti UTC ile 14:20'de API ağ geçidi gecikmesinin beş saniyeyi aşmasıyla başladı, ardından ödeme isteklerinin yüzde elli dördü HTTP 504 ağ geçidi zaman aşımıyla başarısız oldu."},
            {"speaker_id": "ziya", "text_en": "What was the immediate mechanical root cause identified during incident mitigation?", "text_tr": "Olay giderme sırasında tespit edilen acil mekanik kök neden neydi?"},
            {"speaker_id": "maya", "text_en": "A scheduled background batch job initiated an unindexed analytical query across the order history table, triggering a full table scan that held exclusive table locks.", "text_tr": "Zamanlanmış bir arka plan toplu işi sipariş geçmişi tablosunda indekslenmemiş bir analitik sorgu başlattı ve özel tablo kilitleri tutan tam bir tablo taramasını tetikledi."},
            {"speaker_id": "ziya", "text_en": "And that locked query starved the connection pool for incoming transactional customer checkout traffic?", "text_tr": "Ve kilitlenen bu sorgu gelen işlemsel müşteri ödeme trafiği için bağlantı havuzunu tüketti, öyle mi?"},
            {"speaker_id": "maya", "text_en": "Exactly. All two hundred connection pool threads became blocked waiting for lock acquisition. Because our connection acquisition timeout was set to thirty seconds, client requests piled up exponentially.", "text_tr": "Kesinlikle. İki yüz bağlantı havuzu iş parçacığının tümü kilit alımını beklerken engellendi. Bağlantı edinme zaman aşımımız otuz saniyeye ayarlandığından istemci istekleri katlanarak birikti."},
            {"speaker_id": "ziya", "text_en": "What architectural safeguards are we implementing to prevent recurrence?", "text_tr": "Tekrarlanmasını önlemek için hangi mimari önlemleri uyguluyoruz?"},
            {"speaker_id": "maya", "text_en": "First, analytical reporting queries are strictly segregated to a read-only replica with query cancellation timeouts. Second, we are reducing connection acquisition timeouts to two seconds with circuit breaking.", "text_tr": "İlk olarak analitik raporlama sorguları sorgu iptal zaman aşımlarına sahip salt okunur bir kopyaya kesin olarak ayrılıyor. İkinci olarak devre kesici ile bağlantı edinme zaman aşımlarını iki saniyeye düşürüyoruz."},
        ],
        [
            {
                "question_en": "What is the primary cultural principle of Ziya's incident review meeting?",
                "question_tr_hint": "Ziya'nın olay inceleme toplantısının temel kültürel ilkesi nedir?",
                "correct_answer": "A blameless retrospective focused on systemic resilience rather than individual punishment.",
                "distractors": [
                    "Identifying and firing the engineer who wrote the bad query.",
                    "Calculating financial deductions from developer paychecks.",
                    "Reporting software bugs to federal regulatory police."
                ],
                "explanation_en": "Ziya begins by stating it is a blameless retrospective focused on systemic resilience.",
                "explanation_tr": "Ziya bunun sistemik dayanıklılığa odaklanan suçlamasız bir retrospektif olduğunu belirtir."
            },
            {
                "question_en": "What error code did 54 percent of checkout requests return during the outage?",
                "question_tr_hint": "Kesinti sırasında ödeme isteklerinin yüzde 54'ü hangi hata kodunu döndürdü?",
                "correct_answer": "HTTP 504 gateway timeouts.",
                "distractors": [
                    "HTTP 404 page not found.",
                    "HTTP 200 successful OK.",
                    "HTTP 301 permanent redirect."
                ],
                "explanation_en": "Maya states 54% of requests failed with HTTP 504 gateway timeouts.",
                "explanation_tr": "Maya isteklerin %54'ünün HTTP 504 zaman aşımıyla başarısız olduğunu belirtir."
            },
            {
                "question_en": "What triggered the database table lock?",
                "question_tr_hint": "Veritabanı tablo kilidini ne tetikledi?",
                "correct_answer": "An unindexed analytical query initiated by a background batch job.",
                "distractors": [
                    "A physical power cable severed by a backhoe operator.",
                    "A computer virus encrypting all server files.",
                    "An earthquake damaging the primary cloud data center."
                ],
                "explanation_en": "Maya explains an unindexed query triggered a full table scan with exclusive locks.",
                "explanation_tr": "Maya indekslenmemiş bir sorgunun özel kilitli tam tablo taraması başlattığını açıklar."
            },
            {
                "question_en": "Why did client requests pile up exponentially in the connection pool?",
                "question_tr_hint": "Bağlantı havuzunda istemci istekleri neden katlanarak birikti?",
                "correct_answer": "The connection acquisition timeout was set to an excessive thirty seconds.",
                "distractors": [
                    "The database was limited to only two simultaneous users.",
                    "The cloud provider deliberately throttled network traffic.",
                    "Customers clicked the submit button fifty times per second."
                ],
                "explanation_en": "Maya explains the timeout was 30 seconds, causing exponential queue accumulation.",
                "explanation_tr": "Maya zaman aşımının 30 saniye olmasının katlanarak birikmeye yol açtığını açıklar."
            },
            {
                "question_en": "What two architectural safeguards will Maya implement?",
                "question_tr_hint": "Maya hangi iki mimari önlemi uygulayacak?",
                "correct_answer": "Segregating analytical queries to read replicas and cutting acquisition timeouts to two seconds with circuit breaking.",
                "distractors": [
                    "Deleting all historical customer orders and banning background jobs.",
                    "Upgrading to an on-premise mainframe computer running magnetic tape.",
                    "Closing the online checkout store during afternoon peak hours."
                ],
                "explanation_en": "Maya specifies read-only replica segregation and 2-second timeouts with circuit breaking.",
                "explanation_tr": "Maya salt okunur kopya ayrımı ve devre kesicili 2 saniyelik zaman aşımı belirtir."
            }
        ],
        ["communication", "incident-management", "engineering", "postmortem", "databases"]
    ),

    # 9. performance-appraisal-promotion (work-career)
    build_scenario(
        "listening.b2.performance-appraisal-promotion",
        "Negotiating Promotion to Staff Software Engineer",
        "B2", "negotiation",
        "Alper reviews his annual engineering impact metrics and strategic organizational leadership with VP of Engineering Sandra, making the case for promotion to Staff level.",
        [
            {"id": "sandra", "name": "Sandra", "role": "VP of Engineering", "accent": "American"},
            {"id": "alper", "name": "Alper", "role": "Senior Software Engineer", "accent": "Turkish"},
        ],
        "b2_performance_appraisal_promotion.mp3",
        [
            {"speaker_id": "sandra", "text_en": "Good morning Alper. Thank you for submitting your comprehensive self-evaluation packet. You are seeking promotion from Senior Engineer to Staff Engineer.", "text_tr": "Günaydın Alper. Kapsamlı öz değerlendirme paketini sunduğun için teşekkürler. Kıdemli Mühendislikten Staff Mühendisliğe terfi arıyorsun."},
            {"speaker_id": "alper", "text_en": "Morning Sandra. Yes, over the past twelve months, I have deliberately transitioned from individual feature delivery to organizational-level technical leadership.", "text_tr": "Günaydın Sandra. Evet, son on iki ayda bireysel özellik teslimatından kasıtlı olarak organizasyon düzeyinde teknik liderliğe geçiş yaptım."},
            {"speaker_id": "sandra", "text_en": "The promotion committee looks for demonstrated scope of influence across multiple squads. Walk me through your work on the distributed event streaming architecture.", "text_tr": "Terfi komitesi birden fazla ekip genelinde kanıtlanmış etki kapsamı arar. Bana dağıtık olay akışı mimarisi üzerindeki çalışmandan bahset."},
            {"speaker_id": "alper", "text_en": "I authored the RFC for our multi-region event bus, mentored four engineers across three distinct product squads, and established our company-wide asynchronous testing standards.", "text_tr": "Çok bölgeli olay veri yolumuz için RFC'yi kaleme aldım, üç farklı ürün ekibindeki dört mühendise mentorluk yaptım ve şirket çapındaki eşzamansız test standartlarımızı oluşturdum."},
            {"speaker_id": "sandra", "text_en": "The cross-squad mentorship is evident in your peer 360 reviews. What measurable business outcomes did the streaming initiative unlock?", "text_tr": "Ekipler arası mentorluk 360 derece akran değerlendirmelerinde açıkça görülüyor. Akış girişimi hangi ölçülebilir ticari sonuçların önünü açtı?"},
            {"speaker_id": "alper", "text_en": "It reduced our inter-service message latency by sixty percent and dropped cloud infrastructure operational expenses by forty thousand dollars monthly.", "text_tr": "Servisler arası mesaj gecikmemizi yüzde altmış azalttı ve bulut altyapısı operasyonel giderlerini ayda kırk bin dolar düşürdü."},
            {"speaker_id": "sandra", "text_en": "Those metrics provide concrete executive justification. A Staff engineer must also demonstrate strategic technical roadmapping aligned with executive business goals.", "text_tr": "Bu metrikler somut yönetici gerekçelendirmesi sağlıyor. Bir Staff mühendis ayrıca yönetici iş hedefleriyle uyumlu stratejik teknik yol haritaları sergilemelidir."},
            {"speaker_id": "alper", "text_en": "I have drafted a two-year zero-trust data governance roadmap that satisfies our upcoming SOC 2 Type II and GDPR audit compliance requirements.", "text_tr": "Gelecek SOC 2 Tip II ve GDPR denetim uyumluluk gereksinimlerimizi karşılayan iki yıllık sıfır güven veri yönetişimi yol haritası taslağı hazırladım."},
            {"speaker_id": "sandra", "text_en": "Impressive foresight Alper. I will enthusiastically sponsor your dossier at next week's executive calibration committee.", "text_tr": "Etkileyici bir öngörü Alper. Gelecek haftaki yönetici kalibrasyon komitesinde dosyanı coşkuyla destekleyeceğim."},
        ],
        [
            {
                "question_en": "What promotion level is Alper seeking in his appraisal?",
                "question_tr_hint": "Alper değerlendirmesinde hangi terfi seviyesini arıyor?",
                "correct_answer": "From Senior Software Engineer to Staff Software Engineer.",
                "distractors": [
                    "From Junior Developer to Mid-level Developer.",
                    "From Engineering Manager to Chief Executive Officer.",
                    "From Intern to Full-time Associate."
                ],
                "explanation_en": "Sandra notes Alper is seeking promotion from Senior to Staff Engineer.",
                "explanation_tr": "Sandra Alper'in Senior'dan Staff seviyesine terfi aradığını belirtir."
            },
            {
                "question_en": "What primary qualification does the promotion committee demand for Staff level?",
                "question_tr_hint": "Terfi komitesi Staff seviyesi için hangi temel niteliği talep ediyor?",
                "correct_answer": "Demonstrated technical scope of influence across multiple engineering squads.",
                "distractors": [
                    "Working eighty hours per week in the office.",
                    "Having a doctorate degree in theoretical mathematics.",
                    "Winning international programming speed contests."
                ],
                "explanation_en": "Sandra states the committee looks for demonstrated scope of influence across squads.",
                "explanation_tr": "Sandra komitenin ekipler arası kanıtlanmış etki kapsamı aradığını belirtir."
            },
            {
                "question_en": "What tangible financial saving did Alper's event streaming architecture deliver?",
                "question_tr_hint": "Alper'in olay akışı mimarisi ne kadar somut bir finansal tasarruf sağladı?",
                "correct_answer": "Reduced cloud infrastructure expenses by forty thousand dollars monthly.",
                "distractors": [
                    "Saved five dollars on office printer paper.",
                    "Cut executive salaries by fifty percent.",
                    "Generated one billion dollars in immediate stock value."
                ],
                "explanation_en": "Alper mentions dropping cloud operational expenses by $40,000 monthly.",
                "explanation_tr": "Alper bulut operasyonel giderlerini ayda 40.000 dolar düşürdüğünü belirtir."
            },
            {
                "question_en": "What strategic roadmap has Alper drafted for the coming two years?",
                "question_tr_hint": "Alper önümüzdeki iki yıl için nasıl bir stratejik yol haritası taslağı hazırladı?",
                "correct_answer": "A two-year zero-trust data governance roadmap for SOC 2 and GDPR compliance.",
                "distractors": [
                    "A plan to replace all human software developers with AI bots.",
                    "A proposal to close all international sales branch offices.",
                    "A scheme to rewrite the entire codebase in assembly language."
                ],
                "explanation_en": "Alper drafted a zero-trust data governance roadmap for SOC 2 and GDPR.",
                "explanation_tr": "Alper SOC 2 ve GDPR için sıfır güven veri yönetişimi yol haritası hazırladığını açıklar."
            },
            {
                "question_en": "What action does Sandra promise to take regarding Alper's promotion?",
                "question_tr_hint": "Sandra Alper'in terfisiyle ilgili hangi adımı atmaya söz veriyor?",
                "correct_answer": "Enthusiastically sponsor his dossier at next week's executive calibration committee.",
                "distractors": [
                    "Reject his promotion request immediately.",
                    "Order him to take a three-month unpaid leave of absence.",
                    "Transfer him to the customer support call center."
                ],
                "explanation_en": "Sandra promises to enthusiastically sponsor his dossier at the committee.",
                "explanation_tr": "Sandra gelecek haftaki komitede dosyasını coşkuyla destekleyeceğine söz verir."
            }
        ],
        ["work-career", "leadership", "promotion", "performance-review", "engineering"]
    ),

    # 10. sprint-velocity-estimation (work-career)
    build_scenario(
        "listening.b2.sprint-velocity-estimation",
        "Refining User Story Estimation and Technical Debt in Sprint Planning",
        "B2", "engineering_meeting",
        "Scrum master Gizem guides senior engineers Lucas and Tarik through evaluating technical story points and unblocking legacy dependencies during sprint planning.",
        [
            {"id": "gizem", "name": "Gizem", "role": "Agile Delivery Coach", "accent": "Turkish"},
            {"id": "lucas", "name": "Lucas", "role": "Senior Cloud Engineer", "accent": "American"},
        ],
        "b2_sprint_velocity_estimation.mp3",
        [
            {"speaker_id": "gizem", "text_en": "Alright team, let's estimate Ticket 512: migrating our user notification pipeline to serverless event triggers. What is our consensus estimate?", "text_tr": "Pekala ekip, Bilet 512'yi tahmin edelim: kullanıcı bildirim hattımızın sunucusuz olay tetikleyicilerine taşınması. Ortak tahminimiz nedir?"},
            {"speaker_id": "lucas", "text_en": "I pointed this at an eight. While the Lambda function implementation is straightforward, our legacy message queue lacks dead-letter queue handling for dropped payloads.", "text_tr": "Ben buna sekiz puan verdim. Lambda fonksiyonunun uygulanması basit olsa da eski mesaj kuyruğumuzda bırakılan yükler için teslim edilemeyen mesaj (dead-letter) kuyruğu yönetimi yok."},
            {"speaker_id": "gizem", "text_en": "An eight point story consumes nearly twenty percent of our historical forty-point sprint velocity. Can we decompose this story into two vertical deliverable slices?", "text_tr": "Sekiz puanlık bir hikaye geçmiş kırk puanlık sprint hızımızın neredeyse yüzde yirmisini tüketiyor. Bu hikayeyi dikey olarak teslim edilebilir iki dilime ayırabilir miyiz?"},
            {"speaker_id": "lucas", "text_en": "Yes. Story 512A could establish the dead-letter queue and retry infrastructure, which is a solid three points. Story 512B would handle the actual Lambda migration for five points.", "text_tr": "Evet. Hikaye 512A sağlam bir üç puan olan dead-letter kuyruğunu ve yeniden deneme altyapısını kurabilir. Hikaye 512B ise beş puanla asıl Lambda geçişini halleder."},
            {"speaker_id": "gizem", "text_en": "That makes testing and verification much safer. If 512A finishes mid-sprint, quality assurance can validate error retries while you proceed with 512B.", "text_tr": "Bu test ve doğrulamayı çok daha güvenli hale getirir. 512A sprint ortasında biterse siz 512B ile ilerlerken kalite güvence hata yeniden denemelerini doğrulayabilir."},
            {"speaker_id": "lucas", "text_en": "Exactly. Furthermore, it prevents an all-or-nothing delivery risk at the sprint boundary.", "text_tr": "Kesinlikle. Dahası sprint sınırında ya hep ya hiç teslimat riskini de önler."},
            {"speaker_id": "gizem", "text_en": "Are there any cross-squad architectural dependencies on the security team for IAM permissions?", "text_tr": "IAM izinleri konusunda güvenlik ekibine herhangi bir ekipler arası mimari bağımlılık var mı?"},
            {"speaker_id": "lucas", "text_en": "I already paired with security yesterday. They provisioned the least-privilege IAM roles in staging, so we are completely unblocked.", "text_tr": "Dün güvenlikle zaten eşleştik. Hazırlık ortamında en az ayrıcalıklı IAM rollerini sağladılar, bu yüzden önümüz tamamen açık."},
        ],
        [
            {
                "question_en": "Why did Lucas initially estimate Ticket 512 as an 8-point story?",
                "question_tr_hint": "Lucas başlangıçta Bilet 512'yi neden 8 puanlık bir hikaye olarak tahmin etti?",
                "correct_answer": "The legacy queue lacked dead-letter queue handling for dropped payloads.",
                "distractors": [
                    "The entire codebase had to be translated into French.",
                    "No engineers on the team knew how to write Python code.",
                    "The cloud provider was shutting down server operations permanently."
                ],
                "explanation_en": "Lucas explains the legacy queue lacked dead-letter handling for dropped payloads.",
                "explanation_tr": "Lucas eski kuyrukta kayıp yükler için dead-letter yönetiminin eksik olduğunu açıklar."
            },
            {
                "question_en": "What is the team's historical sprint velocity?",
                "question_tr_hint": "Ekibin geçmiş sprint hızı ne kadardır?",
                "correct_answer": "Forty points.",
                "distractors": [
                    "One hundred points.",
                    "Ten points.",
                    "Five hundred points."
                ],
                "explanation_en": "Gizem notes an 8-point story consumes 20% of their 40-point sprint velocity.",
                "explanation_tr": "Gizem 8 puanlık hikayenin 40 puanlık hızın %20'sini tükettiğini belirtir."
            },
            {
                "question_en": "How does Lucas propose decomposing the ticket into deliverable slices?",
                "question_tr_hint": "Lucas bileti teslim edilebilir dilimlere nasıl ayırmayı öneriyor?",
                "correct_answer": "Story 512A (3 points) for queue retries, and 512B (5 points) for Lambda migration.",
                "distractors": [
                    "Canceling the story and ignoring cloud migration completely.",
                    "Assigning the entire project to an offshore external agency.",
                    "Postponing all work until next fiscal calendar year."
                ],
                "explanation_en": "Lucas divides it into 512A (3 points) and 512B (5 points).",
                "explanation_tr": "Lucas bunu 512A (3 puan) ve 512B (5 puan) olarak ikiye böler."
            },
            {
                "question_en": "What testing advantage does splitting the story provide according to Gizem?",
                "question_tr_hint": "Gizem'e göre hikayeyi bölmek nasıl bir test avantajı sağlar?",
                "correct_answer": "QA can validate error retries mid-sprint while developers implement the migration.",
                "distractors": [
                    "It eliminates the need for any software testing whatsoever.",
                    "It allows developers to skip writing automated unit tests.",
                    "It forces all testing to be completed by human end users."
                ],
                "explanation_en": "Gizem points out QA can validate retries while coding continues on 512B.",
                "explanation_tr": "Gizem 512B kodlanırken QA'in yeniden denemeleri doğrulayabileceğini belirtir."
            },
            {
                "question_en": "What status does the dependency on the security team hold?",
                "question_tr_hint": "Güvenlik ekibine olan bağımlılığın durumu nedir?",
                "correct_answer": "Completely unblocked; least-privilege IAM roles were provisioned in staging.",
                "distractors": [
                    "Completely blocked; the security team refused all access.",
                    "Waiting three weeks for an executive board approval signature.",
                    "The security lead resigned from the company yesterday."
                ],
                "explanation_en": "Lucas confirms security provisioned the roles in staging, so they are unblocked.",
                "explanation_tr": "Lucas güvenliğin rolleri sağladığını ve önlerinin tamamen açık olduğunu doğrular."
            }
        ],
        ["work-career", "agile", "scrum", "software-engineering", "planning"]
    ),

    # 11. venture-capital-due-diligence (business)
    build_scenario(
        "listening.b2.venture-capital-due-diligence",
        "Defending SaaS Unit Economics During Series A Due Diligence",
        "B2", "negotiation",
        "Startup founder Kaan defends customer acquisition costs, net revenue retention, and gross margins during a rigorous due diligence interview with venture partner Victoria.",
        [
            {"id": "victoria", "name": "Victoria", "role": "Venture Capital Partner", "accent": "American"},
            {"id": "kaan", "name": "Kaan", "role": "FinTech Startup Founder", "accent": "Turkish"},
        ],
        "b2_venture_capital_due_diligence.mp3",
        [
            {"speaker_id": "victoria", "text_en": "Welcome Kaan. Our investment committee was impressed by your four-million-dollar ARR traction. Today I want to dissect your underlying unit economics.", "text_tr": "Hoş geldin Kaan. Yatırım komitemiz dört milyon dolarlık yıllık yinelenen gelir (ARR) ivmenizden etkilendi. Bugün altta yatan birim ekonominizi derinlemesine incelemek istiyorum."},
            {"speaker_id": "kaan", "text_en": "Glad to dive into the data, Victoria. Our metrics reflect capital-efficient, organic enterprise expansion.", "text_tr": "Verilere dalmaktan memnuniyet duyarım Victoria. Metriklerimiz sermaye açısından verimli, organik kurumsal genişlemeyi yansıtıyor."},
            {"speaker_id": "victoria", "text_en": "Looking at your customer acquisition cost, your blended CAC is twelve thousand dollars, but your CAC payback period has widened from eight to fourteen months.", "text_tr": "Müşteri edinme maliyetinize (CAC) baktığımızda harmanlanmış CAC'niz on iki bin dolar ancak CAC geri ödeme süreniz sekiz aydan on dört aya çıktı."},
            {"speaker_id": "kaan", "text_en": "That lengthening payback is intentional. We shifted from selling twenty-seat mid-market tiers to closing six-figure enterprise contracts, which entail ninety-day procurement cycles.", "text_tr": "Bu uzayan geri ödeme süresi kasıtlıdır. Yirmi koltuklu orta ölçekli kademeleri satmaktan doksan günlük satın alma döngüleri içeren altı haneli kurumsal sözleşmeleri kapatmaya geçtik."},
            {"speaker_id": "victoria", "text_en": "Enterprise moves increase enterprise value, provided Net Revenue Retention (NRR) holds up. What is your cohort expansion rate after month twelve?", "text_tr": "Net Gelir Elde Tutma (NRR) oranı korunduğu sürece kurumsal hamleler işletme değerini artırır. On ikinci aydan sonra kohort genişleme oranınız nedir?"},
            {"speaker_id": "kaan", "text_en": "Our enterprise cohort NRR is one hundred and twenty-eight percent. Customers who adopt our core billing API consistently expand into our automated treasury management module.", "text_tr": "Kurumsal kohort NRR'miz yüzde yüz yirmi sekizdir. Temel faturalandırma API'mizi benimseyen müşteriler sürekli olarak otomatik hazine yönetimi modülümüze genişliyor."},
            {"speaker_id": "victoria", "text_en": "A 128 percent NRR is upper-quartile for B2B infrastructure software. What are your current subscription gross margins?", "text_tr": "Yüzde 128'lik bir NRR B2B altyapı yazılımları için üst çeyrektir. Mevcut abonelik brüt karlarınız nedir?"},
            {"speaker_id": "kaan", "text_en": "We operate at seventy-six percent gross margin. As we optimize our multi-tenant cloud database partitioning next quarter, we project reaching eighty-one percent.", "text_tr": "Yüzde yetmiş altı brüt kar marjı ile çalışıyoruz. Gelecek çeyrekte çok kiracılı bulut veritabanı bölümlememizi optimize ettikçe yüzde seksen bire ulaşmayı öngörüyoruz."},
        ],
        [
            {
                "question_en": "What annual recurring revenue (ARR) traction did Kaan's company achieve?",
                "question_tr_hint": "Kaan'ın şirketi ne kadar yıllık yinelenen gelir (ARR) ivmesi elde etti?",
                "correct_answer": "Four million dollars.",
                "distractors": [
                    "One hundred thousand dollars.",
                    "Fifty million dollars.",
                    "Ten billion dollars."
                ],
                "explanation_en": "Victoria mentions their 4-million-dollar ARR traction.",
                "explanation_tr": "Victoria 4 milyon dolarlık ARR ivmelerinden bahseder."
            },
            {
                "question_en": "Why did the customer acquisition cost (CAC) payback period widen to 14 months?",
                "question_tr_hint": "Müşteri edinme maliyeti (CAC) geri ödeme süresi neden 14 aya uzadı?",
                "correct_answer": "The company shifted from mid-market sales to enterprise contracts with longer sales cycles.",
                "distractors": [
                    "The marketing budget was wasted on expensive television commercials.",
                    "Customers refused to pay their monthly invoices on time.",
                    "The company lost fifty percent of its sales team."
                ],
                "explanation_en": "Kaan explains the shift to 6-figure enterprise contracts with 90-day cycles.",
                "explanation_tr": "Kaan 90 günlük döngüleri olan 6 haneli kurumsal sözleşmelere geçişi açıklar."
            },
            {
                "question_en": "What is the company's enterprise Net Revenue Retention (NRR) rate?",
                "question_tr_hint": "Şirketin kurumsal Net Gelir Elde Tutma (NRR) oranı nedir?",
                "correct_answer": "128 percent.",
                "distractors": [
                    "50 percent.",
                    "90 percent.",
                    "200 percent."
                ],
                "explanation_en": "Kaan confirms their enterprise cohort NRR is 128%.",
                "explanation_tr": "Kaan kurumsal kohort NRR'sinin %128 olduğunu onaylar."
            },
            {
                "question_en": "How do existing customers expand their annual spend according to Kaan?",
                "question_tr_hint": "Kaan'a göre mevcut müşteriler yıllık harcamalarını nasıl genişletiyor?",
                "correct_answer": "They expand from the core billing API into the automated treasury module.",
                "distractors": [
                    "They purchase physical corporate merchandise and T-shirts.",
                    "They are forced to pay penalty fees for late renewals.",
                    "They hire the startup's engineers as private consultants."
                ],
                "explanation_en": "Kaan explains customers expand from the billing API into treasury management.",
                "explanation_tr": "Kaan müşterilerin faturalandırmadan hazine yönetimine genişlediğini açıklar."
            },
            {
                "question_en": "What target gross margin does Kaan project following database optimization?",
                "question_tr_hint": "Kaan veritabanı optimizasyonunun ardından hangi hedef brüt kar marjını öngörüyor?",
                "correct_answer": "81 percent.",
                "distractors": [
                    "50 percent.",
                    "76 percent.",
                    "100 percent."
                ],
                "explanation_en": "Kaan projects reaching 81% from their current 76%.",
                "explanation_tr": "Kaan mevcut %76'dan %81'e ulaşmayı öngördüklerini belirtir."
            }
        ],
        ["business", "venture-capital", "finance", "saas", "startups"]
    ),

    # 12. cloud-architecture-refactor (technology)
    build_scenario(
        "listening.b2.cloud-architecture-refactor",
        "Architecting a Container Migration from Monolithic EC2",
        "B2", "engineering_meeting",
        "Lead cloud architect Melis and systems engineer Patrick evaluate refactoring their monolithic virtual machine infrastructure into containerized microservices managed on AWS EKS.",
        [
            {"id": "patrick", "name": "Patrick", "role": "Senior Cloud Infrastructure Engineer", "accent": "British"},
            {"id": "melis", "name": "Melis", "role": "Lead Cloud Architect", "accent": "Turkish"},
        ],
        "b2_cloud_architecture_refactor.mp3",
        [
            {"speaker_id": "patrick", "text_en": "Morning Melis. I completed our load test analysis on the legacy monolithic EC2 cluster. During peak traffic, autoscaling spin-up takes nearly eight minutes.", "text_tr": "Günaydın Melis. Eski monolitik EC2 kümesindeki yük testi analizimizi tamamladım. Yoğun trafik sırasında otomatik ölçeklendirme başlatması neredeyse sekiz dakika sürüyor."},
            {"speaker_id": "melis", "text_en": "Eight minutes is unacceptable when sudden marketing traffic surges occur. Cold-booting complete virtual machines with full operating systems is far too heavy.", "text_tr": "Ani pazarlama trafiği dalgalanmaları meydana geldiğinde sekiz dakika kabul edilemez. Eksiksiz işletim sistemlerine sahip sanal makineleri sıfırdan başlatmak çok ağır."},
            {"speaker_id": "patrick", "text_en": "Exactly. If we decompose the monolith into lightweight Docker containers orchestrated on managed Kubernetes via EKS, container pod spin-up takes less than twelve seconds.", "text_tr": "Kesinlikle. Monoliti EKS üzerinden yönetilen Kubernetes üzerinde düzenlenen hafif Docker konteynerlerine ayırırsak, konteyner pod başlatması on iki saniyeden az sürer."},
            {"speaker_id": "melis", "text_en": "That represents a massive improvement in autoscaling elasticity. How do we manage stateful database connections across hundreds of ephemeral container pods?", "text_tr": "Bu otomatik ölçeklendirme esnekliğinde muazzam bir gelişmeyi temsil eder. Yüzlerce geçici konteyner pod'u genelinde durum bilgisi içeren veritabanı bağlantılarını nasıl yöneteceğiz?"},
            {"speaker_id": "patrick", "text_en": "We should deploy AWS RDS Proxy between the Kubernetes pods and our Aurora database clusters. It pools and multiplexes connections, insulating the database from connection storms.", "text_tr": "Kubernetes pod'ları ile Aurora veritabanı kümelerimiz arasına AWS RDS Proxy konuşlandırmalıyız. Bağlantıları havuzlayıp çoğullayarak veritabanını bağlantı fırtınalarından yalıtır."},
            {"speaker_id": "melis", "text_en": "What about observability across distributed container microservices?", "text_tr": "Peki dağıtık konteyner mikroservisleri genelinde gözlemlenebilirlik ne olacak?"},
            {"speaker_id": "patrick", "text_en": "We will instrument OpenTelemetry sidecar containers to collect distributed traces, exporting telemetry data into our central Grafana and Jaeger dashboards.", "text_tr": "Dağıtık izleri toplamak için OpenTelemetry sidecar konteynerleri entegre edeceğiz ve telemetri verilerini merkezi Grafana ve Jaeger panolarımıza aktaracağız."},
            {"speaker_id": "melis", "text_en": "Outstanding architecture Patrick. Let's draft the migration proof-of-concept for the authentication service first.", "text_tr": "Mükemmel mimari Patrick. Önce kimlik doğrulama servisi için geçiş konsept kanıtı taslağını hazırlayalım."},
        ],
        [
            {
                "question_en": "What primary operational limitation does the monolithic EC2 cluster exhibit?",
                "question_tr_hint": "Monolitik EC2 kümesi hangi temel operasyonel sınırlamayı sergiliyor?",
                "correct_answer": "Autoscaling cold-booting of virtual machines takes nearly eight minutes during traffic surges.",
                "distractors": [
                    "The virtual machines lose all stored data every midnight.",
                    "The cloud provider charges one million dollars per hour.",
                    "The servers can only run on weekends."
                ],
                "explanation_en": "Patrick explains autoscaling spin-up takes nearly eight minutes during peak traffic.",
                "explanation_tr": "Patrick yoğun trafikte otomatik ölçeklendirmenin neredeyse 8 dakika sürdüğünü açıklar."
            },
            {
                "question_en": "How fast does container pod spin-up take on managed Kubernetes (EKS)?",
                "question_tr_hint": "Yönetilen Kubernetes (EKS) üzerinde konteyner pod başlatması ne kadar sürer?",
                "correct_answer": "Less than twelve seconds.",
                "distractors": [
                    "One full hour.",
                    "Five minutes.",
                    "Three business days."
                ],
                "explanation_en": "Patrick notes container pod spin-up takes less than twelve seconds.",
                "explanation_tr": "Patrick konteyner başlatmasının 12 saniyeden az sürdüğünü belirtir."
            },
            {
                "question_en": "What component will insulate the database from connection exhaustion storms?",
                "question_tr_hint": "Veritabanını bağlantı tükenmesi fırtınalarından hangi bileşen yalıtacak?",
                "correct_answer": "AWS RDS Proxy pooling and multiplexing database connections.",
                "distractors": [
                    "A physical analog circuit breaker in the server rack.",
                    "A manual spreadsheet updated by on-call engineers.",
                    "Disconnecting the database from the internet completely."
                ],
                "explanation_en": "Patrick suggests deploying AWS RDS Proxy to pool and multiplex connections.",
                "explanation_tr": "Patrick bağlantıları havuzlamak için AWS RDS Proxy konuşlandırmayı önerir."
            },
            {
                "question_en": "How will the engineering team achieve distributed observability across microservices?",
                "question_tr_hint": "Mühendislik ekibi mikroservisler genelinde dağıtık gözlemlenebilirliği nasıl sağlayacak?",
                "correct_answer": "OpenTelemetry sidecar containers exporting traces to Grafana and Jaeger.",
                "distractors": [
                    "Printing log files onto physical rolls of thermal paper.",
                    "Asking users to report software bugs via postal letters.",
                    "Recording audio voice memos during code deployment."
                ],
                "explanation_en": "Patrick explains OpenTelemetry sidecars will export traces to Grafana and Jaeger.",
                "explanation_tr": "Patrick OpenTelemetry'nin izleri Grafana ve Jaeger'a aktaracağını açıklar."
            },
            {
                "question_en": "Which service will serve as the initial migration proof-of-concept?",
                "question_tr_hint": "Hangi servis ilk geçiş konsept kanıtı olarak hizmet edecek?",
                "correct_answer": "The authentication service.",
                "distractors": [
                    "The entire global payment billing gateway.",
                    "The legacy database storage engine.",
                    "The corporate marketing public blog."
                ],
                "explanation_en": "Melis suggests drafting the proof-of-concept for the authentication service first.",
                "explanation_tr": "Melis konsept kanıtı için önce kimlik doğrulama servisini taslaklaştırmayı önerir."
            }
        ],
        ["technology", "cloud", "kubernetes", "aws", "architecture"]
    )
]
