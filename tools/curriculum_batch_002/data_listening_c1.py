#!/usr/bin/env python3
"""
Listening Batch 002: C1 Scenarios (13 scenarios).
"""

from listening_builder import build_scenario

SCENARIOS_C1 = [
    # 1. smart-home-iot-integration (technology / daily-life)
    build_scenario(
        "listening.c1.smart-home-iot-integration",
        "Architecting Heterogeneous Smart Home IoT Standards",
        "C1", "product_discovery",
        "Selin, an IoT systems architect, consults with Marcus, a firmware engineering lead, about unifying disparate Zigbee, Thread, and Matter protocols across consumer hardware ecosystems.",
        [
            {"id": "marcus", "name": "Marcus", "role": "Lead Embedded Firmware Engineer", "accent": "American"},
            {"id": "selin", "name": "Selin", "role": "Principal IoT Architect", "accent": "Turkish"},
        ],
        "c1_smart_home_iot_integration.mp3",
        [
            {"speaker_id": "marcus", "text_en": "Selin, looking at our upcoming appliance firmware roadmap, our dual-stack Zigbee and Bluetooth architecture is struggling to maintain low latency when bridging to Matter over Thread.", "text_tr": "Selin, yaklaşan cihaz bellenimi yol haritamıza baktığımızda, ikili Zigbee ve Bluetooth mimarimiz Matter over Thread köprüsüne geçerken düşük gecikmeyi korumakta zorlanıyor."},
            {"speaker_id": "selin", "text_en": "I anticipated this bottleneck. Matter relies heavily on IPv6 routing throughout the mesh, whereas our legacy Zigbee clusters depend on application-layer gateway translation.", "text_tr": "Bu darboğazı öngörmüştüm. Matter, mesh ağı boyunca büyük ölçüde IPv6 yönlendirmesine dayanır, oysa eski Zigbee kümelerimiz uygulama katmanı ağ geçidi çevirisine bağımlıdır."},
            {"speaker_id": "marcus", "text_en": "Exactly. When the gateway translates Zigbee cluster libraries into Matter data models, we observe intermittent packet drops and round-trip times exceeding six hundred milliseconds.", "text_tr": "Kesinlikle. Ağ geçidi Zigbee küme kütüphanelerini Matter veri modellerine dönüştürürken, kesintili paket kayıpları ve altı yüz milisaniyeyi aşan gidiş-dönüş süreleri gözlemliyoruz."},
            {"speaker_id": "selin", "text_en": "We should deprecate direct proprietary bridging. Instead, we can implement an OpenThread Border Router daemon directly on our primary hub chipset.", "text_tr": "Doğrudan tescilli köprülemeyi kullanımdan kaldırmalıyız. Bunun yerine, ana hub yonga setimiz üzerinde doğrudan bir OpenThread Border Router arka plan programı uygulamalıyız."},
            {"speaker_id": "marcus", "text_en": "That would allow Thread-enabled endpoint devices to establish end-to-end encrypted IP sessions without packet payload re-encoding at the gateway boundary.", "text_tr": "Bu, Thread özellikli uç cihazların ağ geçidi sınırında paket yükü yeniden kodlamasına gerek kalmadan uçtan uca şifrelenmiş IP oturumları kurmasına olanak tanır."},
            {"speaker_id": "selin", "text_en": "Precisely. Furthermore, by offloading Matter commissioning to standard Bluetooth Low Energy advertising, initial device onboarding friction drops significantly for non-technical consumers.", "text_tr": "Tam olarak öyle. Dahası, Matter devreye alımını standart Bluetooth Düşük Enerji duyurularına devrederek, teknik olmayan tüketiciler için ilk cihaz kurulum sürtünmesi önemli ölçüde azalır."},
            {"speaker_id": "marcus", "text_en": "What about backward compatibility for the half-million legacy Zigbee units currently in customer households?", "text_tr": "Peki müşterilerin evlerinde bulunan yarım milyon eski Zigbee ünitesinin geriye dönük uyumluluğu ne olacak?"},
            {"speaker_id": "selin", "text_en": "We will containerize the legacy translation service in isolated flash memory, dedicating dedicated hardware crypto-acceleration solely to modern IPv6 traffic.", "text_tr": "Eski çeviri hizmetini yalıtılmış flaş bellekte konteynerleştireceğiz ve özel donanım kripto hızlandırmasını yalnızca modern IPv6 trafiğine tahsis edeceğiz."},
        ],
        [
            {
                "question_en": "What architectural divergence causes latency between Zigbee and Matter?",
                "question_tr_hint": "Zigbee ve Matter arasındaki gecikmeye hangi mimari farklılık neden oluyor?",
                "correct_answer": "Matter operates natively over IPv6 mesh routing, whereas legacy Zigbee relies on application-layer gateway translation.",
                "distractors": [
                    "Zigbee requires high-voltage power lines while Matter runs on solar energy.",
                    "Matter disables all encryption protocols to prioritize raw transmission speed.",
                    "Zigbee lacks radio transmitters capable of broadcasting electromagnetic waves."
                ],
                "explanation_en": "Selin explains that Matter utilizes IPv6 mesh routing while Zigbee requires gateway translation.",
                "explanation_tr": "Selin, Matter'ın IPv6 yönlendirmesi kullandığını, Zigbee'nin ise ağ geçidi çevirisi gerektirdiğini belirtir."
            },
            {
                "question_en": "What specific component does Selin propose implementing on the main hub?",
                "question_tr_hint": "Selin ana hub üzerinde hangi özel bileşenin uygulanmasını öneriyor?",
                "correct_answer": "An OpenThread Border Router daemon running directly on the primary hub chipset.",
                "distractors": [
                    "An external analog modem connected via dial-up phone lines.",
                    "A manual physical toggle switch for alternating between radios.",
                    "A liquid cooling system for battery thermal regulation."
                ],
                "explanation_en": "Selin suggests embedding an OpenThread Border Router daemon on the chipset.",
                "explanation_tr": "Selin, yonga seti üzerinde bir OpenThread Border Router arka plan programı çalıştırmayı önerir."
            },
            {
                "question_en": "How does offloading onboarding to Bluetooth Low Energy improve consumer experience?",
                "question_tr_hint": "Kurulumun BLE'ye aktarılması tüketici deneyimini nasıl iyileştirir?",
                "correct_answer": "It minimizes device onboarding friction during initial setup for non-technical users.",
                "distractors": [
                    "It eliminates the need for household electricity during pairing.",
                    "It guarantees infinite battery life across all home sensors.",
                    "It automatically registers the device with international patent authorities."
                ],
                "explanation_en": "Selin notes that using BLE advertising lowers onboarding friction for regular consumers.",
                "explanation_tr": "Selin, BLE kullanımının teknik olmayan kullanıcılar için kurulum sürtünmesini azalttığını açıklar."
            },
            {
                "question_en": "How will legacy Zigbee devices maintain support under the revised firmware plan?",
                "question_tr_hint": "Eski Zigbee cihazları revize edilen bellenim planı altında nasıl desteklenmeye devam edecek?",
                "correct_answer": "Their translation service will be containerized in isolated flash memory alongside dedicated IPv6 crypto-acceleration.",
                "distractors": [
                    "Customers will be forced to discard all existing hardware immediately.",
                    "Legacy units will communicate strictly using audible ultrasonic frequencies.",
                    "A postal recall program will replace every installed circuit board."
                ],
                "explanation_en": "Selin states the legacy translation service will be containerized in isolated flash memory.",
                "explanation_tr": "Selin, eski çeviri servisinin yalıtılmış bellekte konteynerleştirileceğini belirtir."
            },
            {
                "question_en": "What symptom did Marcus observe during payload conversion at the gateway boundary?",
                "question_tr_hint": "Marcus ağ geçidi sınırında veri yükü dönüşümü sırasında hangi belirtiyi gözlemledi?",
                "correct_answer": "Intermittent packet loss and round-trip times surpassing six hundred milliseconds.",
                "distractors": [
                    "Spontaneous hardware combustion in central processing units.",
                    "Permanent loss of internet connectivity across the entire municipality.",
                    "Inversion of screen display colors on domestic mobile devices."
                ],
                "explanation_en": "Marcus noted packet drops and latency exceeding 600 ms during conversion.",
                "explanation_tr": "Marcus paket kayıpları ve 600 ms'yi aşan gecikmeler gözlemlediğini aktarır."
            }
        ],
        ["technology", "daily_life"]
    ),

    # 2. airline-flight-cancellation-rebooking (travel / customer service)
    build_scenario(
        "listening.c1.airline-flight-cancellation-rebooking",
        "Managing Cascading Transatlantic Flight Disruptions",
        "C1", "incident_response",
        "Mert, an international traveler stranded in London due to airspace restrictions, negotiates complex interline rerouting, visa waivers, and duty-of-care vouchers with Claire, a premium service duty manager.",
        [
            {"id": "claire", "name": "Claire", "role": "Premium Airport Duty Manager", "accent": "British"},
            {"id": "mert", "name": "Mert", "role": "Enterprise Management Consultant", "accent": "Turkish"},
        ],
        "c1_airline_flight_cancellation_rebooking.mp3",
        [
            {"speaker_id": "claire", "text_en": "Good afternoon, Mr. Demir. As you are aware, severe convective weather across the North Atlantic corridor has led to widespread ground stops and cancellations.", "text_tr": "İyi günler Sayın Demir. Bildiğiniz gibi, Kuzey Atlantik koridoru boyunca etkili olan şiddetli konvektif hava koşulları yaygın yer durdurmalarına ve iptallere yol açtı."},
            {"speaker_id": "mert", "text_en": "I understand the meteorological constraints, Claire, but I have a high-stakes board presentation in Chicago tomorrow at nine in the morning local time.", "text_tr": "Meteorolojik kısıtlamaları anlıyorum Claire, ancak yarın yerel saatle sabah dokuzda Chicago'da çok kritik bir yönetim kurulu sunumum var."},
            {"speaker_id": "claire", "text_en": "Under our standard carrier policies, the next available direct seat on our metal departs tomorrow evening, which clearly will not fulfill your operational schedule.", "text_tr": "Standart şirket politikalarımız gereği, kendi uçaklarımızdaki bir sonraki müsait direkt koltuk yarın akşam kalkıyor; bu da çalışma programınıza açıkça uymuyor."},
            {"speaker_id": "mert", "text_en": "Could you invoke an involuntary interline endorsement to route me through Dublin or Frankfurt on a Star Alliance partner carrier tonight?", "text_tr": "Bu akşam bir Star Alliance ortak taşıyıcısıyla beni Dublin veya Frankfurt üzerinden yönlendirmek için zorunlu hatlar arası ciro kuralını işletebilir misiniz?"},
            {"speaker_id": "claire", "text_en": "Because you are traveling on a full-fare business itinerary and hold elite tier status, I can authorize an involuntary reroute on Lufthansa via Frankfurt departing in two hours.", "text_tr": "Tam ücretli business seyahat ettiğiniz ve elit statüye sahip olduğunuz için, iki saat sonra kalkan Frankfurt aktarmalı Lufthansa uçuşuna zorunlu yeniden rota izni verebilirim."},
            {"speaker_id": "mert", "text_en": "Does that transfer my checked luggage with biometric expedited handling, and will transit through Frankfurt necessitate a Schengen transit visa for a Turkish passport with a valid US B1 visa?", "text_tr": "Bu işlem biyometrik hızlandırılmış bagajımı da aktarır mı ve geçerli ABD B1 vizesi olan Türk pasaportu için Frankfurt transit geçişi Schengen transit vizesi gerektirir mi?"},
            {"speaker_id": "claire", "text_en": "Holding a valid United States visa exempts you from airport transit visa requirements in Frankfurt, provided you remain within the international transit concourse. Luggage transfers automatically.", "text_tr": "Geçerli bir Amerika Birleşik Devletleri vizesine sahip olmak, uluslararası transit salonunda kalmanız şartıyla sizi Frankfurt'ta havalimanı transit vizesi şartından muaf tutar. Bagajınız otomatik aktarılacaktır."},
            {"speaker_id": "mert", "text_en": "That is a tremendous relief. Please reissue the electronic boarding passes and print the interline baggage tags immediately.", "text_tr": "Bu muazzam bir rahatlama oldu. Lütfen elektronik biniş kartlarını yeniden düzenleyin ve hatlar arası bagaj etiketlerini hemen yazdırın."},
        ],
        [
            {
                "question_en": "Why is the direct flight offered under standard carrier policy unacceptable to Mert?",
                "question_tr_hint": "Standart şirket politikası kapsamında sunulan direkt uçuş Mert için neden kabul edilemez?",
                "correct_answer": "It departs tomorrow evening, causing him to miss a critical morning board presentation.",
                "distractors": [
                    "It charges an exorbitant additional baggage surcharge.",
                    "It requires him to travel in an unpressurized cargo hold.",
                    "It reroutes through three unauthorized international jurisdictions."
                ],
                "explanation_en": "Mert explains he has a crucial board presentation tomorrow at 9:00 AM, so tomorrow evening is too late.",
                "explanation_tr": "Mert yarın sabah 9'da sunumu olduğunu, bu yüzden yarın akşamki uçuşun çok geç olduğunu belirtir."
            },
            {
                "question_en": "What enables Claire to authorize an involuntary interline endorsement on a partner carrier?",
                "question_tr_hint": "Claire'in ortak bir havayolunda zorunlu hatlar arası bilet onaylamasını sağlayan nedir?",
                "correct_answer": "Mert's full-fare business itinerary combined with his elite frequent-flyer status.",
                "distractors": [
                    "A written presidential emergency decree signed by civil aviation authorities.",
                    "The complete insolvency and bankruptcy of the original operating airline.",
                    "A mandatory random lottery held at the customer service counter."
                ],
                "explanation_en": "Claire points out his full-fare business ticket and elite tier status allow the exception.",
                "explanation_tr": "Claire, tam ücretli business bileti ve elit statüsünün bu istisnaya izin verdiğini söyler."
            },
            {
                "question_en": "Why is Mert exempt from obtaining a Schengen transit visa during his layover in Frankfurt?",
                "question_tr_hint": "Mert Frankfurt'taki aktarma sırasında neden Schengen transit vizesi almaktan muaftır?",
                "correct_answer": "His valid United States visa waives the transit visa rule while staying in the international concourse.",
                "distractors": [
                    "All international visa treaties were permanently abolished across Europe.",
                    "He is traveling as an accredited ambassador of the United Nations.",
                    "Lufthansa operates a private subterranean tunnel outside legal territory."
                ],
                "explanation_en": "Claire clarifies that holding a valid US visa grants an airport transit visa exemption.",
                "explanation_tr": "Claire, geçerli bir ABD vizesine sahip olmanın uluslararası transit bölgesinde muafiyet sağladığını açıklar."
            },
            {
                "question_en": "What route does Claire arrange to accommodate Mert's urgent timeline?",
                "question_tr_hint": "Claire Mert'in acil takvimine uyum sağlamak için hangi rotayı düzenler?",
                "correct_answer": "A Lufthansa flight routing through Frankfurt departing in two hours.",
                "distractors": [
                    "A direct supersonic charter flight landing directly in downtown Chicago.",
                    "A thirty-hour transoceanic maritime ferry across the Atlantic.",
                    "A flight connecting through Tokyo and Vancouver arriving two days later."
                ],
                "explanation_en": "Claire arranges an involuntary reroute on Lufthansa via Frankfurt departing in two hours.",
                "explanation_tr": "Claire, iki saat içinde kalkan Frankfurt aktarmalı bir Lufthansa uçuşu ayarlar."
            },
            {
                "question_en": "What handling will happen to Mert's checked luggage during the Frankfurt connection?",
                "question_tr_hint": "Frankfurt aktarması sırasında Mert'in kayıtlı bagajına ne işlem yapılacaktır?",
                "correct_answer": "It will be automatically transferred across carriers to his final destination.",
                "distractors": [
                    "Mert must collect and re-check it manually during his brief connection.",
                    "It will remain in London until the weather clears three days later.",
                    "It must be discarded due to international hazardous material regulations."
                ],
                "explanation_en": "Claire assures Mert that luggage transfers automatically.",
                "explanation_tr": "Claire, bagajın otomatik olarak aktarılacağı güvencesini verir."
            }
        ],
        ["travel", "customer_service"]
    ),

    # 3. functional-medicine-longevity-consult (health-lifestyle)
    build_scenario(
        "listening.c1.functional-medicine-longevity-consult",
        "Consulting on Integrative Biomarker Diagnostics and Longevity",
        "C1", "product_discovery",
        "Dr. Bennett, a functional medicine specialist, interprets comprehensive metabolomic, lipid particle, and epigenetic methylation profiling for Ayla, tailoring an evidence-based preventative intervention.",
        [
            {"id": "dr_bennett", "name": "Dr. Bennett", "role": "Functional Medicine Physician", "accent": "American"},
            {"id": "ayla", "name": "Ayla", "role": "Healthcare Executive", "accent": "Turkish"},
        ],
        "c1_functional_medicine_longevity_consult.mp3",
        [
            {"speaker_id": "dr_bennett", "text_en": "Welcome back, Ayla. We have gathered your advanced biomarker panel, including your ApoB particle count, fasting insulin sensitivity, and DNA methylation biological age score.", "text_tr": "Tekrar hoş geldiniz Ayla Hanım. ApoB parçacık sayınız, açlık insülin duyarlılığınız ve DNA metilasyon biyolojik yaş skorunuz dahil olmak üzere gelişmiş biyobelirteç panelinizi topladık."},
            {"speaker_id": "ayla", "text_en": "I was anxious to review the results, Dr. Bennett. My routine physical showed normal total cholesterol, but I suspect that aggregated metric masks underlying vascular risk.", "text_tr": "Sonuçları incelemek için sabırsızlanıyordum Dr. Bennett. Rutin tahlillerim toplam kolesterolü normal gösterdi ancak bu birleştirilmiş metriğin altta yatan damar riskini maskelediğinden şüpheleniyorum."},
            {"speaker_id": "dr_bennett", "text_en": "Your intuition is spot on. While standard LDL-C appears benign at ninety-five milligrams per deciliter, your Apolipoprotein B is elevated at one hundred and fifteen.", "text_tr": "Sezgileriniz tam olarak doğru. Standart LDL-C desilitrede doksan beş miligram ile zararsız görünse de, Apolipoprotein B değeriniz yüz on beşte yüksek seyrediyor."},
            {"speaker_id": "ayla", "text_en": "Which indicates an atherogenic particle concentration that could drive plaque accumulation despite normal volumetric lipid concentrations.", "text_tr": "Bu da normal hacimsel lipid konsantrasyonlarına rağmen plak birikimini tetikleyebilecek aterojenik bir parçacık yoğunluğuna işaret ediyor."},
            {"speaker_id": "dr_bennett", "text_en": "Precisely. Furthermore, your Horvath epigenetic clock estimates your biological age at forty-one, approximately three years older than your chronological age.", "text_tr": "Kesinlikle. Dahası, Horvath epigenetik saatiniz biyolojik yaşınızı kırk bir olarak tahmin ediyor, bu da takvim yaşınızdan yaklaşık üç yıl daha yaşlı."},
            {"speaker_id": "ayla", "text_en": "I assume that delta stems from chronic sleep deprivation and elevated cortisol from international corporate restructuring.", "text_tr": "Bu farkın uluslararası kurumsal yeniden yapılanmadan kaynaklanan kronik uykusuzluk ve yüksek kortizolden kaynaklandığını tahmin ediyorum."},
            {"speaker_id": "dr_bennett", "text_en": "Without question. Rather than prescribing statins immediately, I recommend a multifaceted protocol: targeted high-intensity interval training, zone-two mitochondrial conditioning, and time-restricted feeding.", "text_tr": "Şüphesiz. Hemen statin reçete etmek yerine çok yönlü bir protokol öneriyorum: hedefe yönelik yüksek yoğunluklu interval antrenmanı, ikinci bölge mitokondriyal kondisyon ve zaman kısıtlamalı beslenme."},
            {"speaker_id": "ayla", "text_en": "I will incorporate the zone-two aerobic sessions immediately and track my continuous glucose monitor metrics to evaluate glycemic variance.", "text_tr": "İkinci bölge aerobik seanslarını hemen dahil edeceğim ve glisemik değişkenliği değerlendirmek için sürekli glikoz monitörü metriklerimi takip edeceğim."},
        ],
        [
            {
                "question_en": "Why does Dr. Bennett express concern despite Ayla's normal total cholesterol?",
                "question_tr_hint": "Ayla'nın toplam kolesterolü normal olmasına rağmen Dr. Bennett neden endişe duyuyor?",
                "correct_answer": "Her elevated Apolipoprotein B indicates a high concentration of atherogenic plaque-forming particles.",
                "distractors": [
                    "Her red blood cell count has dropped below critical transfusion thresholds.",
                    "She has contracted an acute viral infection affecting respiratory membranes.",
                    "Her body temperature has permanently dropped below hypothermic levels."
                ],
                "explanation_en": "Dr. Bennett notes her ApoB is elevated, revealing atherogenic risk that standard cholesterol hides.",
                "explanation_tr": "Dr. Bennett, yüksek ApoB'nin standart kolesterolün gizlediği aterojenik riski ortaya koyduğunu belirtir."
            },
            {
                "question_en": "What did the epigenetic methylation clock reveal about Ayla's physiology?",
                "question_tr_hint": "Epigenetik metilasyon saati Ayla'nın fizyolojisi hakkında neyi ortaya koydu?",
                "correct_answer": "Her biological age is approximately three years older than her chronological age.",
                "distractors": [
                    "She possesses genetic immunity to all forms of cellular aging.",
                    "Her biological cellular age is equivalent to that of an infant.",
                    "Her genetic markers indicate acute hereditary muscle atrophy."
                ],
                "explanation_en": "The Horvath epigenetic clock estimated her biological age at 41, three years older than her chronological age.",
                "explanation_tr": "Epigenetik saat biyolojik yaşının takvim yaşından 3 yaş büyük olduğunu göstermiştir."
            },
            {
                "question_en": "What primary lifestyle factor does Ayla attribute her biological age acceleration to?",
                "question_tr_hint": "Ayla biyolojik yaşının hızlanmasını hangi temel yaşam tarzı faktörüne bağlıyor?",
                "correct_answer": "Chronic sleep deprivation and sustained cortisol elevation related to professional stress.",
                "distractors": [
                    "Extensive consumption of organic leafy green vegetables.",
                    "Frequent participation in Olympic-distance marathons.",
                    "Living at extremely high altitudes without supplemental oxygen."
                ],
                "explanation_en": "Ayla links the delta to chronic sleep deprivation and elevated cortisol from corporate stress.",
                "explanation_tr": "Ayla bu farkı kurumsal strese bağlı kronik uykusuzluk ve yüksek kortizole bağlar."
            },
            {
                "question_en": "What intervention does Dr. Bennett favor prior to considering pharmacological statins?",
                "question_tr_hint": "Dr. Bennett farmakolojik statinleri düşünmeden önce hangi müdahaleyi tercih ediyor?",
                "correct_answer": "Zone-two mitochondrial conditioning, targeted intervals, and time-restricted nutrition.",
                "distractors": [
                    "Immediate invasive coronary bypass surgical intervention.",
                    "A strictly liquid diet consisting exclusively of citrus juice.",
                    "Complete bed rest and physical immobilization for six months."
                ],
                "explanation_en": "Dr. Bennett recommends zone-two conditioning, HIIT, and time-restricted feeding.",
                "explanation_tr": "Dr. Bennett bölge 2 kondisyonu, HIIT ve zaman kısıtlamalı beslenmeyi önerir."
            },
            {
                "question_en": "How does Ayla plan to objectively monitor her metabolic response to the new regimen?",
                "question_tr_hint": "Ayla yeni düzene verdiği metabolik tepkiyi nesnel olarak nasıl izlemeyi planlıyor?",
                "correct_answer": "By analyzing continuous glucose monitor data to observe glycemic fluctuations.",
                "distractors": [
                    "By measuring her body weight six times throughout each afternoon.",
                    "By counting the exact number of steps taken in her office hallway.",
                    "By recording daily voice memos regarding subjective fatigue."
                ],
                "explanation_en": "Ayla intends to track continuous glucose monitor metrics to evaluate glycemic variance.",
                "explanation_tr": "Ayla glisemik değişkenliği izlemek için sürekli glikoz monitörü verilerini takip etmeyi planlar."
            }
        ],
        ["health_lifestyle", "consulting"]
    ),

    # 4. farm-to-table-procurement (food-shopping / supply chain)
    build_scenario(
        "listening.c1.farm-to-table-procurement",
        "Negotiating Regenerative Agricultural Procurement Contracts",
        "C1", "negotiation",
        "Chef Julien and regional co-op director Elif negotiate an annual supply agreement for biodynamic heirloom produce, addressing harvest yield volatility, cold-chain compliance, and price hedging.",
        [
            {"id": "julien", "name": "Julien", "role": "Executive Restaurateur", "accent": "French"},
            {"id": "elif", "name": "Elif", "role": "Agricultural Cooperative Director", "accent": "Turkish"},
        ],
        "c1_farm_to_table_procurement.mp3",
        [
            {"speaker_id": "julien", "text_en": "Elif, our culinary reputation depends on uncompromising ingredient integrity. We want your cooperative to become our exclusive supplier for heritage brassicas and heirloom nightshades.", "text_tr": "Elif, mutfak itibarımız tavizsiz malzeme bütünlüğüne dayanıyor. Kooperatifinizin ata tohumu lahanagiller ve yadigâr patlıcangiller konusunda tek tedarikçimiz olmasını istiyoruz."},
            {"speaker_id": "elif", "text_en": "We appreciate your patronage, Julien. However, farming under biodynamic regenerative standards leaves us vulnerable to unseasonal microclimate shifts and late frosts.", "text_tr": "İlginiz için teşekkür ederiz Julien. Ancak biyodinamik onarıcı standartlar altında çiftçilik yapmak bizi mevsimsiz mikroklima değişimlerine ve geç donlara karşı savunmasız bırakıyor."},
            {"speaker_id": "julien", "text_en": "We cannot operate a Michelin-starred dining room if half our vegetable menu disappears whenever a frost impacts the Aegean foothills.", "text_tr": "Ege eteklerini bir don vurduğunda sebze menümüzün yarısı ortadan kaybolursa Michelin yıldızlı bir restoranı işletemeyiz."},
            {"speaker_id": "elif", "text_en": "We propose a dynamic quota collar. If extreme weather impairs yield by more than thirty percent, our agreement permits substituting secondary heirloom cultivars that share identical organoleptic profiles.", "text_tr": "Dinamik bir kota aralığı öneriyoruz. Aşırı hava koşulları verimi yüzde otuzdan fazla düşürürse, anlaşmamız aynı organoleptik profili paylaşan ikincil ata tohumu çeşitlerinin ikame edilmesine izin verir."},
            {"speaker_id": "julien", "text_en": "That flexibility is acceptable provided substitution notices arrive at least forty-eight hours before daily prep cycles, allowing our sous chefs to adjust our tasting pairings.", "text_tr": "Sous şeflerimizin tadım eşleştirmelerini ayarlamasına olanak sağlamak için ikame bildirimleri günlük hazırlık döngülerinden en az kırk sekiz saat önce gelirse bu esneklik kabul edilebilir."},
            {"speaker_id": "elif", "text_en": "Agreed. In return, we require a guaranteed price floor with a twenty percent upfront retainer to finance our cover-crop seeding and mycorrhizal soil inoculants.", "text_tr": "Anlaştık. Karşılığında, örtü bitkisi ekimimizi ve mikorizal toprak aşılarımızı finanse etmek için garantili bir taban fiyat ve yüzde yirmi peşin ön ödeme talep ediyoruz."},
            {"speaker_id": "julien", "text_en": "What cold-chain telemetry guarantees do you provide to prevent enzymatic degradation during transport across the Marmara straits?", "text_tr": "Marmara boğazı üzerinden sevkiyat sırasında enzimatik bozulmayı önlemek için hangi soğuk zincir telemetri garantilerini sağlıyorsunuz?"},
            {"speaker_id": "elif", "text_en": "Every delivery crate carries IoT temperature loggers transmitting real-time readings between three and five degrees Celsius directly to your kitchen tablet.", "text_tr": "Her teslimat kasasında, doğrudan mutfak tabletinize üç ila beş santigrat derece arasında gerçek zamanlı okumalar ileten IoT sıcaklık kayıt cihazları bulunur."},
        ],
        [
            {
                "question_en": "What ecological challenge creates supply unpredictability for Elif's cooperative?",
                "question_tr_hint": "Elif'in kooperatifi için hangi ekolojik zorluk tedarik öngörülemezliği yaratıyor?",
                "correct_answer": "Vulnerability to unseasonal microclimate fluctuations and late frost events under regenerative practices.",
                "distractors": [
                    "Catastrophic insect swarms destroying all greenhouse glass structures.",
                    "Contamination of regional water tables with synthetic industrial solvents.",
                    "A total labor embargo enacted by neighboring municipal authorities."
                ],
                "explanation_en": "Elif explains that biodynamic farming is vulnerable to microclimate shifts and late frosts.",
                "explanation_tr": "Elif, onarıcı tarımın mikroklima değişimlerine ve geç donlara duyarlı olduğunu belirtir."
            },
            {
                "question_en": "How does the proposed quota collar handle extreme agricultural yield losses?",
                "question_tr_hint": "Önerilen kota aralığı aşırı tarımsal verim kayıplarını nasıl yönetir?",
                "correct_answer": "It permits substituting secondary heirloom cultivars with matching flavor and culinary characteristics.",
                "distractors": [
                    "It automatically forces the restaurant to close until the next seasonal harvest.",
                    "It imports cheap frozen vegetables from commercial overseas distributors.",
                    "It cancels all contractual liabilities without any financial compensation."
                ],
                "explanation_en": "Elif proposes substituting secondary cultivars with identical organoleptic profiles if yields drop >30%.",
                "explanation_tr": "Elif, verim %30'dan fazla düşerse eşdeğer tat profiline sahip alternatif çeşitlerin ikamesini önerir."
            },
            {
                "question_en": "What operational stipulation does Chef Julien attach to vegetable substitutions?",
                "question_tr_hint": "Şef Julien sebze ikamelerine hangi operasyonel koşulu ekliyor?",
                "correct_answer": "Substitutions must be formally notified at least forty-eight hours ahead of kitchen prep.",
                "distractors": [
                    "The cooperative must supply gold-plated packaging for all substitute produce.",
                    "Substitutions must be personally inspected by regional agricultural ministers.",
                    "Substituted vegetables must be discounted by ninety percent."
                ],
                "explanation_en": "Julien insists on at least 48 hours notice before prep cycles so menus can adjust.",
                "explanation_tr": "Julien, hazırlık döngülerinden en az 48 saat önce bildirim yapılmasını şart koşar."
            },
            {
                "question_en": "Why does Elif request a twenty percent upfront financial retainer?",
                "question_tr_hint": "Elif neden yüzde yirmilik bir peşin avans talep ediyor?",
                "correct_answer": "To finance cover-crop seeding and mycorrhizal soil inoculants ahead of planting.",
                "distractors": [
                    "To construct luxury executive suites at the agricultural co-op headquarters.",
                    "To purchase an armada of commercial cargo airplanes.",
                    "To settle outstanding municipal parking violations across the fleet."
                ],
                "explanation_en": "Elif states the retainer finances cover-crop seeding and mycorrhizal soil inoculants.",
                "explanation_tr": "Elif, avansın örtü bitkisi tohumları ve toprak aşılarını finanse etmek için gerektiğini belirtir."
            },
            {
                "question_en": "How does the cooperative guarantee cold-chain integrity during produce transit?",
                "question_tr_hint": "Kooperatif ürün sevkiyatı sırasında soğuk zincir bütünlüğünü nasıl garanti eder?",
                "correct_answer": "Via IoT temperature loggers transmitting real-time readings between 3°C and 5°C directly to the kitchen.",
                "distractors": [
                    "By shipping all vegetables packed tightly inside dry ice blocks.",
                    "By flying produce aboard supersonic cargo drones in twenty minutes.",
                    "By relying on verbal driver assurances upon delivery arrival."
                ],
                "explanation_en": "Elif explains each crate carries IoT sensors transmitting 3-5°C readings to their tablet.",
                "explanation_tr": "Elif, her kasada tablete 3-5°C arası canlı veri gönderen IoT sensörleri bulunduğunu açıklar."
            }
        ],
        ["food_shopping", "supply_chain"]
    ),

    # 5. co-founder-equity-dispute (relationships / work-career)
    build_scenario(
        "listening.c1.co-founder-equity-dispute",
        "Mediating Founder Vesting and IP Contribution Equities",
        "C1", "negotiation",
        "Venture legal mediator Rachel mediates a restructuring of cap table percentages and reverse-vesting milestones between early-stage tech co-founders Kaan and David following a technical pivot.",
        [
            {"id": "rachel", "name": "Rachel", "role": "Startup Legal Mediator", "accent": "American"},
            {"id": "kaan", "name": "Kaan", "role": "Co-Founder & Chief Technology Officer", "accent": "Turkish"},
        ],
        "c1_co_founder_equity_dispute.mp3",
        [
            {"speaker_id": "rachel", "text_en": "Thank you for coming in, Kaan. David and I spoke earlier. Our goal today is to establish an equitable path forward without compromising your upcoming Series A valuation.", "text_tr": "Geldiğin için teşekkürler Kaan. David ile az önce konuştuk. Bugünkü amacımız, yaklaşan Seri A değerlemenizi tehlikeye atmadan adil bir uzlaşma yolu oluşturmaktır."},
            {"speaker_id": "kaan", "text_en": "I appreciate your neutrality, Rachel. However, when David founded the company, equity was split fifty-fifty based on our initial B2B sales automation concept.", "text_tr": "Tarafsızlığın için teşekkürler Rachel. Ancak şirketi ilk kurduğumuzda hisseler, başlangıçtaki B2B satış otomasyonu konseptimize dayanarak yarı yarıya paylaştırılmıştı."},
            {"speaker_id": "rachel", "text_en": "And since then, the enterprise pivoted entirely into autonomous distributed database architecture, which represents exclusively your proprietary engineering.", "text_tr": "Ve o zamandan bu yana girişim tamamen, münhasıran senin tescilli mühendisliğini temsil eden otonom dağıtık veritabanı mimarisine yöneldi."},
            {"speaker_id": "kaan", "text_en": "Exactly. I have authored eighty percent of the foundational codebase and secured our core patents, while David has struggled to close enterprise pilot contracts.", "text_tr": "Kesinlikle. Temel kod tabanının yüzde seksenini ben yazdım ve ana patentlerimizi aldım; David ise kurumsal pilot sözleşmelerini kapatmakta zorlandı."},
            {"speaker_id": "rachel", "text_en": "David acknowledges the intellectual property disparity, but he argues his early sweat equity and initial seed capital secured your first eighteen months of runway.", "text_tr": "David fikri mülkiyet farkını kabul ediyor ancak erken dönem emek ortaklığının ve tohum sermayesinin ilk on sekiz aylık nakit akışınızı sağladığını savunuyor."},
            {"speaker_id": "kaan", "text_en": "That runway was vital, but investors in our lead syndicate are already questioning why a non-technical CEO retains fifty percent without commercial validation.", "text_tr": "O nakit akışı hayatiydi, ancak lider yatırımcı grubumuzdaki yatırımcılar şimdiden ticari doğrulama olmaksızın teknik olmayan bir CEO'nun neden yüzde elli paya sahip olduğunu sorguluyor."},
            {"speaker_id": "rachel", "text_en": "Here is a balanced compromise: we implement a dynamic milestone-based reverse vesting schedule on a ten percent equity tranche.", "text_tr": "İşte dengeli bir uzlaşma: yüzde onluk bir hisse dilimi üzerinde dinamik, dönüm noktasına dayalı bir ters hak ediş takvimi uygulayabiliriz."},
            {"speaker_id": "kaan", "text_en": "If David hits two million in annual recurring revenue within four quarters, he vests that tranche; if not, those shares return to the employee stock option pool.", "text_tr": "David dört çeyrek içinde iki milyon yıllık tekrarlayan gelire ulaşırsa bu dilimi hak eder; ulaşamazsa o hisseler çalışan hisse opsiyon havuzuna geri döner."},
        ],
        [
            {
                "question_en": "What primary operational shift precipitated the equity disagreement between Kaan and David?",
                "question_tr_hint": "Kaan ve David arasındaki hisse anlaşmazlığını tetikleyen temel operasyonel değişim neydi?",
                "correct_answer": "The startup pivoted from a sales platform to a distributed database system engineered primarily by Kaan.",
                "distractors": [
                    "A hostile corporate buyout by an international defense conglomerate.",
                    "A total relocation of the company headquarters to an offshore territory.",
                    "The accidental loss of all customer credit card information."
                ],
                "explanation_en": "Rachel notes the company pivoted into autonomous distributed database architecture built by Kaan.",
                "explanation_tr": "Rachel, girişimin Kaan'ın geliştirdiği otonom dağıtık veritabanı mimarisine yöneldiğini belirtir."
            },
            {
                "question_en": "What contribution does David cite to justify maintaining his original equity percentage?",
                "question_tr_hint": "David orijinal hisse oranını korumayı haklı çıkarmak için hangi katkısını öne sürüyor?",
                "correct_answer": "His early founder labor and initial seed funding that financed their first eighteen months of operations.",
                "distractors": [
                    "His victory in an international competitive hackathon tournament.",
                    "His authorship of the company's trademarked logo illustrations.",
                    "His personal relationship with federal trade regulatory commissioners."
                ],
                "explanation_en": "Rachel explains David points to his early sweat equity and initial seed capital financing 18 months of runway.",
                "explanation_tr": "Rachel, David'in ilk 18 aylık süreyi finanse eden emek ortaklığı ve tohum sermayesini öne sürdüğünü söyler."
            },
            {
                "question_en": "What pressure from external investors does Kaan convey to the mediator?",
                "question_tr_hint": "Kaan arabulucuya dış yatırımcılardan gelen hangi baskıyı aktarıyor?",
                "correct_answer": "Lead investors question why a non-technical founder holds half the equity without commercial validation.",
                "distractors": [
                    "Investors demand an immediate liquidation of all corporate assets.",
                    "Investors require both founders to surrender all voting rights immediately.",
                    "Investors insist on replacing all human employees with automated algorithms."
                ],
                "explanation_en": "Kaan notes lead investors question why a non-technical CEO retains 50% without commercial traction.",
                "explanation_tr": "Kaan, lider yatırımcıların teknik olmayan CEO'nun ticari başarı olmadan %50 tutmasını sorguladığını söyler."
            },
            {
                "question_en": "What mechanism does Rachel recommend to resolve the cap table stalemate?",
                "question_tr_hint": "Rachel hisse tablosu kilitlenmesini çözmek için hangi mekanizmayı öneriyor?",
                "correct_answer": "A performance-contingent reverse vesting schedule applied to a ten percent equity block.",
                "distractors": [
                    "A coin-toss ceremony conducted before independent court bailiffs.",
                    "Splitting the company into two separate bankrupt corporate entities.",
                    "Selling all company intellectual property to the highest anonymous bidder."
                ],
                "explanation_en": "Rachel proposes a dynamic milestone-based reverse vesting schedule on a 10% equity tranche.",
                "explanation_tr": "Rachel, %10'luk dilim üzerinde dönüm noktasına bağlı ters hak ediş takvimi önerir."
            },
            {
                "question_en": "What consequence occurs if David fails to meet the two-million ARR benchmark?",
                "question_tr_hint": "David iki milyonluk ARR hedefine ulaşamazsa nasıl bir sonuç ortaya çıkar?",
                "correct_answer": "The unvested ten percent equity returns to the employee incentive pool.",
                "distractors": [
                    "David is sentenced to mandatory unpaid community service.",
                    "The shares are confiscated by federal tax authorities.",
                    "Kaan is legally obligated to purchase the shares at full market price."
                ],
                "explanation_en": "Kaan states that if David misses the target, those shares revert to the employee option pool.",
                "explanation_tr": "Kaan, hedefe ulaşılamazsa bu hisselerin çalışan opsiyon havuzuna geri döneceğini belirtir."
            }
        ],
        ["relationships", "work_career"]
    ),

    # 6. university-curriculum-accreditation (education / academic)
    build_scenario(
        "listening.c1.university-curriculum-accreditation",
        "Reviewing Transnational Engineering Accreditation Standards",
        "C1", "stakeholder_alignment",
        "Dean Harrison and Vice Dean Deniz assess program alignment with ABET and EUR-ACE criteria, discussing laboratory modernization, interdisciplinary capstone rubrics, and faculty research quotas.",
        [
            {"id": "harrison", "name": "Dean Harrison", "role": "Faculty Dean", "accent": "British"},
            {"id": "deniz", "name": "Deniz", "role": "Associate Dean of Academic Affairs", "accent": "Turkish"},
        ],
        "c1_university_curriculum_accreditation.mp3",
        [
            {"speaker_id": "harrison", "text_en": "Deniz, our upcoming decennial ABET and EUR-ACE evaluation visits are less than six months away. How is the outcome assessment documentation progressing across departments?", "text_tr": "Deniz, yaklaşan on yıllık ABET ve EUR-ACE değerlendirme ziyaretlerimize altı aydan az bir süre kaldı. Bölümler genelinde çıktı değerlendirme belgeleri nasıl ilerliyor?"},
            {"speaker_id": "deniz", "text_en": "We have consolidated student learning outcome portfolios for ninety percent of core engineering courses, Dean Harrison. However, our multidisciplinary capstone evaluation criteria remain somewhat subjective.", "text_tr": "Temel mühendislik derslerinin yüzde doksanı için öğrenci öğrenme çıktısı portföylerini birleştirdik Dekan Harrison. Ancak çok disiplinli bitirme projesi değerlendirme kriterlerimiz hâlâ biraz sübjektif kalıyor."},
            {"speaker_id": "harrison", "text_en": "Subjectivity in capstones is an immediate red flag for accreditation evaluators. They expect quantifiable rubrics covering professional ethics, life-cycle sustainability, and economic feasibility.", "text_tr": "Bitirme projelerindeki öznellik, akreditasyon denetçileri için doğrudan bir kırmızı bayraktır. Mesleki etik, yaşam döngüsü sürdürülebilirliği ve ekonomik fizibiliteyi kapsayan ölçülebilir dereceli puanlama anahtarları bekliyorlar."},
            {"speaker_id": "deniz", "text_en": "I will standardize an institutional grading matrix benchmarked against IEEE ethical canons and lifecycle carbon accounting before next month's faculty senate.", "text_tr": "Gelecek ayki fakülte senatosundan önce IEEE etik kuralları ve yaşam döngüsü karbon muhasebesi ile kıyaslanan kurumsal bir notlandırma matrisi standartlaştıracağım."},
            {"speaker_id": "harrison", "text_en": "Splendid. What about the feedback from our external industrial advisory board regarding laboratory modernization?", "text_tr": "Harika. Dış endüstriyel danışma kurulumuzun laboratuvar modernizasyonuna ilişkin geri bildirimleri ne durumda?"},
            {"speaker_id": "deniz", "text_en": "Industry advisors voiced concern that our cleanroom semiconductor microfabrication rigs and FPGA hardware synthesis benches lag behind modern wafer fabrication facilities.", "text_tr": "Sektör danışmanları, temiz oda yarı iletken mikro fabrikasyon cihazlarımızın ve FPGA donanım sentez tezgahlarımızın modern yonga üretim tesislerinin gerisinde kaldığı endişesini dile getirdiler."},
            {"speaker_id": "harrison", "text_en": "We have allocated a four-million-euro infrastructure endowment grant to procure modern electron lithography tooling and advanced embedded testbeds this summer.", "text_tr": "Bu yaz modern elektron litografi araçları ve gelişmiş gömülü test yatakları temin etmek için dört milyon avroluk bir altyapı bağış fonu tahsis ettik."},
            {"speaker_id": "deniz", "text_en": "That investment will ensure our laboratory facilities not only satisfy accreditation criteria but position our graduates at the vanguard of applied nanoelectronics.", "text_tr": "Bu yatırım, laboratuvar tesislerimizin yalnızca akreditasyon kriterlerini karşılamakla kalmayıp mezunlarımızı uygulamalı nanoelektroniğin ön saflarına yerleştirmesini sağlayacaktır."},
        ],
        [
            {
                "question_en": "What specific shortcoming does Deniz identify in the current engineering portfolio documentation?",
                "question_tr_hint": "Deniz mevcut mühendislik portföy belgelerinde hangi özel eksikliği tespit ediyor?",
                "correct_answer": "Evaluation rubrics for multidisciplinary capstone design projects remain excessively subjective.",
                "distractors": [
                    "A complete lack of attendance records across all undergraduate lectures.",
                    "Total absence of certified teaching credentials among senior professors.",
                    "Failure to teach basic differential calculus to engineering freshmen."
                ],
                "explanation_en": "Deniz notes that multidisciplinary capstone evaluation criteria remain somewhat subjective.",
                "explanation_tr": "Deniz, çok disiplinli bitirme projelerinin değerlendirme kriterlerinin sübjektif kaldığını belirtir."
            },
            {
                "question_en": "What dimensions must the standardized capstone rubric include to satisfy accreditation teams?",
                "question_tr_hint": "Standartlaştırılmış bitirme projesi puanlama anahtarı akreditasyon ekiplerini tatmin etmek için hangi boyutları içermelidir?",
                "correct_answer": "Professional ethics, life-cycle sustainability, and economic feasibility assessments.",
                "distractors": [
                    "Memorization of historical patent registry identification numbers.",
                    "Physical athletic performance benchmarks in campus recreation facilities.",
                    "Artistic aesthetic evaluations of student engineering sketchbooks."
                ],
                "explanation_en": "Dean Harrison emphasizes rubrics covering professional ethics, sustainability, and economic feasibility.",
                "explanation_tr": "Dekan Harrison mesleki etik, sürdürülebilirlik ve ekonomik fizibiliteyi vurgular."
            },
            {
                "question_en": "What concern was raised by the external industrial advisory board?",
                "question_tr_hint": "Dış endüstriyel danışma kurulu tarafından hangi endişe dile getirildi?",
                "correct_answer": "Semiconductor microfabrication and FPGA laboratory benches lagged behind contemporary industry standards.",
                "distractors": [
                    "The campus cafeteria lacked vegan and gluten-free dining options.",
                    "Engineering students were spending too much time studying mathematics.",
                    "Faculty parking garages lacked electric vehicle recharging plugs."
                ],
                "explanation_en": "Advisors noted microfabrication rigs and FPGA benches lagged behind modern facilities.",
                "explanation_tr": "Danışmanlar temiz oda ve FPGA donanımlarının güncel tesislerin gerisinde kaldığını bildirmiştir."
            },
            {
                "question_en": "How does the administration plan to remediate laboratory equipment deficiencies?",
                "question_tr_hint": "Yönetim laboratuvar ekipmanı eksikliklerini nasıl gidermeyi planlıyor?",
                "correct_answer": "By deploying a four-million-euro endowment to procure electron lithography and embedded testbeds.",
                "distractors": [
                    "By mandating that students bring personal laptop computers to all practical exams.",
                    "By replacing all physical laboratories with virtual two-dimensional video games.",
                    "By requesting second-hand donation surplus from local community colleges."
                ],
                "explanation_en": "Dean Harrison announces a 4-million-euro grant to acquire electron lithography tooling and testbeds.",
                "explanation_tr": "Dekan Harrison 4 milyon avroluk bütçeyle elektron litografi ve test sistemleri alınacağını açıklar."
            },
            {
                "question_en": "When are the formal accreditation peer-review evaluation visits scheduled to take place?",
                "question_tr_hint": "Resmi akreditasyon hakem değerlendirme ziyaretlerinin ne zaman yapılması planlanıyor?",
                "correct_answer": "Within the next six months.",
                "distractors": [
                    "In exactly three years.",
                    "By tomorrow morning.",
                    "In five years following curriculum overhauls."
                ],
                "explanation_en": "Dean Harrison notes the evaluation visits are less than six months away.",
                "explanation_tr": "Dekan Harrison değerlendirme ziyaretlerine altı aydan az bir süre kaldığını belirtir."
            }
        ],
        ["education", "academic"]
    ),

    # 7. crisis-communications-data-breach (communication / incident-response)
    build_scenario(
        "listening.c1.crisis-communications-data-breach",
        "Formulating Executive Crisis Communications for Security Breaches",
        "C1", "incident_response",
        "Strategic communications director Patricia and Chief Information Security Officer Burak draft public disclosure disclosures and regulatory notifications following an unauthorized API extraction incident.",
        [
            {"id": "patricia", "name": "Patricia", "role": "VP of Strategic Communications", "accent": "American"},
            {"id": "burak", "name": "Burak", "role": "Chief Information Security Officer", "accent": "Turkish"},
        ],
        "c1_crisis_communications_data_breach.mp3",
        [
            {"speaker_id": "patricia", "text_en": "Burak, the executive committee convenes in forty-five minutes. We need absolute clarity on the scope of the incident before wire services start running speculative headlines.", "text_tr": "Burak, icra kurulu kırk beş dakika içinde toplanıyor. Haber ajansları spekülatif başlıklar atmaya başlamadan önce olayın kapsamı konusunda mutlak netliğe ihtiyacımız var."},
            {"speaker_id": "burak", "text_en": "Understood, Patricia. At two-fifteen UTC, our automated anomaly detection flagged an unauthorized scraping cluster exploiting an authenticated API rate-limiting vulnerability.", "text_tr": "Anlaşıldı Patricia. Saat 02:15 UTC'de otomatik anomali tespit sistemimiz, kimliği doğrulanmış bir API hız sınırlama açığından yararlanan yetkisiz bir kazıma kümesini işaretledi."},
            {"speaker_id": "patricia", "text_en": "Was personal identifiable information compromised, and does this trigger the statutory seventy-two-hour GDPR European Data Protection Board notification threshold?", "text_tr": "Kişisel olarak tanımlanabilir bilgiler tehlikeye girdi mi ve bu durum yasal yetmiş iki saatlik GDPR Avrupa Veri Koruma Kurulu bildirim eşiğini tetikliyor mu?"},
            {"speaker_id": "burak", "text_en": "Forensic log analysis indicates that salted-and-hashed passwords and financial payment instruments remained completely uncompromised inside our hardware security module vault.", "text_tr": "Adli bilişim kayıt analizi, tuzlanmış ve özetlenmiş parolalar ile finansal ödeme araçlarının donanım güvenlik modülü kasamızda tamamen güvende kaldığını gösteriyor."},
            {"speaker_id": "patricia", "text_en": "What specific data was exfiltrated by the threat actor during the three-hour egress window?", "text_tr": "Üç saatlik veri çıkış penceresi sırasında tehdit aktörü tarafından tam olarak hangi veriler sızdırıldı?"},
            {"speaker_id": "burak", "text_en": "The adversary extracted approximately forty thousand enterprise profile records containing publicly indexed business email addresses, job titles, and hashed corporate domain identifiers.", "text_tr": "Saldırgan; herkese açık olarak dizinlenmiş iş e-posta adreslerini, unvanları ve karma kurumsal alan adı tanımlayıcılarını içeren yaklaşık kırk bin kurumsal profil kaydını çıkardı."},
            {"speaker_id": "patricia", "text_en": "That substantially mitigates our catastrophic liability profile. We will structure our disclosure around transparent remediation rather than evasive corporate obfuscation.", "text_tr": "Bu durum felaket niteliğindeki sorumluluk profilimizi önemli ölçüde hafifletiyor. Açıklamamızı kaçamak kurumsal gizleme yerine şeffaf iyileştirme etrafında yapılandıracağız."},
            {"speaker_id": "burak", "text_en": "We have already revoked all associated session tokens, patched the vulnerable endpoints, and rotated our microservice cryptographic keys.", "text_tr": "İlişkili tüm oturum belirteçlerini iptal ettik, savunmasız uç noktaları yamaladık ve mikro hizmet kriptografik anahtarlarımızı rotasyona tabi tuttuk."}
        ],
        [
            {
                "question_en": "What security vulnerability enabled the unauthorized data extraction?",
                "question_tr_hint": "Yetkisiz veri çıkarımına hangi güvenlik açığı olanak sağladı?",
                "correct_answer": "An authenticated API rate-limiting configuration loophole exploited by automated scrapers.",
                "distractors": [
                    "A physical break-in into the main data center server room.",
                    "An unencrypted backup tape misplaced in a public airport terminal.",
                    "A rogue employee manually copying files onto USB flash drives."
                ],
                "explanation_en": "Burak explains that scrapers exploited an authenticated API rate-limiting vulnerability.",
                "explanation_tr": "Burak, kazıyıcıların kimliği doğrulanmış bir API hız sınırlama açığından yararlandığını belirtir."
            },
            {
                "question_en": "What critical user security data remained fully safeguarded in the security vault?",
                "question_tr_hint": "Güvenlik kasasında hangi kritik kullanıcı güvenliği verileri tamamen korunmuş olarak kaldı?",
                "correct_answer": "Salted-and-hashed passwords and financial payment instrument records.",
                "distractors": [
                    "Employee payroll compensation spreadsheets.",
                    "Public marketing press releases and social media posts.",
                    "Unclassified corporate promotional event flyers."
                ],
                "explanation_en": "Burak confirms hashed passwords and financial payment instruments remained completely uncompromised.",
                "explanation_tr": "Burak, tuzlanmış şifrelerin ve ödeme araçlarının donanım kasasında güvende kaldığını teyit eder."
            },
            {
                "question_en": "What was the exact composition of the data exfiltrated during the incident?",
                "question_tr_hint": "Olay sırasında sızdırılan verilerin tam içeriği neydi?",
                "correct_answer": "Approximately forty thousand enterprise profiles containing public business emails, titles, and domain hashes.",
                "distractors": [
                    "Top-secret national military defense coordinates.",
                    "Patient healthcare genetic diagnostic histories.",
                    "Biometric retinal scan records of all European citizens."
                ],
                "explanation_en": "Burak reports 40,000 enterprise profiles with public business emails, titles, and domain hashes were taken.",
                "explanation_tr": "Burak, 40 bin kurumsal profilin (iş e-postaları, unvanlar, alan adı özetleri) sızdığını açıklar."
            },
            {
                "question_en": "What strategic posture does Patricia select for the upcoming public disclosure?",
                "question_tr_hint": "Patricia yaklaşan kamuoyu açıklaması için nasıl bir stratejik tutum seçiyor?",
                "correct_answer": "Proactive and transparent disclosure highlighting swift technical remediation.",
                "distractors": [
                    "Denying all knowledge of the event until legal injunctions force disclosure.",
                    "Blaming competitor technology companies for fabricating false claims.",
                    "Permanently deleting all corporate social media and public relations channels."
                ],
                "explanation_en": "Patricia chooses transparent remediation over evasive corporate obfuscation.",
                "explanation_tr": "Patricia kaçamak gizleme yerine şeffaf iyileştirme odaklı bir açıklama seçer."
            },
            {
                "question_en": "What immediate technical remediation did Burak's team enact?",
                "question_tr_hint": "Burak'ın ekibi hangi acil teknik iyileştirmeyi gerçekleştirdi?",
                "correct_answer": "Revoking session tokens, deploying API endpoint patches, and rotating microservice crypto keys.",
                "distractors": [
                    "Shutting down the global electrical power grid entirely.",
                    "Destroying all enterprise server hard drives with industrial shredders.",
                    "Reverting corporate infrastructure back to paper-based filing cabinets."
                ],
                "explanation_en": "Burak confirms tokens were revoked, endpoints patched, and crypto keys rotated.",
                "explanation_tr": "Burak oturumların iptal edildiğini, açıkların yamandığını ve anahtarların yenilendiğini belirtir."
            }
        ],
        ["communication", "leadership"]
    ),

    # 8. technical-fellow-executive-briefing (work-career / technology)
    build_scenario(
        "listening.c1.technical-fellow-executive-briefing",
        "Briefing Leadership on AI Compute Infrastructure and Microarchitectures",
        "C1", "executive_briefing",
        "Principal AI Research Scientist Melis briefs Chief Technology Officer Greg on high-bandwidth memory interconnects, accelerator cluster utilization, and capital expenditure forecasts.",
        [
            {"id": "greg", "name": "Greg", "role": "Chief Technology Officer", "accent": "American"},
            {"id": "melis", "name": "Melis", "role": "Distinguished Technical Fellow", "accent": "Turkish"},
        ],
        "c1_technical_fellow_executive_briefing.mp3",
        [
            {"speaker_id": "greg", "text_en": "Melis, the board is questioning our projected fifty-million-dollar capital outlay for next-generation accelerator hardware. They want to know why our current GPU clusters cannot sustain model fine-tuning.", "text_tr": "Melis, yönetim kurulu yeni nesil hızlandırıcı donanımı için öngörülen elli milyon dolarlık sermaye harcamamızı sorguluyor. Mevcut GPU kümelerimizin neden model ince ayarını sürdüremediğini bilmek istiyorlar."},
            {"speaker_id": "melis", "text_en": "The core constraint is not raw floating-point operations per second, Greg; it is the memory-wall bottleneck. Our trillion-parameter multimodal models require high-bandwidth memory interconnects that exceed PCIe bandwidth.", "text_tr": "Temel kısıtlama saniye başına ham kayan nokta işlemleri değil Greg; bellek duvarı darboğazıdır. Trilyon parametreli çok modlu modellerimiz, PCIe bant genişliğini aşan yüksek bant genişlikli bellek ara bağlantıları gerektirir."},
            {"speaker_id": "greg", "text_en": "Meaning that our tensor processing cores spend substantial cycles sitting idle waiting for memory cache lines to refill?", "text_tr": "Yani tensör işleme çekirdeklerimiz bellek önbellek satırlarının yeniden dolmasını beklerken önemli sayıda döngüyü boşta mı geçiriyor?"},
            {"speaker_id": "melis", "text_en": "Precisely. Our Model Flops Utilization is hovering at twenty-eight percent. Transitioning to custom ASIC accelerators with HBM3e stacks and liquid-cooled optical interconnects will elevate MFU to sixty-two percent.", "text_tr": "Kesinlikle. Model Flops Kullanım oranımız yüzde yirmi sekizde seyrediyor. HBM3e yığınlarına ve sıvı soğutmalı optik ara bağlantılara sahip özel ASIC hızlandırıcılarına geçmek MFU'yu yüzde altmış ikiye çıkaracaktır."},
            {"speaker_id": "greg", "text_en": "More than doubling our effective computational throughput per watt of datacenter power consumed.", "text_tr": "Tüketilen veri merkezi gücünün watt'ı başına etkin hesaplama verimimizi iki katından fazla artıracak."},
            {"speaker_id": "melis", "text_en": "Correct. Furthermore, by partitioning KV-cache memory across distributed non-volatile storage, we can scale context windows from thirty-two thousand tokens up to one million tokens without out-of-memory faults.", "text_tr": "Doğru. Dahası, KV önbellek belleğini dağıtık kalıcı depolama boyunca bölümleyerek, bellek yetersizliği hataları olmaksızın bağlam pencerelerini otuz iki bin belirteçten bir milyon belirtece kadar ölçeklendirebiliriz."},
            {"speaker_id": "greg", "text_en": "What is the lead time on procurement and fabrication from our foundry partners?", "text_tr": "Dökümhane ortaklarımızdan tedarik ve üretim için teslim süresi nedir?"},
            {"speaker_id": "melis", "text_en": "If we place wafer allocation deposits this quarter, initial cluster rack integration begins in nine months, allowing production deployment before fiscal year-end.", "text_tr": "Bu çeyrekte plaka tahsis depozitolarını yatırırsak, ilk küme kabin entegrasyonu dokuz ay içinde başlar ve mali yıl sonundan önce üretime geçişe olanak tanır."},
        ],
        [
            {
                "question_en": "What architectural bottleneck prevents existing GPU clusters from efficiently training trillion-parameter models?",
                "question_tr_hint": "Mevcut GPU kümelerinin trilyon parametreli modelleri verimli şekilde eğitmesini hangi mimari darboğaz engelliyor?",
                "correct_answer": "The memory-wall constraint where data bus throughput fails to feed tensor compute cores rapidly.",
                "distractors": [
                    "A complete lack of electrical wiring in modern enterprise server facilities.",
                    "Excessive processor fan noise exceeding occupational health thresholds.",
                    "An incompatible operating system kernel that cannot execute binary files."
                ],
                "explanation_en": "Melis explains the constraint is the memory-wall bottleneck exceeding PCIe bandwidth.",
                "explanation_tr": "Melis, temel kısıtlamanın PCIe bant genişliğini aşan bellek duvarı darboğazı olduğunu açıklar."
            },
            {
                "question_en": "What quantitative improvement in Model Flops Utilization (MFU) does Melis forecast?",
                "question_tr_hint": "Melis Model Flops Kullanımında (MFU) nasıl bir niceliksel iyileşme öngörüyor?",
                "correct_answer": "An increase from twenty-eight percent up to sixty-two percent.",
                "distractors": [
                    "A marginal rise from ninety-eight to ninety-nine percent.",
                    "A reduction down to zero percent during scheduled maintenance.",
                    "A tenfold decrease in computational algorithmic efficiency."
                ],
                "explanation_en": "Melis states MFU will jump from 28% to 62% with custom ASICs and HBM3e.",
                "explanation_tr": "Melis MFU'nun %28'den %62'ye yükseleceğini belirtir."
            },
            {
                "question_en": "How will distributed KV-cache partitioning impact model capabilities?",
                "question_tr_hint": "Dağıtık KV önbellek bölümlemesi model yeteneklerini nasıl etkileyecek?",
                "correct_answer": "It will expand context windows from 32,000 tokens to one million tokens without memory overflow.",
                "distractors": [
                    "It will compress all text documents into binary Morse code.",
                    "It will restrict language models to single-sentence responses.",
                    "It will permanently prevent models from learning foreign languages."
                ],
                "explanation_en": "Melis explains it scales context windows from 32k tokens up to 1 million tokens.",
                "explanation_tr": "Melis bağlam penceresinin 32 bin belirteçten 1 milyon belirtece ölçekleneceğini ifade eder."
            },
            {
                "question_en": "What thermodynamic innovation is incorporated into the proposed accelerator architecture?",
                "question_tr_hint": "Önerilen hızlandırıcı mimarisine hangi termodinamik yenilik dahil edilmiştir?",
                "correct_answer": "Liquid-cooled optical interconnects accompanying HBM3e high-bandwidth memory stacks.",
                "distractors": [
                    "Submerging servers in Arctic glacial ice packs.",
                    "Attaching manual hand-cranked ventilation fans to server doors.",
                    "Operating servers exclusively in outer-space vacuum orbits."
                ],
                "explanation_en": "Melis highlights HBM3e stacks paired with liquid-cooled optical interconnects.",
                "explanation_tr": "Melis HBM3e yığınları ve sıvı soğutmalı optik ara bağlantıları vurgular."
            },
            {
                "question_en": "What operational timeline governs the delivery of the new compute cluster?",
                "question_tr_hint": "Yeni bilgi işlem kümesinin teslimatını hangi operasyonel takvim yönetiyor?",
                "correct_answer": "Rack integration commences in nine months, achieving deployment within the fiscal year.",
                "distractors": [
                    "Hardware delivery arrives by tomorrow afternoon via overnight courier.",
                    "Fabrication will require fifteen consecutive calendar years.",
                    "Deployment will occur indefinitely after corporate dissolution."
                ],
                "explanation_en": "Melis outlines rack integration in 9 months, enabling deployment before fiscal year-end.",
                "explanation_tr": "Melis kabin entegrasyonunun 9 ayda başlayıp mali yıl bitmeden devreye alınacağını belirtir."
            }
        ],
        ["work_career", "technology"]
    ),

    # 9. mergers-acquisitions-due-diligence (business / finance)
    build_scenario(
        "listening.c1.mergers-acquisitions-due-diligence",
        "Evaluating Strategic Cross-Border FinTech Mergers & Acquisitions",
        "C1", "negotiation",
        "Private equity investment partner Simon and acquisition lead Yasemin evaluate regulatory anti-trust exposure, customer churn metrics, and intellectual property indemnification in a cross-border deal.",
        [
            {"id": "simon", "name": "Simon", "role": "Private Equity Partner", "accent": "British"},
            {"id": "yasemin", "name": "Yasemin", "role": "M&A Strategy Lead", "accent": "Turkish"},
        ],
        "c1_mergers_acquisitions_due_diligence.mp3",
        [
            {"speaker_id": "simon", "text_en": "Yasemin, our investment committee sits in forty-eight hours to decide whether to tender our formal offer for BalticPay. Have your due diligence workstreams uncovered any deal-breakers?", "text_tr": "Yasemin, yatırım komitemiz BalticPay için resmi teklifimizi sunup sunmayacağımıza karar vermek üzere kırk sekiz saat içinde toplanıyor. Durum tespiti çalışma gruplarınız herhangi bir anlaşmayı bozacak pürüz ortaya çıkardı mı?"},
            {"speaker_id": "yasemin", "text_en": "We haven't uncovered outright fraud, Simon, but there are two significant valuation adjustments we must account for: recurring customer churn and regulatory exposure under the revised Payment Services Directive.", "text_tr": "Açık bir dolandırıcılık tespit etmedik Simon, ancak hesaba katmamız gereken iki önemli değerleme düzeltmesi var: tekrarlayan müşteri kaybı ve revize edilmiş Ödeme Hizmetleri Direktifi kapsamındaki düzenleyici risk."},
            {"speaker_id": "simon", "text_en": "Let's unpack customer churn first. Their pitch deck claimed ninety-four percent net revenue retention across Nordic enterprise merchants.", "text_tr": "Önce müşteri kaybını ele alalım. Sunum dosyaları İskandinav kurumsal üye işyerleri genelinde yüzde doksan dört net gelir elde tutma oranı iddia ediyordu."},
            {"speaker_id": "yasemin", "text_en": "Our forensic cohort analysis revealed that tier-one merchant retention is masked by heavy discounts. Once promotional interchange subsidies lapse, underlying gross merchant churn spikes to eighteen percent.", "text_tr": "Adli kohort analizimiz, birinci sınıf üye işyeri elde tutma oranının ağır indirimlerle maskelendiğini ortaya koydu. Promosyonel takas sübvansiyonları sona erdiğinde, altta yatan brüt üye işyeri kaybı yüzde on sekize fırlıyor."},
            {"speaker_id": "simon", "text_en": "That substantially dampens our terminal EBITDA multiple expectations. What about their regulatory capital compliance?", "text_tr": "Bu durum nihai FAVÖK çarpanı beklentilerimizi önemli ölçüde zayıflatıyor. Peki ya düzenleyici sermaye uyumları?"},
            {"speaker_id": "yasemin", "text_en": "The European Banking Authority is probing their cross-border remittance corridor into non-EEA jurisdictions. They have under-provisioned anti-money laundering reserves by approximately twelve million euros.", "text_tr": "Avrupa Bankacılık Otoritesi, AEA dışı yargı bölgelerine yönelik sınır ötesi havale koridorlarını inceliyor. Kara para aklamayı önleme karşılıklarını yaklaşık on iki milyon avro eksik ayırmışlar."},
            {"speaker_id": "simon", "text_en": "That necessitates a substantial purchase price haircut and an escrow indemnity holdback.", "text_tr": "Bu da satın alma fiyatında ciddi bir indirim ve bir emanet tazminat kesintisi gerektirir."},
            {"speaker_id": "yasemin", "text_en": "I recommend shaving thirty million euros off the enterprise valuation and conditioning twenty percent of the cash consideration on an eighteen-month regulatory indemnity escrow.", "text_tr": "Firma değerinden otuz milyon avro kırpılmasını ve nakit bedelin yüzde yirmisinin on sekiz aylık bir düzenleyici tazminat emanetine bağlanmasını öneriyorum."},
        ],
        [
            {
                "question_en": "What discrepancy did forensic cohort analysis reveal regarding BalticPay's merchant retention?",
                "question_tr_hint": "Adli kohort analizi BalticPay'in üye işyeri elde tutma oranına ilişkin hangi tutarsızlığı ortaya çıkardı?",
                "correct_answer": "Promotional subsidies concealed an underlying gross merchant churn rate of eighteen percent.",
                "distractors": [
                    "All Nordic merchants had terminated their contracts simultaneously last week.",
                    "BalticPay was processing transactions without using mathematical addition.",
                    "Merchant retention was artificially inflated by fabricating fictitious transactions."
                ],
                "explanation_en": "Yasemin reveals that after promotional subsidies lapse, gross merchant churn spikes to 18%.",
                "explanation_tr": "Yasemin teşvikler bittiğinde gerçek müşteri kaybının %18'e fırladığını ortaya koyar."
            },
            {
                "question_en": "What regulatory liability did Yasemin uncover during compliance audits?",
                "question_tr_hint": "Yasemin uyum denetimleri sırasında hangi düzenleyici yükümlülüğü ortaya çıkardı?",
                "correct_answer": "An estimated twelve-million-euro shortfall in anti-money laundering reserves under investigation.",
                "distractors": [
                    "An unpaid parking ticket issued to BalticPay's delivery bicycles.",
                    "A mandatory bankruptcy decree signed by European commercial courts.",
                    "An environmental fine for using unrecyclable cardboard envelopes."
                ],
                "explanation_en": "Yasemin notes the EBA probe and a 12-million-euro under-provisioning in AML reserves.",
                "explanation_tr": "Yasemin kara para aklamayı önleme karşılıklarında 12 milyon avro açık olduğunu belirtir."
            },
            {
                "question_en": "How does Yasemin propose restructuring the purchase offer to insulate against liabilities?",
                "question_tr_hint": "Yasemin yükümlülüklere karşı korunmak için satın alma teklifini nasıl yeniden yapılandırmayı öneriyor?",
                "correct_answer": "Reducing enterprise value by €30M and withholding 20% in an 18-month indemnity escrow.",
                "distractors": [
                    "Paying double the purchase price in cash to expedite closing.",
                    "Borrowing money from predatory offshore loan syndicates.",
                    "Refusing to sign any legal agreements until next decade."
                ],
                "explanation_en": "Yasemin recommends shaving €30M off valuation and putting 20% in an 18-month indemnity escrow.",
                "explanation_tr": "Yasemin değerlemeden 30 milyon avro düşülmesini ve %20'nin emanet hesabında tutulmasını önerir."
            },
            {
                "question_en": "How do these due diligence findings impact Simon's financial projections?",
                "question_tr_hint": "Bu durum tespiti bulguları Simon'ın finansal projeksiyonlarını nasıl etkiliyor?",
                "correct_answer": "They significantly lower the anticipated terminal EBITDA multiple upon eventual exit.",
                "distractors": [
                    "They guarantee an immediate guaranteed return of ten thousand percent.",
                    "They eliminate all taxes owed by the private equity firm permanently.",
                    "They force the immediate retirement of all private equity general partners."
                ],
                "explanation_en": "Simon notes the findings substantially dampen terminal EBITDA multiple expectations.",
                "explanation_tr": "Simon bulguların nihai FAVÖK çarpanı beklentilerini önemli ölçüde düşürdüğünü söyler."
            },
            {
                "question_en": "When must the private equity investment committee reach a conclusive decision?",
                "question_tr_hint": "Özel sermaye yatırım komitesi ne zaman nihai bir karara varmak zorundadır?",
                "correct_answer": "Within forty-eight hours.",
                "distractors": [
                    "In exactly three business years.",
                    "Before this morning's financial markets close at noon.",
                    "Following the complete restructuring of European banking legislation."
                ],
                "explanation_en": "Simon opens by stating the investment committee sits in forty-eight hours.",
                "explanation_tr": "Simon komitenin kırk sekiz saat içinde toplanacağını belirtir."
            }
        ],
        ["business", "finance"]
    ),

    # 10. cloud-security-penetration-testing (technology / security)
    build_scenario(
        "listening.c1.cloud-security-penetration-testing",
        "Analyzing Zero-Trust Cloud Penetration Test Debriefings",
        "C1", "engineering_meeting",
        "Offensive security specialist Leo debriefs cloud infrastructure director Emre regarding privilege escalation vectors identified in identity federation and cross-tenant IAM policies.",
        [
            {"id": "leo", "name": "Leo", "role": "Lead Penetration Tester", "accent": "Australian"},
            {"id": "emre", "name": "Emre", "role": "Director of Cloud Infrastructure", "accent": "Turkish"},
        ],
        "c1_cloud_security_penetration_testing.mp3",
        [
            {"speaker_id": "leo", "text_en": "G'day Emre. Our red team has finalized our two-week blind adversarial assessment of your multi-cloud production environment. Overall, your perimeter perimeter ingress controls were solid.", "text_tr": "İyi günler Emre. Kırmızı ekibimiz çoklu bulut üretim ortamınızdaki iki haftalık kör düşman değerlendirmesini tamamladı. Genel olarak, çevre giriş kontrolleriniz sağlamdı."},
            {"speaker_id": "emre", "text_en": "Thanks Leo. We invested heavily in web application firewalls and zero-trust mutual TLS. Did you identify any exploitable footholds?", "text_tr": "Teşekkürler Leo. Web uygulaması güvenlik duvarlarına ve sıfır güven karşılıklı TLS'e büyük yatırım yaptık. Yararlanılabilecek herhangi bir sızma noktası belirlediniz mi?"},
            {"speaker_id": "leo", "text_en": "Once we compromised a developer staging credential through an unrotated OAuth refresh token, we bypassed your internal network segmentation via identity federation abuse.", "text_tr": "Döndürülmemiş bir OAuth yenileme belirteci aracılığıyla bir geliştirici hazırlık kimlik bilgisini ele geçirdikten sonra, kimlik federasyonu suistimali yoluyla dahili ağ bölümlemenizi aştık."},
            {"speaker_id": "emre", "text_en": "How did you bridge from a low-privilege staging environment into our restricted production Kubernetes cluster?", "text_tr": "Düşük ayrıcalıklı bir hazırlık ortamından kısıtlı üretim Kubernetes kümemize nasıl geçiş yaptınız?"},
            {"speaker_id": "leo", "text_en": "Your cloud IAM role permitted excessive wildcard permissions on instance metadata services. By assuming an over-privileged CI/CD service principal, we extracted temporary STS session credentials.", "text_tr": "Bulut IAM rolünüz, bulut sunucusu meta veri hizmetlerinde aşırı joker karakter izinlerine olanak tanıyordu. Aşırı yetkili bir CI/CD hizmet sorumlusunu üstlenerek geçici STS oturum kimlik bilgilerini çıkardık."},
            {"speaker_id": "emre", "text_en": "Which granted access to our centralized Key Management Service and secrets store without generating an alert in our Security Information and Event Management platform.", "text_tr": "Bu da Güvenlik Bilgileri ve Olay Yönetimi platformumuzda bir uyarı oluşturmadan merkezi Anahtar Yönetim Hizmetimize ve sırlar depomuza erişim sağladı."},
            {"speaker_id": "leo", "text_en": "Spot on. Because your SIEM lacked behavioral anomaly detection for cross-account assume-role API calls, our lateral movement was virtually indistinguishable from legitimate automated build pipelines.", "text_tr": "Tam üstüne bastın. SIEM platformunuz hesaplar arası rol üstlenme API çağrıları için davranışsal anomali tespitinden yoksun olduğundan, yanal hareketimiz meşru otomatik derleme işlem hatlarından neredeyse ayırt edilemezdi."},
            {"speaker_id": "emre", "text_en": "We will immediately enforce IMDSv2 token-bound metadata across all instances and restrict IAM role assumption using cryptographic session-tag policies.", "text_tr": "Tüm bulut sunucularında derhal IMDSv2 belirteç bağlantılı meta verileri zorunlu kılacağız ve kriptografik oturum etiketi ilkelerini kullanarak IAM rol üstlenimini kısıtlayacağız."},
        ],
        [
            {
                "question_en": "What initial entry vector enabled the red team to gain a foothold?",
                "question_tr_hint": "Kırmızı ekibin sisteme sızmasını sağlayan ilk giriş vektörü neydi?",
                "correct_answer": "An unrotated OAuth refresh token granting access to developer staging credentials.",
                "distractors": [
                    "A brute-force dictionary attack against public SSH ports.",
                    "A malicious USB drive planted in the office reception lobby.",
                    "An unencrypted Wi-Fi router operating without a password."
                ],
                "explanation_en": "Leo explains they compromised a developer staging credential through an unrotated OAuth token.",
                "explanation_tr": "Leo döndürülmemiş bir OAuth belirteci ile geliştirici ortamına sızdıklarını açıklar."
            },
            {
                "question_en": "How did the testers achieve privilege escalation from staging to production?",
                "question_tr_hint": "Test uzmanları hazırlık ortamından üretime ayrıcalık yükseltmeyi nasıl başardı?",
                "correct_answer": "By assuming an over-privileged CI/CD principal via wildcard permissions on instance metadata.",
                "distractors": [
                    "By bribing a senior database administrator with monetary gifts.",
                    "By disconnecting the physical fiber-optic cables connecting the building.",
                    "By guessing the root administrative password on the main console."
                ],
                "explanation_en": "Leo details how excessive wildcard permissions on instance metadata allowed assuming an over-privileged CI/CD role.",
                "explanation_tr": "Leo sunucu meta verilerindeki joker izinlerin aşırı yetkili CI/CD rolünü üstlenmelerini sağladığını söyler."
            },
            {
                "question_en": "Why did the lateral movement fail to trigger alerts in the organization's SIEM platform?",
                "question_tr_hint": "Yanal hareket kuruluşun SIEM platformunda neden uyarı tetiklemedi?",
                "correct_answer": "The monitoring system lacked behavioral anomaly heuristics for cross-account role assumption.",
                "distractors": [
                    "The SIEM software was accidentally turned off during the assessment.",
                    "All logging hard drives were filled to complete capacity.",
                    "The security operations team was absent due to a national holiday."
                ],
                "explanation_en": "Leo notes the SIEM lacked behavioral anomaly detection for cross-account assume-role calls.",
                "explanation_tr": "Leo SIEM sisteminin hesaplar arası rol üstlenme anomalilerini algılayamadığını belirtir."
            },
            {
                "question_en": "What specific cloud security remediation does Emre commit to enacting immediately?",
                "question_tr_hint": "Emre derhal hangi özel bulut güvenliği iyileştirmesini gerçekleştirmeyi taahhüt ediyor?",
                "correct_answer": "Enforcing IMDSv2 token-bound metadata and session-tag policy constraints on role assumptions.",
                "distractors": [
                    "Permanently terminating all cloud contracts and hosting servers on-premises.",
                    "Prohibiting developers from utilizing automated software build pipelines.",
                    "Disabling all user passwords in favor of handwritten paper signatures."
                ],
                "explanation_en": "Emre pledges to mandate IMDSv2 metadata and enforce cryptographic session-tag policies.",
                "explanation_tr": "Emre IMDSv2 meta veri zorunluluğu ve oturum etiketi ilkeleri getireceğini taahhüt eder."
            },
            {
                "question_en": "What aspect of Emre's defensive infrastructure performed well according to Leo?",
                "question_tr_hint": "Leo'ya göre Emre'nin savunma altyapısının hangi yönü iyi performans gösterdi?",
                "correct_answer": "Perimeter ingress boundary controls including web application firewalls and mutual TLS.",
                "distractors": [
                    "Biometric voice recognition access controls on workstation microphones.",
                    "Paper shredding procedures within the corporate accounting department.",
                    "Automated physical security doors guarding executive office corridors."
                ],
                "explanation_en": "Leo acknowledges that perimeter ingress controls and WAF/mTLS were solid.",
                "explanation_tr": "Leo çevre giriş kontrolleri ve WAF/mTLS yapısının sağlam olduğunu kabul eder."
            }
        ],
        ["technology", "security"]
    ),

    # 11. municipal-smart-grid-zoning (society / urban-planning)
    build_scenario(
        "listening.c1.municipal-smart-grid-zoning",
        "Aligning Urban District Decentralized Microgrid Planning",
        "C1", "stakeholder_alignment",
        "City urban development commissioner Vance and renewable energy consortium lead Hande review civic zoning permits, battery energy storage safety easements, and substation interconnects.",
        [
            {"id": "vance", "name": "Vance", "role": "Urban Planning Commissioner", "accent": "American"},
            {"id": "hande", "name": "Hande", "role": "Clean Energy Program Director", "accent": "Turkish"},
        ],
        "c1_municipal_smart_grid_zoning.mp3",
        [
            {"speaker_id": "vance", "text_en": "Hande, our municipal planning commission is enthusiastic about your consortium's proposal for an eight-megawatt decentralized urban microgrid in the historic port district.", "text_tr": "Hande, belediye planlama komisyonumuz konsorsiyumunuzun tarihi liman bölgesinde sekiz megavatlık merkezi olmayan kentsel mikro şebeke önerisi konusunda çok hevesli."},
            {"speaker_id": "hande", "text_en": "Thank you Commissioner Vance. Integrating rooftop solar, commercial district fuel cells, and utility-scale battery energy storage will reduce peak grid strain by thirty-five percent.", "text_tr": "Teşekkürler Komisyon Üyesi Vance. Çatı güneş enerjisini, ticari bölge yakıt hücrelerini ve şebeke ölçeğinde batarya enerji depolamasını entegre etmek zirve şebeke yükünü yüzde otuz beş azaltacaktır."},
            {"speaker_id": "vance", "text_en": "However, several neighborhood civic associations have filed zoning objections concerning the twenty-megawatt-hour lithium iron phosphate battery installation adjacent to residential heritage quarters.", "text_tr": "Ancak, bazı mahalle sivil dernekleri, konut mirası mahallelerine komşu yirmi megavatsaatlik lityum demir fosfat batarya kurulumuyla ilgili imar itirazlarında bulundu."},
            {"speaker_id": "hande", "text_en": "I understand their apprehension regarding thermal runaway and fire propagation risks. However, lithium iron phosphate chemistry is intrinsically resistant to oxygen release compared to nickel manganese cobalt cells.", "text_tr": "Termal kaçak ve yangın yayılma risklerine ilişkin endişelerini anlıyorum. Ancak lityum demir fosfat kimyası nikel manganez kobalt hücrelerine kıyasla oksijen salınımına karşı doğal olarak dirençlidir."},
            {"speaker_id": "vance", "text_en": "What engineered safeguards are integrated into the enclosure design to meet National Fire Protection Association standard 855?", "text_tr": "Ulusal Yangından Korunma Birliği 855 standardını karşılamak için muhafaza tasarımına hangi mühendislik güvenlik önlemleri entegre edilmiştir?"},
            {"speaker_id": "hande", "text_en": "Each battery module incorporates dedicated aerosol fire suppression canisters, deflagration pressure-relief venting, and continuous infrared gas sniffing to detect off-gassing thirty minutes prior to thermal events.", "text_tr": "Her batarya modülü özel aerosol yangın bastırma kutuları, patlama basınç tahliye delikleri ve termal olaylardan otuz dakika önce gaz çıkışını tespit etmek için sürekli kızılötesi gaz algılayıcıları içerir."},
            {"speaker_id": "vance", "text_en": "If you supplement that with a fifty-foot landscaped green buffer and host an informational town hall for local residents, the commission is prepared to approve the zoning variance.", "text_tr": "Bunu elli metrelik peyzajlı yeşil bir tampon bölgeyle tamamlarsanız ve bölge sakinleri için bilgilendirici bir halk toplantısı düzenlerseniz, komisyon imar değişikliğini onaylamaya hazırdır."},
            {"speaker_id": "hande", "text_en": "We will gladly commit to the acoustic green barrier and coordinate with municipal fire chiefs to conduct joint emergency drills next month.", "text_tr": "Akustik yeşil bariyere memnuniyetle bağlı kalacağız ve önümüzdeki ay ortak acil durum tatbikatları yapmak üzere belediye itfaiye şefleriyle koordinasyon sağlayacağız."},
        ],
        [
            {
                "question_en": "What primary civic concern was raised by neighborhood associations regarding the microgrid project?",
                "question_tr_hint": "Mikro şebeke projesine ilişkin mahalle dernekleri tarafından hangi temel toplumsal endişe dile getirildi?",
                "correct_answer": "Fire hazards and thermal runaway risks stemming from battery storage near residential heritage zones.",
                "distractors": [
                    "Radioactivity leaks from underground miniature nuclear reactors.",
                    "Excessive dust generated by manual bicycle charging generators.",
                    "Loss of cellular mobile phone signals throughout the historic port."
                ],
                "explanation_en": "Vance notes objections over fire propagation and thermal runaway risks near homes.",
                "explanation_tr": "Vance konut alanlarına yakın bataryalardaki termal kaçak ve yangın riskine dair itirazları belirtir."
            },
            {
                "question_en": "Why does Hande argue lithium iron phosphate (LFP) chemistry offers superior safety?",
                "question_tr_hint": "Hande lityum demir fosfat (LFP) kimyasının neden üstün güvenlik sunduğunu savunuyor?",
                "correct_answer": "LFP chemistry is fundamentally resistant to thermal runaway oxygen release compared to NMC cells.",
                "distractors": [
                    "LFP cells do not conduct electrical current under any circumstances.",
                    "LFP batteries can be completely extinguished with household tap water.",
                    "LFP enclosures operate at absolute zero degrees temperature permanently."
                ],
                "explanation_en": "Hande clarifies LFP is intrinsically resistant to oxygen release during thermal events.",
                "explanation_tr": "Hande LFP kimyasının NMC hücrelerine kıyasla oksijen salınımına dirençli olduğunu açıklar."
            },
            {
                "question_en": "What advanced early-warning detection system is embedded inside each battery module?",
                "question_tr_hint": "Her batarya modülünün içine hangi gelişmiş erken uyarı algılama sistemi yerleştirilmiştir?",
                "correct_answer": "Continuous infrared gas sniffing that detects off-gassing 30 minutes before thermal acceleration.",
                "distractors": [
                    "A human guard standing watch twenty-four hours a day with binoculars.",
                    "A canary bird cage mounted on top of the external transformer.",
                    "An acoustic microphone listening for lightning strikes."
                ],
                "explanation_en": "Hande describes infrared gas sniffing that detects off-gassing 30 minutes prior to events.",
                "explanation_tr": "Hande termal olaylardan 30 dakika önce gaz çıkışını saptayan kızılötesi sensörleri anlatır."
            },
            {
                "question_en": "What civic concessions does Commissioner Vance require before granting the zoning variance?",
                "question_tr_hint": "Komisyon Üyesi Vance imar değişikliğini onaylamadan önce hangi toplumsal tavizleri şart koşuyor?",
                "correct_answer": "A landscaped fifty-foot green buffer and an informational public town hall for neighborhood residents.",
                "distractors": [
                    "Payment of personal cash stipends to every city municipal employee.",
                    "Painting all solar panels in historical Victorian heritage colors.",
                    "Relocating the entire project to an uninhabited desert island."
                ],
                "explanation_en": "Vance requests a 50-foot green buffer and an informational town hall.",
                "explanation_tr": "Vance 50 metrelik peyzajlı yeşil tampon ve halk bilgilendirme toplantısı talep eder."
            },
            {
                "question_en": "What overall grid performance benefit does the decentralized microgrid deliver?",
                "question_tr_hint": "Merkezi olmayan mikro şebeke genel şebeke performansına nasıl bir fayda sağlar?",
                "correct_answer": "A thirty-five percent reduction in peak electricity grid strain across the urban district.",
                "distractors": [
                    "A hundred percent reduction in water consumption across the municipality.",
                    "Free unlimited electricity for every commercial entity in the nation.",
                    "Total eradication of all atmospheric greenhouse gas emissions globally."
                ],
                "explanation_en": "Hande highlights a 35% reduction in peak grid strain.",
                "explanation_tr": "Hande zirve şebeke yükünde %35 azalma sağlanacağını ifade eder."
            }
        ],
        ["society", "urban_planning"]
    ),

    # 12. museum-curation-indigenous-artifacts (culture / arts)
    build_scenario(
        "listening.c1.museum-curation-indigenous-artifacts",
        "Establishing Ethical Repatriation and Provenance Protocols",
        "C1", "stakeholder_alignment",
        "Senior museum curator Jonathan and cultural heritage jurist Serra confer over contested provenance documentation, reciprocal loan frameworks, and ethical restitution guidelines for antiquities.",
        [
            {"id": "jonathan", "name": "Jonathan", "role": "Senior Antiquities Curator", "accent": "British"},
            {"id": "serra", "name": "Serra", "role": "International Cultural Heritage Jurist", "accent": "Turkish"},
        ],
        "c1_museum_curation_indigenous_artifacts.mp3",
        [
            {"speaker_id": "jonathan", "text_en": "Serra, thank you for reviewing our classical Aegean statuary archive. Our board of trustees is committed to navigating provenance inquiries with absolute ethical rigor.", "text_tr": "Serra, klasik Ege heykelleri arşivimizi incelediğin için teşekkür ederim. Mütevelli heyetimiz köken araştırmalarını mutlak etik titizlikle yürütmeye kararlıdır."},
            {"speaker_id": "serra", "text_en": "It is an essential endeavor, Jonathan. The legal landscape has shifted irreversibly from nineteen-seventy UNESCO compliance toward proactive decolonization and bilateral restitution.", "text_tr": "Bu çok önemli bir girişim Jonathan. Hukuki zemin bin dokuz yüz yetmiş UNESCO uyumluluğundan proaktif dekolonizasyon ve ikili iadelere doğru geri döndürülemez biçimde değişti."},
            {"speaker_id": "jonathan", "text_en": "Specifically, we are examining the provenance file for the fourth-century BCE marble votive frieze acquired through a Geneva antiquities dealership in nineteen eighty-four.", "text_tr": "Özellikle, bin dokuz yüz seksen dört yılında bir Cenevre antikacılık bayisi aracılığıyla edinilen MÖ dördüncü yüzyıl mermer adak frizinin köken dosyasını inceliyoruz."},
            {"speaker_id": "serra", "text_en": "The export documentation in that archive relies on a notarized declaration that recent isotopic marble analysis directly contradicts. The strontium isotope ratios map conclusively to an Anatolian quarry site.", "text_tr": "Bu arşivdeki ihracat belgeleri son izotopik mermer analizinin doğrudan çürüttüğü noter tasdikli bir beyana dayanıyor. Stronsiyum izotop oranları kesin olarak bir Anadolu taş ocağı sahasıyla eşleşiyor."},
            {"speaker_id": "jonathan", "text_en": "Which suggests illicit excavation and clandestine smuggling prior to its arrival in Swiss art trade circles.", "text_tr": "Bu da İsviçre sanat ticareti çevrelerine ulaşmadan önce yasadışı kazı ve gizli kaçakçılığa işaret ediyor."},
            {"speaker_id": "serra", "text_en": "Indubitably. A confrontational legal dispute will generate reputational damage. Instead, I recommend proposing a collaborative bilateral cultural heritage partnership with the origin ministry.", "text_tr": "Kuşkusuz. Çatışmacı bir hukuki ihtilaf itibar kaybı yaratacaktır. Bunun yerine, menşe bakanlığıyla işbirlikçi ikili bir kültürel miras ortaklığı önermenizi tavsiye ederim."},
            {"speaker_id": "jonathan", "text_en": "Whereby legal title unconditionally transfers back to the source nation, while the physical frieze remains on reciprocal long-term loan with collaborative conservation exchanges?", "text_tr": "Yani yasal mülkiyet koşulsuz olarak kaynak ülkeye geri devredilirken, fiziki friz işbirlikçi koruma değişimleriyle karşılıklı uzun vadeli ödünçte mi kalacak?"},
            {"speaker_id": "serra", "text_en": "Precisely. That framework converts a historical injustice into an enduring international scholarly alliance.", "text_tr": "Kesinlikle. Bu çerçeve tarihsel bir adaletsizliği kalıcı bir uluslararası akademik ittifaka dönüştürür."},
        ],
        [
            {
                "question_en": "What scientific evidence contradicted the museum's historic acquisition paperwork?",
                "question_tr_hint": "Müzenin tarihi edinim belgeleriyle hangi bilimsel kanıt çelişti?",
                "correct_answer": "Strontium isotope ratios in the marble conclusively matched an Anatolian quarry rather than documented origin.",
                "distractors": [
                    "Carbon dating revealed the statue was constructed from modern synthetic plastic.",
                    "Infrared photography showed signature stamps from an Italian tourist gift shop.",
                    "Microscopic examination revealed machine-tooled CNC cutting patterns."
                ],
                "explanation_en": "Serra highlights that strontium isotope ratios conclusively map to an Anatolian quarry.",
                "explanation_tr": "Serra mermerdeki stronsiyum izotop analizinin eserin Anadolu menşeini kanıtladığını belirtir."
            },
            {
                "question_en": "Why does Serra advise against pursuing a formal court litigation battle?",
                "question_tr_hint": "Serra neden resmi bir mahkeme davası açılmasını tavsiye etmiyor?",
                "correct_answer": "Public litigation creates severe reputational damage compared to collaborative partnership.",
                "distractors": [
                    "International courts have ceased all legal adjudication globally.",
                    "The statue possesses negative spiritual curses that affect courtroom judges.",
                    "Attorneys fees would exceed the entire national gross domestic product."
                ],
                "explanation_en": "Serra warns that a confrontational legal dispute generates severe reputational damage.",
                "explanation_tr": "Serra çatışmacı bir hukuki sürecin ciddi itibar kaybı yaratacağı konusunda uyarır."
            },
            {
                "question_en": "What collaborative solution does Jonathan formulate based on Serra's counsel?",
                "question_tr_hint": "Jonathan Serra'nın tavsiyesi üzerine nasıl bir işbirlikçi çözüm formüle ediyor?",
                "correct_answer": "Transferring full legal ownership to origin authorities while retaining reciprocal loan and conservation exchanges.",
                "distractors": [
                    "Melting the marble frieze down to build museum cafeteria countertops.",
                    "Selling the artwork at an unreserved international auction to highest private bidder.",
                    "Burying the statue in a deep concrete vault to evade public scrutiny."
                ],
                "explanation_en": "Jonathan suggests transferring legal title while maintaining reciprocal long-term loans.",
                "explanation_tr": "Jonathan mülkiyetin devredilip eserin karşılıklı uzun vadeli ödünçte kalmasını önerir."
            },
            {
                "question_en": "In what decade was the controversial marble frieze originally acquired by the museum?",
                "question_tr_hint": "Tartışmalı mermer friz müze tarafından ilk olarak hangi on yılda edinilmişti?",
                "correct_answer": "The nineteen-eighties (specifically 1984).",
                "distractors": [
                    "The nineteen-twenties.",
                    "The early eighteen-hundreds.",
                    "The late nineteen-fifties."
                ],
                "explanation_en": "Jonathan specifies the frieze was acquired in 1984 through a Geneva dealer.",
                "explanation_tr": "Jonathan frizin 1984 yılında bir Cenevre bayisi aracılığıyla alındığını belirtir."
            },
            {
                "question_en": "How has the broader legal and cultural landscape regarding antiquities shifted according to Serra?",
                "question_tr_hint": "Serra'ya göre antik eserlere ilişkin daha geniş hukuki ve kültürel zemin nasıl değişti?",
                "correct_answer": "From mere treaty compliance toward proactive restitution and cultural decolonization.",
                "distractors": [
                    "Toward the complete privatized commercialization of all public heritage.",
                    "Toward banning all international travel for museum curatorial personnel.",
                    "Toward abolishing all scientific provenance testing on cultural antiquities."
                ],
                "explanation_en": "Serra notes the shift from 1970 UNESCO compliance toward proactive decolonization and restitution.",
                "explanation_tr": "Serra UNESCO uyumundan proaktif iade ve dekolonizasyona doğru bir kayma olduğunu açıklar."
            }
        ],
        ["culture", "arts"]
    ),

    # 13. distributed-consensus-split-brain (problem-solving / engineering)
    build_scenario(
        "listening.c1.distributed-consensus-split-brain",
        "Triage and Remediation of Distributed Raft Split-Brain Partition",
        "C1", "engineering_meeting",
        "Site Reliability Director Chloe and Distributed Database Architect Kerem examine asynchronous network partition logs to verify Paxos/Raft leader lease invariants and eliminate phantom writes.",
        [
            {"id": "chloe", "name": "Chloe", "role": "Site Reliability Director", "accent": "American"},
            {"id": "kerem", "name": "Kerem", "role": "Distributed Systems Architect", "accent": "Turkish"},
        ],
        "c1_distributed_consensus_split_brain.mp3",
        [
            {"speaker_id": "chloe", "text_en": "Kerem, during our transatlantic network fiber flap at twenty past eleven last night, our multi-region database cluster experienced a partial split-brain state.", "text_tr": "Kerem, dün gece saat on biri yirmi geçe yaşanan transatlantik ağ fiber dalgalanması sırasında, çok bölgeli veritabanı kümemiz kısmi bir bölünmüş beyin durumu yaşadı."},
            {"speaker_id": "kerem", "text_en": "I analyzed the Raft consensus state machine logs, Chloe. Both the European and North American regions elected independent leader nodes concurrently for approximately twelve seconds.", "text_tr": "Raft mutabakat durum makinesi günlüklerini inceledim Chloe. Hem Avrupa hem de Kuzey Amerika bölgeleri yaklaşık on iki saniye boyunca eşzamanlı olarak bağımsız lider düğümler seçti."},
            {"speaker_id": "chloe", "text_en": "How could two leaders be elected simultaneously under strict majority quorum consensus across our five-node cluster?", "text_tr": "Beş düğümlü kümemizde kesin çoğunluk nisap mutabakatı altındayken nasıl iki lider aynı anda seçilebildi?"},
            {"speaker_id": "kerem", "text_en": "The root cause was clock drift across AWS regions interacting with our leader-lease renewal mechanism. Node three suffered a six-hundred-millisecond NTP desynchronization.", "text_tr": "Kök neden, lider kiralama yenileme mekanizmamızla etkileşime giren AWS bölgeleri arasındaki saat kaymasıydı. Üçüncü düğümde altı yüz milisaniyelik bir NTP senkronizasyon kaybı yaşandı."},
            {"speaker_id": "chloe", "text_en": "Causing node three to consider its leadership lease valid while the overseas majority quorum had already timed out and initiated a new term election.", "text_tr": "Bu da yurt dışındaki çoğunluk nisabının zaman aşımına uğrayıp yeni bir dönem seçimi başlatmasına rağmen üçüncü düğümün liderlik kiralamasını geçerli saymasına neden oldu."},
            {"speaker_id": "kerem", "text_en": "Exactly. During that twelve-second window, Node three accepted approximately four hundred write transactions that were never replicated to the global majority log.", "text_tr": "Kesinlikle. Bu on iki saniyelik pencere sırasında, Üçüncü Düğüm küresel çoğunluk günlüğüne hiçbir zaman kopyalanmayan yaklaşık dört yüz yazma işlemini kabul etti."},
            {"speaker_id": "chloe", "text_en": "Did those unreplicated writes result in dirty phantom reads or silent data corruption for enterprise banking tenants?", "text_tr": "Bu kopyalanmamış yazmalar kurumsal bankacılık kiracıları için kirli hayali okumalara veya sessiz veri bozulmalarına yol açtı mı?"},
            {"speaker_id": "kerem", "text_en": "Fortunately, our multi-version concurrency control flagged the uncommitted terms, and write-ahead transaction logs rolled back cleanly without data loss. We are now mandating monotonic TrueTime GPS hardware references.", "text_tr": "Neyse ki, çok sürümlü eşzamanlılık kontrolümüz kesinleşmemiş dönemleri işaretledi ve işlem öncesi yazma günlükleri veri kaybı olmaksızın temiz bir şekilde geri alındı. Artık monoton TrueTime GPS donanım referanslarını zorunlu kılıyoruz."},
        ],
        [
            {
                "question_en": "What underlying physical defect triggered concurrent leader elections across regions?",
                "question_tr_hint": "Bölgeler arasında eşzamanlı lider seçimlerini hangi temel fiziksel kusur tetikledi?",
                "correct_answer": "Clock drift and a 600ms NTP desynchronization colliding with the leader-lease interval.",
                "distractors": [
                    "A power outage disabling all five server motherboards at once.",
                    "Malicious ransom software corrupting all disk partitioning tables.",
                    "An unauthorized firmware update deleting the consensus source code."
                ],
                "explanation_en": "Kerem identifies clock drift and a 600ms NTP desynchronization on node 3 as the root cause.",
                "explanation_tr": "Kerem kök nedenin saat kayması ve 3. düğümdeki 600 ms'lik NTP gecikmesi olduğunu belirtir."
            },
            {
                "question_en": "How long did the split-brain dual-leader condition persist during the network flap?",
                "question_tr_hint": "Ağ dalgalanması sırasında çift liderli bölünmüş beyin durumu ne kadar sürdü?",
                "correct_answer": "Approximately twelve seconds.",
                "distractors": [
                    "Over three full calendar weeks.",
                    "Less than one nanosecond.",
                    "Exactly twenty-four hours."
                ],
                "explanation_en": "Kerem states both regions elected independent leaders for approximately 12 seconds.",
                "explanation_tr": "Kerem bağımsız lider durumunun yaklaşık 12 saniye sürdüğünü söyler."
            },
            {
                "question_en": "What happened to the four hundred write transactions accepted by the disconnected leader node?",
                "question_tr_hint": "Bağlantısı kesilen lider düğüm tarafından kabul edilen dört yüz yazma işlemine ne oldu?",
                "correct_answer": "They were identified by multi-version concurrency control and cleanly rolled back without corruption.",
                "distractors": [
                    "They were permanently published to international public newspapers.",
                    "They resulted in the immediate bankruptcy of fifty multinational banks.",
                    "They overwrote the entire operating system of every customer device."
                ],
                "explanation_en": "Kerem reports MVCC flagged uncommitted terms and transaction logs rolled back cleanly.",
                "explanation_tr": "Kerem MVCC kontrolünün işlemleri yakalayıp veri kaybı olmadan temizce geri aldığını belirtir."
            },
            {
                "question_en": "What permanent hardware and architectural safeguard does Kerem mandate to prevent recurrence?",
                "question_tr_hint": "Kerem tekrarı önlemek için hangi kalıcı donanım ve mimari güvenlik önlemini zorunlu kılıyor?",
                "correct_answer": "Mandating monotonic TrueTime GPS hardware time references across all cluster datacenters.",
                "distractors": [
                    "Using mechanical grandfather clocks inside datacenter server halls.",
                    "Requiring software engineers to manually approve every database query.",
                    "Eliminating all automated failover algorithms in favor of manual rebooting."
                ],
                "explanation_en": "Kerem specifies mandating monotonic TrueTime GPS hardware references.",
                "explanation_tr": "Kerem TrueTime GPS donanım saat referanslarının zorunlu kılınacağını açıklar."
            },
            {
                "question_en": "How many total nodes comprise the consensus cluster under review?",
                "question_tr_hint": "İnceleme altındaki mutabakat kümesi toplam kaç düğümden oluşmaktadır?",
                "correct_answer": "Five nodes operating under majority quorum.",
                "distractors": [
                    "Two hundred thousand individual server nodes.",
                    "A single solitary mainframe server node.",
                    "Exactly two interconnected server nodes."
                ],
                "explanation_en": "Chloe refers to strict majority quorum across our five-node cluster.",
                "explanation_tr": "Chloe beş düğümlü kümede kesin çoğunluk nisabından bahseder."
            }
        ],
        ["problem_solving", "engineering"]
    ),
]
