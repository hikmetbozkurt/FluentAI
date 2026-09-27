#!/usr/bin/env python3
"""
Listening Batch 002: A2 Scenarios (7 scenarios).
"""

from listening_builder import build_scenario

SCENARIOS_A2 = [
    # 1. apartment-lease-viewing (daily-life)
    build_scenario(
        "listening.a2.apartment-lease-viewing",
        "Viewing a Rental Apartment in London",
        "A2", "negotiation",
        "Murat visits a one-bedroom apartment in London and discusses the rent, deposit, and move-in date with the property manager, Sarah.",
        [
            {"id": "sarah", "name": "Sarah", "role": "Property Manager", "accent": "British"},
            {"id": "murat", "name": "Murat", "role": "Prospective Tenant", "accent": "Turkish"},
        ],
        "a2_apartment_lease_viewing.mp3",
        [
            {"speaker_id": "sarah", "text_en": "Good morning Murat. Welcome to the apartment on High Street. Let me show you around the living room.", "text_tr": "Günaydın Murat. High Street'teki daireye hoş geldin. Sana oturma odasını gezdireyim."},
            {"speaker_id": "murat", "text_en": "Good morning Sarah. It looks very bright and clean. Is the heating included in the monthly rent?", "text_tr": "Günaydın Sarah. Çok aydınlık ve temiz görünüyor. Isınma aylık kiraya dahil mi?"},
            {"speaker_id": "sarah", "text_en": "No, the rent is one thousand two hundred pounds per month, but water and electricity bills are separate.", "text_tr": "Hayır, kira aylık bin iki yüz sterlin, ancak su ve elektrik faturaları ayrıdır."},
            {"speaker_id": "murat", "text_en": "I understand. And how much is the security deposit before moving in?", "text_tr": "Anlıyorum. Peki taşınmadan önceki güvence depozitosu ne kadar?"},
            {"speaker_id": "sarah", "text_en": "The deposit is five weeks of rent. It is kept safely in a government tenancy protection scheme.", "text_tr": "Depozito beş haftalık kiradır. Hükümetin kiracı koruma planında güvenle tutulur."},
            {"speaker_id": "murat", "text_en": "That sounds fair. When would the apartment be available for me to move in?", "text_tr": "Kulağa adil geliyor. Daireye taşınmam için ne zaman müsait olur?"},
            {"speaker_id": "sarah", "text_en": "It is available starting from the first of next month, once we verify your employment reference.", "text_tr": "İş yeri referansınızı doğruladıktan sonra, gelecek ayın birinden itibaren müsaittir."},
            {"speaker_id": "murat", "text_en": "Great. I will email my employer letter and bank statements to you this afternoon.", "text_tr": "Harika. İşveren mektubumu ve banka hesap dökümlerimi bu öğleden sonra size e-posta ile göndereceğim."},
        ],
        [
            {
                "question_en": "What is the monthly rent for the apartment?",
                "question_tr_hint": "Dairenin aylık kirası ne kadardır?",
                "correct_answer": "One thousand two hundred pounds.",
                "distractors": [
                    "Five hundred pounds.",
                    "Two thousand pounds.",
                    "Three thousand five hundred pounds."
                ],
                "explanation_en": "Sarah states the rent is 1,200 pounds per month.",
                "explanation_tr": "Sarah kiranın ayda 1200 sterlin olduğunu belirtir."
            },
            {
                "question_en": "Are utility bills included in the monthly rent?",
                "question_tr_hint": "Fatura giderleri aylık kiraya dahil midir?",
                "correct_answer": "No, water and electricity bills are separate.",
                "distractors": [
                    "Yes, all bills are completely free.",
                    "Only the high-speed internet bill is included.",
                    "The landlord pays for all heating costs."
                ],
                "explanation_en": "Sarah explains that water and electricity are separate.",
                "explanation_tr": "Sarah su ve elektriğin ayrı olduğunu açıklar."
            },
            {
                "question_en": "How much is the security deposit?",
                "question_tr_hint": "Güvence depozitosu ne kadardır?",
                "correct_answer": "Five weeks of rent.",
                "distractors": [
                    "One full year of rent.",
                    "Ten days of rent.",
                    "Fifty pounds in cash."
                ],
                "explanation_en": "Sarah says the deposit is five weeks of rent.",
                "explanation_tr": "Sarah depozitonun beş haftalık kira olduğunu söyler."
            },
            {
                "question_en": "When can Murat move into the apartment?",
                "question_tr_hint": "Murat daireye ne zaman taşınabilir?",
                "correct_answer": "From the first of next month, after reference verification.",
                "distractors": [
                    "Immediately this afternoon without any paperwork.",
                    "In six months after summer renovation.",
                    "Only on weekends during December."
                ],
                "explanation_en": "Sarah says it is available from the first of next month once references are checked.",
                "explanation_tr": "Sarah referanslar kontrol edildikten sonra gelecek ayın birinden itibaren müsait olduğunu belirtir."
            },
            {
                "question_en": "What will Murat send to Sarah this afternoon?",
                "question_tr_hint": "Murat bu öğleden sonra Sarah'ya ne gönderecek?",
                "correct_answer": "His employer letter and bank statements.",
                "distractors": [
                    "A box of furniture for the living room.",
                    "The keys to his old house.",
                    "A physical paper contract signed in ink."
                ],
                "explanation_en": "Murat promises to email his employer letter and bank statements.",
                "explanation_tr": "Murat işveren mektubunu ve hesap dökümlerini e-postayla göndereceğini söyler."
            }
        ],
        ["daily-life", "housing", "renting", "london"]
    ),

    # 2. metro-transit-card (daily-life)
    build_scenario(
        "listening.a2.metro-transit-card",
        "Buying a Subway Transit Card",
        "A2", "stakeholder_alignment",
        "Deniz asks a subway station customer service clerk, Oliver, for help buying and topping up a travel card in a new city.",
        [
            {"id": "oliver", "name": "Oliver", "role": "Station Customer Service Agent", "accent": "American"},
            {"id": "deniz", "name": "Deniz", "role": "Commuter", "accent": "Turkish"},
        ],
        "a2_metro_transit_card.mp3",
        [
            {"speaker_id": "oliver", "text_en": "Hello there! Can I help you with the ticket machine?", "text_tr": "Merhaba! Bilet makinesi konusunda size yardımcı olabilir miyim?"},
            {"speaker_id": "deniz", "text_en": "Yes please. I am new in town. I need a card for daily subway travel to work.", "text_tr": "Evet lütfen. Şehirde yeniyim. İşe günlük metro seyahatleri için bir karta ihtiyacım var."},
            {"speaker_id": "oliver", "text_en": "You should get our rechargeable MetroPass. The plastic card costs five dollars, and then you add travel credit.", "text_tr": "Şarj edilebilir MetroPass kartımızı almalısınız. Plastik kart beş dolar, ardından seyahat kredisi yüklüyorsunuz."},
            {"speaker_id": "deniz", "text_en": "How much does a single subway ride cost with the pass?", "text_tr": "Kartla tek bir metro yolculuğu ne kadar tutuyor?"},
            {"speaker_id": "oliver", "text_en": "It is two dollars and fifty cents per ride. If you ride every day, a weekly unlimited pass for thirty dollars is cheaper.", "text_tr": "Yolculuk başına iki dolar elli sent. Her gün biniyorsanız, otuz dolarlık haftalık sınırsız kart daha ucuzdur."},
            {"speaker_id": "deniz", "text_en": "That weekly pass sounds perfect for me. Can I pay with a contactless credit card?", "text_tr": "Haftalık kart benim için mükemmel. Temassız kredi kartıyla ödeyebilir miyim?"},
            {"speaker_id": "oliver", "text_en": "Yes, tap your card on the payment terminal right here. Here is your pass and receipt.", "text_tr": "Evet, kartınızı hemen buradaki ödeme terminaline okutun. İşte kartınız ve makbuzunuz."},
            {"speaker_id": "deniz", "text_en": "Thank you Oliver. Which platform goes toward Downtown Central Station?", "text_tr": "Teşekkürler Oliver. Downtown Central İstasyonu'na hangi peron gidiyor?"},
        ],
        [
            {
                "question_en": "Why does Deniz need a transit card?",
                "question_tr_hint": "Deniz neden bir ulaşım kartına ihtiyaç duyuyor?",
                "correct_answer": "For daily subway travel to work in a new town.",
                "distractors": [
                    "To enter an international airport lounge.",
                    "To rent an electric bicycle for the weekend.",
                    "To buy coffee inside the subway terminal."
                ],
                "explanation_en": "Deniz mentions being new in town and needing a card for daily travel to work.",
                "explanation_tr": "Deniz şehirde yeni olduğunu ve işe günlük seyahat için karta ihtiyacı olduğunu söyler."
            },
            {
                "question_en": "How much does the plastic MetroPass card cost initially?",
                "question_tr_hint": "Plastik MetroPass kartın başlangıç maliyeti ne kadardır?",
                "correct_answer": "Five dollars.",
                "distractors": [
                    "Fifty dollars.",
                    "Twenty-five dollars.",
                    "It is completely free."
                ],
                "explanation_en": "Oliver states the plastic card costs five dollars.",
                "explanation_tr": "Oliver plastik kartın beş dolar olduğunu belirtir."
            },
            {
                "question_en": "How much does a weekly unlimited pass cost?",
                "question_tr_hint": "Haftalık sınırsız bilet ne kadardır?",
                "correct_answer": "Thirty dollars.",
                "distractors": [
                    "Two dollars and fifty cents.",
                    "One hundred dollars.",
                    "Ten dollars."
                ],
                "explanation_en": "Oliver explains the weekly unlimited pass is thirty dollars.",
                "explanation_tr": "Oliver haftalık sınırsız kartın otuz dolar olduğunu açıklar."
            },
            {
                "question_en": "How does Deniz pay for the pass?",
                "question_tr_hint": "Deniz biletin ödemesini nasıl yapıyor?",
                "correct_answer": "With a contactless credit card tapped on the terminal.",
                "distractors": [
                    "With physical paper cash bills.",
                    "With an old gold coin.",
                    "By writing a personal paper check."
                ],
                "explanation_en": "Deniz asks if contactless credit card works, and Oliver confirms tapping it on the terminal.",
                "explanation_tr": "Deniz temassız kredi kartını sorar ve Oliver terminale okutmasını onaylar."
            },
            {
                "question_en": "What destination does Deniz ask about at the end?",
                "question_tr_hint": "Deniz sonunda hangi varış noktasını soruyor?",
                "correct_answer": "Downtown Central Station.",
                "distractors": [
                    "North Mountain Ski Resort.",
                    "The regional soccer stadium.",
                    "The international seaport harbor."
                ],
                "explanation_en": "Deniz asks which platform goes toward Downtown Central Station.",
                "explanation_tr": "Deniz hangi peronun Downtown Central İstasyonu'na gittiğini sorar."
            }
        ],
        ["daily-life", "transit", "subway", "tickets"]
    ),

    # 3. hotel-check-in-late (travel)
    build_scenario(
        "listening.a2.hotel-check-in-late",
        "Late Evening Hotel Check-In",
        "A2", "incident_response",
        "Selin arrives at a hotel front desk past midnight after a delayed flight and checks into her reserved room with receptionist James.",
        [
            {"id": "james", "name": "James", "role": "Hotel Night Auditor", "accent": "American"},
            {"id": "selin", "name": "Selin", "role": "Hotel Guest", "accent": "Turkish"},
        ],
        "a2_hotel_check_in_late.mp3",
        [
            {"speaker_id": "james", "text_en": "Good evening, welcome to Grand Central Hotel. How can I assist you tonight?", "text_tr": "İyi akşamlar, Grand Central Hotel'e hoş geldiniz. Bu gece size nasıl yardımcı olabilirim?"},
            {"speaker_id": "selin", "text_en": "Hello. I have a reservation under Selin Yilmaz. My flight was delayed by four hours.", "text_tr": "Merhaba. Selin Yılmaz adına bir rezervasyonum var. Uçağım dört saat rötar yaptı."},
            {"speaker_id": "james", "text_en": "I see your booking right here, Ms. Yilmaz. A standard queen room for three nights. May I see your passport?", "text_tr": "Rezervasyonunuzu tam burada görüyorum Bayan Yılmaz. Üç gece için standart queen oda. Pasaportunuzu görebilir miyim?"},
            {"speaker_id": "selin", "text_en": "Here is my passport. Is breakfast included in the morning?", "text_tr": "İşte pasaportum. Sabah kahvaltı dahil mi?"},
            {"speaker_id": "james", "text_en": "Yes, breakfast is served on the second floor from seven to ten in the morning.", "text_tr": "Evet, kahvaltı sabah yediden ona kadar ikinci katta servis ediliyor."},
            {"speaker_id": "selin", "text_en": "Wonderful. What is the password for the room wireless internet?", "text_tr": "Harika. Oda kablosuz internetinin şifresi nedir?"},
            {"speaker_id": "james", "text_en": "Your room number is 412, and the Wi-Fi password is printed on your keycard sleeve.", "text_tr": "Oda numaranız 412 ve Wi-Fi şifresi anahtar kartı kılıfınızın üzerinde basılıdır."},
            {"speaker_id": "selin", "text_en": "Thank you very much James. Where are the elevators located?", "text_tr": "Çok teşekkür ederim James. Asansörler nerede bulunuyor?"},
        ],
        [
            {
                "question_en": "Why did Selin arrive at the hotel late at night?",
                "question_tr_hint": "Selin otele neden gece geç saatte vardı?",
                "correct_answer": "Her flight was delayed by four hours.",
                "distractors": [
                    "She lost her train ticket at the station.",
                    "She was attending an all-night concert.",
                    "Her rental car ran out of fuel."
                ],
                "explanation_en": "Selin mentions her flight was delayed by four hours.",
                "explanation_tr": "Selin uçuşunun dört saat rötar yaptığını belirtir."
            },
            {
                "question_en": "How many nights is Selin staying at the hotel?",
                "question_tr_hint": "Selin otelde kaç gece konaklıyor?",
                "correct_answer": "Three nights.",
                "distractors": [
                    "One single night.",
                    "Two whole weeks.",
                    "One month."
                ],
                "explanation_en": "James confirms her booking is for three nights.",
                "explanation_tr": "James rezervasyonunun üç gece olduğunu onaylar."
            },
            {
                "question_en": "Where and when is breakfast served?",
                "question_tr_hint": "Kahvaltı nerede ve ne zaman servis ediliyor?",
                "correct_answer": "On the second floor from 7:00 to 10:00 AM.",
                "distractors": [
                    "In the basement at midnight only.",
                    "Delivered to rooms at 5:00 AM.",
                    "At a nearby restaurant down the road."
                ],
                "explanation_en": "James states breakfast is on the 2nd floor from 7 to 10 AM.",
                "explanation_tr": "James kahvaltının 2. katta sabah 7 ile 10 arasında olduğunu belirtir."
            },
            {
                "question_en": "What is Selin's room number?",
                "question_tr_hint": "Selin'in oda numarası kaçtır?",
                "correct_answer": "412.",
                "distractors": [
                    "101.",
                    "850.",
                    "204."
                ],
                "explanation_en": "James tells Selin her room number is 412.",
                "explanation_tr": "James Selin'e oda numarasının 412 olduğunu söyler."
            },
            {
                "question_en": "Where can Selin find the Wi-Fi password?",
                "question_tr_hint": "Selin Wi-Fi şifresini nerede bulabilir?",
                "correct_answer": "Printed on her keycard sleeve.",
                "distractors": [
                    "Written on a chalkboard in the lobby.",
                    "Sent to her personal postal mail address.",
                    "She must call the hotel manager in the morning."
                ],
                "explanation_en": "James notes the Wi-Fi password is on the keycard sleeve.",
                "explanation_tr": "James Wi-Fi şifresinin anahtar kartı kılıfında basılı olduğunu belirtir."
            }
        ],
        ["travel", "hotel", "hospitality", "check-in"]
    ),

    # 4. pharmacy-cold-medicine (health-lifestyle)
    build_scenario(
        "listening.a2.pharmacy-cold-medicine",
        "Asking for Cold Medicine at a Pharmacy",
        "A2", "product_discovery",
        "Emre visits a neighborhood pharmacy to ask pharmacist Claire for advice on over-the-counter medicine for a sore throat and mild fever.",
        [
            {"id": "claire", "name": "Claire", "role": "Pharmacist", "accent": "British"},
            {"id": "emre", "name": "Emre", "role": "Customer", "accent": "Turkish"},
        ],
        "a2_pharmacy_cold_medicine.mp3",
        [
            {"speaker_id": "claire", "text_en": "Hello, how can I assist you at the pharmacy counter today?", "text_tr": "Merhaba, bugün eczane bankosunda size nasıl yardımcı olabilirim?"},
            {"speaker_id": "emre", "text_en": "Hi. I started feeling sick yesterday. I have a sore throat, a runny nose, and a mild fever.", "text_tr": "Merhaba. Dün kendimi hasta hissetmeye başladım. Boğaz ağrım, burun akıntım ve hafif ateşim var."},
            {"speaker_id": "claire", "text_en": "I am sorry to hear that. Do you have any allergies to medications like paracetamol or ibuprofen?", "text_tr": "Bunu duyduğuma üzüldüm. Parasetamol veya ibuprofen gibi ilaçlara herhangi bir alerjiniz var mı?"},
            {"speaker_id": "emre", "text_en": "No, I do not have any known drug allergies.", "text_tr": "Hayır, bilinen herhangi bir ilaç alerjim yok."},
            {"speaker_id": "claire", "text_en": "This multi-symptom cold syrup will relieve your fever and congestion. Take two teaspoons every six hours.", "text_tr": "Bu çok belirtili soğuk algınlığı şurubu ateşinizi ve tıkanıklığınızı rahatlatacaktır. Her altı saatte bir iki çay kaşığı alın."},
            {"speaker_id": "emre", "text_en": "Will this syrup make me feel drowsy or sleepy during the day?", "text_tr": "Bu şurup gün içinde uykulu veya sersem hissettirir mi?"},
            {"speaker_id": "claire", "text_en": "This is a daytime non-drowsy formula, so you can work normally. Also drink plenty of warm fluids.", "text_tr": "Bu gündüz için uyku yapmayan bir formüldür, böylece normal şekilde çalışabilirsiniz. Ayrıca bolca ılık sıvı tüketin."},
            {"speaker_id": "emre", "text_en": "Thank you Claire. I will take this bottle and a box of throat lozenges please.", "text_tr": "Teşekkürler Claire. Bu şişeyi ve bir kutu boğaz pastilini alayım lütfen."},
        ],
        [
            {
                "question_en": "What symptoms has Emre been experiencing since yesterday?",
                "question_tr_hint": "Emre dünden beri hangi semptomları yaşıyor?",
                "correct_answer": "A sore throat, runny nose, and mild fever.",
                "distractors": [
                    "A broken arm and severe wrist pain.",
                    "Extreme toothache after eating ice cream.",
                    "Severe food poisoning from fish."
                ],
                "explanation_en": "Emre describes having a sore throat, runny nose, and mild fever.",
                "explanation_tr": "Emre boğaz ağrısı, burun akıntısı ve hafif ateşi olduğunu anlatır."
            },
            {
                "question_en": "Does Emre have any drug allergies?",
                "question_tr_hint": "Emre'nin herhangi bir ilaç alerjisi var mı?",
                "correct_answer": "No, he has no known medication allergies.",
                "distractors": [
                    "Yes, he is severely allergic to paracetamol.",
                    "Yes, he cannot consume any liquid medicine.",
                    "He is allergic to pure warm water."
                ],
                "explanation_en": "Emre confirms he does not have any drug allergies.",
                "explanation_tr": "Emre bilinen bir ilaç alerjisi olmadığını onaylar."
            },
            {
                "question_en": "How often should Emre take the cold syrup?",
                "question_tr_hint": "Emre soğuk algınlığı şurubunu ne sıklıkla almalıdır?",
                "correct_answer": "Two teaspoons every six hours.",
                "distractors": [
                    "One entire bottle every morning.",
                    "Ten drops every thirty minutes.",
                    "Only once a week before bedtime."
                ],
                "explanation_en": "Claire advises two teaspoons every six hours.",
                "explanation_tr": "Claire her altı saatte bir iki çay kaşığı almasını tavsiye eder."
            },
            {
                "question_en": "Why can Emre work normally after taking the syrup?",
                "question_tr_hint": "Emre şurubu aldıktan sonra neden normal şekilde çalışabilir?",
                "correct_answer": "It is a daytime non-drowsy formula that does not cause sleepiness.",
                "distractors": [
                    "It contains massive doses of caffeine.",
                    "His employer forbids him from resting at home.",
                    "The syrup cures the illness within two minutes."
                ],
                "explanation_en": "Claire confirms it is a non-drowsy daytime formula.",
                "explanation_tr": "Claire ilacın uyku yapmayan gündüz formülü olduğunu doğrular."
            },
            {
                "question_en": "What additional item does Emre decide to purchase?",
                "question_tr_hint": "Emre ek olarak hangi ürünü satın almaya karar veriyor?",
                "correct_answer": "A box of throat lozenges.",
                "distractors": [
                    "A digital medical thermometer.",
                    "A large bottle of hand sanitizer.",
                    "An electric heating blanket."
                ],
                "explanation_en": "Emre asks for a box of throat lozenges in addition to the syrup.",
                "explanation_tr": "Emre şurubun yanı sıra bir kutu boğaz pastili ister."
            }
        ],
        ["health-lifestyle", "pharmacy", "medicine", "wellness"]
    ),

    # 5. grocery-market-fresh (food-shopping)
    build_scenario(
        "listening.a2.grocery-market-fresh",
        "Buying Fresh Produce at a Farmers Market",
        "A2", "product_discovery",
        "Aylin shops for seasonal organic vegetables and fruit at a weekend open-air market and chats with the farmer, Tom.",
        [
            {"id": "tom", "name": "Tom", "role": "Market Vendor", "accent": "American"},
            {"id": "aylin", "name": "Aylin", "role": "Shopper", "accent": "Turkish"},
        ],
        "a2_grocery_market_fresh.mp3",
        [
            {"speaker_id": "tom", "text_en": "Good morning! We harvested these heirloom tomatoes and crisp apples just yesterday morning.", "text_tr": "Günaydın! Bu ata tohumu domatesleri ve çıtır elmaları daha dün sabah hasat ettik."},
            {"speaker_id": "aylin", "text_en": "Good morning! The tomatoes smell wonderful. Are they grown without chemical sprays?", "text_tr": "Günaydın! Domatesler harika kokuyor. Kimyasal ilaçlar olmadan mı yetiştirildiler?"},
            {"speaker_id": "tom", "text_en": "Yes, our farm is certified organic. We use natural compost and biological pest control.", "text_tr": "Evet, çiftliğimiz sertifikalı organiktir. Doğal kompost ve biyolojik zararlı kontrolü kullanıyoruz."},
            {"speaker_id": "aylin", "text_en": "That is great. How much are the red apples per kilogram?", "text_tr": "Bu harika. Kırmızı elmaların kilosu ne kadar?"},
            {"speaker_id": "tom", "text_en": "They are three dollars per kilogram, or you can get a three-kilo wooden basket for eight dollars.", "text_tr": "Kilosu üç dolar ya da sekiz dolara üç kiloluk ahşap bir sepet alabilirsiniz."},
            {"speaker_id": "aylin", "text_en": "I will take the three-kilo basket of apples and two kilos of the organic tomatoes.", "text_tr": "Üç kiloluk elma sepetini ve iki kilo organik domatesi alayım."},
            {"speaker_id": "tom", "text_en": "That comes to fourteen dollars in total. Would you like a paper bag for carrying them?", "text_tr": "Toplamda on dört dolar tutuyor. Taşımak için kağıt bir kese kağıdı ister misiniz?"},
            {"speaker_id": "aylin", "text_en": "No thank you, I brought my own reusable cloth tote bag. Here is my credit card.", "text_tr": "Hayır teşekkürler, kendi yeniden kullanılabilir bez çantamı getirdim. İşte kredi kartım."},
        ],
        [
            {
                "question_en": "When were the tomatoes and apples harvested?",
                "question_tr_hint": "Domatesler ve elmalar ne zaman hasat edildi?",
                "correct_answer": "Yesterday morning.",
                "distractors": [
                    "Three weeks ago.",
                    "Last month during autumn.",
                    "Early this morning before sunrise."
                ],
                "explanation_en": "Tom explains they harvested them yesterday morning.",
                "explanation_tr": "Tom ürünleri dün sabah hasat ettiklerini açıklar."
            },
            {
                "question_en": "How are crops grown on Tom's farm?",
                "question_tr_hint": "Tom'un çiftliğinde ürünler nasıl yetiştiriliyor?",
                "correct_answer": "Organically with natural compost and biological pest control.",
                "distractors": [
                    "Using industrial chemical synthetic sprays.",
                    "Inside artificial glass test tubes without soil.",
                    "Imported from overseas greenhouses."
                ],
                "explanation_en": "Tom confirms the farm is certified organic with natural compost.",
                "explanation_tr": "Tom çiftliğin doğal kompost kullanan sertifikalı organik olduğunu doğrular."
            },
            {
                "question_en": "What special discount does Tom offer for the apples?",
                "question_tr_hint": "Tom elmalar için nasıl özel bir indirim sunuyor?",
                "correct_answer": "A three-kilogram basket for eight dollars instead of nine.",
                "distractors": [
                    "Buy one apple and get five free.",
                    "Free apples if the customer pays in cash.",
                    "A fifty percent discount on rotten fruit."
                ],
                "explanation_en": "Tom says a 3-kilo basket is eight dollars instead of $3/kg.",
                "explanation_tr": "Tom 3 kiloluk sepetin kilo başına 3 dolar yerine 8 dolar olduğunu söyler."
            },
            {
                "question_en": "What is the total price for Aylin's purchases?",
                "question_tr_hint": "Aylin'in alışverişinin toplam tutarı ne kadardır?",
                "correct_answer": "Fourteen dollars.",
                "distractors": [
                    "Five dollars.",
                    "Thirty dollars.",
                    "Fifty dollars."
                ],
                "explanation_en": "Tom calculates the total comes to fourteen dollars.",
                "explanation_tr": "Tom toplamın 14 dolar tuttuğunu belirtir."
            },
            {
                "question_en": "Why does Aylin refuse Tom's offer of a paper bag?",
                "question_tr_hint": "Aylin Tom'un kağıt poşet teklifini neden reddediyor?",
                "correct_answer": "She brought her own reusable cloth tote bag.",
                "distractors": [
                    "She plans to eat all the apples immediately on the street.",
                    "She thinks paper bags are too expensive to buy.",
                    "She has a shopping cart in her car."
                ],
                "explanation_en": "Aylin says she brought her own reusable cloth bag.",
                "explanation_tr": "Aylin kendi bez çantasını getirdiğini söyler."
            }
        ],
        ["food-shopping", "market", "organic", "produce"]
    ),

    # 6. neighbor-package-delivery (relationships)
    build_scenario(
        "listening.a2.neighbor-package-delivery",
        "Collecting a Delivered Parcel from a Neighbor",
        "A2", "stakeholder_alignment",
        "Kerem knocks on his neighbor Emma's apartment door to retrieve an online shopping parcel that the courier left with her.",
        [
            {"id": "emma", "name": "Emma", "role": "Neighbor", "accent": "British"},
            {"id": "kerem", "name": "Kerem", "role": "Apartment Resident", "accent": "Turkish"},
        ],
        "a2_neighbor_package_delivery.mp3",
        [
            {"speaker_id": "kerem", "text_en": "Hi Emma! Sorry to bother you on a Tuesday evening. Did the courier leave a box for me?", "text_tr": "Selam Emma! Bir salı akşamı seni rahatsız ettiğim için kusura bakma. Kurye benim için bir kutu bıraktı mı?"},
            {"speaker_id": "emma", "text_en": "Hello Kerem! Yes, the postal driver rang my doorbell around two o'clock because you were at work.", "text_tr": "Merhaba Kerem! Evet, sen işte olduğun için posta şoförü saat iki civarında benim zilimi çaldı."},
            {"speaker_id": "kerem", "text_en": "Thank you so much for taking it in. It is a new ergonomic keyboard for my home office.", "text_tr": "Aldığın için çok teşekkür ederim. Ev ofisim için yeni bir ergonomik klavyeydi."},
            {"speaker_id": "emma", "text_en": "Here it is. The box was quite light, and it is in perfect condition with no damage.", "text_tr": "İşte burada. Kutu oldukça hafifti ve hiç hasar görmemiş, mükemmel durumda."},
            {"speaker_id": "kerem", "text_en": "I really appreciate your help. If you ever have deliveries when you are away, feel free to use my address.", "text_tr": "Yardımın için gerçekten minnettarım. Uzakta olduğunda teslimatların olursa, çekinmeden benim adresimi kullanabilirsin."},
            {"speaker_id": "emma", "text_en": "That is very kind of you Kerem. Neighbors should look out for each other.", "text_tr": "Çok naziksin Kerem. Komşular birbirine göz kulak olmalı."},
            {"speaker_id": "kerem", "text_en": "By the way, I baked some fresh Turkish walnut cookies yesterday. I will bring some over tomorrow.", "text_tr": "Bu arada dün taze Türk cevizli kurabiyesi pişirdim. Yarın biraz getiririm."},
            {"speaker_id": "emma", "text_en": "Oh, that sounds delicious! Have a wonderful evening Kerem.", "text_tr": "Kulağa çok lezzetli geliyor! İyi akşamlar Kerem."},
        ],
        [
            {
                "question_en": "Why did the postal driver deliver Kerem's package to Emma?",
                "question_tr_hint": "Posta şoförü Kerem'in paketini neden Emma'ya teslim etti?",
                "correct_answer": "Kerem was away at work when the courier arrived.",
                "distractors": [
                    "Kerem forgot his apartment building number.",
                    "The package was addressed to Emma by mistake.",
                    "Kerem's front door was locked with a chain."
                ],
                "explanation_en": "Emma explains the courier rang her bell because Kerem was at work.",
                "explanation_tr": "Emma Kerem işte olduğu için kuryenin kendi zilini çaldığını açıklar."
            },
            {
                "question_en": "What item was inside the delivered parcel?",
                "question_tr_hint": "Teslim edilen paketin içinde ne vardı?",
                "correct_answer": "An ergonomic keyboard for Kerem's home office.",
                "distractors": [
                    "A set of ceramic coffee cups.",
                    "A pair of leather running shoes.",
                    "A printed paperback novel."
                ],
                "explanation_en": "Kerem mentions it is a new ergonomic keyboard for his home office.",
                "explanation_tr": "Kerem ev ofisi için yeni ergonomik bir klavye olduğunu söyler."
            },
            {
                "question_en": "At approximately what time did the delivery occur?",
                "question_tr_hint": "Teslimat yaklaşık olarak saat kaçta gerçekleşti?",
                "correct_answer": "Around two o'clock in the afternoon.",
                "distractors": [
                    "At seven o'clock in the morning.",
                    "Past midnight on Sunday.",
                    "At noon sharp."
                ],
                "explanation_en": "Emma notes the driver rang her doorbell around two o'clock.",
                "explanation_tr": "Emma şoförün saat iki civarında zilini çaldığını belirtir."
            },
            {
                "question_en": "What reciprocal offer does Kerem make to Emma?",
                "question_tr_hint": "Kerem Emma'ya nasıl karşılıklı bir teklifte bulunuyor?",
                "correct_answer": "She can have her packages delivered to his apartment when she is away.",
                "distractors": [
                    "He will clean her apartment windows every Saturday.",
                    "He will pay for her monthly internet subscription.",
                    "He offers to drive her to work every morning."
                ],
                "explanation_en": "Kerem offers that Emma can use his address for deliveries when away.",
                "explanation_tr": "Kerem Emma'nın uzaktayken teslimatlar için kendi adresini kullanabileceğini teklif eder."
            },
            {
                "question_en": "What food item does Kerem promise to share with Emma tomorrow?",
                "question_tr_hint": "Kerem yarın Emma ile hangi yiyeceği paylaşacağına söz veriyor?",
                "correct_answer": "Fresh Turkish walnut cookies.",
                "distractors": [
                    "A bowl of spicy tomato pasta.",
                    "A loaf of freshly baked sourdough bread.",
                    "Homemade chocolate ice cream."
                ],
                "explanation_en": "Kerem says he baked fresh Turkish walnut cookies and will bring some over.",
                "explanation_tr": "Kerem cevizli kurabiye pişirdiğini ve yarın getireceğini söyler."
            }
        ],
        ["relationships", "community", "neighbors", "delivery"]
    ),

    # 7. language-course-registration (education)
    build_scenario(
        "listening.a2.language-course-registration",
        "Enrolling in an Evening English Class",
        "A2", "stakeholder_alignment",
        "Zeynep visits a community college continuing education office to register for an evening conversational English course with advisor Mark.",
        [
            {"id": "mark", "name": "Mark", "role": "Course Registration Advisor", "accent": "American"},
            {"id": "zeynep", "name": "Zeynep", "role": "Prospective Student", "accent": "Turkish"},
        ],
        "a2_language_course_registration.mp3",
        [
            {"speaker_id": "mark", "text_en": "Good afternoon! Are you interested in enrolling in our autumn adult education classes?", "text_tr": "Tünaydın! Sonbahar yetişkin eğitimi sınıflarımıza kaydolmakla ilgileniyor musunuz?"},
            {"speaker_id": "zeynep", "text_en": "Yes, I would like to join an intermediate English conversation class that meets after work hours.", "text_tr": "Evet, mesai saatlerinden sonra toplanan orta düzey bir İngilizce konuşma sınıfına katılmak istiyorum."},
            {"speaker_id": "mark", "text_en": "We have an excellent conversational class on Tuesday and Thursday evenings from six thirty to eight thirty.", "text_tr": "Salı ve perşembe akşamları altı buçuktan sekiz buçuğa kadar mükemmel bir konuşma sınıfımız var."},
            {"speaker_id": "zeynep", "text_en": "That schedule fits my working hours perfectly. How many weeks does the course last?", "text_tr": "Bu program çalışma saatlerime tam olarak uyuyor. Kurs kaç hafta sürüyor?"},
            {"speaker_id": "mark", "text_en": "The term runs for ten weeks, starting on the fifteenth of next month. Total tuition is two hundred dollars.", "text_tr": "Dönem gelecek ayın on beşinde başlayarak on hafta sürüyor. Toplam eğitim ücreti iki yüz dolardır."},
            {"speaker_id": "zeynep", "text_en": "Do I need to take a placement assessment test before my registration is confirmed?", "text_tr": "Kaydım onaylanmadan önce seviye belirleme sınavına girmem gerekiyor mu?"},
            {"speaker_id": "mark", "text_en": "Yes, we have a short fifteen-minute online test to ensure the class level is right for you.", "text_tr": "Evet, sınıf seviyesinin sizin için uygun olduğundan emin olmak için on beş dakikalık kısa bir çevrimiçi testimiz var."},
            {"speaker_id": "zeynep", "text_en": "Sounds great. I will complete the online placement test tonight on my computer.", "text_tr": "Kulağa harika geliyor. Çevrimiçi seviye belirleme sınavını bu gece bilgisayarımda tamamlayacağım."},
        ],
        [
            {
                "question_en": "What type of course is Zeynep looking for?",
                "question_tr_hint": "Zeynep ne tür bir kurs arıyor?",
                "correct_answer": "An intermediate English conversation class meeting after work hours.",
                "distractors": [
                    "A full-time morning intensive medical degree.",
                    "A weekend beginner French cooking workshop.",
                    "An online advanced computer programming course."
                ],
                "explanation_en": "Zeynep states she wants an intermediate English conversation class after work hours.",
                "explanation_tr": "Zeynep iş saatlerinden sonra orta düzey bir konuşma sınıfı istediğini belirtir."
            },
            {
                "question_en": "On which days and times does the course meet?",
                "question_tr_hint": "Kurs hangi günlerde ve saatlerde toplanıyor?",
                "correct_answer": "Tuesday and Thursday evenings from 6:30 to 8:30 PM.",
                "distractors": [
                    "Monday and Wednesday mornings from 9:00 to 11:00 AM.",
                    "Saturday afternoons from 1:00 to 5:00 PM.",
                    "Every day during lunch hour."
                ],
                "explanation_en": "Mark confirms Tuesday and Thursday evenings from 6:30 to 8:30 PM.",
                "explanation_tr": "Mark salı ve perşembe akşamları 18:30 ile 20:30 arasında olduğunu onaylar."
            },
            {
                "question_en": "How long is the course duration and what is the tuition?",
                "question_tr_hint": "Kurs süresi ne kadardır ve eğitim ücreti nedir?",
                "correct_answer": "Ten weeks for two hundred dollars.",
                "distractors": [
                    "Three weeks for five hundred dollars.",
                    "One full year for fifty dollars.",
                    "Six months free of charge."
                ],
                "explanation_en": "Mark states the term runs for 10 weeks and tuition is $200.",
                "explanation_tr": "Mark dönemin 10 hafta olduğunu ve ücretin 200 dolar olduğunu söyler."
            },
            {
                "question_en": "What must Zeynep complete before registration is confirmed?",
                "question_tr_hint": "Kaydı onaylanmadan önce Zeynep neyi tamamlamalıdır?",
                "correct_answer": "A short fifteen-minute online placement assessment test.",
                "distractors": [
                    "A formal three-hour written grammar dissertation.",
                    "A medical physical examination at a clinic.",
                    "An in-person interview with the university president."
                ],
                "explanation_en": "Mark explains she needs a short 15-minute online placement test.",
                "explanation_tr": "Mark 15 dakikalık kısa bir çevrimiçi seviye belirleme sınavı gerektiğini açıklar."
            },
            {
                "question_en": "When does Zeynep plan to take the online placement test?",
                "question_tr_hint": "Zeynep çevrimiçi seviye belirleme sınavına ne zaman girmeyi planlıyor?",
                "correct_answer": "Tonight on her computer.",
                "distractors": [
                    "Next month after classes begin.",
                    "Tomorrow morning at the university campus.",
                    "In two weeks after returning from vacation."
                ],
                "explanation_en": "Zeynep says she will complete the online test tonight on her computer.",
                "explanation_tr": "Zeynep çevrimiçi testi bu gece bilgisayarında tamamlayacağını söyler."
            }
        ],
        ["education", "registration", "language-learning", "college"]
    )
]
