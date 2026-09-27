#!/usr/bin/env python3
"""
Listening Batch 002: B1 Scenarios (9 scenarios).
"""

from listening_builder import build_scenario

SCENARIOS_B1 = [
    # 1. bike-repair-shop (daily-life)
    build_scenario(
        "listening.b1.bike-repair-shop",
        "Diagnosing a Bicycle Drivetrain Problem",
        "B1", "product_discovery",
        "Can brings his commuter bicycle to a neighborhood cycle shop after hearing grinding sounds in the drivetrain, consulting mechanic Sam.",
        [
            {"id": "sam", "name": "Sam", "role": "Bicycle Mechanic", "accent": "British"},
            {"id": "can", "name": "Can", "role": "Cyclist", "accent": "Turkish"},
        ],
        "b1_bike_repair_shop.mp3",
        [
            {"speaker_id": "sam", "text_en": "Good afternoon! What seems to be the problem with your commuter bike today?", "text_tr": "Tünaydın! Bugün şehir bisikletinizle ilgili sorun nedir?"},
            {"speaker_id": "can", "text_en": "Hi Sam. Whenever I pedal uphill or shift into higher gears, there is a metallic grinding sound near the rear wheel.", "text_tr": "Merhaba Sam. Ne zaman yokuş yukarı pedal çevirsem ya da yüksek vitese geçsem arka tekerleğin yakınından metalik bir sürtünme sesi geliyor."},
            {"speaker_id": "sam", "text_en": "Let me put it on the repair stand and check the rear derailleur alignment. Ah, your chain is significantly stretched.", "text_tr": "Tamir sehpasına koyup arka aktarıcı hizalamasını kontrol edeyim. Ah, zinciriniz belirgin şekilde esnemiş."},
            {"speaker_id": "can", "text_en": "Does a stretched chain damage the cassette cogs as well?", "text_tr": "Esnemiş bir zincir kaset dişlilerine de zarar verir mi?"},
            {"speaker_id": "sam", "text_en": "Yes, if you ride with a worn chain, the teeth on the cassette wear down unevenly. Fortunately, your cassette still looks serviceable.", "text_tr": "Evet, aşınmış bir zincirle sürerseniz kasetteki dişler dengesiz aşınır. Neyse ki kasetiniz hala kullanılabilir durumda görünüyor."},
            {"speaker_id": "can", "text_en": "That is a relief. How much would a replacement nine-speed chain and basic tune-up cost?", "text_tr": "Bu rahatlatıcı. Dokuz vitesli yeni bir zincir ve temel bakım ne kadara mal olur?"},
            {"speaker_id": "sam", "text_en": "A durable nickel-plated chain is twenty-five pounds, and labor for the safety inspection and indexing is twenty pounds.", "text_tr": "Dayanıklı nikel kaplı bir zincir yirmi beş sterlin, güvenlik denetimi ve vites ayarı işçiliği ise yirmi sterlindir."},
            {"speaker_id": "can", "text_en": "Sounds great. Could I pick it up tomorrow afternoon before my evening commute?", "text_tr": "Harika. Yarın öğleden sonra akşam iş çıkışı yolculuğumdan önce alabilir miyim?"},
            {"speaker_id": "sam", "text_en": "Absolutely. I will have it fully tuned and test-ridden by three o'clock tomorrow.", "text_tr": "Kesinlikle. Yarın saat üçe kadar tamamen ayarlanmış ve test edilmiş olarak hazır ederim."},
        ],
        [
            {
                "question_en": "What specific noise prompted Can to bring his bicycle to the shop?",
                "question_tr_hint": "Can'ın bisikletini dükkana getirmesine hangi ses neden oldu?",
                "correct_answer": "A metallic grinding sound near the rear wheel when pedaling uphill.",
                "distractors": [
                    "A high-pitched squealing from the front hydraulic brakes.",
                    "An air leakage hissing from the front tire tube.",
                    "A loud clicking from the handlebars during turns."
                ],
                "explanation_en": "Can explains that he hears metallic grinding near the rear wheel under load.",
                "explanation_tr": "Can yük altındayken arka tekerlek yakınında metalik bir sürtünme duyduğunu açıklar."
            },
            {
                "question_en": "What primary mechanical issue does Sam discover upon inspection?",
                "question_tr_hint": "Sam inceleme sırasında hangi temel mekanik sorunu keşfediyor?",
                "correct_answer": "The bicycle chain is significantly stretched from wear.",
                "distractors": [
                    "The rear wheel frame is completely cracked.",
                    "The pedal crank arm has fallen off.",
                    "The hydraulic brake fluid has completely leaked out."
                ],
                "explanation_en": "Sam discovers that the chain is significantly stretched.",
                "explanation_tr": "Sam zincirin belirgin şekilde esnediğini keşfeder."
            },
            {
                "question_en": "What is the condition of the rear cassette?",
                "question_tr_hint": "Arka kaset dişlisinin durumu nasıldır?",
                "correct_answer": "It is still serviceable and does not need immediate replacement.",
                "distractors": [
                    "It is completely stripped and dangerously broken.",
                    "It has completely rusted through from rainwater.",
                    "It was missing three critical mounting bolts."
                ],
                "explanation_en": "Sam confirms the cassette still looks serviceable.",
                "explanation_tr": "Sam kasetin hala kullanılabilir durumda olduğunu doğrular."
            },
            {
                "question_en": "What is the total estimated cost for the replacement chain and labor?",
                "question_tr_hint": "Değiştirilen zincir ve işçilik için tahmini toplam maliyet nedir?",
                "correct_answer": "Forty-five pounds.",
                "distractors": [
                    "Twenty pounds.",
                    "One hundred pounds.",
                    "Seventy-five pounds."
                ],
                "explanation_en": "Twenty-five pounds for chain plus twenty pounds for labor equals 45 pounds.",
                "explanation_tr": "25 sterlin zincir ve 20 sterlin işçilik toplamda 45 sterlin eder."
            },
            {
                "question_en": "When will the bicycle be ready for pickup?",
                "question_tr_hint": "Bisiklet ne zaman teslim alınmaya hazır olacak?",
                "correct_answer": "By 3:00 PM tomorrow afternoon.",
                "distractors": [
                    "Late next week on Friday evening.",
                    "Immediately within ten minutes.",
                    "In one month after replacement parts arrive."
                ],
                "explanation_en": "Sam promises it will be tuned and tested by 3:00 PM tomorrow.",
                "explanation_tr": "Sam yarın saat 15:00'e kadar ayarlanıp test edilmiş olacağını taahhüt eder."
            }
        ],
        ["daily-life", "transportation", "bicycle", "repair"]
    ),

    # 2. home-broadband-upgrade (daily-life)
    build_scenario(
        "listening.b1.home-broadband-upgrade",
        "Troubleshooting Home Internet and Upgrading to Fiber",
        "B1", "incident_response",
        "Merve calls customer technical support to resolve frequent home Wi-Fi disconnects and explores upgrading to a fiber optic connection with technician David.",
        [
            {"id": "david", "name": "David", "role": "Technical Support Specialist", "accent": "American"},
            {"id": "merve", "name": "Merve", "role": "Broadband Customer", "accent": "Turkish"},
        ],
        "b1_home_broadband_upgrade.mp3",
        [
            {"speaker_id": "david", "text_en": "Thank you for calling Apex Broadband support. My name is David. How can I help you?", "text_tr": "Apex Genişbant desteğini aradığınız için teşekkürler. Benim adım David. Size nasıl yardımcı olabilirim?"},
            {"speaker_id": "merve", "text_en": "Hi David. My home internet connection keeps dropping during critical video meetings for work. The router lights turn amber.", "text_tr": "Merhaba David. Ev internet bağlantım iş için yaptığım kritik video toplantıları sırasında sürekli kopuyor. Yönlendirici ışıkları turuncuya dönüyor."},
            {"speaker_id": "david", "text_en": "I can run a remote line diagnostic right now. Yes, I see significant packet loss and latency spikes on your copper DSL line.", "text_tr": "Hemen uzaktan bir hat tanılama testi çalıştırabilirim. Evet, bakır DSL hattınızda belirgin paket kaybı ve gecikme sıçramaları görüyorum."},
            {"speaker_id": "merve", "text_en": "Is the issue with my internal wireless router or the physical neighborhood line?", "text_tr": "Sorun benim dahili kablosuz yönlendiricimde mi yoksa mahalledeki fiziksel hatta mı?"},
            {"speaker_id": "david", "text_en": "It is electrical interference on the legacy copper line from the street cabinet. However, optical fiber was installed in your street last month.", "text_tr": "Sokak panosundan gelen eski bakır hattaki elektriksel parazitten kaynaklanıyor. Ancak geçen ay sokağınıza optik fiber döşendi."},
            {"speaker_id": "merve", "text_en": "What speeds and pricing would a full fiber optic upgrade offer?", "text_tr": "Tam bir fiber optik yükseltme hangi hızları ve fiyatlandırmayı sunar?"},
            {"speaker_id": "david", "text_en": "Our 500-megabit symmetrical fiber plan is forty-five dollars a month, which is actually five dollars cheaper than your current legacy plan.", "text_tr": "500 megabitlik simetrik fiber planımız ayda kırk beş dolar, bu da aslında mevcut eski planınızdan beş dolar daha ucuz."},
            {"speaker_id": "merve", "text_en": "That is fantastic. How soon can a field technician install the optical termination box?", "text_tr": "Bu harika. Bir saha teknisyeni optik sonlandırma kutusunu ne kadar sürede kurabilir?"},
            {"speaker_id": "david", "text_en": "We have an open installation slot this Thursday morning between nine and eleven. I will book that for you right away.", "text_tr": "Bu perşembe sabahı dokuz ile on bir arasında açık bir kurulum randevumuz var. Sizin için hemen ayırtıyorum."},
        ],
        [
            {
                "question_en": "What symptom alerts Merve that her internet connection has dropped?",
                "question_tr_hint": "Merve'yi internet bağlantısının koptuğuna dair hangi belirti uyarıyor?",
                "correct_answer": "Her router lights turn amber and video calls freeze.",
                "distractors": [
                    "Her laptop computer automatically shuts down.",
                    "A loud siren sounds from the wall outlet.",
                    "The television screen turns completely purple."
                ],
                "explanation_en": "Merve states that her router lights turn amber during video meeting drops.",
                "explanation_tr": "Merve video toplantıları koparken yönlendirici ışıklarının turuncuya döndüğünü belirtir."
            },
            {
                "question_en": "What is the technical root cause of Merve's connectivity issues?",
                "question_tr_hint": "Merve'nin bağlantı sorunlarının teknik kök nedeni nedir?",
                "correct_answer": "Electrical interference on the legacy copper DSL line from the street cabinet.",
                "distractors": [
                    "A malicious virus infecting Merve's mobile smartphone.",
                    "Underground earthquake tremors severing submarine cables.",
                    "Merve exceeding her monthly data download ceiling."
                ],
                "explanation_en": "David explains it is electrical interference on the legacy copper line.",
                "explanation_tr": "David eski bakır hattaki elektriksel parazitten kaynaklandığını açıklar."
            },
            {
                "question_en": "What infrastructure improvement was recently completed on Merve's street?",
                "question_tr_hint": "Merve'nin sokağında yakın zamanda hangi altyapı iyileştirmesi tamamlandı?",
                "correct_answer": "Optical fiber was installed last month.",
                "distractors": [
                    "A new satellite communications tower was built.",
                    "All telephone wires were completely removed.",
                    "A massive electrical power plant was constructed."
                ],
                "explanation_en": "David notes that optical fiber was installed in her street last month.",
                "explanation_tr": "David sokağa geçen ay optik fiber döşendiğini belirtir."
            },
            {
                "question_en": "How does the price of the 500-megabit fiber plan compare to Merve's current bill?",
                "question_tr_hint": "500 megabitlik fiber planın fiyatı Merve'nin mevcut faturasıyla nasıl karşılaştırılır?",
                "correct_answer": "It is forty-five dollars a month, which is five dollars cheaper.",
                "distractors": [
                    "It is fifty dollars more expensive each month.",
                    "It costs exactly double the price of the copper plan.",
                    "It requires paying one thousand dollars upfront."
                ],
                "explanation_en": "David explains it is $45/month, which is five dollars cheaper than her current plan.",
                "explanation_tr": "David ayda 45 dolar olduğunu ve mevcut plandan 5 dolar daha ucuz olduğunu açıklar."
            },
            {
                "question_en": "When is the technician scheduled to install the fiber box?",
                "question_tr_hint": "Teknisyenin fiber kutusunu kurması ne zamana planlanıyor?",
                "correct_answer": "This Thursday morning between 9:00 and 11:00 AM.",
                "distractors": [
                    "Late on Sunday evening after dinner.",
                    "Next month on the final business day.",
                    "In six weeks after city council approval."
                ],
                "explanation_en": "David books the installation slot for this Thursday morning between 9 and 11.",
                "explanation_tr": "David kurulumu bu perşembe sabahı 9 ile 11 arasına kaydeder."
            }
        ],
        ["daily-life", "technology", "internet", "support"]
    ),

    # 3. airport-lost-luggage (travel)
    build_scenario(
        "listening.b1.airport-lost-luggage",
        "Filing a Lost Baggage Claim at Dublin Airport",
        "B1", "incident_response",
        "Tolga speaks with an airline baggage recovery agent, Brenda, after his checked suitcase fails to appear on the carousel following an international flight.",
        [
            {"id": "brenda", "name": "Brenda", "role": "Baggage Services Agent", "accent": "Irish"},
            {"id": "tolga", "name": "Tolga", "role": "Arriving Passenger", "accent": "Turkish"},
        ],
        "b1_airport_lost_luggage.mp3",
        [
            {"speaker_id": "brenda", "text_en": "Hello sir, welcome to Dublin Baggage Enquiries. How can I assist you?", "text_tr": "Merhaba efendim, Dublin Bagaj Danışma'ya hoş geldiniz. Size nasıl yardımcı olabilirim?"},
            {"speaker_id": "tolga", "text_en": "Hi Brenda. I just arrived on flight EI 452 from Frankfurt. The carousel stopped, but my large checked suitcase never arrived.", "text_tr": "Merhaba Brenda. Frankfurt'tan gelen EI 452 sefer sayılı uçuşla az önce indim. Bagaj bandı durdu ancak büyük teslim edilmiş valizim hiç gelmedi."},
            {"speaker_id": "brenda", "text_en": "I am very sorry about that. Could you hand me your boarding pass with the baggage receipt barcode attached?", "text_tr": "Bunun için çok üzgünüm. Bagaj fişi barkodunun ekli olduğu biniş kartınızı bana uzatabilir misiniz?"},
            {"speaker_id": "tolga", "text_en": "Here it is. The tag number is TK 89204.", "text_tr": "İşte burada. Etiket numarası TK 89204."},
            {"speaker_id": "brenda", "text_en": "Thank you. Let me check the global baggage tracking database. Ah, your bag missed the tight connection in Frankfurt due to the late transfer gate.", "text_tr": "Teşekkürler. Küresel bagaj takip veritabanını kontrol edeyim. Ah, valiziniz Frankfurt'taki geç transfer kapısı nedeniyle dar bağlantıyı kaçırmış."},
            {"speaker_id": "tolga", "text_en": "Where is the suitcase now, and when can I expect to receive it?", "text_tr": "Valiz şu anda nerede ve ne zaman teslim almayı bekleyebilirim?"},
            {"speaker_id": "brenda", "text_en": "It has already been scanned onto the next evening flight, arriving here at eight tonight. A courier will deliver it directly to your hotel tomorrow morning.", "text_tr": "Bu akşam sekizde buraya varacak olan bir sonraki akşam uçuşuna çoktan tarandı. Bir kurye yarın sabah doğrudan otelinize teslim edecek."},
            {"speaker_id": "tolga", "text_en": "That is good news. Do I receive emergency funds to buy essential toiletries tonight?", "text_tr": "Bu iyi haber. Bu gece temel tuvalet malzemeleri satın almak için acil durum ödeneği alıyor muyum?"},
            {"speaker_id": "brenda", "text_en": "Yes, you can claim up to seventy-five euros for essential hygiene items with valid store receipts. Here is your reference file number.", "text_tr": "Evet, geçerli mağaza fişleriyle temel hijyen ürünleri için yetmiş beş avroya kadar talepte bulunabilirsiniz. İşte referans dosya numaranız."},
        ],
        [
            {
                "question_en": "On which flight did Tolga arrive in Dublin?",
                "question_tr_hint": "Tolga Dublin'e hangi uçuşla vardı?",
                "correct_answer": "Flight EI 452 from Frankfurt.",
                "distractors": [
                    "Flight BA 101 from London Heathrow.",
                    "Flight TK 200 from Istanbul Atatürk.",
                    "Flight AF 789 from Paris Charles de Gaulle."
                ],
                "explanation_en": "Tolga explicitly mentions arriving on flight EI 452 from Frankfurt.",
                "explanation_tr": "Tolga Frankfurt'tan EI 452 uçuşuyla geldiğini açıkça belirtir."
            },
            {
                "question_en": "Why did Tolga's suitcase fail to arrive on the carousel?",
                "question_tr_hint": "Tolga'nın valizi bagaj bandına neden ulaşamadı?",
                "correct_answer": "It missed a tight connection in Frankfurt due to a late transfer.",
                "distractors": [
                    "It was confiscated by border customs agents.",
                    "The suitcase was physically destroyed by airport machinery.",
                    "It fell out of the airplane cargo door in flight."
                ],
                "explanation_en": "Brenda explains the bag missed the tight connection in Frankfurt.",
                "explanation_tr": "Brenda valizin Frankfurt'taki dar bağlantıyı kaçırdığını açıklar."
            },
            {
                "question_en": "When will the missing suitcase arrive in Dublin?",
                "question_tr_hint": "Kayıp valiz Dublin'e ne zaman varacak?",
                "correct_answer": "At 8:00 PM tonight on the next evening flight.",
                "distractors": [
                    "In two weeks after customs clearance.",
                    "At midnight next Saturday.",
                    "Never; it is permanently lost."
                ],
                "explanation_en": "Brenda states it was scanned onto the next flight arriving at 8:00 PM tonight.",
                "explanation_tr": "Brenda bu akşam 20:00'de varacak sonraki uçuşa yüklendiğini belirtir."
            },
            {
                "question_en": "How will the suitcase be delivered to Tolga?",
                "question_tr_hint": "Valiz Tolga'ya nasıl teslim edilecek?",
                "correct_answer": "A courier will deliver it directly to his hotel tomorrow morning.",
                "distractors": [
                    "Tolga must return to the airport and search the basement.",
                    "It will be sent via national postal envelope.",
                    "He must collect it from the Frankfurt train terminal."
                ],
                "explanation_en": "Brenda confirms a courier will deliver it directly to his hotel tomorrow morning.",
                "explanation_tr": "Brenda bir kuryenin yarın sabah doğrudan otele teslim edeceğini onaylar."
            },
            {
                "question_en": "What compensation does Brenda offer for essential hygiene items?",
                "question_tr_hint": "Brenda temel hijyen ürünleri için nasıl bir tazminat sunuyor?",
                "correct_answer": "Up to seventy-five euros with valid store receipts.",
                "distractors": [
                    "One thousand euros in cash immediately.",
                    "A free business-class ticket for his next journey.",
                    "A box of sample soaps from the airline lounge."
                ],
                "explanation_en": "Brenda explains he can claim up to 75 euros for essential items with receipts.",
                "explanation_tr": "Brenda fişlerle 75 avroya kadar talepte bulunabileceğini açıklar."
            }
        ],
        ["travel", "airport", "baggage", "lost-and-found"]
    ),

    # 4. gym-membership-fitness (health-lifestyle)
    build_scenario(
        "listening.b1.gym-membership-fitness",
        "Consulting a Personal Trainer at a Fitness Center",
        "B1", "product_discovery",
        "Yasemin visits a health club to discuss creating a sustainable workout routine to counteract sedentary desk work with personal trainer Marcus.",
        [
            {"id": "marcus", "name": "Marcus", "role": "Senior Fitness Coach", "accent": "American"},
            {"id": "yasemin", "name": "Yasemin", "role": "New Club Member", "accent": "Turkish"},
        ],
        "b1_gym_membership_fitness.mp3",
        [
            {"speaker_id": "marcus", "text_en": "Welcome to Peak Fitness Yasemin! What fitness goals are you hoping to achieve over the next six months?", "text_tr": "Peak Fitness'a hoş geldin Yasemin! Önümüzdeki altı ay içinde hangi fitness hedeflerine ulaşmayı umuyorsun?"},
            {"speaker_id": "yasemin", "text_en": "Hi Marcus. As a remote software engineer, I spend ten hours a day sitting. I have lower back stiffness and low cardiovascular stamina.", "text_tr": "Merhaba Marcus. Uzaktan çalışan bir yazılım mühendisi olarak günde on saat oturuyorum. Bel tutulmam ve düşük kardiyovasküler dayanıklılığım var."},
            {"speaker_id": "marcus", "text_en": "That is very common with desk professionals. We should prioritize posterior chain strengthening and functional core mobility.", "text_tr": "Masa başı profesyonellerinde bu çok yaygındır. Arka zincir güçlendirmesine ve fonksiyonel merkez bölgesi hareketliliğine öncelik vermeliyiz."},
            {"speaker_id": "yasemin", "text_en": "I am worried about lifting heavy barbells because I have never done weight training before.", "text_tr": "Ağır halter kaldırma konusunda endişeliyim çünkü daha önce hiç ağırlık antrenmanı yapmadım."},
            {"speaker_id": "marcus", "text_en": "Do not worry. We start with bodyweight movement mechanics, resistance bands, and kettlebell goblet squats to establish solid posture.", "text_tr": "Endişelenme. Sağlam bir duruş oluşturmak için vücut ağırlığı hareket mekaniği, direnç bantları ve kettlebell goblet squat ile başlıyoruz."},
            {"speaker_id": "yasemin", "text_en": "How many days per week would you recommend for an effective but manageable commitment?", "text_tr": "Etkili ancak yönetilebilir bir taahhüt için haftada kaç gün önerirsiniz?"},
            {"speaker_id": "marcus", "text_en": "Three forty-five-minute sessions per week is optimal. It builds steady neuromuscular adaptation without triggering systemic central nervous fatigue.", "text_tr": "Haftada kırk beş dakikalık üç seans optimaldir. Merkezi sinir sistemi yorgunluğunu tetiklemeden istikrarlı nöromüsküler adaptasyon oluşturur."},
            {"speaker_id": "yasemin", "text_en": "Does the premium membership include access to the heated recovery hydrotherapy pool?", "text_tr": "Premium üyelik ısıtmalı toparlanma hidroterapi havuzuna erişimi içeriyor mu?"},
            {"speaker_id": "marcus", "text_en": "Yes, our Gold membership includes unlimited gym access, all group classes, the sauna, and the hydrotherapy pool.", "text_tr": "Evet, Gold üyeliğimiz sınırsız spor salonu erişimini, tüm grup derslerini, saunayı ve hidroterapi havuzunu içerir."},
        ],
        [
            {
                "question_en": "What lifestyle factor contributes to Yasemin's physical stiffness?",
                "question_tr_hint": "Yasemin'in fiziksel tutulmasına hangi yaşam tarzı faktörü katkıda bulunuyor?",
                "correct_answer": "Sitting for ten hours a day as a remote software engineer.",
                "distractors": [
                    "Training for competitive marathon running.",
                    "Heavy manual construction labor on building sites.",
                    "Lifting heavy boxes in a commercial warehouse."
                ],
                "explanation_en": "Yasemin mentions sitting ten hours a day as a remote software engineer.",
                "explanation_tr": "Yasemin uzaktan yazılım mühendisi olarak günde 10 saat oturduğunu belirtir."
            },
            {
                "question_en": "Why is Yasemin hesitant about traditional weight training?",
                "question_tr_hint": "Yasemin geleneksel ağırlık antrenmanı konusunda neden tereddütlü?",
                "correct_answer": "She has never lifted weights before and fears heavy barbells.",
                "distractors": [
                    "Her doctor forbade any physical movement.",
                    "She believes exercise machines are unhygienic.",
                    "She only wants to participate in competitive boxing."
                ],
                "explanation_en": "Yasemin expresses concern because she has never done weight training before.",
                "explanation_tr": "Yasemin daha önce hiç ağırlık çalışmadığı için endişesini dile getirir."
            },
            {
                "question_en": "What exercises does Marcus recommend for starting out safely?",
                "question_tr_hint": "Marcus güvenli bir başlangıç için hangi egzersizleri öneriyor?",
                "correct_answer": "Bodyweight movement mechanics, resistance bands, and kettlebell squats.",
                "distractors": [
                    "Maximal heavy Olympic deadlifts.",
                    "Running twenty miles on a steep treadmill.",
                    "High-altitude competitive mountain climbing."
                ],
                "explanation_en": "Marcus suggests bodyweight mechanics, resistance bands, and kettlebell squats.",
                "explanation_tr": "Marcus vücut ağırlığı, direnç bantları ve kettlebell squat önerir."
            },
            {
                "question_en": "What weekly training schedule does Marcus recommend?",
                "question_tr_hint": "Marcus haftalık nasıl bir antrenman programı öneriyor?",
                "correct_answer": "Three forty-five-minute sessions per week.",
                "distractors": [
                    "Seven days a week for three hours daily.",
                    "Once a month for an entire weekend.",
                    "Only on early Monday mornings."
                ],
                "explanation_en": "Marcus recommends three 45-minute sessions per week.",
                "explanation_tr": "Marcus haftada üç kez 45 dakikalık seanslar önerir."
            },
            {
                "question_en": "What recovery amenities are included in the Gold membership tier?",
                "question_tr_hint": "Gold üyelik kademesinde hangi toparlanma olanakları yer alıyor?",
                "correct_answer": "Access to the sauna and heated hydrotherapy pool.",
                "distractors": [
                    "A private helicopter ride to the mountains.",
                    "Free sports car rentals on weekends.",
                    "Complimentary hotel stays in tropical resorts."
                ],
                "explanation_en": "Marcus confirms Gold includes the sauna and hydrotherapy pool.",
                "explanation_tr": "Marcus Gold üyeliğin sauna ve hidroterapi havuzunu içerdiğini onaylar."
            }
        ],
        ["health-lifestyle", "fitness", "wellness", "exercise"]
    ),

    # 5. restaurant-table-reservation (food-shopping)
    build_scenario(
        "listening.b1.restaurant-table-reservation",
        "Arranging a Corporate Team Dinner Reservation",
        "B1", "stakeholder_alignment",
        "Emre phones an Italian bistro host, Laura, to book a private dining table for ten colleagues and accommodate specific dietary requirements.",
        [
            {"id": "laura", "name": "Laura", "role": "Restaurant Maitre d'", "accent": "Italian"},
            {"id": "emre", "name": "Emre", "role": "Team Lead", "accent": "Turkish"},
        ],
        "b1_restaurant_table_reservation.mp3",
        [
            {"speaker_id": "laura", "text_en": "Buonasera, Trattoria Bella Vista. Laura speaking. How can I help you?", "text_tr": "İyi akşamlar, Trattoria Bella Vista. Ben Laura. Size nasıl yardımcı olabilirim?"},
            {"speaker_id": "emre", "text_en": "Hello Laura. I would like to reserve a table for a party of ten colleagues this coming Friday evening at seven thirty.", "text_tr": "Merhaba Laura. Bu cuma akşamı saat yedi buçukta on kişilik bir meslektaş grubu için masa ayırtmak istiyorum."},
            {"speaker_id": "laura", "text_en": "Friday is quite busy, but we have a lovely semi-private alcove in the garden room available at seven thirty.", "text_tr": "Cuma oldukça yoğun, ancak saat yedi buçukta bahçe odasında çok hoş yarı özel bir bölmemiz müsait."},
            {"speaker_id": "emre", "text_en": "That would be wonderful. Two of our colleagues have severe gluten allergies, and one is strictly vegan.", "text_tr": "Bu harika olur. İki meslektaşımızın ciddi glüten alerjisi var ve biri kesinlikle vegan."},
            {"speaker_id": "laura", "text_en": "That is no problem at all. Our chef prepares fresh gluten-free handmade pasta in a dedicated kitchen station, and we have an extensive plant-based menu.", "text_tr": "Bu kesinlikle sorun değil. Şefimiz özel bir mutfak istasyonunda taze glütensiz el yapımı makarna hazırlıyor ve kapsamlı bir bitki bazlı menümüz var."},
            {"speaker_id": "emre", "text_en": "Excellent. Can we bring our own celebratory company anniversary cake for dessert?", "text_tr": "Mükemmel. Tatlı için kendi kutlama şirket yıldönümü pastamızı getirebilir miyiz?"},
            {"speaker_id": "laura", "text_en": "Yes, you may. We charge a modest plating fee of two euros per person to slice and serve outside cakes.", "text_tr": "Evet, getirebilirsiniz. Dışarıdan gelen pastaları dilimleyip servis etmek için kişi başı iki avroluk mütevazı bir servis ücreti alıyoruz."},
            {"speaker_id": "emre", "text_en": "That is completely reasonable. Please confirm the reservation under Emre Kaya from FinTech Solutions.", "text_tr": "Bu son derece makul. Lütfen FinTech Solutions'tan Emre Kaya adına rezervasyonu onaylayın."},
        ],
        [
            {
                "question_en": "For how many people is Emre making the dinner reservation?",
                "question_tr_hint": "Emre kaç kişi için akşam yemeği rezervasyonu yapıyor?",
                "correct_answer": "Ten colleagues.",
                "distractors": [
                    "Two people for a romantic dinner.",
                    "Fifty wedding banquet guests.",
                    "Four family members."
                ],
                "explanation_en": "Emre explicitly requests a table for a party of ten colleagues.",
                "explanation_tr": "Emre on kişilik bir meslektaş grubu için masa talep ettiğini açıkça belirtir."
            },
            {
                "question_en": "Where is the reserved table located inside the restaurant?",
                "question_tr_hint": "Rezerve edilen masa restoranın neresinde yer alıyor?",
                "correct_answer": "In a semi-private alcove in the garden room.",
                "distractors": [
                    "Next to the noisy dishwashing station.",
                    "Directly in front of the main entrance door.",
                    "In the underground wine cellar."
                ],
                "explanation_en": "Laura offers a semi-private alcove in the garden room.",
                "explanation_tr": "Laura bahçe odasında yarı özel bir bölme sunar."
            },
            {
                "question_en": "What dietary accommodations can the restaurant provide?",
                "question_tr_hint": "Restoran hangi beslenme uyarlamalarını sağlayabiliyor?",
                "correct_answer": "Gluten-free handmade pasta in a dedicated station and plant-based vegan dishes.",
                "distractors": [
                    "Only raw meat and unpasteurized milk.",
                    "Fast food hamburgers ordered from next door.",
                    "No dietary accommodations whatsoever."
                ],
                "explanation_en": "Laura explains the chef prepares gluten-free pasta and has a plant-based menu.",
                "explanation_tr": "Laura şefin glütensiz makarna hazırladığını ve bitki bazlı menüleri olduğunu açıklar."
            },
            {
                "question_en": "What policy applies if the party brings their own anniversary cake?",
                "question_tr_hint": "Grup kendi yıldönümü pastasını getirirse hangi politika geçerlidir?",
                "correct_answer": "A plating fee of two euros per person is charged for slicing and serving.",
                "distractors": [
                    "Outside cakes are strictly confiscated and thrown away.",
                    "The restaurant charges five hundred euros penalty.",
                    "Customers must wash their own dessert plates."
                ],
                "explanation_en": "Laura mentions a modest plating fee of two euros per person.",
                "explanation_tr": "Laura kişi başı iki avroluk servis ücreti olduğunu belirtir."
            },
            {
                "question_en": "At what time is the dinner reservation booked for Friday?",
                "question_tr_hint": "Akşam yemeği rezervasyonu cuma günü saat kaça yapıldı?",
                "correct_answer": "7:30 PM.",
                "distractors": [
                    "5:00 PM.",
                    "10:00 PM.",
                    "1:00 PM."
                ],
                "explanation_en": "Emre requests 7:30 PM, which Laura confirms.",
                "explanation_tr": "Emre saat 19:30'u talep eder ve Laura bunu onaylar."
            }
        ],
        ["food-shopping", "dining", "restaurant", "reservation"]
    ),

    # 6. flatmate-chore-schedule (relationships)
    build_scenario(
        "listening.b1.flatmate-chore-schedule",
        "Negotiating a Household Chore Agreement with a Flatmate",
        "B1", "negotiation",
        "Burak discusses restructuring the shared apartment cleaning schedule and recycling duties with his flatmate Liam to maintain domestic harmony.",
        [
            {"id": "liam", "name": "Liam", "role": "Flatmate", "accent": "British"},
            {"id": "burak", "name": "Burak", "role": "Flatmate", "accent": "Turkish"},
        ],
        "b1_flatmate_chore_schedule.mp3",
        [
            {"speaker_id": "burak", "text_en": "Hey Liam, do you have ten minutes to chat about our apartment chore routine? The kitchen counter has been getting cluttered lately.", "text_tr": "Selam Liam, daire temizlik rutinimiz hakkında konuşmak için on dakikan var mı? Mutfak tezgahı son zamanlarda çok dağılmaya başladı."},
            {"speaker_id": "liam", "text_en": "Sure Burak. You are right; with my new work deadlines, I have been neglecting dishwashing during the week.", "text_tr": "Elbette Burak. Haklısın; yeni iş teslim tarihlerim yüzünden hafta içi bulaşıkları ihmal ediyordum."},
            {"speaker_id": "burak", "text_en": "I understand completely. How about we establish a rotating system where one person handles kitchen deep-cleaning each week?", "text_tr": "Tamamen anlıyorum. Her hafta bir kişinin mutfağın derinlemesine temizliğini üstlendiği dönüşümlü bir sistem kursak nasıl olur?"},
            {"speaker_id": "liam", "text_en": "That makes sense. If you take the kitchen this week, I can take full responsibility for vacuuming the common areas and taking out the recycling bins.", "text_tr": "Bu mantıklı. Bu hafta mutfağı sen alırsan, ben ortak alanları süpürme ve geri dönüşüm kutularını çıkarma sorumluluğunu üstlenebilirim."},
            {"speaker_id": "burak", "text_en": "That sounds very balanced. For the municipal glass and cardboard recycling, bins need to go to the curb by Tuesday morning.", "text_tr": "Kulağa çok dengeli geliyor. Belediye cam ve karton geri dönüşümü için kutuların salı sabahına kadar kaldırıma çıkarılması gerekiyor."},
            {"speaker_id": "liam", "text_en": "Got it. I will set a recurring reminder on my phone for Monday nights so we never miss the collection truck.", "text_tr": "Anlaşıldı. Pazartesi geceleri için telefonuma tekrarlayan bir hatırlatıcı kuracağım, böylece çöp toplama kamyonunu asla kaçırmayız."},
            {"speaker_id": "burak", "text_en": "Awesome. Let's write down this chore agreement on the whiteboard in the hallway so it is visible to both of us.", "text_tr": "Harika. Bu temizlik anlaşmasını koridordaki beyaz tahtaya yazalım, böylece ikimiz için de görünür olur."},
            {"speaker_id": "liam", "text_en": "Deal. Thanks for bringing this up constructively Burak; it makes living together so much easier.", "text_tr": "Anlaştık. Bunu yapıcı bir şekilde dile getirdiğin için teşekkürler Burak; birlikte yaşamayı çok daha kolaylaştırıyor."},
        ],
        [
            {
                "question_en": "What domestic issue prompted Burak to initiate the conversation?",
                "question_tr_hint": "Burak'ın bu konuşmayı başlatmasına hangi evsel sorun neden oldu?",
                "correct_answer": "Clutter on the kitchen counter and neglected dishwashing during the week.",
                "distractors": [
                    "A loud argument over paying the monthly rent.",
                    "A broken water pipe flooding the bathroom floor.",
                    "Liam playing electric guitar loudly at midnight."
                ],
                "explanation_en": "Burak mentions the kitchen counter getting cluttered and neglected dishes.",
                "explanation_tr": "Burak mutfak tezgahının dağılmasını ve ihmal edilen bulaşıkları belirtir."
            },
            {
                "question_en": "What system does Burak propose for kitchen cleaning?",
                "question_tr_hint": "Burak mutfak temizliği için nasıl bir sistem öneriyor?",
                "correct_answer": "A rotating weekly system where one flatmate handles deep cleaning.",
                "distractors": [
                    "Hiring an expensive daily private maid.",
                    "Never cooking food at home and eating only at restaurants.",
                    "Throwing away all ceramic plates after single use."
                ],
                "explanation_en": "Burak proposes a rotating weekly system for kitchen deep-cleaning.",
                "explanation_tr": "Burak haftalık dönüşümlü bir derinlemesine temizlik sistemi önerir."
            },
            {
                "question_en": "What tasks does Liam volunteer to handle this week?",
                "question_tr_hint": "Liam bu hafta hangi görevleri üstlenmeye gönüllü oluyor?",
                "correct_answer": "Vacuuming common areas and taking out the recycling bins.",
                "distractors": [
                    "Repainting the entire apartment living room.",
                    "Fixing the electrical wiring in the basement.",
                    "Buying groceries for the whole month."
                ],
                "explanation_en": "Liam offers to vacuum common areas and handle the recycling bins.",
                "explanation_tr": "Liam ortak alanları süpürmeyi ve geri dönüşüm kutularını çıkarmayı teklif eder."
            },
            {
                "question_en": "When do municipal recycling bins need to be at the curb?",
                "question_tr_hint": "Belediye geri dönüşüm kutularının ne zamana kadar kaldırımda olması gerekiyor?",
                "correct_answer": "By Tuesday morning.",
                "distractors": [
                    "On Friday afternoon before sunset.",
                    "Every Sunday at midnight sharp.",
                    "Only on the first day of each month."
                ],
                "explanation_en": "Burak states bins need to be at the curb by Tuesday morning.",
                "explanation_tr": "Burak kutuların salı sabahına kadar kaldırımda olması gerektiğini belirtir."
            },
            {
                "question_en": "Where do the flatmates decide to record their agreement?",
                "question_tr_hint": "Ev arkadaşları anlaşmalarını nereye kaydetmeye karar veriyorlar?",
                "correct_answer": "On the hallway whiteboard.",
                "distractors": [
                    "In a formal legal contract sent to their landlord.",
                    "In a text message group on their smartphones.",
                    "Carved into the wooden kitchen table."
                ],
                "explanation_en": "Burak suggests writing it down on the whiteboard in the hallway.",
                "explanation_tr": "Burak koridordaki beyaz tahtaya yazmayı önerir."
            }
        ],
        ["relationships", "housing", "communication", "cooperation"]
    ),

    # 7. university-study-group (education)
    build_scenario(
        "listening.b1.university-study-group",
        "Organizing an Academic Presentation Study Group",
        "B1", "stakeholder_alignment",
        "Ece and Daniel coordinate their research tasks and rehearsal schedule for an upcoming university economics seminar presentation.",
        [
            {"id": "daniel", "name": "Daniel", "role": "University Classmate", "accent": "American"},
            {"id": "ece", "name": "Ece", "role": "Undergraduate Student", "accent": "Turkish"},
        ],
        "b1_university_study_group.mp3",
        [
            {"speaker_id": "daniel", "text_en": "Hey Ece, thanks for meeting at the campus library. Professor Clark expects our twenty-minute macroeconomics presentation in two weeks.", "text_tr": "Selam Ece, kampüs kütüphanesinde buluştuğun için teşekkürler. Profesör Clark yirmi dakikalık makroekonomi sunumumuzu iki hafta içinde bekliyor."},
            {"speaker_id": "ece", "text_en": "Hi Daniel. Yes, our topic is renewable energy subsidies and regional employment growth. We should divide the case studies evenly.", "text_tr": "Merhaba Daniel. Evet, konumuz yenilenebilir enerji sübvansiyonları ve bölgesel istihdam artışı. Vaka çalışmalarını eşit olarak bölmeliyiz."},
            {"speaker_id": "daniel", "text_en": "I can analyze the European offshore wind sector data and prepare the quantitative charts showing job creation figures.", "text_tr": "Ben Avrupa açık deniz rüzgar sektörü verilerini analiz edebilir ve istihdam yaratma rakamlarını gösteren nicel grafikleri hazırlayabilirim."},
            {"speaker_id": "ece", "text_en": "Great. Then I will cover the North American solar manufacturing subsidies and evaluate the policy trade-offs and government debt impacts.", "text_tr": "Harika. O zaman ben de Kuzey Amerika güneş enerjisi üretim sübvansiyonlarını ele alıp politika ödünleşimlerini ve hükümet borç etkilerini değerlendireyim."},
            {"speaker_id": "daniel", "text_en": "That gives us a very clear analytical contrast between wind and solar policies. How many slides should we target?", "text_tr": "Bu bize rüzgar ve güneş politikaları arasında çok net bir analitik karşıtlık sağlar. Kaç slayt hedeflemeliyiz?"},
            {"speaker_id": "ece", "text_en": "Professor Clark emphasized that brevity is key. Around twelve to fifteen clean slides with visual charts rather than dense blocks of text.", "text_tr": "Profesör Clark kısalığın kilit önemde olduğunu vurguladı. Yoğun metin blokları yerine görsel grafikler içeren yaklaşık on iki ila on beş temiz slayt."},
            {"speaker_id": "daniel", "text_en": "Agreed. Let's finish our individual research drafts by next Monday and meet here to rehearse our spoken timing.", "text_tr": "Anlaştık. Bireysel araştırma taslaklarımızı gelecek pazartesiye kadar bitirelim ve konuşma süremizin provasını yapmak için burada buluşalım."},
            {"speaker_id": "ece", "text_en": "Perfect. I will book a private multimedia study room on the third floor for our rehearsal session.", "text_tr": "Mükemmel. Prova seansımız için üçüncü katta özel bir multimedya çalışma odası ayırtacağım."},
        ],
        [
            {
                "question_en": "What is the topic of Ece and Daniel's university presentation?",
                "question_tr_hint": "Ece ve Daniel'in üniversite sunumunun konusu nedir?",
                "correct_answer": "Renewable energy subsidies and regional employment growth.",
                "distractors": [
                    "Ancient Greek archaeological excavations in Crete.",
                    "The history of medieval European banking architecture.",
                    "Quantum computing algorithms for medical diagnosis."
                ],
                "explanation_en": "Ece states their topic is renewable energy subsidies and employment growth.",
                "explanation_tr": "Ece konularının yenilenebilir enerji sübvansiyonları ve istihdam artışı olduğunu belirtir."
            },
            {
                "question_en": "Which sector data will Daniel focus on analyzing?",
                "question_tr_hint": "Daniel hangi sektör verilerini analiz etmeye odaklanacak?",
                "correct_answer": "European offshore wind sector data and job creation figures.",
                "distractors": [
                    "South American copper mining statistics.",
                    "Automotive internal combustion engine manufacturing.",
                    "Global airline passenger traffic trends."
                ],
                "explanation_en": "Daniel offers to analyze the European offshore wind sector data.",
                "explanation_tr": "Daniel Avrupa açık deniz rüzgar sektörü verilerini analiz etmeyi teklif eder."
            },
            {
                "question_en": "What guideline did Professor Clark emphasize regarding the slide deck?",
                "question_tr_hint": "Profesör Clark slayt destesi hakkında hangi kuralı vurguladı?",
                "correct_answer": "Brevity: 12 to 15 clean slides with visual charts rather than dense text.",
                "distractors": [
                    "At least one hundred detailed slides with complete paragraphs.",
                    "Presenting without any digital visual aids whatsoever.",
                    "Reading directly from a printed paper textbook."
                ],
                "explanation_en": "Ece notes Clark emphasized brevity with 12-15 visual slides.",
                "explanation_tr": "Ece Clark'ın 12-15 görsel slaytla kısalığı vurguladığını belirtir."
            },
            {
                "question_en": "When do the students plan to complete their individual research drafts?",
                "question_tr_hint": "Öğrenciler bireysel araştırma taslaklarını ne zaman tamamlamayı planlıyorlar?",
                "correct_answer": "By next Monday.",
                "distractors": [
                    "In two months after final semester exams.",
                    "Late tonight before midnight.",
                    "On the morning of the final presentation."
                ],
                "explanation_en": "Daniel suggests finishing drafts by next Monday.",
                "explanation_tr": "Daniel taslakları gelecek pazartesiye kadar bitirmeyi önerir."
            },
            {
                "question_en": "Where will the rehearsal session take place?",
                "question_tr_hint": "Prova seansı nerede gerçekleşecek?",
                "correct_answer": "In a private multimedia study room on the third floor of the library.",
                "distractors": [
                    "In the university cafeteria during lunch hour.",
                    "At an outdoor coffee patio near the subway.",
                    "Inside Professor Clark's private faculty office."
                ],
                "explanation_en": "Ece plans to book a private multimedia study room on the third floor.",
                "explanation_tr": "Ece üçüncü katta özel bir multimedya çalışma odası ayırtacağını söyler."
            }
        ],
        ["education", "university", "presentation", "teamwork"]
    ),

    # 8. remote-team-catchup (communication)
    build_scenario(
        "listening.b1.remote-team-catchup",
        "Weekly Asynchronous Communication Alignment Call",
        "B1", "engineering_meeting",
        "Sinan and project coordinator Rachel hold a short weekly video alignment to establish clear asynchronous communication guidelines for their distributed software team.",
        [
            {"id": "rachel", "name": "Rachel", "role": "Remote Project Coordinator", "accent": "American"},
            {"id": "sinan", "name": "Sinan", "role": "Frontend Developer", "accent": "Turkish"},
        ],
        "b1_remote_team_catchup.mp3",
        [
            {"speaker_id": "rachel", "text_en": "Good morning Sinan! Thanks for jumping on this quick ten-minute alignment sync. How is the sprint progress on the dashboard redesign?", "text_tr": "Günaydın Sinan! Bu hızlı on dakikalık uyum görüşmesine katıldığın için teşekkürler. Kontrol paneli yeniden tasarımındaki sprint ilerlemesi nasıl gidiyor?"},
            {"speaker_id": "sinan", "text_en": "Morning Rachel. Coding is on schedule, but our developers across different time zones are getting overwhelmed by constant Slack pings.", "text_tr": "Günaydın Rachel. Kodlama takvime uygun gidiyor ancak farklı saat dilimlerindeki geliştiricilerimiz sürekli Slack bildirimlerinden bunalıyor."},
            {"speaker_id": "rachel", "text_en": "I have noticed that as well. People feel pressured to respond instantly, which completely breaks their focused engineering flow.", "text_tr": "Ben de bunu fark ettim. İnsanlar anında yanıt verme baskısı hissediyor, bu da odaklanmış mühendislik akışlarını tamamen bozuyor."},
            {"speaker_id": "sinan", "text_en": "Exactly. Could we establish a team norm that non-urgent inquiries should be posted on our project documentation board instead of direct chat?", "text_tr": "Kesinlikle. Acil olmayan soruların doğrudan sohbet yerine proje dokümantasyon panomuza yazılması yönünde bir ekip normu belirleyebilir miyiz?"},
            {"speaker_id": "rachel", "text_en": "That is an excellent proposal. Let's document an official communication charter: async updates by default, and urgent escalations only via dedicated alert channels.", "text_tr": "Bu mükemmel bir öneri. Resmi bir iletişim tüzüğü hazırlayalım: varsayılan olarak eşzamansız güncellemeler ve acil durumlar yalnızca özel uyarı kanalları üzerinden."},
            {"speaker_id": "sinan", "text_en": "That will protect our engineers' deep focus hours. What response window should we set for general pull request reviews?", "text_tr": "Bu mühendislerimizin derin odaklanma saatlerini koruyacaktır. Genel PR incelemeleri için nasıl bir yanıt süresi belirlemeliyiz?"},
            {"speaker_id": "rachel", "text_en": "A four-hour turnaround during working hours seems reasonable without forcing developers to interrupt active coding tasks.", "text_tr": "Çalışma saatleri içinde dört saatlik bir geri dönüş süresi, geliştiricileri aktif kodlama görevlerini bölmeye zorlamadan makul görünüyor."},
            {"speaker_id": "sinan", "text_en": "Perfect. I will share this draft charter in our team channel before tomorrow's standup meeting.", "text_tr": "Mükemmel. Yarınki ayaküstü toplantıdan önce bu taslak tüzüğü ekip kanalımızda paylaşacağım."},
        ],
        [
            {
                "question_en": "What primary complaint does Sinan share regarding distributed team collaboration?",
                "question_tr_hint": "Sinan dağıtık ekip işbirliği konusunda hangi temel şikayeti paylaşıyor?",
                "correct_answer": "Developers are overwhelmed by constant instant chat notifications across time zones.",
                "distractors": [
                    "Team members refuse to write any unit test code.",
                    "The cloud hosting server crashes every afternoon.",
                    "Internet connection speeds are too slow for video."
                ],
                "explanation_en": "Sinan explains developers across time zones are overwhelmed by chat pings.",
                "explanation_tr": "Sinan farklı saat dilimlerindeki geliştiricilerin sohbet bildirimlerinden bunaldığını açıklar."
            },
            {
                "question_en": "What proposal does Sinan suggest for non-urgent team inquiries?",
                "question_tr_hint": "Sinan acil olmayan ekip soruları için nasıl bir öneride bulunuyor?",
                "correct_answer": "Posting inquiries on the project documentation board rather than in direct chat.",
                "distractors": [
                    "Calling developers on their personal mobile phones at midnight.",
                    "Printing physical paper memos and mailing them through postal courier.",
                    "Canceling all project documentation permanently."
                ],
                "explanation_en": "Sinan proposes posting non-urgent inquiries on the documentation board.",
                "explanation_tr": "Sinan acil olmayan soruların dokümantasyon panosuna yazılmasını önerir."
            },
            {
                "question_en": "What principle will form the baseline of the new team communication charter?",
                "question_tr_hint": "Yeni ekip iletişim tüzüğünün temelini hangi ilke oluşturacak?",
                "correct_answer": "Asynchronous updates by default, with chat reserved for urgent escalations.",
                "distractors": [
                    "Mandatory continuous video recording during all work hours.",
                    "Working eighteen hours per day without breaks.",
                    "Completely eliminating all written communication."
                ],
                "explanation_en": "Rachel outlines async updates by default and alerts only for emergencies.",
                "explanation_tr": "Rachel varsayılan olarak eşzamansız güncellemeleri ve yalnızca acil durumlarda uyarıları belirtir."
            },
            {
                "question_en": "What turnaround window is agreed upon for pull request code reviews?",
                "question_tr_hint": "PR kod incelemeleri için üzerinde anlaşılan geri dönüş süresi nedir?",
                "correct_answer": "A four-hour window during working hours.",
                "distractors": [
                    "Within thirty seconds immediately.",
                    "Three weeks after code submission.",
                    "Only on the last day of each quarter."
                ],
                "explanation_en": "Rachel suggests a 4-hour turnaround during working hours.",
                "explanation_tr": "Rachel çalışma saatleri içinde 4 saatlik bir geri dönüş süresi önerir."
            },
            {
                "question_en": "When will Sinan share the draft communication charter with the team?",
                "question_tr_hint": "Sinan taslak iletişim tüzüğünü ekiple ne zaman paylaşacak?",
                "correct_answer": "Before tomorrow's daily standup meeting.",
                "distractors": [
                    "At the end of next year's annual conference.",
                    "Never; it remains confidential between them.",
                    "During the upcoming weekend holiday."
                ],
                "explanation_en": "Sinan says he will share the draft before tomorrow's standup.",
                "explanation_tr": "Sinan taslağı yarınki standup toplantısından önce paylaşacağını söyler."
            }
        ],
        ["communication", "remote-work", "productivity", "management"]
    ),

    # 9. internship-interview-prep (work-career)
    build_scenario(
        "listening.b1.internship-interview-prep",
        "Preparing for a Tech Internship Interview",
        "B1", "stakeholder_alignment",
        "Defne consults career coach Andrew to practice behavioral interview questions and refine her technical storytelling using the STAR methodology.",
        [
            {"id": "andrew", "name": "Andrew", "role": "University Career Coach", "accent": "British"},
            {"id": "defne", "name": "Defne", "role": "Computer Science Junior", "accent": "Turkish"},
        ],
        "b1_internship_interview_prep.mp3",
        [
            {"speaker_id": "andrew", "text_en": "Welcome Defne! Congratulations on securing an interview with the cloud infrastructure team. How are your preparations coming along?", "text_tr": "Hoş geldin Defne! Bulut altyapı ekibiyle mülakat ayarladığın için tebrikler. Hazırlıkların nasıl gidiyor?"},
            {"speaker_id": "defne", "text_en": "Thank you Andrew. I feel confident with data structure coding challenges, but I struggle to articulate my project experience concisely in behavioral interviews.", "text_tr": "Teşekkürler Andrew. Veri yapıları kodlama sorularında kendime güveniyorum ancak davranışsal mülakatlarda proje deneyimimi özlü bir şekilde ifade etmekte zorlanıyorum."},
            {"speaker_id": "andrew", "text_en": "That is very common among technical students. The key framework you must master is the STAR method: Situation, Task, Action, and Result.", "text_tr": "Bu teknik öğrenciler arasında çok yaygındır. Ustalaşman gereken temel çerçeve STAR yöntemidir: Durum (Situation), Görev (Task), Eylem (Action) ve Sonuç (Result)."},
            {"speaker_id": "defne", "text_en": "How should I structure the 'Action' portion without sounding like I am just listing technical tools?", "text_tr": "Sadece teknik araçları listeliyormuş gibi görünmeden 'Eylem' kısmını nasıl yapılandırmalıyım?"},
            {"speaker_id": "andrew", "text_en": "Focus on the engineering trade-offs you evaluated. Explain why you chose PostgreSQL over a NoSQL database, and highlight your specific individual contribution.", "text_tr": "Değerlendirdiğin mühendislik ödünleşimlerine odaklan. Neden NoSQL yerine PostgreSQL'i seçtiğini açıkla ve kendi bireysel katkını vurgula."},
            {"speaker_id": "defne", "text_en": "That makes sense. In my open-source project, I optimized SQL indexing and reduced query execution latency by forty percent.", "text_tr": "Bu mantıklı. Açık kaynak projemde SQL indekslemeyi optimize ettim ve sorgu yürütme gecikmesini yüzde kırk oranında azalttım."},
            {"speaker_id": "andrew", "text_en": "That is a stellar quantified metric! Interviewers love concrete numbers demonstrating tangible business or technical value.", "text_tr": "Bu harika bir somutlaştırılmış metrik! Mülakatı yapanlar somut ticari veya teknik değeri gösteren net sayıları çok severler."},
            {"speaker_id": "defne", "text_en": "What thoughtful questions should I ask the engineering manager at the conclusion of the interview?", "text_tr": "Mülakatın sonunda mühendislik yöneticisine hangi düşünceli soruları sormalıyım?"},
            {"speaker_id": "andrew", "text_en": "Ask about their on-call incident rotation culture or how the team handles technical debt alongside new product feature velocity.", "text_tr": "Nöbetçi arıza müdahale rotasyon kültürlerini veya ekibin yeni ürün özellikleri geliştirirken teknik borcu nasıl yönettiğini sor."},
        ],
        [
            {
                "question_en": "Which interview component does Defne find most challenging?",
                "question_tr_hint": "Defne mülakatın hangi bileşenini en zorlu buluyor?",
                "correct_answer": "Articulating project experiences concisely in behavioral questions.",
                "distractors": [
                    "Writing complex algorithmic sorting code in Python.",
                    "Answering basic questions about computer hardware.",
                    "Speaking English with a formal British accent."
                ],
                "explanation_en": "Defne explains she struggles to articulate project experience concisely in behavioral interviews.",
                "explanation_tr": "Defne davranışsal mülakatlarda deneyimlerini özlü aktarmakta zorlandığını belirtir."
            },
            {
                "question_en": "What does the STAR interview framework stand for?",
                "question_tr_hint": "STAR mülakat çerçevesi ne anlama gelir?",
                "correct_answer": "Situation, Task, Action, and Result.",
                "distractors": [
                    "Strategy, Testing, Analysis, and Review.",
                    "Software, Technology, Architecture, and Reliability.",
                    "Speed, Timing, Accuracy, and Response."
                ],
                "explanation_en": "Andrew explains STAR stands for Situation, Task, Action, and Result.",
                "explanation_tr": "Andrew STAR'ın Durum, Görev, Eylem ve Sonuç anlamına geldiğini açıklar."
            },
            {
                "question_en": "What quantified achievement does Defne highlight from her open-source project?",
                "question_tr_hint": "Defne açık kaynak projesinden hangi somutlaştırılmış başarıyı vurguluyor?",
                "correct_answer": "Reduced query execution latency by forty percent through SQL indexing.",
                "distractors": [
                    "Wrote one million lines of code in two days.",
                    "Raised ten million dollars in private venture capital.",
                    "Deleted all database tables to save server space."
                ],
                "explanation_en": "Defne mentions optimizing SQL indexing and cutting query latency by 40%.",
                "explanation_tr": "Defne SQL indekslemeyi optimize edip sorgu gecikmesini %40 azalttığını belirtir."
            },
            {
                "question_en": "Why does Andrew praise Defne's latency optimization example?",
                "question_tr_hint": "Andrew Defne'nin gecikme optimizasyonu örneğini neden övüyor?",
                "correct_answer": "Interviewers appreciate concrete metrics demonstrating tangible technical value.",
                "distractors": [
                    "It proves she can work seventy hours per week without sleep.",
                    "It shows she memorized the entire PostgreSQL documentation.",
                    "It indicates she has ten years of corporate executive experience."
                ],
                "explanation_en": "Andrew notes interviewers love concrete numbers demonstrating tangible value.",
                "explanation_tr": "Andrew mülakatçıların somut değeri gösteren net sayıları sevdiğini belirtir."
            },
            {
                "question_en": "What thoughtful question does Andrew recommend asking the manager?",
                "question_tr_hint": "Andrew yöneticiye hangi düşünceli soruyu sormayı öneriyor?",
                "correct_answer": "How the team handles technical debt alongside product feature development.",
                "distractors": [
                    "How much money the company president earns each year.",
                    "Whether employees are allowed to sleep during working hours.",
                    "How many free snacks are stocked in the office kitchen."
                ],
                "explanation_en": "Andrew suggests asking about technical debt management alongside feature velocity.",
                "explanation_tr": "Andrew özellik hızı yanında teknik borcun nasıl yönetildiğini sormayı önerir."
            }
        ],
        ["work-career", "interview", "career", "soft-skills"]
    )
]
